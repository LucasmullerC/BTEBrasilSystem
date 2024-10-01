package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.service.claim.MapService;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class map implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
            if (sender instanceof ConsoleCommandSender) {
                MapService mapService = new MapService();
                mapService.generateMap();
                System.out.println("Mapa Gerado!");
                return true;
            } else {
                Player player = (Player) sender;
                player.sendMessage(Component.text(MessageUtils.getMessage("consoleCommand", player)).color(NamedTextColor.RED));
            }
            return true;
    }
    
}
