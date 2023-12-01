package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.noahhusby.sledgehammer.datasets.projection.GeographicProjection;
import com.noahhusby.sledgehammer.datasets.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.datasets.projection.ScaleProjection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.Mensagens;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.md_5.bungee.api.ChatColor;

public class Sistemas {
    private static final GeographicProjection projection;
    private static final GeographicProjection uprightProj;
    private static final ScaleProjection scaleProj;

    public String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, (double) x, (double) y, (double) z);
        }
        return null;
    }

    public void RemovePermissaoTimes(Player player) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        user.data().remove(Node.builder("group.b_ne").build());
        user.data().remove(Node.builder("group.b_sp").build());
        user.data().remove(Node.builder("group.b_es").build());
        user.data().remove(Node.builder("group.b_rj").build());
        user.data().remove(Node.builder("group.b_mg").build());
        user.data().remove(Node.builder("group.b_co").build());
        user.data().remove(Node.builder("group.b_sul").build());
        user.data().remove(Node.builder("group.b_br").build());
    }

    public Boolean VerificarBuilder(Player player) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        Set<String> groups = user.getNodes(NodeType.INHERITANCE).stream().map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());
        ArrayList<String> al = new ArrayList<String>();
        al.add("b_ne");
        al.add("b_sp");
        al.add("b_rj");
        al.add("b_mg");
        al.add("b_co");
        al.add("b_sul");
        for (String time : al) {
            if (groups.contains(time)) {
                return true;
            }
        }
        return false;
    }

    public String ForNextLvl(String uid, Boolean Prem) {
        Builder builder = new Builder();
        Builders B = builder.getBuilder(uid);
        int Tier = B.getTier();
        double NextLvl;
        NextLvl = (Tier * 150) * 2.25;
        /*
         * if (Prem == true) {
         * NextLvl = (Tier * 100) * 2.25;
         * } else {
         * NextLvl = (Tier * 150) * 2.25;
         * }
         */
        return String.valueOf(NextLvl);

    }

    public String FormLb(int position) {
        Builder builder = new Builder();
        Map<String, Double> unsortMap = builder.GetPointsMap();
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                    Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return printLb(sortedMap, position);
    }

    public <K, V> String printLb(Map<K, V> map, int position) {
        String lb = "";
        int cont = 1;
        Builder builder = new Builder();
        List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());

        position = Math.max(0, Math.min(position, entryList.size() - 1));
    
        for (int i = position; i < entryList.size(); i++) {
            Map.Entry<K, V> entry = entryList.get(i);
            Builders B = builder.getBuilderDiscord(entry.getKey().toString());
    
            if (entry.getKey().equals("nulo")) {
                OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(B.getUUID()));
                lb += "**#" + (i+1) + " ->** _" + pl.getName() + "_\r\n";
            } else {
                lb += "**#" + (i+1) + " ->** <@" + entry.getKey() + ">\r\n";
            }
    
            String pts = String.valueOf(B.getPontos());
            pts = pts.substring(0, pts.indexOf(".") + 2);
    
            lb += ":medal: Tier `" + B.getTier().toString() + "`\r\n :chart_with_upwards_trend: Pontos `"
                    + pts + "`\r\n\r\n";
    
            if (cont == 10) {
                break;
            }
            cont++;
        }
        return lb;
    }

    public String ListConquistas(Builders B) {
        String[] cq = B.getAwards().split(",");
        String Result = "";
        Builder builder = new Builder();
        if (B.getAwards().equals("nulo")) {
            return Mensagens.conquistaList;
        } else {
            for (int i = 0; i < cq.length; i++) {
                Conquistas C = builder.getConquistaPos(cq[i]);
                Result += "● [" + C.getNome() + "](" + C.getURL() + ")\r\n";
            }
            if (Result.equals("")) {
                return Mensagens.conquistaList;
            } else {
                Result += "**DESTAQUE**";
                return Result;
            }
        }
    }

    public void CheckRank(String id) {
        OfflinePlayer Offplayer = Bukkit.getOfflinePlayer(UUID.fromString(id));
        Builder builder = new Builder();
        if (Offplayer.isOnline()) {
            Player player = Offplayer.getPlayer();
            Builders B = builder.getBuilder(id);
            if (B != null) {
                int tier = B.getTier();
                double NextLvl;
                NextLvl = (tier * 150) * 2.25;
                /*
                 * if (player.hasPermission("group.apoiador")) {
                 * NextLvl = (tier * 100) * 2.25;
                 * } else {
                 * NextLvl = (tier * 150) * 2.25;
                 * }
                 */
                if (B.getPontos() >= NextLvl) {
                    Integer newtier = tier + 1;
                    builder.setTier(newtier, id);
                    builder.saveBuilder();
                    player.sendMessage(ChatColor.GREEN + Mensagens.NextLevel1);
                    player.sendMessage(ChatColor.GOLD + Mensagens.NextLevel2);
                    DiscordPonte.NextLevel(newtier.toString(), B.getDiscord(), player);
                    CheckRank(id);
                } else {
                    double resto = NextLvl - B.getPontos();
                    String Sresto = String.valueOf(resto);
                    Sresto = Sresto.substring(0, Sresto.indexOf(".") + 2);
                    player.sendMessage(Mensagens.Level1 + ChatColor.GOLD + ChatColor.BOLD + Sresto
                            + ChatColor.RESET + Mensagens.Level2);
                }
            }
        }
    }

    public void CheckAwardBuilds(String uuid) {
        Builder builder = new Builder();
        Builders B = builder.getBuilder(uuid);
        if (B.getBuilds() >= 1) {
            builder.setConquistas("1build", uuid);
        }
        if (B.getBuilds() >= 10) {
            builder.setConquistas("10build", uuid);
        }
        if (B.getBuilds() >= 30) {
            builder.setConquistas("30build", uuid);
        }
        if (B.getBuilds() >= 50) {
            builder.setConquistas("50build", uuid);
        }
        if (B.getBuilds() >= 100) {
            builder.setConquistas("100build", uuid);
        }
        if (B.getBuilds() >= 300) {
            builder.setConquistas("300build", uuid);
        }
        if (B.getBuilds() >= 500) {
            builder.setConquistas("500build", uuid);
        }
        if (B.getBuilds() >= 500) {
            builder.setConquistas("1000build", uuid);
        }
        builder.saveBuilder();
    }

    public Location getLocation(String coords, World w) {
        String[] ary = coords.split(",");

        int spawnY = w.getHighestBlockYAt(Integer.parseInt(ary[0].split("\\.")[0]),
                Integer.parseInt(ary[1].split("\\.")[0]));

        int x = Integer.parseInt(ary[0].split("\\.")[0]);
        int y = spawnY + 10;
        int z = Integer.parseInt(ary[1].split("\\.")[0]);
        Location loc = new Location(w, x, y, z);
        return loc;

    }

    public double[] toGeo(final double x, final double z) {
        return Sistemas.scaleProj.toGeo(x, z);
    }

    public double[] fromGeo(final double lon, final double lat) {
        return Sistemas.scaleProj.fromGeo(lon, lat);
    }

    static {
        projection = (GeographicProjection) new ModifiedAirocean();
        uprightProj = GeographicProjection.orientProjection(Sistemas.projection,
                GeographicProjection.Orientation.upright);
        scaleProj = new ScaleProjection(Sistemas.uprightProj, 7318261.522857145, 7318261.522857145);
    }
}
