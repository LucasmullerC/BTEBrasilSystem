package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.List;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class Regioes {

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
                                        pointsdnew.add(new BlockVector2D(pointsdold.get(i).getBlockX()+ 90,
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
