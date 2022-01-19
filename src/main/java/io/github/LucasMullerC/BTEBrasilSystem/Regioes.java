package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Zonas;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class Regioes {

    // CLAIMS

    public static String getSelection(Player player, int Tier) {
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Selection selection = worldEdit.getSelection(player);

        if (selection != null) {
            World world = selection.getWorld();
            Integer largura = selection.getWidth();
            Integer leng = selection.getLength();
            int Limite = 212;
            if(player.hasPermission("btebrasil.addcompleto")){
                Limite = 99999;
            }
            else{
                Limite = getLimiteSelection(Tier);
            }
            if (largura <= Limite && leng <= Limite && largura >= 20 && leng >= 20) {
                Polygonal2DSelection polygon = (Polygonal2DSelection) selection;
                List<BlockVector2D> points = polygon.getNativePoints();
                Map<String, ProtectedRegion> rgs = WorldGuardPlugin.inst().getRegionManager(world).getRegions();
                // For passando por cada Ponto da seleção
                for (int c = 1; c < points.size(); c++) {
                    // Atual
                    int x = points.get(c).getBlockX();
                    int z = points.get(c).getBlockZ();
                    // Anterior
                    int xp = points.get(c - 1).getBlockX();
                    int zp = points.get(c - 1).getBlockZ();
                    // Do While fazendo o caminho de um ponto para outro
                    do {
                        if (xp > x) {
                            xp = xp - 1;
                        } else if (xp < x) {
                            xp = xp + 1;
                        }
                        if (zp > z) {
                            zp = zp - 1;
                        } else if (zp < z) {
                            zp = zp + 1;
                        }
                        // For passando por todas as regiões
                        for (ProtectedRegion region : rgs.values()) {
                            // Ingora regiões dos times e global
                            if (region.getId().equals("timenordeste") || region.getId().equals("timesul")
                                    || region.getId().equals("timesp") || region.getId().equals("timeminasgerais")
                                    || region.getId().equals("timerj") || region.getId().equals("timeconorte")
                                    || region.getId().equals("__global__")) {
                            }
                            // Se for um claim
                            else {
                                // Verifica se a coordenada está contida na região
                                if (region.contains(xp, 0, zp)) {
                                    if (xp - x >= 20 && zp - z >= 20){
                                        return "1"; // 1 = Região intersecciona outra região
                                    }
                                }
                            }
                        }
                    } while (x != xp && z != zp);
                }
                String pontos = points.toString().replaceAll("[\\[\\](){}]", "");
                return pontos.replaceAll(" ", "");
            } else {
                return "2"; // 2 = Seleção Fora dos Limites
            }
        } else {
            return "3"; // 3 = Seleção não encontrada
        }
    }

    public static int getLimiteSelection(int Tier) {
        if(Tier >=1 && Tier <= 3){
            return 212;
        }
        else if(Tier >=4 && Tier <= 6){
            return 312;
        }
        else if(Tier >=7 && Tier <= 9){
            return 412;
        }
        else if(Tier >=10 && Tier <= 12){
            return 512;
        }
        else if(Tier >=13 && Tier <= 15){
            return 612;
        }
        else{
            return 712;
        }
    }

    public static void AddClaimGuard(String coords, String Id, Player player) {
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        World w = player.getWorld();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        DefaultDomain members;
        String[] ary = coords.split(",");
        List<BlockVector2D> points = Lists.newArrayList();
        for (int i = 0; i < (ary.length - 1); i += 2) {
            points.add(new BlockVector2D(Integer.parseInt(ary[i].split("\\.")[0]),
                    Integer.parseInt(ary[i + 1].split("\\.")[0])));
        }
        int minY = -100;
        int maxY = 3500;
        ProtectedRegion regiona = new ProtectedPolygonalRegion(Id, points, minY, maxY);
        regiona.setPriority(1);
        regions.addRegion(regiona);
        members = regiona.getMembers();
        members.addPlayer(player.getUniqueId());

        AddFlags(Id);
        addLuckPerms(Id, player.getUniqueId());
    }
    public static void AddFlags(String Id){
        Areas A = GerenciarListas.getArea(Id);
        OfflinePlayer lider = Bukkit.getOfflinePlayer(UUID.fromString(A.getPlayer()));
        String Participantes = lider.getName();
        if (!A.getParticipantes().equals("nulo")) {
            String[] Parts = A.getParticipantes().split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(Parts[i]));
                Participantes += "," + pt.getName();
            }
        }
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();  
        if(A.getStatus().equals("T")){
            Bukkit.dispatchCommand(console, "region flag "+Id+" -w TerraPreGenerated greeting-title &a"+A.getNome());
        }
        else{
            Bukkit.dispatchCommand(console, "region flag "+Id+" -w TerraPreGenerated greeting-title &9"+A.getNome());
        }
        Bukkit.dispatchCommand(console, "region flag "+Id+" -w TerraPreGenerated greeting-subtitle &6"+Participantes);
    }
    public static void RemoveClaim(Areas A, Player player) {
        World w = player.getWorld();
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        regions.removeRegion(A.getClaim());
        GerenciarListas.RemoverArea(A.getClaim());
        if (GerenciarListas.getPendenteClaim(A.getClaim()) != null) {
            GerenciarListas.RemoverPendenteClaim(A.getClaim());
        }
        String Participantes = A.getParticipantes();
        if (Participantes.equals("nulo")) {
            removeLuckPerms(A.getClaim(), UUID.fromString(A.getPlayer()));
        } else {
            removeLuckPerms(A.getClaim(), UUID.fromString(A.getPlayer()));
            String[] Parts = Participantes.split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer convite = Bukkit.getOfflinePlayer(Parts[i]);
                removeLuckPerms(A.getClaim(), convite.getUniqueId());
            }
        }
    }

    public static void CompleteClaim(String ID, Player player, String Participantes) {
        World w = player.getWorld();
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();

        //Só deleta os participantes
        if (!Participantes.equals("nulo")) {
            String[] Parts = Participantes.split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer convite = Bukkit.getOfflinePlayer(Parts[i]);
                members.removePlayer(convite.getUniqueId());
                removeLuckPerms(ID, convite.getUniqueId());
            }
        }

        /*members.removeAll();  //DELETA TODOS OS MEMBROS

        if (!Participantes.equals("nulo")) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(Player.class).getUser(player);
            Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", ID)
                    .build();
            Node nodeb = Node.builder("worldguard.region.select.member." + ID).value(true).build();
            user.data().remove(nodea);
            user.data().remove(nodeb);
            api.getUserManager().saveUser(user);
        } else {
            removeLuckPerms(ID, player.getUniqueId());
            String[] Parts = Participantes.split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer convite = Bukkit.getOfflinePlayer(Parts[i]);
                members.removePlayer(convite.getUniqueId());
                removeLuckPerms(ID, convite.getUniqueId());
            }
        }*/
    }

    public static void addPermissaoWG(String ID, Player player, UUID uid) {
        World w = player.getWorld();
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();
        members.addPlayer(uid);
        AddFlags(ID);
        addLuckPerms(ID, uid);
    }

    public static void removePermissaoWG(String ID, Player player, UUID uid) {
        World w = player.getWorld();
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();
        members.removePlayer(uid);
        AddFlags(ID);
        removeLuckPerms(ID, uid);
    }

    public static void removeLuckPerms(String ID, UUID uid) {
        // Remove permissão no luckyperms
        LuckPerms api = LuckPermsProvider.get();
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uid);
        User user;
        try {
            user = userFuture.get();
            Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", ID)
                    .build();
            Node nodeb = Node.builder("worldguard.region.select.member." + ID).value(true).build();
            user.data().remove(nodea);
            user.data().remove(nodeb);
            api.getUserManager().saveUser(user);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void addLuckPerms(String ID, UUID uid) {
        // add permissão no luckyperms
        LuckPerms api = LuckPermsProvider.get();
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uid);
        User user;
        try {
            user = userFuture.get();
            Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", ID)
                    .build();
                    Node nodeb = Node.builder("worldguard.region.select.member." + ID).value(true).build();
            user.data().add(nodea);
            user.data().add(nodeb);
            api.getUserManager().saveUser(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static double getDistancia(String id, World w, int peso) {
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(id);
        Integer distanciaX = regiona.getMaximumPoint().getBlockX() - regiona.getMinimumPoint().getBlockX();
        Integer distanciaZ = regiona.getMaximumPoint().getBlockZ() - regiona.getMinimumPoint().getBlockZ();
        double pontos = distanciaX + distanciaZ;
        if (peso <= pontos) {
            pontos = pontos / peso;
            return pontos;
        } else {
            return -1;
        }
    }

    // APLICAÇÃO

    @SuppressWarnings("deprecation")
    public static void loadSchematic(Player player, Location location) {

        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        EditSession editSession = new EditSession(new BukkitWorld(player.getLocation().getWorld()), Integer.MAX_VALUE);
        Vector loc = new Vector(location.getX(), location.getY(), location.getZ());
        File schem = new File(
                plugin.getDataFolder() + File.separator + "schematics" + File.separator + "area2.schematic");
        try {
            CuboidClipboard cc = CuboidClipboard.loadSchematic(schem);
            cc.paste(editSession, loc, false);
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        } catch (com.sk89q.worldedit.world.DataException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static void removeRegion(World w, Zonas z) {
        Location meio = new Location(w, z.getla().getX() - 2, 45, z.getla().getZ() - 2);
        Vector loc1 = new Vector(meio.getX() - 45, 60, meio.getZ() - 45);
        Vector loc2 = new Vector(meio.getX() + 45, 25, meio.getZ() + 45);
        CuboidRegion selection = new CuboidRegion(loc1, loc2);
        EditSession editSession = new EditSession(new BukkitWorld(w), Integer.MAX_VALUE);
        BaseBlock bb = new BaseBlock(BlockID.AIR);
        try {
            editSession.setBlocks(selection, bb);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }

    public static void AddPermissao(Player player, String Zona) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "a")
                .build();
        Node nodeb = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "b")
                .build();
        Node nodec = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "c")
                .build();
        Node noded = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "d")
                .build();
        user.data().add(nodea);
        user.data().add(nodeb);
        user.data().add(nodec);
        user.data().add(noded);
        user.data().add(noded);
        api.getUserManager().saveUser(user);
    }

    public static void RemovePermissao(Player player, String Zona) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "a")
                .build();
        Node nodeb = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "b")
                .build();
        Node nodec = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "c")
                .build();
        Node noded = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "d")
                .build();
        user.data().remove(nodea);
        user.data().remove(nodeb);
        user.data().remove(nodec);
        user.data().remove(noded);
        user.data().remove(noded);
        api.getUserManager().saveUser(user);
    }

    public static void CreateRegion(Integer zona, Player player, boolean sobe) {
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        World w = player.getWorld();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        DefaultDomain members;
        if (zona == 1) {
            // 1a
            List<BlockVector2D> points = Lists.newArrayList(); // Call from Guava
            points.add(new BlockVector2D(-649, 288));
            points.add(new BlockVector2D(-611, 288));
            points.add(new BlockVector2D(-612, 294));
            points.add(new BlockVector2D(-613, 298));
            points.add(new BlockVector2D(-614, 302));
            points.add(new BlockVector2D(-615, 304));
            points.add(new BlockVector2D(-616, 306));
            points.add(new BlockVector2D(-617, 308));
            points.add(new BlockVector2D(-625, 317));
            points.add(new BlockVector2D(-629, 320));
            points.add(new BlockVector2D(-631, 321));
            points.add(new BlockVector2D(-633, 322));
            points.add(new BlockVector2D(-635, 323));
            points.add(new BlockVector2D(-639, 324));
            points.add(new BlockVector2D(-643, 325));
            points.add(new BlockVector2D(-649, 326));
            int minY = 31;
            int maxY = 42;

            ProtectedRegion regiona = new ProtectedPolygonalRegion("apply1a", points, minY, maxY);
            regions.addRegion(regiona);
            members = regiona.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1b
            points = Lists.newArrayList(); // Call from Guava
            points.add(new BlockVector2D(-651, 288));
            points.add(new BlockVector2D(-651, 326));
            points.add(new BlockVector2D(-657, 325));
            points.add(new BlockVector2D(-661, 324));
            points.add(new BlockVector2D(-665, 323));
            points.add(new BlockVector2D(-669, 321));
            points.add(new BlockVector2D(-675, 317));
            points.add(new BlockVector2D(-680, 312));
            points.add(new BlockVector2D(-685, 304));
            points.add(new BlockVector2D(-687, 298));
            points.add(new BlockVector2D(-688, 294));
            points.add(new BlockVector2D(-689, 288));
            minY = 31;
            maxY = 42;

            ProtectedRegion regionb = new ProtectedPolygonalRegion("apply1b", points, minY, maxY);
            regions.addRegion(regionb);
            members = regionb.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1c
            points = Lists.newArrayList(); // Call from Guava
            points.add(new BlockVector2D(-651, 286));
            points.add(new BlockVector2D(-689, 286));
            points.add(new BlockVector2D(-689, 281));
            points.add(new BlockVector2D(-688, 280));
            points.add(new BlockVector2D(-688, 277));
            points.add(new BlockVector2D(-687, 276));
            points.add(new BlockVector2D(-687, 273));
            points.add(new BlockVector2D(-686, 272));
            points.add(new BlockVector2D(-686, 271));
            points.add(new BlockVector2D(-685, 270));
            points.add(new BlockVector2D(-685, 269));
            points.add(new BlockVector2D(-684, 268));
            points.add(new BlockVector2D(-684, 267));
            points.add(new BlockVector2D(-683, 266));
            points.add(new BlockVector2D(-683, 265));
            points.add(new BlockVector2D(-682, 264));
            points.add(new BlockVector2D(-681, 263));
            points.add(new BlockVector2D(-680, 262));
            points.add(new BlockVector2D(-680, 261));
            points.add(new BlockVector2D(-676, 257));
            points.add(new BlockVector2D(-675, 257));
            points.add(new BlockVector2D(-672, 254));
            points.add(new BlockVector2D(-671, 254));
            points.add(new BlockVector2D(-670, 253));
            points.add(new BlockVector2D(-669, 253));
            points.add(new BlockVector2D(-668, 252));
            points.add(new BlockVector2D(-667, 252));
            points.add(new BlockVector2D(-666, 251));
            points.add(new BlockVector2D(-665, 251));
            points.add(new BlockVector2D(-664, 250));
            points.add(new BlockVector2D(-661, 250));
            points.add(new BlockVector2D(-660, 249));
            points.add(new BlockVector2D(-657, 249));
            points.add(new BlockVector2D(-656, 248));
            points.add(new BlockVector2D(-651, 248));
            minY = 30;
            maxY = 42;

            ProtectedRegion regionc = new ProtectedPolygonalRegion("apply1c", points, minY, maxY);
            regions.addRegion(regionc);
            members = regionc.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1d
            points = Lists.newArrayList(); // Call from Guava
            points.add(new BlockVector2D(-649, 286));
            points.add(new BlockVector2D(-649, 248));
            points.add(new BlockVector2D(-644, 248));
            points.add(new BlockVector2D(-643, 249));
            points.add(new BlockVector2D(-640, 249));
            points.add(new BlockVector2D(-639, 250));
            points.add(new BlockVector2D(-636, 250));
            points.add(new BlockVector2D(-635, 251));
            points.add(new BlockVector2D(-634, 251));
            points.add(new BlockVector2D(-633, 252));
            points.add(new BlockVector2D(-632, 252));
            points.add(new BlockVector2D(-631, 253));
            points.add(new BlockVector2D(-630, 253));
            points.add(new BlockVector2D(-629, 254));
            points.add(new BlockVector2D(-628, 254));
            points.add(new BlockVector2D(-627, 255));
            points.add(new BlockVector2D(-625, 257));
            points.add(new BlockVector2D(-624, 257));
            points.add(new BlockVector2D(-620, 261));
            points.add(new BlockVector2D(-620, 262));
            points.add(new BlockVector2D(-617, 265));
            points.add(new BlockVector2D(-617, 266));
            points.add(new BlockVector2D(-616, 267));
            points.add(new BlockVector2D(-616, 268));
            points.add(new BlockVector2D(-615, 269));
            points.add(new BlockVector2D(-615, 270));
            points.add(new BlockVector2D(-614, 271));
            points.add(new BlockVector2D(-614, 272));
            points.add(new BlockVector2D(-613, 273));
            points.add(new BlockVector2D(-613, 276));
            points.add(new BlockVector2D(-612, 277));
            points.add(new BlockVector2D(-612, 280));
            points.add(new BlockVector2D(-611, 281));
            points.add(new BlockVector2D(-611, 286));
            minY = 31;
            maxY = 42;

            ProtectedRegion regiond = new ProtectedPolygonalRegion("apply1d", points, minY, maxY);
            regions.addRegion(regiond);
            members = regiond.getMembers();
            members.addPlayer(player.getUniqueId());
        } else {
            if (sobe == true) {
                Integer zonapost = zona - 5;
                ProtectedRegion regionposta = regions.getRegion("apply" + zonapost.toString() + "a");
                ProtectedRegion regionpostb = regions.getRegion("apply" + zonapost.toString() + "b");
                ProtectedRegion regionpostc = regions.getRegion("apply" + zonapost.toString() + "c");
                ProtectedRegion regionpostd = regions.getRegion("apply" + zonapost.toString() + "d");
                // a
                ProtectedPolygonalRegion polygona = (ProtectedPolygonalRegion) regionposta;
                List<BlockVector2D> pointsaold = polygona.getPoints();
                List<BlockVector2D> pointsanew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsaold.size(); i++) {
                    pointsaold.get(i).getBlockX();
                    pointsanew.add(new BlockVector2D(pointsaold.get(i).getBlockX() + 90,
                            pointsaold.get(i).getBlockZ()));
                }
                int minY = 31;
                int maxY = 42;
                ProtectedRegion regiona = new ProtectedPolygonalRegion("apply" + zona.toString() + "a",
                        pointsanew, minY, maxY);
                regions.addRegion(regiona);
                members = regiona.getMembers();
                members.addPlayer(player.getUniqueId());
                // b
                ProtectedPolygonalRegion polygonb = (ProtectedPolygonalRegion) regionpostb;
                List<BlockVector2D> pointsbold = polygonb.getPoints();
                List<BlockVector2D> pointsbnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsbold.size(); i++) {
                    pointsbold.get(i).getBlockX();
                    pointsbnew.add(new BlockVector2D(pointsbold.get(i).getBlockX() + 90,
                            pointsbold.get(i).getBlockZ()));
                }
                minY = 31;
                maxY = 42;
                ProtectedRegion regionb = new ProtectedPolygonalRegion("apply" + zona.toString() + "b",
                        pointsbnew, minY, maxY);
                regions.addRegion(regionb);
                members = regionb.getMembers();
                members.addPlayer(player.getUniqueId());
                // c
                ProtectedPolygonalRegion polygonc = (ProtectedPolygonalRegion) regionpostc;
                List<BlockVector2D> pointscold = polygonc.getPoints();
                List<BlockVector2D> pointscnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointscold.size(); i++) {
                    pointscold.get(i).getBlockX();
                    pointscnew.add(new BlockVector2D(pointscold.get(i).getBlockX() + 90,
                            pointscold.get(i).getBlockZ()));
                }
                minY = 30;
                maxY = 42;
                ProtectedRegion regionc = new ProtectedPolygonalRegion("apply" + zona.toString() + "c",
                        pointscnew, minY, maxY);
                regions.addRegion(regionc);
                members = regionc.getMembers();
                members.addPlayer(player.getUniqueId());
                // d
                ProtectedPolygonalRegion polygond = (ProtectedPolygonalRegion) regionpostd;
                List<BlockVector2D> pointsdold = polygond.getPoints();
                List<BlockVector2D> pointsdnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsdold.size(); i++) {
                    pointsdold.get(i).getBlockX();
                    pointsdnew.add(new BlockVector2D(pointsdold.get(i).getBlockX() + 90,
                            pointsdold.get(i).getBlockZ()));
                }
                minY = 31;
                maxY = 42;
                ProtectedRegion regiond = new ProtectedPolygonalRegion("apply" + zona.toString() + "d",
                        pointsdnew, minY, maxY);
                regions.addRegion(regiond);
                members = regiond.getMembers();
                members.addPlayer(player.getUniqueId());
            } else { // desce
                Integer zonapost = zona - 1;
                ProtectedRegion regionposta = regions.getRegion("apply" + zonapost.toString() + "a");
                ProtectedRegion regionpostb = regions.getRegion("apply" + zonapost.toString() + "b");
                ProtectedRegion regionpostc = regions.getRegion("apply" + zonapost.toString() + "c");
                ProtectedRegion regionpostd = regions.getRegion("apply" + zonapost.toString() + "d");
                // a
                ProtectedPolygonalRegion polygona = (ProtectedPolygonalRegion) regionposta;
                List<BlockVector2D> pointsaold = polygona.getPoints();
                List<BlockVector2D> pointsanew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsaold.size(); i++) {
                    pointsaold.get(i).getBlockX();
                    pointsanew.add(new BlockVector2D(pointsaold.get(i).getBlockX(),
                            pointsaold.get(i).getBlockZ() + 90));
                }
                int minY = 31;
                int maxY = 42;
                ProtectedRegion regiona = new ProtectedPolygonalRegion("apply" + zona.toString() + "a",
                        pointsanew, minY, maxY);
                regions.addRegion(regiona);
                members = regiona.getMembers();
                members.addPlayer(player.getUniqueId());
                // b
                ProtectedPolygonalRegion polygonb = (ProtectedPolygonalRegion) regionpostb;
                List<BlockVector2D> pointsbold = polygonb.getPoints();
                List<BlockVector2D> pointsbnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsbold.size(); i++) {
                    pointsbold.get(i).getBlockX();
                    pointsbnew.add(new BlockVector2D(pointsbold.get(i).getBlockX(),
                            pointsbold.get(i).getBlockZ() + 90));
                }
                minY = 31;
                maxY = 42;
                ProtectedRegion regionb = new ProtectedPolygonalRegion("apply" + zona.toString() + "b",
                        pointsbnew, minY, maxY);
                regions.addRegion(regionb);
                members = regionb.getMembers();
                members.addPlayer(player.getUniqueId());
                // c
                ProtectedPolygonalRegion polygonc = (ProtectedPolygonalRegion) regionpostc;
                List<BlockVector2D> pointscold = polygonc.getPoints();
                List<BlockVector2D> pointscnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointscold.size(); i++) {
                    pointscold.get(i).getBlockX();
                    pointscnew.add(new BlockVector2D(pointscold.get(i).getBlockX(),
                            pointscold.get(i).getBlockZ() + 90));
                }
                minY = 30;
                maxY = 42;
                ProtectedRegion regionc = new ProtectedPolygonalRegion("apply" + zona.toString() + "c",
                        pointscnew, minY, maxY);
                regions.addRegion(regionc);
                members = regionc.getMembers();
                members.addPlayer(player.getUniqueId());
                // d
                ProtectedPolygonalRegion polygond = (ProtectedPolygonalRegion) regionpostd;
                List<BlockVector2D> pointsdold = polygond.getPoints();
                List<BlockVector2D> pointsdnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsdold.size(); i++) {
                    pointsdold.get(i).getBlockX();
                    pointsdnew.add(new BlockVector2D(pointsdold.get(i).getBlockX(),
                            pointsdold.get(i).getBlockZ() + 90));
                }
                minY = 31;
                maxY = 42;
                ProtectedRegion regiond = new ProtectedPolygonalRegion("apply" + zona.toString() + "d",
                        pointsdnew, minY, maxY);
                regions.addRegion(regiond);
                members = regiond.getMembers();
                members.addPlayer(player.getUniqueId());
            }
        }
    }

}
