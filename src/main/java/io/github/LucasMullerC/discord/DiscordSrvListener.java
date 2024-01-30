package io.github.LucasMullerC.discord;

import org.bukkit.plugin.Plugin;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.dv8tion.jda.api.entities.Message;

public class DiscordSrvListener {
    private final Plugin plugin;

    public DiscordSrvListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {
        DiscordUtil.getJda().addEventListener(new JDAListener(plugin));

        plugin.getLogger().info("Chatting on Discord with " + DiscordUtil.getJda().getUsers().size() + " users!");
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        Message msg = event.getMessage();
        String[] cmd = msg.getContentRaw().split("\\s+");
        switch (cmd[0]) {
            case "!perfil":
                //new DiscordPerfil(msg);
                break;
        }
    }
}
