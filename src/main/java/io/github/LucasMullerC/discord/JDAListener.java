package io.github.LucasMullerC.discord;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.dependencies.jda.api.events.guild.GuildUnavailableEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;


public class JDAListener extends ListenerAdapter {

    private final Plugin plugin;

    public JDAListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildUnavailable(@NotNull GuildUnavailableEvent event) {
        plugin.getLogger().severe("Oh no " + event.getGuild().getName() + " went unavailable :(");
    }
}
