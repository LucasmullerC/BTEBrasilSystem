package io.github.LucasMullerC.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.MessageService;
import io.github.LucasMullerC.service.applicant.ApplicantService;
import io.github.LucasMullerC.service.applicant.ApplicationZoneService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.LocationUtil;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.RegionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class back implements CommandExecutor{

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
            for(Claim claim:claimList){
                if(claim.getDifficulty()>0 && ClaimUtils.verifyClaimProperties(claim, player, false) == true){
                    Location location = RegionUtils.getRegionCopyLocation("copy"+claim.getClaim(), player);
                    player.teleport(location);

                    String[] ary = claim.getPoints().split(",");
                    int[] centralCoordinate = LocationUtil.getCentralPoint(ary);
                    double[] coords = RegionUtils.toGeo(centralCoordinate[0],centralCoordinate[1]);
                    String coordinates = coords[1]+","+coords[0];

                    final String input = MessageUtils.getMessage("joinclaiminfo1", player)+" <a:https://www.google.com.br/maps/place/"+coordinates+">"+coordinates+"</a>";
                    if(!builder.getDiscord().equals("nulo")){
                        DiscordActions.sendPrivateMessage(builder.getDiscord(),MessageUtils.getMessage("joinclaiminfo1", player)+" https://www.google.com.br/maps/place/"+coordinates);
                    }
                    MessageService messageService = new MessageService();
                    player.sendMessage(messageService.getMessageWithURL(input).color(NamedTextColor.GREEN));
                    ClaimUtils.createBook(player, input, claim);

                    return true;
                }
            }
        }

        ApplicantService applicantService = new ApplicantService();
        Applicant applicant = applicantService.getApplicant(id.toString());
        if(applicant == null){
            player.sendMessage(Component.text(MessageUtils.getMessage("NenhumaAplicacao", player)).color(NamedTextColor.GOLD));
            return true;
        } else{
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            player.sendMessage(Component.text(MessageUtils.getMessage("AppRemind", player)).color(NamedTextColor.GOLD));
            ApplicationZoneService applicationZoneService = new ApplicationZoneService();
            ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());
            Location location;
            switch (applicant.getSection()) {
                case "a":
                location = applicationZone.getLocationA();
                break;
                case "b":
                location = applicationZone.getLocationB();
                break;
                case "c":
                location = applicationZone.getLocationC();
                break;
                case "d":
                location = applicationZone.getLocationD();
                break;
                default:
                return false;
            }
            player.teleport(location);
            return true;
        }
    }
    
}
