package io.github.LucasMullerC.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.util.MessageUtils;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
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

    public static String getDiscordName(String id) {
        User user = DiscordUtil.getJda().getUserById(id);
        return user.getName();
    }

    public static void addRole(String discordId,String roleId){
        if(CheckDiscord(discordId) == true){
            Guild guild = DiscordUtil.getJda().getGuildById("715528474655326238"); // BTE BR
            Role role = guild.getRoleById(roleId);
            Member member = guild.getMemberById(discordId);
            guild.addRoleToMember(member, role).queue();
        } else {
            String discordName = getDiscordName(discordId);
            sendLogMessage(MessageUtils.getMessageConsole("notpossibletogiverole1")+" "+discordName+MessageUtils.getMessageConsole("notpossibletogiverole2"));
        }
    }
}
