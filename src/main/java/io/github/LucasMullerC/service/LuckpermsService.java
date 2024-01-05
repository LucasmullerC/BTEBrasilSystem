package io.github.LucasMullerC.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class LuckpermsService {
    private LuckPerms luckPerms;

    public LuckpermsService() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        } else {
            System.out.println("Luckperms not Found!");
        }
    }

    public void addGroup(Player player, String groupName) {
        if (luckPerms != null) {
            UserManager userManager = luckPerms.getUserManager();
            net.luckperms.api.model.group.@NonNull GroupManager groupManager = luckPerms.getGroupManager();

            User user = userManager.getUser(player.getUniqueId());
            Group group = groupManager.getGroup(groupName);

            if (user != null && group != null) {
                Node node = Node.builder("group." + groupName).build();
                user.data().add(node);
                userManager.saveUser(user);
            }
        }
    }

    public void removeGroup(Player player, String groupName) {
        if (luckPerms != null) {
            UserManager userManager = luckPerms.getUserManager();
            net.luckperms.api.model.group.@NonNull GroupManager groupManager = luckPerms.getGroupManager();

            User user = userManager.getUser(player.getUniqueId());
            Group group = groupManager.getGroup(groupName);

            if (user != null && group != null) {
                Node node = Node.builder("group." + groupName).build();
                user.data().remove(node);
                userManager.saveUser(user);
            }
        }
    }

    //Permissions
    public void removePermissionLuckPerms(String regionID, UUID uid) {
        LuckPerms api = LuckPermsProvider.get();
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uid);
        User user;
        try {
            user = userFuture.get();
            Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", regionID)
                    .build();
            Node nodeb = Node.builder("worldguard.region.select.member." + regionID).value(true).build();
            user.data().remove(nodea);
            user.data().remove(nodeb);
            api.getUserManager().saveUser(user);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void addPermissionLuckPerms(String regionID, UUID uid) {
        LuckPerms api = LuckPermsProvider.get();
        UserManager userManager = api.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uid);
        User user;
        try {
            user = userFuture.get();
            Node nodea = Node.builder("worldedit.*").value(true).withContext("worldguard:region", regionID)
                    .build();
            Node nodeb = Node.builder("worldguard.region.select.member." + regionID).value(true).build();
            user.data().add(nodea);
            user.data().add(nodeb);
            api.getUserManager().saveUser(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
