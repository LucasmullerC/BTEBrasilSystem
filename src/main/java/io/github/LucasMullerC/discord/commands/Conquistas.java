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
import io.github.LucasMullerC.util.MessageUtils;

public class Conquistas {
    public MessageEmbed getCommand(User sender,int page,User mention) {
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
                ImageInfo img = new ImageInfo(MessageUtils.getMessagePT("PerfilNotBuilderDiscordlink"), null, 105, 30);
                MessageEmbed messageEmbed = new MessageEmbed(null, null,MessageUtils.getMessagePT("PerfilNotBuilderDiscord"),null, null, 52224, null, null, null, null, null,img, null);
                return messageEmbed;
            } else{
                AwardService awardService = new AwardService();
                ImageInfo img = null;
                if(!builder.getFeatured().equals("nulo")){
                    Awards award = awardService.getAward(builder.getFeatured());
                    img = new ImageInfo(award.getURL(), null, 105, 30);
                }
                Footer ft = new Footer(MessageUtils.getMessagePT("awardsfooter1"), null, null);
                String title = MessageUtils.getMessagePT("awardstitle1");
                MessageEmbed messageEmbed = new MessageEmbed(null, title + userName, listAwards(builder, page), null,
                    null, 52224, thumb, null, null, null, ft, img,
                    null);
                return messageEmbed;
            }
    }

    private String listAwards(Builder builder,int pageNumber){
        if(builder.getAwards().equals("nulo")) {
            return MessageUtils.getMessagePT("conquistaList");
        }

        String[]awards=builder.getAwards().split(",");
        int itemsPerPage = 10;
        String result = "";

        int startIndex = (pageNumber - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, awards.length);

        if (startIndex >= awards.length || pageNumber < 1) {
            return MessageUtils.getMessagePT("invalidpage") + 
                   ((awards.length + itemsPerPage - 1) / itemsPerPage) + ".";
        }
        AwardService awardService = new AwardService();
        for (int i = startIndex; i < endIndex; i++) {
            Awards award = awardService.getAward(awards[i]);
            result += "● [" + award.getName() + "](" + award.getURL() + ")\r\n";
        }
        result += "\n**"+MessageUtils.getMessagePT("pageawardsbody")+" " + pageNumber + " "+MessageUtils.getMessagePT("frompageawardsbody")+" " + 
        ((awards.length + itemsPerPage - 1) / itemsPerPage) + "**";

        return result;
    }
    
}
