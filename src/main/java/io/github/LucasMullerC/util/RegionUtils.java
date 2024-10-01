package io.github.LucasMullerC.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.fastasyncworldedit.core.FaweAPI;
import com.noahhusby.sledgehammer.datasets.projection.GeographicProjection;
import com.noahhusby.sledgehammer.datasets.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.datasets.projection.ScaleProjection;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;

public class RegionUtils {
    private static final GeographicProjection projection;
    private static final GeographicProjection uprightProj;
    private static final ScaleProjection scaleProj;

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

    public static ProtectedRegion getRegion(World world,String regionId){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        return regions.getRegion(regionId);
    }

    public static void loadSchematic(Player player, Location location) throws FileNotFoundException, IOException {
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

    public static double[] toGeo(final double x, final double z) {
        return scaleProj.toGeo(x, z);
    }

    public static double[] fromGeo(final double lon, final double lat) {
        return scaleProj.fromGeo(lon, lat);
    }

    static {
        projection = (GeographicProjection) new ModifiedAirocean();
        uprightProj = GeographicProjection.orientProjection(projection,
                GeographicProjection.Orientation.upright);
        scaleProj = new ScaleProjection(uprightProj, 7318261.522857145, 7318261.522857145);
    }
}
