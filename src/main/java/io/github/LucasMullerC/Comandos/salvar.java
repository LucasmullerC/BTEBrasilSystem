package io.github.LucasMullerC.Comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Gerencia.Claim;

public class salvar implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Claim claim = new Claim();
    if (args[0].equalsIgnoreCase("flags")) {
      claim.setFlags();
      System.out.println("Flags Setadas!");
    } else if (args[0].equalsIgnoreCase("perms")) {
      Player player = (Player) sender;
      claim.setPerms(player);
      System.out.println("Perms Setadas!");
    }
    return true;
  }

}
