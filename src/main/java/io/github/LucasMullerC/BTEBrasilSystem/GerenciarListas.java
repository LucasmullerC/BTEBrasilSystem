package io.github.LucasMullerC.BTEBrasilSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.GerarMapa;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaAreas;
import io.github.LucasMullerC.Util.ListaBuilders;
import io.github.LucasMullerC.Util.ListaConquistas;
import io.github.LucasMullerC.Util.ListaPendentes;
import io.github.LucasMullerC.Util.ListaZonas;

public class GerenciarListas {
    public static ListaAplicar aplicante;
    public static ListaPendentes pendente;
    public static ListaZonas zonas;
    public static ListaBuilders builder;
    public static ListaAreas areas;
    public static GerarMapa mapa;
    public static ListaConquistas conquista;
    static Areas Ar;
    static Builders B;
    static Aplicantes A;
    static Pendentes P;
    static Zonas Zn;
    static Conquistas C;

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

    public static void DeletarDeadLines(World w) {
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
        for (Aplicantes d : aplicante.getValues()) {
            if (d.getDeadLine() != null && d.getDeadLine().contains(data)) {
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    Zn = GerenciarListas.getZona(d.getZona());
                    // Remove regiões
                    regions.removeRegion("apply" + d.getZona() + "d");
                    regions.removeRegion("apply" + d.getZona() + "c");
                    regions.removeRegion("apply" + d.getZona() + "b");
                    regions.removeRegion("apply" + d.getZona() + "a");
                    // Remove Listas
                    ParaRemover.add(d);
                    // GerenciarListas.RemoverAplicante(d.getUUID());
                    GerenciarListas.RemoverZona(Zn);
                    if (GerenciarListas.getPendentebyNameAplicacao(d.getUUID()) != null) {
                        GerenciarListas.RemoverPendenteAplicacao(d.getUUID());
                    }
                    // Remover Zona
                    Regioes.removeRegion(w, Zn);
                    // DiscordPonte.sendMessage(d.getDiscord(), Mensagens.TimesUp);
                    cont++;
                }
            }
        }
        if (cont > 0) {
            for (Aplicantes b : ParaRemover) {
                GerenciarListas.RemoverAplicante(b.getUUID());
            }
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

    public static Boolean AplicacaoIsNull() {
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
            Regioes.loadSchematic(player, meio);
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
            Regioes.loadSchematic(player, meio);
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
        Regioes.AddPermissao(player, zn.getZona());
        addAplicante(player.getUniqueId().toString(), Time, Discord, zn.getZona());
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
                if (d.getZona().equals(search)) {
                    return d;
                }

            }
        }
        return null;
    }

    public static Zonas getZona(String zon) {
        return getZonaPos(zon);
    }

    // PENDENTE APLICACAO
    public static void addPendenteAplicacao(String UUID) {
        A = getAplicantePos(UUID);
        P = new Pendentes(UUID);
        P.setArea("nulo");
        P.setTime(A.getTime());
        P.setApp(true);
        P.setBuilds("0");
        pendente.add(P);
        pendente.save();
    }

    public static void RemoverPendenteAplicacao(String UUID) {
        P = getPendentebyNameAplicacao(UUID);
        pendente.remove(P);
        pendente.save();
    }

    public static Pendentes getPendenteAplicacao(String time) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getTime() != null && d.getTime().equals(time)) {
                return d;
            }
        }
        return null;
    }

    public static Pendentes getPendentebyNameAplicacao(String UUID) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(UUID)) {
                if (d.getApp() == true) {
                    return d;
                }
            }
        }
        return null;
    }

    public static Boolean PendenteAplicacaoIsNull() {
        for (Pendentes d : pendente.getValues()) {
            if (d.getUUID() != null && d.getApp() == true) {
                return false;
            }
        }
        return true;
    }

    // PENDENTES CLAIMS
    public static void addPendenteClaim(String Area, String builds) {
        Ar = getAreaPos(Area);
        P = new Pendentes(Ar.getPlayer());
        P.setArea(Area);
        P.setTime("nulo");
        P.setApp(false);
        P.setBuilds(builds);
        pendente.add(P);
        pendente.save();
    }

    public static Pendentes getPendenteClaim(String Area) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getArea() != null && d.getArea().contains(Area)) {
                return d;
            }
        }
        return null;
    }

    public static void RemoverPendenteClaim(String ID) {
        P = getPendenteClaim(ID);
        pendente.remove(P);
        pendente.save();
    }

    public static Pendentes getPendenteClaimAnalisar(String UUID) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getArea() != null && !d.getUUID().equals(UUID)) {
                if (d.getArea() != "nulo") {
                    return d;
                }
            }
        }
        return null;
    }

    // BUILDERS
    public static Boolean addBuilder(String UUID, String Discord) {
        if (builder.getValues().isEmpty() == true) {
            B = new Builders(UUID);
            B.setDiscord(Discord);
            B.setTier(1);
            B.setAwards("nulo");
            B.setBuilds(0);
            B.setPontos(0);
            B.setDestaque("nulo");
            builder.add(B);
            builder.save();
            return true;
        } else {
            if (getBuilderPos(UUID) == null) {
                B = new Builders(UUID);
                B.setDiscord(Discord);
                B.setTier(1);
                B.setAwards("nulo");
                B.setBuilds(0);
                B.setPontos(0);
                B.setDestaque("nulo");
                builder.add(B);
                builder.save();
                return true;
            } else if (getBuilderPos(UUID).getDiscord().equals("nulo")) {
                B = getBuilderPos(UUID);
                B.setDiscord(Discord);
                builder.save();
                return true;
            }
        }
        return false;
    }

    public static void setBuildsBuilder(String UUID, int qtd) {
        B = getBuilderPos(UUID);
        Integer builds = B.getBuilds();
        B.setBuilds(qtd + builds);
        builder.save();
    }

    public static void setPontos(String UUID, double qtd) {
        B = getBuilderPos(UUID);
        double pontos = B.getPontos();
        B.setPontos(qtd + pontos);
        builder.save();
    }

    public static void setTier(int Tier, String UUID) {
        B = getBuilderPos(UUID);
        B.setTier(Tier);
        builder.save();
    }

    public static void setDestaque(String Desc, String UUID) {
        B = getBuilderPos(UUID);
        B.setDestaque(Desc);
        builder.save();
    }

    public static void setConquistas(String idc, String UUID) {
        B = getBuilderPos(UUID);
        String aw = B.getAwards();
        // Verifica se ele já tem essa Award
        String[] allaw = aw.split(",");
        boolean vef = false;
        for (int i = 0; i < allaw.length; i++) {
            if (allaw[i].equals(idc)) {
                vef = true;
                break;
            }
        }
        // Se vef for False add a award e notifica
        if (vef == false) {
            C = getConquistaPos(idc);
            if (aw.equals("nulo")) {
                B.setAwards(idc);
            } else {
                aw = aw + "," + idc;
                B.setAwards(aw);
            }
            setPontos(UUID, C.getPontos());
            DiscordPonte.Awards(C, B.getDiscord(), UUID);
            builder.save();
        }
    }

    public static Builders getBuilder(String UUID) {
        return getBuilderPos(UUID);
    }

    private static Builders getBuilderPos(String search) {
        for (Builders d : builder.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public static Builders getBuilderDiscord(String search) {
        for (Builders d : builder.getValues()) {
            if (d.getDiscord() != null && d.getDiscord().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public static Map<String, Double> GetPointsMap() {
        Map<String, Double> unsortedMap = new HashMap<String, Double>();
        for (Builders d : builder.getValues()) {
            unsortedMap.put(d.getDiscord(), d.getPontos());
        }
        return unsortedMap;
    }

    // AREAS
    public static Boolean addArea(String Nome, String id, String Pontos, String player) {
        if (areas.getValues().isEmpty() == true) {
            Ar = new Areas(id);
            Ar.setNome(Nome);
            Ar.setPontos(Pontos);
            Ar.setPlayer(player);
            Ar.setImgs("nulo");
            Ar.setStatus("F");
            Ar.setParticipantes("nulo");
            Ar.setBuilds(0);
            areas.add(Ar);
            areas.save();
            return true;
        } else {
            Ar = new Areas(id);
            Ar.setNome(Nome);
            Ar.setPontos(Pontos);
            Ar.setPlayer(player);
            Ar.setImgs("nulo");
            Ar.setStatus("F");
            Ar.setParticipantes("nulo");
            Ar.setBuilds(0);
            areas.add(Ar);
            areas.save();
            return true;
        }
    }

    public static void setBuilds(String id, int qtd) {
        Ar = getAreaPos(id);
        int builds = Ar.getBuilds();
        Ar.setBuilds(qtd + builds);
        areas.save();
    }

    public static void setImg(String id, String link) {
        Ar = getAreaPos(id);
        String part = Ar.getImgs();
        if (part.equals("nulo")) {
            Ar.setImgs(link);
        } else {
            part += "," + link;
            Ar.setImgs(part);
        }
        areas.save();
    }

    public static void setNome(String id, String nome) {
        Ar = getAreaPos(id);
        Ar.setNome(nome);
        areas.save();
    }

    public static void setParticipante(String id, String uuid) {
        Ar = getAreaPos(id);
        String part = Ar.getParticipantes();
        if (part.equals("nulo")) {
            Ar.setParticipantes(uuid);
        } else {
            part += "," + uuid;
            Ar.setParticipantes(part);
        }
        areas.save();
    }

    public static void unsetParticipante(String id, String uuid) {
        Ar = getAreaPos(id);
        String part = Ar.getParticipantes();
        String[] div = part.split(",");
        String newPart = "nulo";
        for (int i = 0; i < div.length; i++) {
            if (!div[i].equals(uuid)) {
                if (!div[i].equals("nulo")) {
                    if (newPart.equals("nulo")) {
                        newPart = div[i];
                    } else {
                        newPart += "," + div[i];
                    }
                }
            }
        }
        Ar.setParticipantes(newPart);
        areas.save();
    }

    public static void unsetImg(String id, String link) {
        Ar = getAreaPos(id);
        String part = Ar.getImgs();
        String[] div = part.split(",");
        String newPart = "nulo";
        for (int i = 0; i < div.length; i++) {
            if (!div[i].equals(link)) {
                if (!div[i].equals("nulo")) {
                    if (newPart.equals("nulo")) {
                        newPart = div[i];
                    } else {
                        newPart += "," + div[i];
                    }
                }
            }
        }
        Ar.setImgs(newPart);
        areas.save();
    }

    public static Areas getArea(String Claim) {
        return getAreaPos(Claim);
    }

    private static Areas getAreaPos(String search) {
        for (Areas d : areas.getValues()) {
            if (d.getClaim() != null && d.getClaim().contains(search)) {
                if (d.getClaim().equals(search)) {
                    return d;
                }
            }
        }
        return null;
    }

    public static void setFlags() {
        for (Areas d : areas.getValues()) {
            Regioes.AddFlags(d.getClaim());
        }
    }
    public static void setPerms(Player p) {
        for (Areas d : areas.getValues()) {

            if (d.getParticipantes().equals("nulo")) {
                Regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(d.getPlayer()));
            }
            else{
                Regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(d.getPlayer()));
                String[] Parts = d.getParticipantes().split(",");
                for (int i = 0; i < Parts.length; i++) {
                    Regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(Parts[i]));
                }
            }
        }
    }

    public static Boolean RemoverArea(String ID) {
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            areas.remove(Ar);
            areas.save();
            return true;
        } else {
            return false;
        }
    }

    public static void CompletarArea(String ID) {
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            Ar.setStatus("T");
            areas.save();
            Regioes.AddFlags(ID);
        }
    }

    public static void EditarArea(String ID) {
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            Ar.setStatus("F");
            areas.save();
            Regioes.AddFlags(ID);
        }
    }

    public static boolean getAreaQtdByPlayer(String search, int Limite) {
        int cont = 0;
        for (Areas d : areas.getValues()) {
            if (d.getPlayer() != null && d.getPlayer().contains(search) && d.getStatus().equals("F")) {
                cont++;
            }
            if (cont == Limite) {
                return true;
            }
        }
        return false;
    }

    public static int getAreaQtdByPlayerNum(String search) {
        int cont = 0;
        for (Areas d : areas.getValues()) {
            if (d.getPlayer() != null && d.getPlayer().contains(search) && d.getStatus().equals("F")) {
                cont++;
            }
        }
        return cont;
    }

    public static int getAreaCompletaQtdByPlayerNum(String search) {
        int cont = 0;
        for (Areas d : areas.getValues()) {
            if (d.getPlayer() != null && d.getPlayer().contains(search) && d.getStatus().equals("T")) {
                cont++;
            }
        }
        return cont;
    }

    // CONQUISTAS
    public static void addConquista(String id, Double Pontos, String url, String Nome) {
        if (conquista.getValues().isEmpty() == true) {
            C = new Conquistas(id);
            C.setNome(Nome);
            C.setPontos(Pontos);
            C.setURL(url);
            conquista.add(C);
            conquista.save();
        } else {
            C = new Conquistas(id);
            C.setNome(Nome);
            C.setPontos(Pontos);
            C.setURL(url);
            conquista.add(C);
            conquista.save();
        }
    }

    public static Conquistas getConquistaPos(String search) {
        for (Conquistas d : conquista.getValues()) {
            if (d.getID() != null && d.getID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    // MAPA
    public static void MontarMapa() {
        mapa.save();
    }
}
