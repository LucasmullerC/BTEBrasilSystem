package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.Mensagens;

public class giveconquista {
    public giveconquista(Message msg) {
        MessageChannel channel = msg.getChannel();
        Member M = msg.getMember();
        List<Role> roles = M.getRoles();
        Boolean Vef = false;
        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            if (r.getId().equals("716735505840209950")) {
                Vef = true;
                break;
            }
        }
        if (Vef == true) {
            Builder builder = new Builder();
            String[] cmd = msg.getContentRaw().split("\\s+");
            if (cmd.length < 3) {
                channel.sendMessage(Mensagens.giveconquista).queue();
            } else {
                Builders B = builder.getBuilderDiscord(cmd[1]);
                if (B == null) {
                    channel.sendMessage(Mensagens.EquipeNotBuilder).queue();
                } else {
                    Conquistas C = builder.getConquistaPos(cmd[2]);
                    if (C == null) {
                        channel.sendMessage(Mensagens.conquistaNotFound).queue();
                    } else {
                        builder.setConquistas(cmd[2], B.getUUID());
                        channel.sendMessage(Mensagens.conquistaAdd).queue();
                    }
                }
            }
        } else {
            channel.sendMessage(Mensagens.Perm1).queue();
        }

    }

}
