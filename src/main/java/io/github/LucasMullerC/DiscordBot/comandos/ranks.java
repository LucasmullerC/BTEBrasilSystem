package io.github.LucasMullerC.DiscordBot.comandos;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import io.github.LucasMullerC.Util.Mensagens;

public class ranks {
    public ranks(Message msg) {
        MessageChannel channel = msg.getChannel();

        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        MessageEmbed emb2 = new MessageEmbed(null,Mensagens.RanksTitulo,Mensagens.ListaRanks(), null, null, 52224, null, null, null, null, ft,null, null);
        channel.sendMessage(emb2).queue();
    }

}
