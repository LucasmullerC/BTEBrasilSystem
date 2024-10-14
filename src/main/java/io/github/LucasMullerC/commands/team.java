package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class team implements CommandExecutor{
    
    @Override
    public boolean onCommand(@NotNull CommandSender arg0, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        if ((arg0 instanceof Player)) {
            Player player = (Player) arg0;
            if (player.hasPermission("group.app") == false){
                player.sendMessage(Component.text(MessageUtils.getMessage("AppProx", player)).color(NamedTextColor.RED));
                return true;
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("clickevent", player))
                .clickEvent(ClickEvent.openUrl(MessageUtils.getMessage("teamurl", player)))
                .color(NamedTextColor.BLUE));
                return true;
            }
        }
        return false;
    }
    
}
