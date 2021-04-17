package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Zonas;

public class cancelar implements CommandExecutor {
	Aplicantes A,AP;
	Zonas Zn;
	BTEBrasilSystem plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("cancelar")) {
			BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
	    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (args.length == 0) {
				if (Sistemas.getAplicante(id.toString()).getZona().equals("nulo")
						|| Sistemas.getAplicante(id.toString()) == null) {
					player.sendMessage(ChatColor.GOLD + "Você não tem nenhuma aplicação em andamento.");
					return true;
				} else {
					A = Sistemas.getAplicante(id.toString());
					Zn = Sistemas.getZona(A.getZona());
					Zn.seta(false);
					Zn.setb(false);
					Zn.setc(false);
					Zn.setd(false);
					Zn.setNome("nulo");
					Zn.setOcupado(false);
					if (Sistemas.getPendentebyName(A.getUUID()) != null) {
						AP = Sistemas.getPendentebyName(A.getUUID());
						Sistemas.RemoverPendente(AP);
					}
					Sistemas.RemoverAplicante(id.toString());
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"region removeowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
							+ " permission unset worldedit.* worldguard:region=apply" + Zn.getZona() + A.getSeccao());
					World w = player.getWorld();
					player.sendMessage(ChatColor.RED + "NÃO SE MOVA! Iremos resetar a zona de aplicação!");
					Location l = Sistemas.getZona(A.getZona()).getla();
                	Location lb = new Location(w, l.getX()-2, 45, l.getZ()-2);
                	player.teleport(lb);
					player.chat("/apprstararea");
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
		                @Override
		                public void run() {
		                	Location l = new Location(w, -1163, 80, 300);
					        player.teleport(l);
		                }
		            }, 40L);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					PlayerInventory inventory = player.getInventory();
					inventory.clear();
				}
			} else if (args[0] != null) {
				if (player.hasPermission("btebrasil.lider")) {
					if (Sistemas.getAplicante(args[0]) == null) {
						player.sendMessage(ChatColor.GOLD + "Player não tem nenhuma aplicação em andamento");
						return true;
					}
					else if(Sistemas.getAplicante(args[0]).getZona().equals("nulo")) {
						player.sendMessage(ChatColor.GOLD + "Player não tem nenhuma aplicação em andamento");
						return true;
					}
					else {
						A = Sistemas.getAplicante(args[0]);
						Zn = Sistemas.getZona(A.getZona());
						Zn.seta(false);
						Zn.setb(false);
						Zn.setc(false);
						Zn.setd(false);
						Zn.setNome("nulo");
						Zn.setOcupado(false);
						World w = player.getWorld();
						Sistemas.RemoverAplicante(args[0]);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"region removeowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + A.getUUID() + " permission unset worldedit.* worldguard:region=apply"
										+ Zn.getZona() + A.getSeccao());
						player.sendMessage(ChatColor.RED + "NÃO SE MOVA! Iremos resetar a zona de aplicação!");
						Location l = Sistemas.getZona(A.getZona()).getla();
	                	Location lb = new Location(w, l.getX()-2, 45, l.getZ()-2);
	                	player.teleport(lb);
						player.chat("/apprstararea");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			                @Override
			                public void run() {
			                	Location l = new Location(w, -1163, 80, 300);
						        player.teleport(l);
			                }
			            }, 40L);
						OfflinePlayer pa = Bukkit.getOfflinePlayer(UUID.fromString(args[0]));
						if (pa.isOnline() == true) {
							Player p = Bukkit.getPlayer(UUID.fromString(args[0]));
							p.setGameMode(GameMode.SURVIVAL);
							p.removePotionEffect(PotionEffectType.NIGHT_VISION);
							PlayerInventory inventory = p.getInventory();
							inventory.clear();
						}
						return true;
					}
				} else {
					player.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando!");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Ocorreu um erro. [2]");
			}
		}
		return false;
	}

}
