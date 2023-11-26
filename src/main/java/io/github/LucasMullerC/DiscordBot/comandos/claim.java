package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.ArrayList;
import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Gerencia.Claim;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Util.Mensagens;

public class claim {
    public claim(Message msg) {
        MessageChannel channel = msg.getChannel();
        List<User> Ulist = msg.getMentionedUsers();
        String[] cmd = msg.getContentRaw().split("\\s+");
        User u = null;
        Builder builder = new Builder();
        Builders B = null;

        if (Ulist.size() > 0) {
            u = Ulist.get(0);
            B = builder.getBuilderDiscord(u.getId());
        } else if (cmd.length > 1) {
            u = DiscordUtil.getUserById(cmd[1]);
            if (u != null) {
                B = builder.getBuilderDiscord(cmd[1]);
            }
        } else {
            u = msg.getAuthor();
            B = builder.getBuilderDiscord(msg.getAuthor().getId());
        }

        if (B == null) {
            channel.sendMessage(Mensagens.PerfilNotBuilderDiscord).queue();
        } else {
            int claimsCompletosNum, claimEmProgressoNum;
            ArrayList<Areas> completedClaims = new ArrayList<>();
            ArrayList<Areas> notcompletedClaims = new ArrayList<>();
            Claim claim = new Claim();

            claimEmProgressoNum = claim.getAreaQtdByPlayerNum(B.getUUID());
            claimsCompletosNum = claim.getAreaCompletaQtdByPlayerNum(B.getUUID());
            notcompletedClaims = claim.getAreaNotCompletedByPlayerUuid(B.getUUID());
            completedClaims = claim.getAreaCompletedByPlayerUuid(B.getUUID());

            Thumbnail thumb = new Thumbnail(u.getAvatarUrl(), null, 100, 100); // thumb
            Footer ft = new Footer("!perfil para ver seu perfil.", null, null);
            ImageInfo img = null;

            MessageEmbed emb2 = new MessageEmbed(null, Mensagens.PerfilDiscordTitle(u.getName()),
                    Mensagens.claimBody(claimsCompletosNum, claimEmProgressoNum, completedClaims, notcompletedClaims),
                    null, null, 52224, thumb, null, null, null, ft,
                    img, null);

            channel.sendMessage(emb2).queue();

        }
    }
}