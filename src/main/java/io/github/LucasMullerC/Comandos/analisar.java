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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Objetos.Zonas;

public class analisar implements CommandExecutor {
	Aplicantes A;
	Zonas Zn;
	BTEBrasilSystem plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		UUID id = player.getUniqueId();
		if (command.getName().equalsIgnoreCase("analisar")) {
			BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			String time = Sistemas.getEquipe(id.toString()).getTime();
			String[] times = time.split(",");
			Boolean pass = false;
			for (int b = 0; b < times.length; b++) {
				if (Sistemas.getPendente(times[b]) != null) {
					pass = true;
					time = times[b];
					break;
				} else {
					pass = false;
				}
			}
			if (pass == true) {
				A = Sistemas.getPendente(time);
				Zn = Sistemas.getZona(A.getZona());
				String msg = "";
				for (int i = 0; i < args.length; i++) {
					msg += args[i] + " ";
				}
				msg = msg.trim();
				String[] arrayValores = msg.split(" ");
				if (arrayValores[0].equalsIgnoreCase("confirmar")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "region removeowner apply" + A.getZona()
							+ A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID()
							+ " permission unset worldedit.* worldguard:region=apply" + A.getZona() + A.getSeccao());
					Zn = Sistemas.getZona(A.getZona());
					Sistemas.addJogador(A.getUUID(), A.getTime(), A.getDiscord());
					Sistemas.RemoverAplicante(A.getUUID());
					Sistemas.RemoverPendente(A);
					Zn.setNome("nulo");
					Zn.setOcupado(false);
					Zn.setd(false);
					World w = player.getWorld();
					DiscordPonte.addCargo(A.getUUID(), A.getTime(), A.getDiscord());
					OfflinePlayer pa = Bukkit.getOfflinePlayer(UUID.fromString(A.getUUID()));
					if (pa.isOnline() == true) {
						Player app = Bukkit.getPlayer(UUID.fromString(A.getUUID()));
						Location l = new Location(w, -1163, 80, 300);
						app.teleport(l);
						app.setGameMode(GameMode.SURVIVAL);
						app.removePotionEffect(PotionEffectType.NIGHT_VISION);
						PlayerInventory inventoryp = app.getInventory();
						inventoryp.clear();
					}
					player.sendMessage(ChatColor.RED + "NÃO SE MOVA! Iremos resetar a zona de aplicação!");
					if (A.getTime().equals("ne")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_ne");
					} else if (A.getTime().equals("sp")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_sp");
					} else if (A.getTime().equals("sul")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								"lp user " + A.getUUID() + " group add b_sul");
					} else if (A.getTime().equals("co")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_co");
					} else if (A.getTime().equals("mg")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_mg");
					} else if (A.getTime().equals("rj")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_rj");
					} else if (A.getTime().equals("es")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + A.getUUID() + " group add b_es");
					}
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							Location l = Sistemas.getZona(A.getZona()).getla();
							Location lb = new Location(w, l.getX() - 2, 45, l.getZ() - 2);
							player.teleport(lb);
							player.chat("/apprstararea");
						}
					}, 20L);
					player.sendMessage(ChatColor.GREEN + "Area resetada!");
					Location l = new Location(w, -1163, 80, 300);
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(l);
						}
					}, 20L);
					player.setGameMode(GameMode.SURVIVAL);
					PlayerInventory inventory = player.getInventory();
					inventory.clear();
					player.sendMessage(ChatColor.GOLD + "Aplicação aprovada! Builder adicionado!");
					String txt = "Sua aplicação foi aprovada! Agora você é um builder oficial!";
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					return true;
				} else if (arrayValores[0].equalsIgnoreCase("recusar")) {
					String motivo = "";
					for (int i = 1; i < arrayValores.length; i++) {
						motivo += arrayValores[i] + " ";
					}
					motivo = motivo.trim();
					Sistemas.RemoverPendente(A);
					World w = player.getWorld();
					String txt = "Sua aplicação foi recusada pelo motivo: " + motivo
							+ " | Volte até lá e corrija os problemas!";
					DiscordPonte.sendMessage(A.getDiscord(), txt);
					Location l = new Location(w, -1163, 80, 300);
					player.teleport(l);
					player.setGameMode(GameMode.SURVIVAL);
					PlayerInventory inventory = player.getInventory();
					inventory.clear();
					player.sendMessage(ChatColor.GOLD + "Aplicação recusada!");
					return true;
				} else {
					World w = player.getWorld();
					Location l = Sistemas.getZona(A.getZona()).getld();
					player.teleport(l);
					player.setGameMode(GameMode.CREATIVE);
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
					player.sendMessage(ChatColor.GOLD + "Digite /analisar confirmar - Para aceitar a aplicação.");
					player.sendMessage(
							ChatColor.GOLD + "Ou digite /analisar recusar (Motivo) - Para recusar a aplicação.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.GOLD + "Seu time não tem nenhuma aplicação pendente.");
				return true;
			}

		}
		return false;

	}
}
