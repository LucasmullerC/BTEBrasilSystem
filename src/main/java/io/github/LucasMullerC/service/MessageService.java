package io.github.LucasMullerC.service;

import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

public class MessageService {

    public Component getMessageWithURL(String message){
        final MiniMessage extendedInstance = MiniMessage.builder()
        .editTags(b -> b.tag("a", MessageService::createA))
        .build();

        return extendedInstance.deserialize(message);
    }

    private static Tag createA(final ArgumentQueue args, final Context ctx) {
        final String link = args.popOr("").value();

        return Tag.styling(
            NamedTextColor.BLUE,
            TextDecoration.UNDERLINED,
            ClickEvent.openUrl(link),
            HoverEvent.showText(Component.text(MessageUtils.getMessageConsole("openLink") + link))
        );
    }
    
}
