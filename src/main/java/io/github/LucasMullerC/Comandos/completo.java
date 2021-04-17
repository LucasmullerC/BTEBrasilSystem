package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Zonas;

public class completo implements CommandExecutor {
	Aplicantes A;
	Zonas Zn;
	BTEBrasilSystem plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		if (command.getName().equalsIgnoreCase("completo")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (Sistemas.getAplicante(id.toString()).getZona().equals("nulo")
					|| Sistemas.getAplicante(id.toString()) == null) {
				player.sendMessage(ChatColor.GOLD + "Você não tem nenhuma aplicação em andamento.");
				return true;
			} else {
				A = Sistemas.getAplicante(id.toString());
				Zn = Sistemas.getZona(A.getZona());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"region removeowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
						+ " permission unset worldedit.* worldguard:region=apply" + Zn.getZona() + A.getSeccao());
				World w = player.getWorld();
				if (A.getSeccao().equals("a")) {
					A.setSeccao("b");
					Zn.seta(false);
					Zn.setb(true);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"region addowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
							+ " permission set worldedit.* worldguard:region=apply" + Zn.getZona() + A.getSeccao());
					Location l = Zn.getlb();
			        player.teleport(l);
			        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							String txt = "Assista o video e siga as instruções: https://www.youtube.com/watch?v=CMHG-txFpEc";
							player.sendMessage(ChatColor.GOLD + txt);
							DiscordPonte.sendMessage(A.getDiscord(), txt);
						}
					}, 40L);
					return true;
				} else if (A.getSeccao().equals("b")) {
					A.setSeccao("c");
					Zn.setb(false);
					Zn.setc(true);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"region addowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
							+ " permission set worldedit.* worldguard:region=apply" + Zn.getZona() + A.getSeccao());
					Location l = Zn.getlc();
			        player.teleport(l);
			        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							String txt = "Assista o video e siga as instruções: https://www.youtube.com/watch?v=gajlcoGs6Ro";
							player.sendMessage(ChatColor.GOLD + txt);
							player.sendMessage(ChatColor.YELLOW + "A altura correta deste terreno está no Y: 32, Ajuste!");
							DiscordPonte.sendMessage(A.getDiscord(), txt);
						}
					}, 40L);
					return true;
				} else if (A.getSeccao().equals("c")) {
					A.setSeccao("d");
					Zn.setc(false);
					Zn.setd(true);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"region addowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
							+ " permission set worldedit.* worldguard:region=apply" + Zn.getZona() + A.getSeccao());
					Location l = Zn.getld();
			        player.teleport(l);
			        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							String txt = "Assista o video e siga as instruções: https://www.youtube.com/watch?v=f9mAnVJvqXU";
							String txt2 = "Link para o Google Maps: https://goo.gl/maps/uTwZwvUUh7u3jutj9";
							String txt3 = "IMGUR: https://imgur.com/  | So digite /completo após enviar a print e completar o formulario na pagina de cadastro do time.";
							player.sendMessage(ChatColor.GOLD + txt);
							player.sendMessage(ChatColor.GOLD + txt2);
							DiscordPonte.sendMessage(A.getDiscord(), txt);
							DiscordPonte.sendMessage(A.getDiscord(), txt2);
							DiscordPonte.sendMessage(A.getDiscord(), txt3);
						}
					}, 40L);
					return true;
				} else if (A.getSeccao().equals("d")) { // ultimo
					if (Sistemas.getPendentebyName(A.getUUID()) == null) {
						Sistemas.addPendente(id.toString());
						DiscordPonte.AnalisarReserva(A.getTime());
						String txt = "Sua aplicação foi enviada para o Time que você escolheu e logo será analisada!";
						player.sendMessage(ChatColor.GOLD + txt);
					}
					else {
						player.sendMessage(ChatColor.RED + "Você já tem uma analise pendente!");
					}
					return true;
				}
			}
		}
		return false;
	}

}
