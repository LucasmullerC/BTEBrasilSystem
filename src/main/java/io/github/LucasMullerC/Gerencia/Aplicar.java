package io.github.LucasMullerC.Gerencia;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaPendentes;
import io.github.LucasMullerC.Util.ListaZonas;

public class Aplicar {
    public ListaAplicar aplicante;
    public ListaPendentes pendente;
    public ListaZonas zonas;
    public Aplicantes A;
    public Pendentes P;
    public Zonas Zn;

    public Aplicar() {
        // Obter arquivos
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        aplicante = new ListaAplicar(new File(pluginFolder + File.separator + "aplicantes.txt"));
        pendente = new ListaPendentes(new File(pluginFolder + File.separator + "pendente.txt"));
        zonas = new ListaZonas(new File(pluginFolder + File.separator + "zonas.txt"));
        aplicante.load();
        pendente.load();
        zonas.load();

    }

    // APLICANTE
    public Aplicantes addAplicante(String UUID, String Discord, String Zona) {
        if (getAplicantePos(UUID) != null) {
            return getAplicantePos(UUID);
        } else {
            A = new Aplicantes(UUID);
            A.setDiscord(Discord);
            A.setSeccao("a");
            A.setZona(Zona);
            LocalDate deadline = LocalDate.now().plusDays(3); // x = 10
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
            String data = deadline.format(formatter);
            A.setDeadline(data);
            aplicante.add(A);
            // Salva a lista
            aplicante.save();
            return A;
        }
    }

    public void RemoverAplicante(String UUID) {
        A = getAplicantePos(UUID);
        aplicante.remove(A);
        aplicante.save();
    }

