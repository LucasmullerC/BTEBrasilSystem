package io.github.LucasMullerC.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.entity.Player;

public class MessageUtils {
    private static ResourceBundle messages;

    public static String getMessage(String key, Player player) {
        Locale.setDefault(new Locale(player.getLocale()));
        messages = ResourceBundle.getBundle("messages");

        return messages.getString(key);
    }

    public static String getMessageConsole(String key) {
        messages = ResourceBundle.getBundle("messages");

        return messages.getString(key);
    }
}
