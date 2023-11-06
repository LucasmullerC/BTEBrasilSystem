package io.github.LucasMullerC.Gerencia;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.GerarMapa;
import io.github.LucasMullerC.Util.ListaBuilders;
import io.github.LucasMullerC.Util.ListaConquistas;

public class Builder {
    public ListaBuilders builder;
    public GerarMapa mapa;
    public ListaConquistas conquista;
    public Builders B;
    public Conquistas C;

    public Builder() {
        // Obter arquivos
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        builder = new ListaBuilders(new File(pluginFolder + File.separator + "builders.txt"));
        mapa = new GerarMapa(new File(pluginFolder + File.separator + "claims.kml"));
        conquista = new ListaConquistas(new File(pluginFolder + File.separator + "conquistas.txt"));
        builder.load();
        conquista.load();

    }

    // BUILDERS
    public Boolean addBuilder(String UUID, String Discord) {
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
            } else if (getBuilderPos(UUID).getDiscord().equals("nulo")
                    || getBuilderPos(UUID).getDiscord().equals("null")) {
                B = getBuilderPos(UUID);
                B.setDiscord(Discord);
                builder.save();
                return true;
            }
        }
        return false;
    }

    public void saveBuilder() {
        builder.save();
    }

    public void setBuildsBuilder(String UUID, int qtd) {
        B = getBuilderPos(UUID);
        Integer builds = B.getBuilds();
        B.setBuilds(qtd + builds);
        builder.save();
    }

    public void removeBuildsBuilder(String UUID, int qtd) {
        B = getBuilderPos(UUID);
        Integer builds = B.getBuilds();
        B.setBuilds(builds - qtd);
        builder.save();
    }

    public void setPontos(String UUID, double qtd) {
        B = getBuilderPos(UUID);
        double pontos = B.getPontos();
        B.setPontos(qtd + pontos);
        builder.save();
    }

    public void removePontos(String UUID, double qtd) {
        B = getBuilderPos(UUID);
        double pontos = B.getPontos();
        B.setPontos(pontos - qtd);
        builder.save();
    }

    public void setTier(int Tier, String UUID) {
        B = getBuilderPos(UUID);
        B.setTier(Tier);
        builder.save();
    }

    public void setDestaque(String Desc, String UUID) {
        B = getBuilderPos(UUID);
        B.setDestaque(Desc);
        builder.save();
    }

    public void setConquistas(String idc, String UUID) {
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

    public Builders getBuilder(String UUID) {
        return getBuilderPos(UUID);
    }

    private Builders getBuilderPos(String search) {
        for (Builders d : builder.getValues()) {
            if (d.getUUID() != null && d.getUUID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public Builders getBuilderDiscord(String search) {
        for (Builders d : builder.getValues()) {
            if (d.getDiscord() != null && d.getDiscord().contains(search)) {
                return d;
            }
        }
        return null;
    }

    public Map<String, Double> GetPointsMap() {
        Map<String, Double> unsortedMap = new HashMap<String, Double>();
        for (Builders d : builder.getValues()) {
            unsortedMap.put(d.getDiscord(), d.getPontos());
        }
        return unsortedMap;
    }

    // CONQUISTAS
    public void addConquista(String id, Double Pontos, String url, String Nome) {
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

    public Conquistas getConquistaPos(String search) {
        for (Conquistas d : conquista.getValues()) {
            if (d.getID() != null && d.getID().contains(search)) {
                return d;
            }
        }
        return null;
    }

    // MAPA
    public void MontarMapa() {
        mapa.save();
    }
}
