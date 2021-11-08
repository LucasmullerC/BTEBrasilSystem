package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Util.Mensagens;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class addequipe implements CommandExecutor {
    Jogadores J;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("addequipe")) {
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            if (args.length == 0) {
                player.sendMessage(ChatColor.GOLD + Mensagens.addEquipe1);
                player.sendMessage(ChatColor.RED + Mensagens.PlayerOn);
                return true;
            } else {
                Player EquipeP = Bukkit.getPlayer(args[0]);
                if (EquipeP != null) {
                    UUID id2 = EquipeP.getUniqueId();
                    J = GerenciarListas.getEquipe(id.toString());
                    Boolean vef = GerenciarListas.addEquipe(id2.toString(), J.getTime());
                    if (vef == true) {
                        LuckPerms api = LuckPermsProvider.get();
                        User user = api.getPlayerAdapter(Player.class).getUser(EquipeP);
                        user.data().add(Node.builder("group.reviewer").build());
                        api.getUserManager().saveUser(user);
                        player.sendMessage(ChatColor.GREEN + Mensagens.Equipe1);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + Mensagens.Equipe2);
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + Mensagens.PlayerOn);
                    return true;
                }
            }
        }
        return false;
    }

}
