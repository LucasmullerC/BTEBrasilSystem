package io.github.LucasMullerC.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.LucasMullerC.model.Claim;

public class WorldGuardService {

    public void AddRegion(String coords, String Id, Player player) {
        RegionManager regions = getRegions(player);
        DefaultDomain members;
        String[] coordinatesArray = coords.split(",");
        List<BlockVector2> points = Lists.newArrayList();
        for (int i = 0; i < (coordinatesArray.length - 1); i += 2) {
            points.add(BlockVector2.at(Integer.parseInt(coordinatesArray[i].split("\\.")[0]),
                    Integer.parseInt(coordinatesArray[i + 1].split("\\.")[0])));
        }
        int minY = -100;
        int maxY = 3500;
        ProtectedRegion newRegion = new ProtectedPolygonalRegion(Id, points, minY, maxY);
        newRegion.setPriority(1);
        regions.addRegion(newRegion);
        if(player != null){
            members = newRegion.getMembers();
            members.addPlayer(player.getUniqueId());
    
            LuckpermsService Luckperms = new LuckpermsService();
            Luckperms.addPermissionLuckPerms(Id, player.getUniqueId());
        }
    }

    public void addClaimZone(Player player,Location location,String claimName,int totalSize){
        int radius = 20 + totalSize;
        RegionManager regions = getRegions(player);
        DefaultDomain members;

        BlockVector3 min = BlockVector3.at(
                location.getBlockX() - radius,
                0,                          
                location.getBlockZ() - radius 
        );
        BlockVector3 max = BlockVector3.at(
            location.getBlockX() + radius, 
                255,                    
                location.getBlockZ() + radius
        );

        ProtectedCuboidRegion region = new ProtectedCuboidRegion("copy"+claimName, min, max);
        regions.addRegion(region);
        members = region.getMembers();
        members.addPlayer(player.getUniqueId());
    }

    public void addApplicationZone(Player player,String Id,List<BlockVector2> points,int minY,int maxY){
        RegionManager regions = getRegions(player);
        DefaultDomain members;
        ProtectedRegion newRegion = new ProtectedPolygonalRegion(Id, points, minY, maxY);
        regions.addRegion(newRegion);
        members = newRegion.getMembers();
        members.addPlayer(player.getUniqueId());
    }

    public void RemoveRegion(String regionId, Player player) {
        LuckpermsService Luckperms = new LuckpermsService();
        RegionManager regions = getRegions(player);
        Set<UUID> members = regions.getRegion(regionId).getMembers().getUniqueIds();
        for(UUID member:members){
            Luckperms.removePermissionLuckPerms(regionId, member);
        }
        regions.removeRegion(regionId);
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

    public void addPermissionWG(String regionId, Player player, UUID uid) {
        RegionManager regions = getRegions(player);
        ProtectedRegion region = regions.getRegion(regionId);
        DefaultDomain members = region.getMembers();
        members.addPlayer(uid);

        LuckpermsService Luckperms = new LuckpermsService();
        Luckperms.addPermissionLuckPerms(regionId, uid);
    }

    public void removePermissionWG(String regionId, Player player, UUID uid) {
        RegionManager regions = getRegions(player);
        ProtectedRegion region = regions.getRegion(regionId);
        DefaultDomain members = region.getMembers();
        members.removePlayer(uid);

        LuckpermsService Luckperms = new LuckpermsService();
        Luckperms.removePermissionLuckPerms(regionId, uid);
    }

    public boolean isRegionPresent(Player player, int x, int y, int z) {
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(w);
        if (regionManager == null) {
            return false;
        }
        BlockVector3 position = BlockVector3.at(x, y, z);

        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(position);

        return regionSet.size() > 0;
    }

    public boolean isRegionIntersecting(Player player, int x, int y, int z, int regionSize) {
        RegionManager regions = getRegions(player);
    
        // Define the bounding box for the new region
        BlockVector3 min = BlockVector3.at(x - regionSize, 0, z - regionSize);
        BlockVector3 max = BlockVector3.at(x + regionSize, 255, z + regionSize);
        ProtectedCuboidRegion newRegion = new ProtectedCuboidRegion("tempCheck", min, max);
    
        // Iterate through all existing regions and check for overlap
        for (ProtectedRegion region : regions.getRegions().values()) {
            if (isBoundingBoxIntersecting(newRegion, region)) {
                return true; // Found intersection, position not valid
            }
        }
    
        return false; // No intersections, position is safe
    }
    
    private boolean isBoundingBoxIntersecting(ProtectedCuboidRegion region1, ProtectedRegion region2) {
        if (!(region2 instanceof ProtectedCuboidRegion)) {
            return false;
        }
    
        ProtectedCuboidRegion regionB = (ProtectedCuboidRegion) region2;
    
        BlockVector3 min1 = region1.getMinimumPoint();
        BlockVector3 max1 = region1.getMaximumPoint();
        BlockVector3 min2 = regionB.getMinimumPoint();
        BlockVector3 max2 = regionB.getMaximumPoint();
    
        return !(min1.getX() > max2.getX() || max1.getX() < min2.getX() || // X-axis
                 min1.getY() > max2.getY() || max1.getY() < min2.getY() || // Y-axis
                 min1.getZ() > max2.getZ() || max1.getZ() < min2.getZ());  // Z-axis
    }

    private RegionManager getRegions(Player player){
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return container.get(w);
    }
}
