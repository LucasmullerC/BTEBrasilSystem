package io.github.LucasMullerC.discord.commands;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;

public class AddBuilds {
    public String getCommand(List<Role> roles,int builds,String discordId){
        boolean hasPermission = false;
        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            if (r.getId().equals("716735505840209950")) {
                hasPermission = true;
                break;
            }
        }
        if(hasPermission){
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderDiscord(discordId);
            if(builder == null){
                return MessageUtils.getMessageConsole("EquipeNotBuilder");
            } else{
                BuilderUtils.addBuilds(builder, builderService, builds);
                return MessageUtils.getMessageConsole("Sucesso");
            }
        } else {
            return MessageUtils.getMessageConsole("Perm1");
        }
    }
}
