package io.github.LucasMullerC.discord.commands;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Awards;
import io.github.LucasMullerC.service.AwardService;
import io.github.LucasMullerC.util.MessageUtils;

public class CreateAward {
    public String getCommand(List<Role> roles,int points,String awardId,String url,String name){
        boolean hasPermission = DiscordActions.checkAdmin(roles);
        if(hasPermission){
            AwardService awardService = new AwardService();
            Awards award = awardService.getAward(awardId);
            if(award == null){
                Awards newAward = new Awards(awardId);
                newAward.setName(name);
                newAward.setPoints(points);
                newAward.setURL(url);
                awardService.addAward(newAward);
                return MessageUtils.getMessageConsole("conquistaAdd2");
            } else {
                return MessageUtils.getMessageConsole("conquistaAlreadyExist");
            }
        } else {
            return MessageUtils.getMessageConsole("Perm1");
        }
    }
}
