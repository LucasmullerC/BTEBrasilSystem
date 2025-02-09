package io.github.LucasMullerC.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.util.MessageUtils;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public class DiscordActions {
    static TextChannel chatbr = DiscordUtil.getTextChannelById("717528858777354272");
    static TextChannel chatbrlog = DiscordUtil.getTextChannelById("921811170086776942");
    //static TextChannel chatbrbot = DiscordUtil.getTextChannelById("717528314423803974"); //OFICIAL
    static TextChannel chatbrbot = DiscordUtil.getTextChannelById("1129556584469643276"); //TESTES

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

    public static boolean checkAdmin(List<Role> roles) {
        boolean hasPermission = false;
        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            if (r.getId().equals("716735505840209950")) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }
    
    public static void sendPrivateMessage(String id, String content) { 
        User user = DiscordUtil.getJda().getUserById(id);
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    public static void sendLogMessage(String content) {
        chatbrlog.sendMessage(content).queue();
    }

    public static void sendBotMessage(String content) {
        chatbrbot.sendMessage(content).queue();
    }

    public static void sendBotEmbed(MessageEmbed content) {
        chatbrbot.sendMessageEmbeds(content).queue();
    }

    public static void sendError(@NotNull SlashCommandEvent event, String error) {
        event.replyEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(error)
                        .build()
        ).setEphemeral(true).queue();
    }

    public static void sendErrorHooked(@NotNull SlashCommandEvent event, String error) {
        event.getHook().sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(error)
                        .build()
        ).setEphemeral(true).queue();
    }

    public static String getDiscordId(UUID id){
        return DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(id);
    }

    public static String getDiscordName(String id) {
        User user = DiscordUtil.getJda().getUserById(id);
        if(user == null){
            return null;
        } else{
            return user.getName();
        }
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
