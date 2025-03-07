package io.github.LucasMullerC.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class completed implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        BuilderService builderService = new BuilderService();
        Builder builder = builderService.getBuilderUuid(id.toString());
        if(builder != null){
            ClaimService claimService = new ClaimService();
            ArrayList<Claim> claimList = claimService.getClaimListByPlayer(id.toString());
            PendingService pendingService = new PendingService();
            for(Claim claim:claimList){
                if(claim.getDifficulty()>0){
                    Pending pending = pendingService.getPendingClaim(claim.getClaim());
                    if (pending == null){
                        ClaimUtils.finalizeClaim(player, claim, "1");
                        player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCompleto", player)).color(NamedTextColor.GREEN));
                        return true;
                    }
                }
            }
        } 

        ApplicantService applicantService = new ApplicantService();
        PendingService pendingService = new PendingService();
        Applicant applicant = applicantService.getApplicant(id.toString());
        if(pendingService.getPendingApplication(id.toString()) == null){
            if(applicant.getSection().equals("d")){
                //add pending
                Pending pending = new Pending(id.toString());
                pending.setregionId("nulo");
                pending.setisApplication(true);
                pending.setbuilds("0");
                pendingService.addPending(pending);

                //send discord msg to reviewers
                //DiscordActions.sendLogMessage("<@&TESTEBETA6.0> " + MessageUtils.getMessageConsole("PendenteMsg"));
                DiscordActions.sendLogMessage("<@&826599049297264640> " + MessageUtils.getMessageConsole("PendenteMsg"));
                DiscordActions.sendLogMessage("Usuário: " + DiscordActions.getDiscordName(applicant.getDiscord()));

                WorldGuardService worldGuardService = new WorldGuardService();
                worldGuardService.removePermissionWG("apply"+applicant.getgetZone()+"d", player, id);

                player.sendMessage(Component.text(MessageUtils.getMessage("AppEnviada", player)).color(NamedTextColor.GOLD));
                return true;
            } else{
                player.sendMessage(Component.text(MessageUtils.getMessage("NotCompleto", player)).color(NamedTextColor.YELLOW));
                return true;
            }

        } else{
            player.sendMessage(Component.text(MessageUtils.getMessage("AnalisePend", player)).color(NamedTextColor.RED));
            return true;
        }   
    }
    
}
