package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

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
import io.github.LucasMullerC.Gerencia.Aplicar;
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
		// Inicializou sem problemas.
		getLogger().info("BTEBrasilSystem Ativado!");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getFrom().getBlockX() != event.getTo().getBlock().getX()
				&& event.getFrom().getBlockY() != event.getTo().getBlock().getY()) {
			Player player = event.getPlayer();
			com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
			ApplicableRegionSet playerRegion = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery()
					.getApplicableRegions(loc);

			// Verifica se o jogador está em alguma região
			if (playerRegion != null) {
				for (ProtectedRegion region : playerRegion.getRegions()) {
					for (UUID uuids : region.getMembers().getUniqueIds()) {
						System.out.println(uuids);
						if (player.getUniqueId().equals(uuids)) {
							if (player.hasPermission("group.builder_not")) {
								GroupManager gp = new GroupManager();
								gp.removeGroup(player, "builder_not");
								gp.addGroup(player, "b_br");
								return;
							} else {
								return;
							}
						}

					}
					if (player.hasPermission("group.b_br")) {
						GroupManager gp = new GroupManager();
						gp.addGroup(player, "builder_not");
						gp.removeGroup(player, "b_br");
						return;
					}
				}
			} else {
				if (player.hasPermission("group.builder_not")) {
					GroupManager gp = new GroupManager();
					gp.removeGroup(player, "builder_not");
					gp.addGroup(player, "b_br");
				}
			}
		}
	}

	// Listener para quando alguém entrar no servidor
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Aplicar aplicar = new Aplicar();
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		// Pega o ID do discord
		String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());
		// Verifica se tá linkado
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
		} else {
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
			} else {
				scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {
					@Override
					public void run() {
						final Player p = event.getPlayer();
						event.getPlayer().sendMessage(ChatColor.GREEN + Mensagens.Bnovamente + user.getAsTag() + "!");
						int cont = aplicar.getPendenteAplicacaoQtd();

						if (cont > 0) {
							p.sendMessage(ChatColor.RED + Mensagens.Atencao);
							p.sendMessage(
									ChatColor.GOLD + Mensagens.SeuTime1 + cont + Mensagens.SeuTime2 + ChatColor.BLUE
											+ Mensagens.analisar);
						}
						Sistemas sistemas = new Sistemas();
						sistemas.CheckRank(p.getUniqueId().toString());
					}
				}, 30L);
			}
		}
		if (aplicar.AplicacaoIsNull() == false) {
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.instance, new Runnable() {

				@Override
				public void run() {
					aplicar.DeletarDeadLines(event.getPlayer());
				}

			}, 30L);
		}
	}

	@Override
	public void onDisable() {
		DiscordSRV.api.unsubscribe(discordsrvListener);
		getLogger().info("BTEBrasilSystem Desativado!");
	}

	public static BTEBrasilSystem getPlugin() {
		return instance;
	}
}
