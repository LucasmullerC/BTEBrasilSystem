package io.github.LucasMullerC.discord.commands;

import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import io.github.LucasMullerC.model.Awards;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.AwardService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.util.MessageUtils;

public class Destacar {
    public String getCommand(User sender,String awardId) {
        BuilderService builderService = new BuilderService();
        String discordId = sender.getId().toString();

        Builder builder = builderService.getBuilderDiscord(discordId);
        if(builder == null){
            return MessageUtils.getMessagePT("PerfilNotBuilderDiscord")+" \r\n"+MessageUtils.getMessagePT("PerfilNotBuilderDiscordlink");
        } else{
            AwardService awardService = new AwardService();
            Awards award = awardService.getAward(awardId);
            if(award == null){
                return MessageUtils.getMessagePT("conquistaNotFound");
            } else{
                if(hasAward(builder, awardId)){
                    builder.setFeatured(awardId);
                    builderService.updateBuilder(builder);
                    return MessageUtils.getMessagePT("conquistaDestaque");
                } else{
                    return MessageUtils.getMessagePT("conquistaPermission");
                }
            }
        }
    }

    public boolean hasAward(Builder builder,String awardId){
        String awards = builder.getAwards();
        String[] awardList = awards.split(",");
        Boolean hasAward = false;
        for (int i = 0; i < awardList.length; i++) {
            if (awardList[i].equals(awardId)) {
                hasAward = true;
                break;
            }
        }
        return hasAward;
    }
}
