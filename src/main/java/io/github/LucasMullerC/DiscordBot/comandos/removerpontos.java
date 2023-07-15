package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Util.Mensagens;

public class removerpontos {
    public removerpontos(Message msg) {
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
            if (cmd.length < 3) {
                channel.sendMessage(Mensagens.addpontos).queue();
            } else {
                Builder builder = new Builder();
                Builders B = builder.getBuilderDiscord(cmd[1]);
                if (B == null) {
                    channel.sendMessage(Mensagens.EquipeNotBuilder).queue();
                } else {
                    if (isNumeric(cmd[2]) == true) {
                        builder.removePontos(B.getUUID(), Double.parseDouble(cmd[2]));
                        channel.sendMessage(Mensagens.Sucesso).queue();
                    } else {
                        channel.sendMessage(Mensagens.MustBeNumber).queue();
                    }
                }
            }
        } else {
            channel.sendMessage(Mensagens.Perm1).queue();
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
