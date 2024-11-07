package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class profile implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
                Player player = (Player) sender;
                player.sendMessage(Component.text(MessageUtils.getMessage("profile1", player)+" ").color(NamedTextColor.BLUE).
                append(Component.text(player.getName()).color(NamedTextColor.GREEN)));
                // ADMIN
                if (player.hasPermission("group.administrator")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("admin", player)+" ").color(NamedTextColor.DARK_RED).
                    append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                }
                // MODERADOR
                if (player.hasPermission("group.moderator")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("moderator", player)+" ").color(NamedTextColor.GREEN).
                    append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                }
                // SUPORTE
                if (player.hasPermission("group.helper")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("support", player)+" ").color(NamedTextColor.YELLOW).
                    append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                }
                // REVIEWER
                if (player.hasPermission("group.reviewer")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("reviewerprofile", player)+" ").color(NamedTextColor.BLUE).
                    append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                }
                // APOIADOR
                if (player.hasPermission("group.apoiador")) {
                    player.sendMessage(Component.text(MessageUtils.getMessage("donator", player)+" ").color(NamedTextColor.LIGHT_PURPLE).
                    append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                }
                // CONSTRUTOR
                if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()) != null) {
                    if (player.hasPermission("group.b_br") || player.hasPermission("group.builder_not")) {
                        player.sendMessage(Component.text(MessageUtils.getMessage("builderprofile", player)+" ").color(NamedTextColor.AQUA).
                        append(Component.text(MessageUtils.getMessage("check", player)).color(NamedTextColor.GOLD)));
                    }
                    return true;
                } else {
                    player.sendMessage(Component.text(MessageUtils.getMessage("PerfilNotBuilder", player)).color(NamedTextColor.GOLD));
                    return true; 
                }
    }
    
}
