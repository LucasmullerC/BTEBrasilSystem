package io.github.LucasMullerC.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import io.github.LucasMullerC.service.ReadFileService;

public class data implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
        if (sender instanceof ConsoleCommandSender) {
            if (arg3.length == 0) {
                sender.sendMessage("<red>/data [convert]");
                return true;
            }else if (arg3[0].equalsIgnoreCase("convert")) {
                new ReadFileService().convertOldFilesToSql();
                sender.sendMessage("<green>Conversion finished!");
                return true;
            }

            return true;
        }
        else{
            return false;
        }
    }

}
