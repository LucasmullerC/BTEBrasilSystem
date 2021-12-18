package io.github.LucasMullerC.BTEBrasilSystem;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.Util.Mensagens;

public class DiscordPonte {
    static TextChannel chatne = DiscordUtil.getTextChannelById("809900341436350484");
    static TextChannel chatsp = DiscordUtil.getTextChannelById("701909180054765609");
    static TextChannel chatsul = DiscordUtil.getTextChannelById("782698775356375090");
    static TextChannel chatco = DiscordUtil.getTextChannelById("796466946001993749");
    static TextChannel chatmg = DiscordUtil.getTextChannelById("812707639200055327");
    static TextChannel chatrj = DiscordUtil.getTextChannelById("781654144108331038");
    static TextChannel chates = DiscordUtil.getTextChannelById("801625498026377226");
    static TextChannel chatbr = DiscordUtil.getTextChannelById("717528858777354272");
    static TextChannel chatbrlog = DiscordUtil.getTextChannelById("921811170086776942");

    public static boolean CheckDiscord(String id, String Server) {
        Guild guild = DiscordUtil.getJda().getGuildById(Server); // ID do discord do time
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

    public static String getDiscordByName(String Discord) {
        User user = DiscordUtil.getJda().getUserByTag(Discord);
        return user.getId();

    }

    public static void sendMessage(String id, String content) { // msg no privado
        User user = DiscordUtil.getJda().getUserById(id);
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    public static void TimesUpMsg(Integer cont) {
        chatbr.sendMessage(cont.toString() + Mensagens.TimesUpAdm).queue();
    }

    public static String GetDiscordName(String id) {
        User user = DiscordUtil.getJda().getUserById(id);
        return user.getName();
    }

    public static void AnalisarReserva(String time,String Discord) {
        String Equipe = "";
        if (time.equals("b_ne")) {
            Equipe = "921809374819799181";
        } else if (time.equals("b_sp")) {
            Equipe = "921809921492787230";
        } else if (time.equals("b_sul")) {
            Equipe = "921810096131043418";
        } else if (time.equals("b_co")) {
            Equipe = "921810711364120576";
        } else if (time.equals("b_mg")) {
            Equipe = "921810239735595021";
        } else if (time.equals("b_es")) {
            Equipe = "921810397391110184";
        } else if (time.equals("b_rj")) {
            Equipe = "921810397391110184";
        }
        chatbrlog.sendMessage("<@&"+Equipe+"> " + Mensagens.PendenteMsg).queue();
        chatbrlog.sendMessage("Usuário: " + GetDiscordName(Discord)).queue();
    }

    public static void addCargo(String UUID, String time, String Discord) {
        if (CheckDiscord(Discord, "715528474655326238") == true) {
            Guild guild = DiscordUtil.getJda().getGuildById("715528474655326238"); // BTE BR/PT
            Role role = guild.getRoleById("721861941517353021"); // Construtor br/pt
            Member member = guild.getMemberById(Discord);
            guild.addRoleToMember(member, role).queue();
        }
        if (time.equals("b_ne")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("735561960254341121"); // ID do discord do time
            Role roleT = guildT.getRoleById("815586676159676467");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("sp")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("695813656490803223");
            Role roleT = guildT.getRoleById("696887529944645716");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("sul")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("782652814831779850");
            Role roleT = guildT.getRoleById("782653956157472820");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("co")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("796238091207180338");
            Role roleT = guildT.getRoleById("796465391894790154");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("mg")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("701801798154846298");
            Role roleT = guildT.getRoleById("701816986081951895");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("es")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("801618489600901161");
            Role roleT = guildT.getRoleById("801623044812177418");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        } else if (time.equals("rj")) {
            Guild guildT = DiscordUtil.getJda().getGuildById("738444755330924625");
            Role roleT = guildT.getRoleById("776573196097814551");
            Member memberT = guildT.getMemberById(Discord);
            guildT.addRoleToMember(memberT, roleT).queue();
        }
    }

}
