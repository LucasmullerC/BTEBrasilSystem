package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Cargos;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Eventos;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;

public class link implements CommandExecutor {
	Aplicantes G;
	Jogadores J;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final Player player = (Player)sender;
        final UUID id = player.getUniqueId();
		BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        if (args[0] == null) {
            return false;
        }
        String msg = "";
        for (int i = 0; i < args.length; ++i) {
            msg = msg + args[i] + " ";
        }
        msg = msg.trim();
        this.J = Sistemas.getJogador(id.toString());
        if (Sistemas.getJogador(id.toString()) == null) {
        	J = Sistemas.addJogador(id.toString(),"nulo", "nulo");
        }
        else if (!this.J.getDiscord().equals("nulo")) {
            player.sendMessage(ChatColor.GREEN + "Seu Discord ja foi verificado!");
            return true;
        }
        String discord = "nulo"; 
        String discord1 = "nulo"; 
        discord1 = DiscordPonte.CheckDiscord(msg); 
        if (discord1.equals("nulo") || discord1 == null) {
        	Sistemas.RemoverJogador(id.toString());
			player.sendMessage(ChatColor.RED
					+ "Discord não identificado, Verifique se você está conectado em nosso servidor ou se você colocou o nome e o código exato.");
			player.sendMessage(ChatColor.GOLD + "Ex: /link Lucas M#4469");
			player.sendMessage(ChatColor.BLUE + "Link do Discord: https://discord.gg/kv4AqSzXYQ");
			return true;
		}
        if (!DiscordPonte.CheckDiscordSudeste(msg,"695813656490803223").equals("nulo")) {  //sp
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"695813656490803223");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"701801798154846298").equals("nulo")) { //mg
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"701801798154846298");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"801618489600901161").equals("nulo")) { //es
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"801618489600901161");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"738444755330924625").equals("nulo")) { //rj
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"738444755330924625");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"735561960254341121").equals("nulo")) { //ne
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"735561960254341121");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"782652814831779850").equals("nulo")) { //sul
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"782652814831779850");
        }
        else if(!DiscordPonte.CheckDiscordSudeste(msg,"796238091207180338").equals("nulo")) { //co
        	discord = DiscordPonte.CheckDiscordSudeste(msg,"796238091207180338");
        }
        if (discord.equals("nulo")) {
        	Sistemas.RemoverJogador(id.toString());
            player.sendMessage(ChatColor.RED + "Discord nao identificado, Verifique se voce esta conectado em nosso servidor ou se voce colocou o nome e o codigo exato.");
            player.sendMessage(ChatColor.GOLD + "Ex: /link Lucas M#4469");
            return true;
        }
        if (Sistemas.getDiscord(discord) != null) {
        	Sistemas.RemoverJogador(id.toString());
        	player.sendMessage(ChatColor.RED + "Este Discord já está cadastrado em outro jogador!");
        	return true;
        }
        J.setDiscord(discord);
        player.sendMessage(ChatColor.GREEN + "Discord verificado!");
        Cargos.gerenciarcargo(player);
        //Cargos.gerenciarcargo(player);
        return true;
    }
}
		/*Player player = (Player) sender;
		UUID id = player.getUniqueId();
		if (Sistemas.getJogador(id.toString()) != null) {
			player.sendMessage(ChatColor.GREEN + "Seu Discord já foi verificado!");
			return true;
		}
		if (Sistemas.getAplicante(id.toString()) == null) {
			G = Sistemas.addAplicante(id.toString(), "nulo","nulo");
		}
		G = Sistemas.getAplicante(id.toString());
		if (args[0] != null) {
			String msg = "";
			for (int i = 0; i < args.length; i++) {
				msg += args[i] + " ";
			}
			msg = msg.trim();
			if (G.getDiscord().equals("nulo")) {
				System.out.println(msg);
				String discord = DiscordPonte.CheckDiscord(msg);
				if (discord.equals("nulo") || discord == null) {
					player.sendMessage(ChatColor.RED
							+ "Discord não identificado, Verifique se você está conectado em nosso servidor ou se você colocou o nome e o código exato.");
					player.sendMessage(ChatColor.GOLD + "Ex: /link Lucas M#4469");
					player.sendMessage(ChatColor.BLUE + "Link do Discord: https://discord.gg/kv4AqSzXYQ");
					return true;
				} else {
					G.setDiscord(discord);
					player.sendMessage(ChatColor.GREEN + "Discord verificado!");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.GREEN + "Seu Discord já foi verificado!");
				return true;
			}
		}
		return false;
	}

}*/
		
