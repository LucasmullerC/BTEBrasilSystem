package io.github.LucasMullerC.discord.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.util.MessageUtils;

public class Leaderboard {
    public MessageEmbed getCommand(int page) {
        Thumbnail thumb = new Thumbnail(MessageUtils.getMessagePT("slashlbthumb"), null, 100, 100);
        Footer ft = new Footer(MessageUtils.getMessagePT("footerbtebrasil"), null, null);
        int position = 0;
        if(page != 1 ){
            position = (page - 1) * 10;
        }
        MessageEmbed emb2 = new MessageEmbed(null, MessageUtils.getMessagePT("slashlbtitle"), formLb(position), null, null, 52224, thumb, null,
                null, null, ft, null,
                null);
        return emb2;
    }

    private String formLb(int position) {
        BuilderService builderService = new BuilderService();
        Map<String, Double> unsortedMap = builderService.getPointsMap();

        Map<String, Double> sortedMap = unsortedMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordena por valor em ordem decrescente
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        return printLb(sortedMap, position);
    }

    private <K, V> String printLb(Map<K, V> map, int position) {
        String lb = "";
        int cont = 1;
        List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
        BuilderService builderService = new BuilderService();

        position = Math.max(0, Math.min(position, entryList.size() - 1));
    
        for (int i = position; i < entryList.size(); i++) {
            Map.Entry<K, V> entry = entryList.get(i);
            Builder builder = builderService.getBuilderDiscord(entry.getKey().toString());
    
            if (entry.getKey().equals("nulo")) {
                OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(builder.getUUID()));
                lb += "**#" + (i+1) + " ->** _" + pl.getName() + "_\r\n";
            } else {
                lb += "**#" + (i+1) + " ->** <@" + entry.getKey() + ">\r\n";
            }
    
            String pts = String.valueOf(builder.getPoints());
            pts = pts.substring(0, pts.indexOf(".") + 2);

            lb += ":medal: "+MessageUtils.getMessagePT("slashlbtier")+" `" + builder.getTier().toString() + "`\r\n :chart_with_upwards_trend: "+MessageUtils.getMessagePT("slashlbpoints")+" `"
                    + pts + "`\r\n\r\n";
    
            if (cont == 10) {
                break;
            }
            cont++;
        }
        return lb;
    }


}
