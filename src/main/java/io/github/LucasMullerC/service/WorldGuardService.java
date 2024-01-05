package io.github.LucasMullerC.service;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.LucasMullerC.model.Claim;

public class WorldGuardService {

    public void AddToGuard(String coords, String Id, Player player) {
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

        LuckpermsService Luckperms = new LuckpermsService();
        Luckperms.addPermissionLuckPerms(Id, player.getUniqueId());
    }

    public void AddFlags(Claim claim) {
        OfflinePlayer leader = Bukkit.getOfflinePlayer(UUID.fromString(claim.getPlayer()));
        String Participantes = leader.getName();
        if (!claim.getParticipants().equals("nulo")) {
            String[] Parts = claim.getParticipants().split(",");
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(Parts[i]));
                Participantes += "," + pt.getName();
            }
        }
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        if (claim.getStatus().equals("T")) {
            Bukkit.dispatchCommand(console,
                    "region flag " + claim.getClaim() + " -w world greeting-title &a" + claim.getName());
        } else {
            Bukkit.dispatchCommand(console,
                    "region flag " + claim.getClaim() + " -w world greeting-title &9" + claim.getName());
        }
        Bukkit.dispatchCommand(console,
                "region flag " + claim.getClaim() + " -w world greeting-subtitle &6" + Participantes);
        Bukkit.dispatchCommand(console, "region flag " + claim.getClaim() + " worldedit -w world -g members allow");
    }

    public void addPermissionWG(String ID, Player player, UUID uid) {
        DefaultDomain members = getRegionMembers(ID, player);
        members.addPlayer(uid);

        LuckpermsService Luckperms = new LuckpermsService();
        Luckperms.addPermissionLuckPerms(ID, uid);
    }

    public void removePermissionWG(String ID, Player player, UUID uid) {
        DefaultDomain members = getRegionMembers(ID, player);
        members.removePlayer(uid);

        LuckpermsService Luckperms = new LuckpermsService();
        Luckperms.addPermissionLuckPerms(ID, uid);
    }

    private DefaultDomain getRegionMembers(String ID,Player player){
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        ProtectedRegion regiona = regions.getRegion(ID);
        return regiona.getMembers();
    }
}
