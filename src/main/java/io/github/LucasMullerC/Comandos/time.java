package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;

public class time implements CommandExecutor {
	Aplicantes A;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		UUID id = player.getUniqueId();
		if (command.getName().equalsIgnoreCase("time")) {
			A = Sistemas.getAplicante(id.toString());
			if (A != null) {
				if (A.getTime().equals("ne")) {
					String txt = "https://buildtheearth.net/buildteams/217/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("sp")) {
					String txt = "https://buildtheearth.net/buildteams/45/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("sul")) {
					String txt = "https://buildtheearth.net/buildteams/233/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("co")) {
					String txt = "https://buildtheearth.net/buildteams/239/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("mg")) {
					String txt = "https://buildtheearth.net/buildteams/140/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("es")) {
					String txt = "https://buildtheearth.net/buildteams/246/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (A.getTime().equals("rj")) {
					String txt = "https://buildtheearth.net/buildteams/228/join";
					player.sendMessage(ChatColor.BLUE + txt);
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED + "Você não está em aplicação!");
				return true;
			}
		}
		return false;
	}

}
