package io.github.LucasMullerC.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.model.Builder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BuilderUtils {
    public static void checkRank(Builder builder){
        OfflinePlayer Offplayer = Bukkit.getOfflinePlayer(UUID.fromString(builder.getUUID()));
        if (builder != null) {
            boolean leveledUp = false;
            int initalTier = builder.getTier();
            int tier = builder.getTier();
            String stringToNextLvl = "";
            do{
                tier = builder.getTier();
                double NextLvl = (tier * 150) * 2.25;
    
                if (builder.getPoints() >= NextLvl) {
                    Integer newtier = tier + 1;
                    builder.setTier(newtier);
                    leveledUp = true;
                }
                else{
                    double toNextLvl = NextLvl - builder.getPoints();
                    stringToNextLvl = String.valueOf(toNextLvl);
                    stringToNextLvl = stringToNextLvl.substring(0, stringToNextLvl.indexOf(".") + 2);
                    leveledUp = false;
                }
            }while(leveledUp == true);

            if (Offplayer.isOnline()) {
                Player player = Offplayer.getPlayer();
                if(initalTier == tier){          
                    player.sendMessage(Component.text(MessageUtils.getMessage("Level1", player))
                    .append(Component.text(stringToNextLvl)
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.BOLD, true)).append(Component.text(MessageUtils.getMessage("Level2", player))));
                }
                else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("NextLevel1", player)).color(NamedTextColor.GREEN));
                    player.sendMessage(Component.text(MessageUtils.getMessage("NextLevel2", player)).color(NamedTextColor.GOLD));

                    //TODO: ENVIAR MSG NO DISCORD
                    //DiscordPonte.NextLevel(newtier.toString(), B.getDiscord(), player);
                }
            }
        }
    }
}
