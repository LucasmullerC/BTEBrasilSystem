package io.github.LucasMullerC.DiscordBot;

import org.bukkit.plugin.Plugin;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.DiscordBot.comandos.DiscordPerfil;
import io.github.LucasMullerC.DiscordBot.comandos.addconquista;
import io.github.LucasMullerC.DiscordBot.comandos.addpontos;
import io.github.LucasMullerC.DiscordBot.comandos.conquistas;
import io.github.LucasMullerC.DiscordBot.comandos.destacar;
import io.github.LucasMullerC.DiscordBot.comandos.giveconquista;
import io.github.LucasMullerC.DiscordBot.comandos.lb;
import io.github.LucasMullerC.DiscordBot.comandos.ranks;

public class DiscordSrvListener {
    private final Plugin plugin;

    public DiscordSrvListener(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {
        // We need to wait until DiscordSRV has initialized JDA, thus we're doing this inside DiscordReadyEvent
        DiscordUtil.getJda().addEventListener(new JDAListener(plugin));

        plugin.getLogger().info("Chatting on Discord with " + DiscordUtil.getJda().getUsers().size() + " users!");
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        Message msg = event.getMessage();
        String[] cmd = msg.getContentRaw().split("\\s+");
        switch(cmd[0]){
            case "!perfil":
                new DiscordPerfil(msg);
                break;
            case "!giveconquista":
                new giveconquista(msg);
                break;
            case "!addconquista":
                new addconquista(msg);
                break;
            case "!destacar":
                new destacar(msg);
                break;
            case "!ranks":
                new ranks(msg);
                break;
            case "!rank":
                new ranks(msg);
                break;
            case "!lb":
                new lb(msg);
                break;
            case "!conquistas":
                new conquistas(msg);
                break;
            case "!conquista":
                new conquistas(msg);
                break;
            case "!addpontos":
                new addpontos(msg);
                break;
        }
    }

}
