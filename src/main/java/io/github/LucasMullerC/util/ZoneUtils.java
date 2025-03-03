package io.github.LucasMullerC.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.service.LuckpermsService;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;

public class ZoneUtils {
    public static Location buildClaimCopy(Player player, int startX, int startY, int startZ,String claimName){
                int x = startX;
                int y = startY;
                int z = startZ;

                int[] dimensions = RegionUtils.getRegionDimensions(claimName, player);
                if (dimensions == null) {
                    return null;
                }
                org.bukkit.World w = player.getWorld();
                Location finalLoc;

                int regionSize = 20 + Math.max(dimensions[0], dimensions[1]); // Usa a mesma lógica do totalSize
                int xIncrement = regionSize + 50; // Dobra o tamanho para evitar sobreposição
                int zIncrement = regionSize + 50;

                int maxAttemptsPerLine = 5;
        
                int attempts = 0;
        
                while (true) {
                        WorldGuardService worldGuardService = new WorldGuardService();
                    if (!worldGuardService.isRegionIntersecting(player, x, y, z,regionSize)) {
                        finalLoc = new Location(w, x, y, z);     
                        int maxDimension = Math.max(dimensions[0], dimensions[1]);
                        worldGuardService.addClaimZone(player, finalLoc, claimName,maxDimension);   
                        return finalLoc;       
                    }
        
                    x += xIncrement;
                    attempts++;
        
                    if (attempts >= maxAttemptsPerLine) {
                        x = startX;
                        z += zIncrement;
                        attempts = 0;
                    }
                }
    }

