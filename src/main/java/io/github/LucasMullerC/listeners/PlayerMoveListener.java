package io.github.LucasMullerC.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.github.LucasMullerC.service.LuckpermsService;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlock().getX()
                || event.getFrom().getBlockZ() != event.getTo().getBlock().getZ()) {
            Player player = event.getPlayer();
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
            ApplicableRegionSet playerRegion = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery()
                    .getApplicableRegions(loc);

            // Verify if player is inside their claim
            if (playerRegion != null) {
                if (playerRegion.getRegions().size() < 1) {
                    LuckpermsService luckpermsService = new LuckpermsService();
                    if (player.hasPermission("group.builder_not")) {
                        luckpermsService.removeGroup(player, "builder_not");
                        luckpermsService.addGroup(player, "b_br");
                        if (player.hasPermission("group.app")) {
                            luckpermsService.removeGroup(player, "app");
                        }
                        return;
                    } else if (player.hasPermission("group.app")) {
                        luckpermsService.removeGroup(player, "app");
                        if(player.getGameMode() == GameMode.CREATIVE){
                            player.setGameMode(GameMode.SPECTATOR);
                        }
                    }
                } else {
                    for (ProtectedRegion region : playerRegion.getRegions()) {
                        for (UUID uuids : region.getMembers().getUniqueIds()) {
                            if (player.getUniqueId().equals(uuids)) {
                                if (player.hasPermission("group.builder_not")) {
                                    LuckpermsService luckpermsService = new LuckpermsService();
                                    luckpermsService.removeGroup(player, "builder_not");
                                    luckpermsService.addGroup(player, "b_br");
                                    if (player.hasPermission("group.app")) {
                                        luckpermsService.removeGroup(player, "app");
                                    }
                                    return;
                                } else if (!player.hasPermission("group.builder_not")
                                        && !player.hasPermission("group.b_br")) {
                                    LuckpermsService luckpermsService = new LuckpermsService();
                                    luckpermsService.addGroup(player, "app");
                                    if(player.getGameMode() == GameMode.SPECTATOR){
                                        player.setGameMode(GameMode.CREATIVE);
                                    }
                                    return;
                                } else {
                                    return;
                                }
                            }

                        }
                        if (player.hasPermission("group.b_br")) {
                            LuckpermsService luckpermsService = new LuckpermsService();
                            luckpermsService.addGroup(player, "builder_not");
                            luckpermsService.removeGroup(player, "b_br");
                            if (player.hasPermission("group.app")) {
                                luckpermsService.removeGroup(player, "app");
                            }
                            return;
                        }
                    }
                    if (player.hasPermission("group.app")) {
                        LuckpermsService luckpermsService = new LuckpermsService();
                        luckpermsService.removeGroup(player, "app");
                        if(player.getGameMode() == GameMode.CREATIVE){
                            player.setGameMode(GameMode.SPECTATOR);
                        }
                    }
                }
            }
        }
    }
}
