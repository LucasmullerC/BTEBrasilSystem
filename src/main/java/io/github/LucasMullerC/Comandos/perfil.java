package io.github.LucasMullerC.Comandos;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Util.Mensagens;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class perfil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.BLUE + "PERFIL - " + ChatColor.GREEN + player.getDisplayName());
        // ADMIN
        if (player.hasPermission("group.administrator")) {
            player.sendMessage(ChatColor.DARK_RED + "ADMIN - " + ChatColor.GOLD + "✓");
        }
        // MODERADOR
        if (player.hasPermission("group.moderator")) {
            player.sendMessage(ChatColor.GREEN + "MODERADOR - " + ChatColor.GOLD + "✓");
        }
        // SUPORTE
        if (player.hasPermission("group.helper")) {
            player.sendMessage(ChatColor.YELLOW + "SUPORTE - " + ChatColor.GOLD + "✓");
        }
        // REVIEWER
        if (player.hasPermission("group.reviewer")) {
            player.sendMessage(ChatColor.BLUE + "REVIEWER - " + ChatColor.GOLD + "✓");
        }
        // REVIEWER
        if (player.hasPermission("group.apoiador")) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "APOIADOR - " + ChatColor.GOLD + "✓");
        }
        // CONSTRUTOR
        if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()) != null) {
            String motivo = "BTE Brasil";
            player.sendMessage(ChatColor.AQUA + "CONSTRUTOR - " + ChatColor.GOLD + motivo);
            return true;
        } else

        {
            player.sendMessage(ChatColor.GOLD + Mensagens.PerfilNotBuilder);
            return true;
        }
    }

}
