package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Jogadores;

public class addequipe implements CommandExecutor {
	Jogadores J;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("addequipe")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "/addequipe Nick_Mine");
				player.sendMessage(ChatColor.RED + "O jogador deve estar Online!");
				return true;
			} else if (args[0].equalsIgnoreCase(Sistemas.getPlayerbyname(args[0]))) {
				J = Sistemas.getEquipe(id.toString());
				Player p = Bukkit.getPlayer(args[0]);
				UUID id2 = p.getUniqueId();
				Boolean vef = Sistemas.addEquipe(id2.toString(), J.getTime());
				if (vef == true) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " group add reviewer");
					player.sendMessage(ChatColor.GREEN + "Jogador adicionado a equipe!");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "O Jogador já faz parte de uma equipe!");
					return true;
				}
			} else if (args[0] != null) { // addequipe time nick
				if (player.hasPermission("btebrasil.adm")) {
					if (args[1].equalsIgnoreCase(Sistemas.getPlayerbyname(args[1]))) {
						Player p = Bukkit.getPlayer(args[1]);
						UUID id2 = p.getUniqueId();
						Boolean vef = Sistemas.addEquipe(id2.toString(), args[0]);
						if (vef == true) {
							player.sendMessage(ChatColor.GREEN + "Jogador adicionado a equipe!");
							return true;
						} else {
							player.sendMessage(ChatColor.RED + "O Jogador já faz parte de uma equipe!");
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED + "Player não identificado.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando!");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.GOLD + "/addequipe Nick_Mine");
				player.sendMessage(ChatColor.RED + "O jogador deve estar Online!");
				return true;
			}

		}
		return false;
	}

}
