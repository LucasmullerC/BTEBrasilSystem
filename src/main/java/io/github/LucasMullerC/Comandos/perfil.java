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
        String time = Sistemas.VerificarEquipe(player);
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
        // STAFF
        if (time != "") {
            String[] times = time.split(",");
            String motivo = "";
            for (int b = 0; b < times.length; b++) {
                if (times[b].equals("b_ne")) {
                    motivo += "Time Nordeste,";
                } else if (times[b].equals("b_sp")) {
                    motivo += "Time São Paulo,";
                } else if (times[b].equals("b_mg")) {
                    motivo += "Time Minas Gerais,";
                } else if (times[b].equals("b_es")) {
                    motivo += "Time Espírito Santo,";
                } else if (times[b].equals("b_rj")) {
                    motivo += "Time Rio de Janeiro e Espírito Santo,";
                } else if (times[b].equals("b_sul")) {
                    motivo += "Time Sul,";
                } else if (times[b].equals("b_co")) {
                    motivo += "Time Centro-Oeste e Norte,";
                }
            }
            motivo = motivo.trim();
            player.sendMessage(ChatColor.RED + "STAFF - " + ChatColor.GOLD + motivo);
        }
        // REVIEWER
        if (player.hasPermission("group.apoiador")) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "APOIADOR - " + ChatColor.GOLD + "✓");
        }
        // CONSTRUTOR
        if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()) != null) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(Player.class).getUser(player);
            Collection<Node> grupos = user.getNodes();
            String motivo = "";
            for (Node d : grupos) {
                if (d.getKey() != null && d.getKey().contains("group.b_sp")) {
                    motivo += "Time São Paulo,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_ne")) {
                    motivo += "Time Nordeste,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_mg")) {
                    motivo += "Time Minas Gerais,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_es")) {
                    motivo += "Time Espírito Santo,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_rj")) {
                    motivo += "Time Rio de Janeiro e Espirito Santo,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_sul")) {
                    motivo += "Time Sul,";
                } else if (d.getKey() != null && d.getKey().contains("group.b_co")) {
                    motivo += "Time Centro-Oeste e Norte,";
                }
            }
            motivo = motivo.trim();
            player.sendMessage(ChatColor.AQUA + "CONSTRUTOR - " + ChatColor.GOLD + motivo);
            return true;
        } else

        {
            player.sendMessage(ChatColor.GOLD + Mensagens.PerfilNotBuilder);
            return true;
        }
    }

}
