package io.github.LucasMullerC.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;

public class salvar implements CommandExecutor {
    GerenciarListas S;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("salvar")) {
            final Player player = (Player) sender;
            S.equipe.save();
            S.aplicante.save();
            S.pendente.save();
            S.zonas.save();
            player.sendMessage(ChatColor.GREEN + "BTEBrasil System Salvo!");
        }
        return false;
    }

}
