package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.Mensagens;

public class conquistas {
    public conquistas(Message msg) {

        MessageChannel channel = msg.getChannel();
        List<User> Ulist = msg.getMentionedUsers();
        String[] cmd = msg.getContentRaw().split("\\s+");
        Builders B = null;
        User u = null;
        Builder builder = new Builder();
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
            ImageInfo img = null;
            if (!B.getDestaque().equals("nulo")) {
                Conquistas C = builder.getConquistaPos(B.getDestaque());
                img = new ImageInfo(C.getURL(), null, 105, 30);
            }
            Sistemas sistemas = new Sistemas();
            Thumbnail thumb = new Thumbnail(u.getAvatarUrl(), null, 100, 100); // thumb
            Footer ft = new Footer("Acesse https://bit.ly/31TXIfQ Para ver todas as conquistas.", null, null);
            MessageEmbed emb2 = new MessageEmbed(null, "Conquistas - " + u.getName(), sistemas.ListConquistas(B), null,
                    null, 52224, thumb, null, null, null, ft, img,
                    null);
            channel.sendMessage(emb2).queue();
        }
    }
}
