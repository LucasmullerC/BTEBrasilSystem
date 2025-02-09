package io.github.LucasMullerC.util;

import java.util.UUID;
import java.util.stream.Collectors;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static String buildClaim(Player player,String input,String selectionPoints,boolean event,String AwardUrl){
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
        claim.setEvent(event);
        claim.setAward(AwardUrl);
        claimService.addClaim(claim,player);

        return claimId;
    }

    public static String buildPlayerClaim(Player player,String input,String selectionPoints,int difficulty){
        String claimId;
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
        claim.setPlayer("nulo");
        claim.setImage("nulo");
        claim.setStatus("F");
        claim.setParticipants("nulo");
        claim.setBuilds(0);
        claim.setEvent(false);
        claim.setAward("nulo");
        claim.setDifficulty(difficulty);
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

        //DiscordActions.sendLogMessage("<@&826599049297264640> "+MessageUtils.getMessageConsole("alertadminuser")+
        //" **"+discordName+"** "+MessageUtils.getMessageConsole("PendenteMsgClaim1")+"**"+claim.getClaim()+"**"+
        //MessageUtils.getMessageConsole("PendenteMsgClaim2"));

        DiscordActions.sendLogMessage("<@&teste60> "+MessageUtils.getMessageConsole("alertadminuser")+
        " **"+discordName+"** "+MessageUtils.getMessageConsole("PendenteMsgClaim1")+"**"+claim.getClaim()+"**"+
        MessageUtils.getMessageConsole("PendenteMsgClaim2"));
    }

    public static void CompleteClaim(Claim claim, ClaimService claimService){
        claim.setStatus("T");
        claimService.updateClaim(claim);
        WorldGuardService worldguardService = new WorldGuardService();
        worldguardService.AddFlags(claim);
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
        if(id.equals(claim.getPlayer())){
            player.sendMessage(Component.text(MessageUtils.getMessage("Equipe2", player)).color(NamedTextColor.RED));
            return;
        }
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

    public static void addBuilds(Claim claim,ClaimService claimService,int newBuilds){
        int builds = claim.getBuilds();
        claim.setBuilds(newBuilds+builds);
        claimService.updateClaim(claim);
    }
    
    public static Map<String, Object> getClaimInfos(String playerId) {
        int qtdArea = 0;
        int qtdAreaCompleta = 0;
        ArrayList<Claim> notCompleted = new ArrayList<>();
        ArrayList<Claim> completed = new ArrayList<>();
        ArrayList<Claim> claims = new ArrayList<>();
        ClaimService claimService = new ClaimService();
        claims = claimService.getClaimList();

        for (Claim claim : claims) {
            if (claim.getPlayer() != null && claim.getPlayer().contains(playerId)) {
                if (claim.getStatus().equals("F")) {
                    qtdArea++;
                    notCompleted.add(claim);
                } else if (claim.getStatus().equals("T")) {
                    qtdAreaCompleta++;
                    completed.add(claim);
                }
            }
        }
    
        Map<String, Object> result = new HashMap<>();
        result.put("qtdClaim", qtdArea);
        result.put("qtdCompleted", qtdAreaCompleta);
        result.put("notCompleted", notCompleted);
        result.put("completed", completed);
    
        return result;
    }

    public static void printClaimsMinecraft(ArrayList<Claim> claims,Player player,int infoPage){
        int itemsPerPage = 5;
        int totalItems = claims.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
    
        int startIndex = (infoPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        if(totalItems < 1){
            return;
        }

        if (infoPage > totalPages) {
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopageinvalid", player) + totalPages).color(NamedTextColor.RED));
            return;
        }

        for (int i = startIndex; i < endIndex; i++) {
            Claim claim = claims.get(i);
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfoid", player))
                .color(NamedTextColor.RED)
                .append(Component.text(claim.getClaim()))
                .color(NamedTextColor.GOLD));
        }

        if (infoPage < totalPages) {
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopagecommand1", player) +" "+ (infoPage + 1) + " "+MessageUtils.getMessage("claiminfopagecommand2", player))
                .color(NamedTextColor.GREEN));
        }
    }

    public static String printClaimsDiscord(ArrayList<Claim> claims,int infoPage){
        int itemsPerPage = 5;
        int totalItems = claims.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
    
        int startIndex = (infoPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        String claimList = "";

        if(totalItems < 1){
            return "";
        }

        if (infoPage > totalPages) {
            return "";
        }

        for (int i = startIndex; i < endIndex; i++) {
            Claim claim = claims.get(i);
            if(!claim.isEvent()){
                claimList += MessageUtils.getMessageConsole("claimbody4") + claim.getClaim();
            }
        }
        return claimList;
    }

    public static Claim chooseClaim(String name,int difficulty,UUID id){
        ClaimService claimService = new ClaimService();
        ArrayList<Claim> claimList = claimService.getClaimList();

        
        List<Claim> filteredClaims = claimList.stream()
        .filter(claim -> claim.getDifficulty() == difficulty && "F".equalsIgnoreCase(claim.getStatus()))
        .collect(Collectors.toList());

        if (name != null && !name.isEmpty()) {
            filteredClaims = filteredClaims.stream()
                    .filter(claim -> claim.getName().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filteredClaims.isEmpty()) {
            return null;
        }

        Random random = new Random();
        Claim chosenClaim = filteredClaims.get(random.nextInt(filteredClaims.size()));
        chosenClaim.setPlayer(id.toString());
        claimService.saveClaim();

        return chosenClaim;
        
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
