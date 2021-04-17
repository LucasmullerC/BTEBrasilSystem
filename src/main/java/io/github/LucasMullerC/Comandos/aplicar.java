package io.github.LucasMullerC.Comandos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Objetos.Zonas;

public class aplicar implements CommandExecutor {
	BTEBrasilSystem plugin;
	Aplicantes A;
	Jogadores J;
	Zonas Zn = null;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("aplicar")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Inválido");
				return true;
			}

			else if (args[0] != null) {
				if (Sistemas.getJogador(id.toString()) != null) {
					if (Sistemas.getAplicante(id.toString()) != null) {
						A = Sistemas.getAplicante(id.toString());
						if (A.getTime().equals("nulo")) {
							A = Sistemas.addAplicante(id.toString(), args[0], Sistemas.getJogador(id.toString()).getDiscord());
						}
						else {
							player.sendMessage(ChatColor.RED + "Você já tem uma aplicação em andamento!");
							player.sendMessage(ChatColor.GOLD + "Digite /cancelar para cancelar sua aplicação pendente");
							player.sendMessage(ChatColor.GOLD + "Ou /continuar para continuar o processo.");
							return true;
						}
					}
					else {
						A = Sistemas.addAplicante(id.toString(), args[0], Sistemas.getJogador(id.toString()).getDiscord());
					}
				}
				else {
					if (Sistemas.getAplicante(id.toString()) != null) {
						A = Sistemas.getAplicante(id.toString());
						if (A.getTime().equals("nulo")) {
							A.setTime(args[0]);
						}
						else {
							player.sendMessage(ChatColor.RED + "Você já tem uma aplicação em andamento!");
							player.sendMessage(ChatColor.GOLD + "Digite /cancelar para cancelar sua aplicação pendente");
							player.sendMessage(ChatColor.GOLD + "Ou /continuar para continuar o processo.");
							return true;
						}
					}
					else {
						player.sendMessage(
								ChatColor.GOLD + "Você precisa verificar seu Discord para fazer parte de um Time!");
						player.sendMessage(ChatColor.GOLD + "Para verificar digite:");
						player.sendMessage(ChatColor.YELLOW + "/link Seu nome no Discord");
						player.sendMessage(ChatColor.GOLD + "Ex: /link Lucas M#4469");
						return true;
					}
				}
				if (A.getDiscord().equals("nulo")) { // check Discord
					Sistemas.RemoverAplicante(id.toString());
					player.sendMessage(
							ChatColor.GOLD + "Você precisa verificar seu Discord para fazer parte de um Time!");
					player.sendMessage(ChatColor.GOLD + "Para verificar digite:");
					player.sendMessage(ChatColor.YELLOW + "/link Seu nome no Discord");
					player.sendMessage(ChatColor.GOLD + "Ex: /link Lucas M#4469");
					return true;
				} else {
					if (A.getZona().equals("nulo") == false) { // Aplicação em andamento
						player.sendMessage(ChatColor.RED + "Você já tem uma aplicação em andamento!");
						player.sendMessage(ChatColor.RED + "Digite /continuar para voltar.");
						return true;
					} else {
						if (Sistemas.getJogador(id.toString()) == null) { // Primeira vez
							AplicarCheck(player, args[0]);
							return true;

						} else { // Já cadastrou uma vez
							J = Sistemas.getJogador(id.toString());
							String Times = J.getTime();
							String[] result = Times.split(",");
							for (int i = 0; i < result.length; i++) {
								if (args[0].equals(result[i])) {
									player.sendMessage(ChatColor.RED + "Você já é um Construtor neste time!");
									Sistemas.RemoverAplicante(id.toString());
									return true;
								}
							}
							AplicarCheck(player, args[0]);
							return true;

						}
					}

				}

			} else {
				player.sendMessage(ChatColor.RED + "Invalido!");
				return true;
			}

		}
		return false;
	}

	public void AplicarCheck(Player player, String Time) {
		UUID id = player.getUniqueId();
		ArrayList<Zonas> Z = Sistemas.getListaZonas();
		for (int o = 0; o < Z.size(); o++) {
			if (Z.get(o).getOcupado() == false) {
				Zn = Z.get(o);
				break;
			}
		}
		if (Zn != null) {
			Zn.setOcupado(true);
			A.setZona(Zn.getZona());
			A.setTime(Time);
			A.setSeccao("a");
			Zn.setNome(id.toString());
			World w = player.getWorld();
			Location l = Zn.getla();
			player.teleport(l);
			Zn.seta(true);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					"region addowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w TerraPreGenerated");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + id.toString()
					+ " permission set worldedit.* worldguard:region=apply" + Zn.getZona() + "a");
			//LocalDate currentDate = LocalDate.now();
			LocalDate deadline = LocalDate.now().plusDays(3); // x = 10
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
			String data = deadline.format(formatter);
			A.setDeadline(data);
			player.setGameMode(GameMode.CREATIVE);
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
			player.getInventory().addItem(new ItemStack(Material.WOOD_AXE, 1));
			player.sendMessage(ChatColor.GREEN + "Aplicação iniciada!");
			String txt = "Assista o video e siga as instruções: https://youtu.be/KEvtqOYDatE";
			player.sendMessage(ChatColor.GOLD + txt);
			DiscordPonte.sendMessage(A.getDiscord(), txt);
		} else {
			player.sendMessage(ChatColor.GOLD + "Todas as zonas de aplicação estão ocupadas!");
		}
	}

}
