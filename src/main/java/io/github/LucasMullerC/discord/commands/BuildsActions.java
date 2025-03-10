package io.github.LucasMullerC.discord.commands;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;

public class BuildsActions {
    public String getCommand(List<Role> roles,int builds,String discordId,boolean remove){
        boolean hasPermission = DiscordActions.checkAdmin(roles);
        if(hasPermission){
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderDiscord(discordId);
            if(builder == null){
                return MessageUtils.getMessagePT("EquipeNotBuilder");
            } else{
                if(remove){
                    BuilderUtils.removeBuilds(builder, builderService, builds);
                } else{
                    BuilderUtils.addBuilds(builder, builderService, builds);
                }
                return MessageUtils.getMessagePT("Sucesso");
            }
        } else {
            return MessageUtils.getMessagePT("Perm1");
        }
    }
}
