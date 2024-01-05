/*package io.github.LucasMullerC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.util.MessageUtils;
import net.dv8tion.jda.api.entities.User;

public class PlayerJoinListener implements Listener{
    	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());
		Aplicar aplicar = new Aplicar();

		if (discordId == null) {
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {

				@Override
				public void run() {
					event.getPlayer().sendMessage(ChatColor.RED + MessageUtils.getMessage("link1", event.getPlayer()));
					event.getPlayer()
							.sendMessage(ChatColor.YELLOW + MessageUtils.getMessage("Link2", event.getPlayer()) + ChatColor.BLUE + MessageUtils.getMessage("InviteDiscord", event.getPlayer()));
					event.getPlayer().sendMessage(ChatColor.YELLOW + MessageUtils.getMessage("Link3", event.getPlayer()) + ChatColor.BLUE + MessageUtils.getMessage("Link4", event.getPlayer())
							+ ChatColor.YELLOW + MessageUtils.getMessage("Link5", event.getPlayer()));
				}
			}, 30L);
		} else {
			User user = DiscordUtil.getJda().getUserById(discordId);
			if (user == null) {

				scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {

					@Override
					public void run() {
						event.getPlayer().sendMessage(ChatColor.YELLOW + MessageUtils.getMessage("Link6", event.getPlayer()));
						event.getPlayer().sendMessage(ChatColor.BLUE + MessageUtils.getMessage("InviteDiscord", event.getPlayer()));
						return;
					}
				}, 30L);
			} else {
				scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {
					@Override
					public void run() {
						final Player player = event.getPlayer();
						event.getPlayer().sendMessage(ChatColor.GREEN + MessageUtils.getMessage("Bnovamente", event.getPlayer()) + user.getAsTag() + "!");

						if (event.getPlayer().hasPermission("group.reviewer")) {
							int cont = aplicar.getPendenteAplicacaoQtd();

							if (cont > 0) {
								player.sendMessage(ChatColor.RED + MessageUtils.getMessage("Atencao", event.getPlayer()));
								player.sendMessage(
										ChatColor.GOLD + MessageUtils.getMessage("SeuTime1", event.getPlayer()) + cont + MessageUtils.getMessage("SeuTime2", event.getPlayer()) + ChatColor.BLUE
												+ MessageUtils.getMessage("analisar", event.getPlayer()));
							}
						}
						Sistemas sistemas = new Sistemas();
						sistemas.CheckRank(player.getUniqueId().toString());
					}
				}, 30L);
			}
		}
		if (aplicar.AplicacaoIsNull() == false) {
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {

				@Override
				public void run() {
					aplicar.DeletarDeadLines(event.getPlayer());
				}

			}, 30L);
		}
	}
}*/
