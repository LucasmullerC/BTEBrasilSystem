package io.github.LucasMullerC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.applicant.DeadlineService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerJoinListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId());

		if (discordId == null) {
			// Not Linked
			scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {
				@Override
				public void run() {
					event.getPlayer().sendMessage(Component.text(MessageUtils.getMessage("link1", event.getPlayer()))
							.color(NamedTextColor.RED));

					event.getPlayer()
							.sendMessage(Component.text(MessageUtils.getMessage("Link2", event.getPlayer()))
									.color(NamedTextColor.YELLOW)
									.append(Component.text(MessageUtils.getMessage("clickevent", event.getPlayer()))
									.clickEvent(ClickEvent.openUrl(MessageUtils.getMessage("InviteDiscord", event.getPlayer())))
											.color(NamedTextColor.BLUE)));
					event.getPlayer()
							.sendMessage(Component.text(MessageUtils.getMessage("Link3", event.getPlayer()))
									.color(NamedTextColor.YELLOW)
									.append(Component.text(MessageUtils.getMessage("Link4", event.getPlayer()))
											.color(NamedTextColor.BLUE)
											.append(Component.text(MessageUtils.getMessage("Link5", event.getPlayer()))
													.color(NamedTextColor.YELLOW))));
				}
			}, 30L);
		} else {
			//Not in Discord
			User user = DiscordUtil.getJda().getUserById(discordId);
			if (user == null) {

				scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {

					@Override
					public void run() {
						event.getPlayer()
								.sendMessage(Component.text(MessageUtils.getMessage("Link6", event.getPlayer())).color(NamedTextColor.YELLOW));
						event.getPlayer().sendMessage(
							Component.text(MessageUtils.getMessage("clickevent", event.getPlayer()))
							.clickEvent(ClickEvent.openUrl(MessageUtils.getMessage("InviteDiscord", event.getPlayer())))
							.color(NamedTextColor.BLUE));
						return;
					}
				}, 30L);
			} else {
				//Linkado
				scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {
					@Override
					public void run() {
						final Player player = event.getPlayer();
						event.getPlayer().sendMessage(
							Component.text(MessageUtils.getMessage("Bnovamente", event.getPlayer()) + user.getAsTag() + "!").color(NamedTextColor.GREEN));

						if (event.getPlayer().hasPermission("group.reviewer")) {
							PendingService pendingService = new PendingService();
							int cont = pendingService.getTotalPendingCount(true);
							int contClaim = pendingService.getTotalPendingCount(false); 

							if (cont > 0 || contClaim > 0) {
								player.sendMessage(Component.text(MessageUtils.getMessage("Atencao", event.getPlayer())).color(NamedTextColor.RED));

								player.sendMessage(Component.text(MessageUtils.getMessage("SeuTime1", event.getPlayer()) 
								+ cont + " "+
								MessageUtils.getMessage("SeuTime2", event.getPlayer()) +contClaim +" "+ MessageUtils.getMessage("SeuTime3", event.getPlayer())).color(NamedTextColor.GOLD).append(Component.text(MessageUtils.getMessage("analisar", event.getPlayer())).color(NamedTextColor.BLUE)));
							}
						}
						BuilderService builderService = new BuilderService();
						Builder builder = builderService.getBuilderUuid(player.getUniqueId().toString());
						if(builder != null){
							BuilderUtils.checkRank(builder,builderService);
						}
					}
				}, 30L);
			}
		}
		scheduler.scheduleSyncDelayedTask(BTEBrasilSystem.getPlugin(), new Runnable() {

			@Override
			public void run() {
				new DeadlineService(event.getPlayer());
			}

		}, 30L);
	}
}
