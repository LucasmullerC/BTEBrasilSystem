package io.github.LucasMullerC.service;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.function.BiConsumer;

public class PromptService extends StringPrompt {

    private final String question;
    private final BiConsumer<ConversationContext, String> responseHandler;

    public PromptService(String question, BiConsumer<ConversationContext, String> responseHandler) {
        this.question = question;
        this.responseHandler = responseHandler;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.BOLD + question;
        //return ChatColor.BOLD + question;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        responseHandler.accept(context, input);
        return END_OF_CONVERSATION;
    }
}
