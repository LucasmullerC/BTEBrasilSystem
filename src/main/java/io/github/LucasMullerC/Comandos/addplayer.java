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
import io.github.LucasMullerC.Objetos.Jogadores;

public class addplayer implements CommandExecutor {
	Jogadores J, E;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("addplayer")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			String msg = "";
			for (int i = 0; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg = msg.trim();
			String[] arrayValores = msg.split(" ");
			if (args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "/addplayer UUID Nick_do_Discord_do_Player");
				return true;
			} else if (arrayValores[0] != null) {
				String discord = "";
				for (int i = 1; i <= arrayValores.length; i++) {
					discord += arrayValores[i] + " ";
				}
				discord = discord.trim();
				String discordid = DiscordPonte.CheckDiscord(discord);
				if (discordid.equals("nulo") || discordid == null) {
					player.sendMessage(ChatColor.RED
							+ "Discord não identificado, Verifique se ele está conectado em nosso servidor ou se você colocou o nome e o código exato.");
					return true;
				} else {
					if (Sistemas.getJogador(arrayValores[0]) != null) {
						String[] result = Sistemas.getJogador(arrayValores[0]).getTime().split(",");
						for (int i = 0;i<result.length;i++) {
							if (result[i].equals(Sistemas.getEquipe(id.toString()).getTime())) {
								player.sendMessage(ChatColor.YELLOW + "Este jogador já é um construtor no seu time!");
								return true;
							}
						}
					}
					String time =  Sistemas.getEquipe(id.toString()).getTime();
					if (time.equals("ne")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtorne");
					} else if (time.equals("sp")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtorsp");
					} else if (time.equals("sul")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtorsul");
					} else if (time.equals("co")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtorco");
					} else if (time.equals("mg")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtormg");
					} else if (time.equals("rj")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtorrj");
					} else if (time.equals("es")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + arrayValores[0] + " group add construtores");
					}
					Sistemas.addJogador(arrayValores[0], Sistemas.getEquipe(id.toString()).getTime(), discordid);
					DiscordPonte.addCargo(arrayValores[0], Sistemas.getEquipe(id.toString()).getTime(), discordid);
					player.sendMessage(ChatColor.GREEN + "Jogador adicionado!");
					return true;
				}
			}
		}
		return false;
	}

}
