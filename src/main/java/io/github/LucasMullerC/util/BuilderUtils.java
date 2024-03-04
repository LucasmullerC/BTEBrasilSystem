package io.github.LucasMullerC.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import io.github.LucasMullerC.model.Builder;

public class BuilderUtils {
    public static void checkRank(Builder builder){
        OfflinePlayer Offplayer = Bukkit.getOfflinePlayer(UUID.fromString(builder.getUUID()));
        if (builder != null) {
            int tier = builder.getTier();
            double NextLvl = (tier * 150) * 2.25;

            if (builder.getPoints() >= NextLvl) {
                //TODO Verificar forma de fazer um while até upar todos os ranks e enviar uma mensagem com todos os ranks upados de uma só vez.
            }
            else{

            }
        }
        if (Offplayer.isOnline()) {
            //somente usar para enviar a msg no mine
        }
    }
}
