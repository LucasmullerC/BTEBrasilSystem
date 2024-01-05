package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.service.ReadFileService;
import io.github.LucasMullerC.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class data implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        if (sender instanceof ConsoleCommandSender) {
            if (arg3.length == 0) {
                sender.sendMessage(Component.text("/data [convert]").color(NamedTextColor.RED));
                return true;
            }else if (arg3[0].equalsIgnoreCase("convert")) {
                new ReadFileService().convertOldFilesToSql();
                sender.sendMessage(Component.text(MessageUtils.getMessageConsole("convertionDone")).color(NamedTextColor.GREEN));
                return true;
            }

            return true;
        }
        else{
            return false;
        }
    }

}
