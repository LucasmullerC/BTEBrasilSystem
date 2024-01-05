package io.github.LucasMullerC.util;

import java.util.ArrayList;
import java.util.List;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

public class RegionUtils {

    public static String convertToPolygonal2DRegion(Region region) {
        if (region instanceof Polygonal2DRegion) {
            Polygonal2DRegion polygonalRegion = (Polygonal2DRegion) region; // If the region is already a
                                                                            // Polygonal2DRegion, simply return it
            List<BlockVector2> points = polygonalRegion.getPoints();
            String pontos = points.toString().replaceAll("[\\[\\](){}]", "");
            return pontos.replaceAll(" ", "");
        } else {
            List<BlockVector3> points = new ArrayList<BlockVector3>();
            points.add(region.getMinimumPoint());
            points.add(region.getMaximumPoint());
            String pontos = points.toString().replaceAll("[\\[\\](){}]", "");
            return pontos.replaceAll(" ", "");
        }
    }

    public static int getSelectionDimension(LocalSession localSession, World world, String dimension) {
        BlockVector3 min = localSession.getRegionSelector(world).getRegion().getMinimumPoint();
        BlockVector3 max = localSession.getRegionSelector(world).getRegion().getMaximumPoint();

        int dimensionValue;
        switch (dimension) {
            case "WIDTH":
                dimensionValue = Math.abs(max.getBlockX() - min.getBlockX()) + 1;
                break;
            case "LENGTH":
                dimensionValue = Math.abs(max.getBlockZ() - min.getBlockZ()) + 1;
                break;
            default:
                dimensionValue = 0;
                break;
        }

        return dimensionValue;
    }
}
