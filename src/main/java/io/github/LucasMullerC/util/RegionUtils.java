package io.github.LucasMullerC.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fastasyncworldedit.core.FaweAPI;
import com.noahhusby.sledgehammer.datasets.projection.GeographicProjection;
import com.noahhusby.sledgehammer.datasets.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.datasets.projection.ScaleProjection;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
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
            BlockVector3 minPoint = region.getMinimumPoint();
            BlockVector3 maxPoint = region.getMaximumPoint();

            List<BlockVector2> polygonPoints = new ArrayList<>();
            polygonPoints.add(BlockVector2.at(minPoint.getX(), minPoint.getZ())); // Inferior esquerdo
            polygonPoints.add(BlockVector2.at(maxPoint.getX(), minPoint.getZ())); // Inferior direito
            polygonPoints.add(BlockVector2.at(maxPoint.getX(), maxPoint.getZ())); // Superior direito
            polygonPoints.add(BlockVector2.at(minPoint.getX(), maxPoint.getZ())); // Superior esquerdo

            String pontos = polygonPoints.toString().replaceAll("[\\[\\](){}]", "");
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

    public static boolean copyClaim(String claimId,Player player,Location location){
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(player.getWorld());
        ProtectedRegion protectedRegion = getRegion(w,claimId);
        if(protectedRegion!= null){
            Region region = convertProtectedRegionToRegion(protectedRegion, w);
            //BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(w)) {
                int surfaceY = getSurfaceY(region.getMaximumPoint(), w);
                //int surfaceY = region.getMaximumPoint().getBlockY();

                if (surfaceY == -1) {
                    player.sendMessage(MessageUtils.getMessage("joinclaimerror2", player));
                    return false;
                }

                int yOffset = -25 - surfaceY;
                //System.out.println(yOffset);
                BlockVector3 target = BlockVector3.at(location.getX(), yOffset, location.getZ());
                RegionMask regionMask = new RegionMask(region);

                ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    w,    
                    region,        
                    editSession,   
                    target        
                );

                forwardExtentCopy.setSourceMask(regionMask);
                forwardExtentCopy.setSourceMask(new BlockTypeMask(w, BlockTypes.STONE).inverse());
                forwardExtentCopy.setCopyingEntities(true); 
                //forwardExtentCopy.setSourceMask(new BlockTypeMask(w, BlockTypes.AIR));
                
                Operations.complete(forwardExtentCopy); 

                editSession.close();
            }
            return true;
        } else{
            return false;
        } 
    }

    public static boolean updateClaim(String claimId,Player player,String copyId, int totalSize){
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(player.getWorld());
        ProtectedRegion protectedRegion = getRegion(w,copyId);
        ProtectedRegion protectedClaimRegion = getRegion(w,claimId);
        if(protectedClaimRegion != null){
            Region region = convertProtectedRegionToRegion(protectedRegion, w);
            Region originalRegion = convertProtectedRegionToRegion(protectedClaimRegion, w);
            //BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(w)) {
                int surfaceY = getSurfaceY(originalRegion.getMaximumPoint(), w);
                //int surfaceY = region.getMaximumPoint().getBlockY();

                if (surfaceY == -1) {
                    player.sendMessage(MessageUtils.getMessage("joinclaimerror2", player));
                    return false;
                }
                int radius = 20 + totalSize;
                int yOffset = surfaceY - 39;

                BlockVector3 target = BlockVector3.at(originalRegion.getMinimumPoint().getX() - radius, yOffset, originalRegion.getMinimumPoint().getZ() - radius);
                
                ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    w,    
                    region,        
                    editSession,   
                    target        
                );
                
                forwardExtentCopy.setSourceMask(new BlockTypeMask(w, BlockTypes.AIR).inverse());
                forwardExtentCopy.setCopyingEntities(true);
                
                //forwardExtentCopy.setSourceMask(new BlockTypeMask(w, BlockTypes.AIR));
                Operations.complete(forwardExtentCopy);  

                editSession.setBlocks(region, BukkitAdapter.adapt(Material.AIR.createBlockData()));
                
                editSession.close();
            } catch (Exception e) {
                System.out.println("Erro updateClaim: "+e);
            }
            return true;
        } else{
            return false;
        } 
    }

    public static boolean deleteCopyClaim(String copyId, Player player){
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(player.getWorld());
        ProtectedRegion protectedRegion = getRegion(w,copyId);
        if(protectedRegion != null){
            Region region = convertProtectedRegionToRegion(protectedRegion, w);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(w)) {
                editSession.setBlocks(region, BukkitAdapter.adapt(Material.AIR.createBlockData()));
                
                editSession.close();
            }
            return true;
        } else{
            return false;
        } 
    }

    public static Location getRegionCopyLocation(String claimId,Player player){
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(player.getWorld());
        ProtectedRegion protectedRegion = getRegion(w,claimId);
        if(protectedRegion!= null){
            Region region = convertProtectedRegionToRegion(protectedRegion, w);

            BlockVector3 min = region.getMinimumPoint();
            BlockVector3 max = region.getMaximumPoint();
    
            double centerX = (min.getX() + max.getX()) / 2.0;
            double centerZ = (min.getZ() + max.getZ()) / 2.0;
            double centerY = 45;
    
            return new Location(player.getWorld(), centerX, centerY, centerZ);
        } else {
            return null;
        }
    }

    public static int[] getRegionDimensions(String claimId,Player player){
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(player.getWorld());
        ProtectedRegion protectedRegion = getRegion(w,claimId);
        if(protectedRegion!= null){
            Region region = convertProtectedRegionToRegion(protectedRegion, w);
            int width = region.getWidth();
            int length = region.getLength();
            return new int[]{width, length};
        } else{
            return null;
        }
    }

    private static Region convertProtectedRegionToRegion(ProtectedRegion protectedRegion, com.sk89q.worldedit.world.World world) {
        if (protectedRegion.getPoints().size() < 0) {
            BlockVector3 min = protectedRegion.getMinimumPoint();
            BlockVector3 max = protectedRegion.getMaximumPoint();
            return new CuboidRegion(world, min, max);
        } else {
            return new Polygonal2DRegion(world, protectedRegion.getPoints(), protectedRegion.getMinimumPoint().getBlockY(), protectedRegion.getMaximumPoint().getBlockY());
        }
    }

    public static double getDistance(String claimId, org.bukkit.World world) {
        com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(world);
        ProtectedRegion protectedRegion = getRegion(w, claimId);
        Integer distanceX = protectedRegion.getMaximumPoint().getBlockX() - protectedRegion.getMinimumPoint().getBlockX();
        Integer distanceZ = protectedRegion.getMaximumPoint().getBlockZ() - protectedRegion.getMinimumPoint().getBlockZ();
        return distanceX + distanceZ;
    }

    private static int getSurfaceY(BlockVector3 location, com.sk89q.worldedit.world.World world) {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            for (int y = 3000; y >= 0; y--) {
                BlockVector3 position = BlockVector3.at(location.getBlockX(), y, location.getBlockZ());
                if (!editSession.getBlock(position).getBlockType().getMaterial().isAir()) {
                    return y;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 se nenhuma superfície for encontrada
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
