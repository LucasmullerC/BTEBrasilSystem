package io.github.LucasMullerC.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.AuthorInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import io.github.LucasMullerC.model.Awards;

public class DiscordUtils {
    public static MessageEmbed getAwardEmbed(Awards award,String playerId){
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(playerId));
        Thumbnail thumb = new Thumbnail("https://i.imgur.com/M5E4LHt.png", null, 100, 100); // thumb~
        AuthorInfo auth = new AuthorInfo(player.getName(), null,"https://crafatar.com/avatars/" + playerId, null);
        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        ImageInfo img = new ImageInfo(award.getURL(), null, 105, 30);

        MessageEmbed emb2 = new MessageEmbed(null, MessageUtils.getMessageConsole("Parabens"),
        awardMessage(String.valueOf(award.getPoints()),award.getName(),award.getID()), null, null, 52224,
        thumb, null, auth, null, ft,
        img, null);

        return emb2;
    }

    public static MessageEmbed getNextLevelEmbed(String rank,Player player){
        Thumbnail thumb = new Thumbnail("https://i.imgur.com/M5E4LHt.png", null, 100, 100); // thumb
        AuthorInfo auth = new AuthorInfo(player.getName(), null,"https://crafatar.com/avatars/" + player.getUniqueId().toString(), null);
        Footer ft = new Footer("Build The Earth: Brasil", null, null);
        MessageEmbed emb2 = new MessageEmbed(null, MessageUtils.getMessageConsole("Parabens"),
        nextLevelMessage(rank), null, null, 52224, thumb, null, auth, null, ft,
        null, null);
        return emb2;
    }

    private static String nextLevelMessage(String rank){
        return MessageUtils.getMessageConsole("nextlevelbody1")+" " + rank + "**!\r\n"
        + MessageUtils.getMessageConsole("nextlevelbody2");
    }

    private static String awardMessage(String points, String name, String id) {
        return MessageUtils.getMessageConsole("awardbody1") + name + " (" + id + ")**!\r\n" + MessageUtils.getMessageConsole("awardbody2") + points
                + MessageUtils.getMessageConsole("awardbody3")+" " + id + MessageUtils.getMessageConsole("awardbody4");
    }
}
