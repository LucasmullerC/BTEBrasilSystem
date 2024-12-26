/*
 * This code was adapted from the [Repository BTEConoSur/bteConoSurCore] repository (https://github.com/BTEConoSur/bteConoSurCore).
 * Original code by BTEConoSur.
 * Modified by Lucas Müller.
 */
package io.github.LucasMullerC.discord.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.OptionMapping;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.util.ImageUtils;
import io.github.LucasMullerC.util.MessageUtils;

public class FindColor{
    private final Map<BufferedImage, Color> averageColors = new HashMap<>();
    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpeg", "jpg");
    
    BTEBrasilSystem plugin;

    public FindColor(@NotNull BTEBrasilSystem plugin) throws URISyntaxException, IOException {
        this.plugin = plugin;
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        File textures = new File(pluginFolder, "textures");
        File[] files = textures.listFiles();

        if (files != null) {
            for (File file : files) {
                BufferedImage img;
                try {
                    img = ImageIO.read(file);
                } catch (IOException e) {
                    return;
                }

                int red = 0, green = 0, blue = 0;
                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {
                        Color pixel = new Color(img.getRGB(x, y));
                        red += pixel.getRed();
                        green += pixel.getGreen();
                        blue += pixel.getBlue();
                    }
                }

                int total = img.getWidth() * img.getHeight();
                Color color = new Color(red/total, green/total, blue/total);

                averageColors.put(img, color);
            }
        }
    }

    public void onSlashCommandInteraction(@NotNull SlashCommandEvent event) {
        String subcommandName = event.getSubcommandName();
        assert subcommandName != null;
        
        Color color;
        if (subcommandName.equals("code")) {

            OptionMapping codeMapping = event.getOption("code");
            assert codeMapping != null;
            String code = codeMapping.getAsString();
            if (!code.matches("[0-9a-f]{6}")) {
                DiscordActions.sendError(event, MessageUtils.getMessagePT("slashfindcolor1"));
                return;
            }
            color = Color.decode("#" + code);
            generateTextures(color, event,false);
        } else {
            event.deferReply().queue();
            event.getChannel().getHistory().retrievePast(2).queue(messages -> {
                Color colorImage;
                Message.Attachment attachment;
                List<Message.Attachment> attachments = new ArrayList<>();
                for (Message message : messages) {
                    if(message.getAttachments() != null){
                        attachments = message.getAttachments();
                    }
                }

                if (!attachments.isEmpty()) {
                    attachment = attachments.get(0);
                } else{
                    DiscordActions.sendErrorHooked(event, MessageUtils.getMessagePT("slashfindcolor2"));
                    return;
                }
    
                if (!ALLOWED_EXTENSIONS.contains(attachment.getFileExtension())) {
                    DiscordActions.sendErrorHooked(event, MessageUtils.getMessagePT("slashfindcolor3")+" [2]");
                    return;
                }
    
                InputStream is;
                try {
                    is = attachment.retrieveInputStream().get();
                } catch (InterruptedException | ExecutionException e) {
                    DiscordActions.sendErrorHooked(event, MessageUtils.getMessagePT("slashfindcolor4"));
                    return;
                }
    
                BufferedImage img;
                try {
                    img = ImageIO.read(is);
                } catch (IOException e) {
                    DiscordActions.sendErrorHooked(event, MessageUtils.getMessagePT("slashfindcolor5"));
                    return;
                }
    
                int red = 0, green = 0, blue = 0;
                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {
                        Color pixel = new Color(img.getRGB(x, y));
                        red += pixel.getRed();
                        green += pixel.getGreen();
                        blue += pixel.getBlue();
                    }
                }
    
                int total = img.getWidth() * img.getHeight();
                colorImage = new Color(red/total, green/total, blue/total);

                generateTextures(colorImage, event,true);
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void generateTextures(Color color,SlashCommandEvent event,boolean hooked){
        List<Map.Entry<BufferedImage, Double>> distances = new ArrayList<>();
        for (Map.Entry<BufferedImage, Color> entry : averageColors.entrySet()) {

            double distance = Math.pow(entry.getValue().getRed() - color.getRed(), 2) + Math.pow(entry.getValue().getGreen() - color.getGreen(), 2) + Math.pow(entry.getValue().getBlue() - color.getBlue(), 2);
            distances.add(
                    new AbstractMap.SimpleEntry<>(
                            entry.getKey(), distance
                    )
            );

        }

        distances.sort(Map.Entry.comparingByValue());

        BufferedImage img = new BufferedImage(768, 256, 1);
        Graphics2D g = img.createGraphics();

        g.drawImage(distances.get(0).getKey().getScaledInstance(256, 256, 1), 0,0,null);
        g.drawImage(distances.get(1).getKey().getScaledInstance(256, 256, 1), 256,0,null);
        g.drawImage(distances.get(2).getKey().getScaledInstance(256, 256, 1), 512,0,null);

        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        if(hooked){
            try {
                InputStream imageStream = ImageUtils.getStream(img);
                event.getHook().sendMessageEmbeds(
                        new EmbedBuilder()
                                .setTitle(MessageUtils.getMessagePT("slashfindcolor6") + hex.toUpperCase())
                                .setColor(color)
                                .setImage("attachment://image.png")
                                .build()
                ).addFile(imageStream, "image.png")
                 .queue(
                        //msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.MINUTES)
                );
            } catch (IOException e) {
                DiscordActions.sendErrorHooked(event, MessageUtils.getMessagePT("ErrorGeneric"));
            }
        } else{
            try {
                InputStream imageStream = ImageUtils.getStream(img);
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setTitle("Texturas más cercanas al color #" + hex.toUpperCase())
                                .setColor(color)
                                .setImage("attachment://image.png")
                                .build()
                ).addFile(imageStream, "image.png")
                 .queue(
                        msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.MINUTES)
                );
            } catch (IOException e) {
                DiscordActions.sendError(event, MessageUtils.getMessagePT("ErrorGeneric"));
            }
        }
    }
}
