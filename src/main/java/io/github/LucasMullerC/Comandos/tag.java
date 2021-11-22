package io.github.LucasMullerC.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Util.Mensagens;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class tag implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tag")) {
            LuckPerms api = LuckPermsProvider.get();
            Player player = (Player) sender;
            if (args.length == 0) {
                player.chat("/tagmenu");
                return true;
            } else if (args[0].equals("del")) {
                removePermission(player, api);
                player.sendMessage(ChatColor.GREEN + Mensagens.TagRemove);
                return true;
            } else {
                if (player.hasPermission("group.b_" + args[0])) {
                    addPermission(player, "prefix.0.&9Construtor_" + args[0].toUpperCase(), api);
                    player.sendMessage(ChatColor.GREEN + Mensagens.Tagadd);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + Mensagens.Notconstrutor);
                    return true;
                }
            }
        }
        return false;
    }

    public void addPermission(Player player, String permission, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        user.data().remove(Node.builder("prefix.0.&9Construtor_SP").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_RJ").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_MG").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_SUL").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_NE").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_CO_N").build());
        user.data().add(Node.builder(permission).build());
        api.getUserManager().saveUser(user);
    }

    public void removePermission(Player player, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        user.data().remove(Node.builder("prefix.0.&9Construtor_SP").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_RJ").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_MG").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_SUL").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_NE").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_CO_N").build());
        api.getUserManager().saveUser(user);
    }
}
