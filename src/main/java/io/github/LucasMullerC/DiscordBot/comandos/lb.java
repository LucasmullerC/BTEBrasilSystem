package io.github.LucasMullerC.DiscordBot.comandos;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;

public class lb {
    public lb(Message msg) {
        Sistemas sistemas = new Sistemas();
        MessageChannel channel = msg.getChannel();
        String[] cmd = msg.getContentRaw().split("\\s+");
        int position = 0;

        if (cmd.length > 1) {
            int pageNumber = Integer.parseInt(cmd[1]);
            position = (pageNumber - 1) * 10;
        }

        Thumbnail thumb = new Thumbnail("https://i.imgur.com/M5E4LHt.png", null, 100, 100); // thumb
        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        MessageEmbed emb2 = new MessageEmbed(null, "Leaderboard", sistemas.FormLb(position), null, null, 52224, thumb, null,
                null, null, ft, null,
                null);
        channel.sendMessage(emb2).queue();
    }
}
