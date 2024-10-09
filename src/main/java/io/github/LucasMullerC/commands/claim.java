package io.github.LucasMullerC.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimLimitService;
import io.github.LucasMullerC.service.claim.ClaimPromptService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.PendingUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class claim implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
            Player player = (Player) sender;
            if (player.hasPermission("group.b_br") == false && player.hasPermission("group.builder_not") == false) {
                player.sendMessage(Component.text(MessageUtils.getMessage("notabuilder1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("notabuilder2", player)).color(NamedTextColor.GOLD));
                return true;
            }
            UUID id = player.getUniqueId();
            String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(id);
            if (discordId == null) {
                player.sendMessage(Component.text(MessageUtils.getMessage("discordnotlinked1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("discordnotlinked2", player)).color(NamedTextColor.GOLD));
                return true;
            }
            BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
            //Build builder to database
            BuilderService builderService = new BuilderService();
            builderService.buildBuilder(id.toString(), discordId);

            ConversationFactory cf = new ConversationFactory(plugin);

            //ADD CLAIM
            if(arg3[0].equalsIgnoreCase("add")){
                Builder builder = builderService.getBuilderUuid(id.toString());
                ClaimLimitService claimLimitService = new ClaimLimitService(player);
                if(claimLimitService.getLimitTier(builder.getTier(), player) == false || player.hasPermission("group.nolimit")){
                    String selectionPoints = claimLimitService.VerifyClaimLimits(player,builder.getTier());
                    if(selectionPoints.length()==1){
                        switch (selectionPoints) {
                            case "1":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasIntersec", player)).color(NamedTextColor.RED));
                                player.sendMessage(Component.text(MessageUtils.getMessage("LinkMapa", player)).color(NamedTextColor.GOLD));
                                return true;
                            case "2":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                                return true;
                            case "3":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasSelecao", player)).color(NamedTextColor.RED));
                                return true;
                        }
                    } else{
                        Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, selectionPoints).Add).withLocalEcho(true)
                        .buildConversation(player);
                        conv.begin();
                        return true;
                    }
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                    return true;
                }

            //REMOVE CLAIM
            } else if (arg3[0].equalsIgnoreCase("abandonar") || arg3[0].equalsIgnoreCase("remover")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").remove).withLocalEcho(true)
                .buildConversation(player);
                conv.begin();
                return true;

            //FINISH CLAIM
            } else if (arg3[0].equalsIgnoreCase("completo")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").completedStart).withLocalEcho(true)
                .buildConversation(player);
                conv.begin();
                return true;

            //CLAIM TEAM MENU
            } else if (arg3[0].equalsIgnoreCase("equipe")) {
                if(arg3.length <= 1){
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand1", player)).color(NamedTextColor.RED));
                    return true;
                }
                if (arg3[1].equalsIgnoreCase("add")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamAdd).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("remover")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamRemove).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("sair")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamLeave).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else {
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand1", player)).color(NamedTextColor.RED));
                    return true;
                }

            //CLAIM EDIT (DEPRECATED) (When completed players still have permission to build)
            }  else if (arg3[0].equalsIgnoreCase("editar")) {
                player.sendMessage(Component.text(MessageUtils.getMessage("commanddeactivated", player)).color(NamedTextColor.GOLD));
                return true;

            //CLAIM IMG MENU   
            } else if (arg3[0].equalsIgnoreCase("img")) {
                if(arg3.length <= 1){
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand2", player)).color(NamedTextColor.GOLD));
                    return true;
                }
                if (arg3[1].equalsIgnoreCase("add")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").claimImgAdd).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("remover")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").claimImgRemove).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                }

            //CLAIM INFO
            } else if (arg3[0].equalsIgnoreCase("info")) {
                    int pageNum = 1;
                    if(arg3.length > 1){
                        try {
                            pageNum = Integer.valueOf(arg3[1]);
                        } catch (NumberFormatException e) {
                            pageNum = 1;
                        }
                    }
                    Map<String, Object> infos = ClaimUtils.getClaimInfos(player.getUniqueId().toString());
                    int qtdClaim = (int) infos.get("qtdClaim");
                    int qtdCompleted = (int) infos.get("qtdCompleted");
                    @SuppressWarnings("unchecked")
                    ArrayList<Claim> notCompleted = (ArrayList<Claim>) infos.get("notCompleted");
                    @SuppressWarnings("unchecked")
                    ArrayList<Claim> completed = (ArrayList<Claim>) infos.get("completed");

                    PendingService pendingService = new PendingService();
                    ArrayList<Pending> pendingList = pendingService.getPendingPlayer(player.getUniqueId().toString());

                    Component.text("=========").color(NamedTextColor.GOLD);
                    //CLAIMS EM CONSTRUÇÃO - 
                    player.sendMessage(Component.text(MessageUtils.getMessage("claimsunderconstruction", player)).color(NamedTextColor.DARK_BLUE)
                    .append(Component.text(qtdClaim).color(NamedTextColor.GOLD)));
                    ClaimUtils.printClaimsMinecraft(notCompleted,player,pageNum);

                    //CLAIMS EM ANALISE -
                    if(!pendingList.isEmpty()) {
                        player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopending", player)).color(NamedTextColor.YELLOW)
                        .append(Component.text(pendingList.size()).color(NamedTextColor.GOLD)));
                        PendingUtils.printPendingMinecraft(pendingList, player, pageNum);
                    }

                    //CLAIMS COMPLETOS -
                    player.sendMessage(Component.text(MessageUtils.getMessage("claiminfocompleted", player)).color(NamedTextColor.GREEN)
                    .append(Component.text(qtdCompleted).color(NamedTextColor.GOLD)));
                    ClaimUtils.printClaimsMinecraft(completed,player,pageNum);
                    Component.text("=========").color(NamedTextColor.GOLD);
                    return true;
                }
            return false;
    }
    
}
