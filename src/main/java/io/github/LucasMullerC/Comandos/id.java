package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;

public class id implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("id")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "/id (Nick do jogador)");
				return true;
			} else {
				Player p;
				p = Bukkit.getPlayerExact(args[0]);
				if (p != null) {
					UUID id;
					id = p.getUniqueId();
					Player player = (Player) sender;
					UUID idp = player.getUniqueId();
					String txt = "O ID do jogador " + p.getDisplayName() + " é " + id.toString();
					player.sendMessage(ChatColor.RED + txt);
					if (Sistemas.getJogador(idp.toString()) == null) {
						player.sendMessage(ChatColor.RED + "Você precisar ser um jogador para usar este comando!");
					}
					else {
						DiscordPonte.sendMessage(Sistemas.getJogador(idp.toString()).getDiscord(), txt);
					}
					return true;
				} else {
					Player player = (Player) sender;
					player.sendMessage(ChatColor.RED + "O jogador não foi encontrado!");
					return true;
				}
			}
		}
		return false;
	}

}
