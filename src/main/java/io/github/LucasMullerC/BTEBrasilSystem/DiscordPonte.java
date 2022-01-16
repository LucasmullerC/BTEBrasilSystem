package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.AuthorInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Conquistas;
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
        chatbrlog.sendMessage(cont.toString() + Mensagens.TimesUpAdm).queue();
    }
    public static void EditarMsg(Areas A) {
        chatbrlog.sendMessage(Mensagens.OClaim+"**"+A.getClaim()+"**"+Mensagens.ClaimEditarDiscord+"**"+A.getBuilds()+"**").queue();
    }
    public static void ImgAdicionada(Areas A,String Link) {
        chatbrlog.sendMessage(Mensagens.NovaImgMsg(A.getClaim(), Link)).queue();
    }

    public static void NextLevel(String rank,String Discord,Player player) {
        TextChannel BTEbrBots = DiscordUtil.getTextChannelById("717528314423803974");

        Thumbnail thumb = new Thumbnail("https://i.imgur.com/M5E4LHt.png", null, 100, 100); // thumb
        AuthorInfo auth = new AuthorInfo(player.getDisplayName(), null,
                "https://crafatar.com/avatars/"+player.getUniqueId().toString(), null);
        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        MessageEmbed emb2 = new MessageEmbed(null, Mensagens.Parabens,
                Mensagens.NextLevelDiscord(rank), null, null, 52224, thumb, null, auth, null, ft,
                null, null);
        //MUDAR CHAT DE BOTS BTE BR OU PRIVADO        
        BTEbrBots.sendMessage("<@"+Discord+">").queue();
        BTEbrBots.sendMessage(emb2).queue();
    }
    public static void Awards(Conquistas C,String Discord,String uid) {
        TextChannel BTEbrBots = DiscordUtil.getTextChannelById("717528314423803974");

        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uid));
        Thumbnail thumb = new Thumbnail("https://i.imgur.com/M5E4LHt.png", null, 100, 100); // thumb
        AuthorInfo auth = new AuthorInfo(player.getName(), null,
                "https://crafatar.com/avatars/"+player.getUniqueId().toString(), null);
        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        ImageInfo img = new ImageInfo(C.getURL(), null, 105, 30); //Award talvez?
        MessageEmbed emb2 = new MessageEmbed(null, Mensagens.Parabens,
                Mensagens.ConquistaDiscord(String.valueOf(C.getPontos()),C.getNome(),C.getID()), null, null, 52224, thumb, null, auth, null, ft,
                img, null);
        //MUDAR CHAT DE BOTS BTE BR OU PRIVADO        
        BTEbrBots.sendMessage("<@"+Discord+">").queue();
        BTEbrBots.sendMessage(emb2).queue();
    }

    public static String GetDiscordName(String id) {
        User user = DiscordUtil.getJda().getUserById(id);
        return user.getName();
    }

    public static void AnalisarReserva(String time, String Discord) {
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
        chatbrlog.sendMessage("<@&" + Equipe + "> " + Mensagens.PendenteMsg).queue();
        chatbrlog.sendMessage("Usuário: " + GetDiscordName(Discord)).queue();
    }

    public static void AnalisarClaim(String area, String Discord) {
        // MUDAR PARA chatbrlog
        chatbrlog.sendMessage("<@&826599049297264640> O usuário: **" + GetDiscordName(Discord) + "**"
                + Mensagens.PendenteMsgClaim1 + "**" + area + "**" + Mensagens.PendenteMsgClaim2).queue();
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
