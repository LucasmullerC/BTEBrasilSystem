package io.github.LucasMullerC.util;

import java.util.UUID;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
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

    public static void addParticipant(Claim claim, Player player, ClaimService claimService){
        String id = player.getUniqueId().toString();
        String participants = claim.getParticipants();
        if(participants.equals("nulo")){
            claim.setParticipants(id);
        } else{
            String [] participantList = participants.split(",");
            if (!Arrays.stream(participantList).anyMatch(id::equals)) {
                participants += "," + id;
                claim.setParticipants(participants);
            } else {
                player.sendMessage(Component.text(MessageUtils.getMessage("Equipe2", player)).color(NamedTextColor.RED));
                return;
            }
        }

        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.addPermissionWG(claim.getClaim(), player, player.getUniqueId());
        claimService.saveClaim();
        player.sendMessage(Component.text(MessageUtils.getMessage("Equipe1", player)).color(NamedTextColor.GREEN));
    }

    public static void removeParticipant(Claim claim, Player player,Player participant, ClaimService claimService){
        String participantId = participant.getUniqueId().toString();
        String participants = claim.getParticipants();
        if(participants.equals("nulo")){
            player.sendMessage(Component.text(MessageUtils.getMessage("EquipeNotFound", player)).color(NamedTextColor.RED));
            return;
        } else{
            String [] participantList = participants.split(",");
            if (!Arrays.stream(participantList).anyMatch(participantId::equals)) {
                player.sendMessage(Component.text(MessageUtils.getMessage("EquipeNotFound", player)).color(NamedTextColor.RED));
                return;
            } else{
                String newParticipantList = "nulo";
                for(int i = 0; i < participantList.length; i++){
                    if(!participantList[i].equals(participantId)) {
                        if(!participantList[i].equals("nulo")){
                            if(newParticipantList.equals("nulo")){
                                newParticipantList = participantList[i];
                            } else{
                                newParticipantList += "," + participantList[i];
                            }
                        }
                    }
                }
                claim.setParticipants(newParticipantList);
                claimService.saveClaim();
                WorldGuardService worldGuardService = new WorldGuardService();
                worldGuardService.removePermissionWG(claim.getClaim(), player, participant.getUniqueId());
                if(player.getUniqueId().toString().equals(participantId)){
                    player.sendMessage(Component.text(MessageUtils.getMessage("RemovidoEquipe", player)).color(NamedTextColor.GREEN));
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("EquipeRemove", player)).color(NamedTextColor.GREEN));
                }
                return;
            }
        }
    }

    public static void addImage(Claim claim, String inputUrl, Player player, ClaimService claimService){
        try {
            URL url = new URL(inputUrl);
            String images = claim.getImage();
            if(images.equals("nulo")){
                claim.setImage(inputUrl);
            } else{
                images += "," + inputUrl;
                claim.setImage(images);
            }
            claimService.saveClaim();
            DiscordActions.sendLogMessage(MessageUtils.getMessageConsole("newimageadded1")+
            claim.getClaim()+MessageUtils.getMessageConsole("newimageadded2")+" "+inputUrl);

            player.sendMessage(Component.text(MessageUtils.getMessage("ImgAdd", player)).color(NamedTextColor.GREEN));
        } catch (MalformedURLException e) {
            player.sendMessage(Component.text(MessageUtils.getMessage("LinkImg", player)).color(NamedTextColor.RED));
        }
    }

public static void removeImage(Claim claim, String imageId, Player player, ClaimService claimService) {
    String[] images = claim.getImage().split(",");

    try {
        int index = Integer.parseInt(imageId) - 1;

        if (index >= 0 && index < images.length) {
            List<String> imageList = new ArrayList<>(Arrays.asList(images));
            imageList.remove(index);
            if (imageList.isEmpty()) {
                claim.setImage("nulo");
            } else {
                claim.setImage(String.join(",", imageList));
            }
            player.sendMessage(Component.text(MessageUtils.getMessage("ImgRemovida", player)).color(NamedTextColor.GREEN));
            claimService.saveClaim();
        } else {
            player.sendMessage(Component.text(MessageUtils.getMessage("imageidnotfound", player)).color(NamedTextColor.RED));
        }
    } catch (NumberFormatException e) {
        player.sendMessage(Component.text(MessageUtils.getMessage("imageidnotfound", player)).color(NamedTextColor.RED));
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
