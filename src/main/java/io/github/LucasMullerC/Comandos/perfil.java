package io.github.LucasMullerC.Comandos;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Util.Mensagens;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class perfil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Jogadores E;
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        player.sendMessage(ChatColor.BLUE + "PERFIL - " + ChatColor.GREEN + player.getDisplayName());
        if (GerenciarListas.getEquipe(id.toString()) != null) {
            E = GerenciarListas.getEquipe(id.toString());
            if (E.getTime().equals("b_ne")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Nordeste");
            } else if (E.getTime().equals("b_sp")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time São Paulo");
            } else if (E.getTime().equals("b_mg")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Minas Gerais");
            } else if (E.getTime().equals("b_es")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Espírito Santo");
            } else if (E.getTime().equals("b_rj")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Rio de Janeiro");
            } else if (E.getTime().equals("b_sul")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Sul");
            } else if (E.getTime().equals("b_co")) {
                player.sendMessage(ChatColor.RED + "EQUIPE - " + ChatColor.GOLD + "Time Centro-Oeste e Norte");
            }
        }
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
                    motivo += "Time Rio de Janeiro,";
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
