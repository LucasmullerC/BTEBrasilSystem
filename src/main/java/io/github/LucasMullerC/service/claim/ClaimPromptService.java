package io.github.LucasMullerC.service.claim;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ClaimPromptService {
    Player player;
    OfflinePlayer participant;
    String selectionPoints,builds,awardId = "nulo";
    boolean event = false;
    int size, participantCont = 0;
    String[] participants;
    Claim claim;
    ClaimService claimService;

    public ClaimPromptService(Player player, String selectionPoints) {
        this.player = player;
        this.selectionPoints = selectionPoints;
    }

    public void setEvent(boolean event,String awardId){
        this.awardId = awardId;
        this.event = event;
    }

    // CLAIM ADD
    public Prompt Add = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("placename", player);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
            String claimId =ClaimUtils.buildClaim(player, input, selectionPoints,event,awardId);
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

    // CLAIM TEAM ADD
    public Prompt teamAdd = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoaddparticipant", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim,player,false) == true) {
                return teamAdd2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM TEAM ADD 2
    public Prompt teamAdd2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("nicknameofparticipant", player)+ ChatColor.RED
            +" "+MessageUtils.getMessage("mustbeonline", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            Player invite = Bukkit.getPlayer(input);
            if(invite != null){
                String id = player.getUniqueId().toString();
                String idInvite = invite.getUniqueId().toString();
                if(!idInvite.equals(id)){
                    if (invite.hasPermission("group.b_br") == false && invite.hasPermission("group.builder_not") == false) {
                        player.sendMessage(Component.text(MessageUtils.getMessage("EquipeNotBuilder", player)).color(NamedTextColor.RED));
                        return END_OF_CONVERSATION;
                    }
                    else{
                        String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(invite.getUniqueId());
                        if (discordId == null) {
                            player.sendMessage(Component.text(MessageUtils.getMessage("participantnotlinked", player)).color(NamedTextColor.RED));
                            return END_OF_CONVERSATION; 
                        }
                        //Build builder to database
                        BuilderService builderService = new BuilderService();
                        builderService.buildBuilder(id.toString(), discordId);
                        
                        ClaimUtils.addParticipant(claim, invite,claimService);
                        return END_OF_CONVERSATION; 
                    }
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("MuyMaloEasterEgg", player)).color(NamedTextColor.RED));
                    return END_OF_CONVERSATION; 
                }
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("PlayerOn", player)).color(NamedTextColor.RED));
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM TEAM REMOVE
    public Prompt teamRemove = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoremoveparticipant", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim,player,false) == true) {
                return teamRemove2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM TEAM REMOVE 2
    public Prompt teamRemove2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("nicknameofparticipantremove", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            OfflinePlayer participant = Bukkit.getOfflinePlayer(input);
            if(participant != null){
                String participantId = participant.getUniqueId().toString();
                String playerId = player.getUniqueId().toString();
                if(!participantId.equals(playerId)){
                    ClaimUtils.removeParticipant(claim, player, participant.getPlayer(),claimService);
                    return END_OF_CONVERSATION; 
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("MuyMaloEasterEgg", player)).color(NamedTextColor.RED));
                    return END_OF_CONVERSATION; 
                }
            }
            else{
                player.sendMessage(Component.text(MessageUtils.getMessage("PlayerNotFound", player)).color(NamedTextColor.RED));
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM TEAM LEAVE
    public Prompt teamLeave = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoleave", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim,player,false) == true) {
                ClaimUtils.removeParticipant(claim, player, player,claimService);
                return END_OF_CONVERSATION; 
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM IMG ADD
    public Prompt claimImgAdd = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoaddimage", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim,player,true) == true) {
                return claimImgAdd2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM IMG ADD 2
    public Prompt claimImgAdd2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("imagetutorial1", player) + ChatColor.BLUE +MessageUtils.getMessage("imgururl", player)+
            ChatColor.RED+" "+MessageUtils.getMessage("imagetutorial2", player)+ ChatColor.WHITE + ")";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            ClaimUtils.addImage(claim, input, player,claimService);
            return END_OF_CONVERSATION;
        }
    };

    // CLAIM IMG REMOVE
    public Prompt claimImgRemove = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoremoveimage", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (ClaimUtils.verifyClaimProperties(claim,player,true) == true) {
                return claimImgRemove2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM IMG REMOVE 2
    public Prompt claimImgRemove2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtoremoveimage2", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            ClaimUtils.removeImage(claim, input, player,claimService);
            return END_OF_CONVERSATION;
        }
    };

    // CLAIM EDIT
    public Prompt claimEdit = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("idtorename", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claimService = new ClaimService();
            claim = claimService.getClaim(input);
            if (player.hasPermission("btebrasil.adm")) {
                return claimEdit2;
            }
            else if (ClaimUtils.verifyClaimProperties(claim,player,true) == true) {
                return claimEdit2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM EDIT 2
    public Prompt claimEdit2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + MessageUtils.getMessage("claimnewname", player);
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claim.setName(input);
            claimService.saveClaim();
            player.sendMessage(Component.text(MessageUtils.getMessage("NomeClaim", player)).color(NamedTextColor.GREEN));
            return END_OF_CONVERSATION;
        }
    };
}
