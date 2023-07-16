package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fastasyncworldedit.core.FaweAPI;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Gerencia.Claim;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Zonas;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class Regioes {

    // CLAIMS

    public String getSelection(Player player, int Tier) {
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        Region region;

        World selection = localSession.getSelectionWorld();
        try {
            if (selection == null)
                throw new IncompleteRegionException();
            region = localSession.getSelection(selection);
            // World world = selection.getWorld();
            Integer largura = getSelectionWidth(localSession, selection);
            Integer leng = getSelectionLength(localSession, selection);
            int Limite = 212;
            if (player.hasPermission("btebrasil.addcompleto")) {
                Limite = 99999;
            } else {
                Limite = getLimiteSelection(Tier);
            }
            if (largura <= Limite && leng <= Limite && largura >= 20 && leng >= 20) {
                Polygonal2DRegion polygonalRegion = (Polygonal2DRegion) selection;
                List<BlockVector2> points = polygonalRegion.getPoints();
                /*
                 * Map<String, ProtectedRegion> rgs =
                 * WorldGuardPlugin.inst().getRegionManager(selection).getRegions();
                 * // For passando por cada Ponto da seleção
                 * for (int c = 1; c < points.size(); c++) {
                 * // Atual
                 * int x = points.get(c).getBlockX();
                 * int z = points.get(c).getBlockZ();
                 * // Anterior
                 * int xp = points.get(c - 1).getBlockX();
                 * int zp = points.get(c - 1).getBlockZ();
                 * // Do While fazendo o caminho de um ponto para outro
                 * do {
                 * if (xp > x) {
                 * xp = xp - 1;
                 * } else if (xp < x) {
                 * xp = xp + 1;
                 * }
                 * if (zp > z) {
                 * zp = zp - 1;
                 * } else if (zp < z) {
                 * zp = zp + 1;
                 * }
                 * // For passando por todas as regiões
                 * for (ProtectedRegion region : rgs.values()) {
                 * // Ingora regiões dos times e global
                 * if (region.getId().equals("timenordeste") || region.getId().equals("timesul")
                 * || region.getId().equals("timesp") ||
                 * region.getId().equals("timeminasgerais")
                 * || region.getId().equals("timerj") || region.getId().equals("timeconorte")
                 * || region.getId().equals("__global__")) {
                 * }
                 * // Se for um claim
                 * else {
                 * // Verifica se a coordenada está contida na região
                 * if (region.contains(xp, 0, zp)) {
                 * if (xp - x >= 20 && zp - z >= 20){
                 * return "1"; // 1 = Região intersecciona outra região
                 * }
                 * }
                 * }
                 * }
                 * } while (x != xp && z != zp);
                 * }
                 */
                String pontos = points.toString().replaceAll("[\\[\\](){}]", "");
                return pontos.replaceAll(" ", "");
            } else {
                return "2"; // 2 = Seleção Fora dos Limites
            }
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Faça uma seleção primeiro."));
            return "3"; // 3 = Seleção não encontrada
        }

    }

    private static int getSelectionWidth(LocalSession localSession, World w) {
        BlockVector3 min = localSession.getRegionSelector(w).getRegion()
                .getMinimumPoint();
        BlockVector3 max = localSession.getRegionSelector(w).getRegion()
                .getMaximumPoint();

        return Math.abs(max.getBlockX() - min.getBlockX()) + 1;
    }

    private static int getSelectionLength(LocalSession localSession, World w) {
        BlockVector3 min = localSession.getRegionSelector(w).getRegion()
                .getMinimumPoint();
        BlockVector3 max = localSession.getRegionSelector(w).getRegion()
                .getMaximumPoint();

        return Math.abs(max.getBlockZ() - min.getBlockZ()) + 1;
    }

    public int getLimiteSelection(int Tier) {
        if (Tier >= 1 && Tier <= 2) {
            return 212;
        } else if (Tier >= 3 && Tier <= 4) {
            return 312;
        } else if (Tier >= 5 && Tier <= 6) {
            return 412;
        } else if (Tier >= 7 && Tier <= 8) {
            return 512;
        } else if (Tier >= 9 && Tier <= 10) {
            return 612;
        } else {
            return 712;
        }
    }

    public void AddClaimGuard(String coords, String Id, Player player) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        DefaultDomain members;
        String[] ary = coords.split(",");
        List<BlockVector2> points = Lists.newArrayList();
        for (int i = 0; i < (ary.length - 1); i += 2) {
            points.add(BlockVector2.at(Integer.parseInt(ary[i].split("\\.")[0]),
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

    public void AddFlags(String Id) {
        Claim claim = new Claim();
        Areas A = claim.getArea(Id);
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
        if (A.getStatus().equals("T")) {
            Bukkit.dispatchCommand(console,
                    "region flag " + Id + " -w world greeting-title &a" + A.getNome());
        } else {
            Bukkit.dispatchCommand(console,
                    "region flag " + Id + " -w world greeting-title &9" + A.getNome());
        }
        Bukkit.dispatchCommand(console,
                "region flag " + Id + " -w world greeting-subtitle &6" + Participantes);
        Bukkit.dispatchCommand(console, "region flag " + Id + " worldedit -w world -g members allow");
    }

    public void RemoveClaim(Areas A, Player player) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        Claim claim = new Claim();
        regions.removeRegion(A.getClaim());
        claim.RemoverArea(A.getClaim());
        if (claim.getPendenteClaim(A.getClaim()) != null) {
            claim.RemoverPendenteClaim(A.getClaim());
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

    public void CompleteClaim(String ID, Player player, String Participantes) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();

        // Só deleta os participantes
        if (!Participantes.equals("nulo")) {
            String[] Parts = Participantes.split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer convite = Bukkit.getOfflinePlayer(Parts[i]);
                members.removePlayer(convite.getUniqueId());
                removeLuckPerms(ID, convite.getUniqueId());
            }
        }

        /*
         * members.removeAll(); //DELETA TODOS OS MEMBROS
         * 
         * if (!Participantes.equals("nulo")) {
         * LuckPerms api = LuckPermsProvider.get();
         * User user = api.getPlayerAdapter(Player.class).getUser(player);
         * Node nodea =
         * Node.builder("worldedit.*").value(true).withContext("worldguard:region", ID)
         * .build();
         * Node nodeb = Node.builder("worldguard.region.select.member." +
         * ID).value(true).build();
         * user.data().remove(nodea);
         * user.data().remove(nodeb);
         * api.getUserManager().saveUser(user);
         * } else {
         * removeLuckPerms(ID, player.getUniqueId());
         * String[] Parts = Participantes.split(",");
         * for (int i = 0; i < Parts.length; i++) {
         * OfflinePlayer convite = Bukkit.getOfflinePlayer(Parts[i]);
         * members.removePlayer(convite.getUniqueId());
         * removeLuckPerms(ID, convite.getUniqueId());
         * }
         * }
         */
    }

    public void addPermissaoWG(String ID, Player player, UUID uid) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();
        members.addPlayer(uid);
        AddFlags(ID);
        addLuckPerms(ID, uid);
    }

    public void removePermissaoWG(String ID, Player player, UUID uid) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        DefaultDomain members = regiona.getMembers();
        members.removePlayer(uid);
        AddFlags(ID);
        removeLuckPerms(ID, uid);
    }

    public void removeLuckPerms(String ID, UUID uid) {
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

    public void addLuckPerms(String ID, UUID uid) {
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

    public double getDistancia(String id, org.bukkit.World world, int peso) {
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(world);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
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
    public void loadSchematic(Player player, Location location) throws FileNotFoundException, IOException {
        Clipboard clipboard;
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();

        File schem = new File(
                plugin.getDataFolder() + File.separator + "schematics" + File.separator + "area2.schematic");

        BlockVector3 to = BlockVector3.at(location.getX(), location.getY(), location.getZ());
        ClipboardFormat format = ClipboardFormats.findByFile(schem);

        try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
            clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(FaweAPI.getWorld("world"))
                    .build()) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession) // Create a builder using the edit session
                        .to(to) // Set where you want the paste to go
                        .ignoreAirBlocks(false) // Tell world edit not to paste air blocks (true/false)
                        .build(); // Build the operation
                Operations.complete(operation); // This'll complete a operation synchronously until it's finished
                editSession.close(); // We now close it to flush the buffers and run the cleanup tasks.
            }
        }
    }

    public void removeRegion(Player player, Zonas z) {
        WorldEdit worldEdit = WorldEdit.getInstance();
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        EditSession editSession = worldEdit.getEditSessionFactory().getEditSession(actor.getWorld(), -1);

        Location meio = new Location(player.getWorld(), z.getla().getX() - 2, 45, z.getla().getZ() - 2);
        BlockVector3 minPoint = BlockVector3.at(meio.getX() - 45, 60, meio.getZ() - 45);
        BlockVector3 maxPoint = BlockVector3.at(meio.getX() + 45, 25, meio.getZ() + 45);

        CuboidRegion selection = new CuboidRegion(minPoint, maxPoint);
        editSession.setBlocks((Region) selection, BukkitAdapter.adapt(Material.AIR.createBlockData()));

    }

    public void AddPermissao(Player player, String Zona) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);

        Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "a")
                .build();
        Bukkit.dispatchCommand(console,
                "region flag apply" + Zona + "a worldedit -w world -g members allow");

        Node nodeb = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "b")
                .build();
        Bukkit.dispatchCommand(console,
                "region flag apply" + Zona + "b worldedit -w world -g members allow");

        Node nodec = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "c")
                .build();
        Bukkit.dispatchCommand(console,
                "region flag apply" + Zona + "c worldedit -w world -g members allow");

        Node noded = Node.builder("worldedit.*").value(true).withContext("worldguard:region", "apply" + Zona + "d")
                .build();
        Bukkit.dispatchCommand(console,
                "region flag apply" + Zona + "d worldedit -w world -g members allow");

        user.data().add(nodea);
        user.data().add(nodeb);
        user.data().add(nodec);
        user.data().add(noded);
        user.data().add(noded);
        api.getUserManager().saveUser(user);
    }

    public void RemovePermissao(Player player, String Zona) {
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

    public void CreateRegion(Integer zona, Player player, boolean sobe) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        DefaultDomain members;
        if (zona == 1) {
            // 1a
            List<BlockVector2> points = Lists.newArrayList(); // Call from Guava
            points.add(BlockVector2.at(-649, 288));
            points.add(BlockVector2.at(-611, 288));
            points.add(BlockVector2.at(-612, 294));
            points.add(BlockVector2.at(-613, 298));
            points.add(BlockVector2.at(-614, 302));
            points.add(BlockVector2.at(-615, 304));
            points.add(BlockVector2.at(-616, 306));
            points.add(BlockVector2.at(-617, 308));
            points.add(BlockVector2.at(-625, 317));
            points.add(BlockVector2.at(-629, 320));
            points.add(BlockVector2.at(-631, 321));
            points.add(BlockVector2.at(-633, 322));
            points.add(BlockVector2.at(-635, 323));
            points.add(BlockVector2.at(-639, 324));
            points.add(BlockVector2.at(-643, 325));
            points.add(BlockVector2.at(-649, 326));
            int minY = 31;
            int maxY = 42;

            ProtectedRegion regiona = new ProtectedPolygonalRegion("apply1a", points, minY, maxY);
            regions.addRegion(regiona);
            members = regiona.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1b
            points = Lists.newArrayList(); // Call from Guava
            points.add(BlockVector2.at(-651, 288));
            points.add(BlockVector2.at(-651, 326));
            points.add(BlockVector2.at(-657, 325));
            points.add(BlockVector2.at(-661, 324));
            points.add(BlockVector2.at(-665, 323));
            points.add(BlockVector2.at(-669, 321));
            points.add(BlockVector2.at(-675, 317));
            points.add(BlockVector2.at(-680, 312));
            points.add(BlockVector2.at(-685, 304));
            points.add(BlockVector2.at(-687, 298));
            points.add(BlockVector2.at(-688, 294));
            points.add(BlockVector2.at(-689, 288));
            minY = 31;
            maxY = 42;

            ProtectedRegion regionb = new ProtectedPolygonalRegion("apply1b", points, minY, maxY);
            regions.addRegion(regionb);
            members = regionb.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1c
            points = Lists.newArrayList(); // Call from Guava
            points.add(BlockVector2.at(-651, 286));
            points.add(BlockVector2.at(-689, 286));
            points.add(BlockVector2.at(-689, 281));
            points.add(BlockVector2.at(-688, 280));
            points.add(BlockVector2.at(-688, 277));
            points.add(BlockVector2.at(-687, 276));
            points.add(BlockVector2.at(-687, 273));
            points.add(BlockVector2.at(-686, 272));
            points.add(BlockVector2.at(-686, 271));
            points.add(BlockVector2.at(-685, 270));
            points.add(BlockVector2.at(-685, 269));
            points.add(BlockVector2.at(-684, 268));
            points.add(BlockVector2.at(-684, 267));
            points.add(BlockVector2.at(-683, 266));
            points.add(BlockVector2.at(-683, 265));
            points.add(BlockVector2.at(-682, 264));
            points.add(BlockVector2.at(-681, 263));
            points.add(BlockVector2.at(-680, 262));
            points.add(BlockVector2.at(-680, 261));
            points.add(BlockVector2.at(-676, 257));
            points.add(BlockVector2.at(-675, 257));
            points.add(BlockVector2.at(-672, 254));
            points.add(BlockVector2.at(-671, 254));
            points.add(BlockVector2.at(-670, 253));
            points.add(BlockVector2.at(-669, 253));
            points.add(BlockVector2.at(-668, 252));
            points.add(BlockVector2.at(-667, 252));
            points.add(BlockVector2.at(-666, 251));
            points.add(BlockVector2.at(-665, 251));
            points.add(BlockVector2.at(-664, 250));
            points.add(BlockVector2.at(-661, 250));
            points.add(BlockVector2.at(-660, 249));
            points.add(BlockVector2.at(-657, 249));
            points.add(BlockVector2.at(-656, 248));
            points.add(BlockVector2.at(-651, 248));
            minY = 30;
            maxY = 42;

            ProtectedRegion regionc = new ProtectedPolygonalRegion("apply1c", points, minY, maxY);
            regions.addRegion(regionc);
            members = regionc.getMembers();
            members.addPlayer(player.getUniqueId());
            // 1d
            points = Lists.newArrayList(); // Call from Guava
            points.add(BlockVector2.at(-649, 286));
            points.add(BlockVector2.at(-649, 248));
            points.add(BlockVector2.at(-644, 248));
            points.add(BlockVector2.at(-643, 249));
            points.add(BlockVector2.at(-640, 249));
            points.add(BlockVector2.at(-639, 250));
            points.add(BlockVector2.at(-636, 250));
            points.add(BlockVector2.at(-635, 251));
            points.add(BlockVector2.at(-634, 251));
            points.add(BlockVector2.at(-633, 252));
            points.add(BlockVector2.at(-632, 252));
            points.add(BlockVector2.at(-631, 253));
            points.add(BlockVector2.at(-630, 253));
            points.add(BlockVector2.at(-629, 254));
            points.add(BlockVector2.at(-628, 254));
            points.add(BlockVector2.at(-627, 255));
            points.add(BlockVector2.at(-625, 257));
            points.add(BlockVector2.at(-624, 257));
            points.add(BlockVector2.at(-620, 261));
            points.add(BlockVector2.at(-620, 262));
            points.add(BlockVector2.at(-617, 265));
            points.add(BlockVector2.at(-617, 266));
            points.add(BlockVector2.at(-616, 267));
            points.add(BlockVector2.at(-616, 268));
            points.add(BlockVector2.at(-615, 269));
            points.add(BlockVector2.at(-615, 270));
            points.add(BlockVector2.at(-614, 271));
            points.add(BlockVector2.at(-614, 272));
            points.add(BlockVector2.at(-613, 273));
            points.add(BlockVector2.at(-613, 276));
            points.add(BlockVector2.at(-612, 277));
            points.add(BlockVector2.at(-612, 280));
            points.add(BlockVector2.at(-611, 281));
            points.add(BlockVector2.at(-611, 286));
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
                List<BlockVector2> pointsaold = polygona.getPoints();
                List<BlockVector2> pointsanew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsaold.size(); i++) {
                    pointsaold.get(i).getBlockX();
                    pointsanew.add(BlockVector2.at(pointsaold.get(i).getBlockX() + 90,
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
                List<BlockVector2> pointsbold = polygonb.getPoints();
                List<BlockVector2> pointsbnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsbold.size(); i++) {
                    pointsbold.get(i).getBlockX();
                    pointsbnew.add(BlockVector2.at(pointsbold.get(i).getBlockX() + 90,
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
                List<BlockVector2> pointscold = polygonc.getPoints();
                List<BlockVector2> pointscnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointscold.size(); i++) {
                    pointscold.get(i).getBlockX();
                    pointscnew.add(BlockVector2.at(pointscold.get(i).getBlockX() + 90,
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
                List<BlockVector2> pointsdold = polygond.getPoints();
                List<BlockVector2> pointsdnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsdold.size(); i++) {
                    pointsdold.get(i).getBlockX();
                    pointsdnew.add(BlockVector2.at(pointsdold.get(i).getBlockX() + 90,
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
                List<BlockVector2> pointsaold = polygona.getPoints();
                List<BlockVector2> pointsanew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsaold.size(); i++) {
                    pointsaold.get(i).getBlockX();
                    pointsanew.add(BlockVector2.at(pointsaold.get(i).getBlockX(),
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
                List<BlockVector2> pointsbold = polygonb.getPoints();
                List<BlockVector2> pointsbnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsbold.size(); i++) {
                    pointsbold.get(i).getBlockX();
                    pointsbnew.add(BlockVector2.at(pointsbold.get(i).getBlockX(),
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
                List<BlockVector2> pointscold = polygonc.getPoints();
                List<BlockVector2> pointscnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointscold.size(); i++) {
                    pointscold.get(i).getBlockX();
                    pointscnew.add(BlockVector2.at(pointscold.get(i).getBlockX(),
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
                List<BlockVector2> pointsdold = polygond.getPoints();
                List<BlockVector2> pointsdnew = Lists.newArrayList(); // Call from Guava
                for (int i = 0; i < pointsdold.size(); i++) {
                    pointsdold.get(i).getBlockX();
                    pointsdnew.add(BlockVector2.at(pointsdold.get(i).getBlockX(),
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
