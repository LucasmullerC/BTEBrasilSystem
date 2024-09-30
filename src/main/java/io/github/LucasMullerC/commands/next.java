package io.github.LucasMullerC.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class next implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        ApplicantService applicantService = new ApplicantService();
        Applicant applicant = applicantService.getApplicant(id.toString());
        if(applicant == null){
            player.sendMessage(Component.text(MessageUtils.getMessage("AppProx", player)).color(NamedTextColor.GOLD));
            return true;
        } else{
            ApplicationZoneService applicationZoneService = new ApplicationZoneService();
            ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            Location location;
            switch (applicant.getSection()) {
                case "a":
                    applicant.setSection("b");
                    applicationZone.setIsAtZoneA(false);
                    applicationZone.setIsAtZoneb(true);
                    location = applicationZone.getLocationB();
                    scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                            player.sendMessage(Component.text(MessageUtils.getMessage("AppSec2", player)).color(NamedTextColor.GOLD));
                            DiscordActions.sendPrivateMessage(applicant.getDiscord(),MessageUtils.getMessage("AppSec2", player));
                        }
                    }, 40L);
                    break;
                case "b":
                    applicant.setSection("c");
                    applicationZone.setIsAtZoneb(false);
                    applicationZone.setIsAtZonec(true);
                    location = applicationZone.getLocationC();
                    scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(Component.text(MessageUtils.getMessage("AppSec3", player)).color(NamedTextColor.GOLD));
                        player.sendMessage(Component.text(MessageUtils.getMessage("AppTerr", player)).color(NamedTextColor.YELLOW));
                        DiscordActions.sendPrivateMessage(applicant.getDiscord(),MessageUtils.getMessage("AppSec3", player));
                        }
                    }, 40L);
                    break;
                case "c":
                    applicant.setSection("d");
                    applicationZone.setIsAtZonec(false);
                    applicationZone.setIsAtZoned(true);
                    location = applicationZone.getLocationD();
                    scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(Component.text(MessageUtils.getMessage("AppSec4", player)).color(NamedTextColor.GOLD));
                        player.sendMessage(Component.text(MessageUtils.getMessage("GoogleMaps", player)).color(NamedTextColor.GOLD));
                        player.sendMessage(Component.text(MessageUtils.getMessage("AppSec5", player)).color(NamedTextColor.GOLD));
                        DiscordActions.sendPrivateMessage(applicant.getDiscord(),MessageUtils.getMessage("AppSec4", player));
                        DiscordActions.sendPrivateMessage(applicant.getDiscord(),MessageUtils.getMessage("GoogleMaps", player));
                        DiscordActions.sendPrivateMessage(applicant.getDiscord(),MessageUtils.getMessage("AppSec5", player));
                        }
                    }, 40L);
                    break;
                default:
                    player.sendMessage(Component.text(MessageUtils.getMessage("AppCont", player)).color(NamedTextColor.GOLD));
                    return true;
            }
            applicantService.saveApplicant();
            applicationZoneService.saveApplicationZone();
            player.teleport(location);
            return true;
        }
    }
    
}
