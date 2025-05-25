package io.github.LucasMullerC.discord.commands;

import java.util.ArrayList;
import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.MessageUtils;

public class Plots {
    public MessageEmbed getCommand(List<Role> roles){
        boolean hasPermission = DiscordActions.checkAdmin(roles);
        if(hasPermission){
            ClaimService claimService = new ClaimService();
            ArrayList<Claim> claimList = claimService.getClaimList();
            String incompletePlotList = "";
            for(Claim claim:claimList){
                if(claim.getDifficulty()>0 && claim.getStatus().equals("F")){
                    if(claim.getParticipants().equals("nulo")){
                        incompletePlotList += MessageUtils.getMessageConsole("claimbody4") + claim.getClaim();
                    } else{
                        incompletePlotList += MessageUtils.getMessageConsole("claimbody4") + claim.getClaim()+MessageUtils.getMessageConsole("plotbody2") + claim.getParticipants()+"**";
                    }
                }
            }
            Thumbnail thumb = new Thumbnail(MessageUtils.getMessagePT("slashlbthumb"), null, 100, 100);
            Footer ft = new Footer(MessageUtils.getMessagePT("footerbtebrasil"), null, null);
            String title = MessageUtils.getMessagePT("plotbody1");
            MessageEmbed messageEmbed = new MessageEmbed(null, title, incompletePlotList, null,
                null, 52224, thumb, null, null, null, ft, null,
                null);
            return messageEmbed;
        } else{
            MessageEmbed messageEmbed = new MessageEmbed(null, null, MessageUtils.getMessagePT("Perm1"), null,
                null, 52224, null, null, null, null, null, null,
                null);
            return messageEmbed; 
        }
    }
}
