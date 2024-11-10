package io.github.LucasMullerC.discord.commands;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;

public class AddPoints {
    public String getCommand(List<Role> roles,int points,String discordId){
        boolean hasPermission = DiscordActions.checkAdmin(roles);
        if(hasPermission){
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderDiscord(discordId);
            if(builder == null){
                return MessageUtils.getMessageConsole("EquipeNotBuilder");
            } else{
                BuilderUtils.addPoints(builder, builderService, points);
                return MessageUtils.getMessageConsole("Sucesso");
            }
        } else {
            return MessageUtils.getMessageConsole("Perm1");
        }
    }
}
