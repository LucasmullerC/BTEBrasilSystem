package io.github.LucasMullerC.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.MessageService;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.service.pending.PendingPromptService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.LocationUtil;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.RegionUtils;
import io.github.LucasMullerC.util.ZoneUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class analyse implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
                if(arg3.length == 0){
                    return false;
                }
                Player player = (Player) sender;
                if (arg3[0].equalsIgnoreCase("claim") && player.hasPermission("group.reviewer")) {
                    analyseClaim(arg3, player);
                    return true;
                } else if(arg3[0].equalsIgnoreCase("app") && player.hasPermission("group.reviewer")){
                    analyseApplication(arg3, player);
                    return true;
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("Analisar404", player)).color(NamedTextColor.GOLD));
                    player.sendMessage(Component.text(MessageUtils.getMessage("Analisar4041", player)).color(NamedTextColor.GOLD));
                    return false;
                }
    }

    private void analyseClaim(String[] command,Player player) {
        PendingService pendingService = new PendingService();
        Pending pending = null;
        if(player.hasPermission("btebrasil.bypass"))
            //FOR TESTS ONLY
            pending = pendingService.getNextPendingClaim("bypass");
         else {
            pending = pendingService.getNextPendingClaim(player.getUniqueId().toString());
        }

        if(pending != null){
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderUuid(pending.getUUID());

            ClaimService claimService = new ClaimService();
            Claim claim = claimService.getClaim(pending.getregionId());
            String regionId = "";
            Location location;
            if(claim.getDifficulty() > 0){
                regionId = "copy"+pending.getregionId();
                location = RegionUtils.getRegionCopyLocation(regionId, player);

                String[] ary = claim.getPoints().split(",");
                int[] centralCoordinate = LocationUtil.getCentralPoint(ary);
                double[] coords = RegionUtils.toGeo(centralCoordinate[0],centralCoordinate[1]);
                String coordinates = coords[1]+","+coords[0];
                final String input = MessageUtils.getMessage("joinclaiminfo1", player)+" <a:https://www.google.com.br/maps/place/"+coordinates+">"+coordinates+"</a>";
                
                MessageService messageService = new MessageService();
                player.sendMessage(messageService.getMessageWithURL(input).color(NamedTextColor.GREEN));
            } else{
                regionId = pending.getregionId();
                location = LocationUtil.getLocationFromPoints(claim.getPoints(), player.getWorld());
            }
            if(location==null){
                player.sendMessage(Component.text(MessageUtils.getMessage("locationerror1", player)).color(NamedTextColor.RED));
                return;
            }
            if(command.length <= 1){
                String name = DiscordActions.getDiscordName(builder.getDiscord());
                if(name == null){
                    pendingService.removePending(pending);
                    player.sendMessage(Component.text(MessageUtils.getMessage("playerleave", player)).color(NamedTextColor.RED));
                    return;
                }
                player.chat("/region select "+regionId);
                player.sendMessage(Component.text(MessageUtils.getMessage("AnaliseClaim1", player)+
                pending.getregionId()+MessageUtils.getMessage("VoceAnalisa2", player)+name).color(NamedTextColor.GOLD));

                player.teleport(location);
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar2", player)).color(NamedTextColor.GOLD));
                return;
            }
            if(command[1].equalsIgnoreCase("confirmar")){
                World world = player.getWorld();
                OfflinePlayer claimOwner = Bukkit.getOfflinePlayer(UUID.fromString(claim.getPlayer()));
                BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
                ConversationFactory cf = new ConversationFactory(plugin);
                PendingPromptService pendingPromptService = new PendingPromptService(claim, pending, claimOwner, player, world,claimService);
                Conversation conv = cf.withFirstPrompt(pendingPromptService.Builds).withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();

                pendingService.removePending(pending);
            } else if(command[1].equalsIgnoreCase("recusar")){
                String reason = "";
                for (int i = 2; i < command.length; i++) {
                    reason += command[i] + " ";
                }
                reason = reason.trim();

                DiscordActions.sendPrivateMessage(builder.getDiscord(),MessageUtils.getMessageConsole("ClaimRecusada1")+ 
                claim.getClaim() + 
                MessageUtils.getMessageConsole("ClaimRecusada2") +
                reason +
                MessageUtils.getMessageConsole("ClaimRecusada3")
                );

                if(!claim.getParticipants().equals("nulo")){
                    String[] participants = claim.getParticipants().split(",");
                    for (int i = 0; i < participants.length; i++) {
                        String participantDiscord = builderService.getBuilderUuid(participants[i]).getDiscord();
                        if(!participantDiscord.equals("nulo")){
                            DiscordActions.sendPrivateMessage(participantDiscord,MessageUtils.getMessageConsole("ClaimRecusada1")+ 
                            claim.getClaim() + 
                            MessageUtils.getMessageConsole("ClaimRecusada2") +
                            reason +
                            MessageUtils.getMessageConsole("ClaimRecusada3")
                            );
                        }
                    }
                }
                pendingService.removePending(pending);
                player.sendMessage(Component.text(MessageUtils.getMessage("ClaimRecusada4", player)).color(NamedTextColor.GOLD));
            } else{
                String name = DiscordActions.getDiscordName(builder.getDiscord());
                if(name == null){
                    pendingService.removePending(pending);
                    player.sendMessage(Component.text(MessageUtils.getMessage("playerleave", player)).color(NamedTextColor.RED));
                    return;
                }

                player.chat("/region select "+regionId);
                player.sendMessage(Component.text(MessageUtils.getMessage("AnaliseClaim1", player)+
                pending.getregionId()+
                MessageUtils.getMessage("VoceAnalisa2", player)+name).color(NamedTextColor.GOLD));

                player.teleport(location);
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar2", player)).color(NamedTextColor.GOLD));
                return;
            }
        } else{
            player.sendMessage(Component.text(MessageUtils.getMessage("NotClaim", player)).color(NamedTextColor.GOLD));
        }
    }

    public void analyseApplication(String[] command, Player player){
        PendingService pendingService = new PendingService();
        Pending pending = null;

        pending = pendingService.getNextPendingApplication();
        if(pending != null){
            ApplicantService applicantService = new ApplicantService();
            Applicant applicant = applicantService.getApplicant(pending.getUUID());
            ApplicationZoneService applicationZoneService = new ApplicationZoneService();
            ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());
            OfflinePlayer applicationPlayer = Bukkit.getOfflinePlayer(UUID.fromString(applicant.getUUID()));
            if(command.length <= 1){
                Location location = applicationZone.getLocationD();
                player.sendMessage(Component.text(MessageUtils.getMessage("VoceAnalisa", player) +" "+ applicationPlayer.getName() +
                MessageUtils.getMessage("VoceAnalisa2", player) + DiscordActions.getDiscordName(applicant.getDiscord())));

                player.teleport(location);
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("Analisar2", player)).color(NamedTextColor.GOLD));
                return;
            }
            if(command[1].equalsIgnoreCase("confirmar")){

                //remove regions
                WorldGuardService worldGuardService = new WorldGuardService();
                worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "d", player);
                worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "c", player);
                worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "b", player);
                worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "a", player);

                //remove from lists
                applicationZoneService.removeApplicationZone(applicationZone);
                applicantService.removeApplicant(applicant);
                pendingService.removePending(pending);

                //add builder role on discord
                DiscordActions.addRole(applicant.getDiscord(),"721861941517353021");

                //teleport player to spawn
                World world = player.getWorld();
                Location location = new Location(world, -1163, 80, 300);
                if (applicationPlayer != null) {
                    Player applicantionOnlinePlayer = Bukkit.getPlayer(UUID.fromString(applicant.getUUID()));
                    applicantionOnlinePlayer.teleport(location);
                    applicantionOnlinePlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    PlayerInventory inventoryp = applicantionOnlinePlayer.getInventory();
                    inventoryp.clear();
                }
                //remove zone from map
                player.sendMessage(Component.text(MessageUtils.getMessage("ZonaDel", player)).color(NamedTextColor.RED));
                ZoneUtils.removeRegion(player, applicationZone);
                player.sendMessage(Component.text(MessageUtils.getMessage("ZonaDel1", player)).color(NamedTextColor.GREEN));

                //teleport reviewer
                player.teleport(location);
                player.sendMessage(Component.text(MessageUtils.getMessage("AppAprov", player)).color(NamedTextColor.GOLD));
                DiscordActions.sendPrivateMessage(applicant.getDiscord(), MessageUtils.getMessageConsole("AppAprovBuilder"));
            } else if(command[1].equalsIgnoreCase("recusar")){
                String reason = "";
                for (int i = 2; i < command.length; i++) {
                    reason += command[i] + " ";
                }
                reason = reason.trim();
                pendingService.removePending(pending);
                DiscordActions.sendPrivateMessage(applicant.getDiscord(), MessageUtils.getMessageConsole("AppRecusada1")+reason+
                MessageUtils.getMessageConsole("AppRecusada2"));

                World world = player.getWorld();
                Location location = new Location(world, -1163, 80, 300);
                player.teleport(location);
                if (applicationPlayer != null) {
                    WorldGuardService worldGuardService = new WorldGuardService();
                    worldGuardService.addPermissionWG("apply"+applicant.getgetZone()+"d",applicationPlayer.getPlayer(),applicationPlayer.getUniqueId());
                }
                player.sendMessage(Component.text(MessageUtils.getMessage("AppRecusada3", player)).color(NamedTextColor.GOLD));
            }
        } else{
            player.sendMessage(Component.text(MessageUtils.getMessage("NotAnalisar", player)).color(NamedTextColor.GOLD));
        }

    }
}

