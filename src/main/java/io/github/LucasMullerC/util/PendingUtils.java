package io.github.LucasMullerC.util;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import io.github.LucasMullerC.model.Pending;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PendingUtils {
        public static void printPendingMinecraft(ArrayList<Pending> pendings,Player player,int infoPage){
        int itemsPerPage = 5;
        int totalItems = pendings.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
    
        int startIndex = (infoPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        if (infoPage < 1 || infoPage > totalPages) {
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopageinvalid", player) + totalPages).color(NamedTextColor.RED));
            return;
        }

        for (int i = startIndex; i < endIndex; i++) {
            Pending pending = pendings.get(i);
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfoid", player))
                .color(NamedTextColor.RED)
                .append(Component.text(pending.getregionId()))
                .color(NamedTextColor.GOLD));
        }

        if (infoPage < totalPages) {
            player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopagecommand1", player) +" "+ (infoPage + 1) + " "+MessageUtils.getMessage("claiminfopagecommand2", player))
                .color(NamedTextColor.GREEN));
        }
    }

    public static String printPendingDiscord(ArrayList<Pending> pendings,int infoPage){
        int itemsPerPage = 5;
        int totalItems = pendings.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
    
        int startIndex = (infoPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        String pendingList = "";

        if (infoPage < 1 || infoPage > totalPages) {
            return "";
        }

        for (int i = startIndex; i < endIndex; i++) {
            Pending pending = pendings.get(i);
            pendingList += MessageUtils.getMessageConsole("claimbody4") + pending.getregionId();
        }
        return pendingList;
    }
}
