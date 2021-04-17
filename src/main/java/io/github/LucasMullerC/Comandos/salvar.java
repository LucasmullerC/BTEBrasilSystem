package io.github.LucasMullerC.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;

public class salvar implements CommandExecutor{
	Sistemas S;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("salvar")) {
			final Player player = (Player)sender;
			S.equipe.save();
			S.aplicante.save();
			S.pendente.save();
			S.jogador.save();
			S.zonas.save();
			player.sendMessage(ChatColor.GREEN + "ReservasNE Salvo!");
        }
		return false;
	}
	

}
