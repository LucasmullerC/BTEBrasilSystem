package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.Mensagens;

public class addconquista {
    public addconquista(Message msg) {
        MessageChannel channel = msg.getChannel();
        String[] cmd = msg.getContentRaw().split("\\s+");

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
            if (cmd.length < 4) {
                channel.sendMessage(Mensagens.addconquista).queue();
            } else {
                Builder builder = new Builder();
                Conquistas C = builder.getConquistaPos(cmd[1]);
                if (C == null) {
                    String name = "";
                    for (int i = 4; i < cmd.length; i++) {
                        name += cmd[i] + " ";
                    }
                    builder.addConquista(cmd[1], Double.parseDouble(cmd[2]), cmd[3], name);
                    channel.sendMessage(Mensagens.conquistaAdd2).queue();
                } else {
                    channel.sendMessage(Mensagens.conquistaAlreadyExist).queue();
                }
            }
        } else {
            channel.sendMessage(Mensagens.Perm1).queue();
        }

    }
}
