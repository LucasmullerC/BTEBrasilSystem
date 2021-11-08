package io.github.LucasMullerC.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Util.Mensagens;

public class removerequipe implements CommandExecutor {
    Jogadores J;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("removerequipe")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.GOLD + Mensagens.Equipe3);
                return true;
            }
        } else if (GerenciarListas.getEquipe(args[0]) != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " group remove reviewer");
            GerenciarListas.RemoverEquipe(args[0]);
            sender.sendMessage(ChatColor.GREEN + Mensagens.EquipeRemove);
            return true;
        }
        sender.sendMessage(ChatColor.RED + Mensagens.EquipeNotFound);
        return true;

    }
}
