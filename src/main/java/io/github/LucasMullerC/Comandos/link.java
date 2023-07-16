package io.github.LucasMullerC.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class link implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player) sender;
        if (args[0] == null) {
            return false;
        }
        /*
         * String msg = "";
         * for (int i = 0; i < args.length; ++i) {
         * msg = msg + args[i] + " ";
         * }
         * msg = msg.trim();
         * String Discord = DiscordPonte.getDiscordByName(msg);
         * DiscordPonte.sendMessage(Discord, Mensagens.LinkDiscord);
         */
        player.chat("/discord link");
        return true;

    }

}
