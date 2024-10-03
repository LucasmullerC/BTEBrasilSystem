package io.github.LucasMullerC.util;

import java.util.UUID;
import java.text.Normalizer;

import org.bukkit.entity.Player;

import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.claim.ClaimService;
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

    public static boolean verifyClaimProperties(Claim claim,Player player,Boolean edit){
        UUID id = player.getUniqueId();
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

}
