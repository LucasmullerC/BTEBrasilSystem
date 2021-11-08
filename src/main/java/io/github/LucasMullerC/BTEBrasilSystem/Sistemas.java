package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Zonas;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class Sistemas {
    public static String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public static Location getLocationString(final String s) {
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

    public static void RemovePermissaoTimes(Player player) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        user.data().remove(Node.builder("group.b_ne").build());
        user.data().remove(Node.builder("group.b_sp").build());
        user.data().remove(Node.builder("group.b_es").build());
        user.data().remove(Node.builder("group.b_rj").build());
        user.data().remove(Node.builder("group.b_mg").build());
        user.data().remove(Node.builder("group.b_co").build());
        user.data().remove(Node.builder("group.b_sul").build());
    }

    public static void DetectarDeadLine() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                GerenciarListas.DeletarDeadLines();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
}
