package io.github.LucasMullerC.Comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;

public class salvar implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            GerenciarListas.aplicante.save();
            GerenciarListas.pendente.save();
            GerenciarListas.zonas.save();
            GerenciarListas.builder.save();
            GerenciarListas.areas.save();
            GerenciarListas.mapa.save();
            GerenciarListas.conquista.save();
            System.out.println("BTEBrasil System Salvo!");
        }
        if (args[0].equalsIgnoreCase("flags")) {
          GerenciarListas.setFlags();
          System.out.println("Flags Setadas!");
        }
        else if (args[0].equalsIgnoreCase("perms")) {
            Player player = (Player) sender;
            GerenciarListas.setPerms(player);
            System.out.println("Perms Setadas!");
          }
        return true;
    }

}
