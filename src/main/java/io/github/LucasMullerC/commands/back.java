package io.github.LucasMullerC.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class back implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        ApplicantService applicantService = new ApplicantService();
        Applicant applicant = applicantService.getApplicant(id.toString());
        if(applicant == null){
            player.sendMessage(Component.text(MessageUtils.getMessage("NenhumaAplicacao", player)).color(NamedTextColor.GOLD));
            return true;
        } else{
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            player.sendMessage(Component.text(MessageUtils.getMessage("AppRemind", player)).color(NamedTextColor.GOLD));
            ApplicationZoneService applicationZoneService = new ApplicationZoneService();
            ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());
            Location location;
            switch (applicant.getSection()) {
                case "a":
                location = applicationZone.getLocationA();
                break;
                case "b":
                location = applicationZone.getLocationB();
                break;
                case "c":
                location = applicationZone.getLocationC();
                break;
                case "d":
                location = applicationZone.getLocationD();
                break;
                default:
                return false;
            }
            player.teleport(location);
            return true;
        }
    }
    
}