    public void DeletarDeadLines(World w) {
        ArrayList<Aplicantes> ParaRemover = new ArrayList<Aplicantes>();
        // get data
        LocalDate deadline = LocalDate.now(); // x = 10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String data = deadline.format(formatter);
        // WorldGuard
        // World w = Bukkit.getServer().getWorld("TerraPreGenerated");
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        int cont = 0;
        Regioes regioes = new Regioes();
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getDeadLine() != null && d.getDeadLine().contains(data)) {
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    Zn = getZona(d.getZona());
                    // Remove regiões
                    regions.removeRegion("apply" + d.getZona() + "d");
                    regions.removeRegion("apply" + d.getZona() + "c");
                    regions.removeRegion("apply" + d.getZona() + "b");
                    regions.removeRegion("apply" + d.getZona() + "a");
                    // Remove Listas
                    ParaRemover.add(d);
                    // GerenciarListas.RemoverAplicante(d.getUUID());
                    RemoverZona(Zn);
                    if (getPendentebyNameAplicacao(d.getUUID()) != null) {
                        RemoverPendenteAplicacao(d.getUUID());
                    }
                    // Remover Zona
                    regioes.removeRegion(w, Zn);
                    // DiscordPonte.sendMessage(d.getDiscord(), Mensagens.TimesUp);
                    cont++;
                }
            }
        }
        if (cont > 0) {
            for (Aplicantes b : ParaRemover) {
                RemoverAplicante(b.getUUID());
            }
            DiscordPonte.TimesUpMsg(cont);
        }
    }

    public Aplicantes getAplicante(String UUID) {
        return getAplicantePos(UUID);
    }

    private Aplicantes getAplicantePos(String search) {
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public Boolean AplicacaoIsNull() {
        // get data
        LocalDate deadline = LocalDate.now(); // x = 10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String data = deadline.format(formatter);
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getUUID() != null && d.getDeadLine().contains(data)) {
                return false;
            }
        }
        return true;
    }

    // PENDENTE APLICACAO
    public void addPendenteAplicacao(String UUID) {
        A = getAplicantePos(UUID);
        P = new Pendentes(UUID);
        P.setArea("nulo");
        P.setApp(true);
        P.setBuilds("0");
        pendente.add(P);
        pendente.save();
    }

    public void RemoverPendenteAplicacao(String UUID) {
        P = getPendentebyNameAplicacao(UUID);
        pendente.remove(P);
        pendente.save();
    }

    public Pendentes getPendenteAplicacao() {
        for (Pendentes d : pendente.getValues()) {
            return d;
        }
        return null;
    }

    public int getPendenteAplicacaoQtd() {
        int cont = 0;
        for (Pendentes d : pendente.getValues()) {
            if (d.getUUID() != null) {
                cont++;
            }
        }
        return cont;
    }

    public Pendentes getPendentebyNameAplicacao(String UUID) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(UUID)) {
                if (d.getApp() == true) {
                    return d;
                }
            }
        }
        return null;
    }

    public Boolean PendenteAplicacaoIsNull() {
        for (Pendentes d : pendente.getValues()) {
            if (d.getUUID() != null && d.getApp() == true) {
                return false;
            }
        }
        return true;
    }

    // ZONAS
    public void addZonas(Player player, String Time, String Discord) {
        World w = player.getWorld();
        ArrayList<Zonas> z = zonas.getValues();
        Zonas zonaold;
        Zonas zn;
        Location L, lb, lc, ld, meio;
        Integer result = 1;
        Integer pos;
        Regioes regioes = new Regioes();
        if (!z.isEmpty()) {
            result = EncontrarZonaF(z);
        }
        if (z.isEmpty() == true || result == 1) {
            zn = new Zonas("1");
            L = new Location(w, -648, 31, 289);
            lb = new Location(w, -653, 31, 289);
            lc = new Location(w, -653, 38, 284);
            ld = new Location(w, -648, 31, 284);
            meio = new Location(w, -650, 45, 287);
            // worldguard
            regioes.CreateRegion(1, player, false);
            regioes.loadSchematic(player, meio);
        } else {
            if (result == 0) {
                Integer posterior = z.size();
                pos = z.size() + 1;
                zonaold = getZonaPos(posterior.toString());
                zn = new Zonas(String.valueOf(pos));
            } else {
                Integer posterior = result - 1;
                zonaold = getZonaPos(posterior.toString());
                pos = result;
                zn = new Zonas(String.valueOf(pos));
            }
            if (pos == 6 || pos == 11 || pos == 16 || pos == 21 || pos == 26 || pos == 31 || pos == 36) {
                Integer posterior = pos - 5;
                zonaold = getZonaPos(posterior.toString());
                L = new Location(w, zonaold.getla().getX() + 90, zonaold.getla().getY(), zonaold.getla().getZ());
                lb = new Location(w, zonaold.getlb().getX() + 90, zonaold.getlb().getY(), zonaold.getlb().getZ());
                lc = new Location(w, zonaold.getlc().getX() + 90, zonaold.getlc().getY(), zonaold.getlc().getZ());
                ld = new Location(w, zonaold.getld().getX() + 90, zonaold.getld().getY(), zonaold.getld().getZ());
                // worldguard
                regioes.CreateRegion(pos, player, true);
            } else {
                // Coordenadas
                L = new Location(w, zonaold.getla().getX(), zonaold.getla().getY(), zonaold.getla().getZ() + 90);
                lb = new Location(w, zonaold.getlb().getX(), zonaold.getlb().getY(), zonaold.getlb().getZ() + 90);
                lc = new Location(w, zonaold.getlc().getX(), zonaold.getlc().getY(), zonaold.getlc().getZ() + 90);
                ld = new Location(w, zonaold.getld().getX(), zonaold.getld().getY(), zonaold.getld().getZ() + 90);
                // worldguard
                regioes.CreateRegion(pos, player, false);
            }
            meio = new Location(w, L.getX() - 2, 45, L.getZ() - 2);
            regioes.loadSchematic(player, meio);
        }
        zn.setNome(player.getUniqueId().toString());
        zn.setOcupado(true);
        zn.seta(true);
        zn.setb(false);
        zn.setc(false);
        zn.setd(false);
        // locs
        zn.setla(L);
        zn.setlb(lb);
        zn.setlc(lc);
        zn.setld(ld);
        zonas.add(zn);
        zonas.save();
        regioes.AddPermissao(player, zn.getZona());
        addAplicante(player.getUniqueId().toString(), Discord, zn.getZona());
        player.teleport(L);
    }

    public void RemoverZona(Zonas z) {
        zonas.remove(z);
        zonas.save();
    }

    public Integer EncontrarZonaF(ArrayList<Zonas> z) {
        String cont;
        Integer result;
        for (int i = 0; i < z.size(); i++) {
            result = i + 1;
            cont = result.toString();
            if (getZonaPos(cont) == null) {
                return result;
            }
        }
        return 0;
    }

    private Zonas getZonaPos(String search) {
        for (Zonas d : zonas.getValues()) {
            if (d.getZona() != null && d.getZona().contains(search)) {
                if (d.getZona().equals(search)) {
                    return d;
                }

            }
        }
        return null;
    }

    public Zonas getZona(String zon) {
        return getZonaPos(zon);
    }

}
