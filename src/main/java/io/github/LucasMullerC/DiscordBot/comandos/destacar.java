package io.github.LucasMullerC.DiscordBot.comandos;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Util.Mensagens;

public class destacar {
    public destacar(Message msg) {
        MessageChannel channel = msg.getChannel();
        String[] cmd = msg.getContentRaw().split("\\s+");
        if (cmd.length < 2) {
            channel.sendMessage(Mensagens.destaque).queue();
        } else {
            Builder builder = new Builder();
            Builders B = builder.getBuilderDiscord(msg.getAuthor().getId());
            if (B == null) {
                channel.sendMessage(Mensagens.Notconstrutor2).queue();
            } else {
                String part = B.getAwards();
                String[] div = part.split(",");
                boolean vef = false;
                for (int i = 0; i < div.length; i++) {
                    if (cmd[1].equals(div[i])) {
                        vef = true;
                        builder.setDestaque(cmd[1], B.getUUID());
                        channel.sendMessage(Mensagens.conquistaDestaque).queue();
                        break;
                    }
                }
                if (vef == false) {
                    channel.sendMessage(Mensagens.conquistaPermission).queue();
                }
            }
        }
    }
}
