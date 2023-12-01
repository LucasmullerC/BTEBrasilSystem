package io.github.LucasMullerC.Util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Pendentes;

public class ClaimUtils {
    
    public static void printAreasMinecraft(ArrayList<Areas> areas,Player player){
        for (Areas area : areas) {
            player.sendMessage(ChatColor.RED + "ID - " + ChatColor.GOLD + area.getClaim());
        }
    }
    public static String printAreasString(ArrayList<Areas> areas){
        String areasList = "";
        for (Areas area : areas) {
            areasList += "\r\n\r\n ID - "+area.getClaim();
        }
        return areasList;
    }

    public static void printPendentesMinecraft(ArrayList<Pendentes> pendentes,Player player){
        for (Pendentes pendente : pendentes) {
            player.sendMessage(ChatColor.RED + "ID - " + ChatColor.GOLD + pendente.getArea());
        }
    }

    public static String printPendentesString(ArrayList<Pendentes> pendentes){
        String pendentesList = "";
        for(Pendentes pendente : pendentes){
            pendentesList += "\r\n\r\n ID - "+pendente.getArea();
        }
        return pendentesList;
    }
}
