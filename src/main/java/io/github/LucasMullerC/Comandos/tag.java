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
        LuckPerms api = LuckPermsProvider.get();
        Player player = (Player) sender;
        if (args.length == 0) {
            player.chat("/tagmenu");
            return true;
        }
        //construtor
        if(args[0].equalsIgnoreCase("construtor")){
            if (args[1].equals("del")) {
                removePermissionBuilder(player, api);
                player.sendMessage(ChatColor.GREEN + Mensagens.TagRemove);
                return true;
            } else {
                if (player.hasPermission("group.b_" + args[1])) {
                    addPermissionBuilder(player, "prefix.0.&9Construtor " + args[1].toUpperCase(), api);
                    player.sendMessage(ChatColor.GREEN + Mensagens.Tagadd);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + Mensagens.Notconstrutor);
                    return true;
                }
            }
        }
        else if(args[0].equalsIgnoreCase("outros")){
            if (args[1].equals("del")) {
                removePermissionOutros(player, api);
                player.sendMessage(ChatColor.GREEN + Mensagens.TagRemove);
                return true;
            } else {
                if (player.hasPermission("group." + args[1])) {
                    addPermissionOutros(player, "prefix.0." + convertPerm(args[1]), api);
                    player.sendMessage(ChatColor.GREEN + Mensagens.Tagadd);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + Mensagens.Notperm);
                    return true;
                }
            }
        }
        return false;
        
        }  

    public void addPermissionBuilder(Player player, String permission, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        removePermissionBuilder(player, api);
        user.data().add(Node.builder(permission).build());
        api.getUserManager().saveUser(user);
    }

    public void removePermissionBuilder(Player player, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        //TAGS ANTIGAS
        user.data().remove(Node.builder("prefix.0.&9Construtor_SP").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_RJ_ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_MG").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_SUL").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_NE").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor_CO_N").build());
        //TAGS NOVAS
        user.data().remove(Node.builder("prefix.0.&9Construtor SP").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor RJ & ES").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor MG").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor SUL").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor NE").build());
        user.data().remove(Node.builder("prefix.0.&9Construtor C-O & N").build());
        
        api.getUserManager().saveUser(user);
    }

    public void addPermissionOutros(Player player, String permission, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        removePermissionOutros(player, api);
        user.data().add(Node.builder(permission).build());
        api.getUserManager().saveUser(user);
    }

    public void removePermissionOutros(Player player, LuckPerms api) {
        User user = api.getPlayerAdapter(Player.class).getUser(player);
        user.data().remove(Node.builder("prefix.0.&dApoiador").build());
        user.data().remove(Node.builder("prefix.0.&5BTE Staff").build());
        user.data().remove(Node.builder("prefix.0.&6Staff SUL").build());
        user.data().remove(Node.builder("prefix.0.&6Staff SP").build());
        user.data().remove(Node.builder("prefix.0.&6Staff RJ & ES").build());
        user.data().remove(Node.builder("prefix.0.&6Staff NE").build());
        user.data().remove(Node.builder("prefix.0.&6Staff MG").build());
        user.data().remove(Node.builder("prefix.0.&6Staff CO & N").build());
        user.data().remove(Node.builder("prefix.0.&bReviewer").build());
        user.data().remove(Node.builder("prefix.0.&eSuporte").build());
        user.data().remove(Node.builder("prefix.0.&2Moderador").build());
        user.data().remove(Node.builder("prefix.0.&4Admin").build());
        api.getUserManager().saveUser(user);
    }

    public String convertPerm(String perm){
        switch(perm){
            case "helper":
                return "&eSuporte";
            case "apoiador":
                return "&dApoiador";
            case "bte_staff":
                return "&5BTE Staff";
            case "c_sul":
                return "&6Staff SUL";
            case "c_sp":
                return "&6Staff SP";
            case "c_rj":
                return "&6Staff RJ & ES";
            case "c_ne":
                return "&6Staff NE";
            case "c_mg":
                return "&6Staff MG";
            case "c_co":
                return "&6Staff CO & N";
            case "reviewer":
                return "&bReviewer";
            case "moderator":
                return "&2Moderador";
            case "administrator":
                return "&4Admin";

        }
        return perm;
    }


}
