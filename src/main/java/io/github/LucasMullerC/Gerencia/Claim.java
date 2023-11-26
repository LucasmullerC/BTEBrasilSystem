package io.github.LucasMullerC.Gerencia;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Util.ListaAreas;
import io.github.LucasMullerC.Util.ListaPendentes;

public class Claim {
    public ListaPendentes pendente;
    public ListaAreas areas;
    public Areas Ar;
    public Pendentes P;

    public Claim() {
        // Obter arquivos
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        areas = new ListaAreas(new File(pluginFolder + File.separator + "areas.txt"));
        pendente = new ListaPendentes(new File(pluginFolder + File.separator + "pendente.txt"));
        areas.load();
        pendente.load();
    }

    public void saveClaim() {
        areas.save();
        pendente.save();
    }

    // PENDENTES CLAIMS
    public void addPendenteClaim(String Area, String builds) {
        Ar = getAreaPos(Area);
        P = new Pendentes(Ar.getPlayer());
        P.setArea(Area);
        P.setApp(false);
        P.setBuilds(builds);
        pendente.add(P);
        pendente.save();
    }

    public Pendentes getPendenteClaim(String Area) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getArea() != null && d.getArea().contains(Area)) {
                return d;
            }
        }
        return null;
    }

    public void RemoverPendenteClaim(String ID) {
        P = getPendenteClaim(ID);
        pendente.remove(P);
        pendente.save();
    }

    public Pendentes getPendenteClaimAnalisar(String UUID) {
        for (Pendentes d : pendente.getValues()) {
            if (d.getArea() != null && !d.getUUID().equals(UUID)) {
                if (!d.getArea().equals("nulo")) {
                    return d;
                }
            }
        }
        return null;
    }

    // AREAS
    public Boolean addArea(String Nome, String id, String Pontos, String player) {
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

    public void setBuilds(String id, int qtd) {
        Ar = getAreaPos(id);
        int builds = Ar.getBuilds();
        Ar.setBuilds(qtd + builds);
        areas.save();
    }

    public void setImg(String id, String link) {
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

    public void setNome(String id, String nome) {
        Ar = getAreaPos(id);
        Ar.setNome(nome);
        areas.save();
    }

    public void setParticipante(String id, String uuid) {
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

    public void unsetParticipante(String id, String uuid) {
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

    public void unsetImg(String id, String link) {
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

    public Areas getArea(String Claim) {
        return getAreaPos(Claim);
    }

    private Areas getAreaPos(String search) {
        for (Areas d : areas.getValues()) {
            if (d.getClaim() != null && d.getClaim().contains(search)) {
                if (d.getClaim().equals(search)) {
                    return d;
                }
            }
        }
        return null;
    }

    public void setFlags() {
        Regioes regioes = new Regioes();
        for (Areas d : areas.getValues()) {
            regioes.AddFlags(d.getClaim());
        }
    }

    public void setPerms(Player p) {
        Regioes regioes = new Regioes();
        for (Areas d : areas.getValues()) {

            if (d.getParticipantes().equals("nulo")) {
                regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(d.getPlayer()));
            } else {
                regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(d.getPlayer()));
                String[] Parts = d.getParticipantes().split(",");
                for (int i = 0; i < Parts.length; i++) {
                    regioes.addPermissaoWG(d.getClaim(), p, UUID.fromString(Parts[i]));
                }
            }
        }
    }

    public Boolean RemoverArea(String ID) {
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            areas.remove(Ar);
            areas.save();
            return true;
        } else {
            return false;
        }
    }

    public void CompletarArea(String ID) {
        Regioes regioes = new Regioes();
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            Ar.setStatus("T");
            areas.save();
            regioes.AddFlags(ID);
        }
    }

    public void EditarArea(String ID) {
        Regioes regioes = new Regioes();
        if (getAreaPos(ID) != null) {
            Ar = getAreaPos(ID);
            Ar.setStatus("F");
            areas.save();
            regioes.AddFlags(ID);
        }
    }

    public boolean getAreaQtdByPlayer(String search, int Limite) {
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

    public int getAreaQtdByPlayerNum(String search) {
        int cont = 0;
        for (Areas d : areas.getValues()) {
            if (d.getPlayer() != null && d.getPlayer().contains(search) && d.getStatus().equals("F")) {
                cont++;
            }
        }
        return cont;
    }

    public ArrayList<Areas> getAreaNotCompletedByPlayerUuid(String playerUuid) {
        ArrayList<Areas> notCompletedAreas = new ArrayList<>();
        for (Areas area : areas.getValues()) {
            if (area.getPlayer() != null && area.getPlayer().contains(playerUuid) && area.getStatus().equals("F")) {
                notCompletedAreas.add(area);
            }
        }
        return notCompletedAreas;
    }

    public ArrayList<Areas> getAreaCompletedByPlayerUuid(String playerUuid) {
        ArrayList<Areas> completedAreas = new ArrayList<>();
        for (Areas area : areas.getValues()) {
            if (area.getPlayer() != null && area.getPlayer().contains(playerUuid) && area.getStatus().equals("T")) {
                completedAreas.add(area);
            }
        }
        return completedAreas;
    }

    public int getAreaCompletaQtdByPlayerNum(String search) {
        int cont = 0;
        for (Areas d : areas.getValues()) {
            if (d.getPlayer() != null && d.getPlayer().contains(search) && d.getStatus().equals("T")) {
                cont++;
            }
        }
        return cont;
    }
}
