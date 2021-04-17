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

public class tag implements CommandExecutor{
	Jogadores J;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("tag")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			J = Sistemas.getJogador(id.toString());
			String[] result = J.getTime().split(",");
			if (args.length == 0) {
				player.chat("/tagmenu");
				return true;
			}
			else if (args[0].equals("del")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_SP");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_ES");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_RJ");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_MG");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_SUL");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_NE");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
				+ " permission unset prefix.0.&9Construtor_CO");
				player.sendMessage(ChatColor.GREEN + "Tag removida!");
				return true;
			}
			else {
				for (int g = 0;g<result.length;g++) {
					if (result[g].equals(args[0])) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_SP");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_ES");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_RJ");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_MG");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_SUL");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_NE");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission unset prefix.0.&9Construtor_CO");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + J.getUUID()
						+ " permission set prefix.0.&9Construtor_"+args[0].toUpperCase());
						player.sendMessage(ChatColor.GREEN + "Tag adicionada!");
						return true;
					}
				}
				player.sendMessage(ChatColor.RED + "Você não é um construtor do time selecionado!");
				return true;
			}
			
			
		}
		return false;
	}
	

}
