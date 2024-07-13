package io.github.LucasMullerC.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import java.util.UUID;

public class DiscordActions {
    static TextChannel chatbr = DiscordUtil.getTextChannelById("717528858777354272");
    static TextChannel chatbrlog = DiscordUtil.getTextChannelById("921811170086776942");

    public static boolean CheckDiscord(String id) {
        Guild guild = DiscordUtil.getJda().getGuildById("715528474655326238");
        User Builder = DiscordUtil.getJda().getUserById(id);
        if (Builder != null) {
            Member member = guild.getMemberById(Builder.getId());
            if (member == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    public static void sendPrivateMessage(String id, String content) { 
        User user = DiscordUtil.getJda().getUserById(id);
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    public static void sendLogMessage(String content) {
        chatbrlog.sendMessage(content).queue();
    }

    public static String getDiscordId(UUID id){
        return DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(id);
    }
}
