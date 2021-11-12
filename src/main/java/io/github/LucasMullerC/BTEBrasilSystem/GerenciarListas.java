package io.github.LucasMullerC.BTEBrasilSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaJogadores;
import io.github.LucasMullerC.Util.ListaZonas;
import io.github.LucasMullerC.Util.Mensagens;

public class GerenciarListas {
    public static ListaAplicar aplicante;
    public static ListaAplicar pendente;
    public static ListaZonas zonas;
    public static ListaJogadores equipe;
    static Aplicantes A;
    static Jogadores J;
    static Zonas Zn;

    // APLICANTE
    public static Aplicantes addAplicante(String UUID, String Time, String Discord, String Zona) {
        if (getAplicantePos(UUID) != null) {
            return getAplicantePos(UUID);
        } else {
            A = new Aplicantes(UUID);
            A.setDiscord(Discord);
            A.setTime(Time);
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

    public static void RemoverAplicante(String UUID) {
        A = getAplicantePos(UUID);
        aplicante.remove(A);
        aplicante.save();
    }

    public static void DeletarDeadLines() {
        Aplicantes AP;
        // get data
        LocalDate deadline = LocalDate.now(); // x = 10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String data = deadline.format(formatter);
        // WorldGuard
        World w = Bukkit.getWorld("TerraPreGenerated");
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        int cont = 0;
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getDeadLine() != null && d.getDeadLine().contains(data)) {
                Zn = GerenciarListas.getZona(d.getZona());
                // Remove regiões
                regions.removeRegion("apply" + d.getZona() + "d");
                regions.removeRegion("apply" + d.getZona() + "c");
                regions.removeRegion("apply" + d.getZona() + "b");
                regions.removeRegion("apply" + d.getZona() + "a");
                // Remove Listas
                GerenciarListas.RemoverAplicante(d.getUUID());
                GerenciarListas.RemoverZona(Zn);
                if (GerenciarListas.getPendentebyName(d.getUUID()) != null) {
                    AP = GerenciarListas.getPendentebyName(d.getUUID());
                    GerenciarListas.RemoverPendente(AP);
                }
                // Remover Zona
                Sistemas.removeRegion(w, Zn);
                DiscordPonte.sendMessage(d.getDiscord(), Mensagens.TimesUp);
                cont++;
            }
        }
        if (cont > 0) {
            DiscordPonte.TimesUpMsg(cont);
        }
    }

    public static Aplicantes getAplicante(String UUID) {
        return getAplicantePos(UUID);
    }

    private static Aplicantes getAplicantePos(String search) {
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    // ZONAS
    public static void addZonas(Player player, String Time, String Discord) {
        World w = player.getWorld();
        ArrayList<Zonas> z = zonas.getValues();
        Zonas zonaold;
        Zonas zn;
        Location L, lb, lc, ld, meio;
        Integer result = 1;
        Integer pos;
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
            Regioes.CreateRegion(1, player, false);
            Sistemas.loadSchematic(player, meio);
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
                L = new Location(w, zonaold.getla().getX() - 90, zonaold.getla().getY(), zonaold.getla().getZ());
                lb = new Location(w, zonaold.getlb().getX() - 90, zonaold.getlb().getY(), zonaold.getlb().getZ());
                lc = new Location(w, zonaold.getlc().getX() - 90, zonaold.getlc().getY(), zonaold.getlc().getZ());
                ld = new Location(w, zonaold.getld().getX() - 90, zonaold.getld().getY(), zonaold.getld().getZ());
                // worldguard
                Regioes.CreateRegion(pos, player, true);
            } else {
                // Coordenadas
                L = new Location(w, zonaold.getla().getX(), zonaold.getla().getY(), zonaold.getla().getZ() + 90);
                lb = new Location(w, zonaold.getlb().getX(), zonaold.getlb().getY(), zonaold.getlb().getZ() + 90);
                lc = new Location(w, zonaold.getlc().getX(), zonaold.getlc().getY(), zonaold.getlc().getZ() + 90);
                ld = new Location(w, zonaold.getld().getX(), zonaold.getld().getY(), zonaold.getld().getZ() + 90);
                // worldguard
                Regioes.CreateRegion(pos, player, false);
            }
            meio = new Location(w, L.getX() - 2, 45, L.getZ() - 2);
            Sistemas.loadSchematic(player, meio);
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
        Sistemas.AddPermissao(player, zn.getZona());
        addAplicante(player.getUniqueId().toString(), Time, Discord,zn.getZona());
        player.teleport(L);
    }

    public static void RemoverZona(Zonas z) {
        zonas.remove(z);
        zonas.save();
    }

    public static Integer EncontrarZonaF(ArrayList<Zonas> z) {
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

    private static Zonas getZonaPos(String search) {
        for (Zonas d : zonas.getValues()) {
            if (d.getZona() != null && d.getZona().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public static Zonas getZona(String zon) {
        return getZonaPos(zon);
    }

    // PENDENTE
    public static void addPendente(String UUID) {
        A = getAplicantePos(UUID);
        pendente.add(A);
        pendente.save();
    }

    public static void RemoverPendente(Aplicantes p) {
        pendente.remove(p);
        pendente.save();
    }

    public static Aplicantes getPendente(String time) {
        for (Aplicantes d : pendente.getValues()) {
            if (d.getTime() != null && d.getTime().contains(time)) {
                return d;
            }
        }
        return null;
    }

    public static Aplicantes getPendentebyName(String time) {
        for (Aplicantes d : pendente.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(time)) {
                return d;
            }
        }
        return null;
    }

    // EQUIPE
    public static Boolean addEquipe(String UUID, String Time) {
        if (equipe.getValues().isEmpty() == true) {
            J = new Jogadores(UUID);
            J.setDiscord("nulo");
            J.setTime(Time);
            equipe.add(J);
            return true;
        } else {
            if (getEquipePos(UUID) != null) {
                String T;
                J = getEquipePos(UUID);
                T = J.getTime();
                if (T.equals(Time)) {
                    return false;
                } else {
                    T = T + "," + Time;
                    J.setTime(T);
                    return true;
                }

            } else {
                J = new Jogadores(UUID);
                J.setDiscord("nulo");
                J.setTime(Time);
                equipe.add(J);
                return true;
            }
        }
    }

    public static Boolean RemoverEquipe(String UUID) {
        if (getEquipePos(UUID) != null) {
            J = getEquipePos(UUID);
            equipe.remove(J);
            return true;
        } else {
            return false;
        }
    }

    public static Jogadores getEquipe(String UUID) {
        return getEquipePos(UUID);
    }

    private static Jogadores getEquipePos(String search) {
        for (Jogadores d : equipe.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(search)) {
                return d;
            }
        }
        return null;
    }
}
