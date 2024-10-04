package io.github.LucasMullerC.util;

import java.util.UUID;
import java.text.Normalizer;

import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.service.pending.PendingService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ClaimUtils {
    public static String buildClaim(Player player,String input,String selectionPoints){
        String claimId;
        UUID id = player.getUniqueId();
        Integer cont = 0;
        ClaimService claimService = new ClaimService();
        do {
            cont++;
            claimId = Normalizer.normalize(input, Normalizer.Form.NFD);
            claimId = claimId.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            claimId = claimId.replaceAll(" ", "") + cont.toString();
            claimId = claimId.replaceAll("[\\[\\](){}]", "");
            claimId = claimId.toLowerCase();
        } while (claimService.getClaim(claimId) != null);
        Claim claim = new Claim(claimId);
        claim.setName(input);
        claim.setPoints(selectionPoints);
        claim.setPlayer(id.toString());
        claim.setImage("nulo");
        claim.setStatus("F");
        claim.setParticipants("nulo");
        claim.setBuilds(0);
        claimService.addClaim(claim,player);

        return claimId;
    }

    public static void finalizeClaim(Player player,Claim claim, String builds){
        String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        String discordName = DiscordActions.getDiscordName(discordId);
        PendingService pendingService = new PendingService();
        Pending pending = new Pending(player.getUniqueId().toString());
        pending.setregionId(claim.getClaim());
        pending.setisApplication(false);
        pending.setbuilds(builds);
        pendingService.addPending(pending);

        DiscordActions.sendLogMessage("<@&826599049297264640> "+MessageUtils.getMessageConsole("alertadminuser")+
        " **"+discordName+"** "+MessageUtils.getMessageConsole("PendenteMsgClaim1")+"**"+claim.getClaim()+"**"+
        MessageUtils.getMessageConsole("PendenteMsgClaim2"));
    }

    public static boolean verifyClaimProperties(Claim claim,Player player,Boolean edit){
        UUID id = player.getUniqueId();
        if(claim != null){
            if(claim.getPlayer().equals(id.toString())){
                if(claim.getStatus().equals("F") || edit == true){
                    return true;
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCompleto2", player)).color(NamedTextColor.GOLD));
                    return false;
                }
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("ClaimPlayer", player)).color(NamedTextColor.RED));
                return false;
            }
        }
        else{
            player.sendMessage(Component.text(MessageUtils.getMessage("ClaimNotFound", player)).color(NamedTextColor.RED));
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
