package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.service.LuckpermsService;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class tag implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
                Player player = (Player) sender;
                if (arg3.length == 0) {
                    player.chat("/tagmenu");
                    return true;
                }
                if (arg3[0].equalsIgnoreCase("construtor")) {
                    if(arg3.length <= 1){
                        return false;
                    }
                    LuckpermsService luckpermsService = new LuckpermsService();
                    if (arg3[1].equals("del")) {
                        luckpermsService.removeTagLuckPerms(player.getUniqueId());
                        player.sendMessage(Component.text(MessageUtils.getMessage("TagRemove", player)).color(NamedTextColor.GREEN));
                        return true;
                    } else {
                        if (player.hasPermission("group.b_" + arg3[1])) {
                            luckpermsService.addTagLuckPerms(player.getUniqueId(), "prefix.0.&9Construtor " + arg3[1].toUpperCase());
                            player.sendMessage(Component.text(MessageUtils.getMessage("Tagadd", player)).color(NamedTextColor.GREEN));
                            return true;
                        } else {
                            player.sendMessage(Component.text(MessageUtils.getMessage("Notconstrutor", player)).color(NamedTextColor.RED));
                            return true;
                        }
                    }
                } else if (arg3[0].equalsIgnoreCase("outros")) {
                    if(arg3.length <= 1){
                        return false;
                    }
                    LuckpermsService luckpermsService = new LuckpermsService();
                    if (arg3[1].equals("del")) {
                        luckpermsService.removeTagLuckPerms(player.getUniqueId());
                        player.sendMessage(Component.text(MessageUtils.getMessage("TagRemove", player)).color(NamedTextColor.GREEN));
                        return true;
                    } else {
                        if (player.hasPermission("group." + arg3[1])) {
                            luckpermsService.addTagLuckPerms(player.getUniqueId(), "prefix.0." + convertPerm(arg3[1]));
                            player.sendMessage(Component.text(MessageUtils.getMessage("Tagadd", player)).color(NamedTextColor.GREEN));
                            return true;
                        } else {
                            player.sendMessage(Component.text(MessageUtils.getMessage("Notperm", player)).color(NamedTextColor.RED));
                            return true;
                        }
                    }
                }
                return false;
    }

    private String convertPerm(String perm) {
        switch (perm) {
            case "helper":
                return "&eSuporte";
            case "apoiador":
                return "&dApoiador";
            case "bte_staff":
                return "&5BTE Staff";
            case "reviewer":
                return "&bReviewer";
            case "moderator":
                return "&2Moderador";
            case "administrator":
                return "&4Admin";

        }
        return perm;
    }
    
}
