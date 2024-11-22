package io.github.LucasMullerC.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Awards;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.AwardService;
import io.github.LucasMullerC.service.builder.BuilderService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class BuilderUtils {
    public static void checkRank(Builder builder, BuilderService builderService){
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
                    builderService.updateBuilder(builder);
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

                    DiscordActions.sendBotMessage("<@" + builder.getDiscord() + ">");
                    DiscordActions.sendBotEmbed(DiscordUtils.getNextLevelEmbed(String.valueOf(tier), player));
                }
            }
        }
    }

    public static String toNextLevel(Builder builder){
        int tier = builder.getTier();
        double nextLevel = (tier * 150) * 2.25;
        return String.valueOf(nextLevel);
    }

    public static void CheckAwardsBuilds(Builder builder,BuilderService builderService){
        if (builder.getBuilds() >= 1) {
            addAward(builder,builderService,"1build");
        }
        if (builder.getBuilds() >= 10) {
            addAward(builder,builderService,"10build");
        }
        if (builder.getBuilds() >= 30) {
            addAward(builder,builderService,"30build");
        }
        if (builder.getBuilds() >= 50) {
            addAward(builder,builderService,"50build");
        }
        if (builder.getBuilds() >= 100) {
            addAward(builder,builderService,"100build");
        }
        if (builder.getBuilds() >= 300) {
            addAward(builder,builderService,"300build");
        }
        if (builder.getBuilds() >= 500) {
            addAward(builder,builderService,"500build");
        }
        if (builder.getBuilds() >= 500) {
            addAward(builder,builderService,"1000build");
        }
    }

    public static void addBuilds(Builder builder, BuilderService builderService,int newBuilds){
        Integer oldBuilds = builder.getBuilds();
        builder.setBuilds(oldBuilds+newBuilds);
        builderService.updateBuilder(builder);
    }

    public static void removeBuilds(Builder builder, BuilderService builderService,int removedBuilds){
        Integer oldBuilds = builder.getBuilds();
        builder.setBuilds(oldBuilds-removedBuilds);
        builderService.updateBuilder(builder);
    }

    public static void addPoints(Builder builder, BuilderService builderService,double newPoints){
        double oldPoints = builder.getPoints();
        builder.setPoints(oldPoints+newPoints);
        builderService.updateBuilder(builder);
    }

    public static void removePoints(Builder builder, BuilderService builderService,double removedPoints){
        double oldPoints = builder.getPoints();
        builder.setPoints(oldPoints-removedPoints);
        builderService.updateBuilder(builder);
    }

    public static void addAward(Builder builder, BuilderService builderService, String award){
        String awards = builder.getAwards();
        String[] awardList = awards.split(",");
        Boolean hasAward = false;
        for (int i = 0; i < awardList.length; i++) {
            if (awardList[i].equals(award)) {
                hasAward = true;
                break;
            }
        }
        if(hasAward == false){
            AwardService awardService = new AwardService();
            Awards singleAward = awardService.getAward(award);
            if(awards.equals("nulo")){
                builder.setAwards(award);
            } else{
                awards = awards + "," + award;
                builder.setAwards(awards);
            }
            //Saving(Updating) inside addPoints
            addPoints(builder, builderService, singleAward.getPoints());
            DiscordActions.sendBotMessage("<@" + builder.getDiscord() + ">");
            DiscordActions.sendBotEmbed(DiscordUtils.getAwardEmbed(singleAward, builder.getUUID()));
        }
    }

    public static void removeAward(Builder builder, BuilderService builderService, String award){
        String awards = builder.getAwards();
        if (awards == null || awards.equals("nulo")) {
            return;
        }

        List<String> awardList = new ArrayList<>(Arrays.asList(awards.split(",")));
        boolean hasAward = awardList.removeIf(a -> a.equals(award));

        if(hasAward){
            AwardService awardService = new AwardService();
            Awards singleAward = awardService.getAward(award);
            if(awards.equals("nulo")){
                builder.setAwards("nulo");
            } else{
                builder.setAwards(String.join(",", awardList));
            }
            //Saving(Updating) inside removePoints
            removePoints(builder, builderService, singleAward.getPoints());
        }
    }
}
