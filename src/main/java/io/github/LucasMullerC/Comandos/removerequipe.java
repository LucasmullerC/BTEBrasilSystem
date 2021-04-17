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

public class removerequipe implements CommandExecutor {
	Jogadores J;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("removerequipe")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (args.length == 0) {
				player.sendMessage(ChatColor.GOLD + "/removerequipe UUID");
				return true;
			}
		} else if ( Sistemas.getEquipe(args[0]) != null) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " group remove reviewer");
			Sistemas.RemoverEquipe(args[0]);
			sender.sendMessage(ChatColor.GREEN + "Jogador removido da sua equipe!");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Jogador não encontrado ou não faz parte da sua equipe!");
		return false;

	}
}
