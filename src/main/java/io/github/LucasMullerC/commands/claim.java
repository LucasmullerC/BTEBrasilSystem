package io.github.LucasMullerC.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.PromptService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimLimitService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
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
                        cf.withFirstPrompt(new PromptService(
                            MessageUtils.getMessage("placename", player),
                            (context, input) -> {
                                String claimId =ClaimUtils.buildClaim(player, input, selectionPoints);
                                player.sendMessage(Component.text(MessageUtils.getMessage("AddAreas1", player)).color(NamedTextColor.GOLD)
                                .append(Component.text(claimId)).color(NamedTextColor.GREEN));
                                player.sendMessage(Component.text(MessageUtils.getMessage("AddAreas2", player)).color(NamedTextColor.GOLD));
                            })).withLocalEcho(true).buildConversation(player).begin();
                            }
                            return true;
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                    return true;
                }

            //REMOVE CLAIM
            } else if (arg3[0].equalsIgnoreCase("abandonar") || arg3[0].equalsIgnoreCase("remover")) {
                cf.withFirstPrompt(new PromptService(
                    MessageUtils.getMessage("idtoremoveclaim", player),
                    (context, input) -> {
                        ClaimService claimService = new ClaimService();
                        Claim claim = claimService.getClaim(input);
                        if(claim != null){
                            if (ClaimUtils.verifyClaimProperties(claim, player, false) == true || player.hasPermission("btebrasil.adm")) {
                                claimService.removeClaim(claim, player);
                                player.sendMessage(Component.text(MessageUtils.getMessage("ClaimRemoved", player)).color(NamedTextColor.GREEN));
                            }
                        }else{
                            player.sendMessage(Component.text(MessageUtils.getMessage("ClaimNotFound", player)).color(NamedTextColor.RED));
                        }
                })).withLocalEcho(true).buildConversation(player).begin();
                return true;
            }
            return false;
    }
    
}
