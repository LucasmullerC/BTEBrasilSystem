package io.github.LucasMullerC.service.pending;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.LocationUtil;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.RegionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PendingPromptService {
    int participantsNum = 0, size;
    Claim claim;
    Pending pending;
    OfflinePlayer participant, player;
    Player reviewer;
    double points = 0,total = 0;
    String[] builds,participants;
    World world;
    ClaimService claimService;

    public PendingPromptService(Claim claim, Pending pending, OfflinePlayer player,Player reviewer,World world, ClaimService claimService) {
        this.claim = claim;
        this.player = player;
        this.reviewer = reviewer;
        this.builds = pending.getbuilds().split(",");
        this.size = builds.length;
        this.participants = claim.getParticipants().split(",");
        this.world = world;
        this.claimService = claimService;
    }

    public Prompt Builds = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            if(participantsNum == 0){
                return ChatColor.BOLD + player.getName() + " "+
                MessageUtils.getMessageConsole("thisbuilderbuilt")+ " "+
                builds[participantsNum] +" "+  MessageUtils.getMessage("buildsinsidethisclaim",reviewer)+ 
                ChatColor.RED +MessageUtils.getMessageConsole("thisinfoiscorrect");
            } else{
                participant = Bukkit.getOfflinePlayer(UUID.fromString(participants[participantsNum - 1]));

                return ChatColor.BOLD + participant.getName() + " "+
                MessageUtils.getMessageConsole("thisbuilderbuilt")+ " "+
                builds[participantsNum] +" "+  MessageUtils.getMessage("buildsinsidethisclaim",reviewer)+ 
                ChatColor.RED +MessageUtils.getMessage("thisinfoiscorrect",reviewer);
            }
        }
        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if(ClaimUtils.isNumeric(input) == true){
                Integer num = Integer.parseInt(input);
                if (num >= 0) {
                    builds[participantsNum] = input;
                    participantsNum++;
                    if(participantsNum < size){
                        return Builds;
                    } else{
                        participantsNum = 0;
                        return specialBuilds;
                    }
                } else{
                    reviewer.sendMessage(Component.text(MessageUtils.getMessage("MaiorZero",reviewer)).color(NamedTextColor.RED));
                    return Builds;
                }

            } else{
                reviewer.sendMessage(Component.text(MessageUtils.getMessage("MustBeNumber",reviewer)).color(NamedTextColor.RED));
                return Builds;
            }
        }
    };

    Prompt specialBuilds = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("specialbuilds",reviewer);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if(ClaimUtils.isNumeric(input) == true){
                Integer num = Integer.parseInt(input);
                if (num > 0) {
                    points = (num * 5) + points;
                    return weights;
                } else{
                    return weights;
                }

            }  else{
                reviewer.sendMessage(Component.text(MessageUtils.getMessage("MustBeNumber",reviewer)).color(NamedTextColor.RED));
                return specialBuilds;
            }
        }
    };

    Prompt weights = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("weight1",reviewer) + ChatColor.RED
                    +" "+ MessageUtils.getMessage("weight2",reviewer);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if(ClaimUtils.isNumeric(input) == true){
                Integer num = Integer.parseInt(input);
                if (num >= 2) {
                    double distance = RegionUtils.getDistance(claim.getClaim(), world);
                    if (num <= distance) {
                        total = distance / num;
                        return confirmPoints;
                    } else {
                        total = -1;
                        return confirmPoints;
                    }
                } else {
                    reviewer.sendMessage(Component.text(MessageUtils.getMessage("MaiorDois",reviewer)).color(NamedTextColor.RED));
                    return weights;
                }
            } else{
                reviewer.sendMessage(Component.text(MessageUtils.getMessage("MustBeNumber",reviewer)).color(NamedTextColor.RED));
                return weights;
            }
        }
    };

    Prompt confirmPoints = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("confirmationmessage1",reviewer)+" " + ChatColor.BLUE + String.valueOf(total)
                    + MessageUtils.getMessage("confirmationmessage2",reviewer)+" " + ChatColor.RED + " "+MessageUtils.getMessage("confirmationmessage3",reviewer);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (input.equalsIgnoreCase("S")) {
                points = total + points;
                int totalBuilds = 0;
                BuilderService builderService = new BuilderService();
                for (int i = 0; i < size; i++) {
                    String discordId = "";
                    Builder builder;
                    double individualTotal = Integer.parseInt(builds[i]) + points;
                    if(i == 0){
                        builder = builderService.getBuilderUuid(player.getUniqueId().toString());
                        discordId = builder.getDiscord();
                    } else {
                        builder = builderService.getBuilderUuid(participants[i - 1]);
                        discordId = builder.getDiscord();
                    }
                    BuilderUtils.addBuilds(builder, builderService, Integer.parseInt(builds[i]));
                    BuilderUtils.addPoints(builder, builderService, individualTotal);
                    BuilderUtils.checkRank(builder, builderService);
                    BuilderUtils.CheckAwardsBuilds(builder, builderService);

                    totalBuilds = Integer.parseInt(builds[i]) + totalBuilds;
                    if (!discordId.equals("nulo")) {
                        DiscordActions.sendPrivateMessage(discordId, MessageUtils.getMessageConsole("ClaimAprovadoDiscord1") + "**" + claim.getClaim() + "**" + " "+
                        MessageUtils.getMessageConsole("ClaimAprovadoDiscord2") + MessageUtils.getMessageConsole("Seta") + builds[i] + " "+
                        MessageUtils.getMessageConsole("ClaimAprovadoDiscord3") + MessageUtils.getMessageConsole("Seta") + String.valueOf(individualTotal) + " "+
                        MessageUtils.getMessageConsole("ClaimAprovadoDiscord4"));
                    }
                }
                //PendingService pendingService = new PendingService();
                ClaimUtils.addBuilds(claim, claimService, totalBuilds);
                ClaimUtils.CompleteClaim(claim, claimService);

                if(claim.getDifficulty() > 0){
                    int[] dimensions = RegionUtils.getRegionDimensions(claim.getClaim(), reviewer);
                    if (dimensions == null) {
                        reviewer.sendMessage(Component.text(MessageUtils.getMessage("claimerror1", reviewer)).color(NamedTextColor.RED));
                        return END_OF_CONVERSATION;
                    }
                    int maxDimension = Math.max(dimensions[0], dimensions[1]);
                    boolean completed = RegionUtils.updateClaim(claim.getClaim(),reviewer,"copy"+claim.getClaim(),maxDimension);
                    if(!completed){
                        reviewer.sendMessage(Component.text(MessageUtils.getMessage("claimerror1", reviewer)).color(NamedTextColor.RED));
                    }
                    WorldGuardService worldGuardService = new WorldGuardService();
                    worldGuardService.RemoveRegion("copy" + claim.getClaim(), reviewer);
                    worldGuardService.addPermissionWG(claim.getClaim(), reviewer, UUID.fromString(claim.getPlayer()));
                    //No support to teams yet. Sem suporte para equipes no momento.

                    reviewer.teleport(LocationUtil.getLocationFromPoints(claim.getPoints(), reviewer.getWorld()));
                }
                //pendingService.removePending(pending);
                reviewer.sendMessage(Component.text(MessageUtils.getMessage("ClaimAprovado",reviewer)).color(NamedTextColor.GREEN));
                return END_OF_CONVERSATION;
            }else if (input.equalsIgnoreCase("N")) {
                return weights;
            } else {
                reviewer.sendMessage(Component.text(MessageUtils.getMessage("Certeza",reviewer)).color(NamedTextColor.RED));
                return confirmPoints;
            }
        }
    };
}
