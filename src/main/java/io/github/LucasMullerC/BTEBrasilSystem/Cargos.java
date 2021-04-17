package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import io.github.LucasMullerC.Objetos.Jogadores;

public class Cargos {
	public static void gerenciarcargo(final Player p) {
		BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		UUID id = p.getUniqueId();
		if (Sistemas.getJogador(id.toString()) == null) {
			p.sendMessage(ChatColor.YELLOW + "Para ganhar seus cargos, Digite /link Seu nome no Discord");
			p.sendMessage(ChatColor.RED + "Ex: /link Lucas M#4469");
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.YELLOW + "To get your permissions, Type /link Your Discord Name");
					p.sendMessage(ChatColor.RED + "Ex: /link Lucas M#4469");
				}
			}, 40L);
		} else if (Sistemas.getJogador(id.toString()).getDiscord().equals("nulo")) {
			p.sendMessage(ChatColor.YELLOW + "Para ganhar seus cargos, Digite /link Seu nome no Discord");
			p.sendMessage(ChatColor.RED + "Ex: /link Lucas M#4469");
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.YELLOW + "To get your permissions, Type /link Your Discord Name");
					p.sendMessage(ChatColor.RED + "Ex: /link Lucas M#4469");
				}
			}, 40L);
		} else {
			final Jogadores G = Sistemas.getJogador(id.toString());
			// Procedimento apenas válido para o Sudeste
			final Boolean cargoSP = DiscordPonte.getCargoSudeste(G.getDiscord(), "695813656490803223",
					"696887529944645716");
			final Boolean cargoRJ = DiscordPonte.getCargoSudeste(G.getDiscord(), "738444755330924625",
					"776573196097814551");
			final Boolean cargoMG = DiscordPonte.getCargoSudeste(G.getDiscord(), "701801798154846298",
					"701816986081951895");
			final Boolean cargoES = DiscordPonte.getCargoSudeste(G.getDiscord(), "801618489600901161",
					"801623044812177418");
			final Boolean cargoNE = DiscordPonte.getCargoSudeste(G.getDiscord(), "735561960254341121",
					"815586676159676467");
			final Boolean cargoSUL = DiscordPonte.getCargoSudeste(G.getDiscord(), "782652814831779850",
					"782653956157472820");
			final Boolean cargoCO = DiscordPonte.getCargoSudeste(G.getDiscord(), "796238091207180338",
					"796465391894790154");
			String[] result = Sistemas.getJogador(G.getUUID()).getTime().split(",");
			String times = "";
			if (G.getCargo() == true) {
				boolean vef;
				for (int g = 0; g < result.length; g++) {
					vef = false;
					if (cargoSP == false&&"sp".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_sp");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time São Paulo foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoRJ == false&&"rj".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_rj");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Rio de Janeiro foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoMG == false&&"mg".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_mg");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Minas Gerais foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoES == false&&"es".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_es");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Espírito Santo foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoNE == false&&"ne".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_ne");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Nordeste foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoSUL == false&&"sul".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_sul");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Sul foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (cargoCO == false&&"co".equals(result[g])) {
						Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
								"lp user " + G.getUUID() + " group remove b_co");
						vef = true;
						G.setCargo(false);
						p.sendMessage(ChatColor.RED
								+ "Seus cargos no Time Centro-Oeste foram removidos pois voce nao e mais um Builder.");
						scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.RED
										+ "Your permissions was removed because you are not a builder anymore.");
							}
						}, 40L);
					}
					if (vef == false) {
						times = "";
						times += result[g] + ",";
					}
				}
				G.setTime(times);
			} else if (cargoSP == false && cargoRJ == false && cargoMG == false && cargoES == false && cargoNE == false && cargoSUL == false && cargoCO == false ) {
				p.sendMessage(ChatColor.GOLD + "Para poder construir considere se tornar um builder em nosso time!");
				p.sendMessage(ChatColor.YELLOW + "Junte-se ao nosso Discord para mais informacoes");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GOLD + "To help us, you need to be a builder");
						p.sendMessage(ChatColor.YELLOW + "Join our Discord for more informations");
					}
				}, 40L);
			}
			boolean vef,sp1=false,mg1=false,rj1=false,es1=false,ne1=false,sul1=false,co1=false;
			times = "";
			for (int g = 0; g < result.length; g++) {
				vef = false;
				if ("sp".equals(result[g])) {
					times += result[g] + ",";
					sp1 = true;
				}
				if ("mg".equals(result[g])) {
					times += result[g] + ",";
					mg1 = true;
				}
				if ("es".equals(result[g])) {
					times += result[g] + ",";
					es1 = true;
				}
				if ("rj".equals(result[g])) {
					times += result[g] + ",";
					rj1 = true;
				}
				if ("ne".equals(result[g])) {
					times += result[g] + ",";
					ne1 = true;
				}
				if ("sul".equals(result[g])) {
					times += result[g] + ",";
					sul1 = true;
				}
				if ("co".equals(result[g])) {
					times += result[g] + ",";
					co1 = true;
				}
			}
			G.setTime(times);
			for (int i = 0; i < result.length; i++) {				
			if (cargoSP == true && sp1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_sp");
				times += "sp" + ",";
				sp1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time São Paulo!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoRJ == true && rj1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_rj");
				times += "rj" + ",";
				rj1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Rio de Janeiro!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoMG == true && mg1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_mg");
				times += "mg" + ",";
				mg1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Minas Gerais!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoES == true && es1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_es");
				times += "es" + ",";
				es1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Espírito Santo!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoNE == true && ne1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_ne");
				times += "ne" + ",";
				ne1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Nordeste!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoSUL == true && sul1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_sul");
				times += "sul" + ",";
				sul1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Sul!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			if (cargoCO == true && co1 == false) {
				Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
						"lp user " + G.getUUID() + " group add b_co");
				times += "co" + ",";
				vef = true;
				co1 = true;
				G.setCargo(true);
				p.sendMessage(ChatColor.GREEN + "Seus cargos foram adicionados! Bem-vindo ao time Centro-Oeste!");
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.GREEN + "You are now a builder! Welcome to our team!");
					}
				}, 40L);
			}
			G.setTime(times);
			}
		}
	}

}
