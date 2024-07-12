package io.github.LucasMullerC.discord;

import github.scarsz.discordsrv.util.DiscordUtil;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DiscordActions {
    static TextChannel chatbr = DiscordUtil.getTextChannelById("717528858777354272");
    static TextChannel chatbrlog = DiscordUtil.getTextChannelById("921811170086776942");

    public static void sendPrivateMessage(String id, String content) { 
        User user = DiscordUtil.getJda().getUserById(id);
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    public static void sendLogMessage(String content) {
        chatbrlog.sendMessage(content).queue();
    }
}
