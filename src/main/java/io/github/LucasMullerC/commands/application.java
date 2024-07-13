package io.github.LucasMullerC.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.service.LuckpermsService;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.ZoneUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class application implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        Player player = (Player) sender;
        if (player.hasPermission("group.b_br") || player.hasPermission("group.builder_not") == true) {
            player.sendMessage(Component.text(MessageUtils.getMessage("Isconstrutor", player)).color(NamedTextColor.RED));
            return true;
        }
        else{
            ApplicantService applicantService = new ApplicantService();
            UUID id = player.getUniqueId();
            String discordId = DiscordActions.getDiscordId(id);
            if(applicantService.getApplicant(id.toString()) != null){ //Already applying to the team
                player.sendMessage(Component.text(MessageUtils.getMessage("AplicacaoAndamento", player)).color(NamedTextColor.RED));
                player.sendMessage(Component.text(MessageUtils.getMessage("AplicacaoAndamento2", player)).color(NamedTextColor.GOLD));
                return true;
            } else{
                if (discordId == null) { //did not linked discord
                    player.getPlayer().sendMessage(Component.text(MessageUtils.getMessage("link1", player.getPlayer()))
                    .color(NamedTextColor.RED));

                    player.getPlayer()
                            .sendMessage(Component.text(MessageUtils.getMessage("Link2", player.getPlayer()))
                                    .color(NamedTextColor.YELLOW)
                                    .append(Component.text(MessageUtils.getMessage("InviteDiscord", player.getPlayer()))
                                            .color(NamedTextColor.BLUE)));
                    player.getPlayer()
                            .sendMessage(Component.text(MessageUtils.getMessage("Link3", player.getPlayer()))
                                    .color(NamedTextColor.YELLOW)
                                    .append(Component.text(MessageUtils.getMessage("Link4", player.getPlayer()))
                                            .color(NamedTextColor.BLUE)
                                            .append(Component.text(MessageUtils.getMessage("Link5", player.getPlayer()))
                                                    .color(NamedTextColor.YELLOW))));
                    return true;
                } else{
                    if (DiscordActions.CheckDiscord(discordId) == true) {
                        ApplicationZone applicationZone = ZoneUtils.buildApplicationZone(player);
                        ApplicationZoneService applicationZoneService = new ApplicationZoneService();
                        applicationZoneService.addAppicationZone(applicationZone);

                        Applicant applicant = new Applicant(player.getUniqueId().toString());
                        applicant.setDiscord(discordId);
                        applicant.setSection("a");
                        applicant.setgetZone(applicationZone.getApplicationZone());
                        applicant.setDeadline(createDeadline());
                        applicantService.addApplicant(applicant);

                        player.teleport(applicationZone.getLocationA());

                        startApplication(player, discordId);

                    }else{
                        player.sendMessage(Component.text(MessageUtils.getMessage("NotDiscord", player)).color(NamedTextColor.YELLOW));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void startApplication(Player player, String Discord) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
        player.getInventory().addItem(new ItemStack(Material.getMaterial("WOOD_AXE"), 1));
        player.sendMessage(Component.text(MessageUtils.getMessage("AppInit", player.getPlayer())).color(NamedTextColor.GREEN));
        player.sendMessage(Component.text(MessageUtils.getMessage("AppSec1", player.getPlayer())).color(NamedTextColor.GOLD));
        DiscordActions.sendPrivateMessage(Discord, MessageUtils.getMessage("AppSec1", player.getPlayer()));
    }

    private String createDeadline(){
        LocalDate deadline = LocalDate.now().plusDays(3); // x = 10
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return deadline.format(formatter);
    }
    
}