    public static ApplicationZone buildApplicationZone(Player player){
        org.bukkit.World w = player.getWorld();
        ApplicationZone zoneold;
        ApplicationZone zonenew;
        Location L, lb, lc, ld, meio;
        Integer result = 1;
        Integer pos;
        ApplicationZoneService applicationZoneService = new ApplicationZoneService();
        if (!applicationZoneService.isEmpty()) {
            result = applicationZoneService.findAvailableZone();
        }
        if (applicationZoneService.isEmpty() == true || result == 1) {
            zonenew = new ApplicationZone("1");
            L = new Location(w, -648, 31, 289);
            lb = new Location(w, -653, 31, 289);
            lc = new Location(w, -653, 38, 284);
            ld = new Location(w, -648, 31, 284);
            meio = new Location(w, -650, 45, 287);
            // worldguard
            createRegion(1, player, false);
            try {
                RegionUtils.loadSchematic(player, meio);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (result == 0) {
                Integer zoneSize = applicationZoneService.getSize();
                Integer posterior = zoneSize;
                pos = zoneSize + 1;
                zoneold = applicationZoneService.getApplicationZone(posterior.toString());
                zonenew = new ApplicationZone(String.valueOf(pos));
            } else {
                Integer posterior = result - 1;
                zoneold = applicationZoneService.getApplicationZone(posterior.toString());
                pos = result;
                zonenew = new ApplicationZone(String.valueOf(pos));
            }
            if (pos == 6 || pos == 11 || pos == 16 || pos == 21 || pos == 26 || pos == 31 || pos == 36) {
                Integer posterior = pos - 5;
                zoneold = applicationZoneService.getApplicationZone(posterior.toString());
                L = new Location(w, zoneold.getLocationA().getX() + 90, zoneold.getLocationA().getY(), zoneold.getLocationA().getZ());
                lb = new Location(w, zoneold.getLocationB().getX() + 90, zoneold.getLocationB().getY(), zoneold.getLocationB().getZ());
                lc = new Location(w, zoneold.getLocationC().getX() + 90, zoneold.getLocationC().getY(), zoneold.getLocationC().getZ());
                ld = new Location(w, zoneold.getLocationD().getX() + 90, zoneold.getLocationD().getY(), zoneold.getLocationD().getZ());
                // worldguard
                createRegion(pos, player, true);
            } else {
                // Coordenadas
                L = new Location(w, zoneold.getLocationA().getX(), zoneold.getLocationA().getY(), zoneold.getLocationA().getZ() + 90);
                lb = new Location(w, zoneold.getLocationB().getX(), zoneold.getLocationB().getY(), zoneold.getLocationB().getZ() + 90);
                lc = new Location(w, zoneold.getLocationC().getX(), zoneold.getLocationC().getY(), zoneold.getLocationC().getZ() + 90);
                ld = new Location(w, zoneold.getLocationD().getX(), zoneold.getLocationD().getY(), zoneold.getLocationD().getZ() + 90);
                // worldguard
                createRegion(pos, player, false);
            }
            meio = new Location(w, L.getX() - 2, 45, L.getZ() - 2);
            try {
                RegionUtils.loadSchematic(player, meio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        zonenew.setName(player.getUniqueId().toString());
        zonenew.setIsBusy(true);
        zonenew.setIsAtZoneA(true);
        zonenew.setIsAtZoneb(false);
        zonenew.setIsAtZonec(false);
        zonenew.setIsAtZoned(false);
        // locs
        zonenew.setLocationA(L);
        zonenew.setLocationB(lb);
        zonenew.setLocationC(lc);
        zonenew.setLocationD(ld);

        LuckpermsService luckpermsService = new LuckpermsService();
        luckpermsService.addPermissionLuckPerms("apply" + zonenew.getApplicationZone() + "a", player.getUniqueId());
        luckpermsService.addPermissionLuckPerms("apply" + zonenew.getApplicationZone() + "b", player.getUniqueId());
        luckpermsService.addPermissionLuckPerms("apply" + zonenew.getApplicationZone() + "c", player.getUniqueId());
        luckpermsService.addPermissionLuckPerms("apply" + zonenew.getApplicationZone() + "d", player.getUniqueId());

        return zonenew;
    }

    public static void removeRegion(Player player, ApplicationZone applicationZone) {
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        
        World adaptedWorld = actor.getWorld();
        

        Location meio = new Location(player.getWorld(), applicationZone.getLocationA().getX() - 2, 45, applicationZone.getLocationA().getZ() - 2);
        BlockVector3 minPoint = BlockVector3.at(meio.getX() - 45, 60, meio.getZ() - 45);
        BlockVector3 maxPoint = BlockVector3.at(meio.getX() + 45, 25, meio.getZ() + 45);
        
        CuboidRegion selection = new CuboidRegion(minPoint, maxPoint);
        
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(adaptedWorld)
                .build()) {
            
            //editSession.setBlocks(selection, BlockTypes.AIR.getDefaultState());
            editSession.setBlocks((Region) selection, BukkitAdapter.adapt(Material.AIR.createBlockData()));
        }
    }

    private static void createRegion(Integer zoneNum,Player player, boolean up){
        WorldGuardService worldGuardService = new WorldGuardService();
        if (zoneNum == 1) {
            // 1a
            List<BlockVector2> points = Lists.newArrayList();
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

            worldGuardService.addApplicationZone(player, "apply1a", points, minY, maxY);

            // 1b
            points = Lists.newArrayList();
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

            worldGuardService.addApplicationZone(player, "apply1b", points, minY, maxY);

            // 1c
            points = Lists.newArrayList();
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

            worldGuardService.addApplicationZone(player, "apply1c", points, minY, maxY);

            // 1d
            points = Lists.newArrayList();
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

            worldGuardService.addApplicationZone(player, "apply1d", points, minY, maxY);
        }else {
            com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
            World world = playerbukkit.getWorld();
            Integer posteriorZone;
            if (up == true) {
                posteriorZone = zoneNum - 5;
            } else{ //down
                posteriorZone = zoneNum - 1;
            }
            ProtectedRegion regionposta = RegionUtils.getRegion(world,"apply" + posteriorZone.toString() + "a");
            ProtectedRegion regionpostb = RegionUtils.getRegion(world,"apply" + posteriorZone.toString() + "b");
            ProtectedRegion regionpostc = RegionUtils.getRegion(world,"apply" + posteriorZone.toString() + "c");
            ProtectedRegion regionpostd = RegionUtils.getRegion(world,"apply" + posteriorZone.toString() + "d");

            // a
            ProtectedPolygonalRegion polygona = (ProtectedPolygonalRegion) regionposta;
            List<BlockVector2> pointsaold = polygona.getPoints();
            List<BlockVector2> pointsanew = Lists.newArrayList(); 
            for (int i = 0; i < pointsaold.size(); i++) {
                pointsaold.get(i).getBlockX();
                pointsanew.add(BlockVector2.at(pointsaold.get(i).getBlockX() + 90,
                        pointsaold.get(i).getBlockZ()));
            }
            int minY = 31;
            int maxY = 42;

            worldGuardService.addApplicationZone(player, "apply" + zoneNum.toString() + "a",
                    pointsanew, minY, maxY);

            // b
            ProtectedPolygonalRegion polygonb = (ProtectedPolygonalRegion) regionpostb;
            List<BlockVector2> pointsbold = polygonb.getPoints();
            List<BlockVector2> pointsbnew = Lists.newArrayList();
            for (int i = 0; i < pointsbold.size(); i++) {
                pointsbold.get(i).getBlockX();
                pointsbnew.add(BlockVector2.at(pointsbold.get(i).getBlockX() + 90,
                        pointsbold.get(i).getBlockZ()));
            }
            minY = 31;
            maxY = 42;

            worldGuardService.addApplicationZone(player, "apply" + zoneNum.toString() + "b",
            pointsbnew, minY, maxY);

            // c
            ProtectedPolygonalRegion polygonc = (ProtectedPolygonalRegion) regionpostc;
            List<BlockVector2> pointscold = polygonc.getPoints();
            List<BlockVector2> pointscnew = Lists.newArrayList(); 
            for (int i = 0; i < pointscold.size(); i++) {
                pointscold.get(i).getBlockX();
                pointscnew.add(BlockVector2.at(pointscold.get(i).getBlockX() + 90,
                        pointscold.get(i).getBlockZ()));
            }
            minY = 30;
            maxY = 42;

            worldGuardService.addApplicationZone(player, "apply" + zoneNum.toString() + "c",
            pointscnew, minY, maxY);

            // d
            ProtectedPolygonalRegion polygond = (ProtectedPolygonalRegion) regionpostd;
            List<BlockVector2> pointsdold = polygond.getPoints();
            List<BlockVector2> pointsdnew = Lists.newArrayList();
            for (int i = 0; i < pointsdold.size(); i++) {
                pointsdold.get(i).getBlockX();
                pointsdnew.add(BlockVector2.at(pointsdold.get(i).getBlockX() + 90,
                        pointsdold.get(i).getBlockZ()));
            }
            minY = 31;
            maxY = 42;

            worldGuardService.addApplicationZone(player, "apply" + zoneNum.toString() + "d",
            pointsdnew, minY, maxY);
        }
    } 
}
