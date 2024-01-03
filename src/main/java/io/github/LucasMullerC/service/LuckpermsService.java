package io.github.LucasMullerC.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import net.luckperms.api.LuckPerms;
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
}
