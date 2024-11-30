package io.github.LucasMullerC.discord.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.ElevationService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.PendingUtils;
import io.github.LucasMullerC.util.RegionUtils;

public class DiscordClaim {
    public MessageEmbed getCommand(User sender,int page,User mention,String claimId) {
        //User Claims
        if(claimId == null){
            String discordId,userName;
            Thumbnail thumb;
            if(mention == null){
                discordId = sender.getId().toString();
                userName = sender.getName();
                thumb = new Thumbnail(sender.getAvatarUrl(), null, 100, 100);
            } else {
                discordId = mention.getId().toString();
                userName = mention.getName();
                thumb = new Thumbnail(mention.getAvatarUrl(), null, 100, 100);
            }
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderDiscord(discordId);
            if(builder == null){
                ImageInfo img = new ImageInfo(MessageUtils.getMessageConsole("PerfilNotBuilderDiscordlink"), null, 105, 30);
                MessageEmbed messageEmbed = new MessageEmbed(null, null,MessageUtils.getMessageConsole("PerfilNotBuilderDiscord"),null, null, 52224, null, null, null, null, null,img, null);
                return messageEmbed;
            } else {
                String senderUuid = builder.getUUID();
                Footer ft = new Footer(MessageUtils.getMessageConsole("claimfooter1"), null, null);
                String title = MessageUtils.getMessageConsole("claimtitle1");
                MessageEmbed messageEmbed = new MessageEmbed(null, title + userName, claimProfileBody(senderUuid, page), null,
                null, 52224, thumb, null, null, null, ft, null,
                null);
                return messageEmbed;
            }

        //Especific Claim
        } else {
            ClaimService claimService = new ClaimService();
            Claim claim = claimService.getClaim(claimId);
            if(claim == null){
                MessageEmbed messageEmbed = new MessageEmbed(null, null,MessageUtils.getMessageConsole("ClaimNotFound"),null, null, 52224, null, null, null, null, null,null, null);
                return messageEmbed;
            } else {
                Footer ft = new Footer(MessageUtils.getMessageConsole("claimfooter1"), null, null);
                String title = MessageUtils.getMessageConsole("claimtitle2");
                Thumbnail thumb = new Thumbnail(getCoordinateURL(claim), null, 100, 100);

                ImageInfo img = null;
                if(!claim.getImage().equals("nulo")){
                    String[] imageUrls = claim.getImage().split(",");
                    String firstImageUrl = imageUrls[0].trim();
                    img = new ImageInfo(firstImageUrl, null, 100, 100);
                }

                MessageEmbed messageEmbed = new MessageEmbed(null, title + claimId, claimInfoBody(claim), null,
                null, 52224, thumb, null, null, null, ft, img,
                null);
                return messageEmbed;
            }
        }
    }
    private static String getCoordinateURL(Claim claim) {
        int zoom = 14;
        String[] ary = claim.getPoints().split(",");
        double[] coords = RegionUtils.toGeo(Integer.parseInt(ary[0].split("\\.")[0]),
        Integer.parseInt(ary[1].split("\\.")[0]));

        int tileX = (int) Math.floor((coords[0] + 180) / 360 * Math.pow(2, zoom));
        double rad = Math.toRadians(coords[1]);
        int tileY = (int) Math.floor((1 - Math.log(Math.tan(rad) + 1 / Math.cos(rad)) / Math.PI) / 2 * Math.pow(2, zoom));
        String tileUrl = String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, tileX, tileY);
        return tileUrl;
    }

    private static String getBlueMap(Claim claim){
        String[] ary = claim.getPoints().split(",");
        double[] coords = RegionUtils.toGeo(Integer.parseInt(ary[0].split("\\.")[0]),
        Integer.parseInt(ary[1].split("\\.")[0]));
        ElevationService elevationService = new ElevationService();
        String elevation = elevationService.getElevationOpenElevation(coords[1], coords[0]);
        String bluemapurl = String.format("http://btebrasil.wither.host:8578/#world:%s:%s:%s:0:-1.07:1.36:0:0:free",ary[0],elevation,ary[1]);
        return bluemapurl;
    }

    private String claimInfoBody(Claim claim) {
        StringBuilder formattedParticipantList = new StringBuilder();
        
        addFormattedParticipant(claim.getPlayer(), formattedParticipantList);
        
        String participants = claim.getParticipants();
        if (!participants.equals("nulo")) {
            String[] participantList = participants.split(",");
            for (String participant : participantList) {
                addFormattedParticipant(participant, formattedParticipantList);
            }
        }
        String status = "";
        if(claim.getStatus().equals("T")){
            status = MessageUtils.getMessageConsole("claiminfobody3");
        } else{
            status = MessageUtils.getMessageConsole("claiminfobody2");
        }
        
        return MessageUtils.getMessageConsole("claiminfobody4")+claim.getName()+status+MessageUtils.getMessageConsole("claiminfobody5")+claim.getBuilds()+
        MessageUtils.getMessageConsole("claiminfobody6")+"["+MessageUtils.getMessageConsole("claiminfobody7")+"]("+getBlueMap(claim)+")"+
        MessageUtils.getMessageConsole("claiminfobody1") +"\n"+ formattedParticipantList.toString();
    }

    private void addFormattedParticipant(String playerUUID, StringBuilder participantList) {
        try {
            String discordId = DiscordActions.getDiscordId(UUID.fromString(playerUUID));
            if (discordId != null) {
                participantList.append("• <@").append(discordId).append("> \r\n\r\n");
            } else {
                String playerName = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName();
                if (playerName != null) {
                    participantList.append("• ").append(playerName).append(" \r\n\r\n ");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("UUID inválido (/claim claimid): " + playerUUID);
        }
    }

    private String claimProfileBody(String senderUuid,int page){
        ClaimService claimService = new ClaimService();
        PendingService pendingService = new PendingService();
        String claimNum = String.valueOf(claimService.getClaimQtdByPlayer(senderUuid));
        ArrayList<Claim> playerClaim = claimService.getClaimListByPlayer(senderUuid);
        String completedClaimNum = String.valueOf(claimService.getCompletedClaimQtdByPlayer(senderUuid));
        ArrayList<Claim> playerCompletedClaim = claimService.getCompletedClaimListByPlayer(senderUuid);
        ArrayList<Pending> playerPending = pendingService.getPendingPlayerListClaim(senderUuid);

        String pendentesBody = "";
        if(!playerPending.isEmpty()){
            pendentesBody = MessageUtils.getMessageConsole("claimbody1") + playerPending.size()+PendingUtils.printPendingDiscord(playerPending, page);
        }

        return MessageUtils.getMessageConsole("claimbody2")+claimNum+ClaimUtils.printClaimsDiscord(playerClaim, page)+ pendentesBody+
        MessageUtils.getMessageConsole("claimbody3")+completedClaimNum+ClaimUtils.printClaimsDiscord(playerCompletedClaim, page);
    }
}
