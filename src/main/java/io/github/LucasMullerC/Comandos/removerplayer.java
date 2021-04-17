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

public class removerplayer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Jogadores J;
		if (command.getName().equalsIgnoreCase("removerplayer")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "/removerplayer UUID");
				return true;
			} else if ( Sistemas.getJogador(args[0]) != null) {			
				String[] result = Sistemas.getJogador(args[0]).getTime().split(",");
				for (int i = 0;i<result.length;i++) {
					if (result[i].equals(Sistemas.getEquipe(id.toString()).getTime())) {
						if (result[i].equals("ne")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtorne");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_NE");
						} else if (result[i].equals("sp")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtorsp");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_SP");
						} else if (result[i].equals("sul")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtorsul");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_SUL");
						} else if (result[i].equals("co")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtorco");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_CO");
						} else if (result[i].equals("mg")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtormg");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_MG");
						} else if (result[i].equals("rj")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtorrj");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_RJ");
						} else if (result[i].equals("es")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"lp user " + args[0] + " group remove construtores");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
							+ " permission unset prefix.0.&9Construtor_ES");
						}
						J = Sistemas.getJogador(args[0]);
						if (i > 0) {
							String times = "";
							for (int g = 0;g<result.length;g++) {
								if (i != g) {
									times += result[g] + ",";
								}
							}
							J.setTime(times);
						}
						else {
							DiscordPonte.removerCargo(args[0], Sistemas.getEquipe(id.toString()).getTime(), J.getDiscord());
							Sistemas.RemoverJogador(args[0]);
						}
						player.sendMessage(ChatColor.GREEN + "O Jogador foi removido do seu time.");
						return true;
					}
				}
				player.sendMessage(ChatColor.RED + "Este jogador não é um construtor no seu time!");
				return true;
				
			}
			player.sendMessage(ChatColor.RED + "O Jogador não foi encontrado.");
			return true;
		}
		return false;
	}

}
