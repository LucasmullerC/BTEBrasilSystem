package io.github.LucasMullerC.discord.commands;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import io.github.LucasMullerC.model.Awards;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.AwardService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;

public class DiscordProfile {
    public MessageEmbed getCommand(User sender,User mention) {
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
        if(discordId == null){
            String notLinked = MessageUtils.getMessageConsole("discordnotlinked1")+" "+MessageUtils.getMessageConsole("discordnotlinked3");
            MessageEmbed messageEmbed = new MessageEmbed(null, null,notLinked,null, null, 52224, null, null, null, null, null,null, null);
            return messageEmbed;
        } else {
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderDiscord(discordId);
            if(builder == null){
                ImageInfo img = new ImageInfo(MessageUtils.getMessageConsole("PerfilNotBuilderDiscordlink"), null, 105, 30);
                MessageEmbed messageEmbed = new MessageEmbed(null, null,MessageUtils.getMessageConsole("PerfilNotBuilderDiscord"),null, null, 52224, null, null, null, null, null,img, null);
                return messageEmbed;
            } else {
                String senderUuid = builder.getUUID();
                ClaimService claimService = new ClaimService();
                AwardService awardService = new AwardService();
                String claimNum = String.valueOf(claimService.getClaimQtdByPlayer(senderUuid));
                String completedClaimNum = String.valueOf(claimService.getCompletedClaimQtdByPlayer(senderUuid));
                String nextLevel = BuilderUtils.toNextLevel(builder);
                String[] builderAwards = builder.getAwards().split(",");
                int totalAwards = awardService.totalAwards();
                String points = String.valueOf(builder.getPoints());
                points = points.substring(0, points.indexOf(".") + 2);

                Footer ft = new Footer(MessageUtils.getMessageConsole("profilefooter1"), null, null);
                ImageInfo img = null;
                Boolean description = false;
                if(!builder.getFeatured().equals("nulo")){
                    Awards award = awardService.getAward(builder.getFeatured());
                    img = new ImageInfo(award.getURL(), null, 105, 30);
                    description = true;
                }

                String title = MessageUtils.getMessageConsole("profile1")+" "+userName+" "+MessageUtils.getMessageConsole("profiletitle1");
                String body = getEmbedBody(String.valueOf(builder.getTier()), String.valueOf(builder.getBuilds()), claimNum, completedClaimNum,
                 points, description, nextLevel,totalAwards,builderAwards.length);


                //Footer
                //MessageEmbed messageEmbed = new MessageEmbed(null, title,body,null, null, 52224, thumb, null, null, null, ft,img, null);
                //No footer
                MessageEmbed messageEmbed = new MessageEmbed(null, title,body,null, null, 52224, thumb, null, null, null, null,img, null);
                return messageEmbed;
            }
        }
    }

    private String getEmbedBody(String rank, String builds, String claimNum,String completedClaimNum,String points, Boolean featured, String nextLevel,
    int totalAwards, int builderAwards){
        if(featured == true){
            return MessageUtils.getMessageConsole("profilebodytier")+rank+"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodybuilds")+builds
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodypoints")+points+" / "+nextLevel
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodyclaimsunderconstruction")+claimNum
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodycompletedclaims")+ completedClaimNum
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodyawards")+builderAwards+" / "+totalAwards
            +"\r\n **--------------------------\r\n\r\n "+MessageUtils.getMessageConsole("profilebodyfeatured");
        } else{
            return MessageUtils.getMessageConsole("profilebodytier")+rank+"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodybuilds")+builds
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodypoints")+points+" / "+nextLevel
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodyclaimsunderconstruction")+claimNum
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodycompletedclaims")+ completedClaimNum
            +"\r\n\r\n "+MessageUtils.getMessageConsole("profilebodyawards")+builderAwards+" / "+totalAwards
            +"\r\n **--------------------------\r\n\r\n "+MessageUtils.getMessageConsole("profilebodynotfeatured");
        }
    }
}
