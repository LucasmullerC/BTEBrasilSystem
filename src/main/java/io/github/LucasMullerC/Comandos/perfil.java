package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;

public class perfil implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Aplicantes A;
		Jogadores J,E;
		if (command.getName().equalsIgnoreCase("perfil")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			player.sendMessage(ChatColor.BLUE + "PERFIL - "+ChatColor.GREEN+player.getDisplayName());
			if (Sistemas.getEquipe(id.toString()) != null) {
				E = Sistemas.getEquipe(id.toString());			
				if (E.getTime().equals("ne")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Nordeste");
				}
				else if(E.getTime().equals("sp")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time São Paulo");
				}
				else if(E.getTime().equals("mg")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Minas Gerais");
				}
				else if(E.getTime().equals("es")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Espírito Santo");
				}
				else if(E.getTime().equals("rj")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Rio de Janeiro");
				}
				else if(E.getTime().equals("sul")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Sul");
				}
				else if(E.getTime().equals("co")) {
					player.sendMessage(ChatColor.RED + "EQUIPE - "+ChatColor.GOLD+"Time Centro-Oeste");
				}
			}
			if (Sistemas.getJogador(id.toString()) != null) {
				J = Sistemas.getJogador(id.toString());			
				String times = J.getTime();
				String[] parts = times.split(",");
				String motivo = "";
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].equals("ne")) {
						motivo +=  "Time Nordeste,";
					}
					else if(parts[i].equals("sp")) {
						motivo +=  "Time São Paulo,";
					}
					else if(parts[i].equals("mg")) {
						motivo +=  "Time Minas Gerais,";
					}
					else if(parts[i].equals("es")) {
						motivo +=  "Time Espírito Santo,";
					}
					else if(parts[i].equals("rj")) {
						motivo +=  "Time Rio de Janeiro,";
					}
					else if(parts[i].equals("sul")) {
						motivo +=  "Time Sul,";
					}
					else if(parts[i].equals("co")) {
						motivo +=  "Time Centro-Oeste,";
					}
				}
				motivo = motivo.trim();
				player.sendMessage(ChatColor.AQUA + "CONSTRUTOR - "+ChatColor.GOLD+motivo);
			}
			else {
				player.sendMessage(ChatColor.GOLD + "Você não é construtor em nenhum time :(, Porque não se junta á nós?");
			}
			return true;
		}
		return false;
	}
	

}
