package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;

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
import io.github.LucasMullerC.Comandos.analisar;
import io.github.LucasMullerC.Comandos.anterior;
import io.github.LucasMullerC.Comandos.aplicacao;
import io.github.LucasMullerC.Comandos.cancelar;
import io.github.LucasMullerC.Comandos.claim;
import io.github.LucasMullerC.Comandos.completo;
import io.github.LucasMullerC.Comandos.continuar;
import io.github.LucasMullerC.Comandos.deslink;
import io.github.LucasMullerC.Comandos.link;
import io.github.LucasMullerC.Comandos.perfil;
import io.github.LucasMullerC.Comandos.proximo;
import io.github.LucasMullerC.Comandos.salvar;
import io.github.LucasMullerC.Comandos.status;
import io.github.LucasMullerC.Comandos.tag;
import io.github.LucasMullerC.Comandos.time;
import io.github.LucasMullerC.DiscordBot.DiscordSrvListener;
import io.github.LucasMullerC.Util.GerarMapa;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaAreas;
import io.github.LucasMullerC.Util.ListaBuilders;
import io.github.LucasMullerC.Util.ListaConquistas;
import io.github.LucasMullerC.Util.ListaPendentes;
import io.github.LucasMullerC.Util.ListaZonas;
import io.github.LucasMullerC.Util.Mensagens;

public final class BTEBrasilSystem extends JavaPlugin implements Listener {
	private static BTEBrasilSystem instance;
	private DiscordSrvListener discordsrvListener = new DiscordSrvListener(this);

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
		getCommand("salvar").setExecutor(new salvar());
		getCommand("link").setExecutor(new link());
		getCommand("deslink").setExecutor(new deslink());
		getCommand("perfil").setExecutor(new perfil());
		getCommand("claim").setExecutor(new claim());
		getCommand("status").setExecutor(new status());
		// Inicializa Listener
		DiscordSRV.api.subscribe(discordsrvListener);
		getServer().getPluginManager().registerEvents(this, this);
		// Obter arquivos
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		GerenciarListas.aplicante = new ListaAplicar(new File(pluginFolder + File.separator + "aplicantes.txt"));
		GerenciarListas.pendente = new ListaPendentes(new File(pluginFolder + File.separator + "pendente.txt"));
		GerenciarListas.zonas = new ListaZonas(new File(pluginFolder + File.separator + "zonas.txt"));
		GerenciarListas.builder = new ListaBuilders(new File(pluginFolder + File.separator + "builders.txt"));
		GerenciarListas.areas = new ListaAreas(new File(pluginFolder + File.separator + "areas.txt"));
		GerenciarListas.mapa = new GerarMapa(new File(pluginFolder + File.separator + "claims.kml"));
		GerenciarListas.conquista = new ListaConquistas(new File(pluginFolder + File.separator + "conquistas.txt"));
		GerenciarListas.areas.load();
		GerenciarListas.builder.load();
		GerenciarListas.aplicante.load();
		GerenciarListas.pendente.load();
		GerenciarListas.zonas.load();
		GerenciarListas.conquista.load();
		// Inicializou sem problemas.
		getLogger().info("BTEBrasilSystem Ativado!");
	}

	// Listener para quando alguém entrar no servidor
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		//Pega o ID do discord
		String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());
        //Verifica se tá linkado
		if (discordId == null) {
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.RED + Mensagens.Link1);
					event.getPlayer()
							.sendMessage(ChatColor.YELLOW + Mensagens.Link2 + ChatColor.BLUE + Mensagens.InviteDiscord);
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link3 + ChatColor.BLUE + Mensagens.Link4
							+ ChatColor.YELLOW + Mensagens.Link5);
				}
			}, 30L);
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.RED + Mensagens.Link1en);
					event.getPlayer().sendMessage(
							ChatColor.YELLOW + Mensagens.Link2en + ChatColor.BLUE + Mensagens.InviteDiscord);
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link3en + ChatColor.BLUE
							+ Mensagens.Link4en + ChatColor.YELLOW + Mensagens.Link5en);
				}
			}, 30L);
		}
		else{
		User user = DiscordUtil.getJda().getUserById(discordId);
		if (user == null) {

			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.YELLOW + Mensagens.Link6);
					event.getPlayer().sendMessage(ChatColor.BLUE + Mensagens.InviteDiscord);
					return;
				}
			}, 30L);
		}
		else{
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {
				@Override
				public void run() {
					final Player p = event.getPlayer();
					event.getPlayer().sendMessage(ChatColor.GREEN + Mensagens.Bnovamente + user.getAsTag() + "!");
					String time = Sistemas.VerificarEquipe(p); 
					if (time != "") {  
						String[] times = time.split(",");
						int cont = 0;
						for (int b = 0; b < times.length; b++) {
							if (GerenciarListas.getPendenteAplicacao(times[b]) != null) {
								cont++;
							}
						}
						if (cont > 0) {
							p.sendMessage(ChatColor.RED + Mensagens.Atencao);
							p.sendMessage(ChatColor.GOLD + Mensagens.SeuTime1 + cont + Mensagens.SeuTime2 + ChatColor.BLUE
									+ Mensagens.analisar);
						}
					}
					Sistemas.CheckRank(p.getUniqueId().toString());
				}
			}, 30L);
		}
	}
		if(GerenciarListas.AplicacaoIsNull() == false){
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {

				@Override
				public void run() {
					GerenciarListas.DeletarDeadLines(event.getPlayer().getWorld());
				}
			}, 30L);
		}
	}

	@Override
	public void onDisable() {
		GerenciarListas.aplicante.save();
		GerenciarListas.pendente.save();
		GerenciarListas.zonas.save();
		GerenciarListas.builder.save();
		GerenciarListas.areas.save();
		GerenciarListas.mapa.save();
		GerenciarListas.conquista.save();
		DiscordSRV.api.unsubscribe(discordsrvListener);
		getLogger().info("BTEBrasilSystem Desativado!");
	}

	public static BTEBrasilSystem getPlugin() {
		return instance;
	}
}
