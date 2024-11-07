package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.util.BuilderUtils;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class status implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
            Player player = (Player) sender;
            BuilderService builderService = new BuilderService();
            Builder builder = builderService.getBuilderUuid(player.getUniqueId().toString());

            if(builder == null){
                player.sendMessage(Component.text(MessageUtils.getMessage("PerfilNotBuilder", player)).color(NamedTextColor.GOLD));
                return true;
            } else {
                ClaimService claimService = new ClaimService();
                String claimNum = String.valueOf(claimService.getClaimQtdByPlayer(player.getUniqueId().toString()));
                String completedClaimNum = String.valueOf(claimService.getCompletedClaimQtdByPlayer(player.getUniqueId().toString()));
                String nextLevel = BuilderUtils.toNextLevel(builder);

                player.sendMessage(Component.text(MessageUtils.getMessage("statustitle", player)+" ").color(NamedTextColor.BLUE).
                append(Component.text(player.getName()).color(NamedTextColor.GREEN)));

                player.sendMessage(Component.text(MessageUtils.getMessage("tier", player)+" ").color(NamedTextColor.DARK_RED).
                append(Component.text(builder.getTier().toString()).color(NamedTextColor.GOLD)));

                player.sendMessage(Component.text(MessageUtils.getMessage("pointstitle", player)+" ").color(NamedTextColor.YELLOW).
                append(Component.text(String.valueOf(builder.getPoints())+ " / " + nextLevel).color(NamedTextColor.GOLD)));

                player.sendMessage(Component.text(MessageUtils.getMessage("claimsunderconstructiontitle", player)+" ").color(NamedTextColor.BLUE).
                append(Component.text(claimNum).color(NamedTextColor.GOLD)));

                player.sendMessage(Component.text(MessageUtils.getMessage("completedclaimstitle", player)+" ").color(NamedTextColor.GREEN).
                append(Component.text(completedClaimNum).color(NamedTextColor.GOLD)));

                return true;
            }

    }
    
}
