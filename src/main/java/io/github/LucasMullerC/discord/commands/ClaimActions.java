package io.github.LucasMullerC.discord.commands;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.MessageUtils;

public class ClaimActions {
    public String getCommand(List<Role> roles,String awardId,String claimId,boolean isEvent,boolean remove){
        boolean hasPermission = DiscordActions.checkAdmin(roles);
        if(hasPermission){
            ClaimService claimService = new ClaimService();
            Claim claim = claimService.getClaim(claimId);
            if(claim == null){
                return MessageUtils.getMessagePT("ClaimNotFound");
            } else{
                if(remove){
                    return MessageUtils.getMessagePT("wip");
                } else{
                    claim.setEvent(isEvent);
                    if(!awardId.equals("nulo")){
                        claim.setAward(awardId);
                    }
                    claimService.saveClaim();
                }
                return MessageUtils.getMessagePT("Sucesso");
            }
        } else {
            return MessageUtils.getMessagePT("Perm1");
        }
    }
    
}
