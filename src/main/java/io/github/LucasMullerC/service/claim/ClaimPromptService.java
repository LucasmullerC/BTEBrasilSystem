package io.github.LucasMullerC.service.claim;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ClaimPromptService {
    Player player;
    OfflinePlayer participant;
    String selectionPoints,builds;
    int size, participantCont = 0;
    String[] participants;
    Claim claim;

    public ClaimPromptService(Player player, String selectionPoints) {
        this.player = player;
        this.selectionPoints = selectionPoints;
    }

    // CLAIM ADD
    public Prompt Add = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("placename", player);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
            String claimId =ClaimUtils.buildClaim(player, input, selectionPoints);
            player.sendMessage(Component.text(MessageUtils.getMessage("AddAreas1", player)).color(NamedTextColor.GOLD)
            .append(Component.text(claimId)).color(NamedTextColor.GREEN));
            player.sendMessage(Component.text(MessageUtils.getMessage("AddAreas2", player)).color(NamedTextColor.GOLD));
            return END_OF_CONVERSATION;
        }
    };

    // CLAIM REMOVE
    public Prompt remove = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoremoveclaim", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            ClaimService claimService = new ClaimService();
            Claim claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim, player, false) == true || player.hasPermission("btebrasil.adm")) {
                claimService.removeClaim(claim, player);
                player.sendMessage(Component.text(MessageUtils.getMessage("ClaimRemoved", player)).color(NamedTextColor.GREEN));
            }
            return END_OF_CONVERSATION;
        }
    };
    
    // CLAIM COMPLETED
    public Prompt completedStart = new StringPrompt() {
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.BOLD + MessageUtils.getMessage("idtocompleteclaim", player);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        ClaimService claimService = new ClaimService();
        claim = claimService.getClaim(input);
        if (ClaimUtils.verifyClaimProperties(claim, player, false) == true) {
            PendingService pendingService = new PendingService();
            Pending pending = pendingService.getPendingClaim(claim.getClaim());
            if(pending != null){
                player.sendMessage(Component.text(MessageUtils.getMessage("AnalisePend", player)).color(NamedTextColor.GOLD));
                return END_OF_CONVERSATION;
            }
            else{
                return completedbuilds;
            }
        }
        return END_OF_CONVERSATION;
    }
    };

    // CLAIM COMPLETED 2
    public Prompt completedbuilds = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("howmanybuilds", player);
        }
    
        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (ClaimUtils.isNumeric(input) == true) {
                builds = input;
                if(claim.getParticipants().equals("nulo")){
                    ClaimUtils.finalizeClaim(player, claim, builds);
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCompleto", player)).color(NamedTextColor.GREEN));
                    return END_OF_CONVERSATION;
                } else{
                    participants = claim.getParticipants().split(",");
                    participant = Bukkit.getOfflinePlayer(UUID.fromString(participants[participantCont]));
                    return completedParticipants;
                }
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("MustBeNumber", player)).color(NamedTextColor.RED));
                return completedbuilds;
                
            }
        }
        };

    // CLAIM COMPLETED 3 - Participants
    public Prompt completedParticipants = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("howmanybuildsparticipants", player)+" "+
            participant.getName()+" "+MessageUtils.getMessage("howmanybuildsparticipants2", player)+ ChatColor.RED+" "+
            MessageUtils.getMessage("warningbuilds", player);
        }
    
        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            int size = participants.length;
            if (ClaimUtils.isNumeric(input) == true) {
                Integer buildNumber = Integer.parseInt(input);
                if(buildNumber >= 0){
                    builds += "," + input;
                    participantCont++;
                    if(participantCont < size){
                        participant = Bukkit.getOfflinePlayer(UUID.fromString(participants[participantCont]));
                        return completedParticipants;
                    } else{
                        ClaimUtils.finalizeClaim(player, claim, builds);
                        player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCompleto", player)).color(NamedTextColor.GREEN));
                        return END_OF_CONVERSATION;
                    }
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("MaiorZero", player)).color(NamedTextColor.RED));
                    return completedParticipants;
                }
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("MustBeNumber", player)).color(NamedTextColor.RED));
                return completedParticipants;
                
            }
        }
        };
}
