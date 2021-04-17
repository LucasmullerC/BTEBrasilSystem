package io.github.LucasMullerC.Comandos;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Zonas;

public class addzona implements CommandExecutor {
	ArrayList<Zonas> z;
	Zonas zn;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("addzona")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			Sistemas.addZonas(player);
			player.sendMessage(ChatColor.GOLD + "Área adicionada!");
		}
		return false;
	}
	

}
