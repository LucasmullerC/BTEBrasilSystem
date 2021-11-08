package io.github.LucasMullerC.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;

public class deslink implements CommandExecutor {
    BTEBrasilSystem plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin = BTEBrasilSystem.getPlugin();
        final Player player = (Player) sender;
        player.chat("/discord unlink");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
                if (discordId != null) {
                    player.sendMessage(ChatColor.RED + "Ocorreu um erro.");

                } else {
                    Sistemas.RemovePermissaoTimes(player);

                }
            }

        }, 30L);
        return true;
    }

}
