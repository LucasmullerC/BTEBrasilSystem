package io.github.LucasMullerC.discord.commands;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import io.github.LucasMullerC.util.MessageUtils;

public class Ranks {
    public MessageEmbed getCommand() {
        Thumbnail thumb = new Thumbnail(MessageUtils.getMessagePT("slashlbthumb"), null, 100, 100);
        Footer ft = new Footer(MessageUtils.getMessagePT("footerbtebrasil"), null, null);
        MessageEmbed emb2 = new MessageEmbed(null, MessageUtils.getMessagePT("slashrankstitle"), MessageUtils.getMessagePT("slasbranksbody"), null, null, 52224, thumb, null,
        null, null, ft, null,
        null);
return emb2;
    }
}
