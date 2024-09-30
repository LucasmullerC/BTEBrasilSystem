package io.github.LucasMullerC.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.ZoneUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class cancel implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        ApplicantService applicantService = new ApplicantService();
        Applicant applicant = null;
        if (arg3.length != 0) {
            if (player.hasPermission("btebrasil.lider")) {
                applicant = applicantService.getApplicant(arg3[0]);
                if (applicant == null || applicant.getgetZone().equals("nulo")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("NenhumaAplicacaoPlayer", player)).color(NamedTextColor.GOLD));
                    return true;
                }
            }
            else{
                player.sendMessage(Component.text(MessageUtils.getMessage("Perm1", player)).color(NamedTextColor.RED));
                return true;
            }
        }
        else{
            applicant = applicantService.getApplicant(id.toString());
        }
        if(applicant != null){
            ApplicationZoneService applicationZoneService = new ApplicationZoneService();
            ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());

            //removing Worldguard and Luckperms Regions
            WorldGuardService worldGuardService = new WorldGuardService();
            worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "d", player);
            worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "c", player);
            worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "b", player);
            worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "a", player);

            applicantService.removeApplicant(applicant);
            applicationZoneService.removeApplicationZone(applicationZone);

            PendingService pendingService = new PendingService();
            Pending pending = pendingService.getPendingApplication(applicant.getUUID());
            if(pending != null){
                pendingService.removePending(pending);
            }

            player.sendMessage(Component.text(MessageUtils.getMessage("ZonaDel", player)).color(NamedTextColor.RED));
            ZoneUtils.removeRegion(player, applicationZone);
            player.sendMessage(Component.text(MessageUtils.getMessage("ZonaDel1", player)).color(NamedTextColor.GREEN));

            // Teleporta Player
            World world = player.getWorld();
            Location l = new Location(world, -1163, 80, 300);
            player.teleport(l);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            PlayerInventory inventory = player.getInventory();
            inventory.clear();

            return true;
        }
        player.sendMessage(Component.text(MessageUtils.getMessage("ErroCancelar", player)).color(NamedTextColor.RED));
        return false;
    }
    
}
