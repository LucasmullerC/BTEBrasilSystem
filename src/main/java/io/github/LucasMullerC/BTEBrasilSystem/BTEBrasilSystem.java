package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.Comandos.addequipe;
import io.github.LucasMullerC.Comandos.analisar;
import io.github.LucasMullerC.Comandos.anterior;
import io.github.LucasMullerC.Comandos.aplicacao;
import io.github.LucasMullerC.Comandos.cancelar;
import io.github.LucasMullerC.Comandos.completo;
import io.github.LucasMullerC.Comandos.continuar;
import io.github.LucasMullerC.Comandos.deslink;
import io.github.LucasMullerC.Comandos.link;
import io.github.LucasMullerC.Comandos.perfil;
import io.github.LucasMullerC.Comandos.proximo;
import io.github.LucasMullerC.Comandos.removerequipe;
import io.github.LucasMullerC.Comandos.salvar;
import io.github.LucasMullerC.Comandos.tag;
import io.github.LucasMullerC.Comandos.time;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaJogadores;
import io.github.LucasMullerC.Util.ListaZonas;
import io.github.LucasMullerC.Util.Mensagens;

public final class BTEBrasilSystem extends JavaPlugin implements Listener {
	private static BTEBrasilSystem instance;
	GerenciarListas GL;
	public static boolean aps = false;

	@Override
	public void onEnable() {
		instance = this;
		// Comandos
		getCommand("aplicacao").setExecutor(new aplicacao());
		getCommand("anterior").setExecutor(new anterior());
		getCommand("proximo").setExecutor(new proximo());
		getCommand("continuar").setExecutor(new continuar());
		getCommand("time").setExecutor(new time());
		getCommand("completo").setExecutor(new completo());
		getCommand("tag").setExecutor(new tag());
		getCommand("analisar").setExecutor(new analisar());
		getCommand("cancelar").setExecutor(new cancelar());
		getCommand("addequipe").setExecutor(new addequipe());
		getCommand("removerequipe").setExecutor(new removerequipe());
		getCommand("salvar").setExecutor(new salvar());
		getCommand("link").setExecutor(new link());
		getCommand("deslink").setExecutor(new deslink());
		getCommand("perfil").setExecutor(new perfil());
		// Inicializa Listener
		getServer().getPluginManager().registerEvents(this, this);
		// Obter arquivos
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		GL.aplicante = new ListaAplicar(new File(pluginFolder + File.separator + "aplicantes.txt"));
		GL.pendente = new ListaAplicar(new File(pluginFolder + File.separator + "pendente.txt"));
		GL.zonas = new ListaZonas(new File(pluginFolder + File.separator + "zonas.txt"));
		GL.equipe = new ListaJogadores(new File(pluginFolder + File.separator + "equipes.txt"));
		GL.equipe.load();
		GL.aplicante.load();
		GL.pendente.load();
		GL.zonas.load();
		// Inicializou sem problemas.
		getLogger().info("BTEBrasilSystem Ativado!");
		Sistemas.DetectarDeadLine();
	}

	// Listener para quando alguém entrar no servidor
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());
		if (discordId == null) {
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.RED + Mensagens.Link1);
					event.getPlayer()
							.sendMessage(ChatColor.YELLOW + Mensagens.Link2 + ChatColor.BLUE + Mensagens.InviteDiscord);
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link3 + ChatColor.BLUE + Mensagens.Link4
							+ ChatColor.YELLOW + Mensagens.Link5);
					return;
				}
			}, 30L);
			scheduler.scheduleSyncDelayedTask(this.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.RED + Mensagens.Link1en);
					event.getPlayer().sendMessage(
							ChatColor.YELLOW + Mensagens.Link2en + ChatColor.BLUE + Mensagens.InviteDiscord);
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link3en + ChatColor.BLUE
							+ Mensagens.Link4en + ChatColor.YELLOW + Mensagens.Link5en);
					return;
				}
			}, 30L);
		}

		User user = DiscordUtil.getJda().getUserById(discordId);
		if (user == null) {

			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link6);
					event.getPlayer().sendMessage(ChatColor.BLUE + Mensagens.InviteDiscord);
					return;
				}
			}, 30L);
		}

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(this.instance, new Runnable() {

			@Override
			public void run() {
				final Player p = event.getPlayer();
				UUID id = p.getUniqueId();
				event.getPlayer().sendMessage(ChatColor.GREEN + Mensagens.Bnovamente + user.getAsTag() + "!");
				if (GerenciarListas.getEquipe(id.toString()) != null) {
					String time = GerenciarListas.getEquipe(id.toString()).getTime();
					String[] times = time.split(",");
					int cont = 0;
					for (int b = 0; b < times.length; b++) {
						if (GerenciarListas.getPendente(times[b]) != null) {
							cont++;
						}
					}
					if (cont > 0) {
						p.sendMessage(ChatColor.RED + Mensagens.Atencao);
						p.sendMessage(ChatColor.GOLD + Mensagens.SeuTime1 + cont + Mensagens.SeuTime2 + ChatColor.BLUE
								+ Mensagens.analisar);
					}
				}
			}
		}, 30L);
		if(aps == true){
			scheduler.scheduleSyncDelayedTask(this.instance, new Runnable() {

				@Override
				public void run() {
					GerenciarListas.DeletarDeadLines(event.getPlayer().getWorld());
					aps = false;
				}
			}, 30L);
		}
	}

	@Override
	public void onDisable() {
		GL.equipe.save();
		GL.aplicante.save();
		GL.pendente.save();
		GL.zonas.save();
		getLogger().info("BTEBrasilSystem Desativado!");
	}

	public static BTEBrasilSystem getPlugin() {
		return instance;
	}
}
