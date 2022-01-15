package io.github.LucasMullerC.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Util.Mensagens;

public class status implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Builders B = GerenciarListas.getBuilder(player.getUniqueId().toString());
        if (B == null) {
            player.sendMessage(ChatColor.GOLD + Mensagens.PerfilNotBuilder);
            return true;
        } else {
            Boolean Vef = false;
            if(player.hasPermission("group.apoiador")){
                Vef = true;
            }
            String qtdAreas = String.valueOf(GerenciarListas.getAreaQtdByPlayerNum(B.getUUID()));
            String qtdCompletos = String.valueOf(GerenciarListas.getAreaCompletaQtdByPlayerNum(B.getUUID()));
            String NextLvl = Sistemas.ForNextLvl(B.getUUID(), Vef);

            player.sendMessage(ChatColor.BLUE + "STATUS - " + ChatColor.GREEN + player.getDisplayName());
            player.sendMessage(ChatColor.DARK_RED + "TIER - " + ChatColor.GOLD + B.getTier().toString());
            player.sendMessage(ChatColor.YELLOW + "PONTOS - " + ChatColor.GOLD +   String.valueOf(B.getPontos()) +" / " + NextLvl);
            player.sendMessage(ChatColor.BLUE + "CLAIMS EM CONSTRUÇÃO - " + ChatColor.GOLD + qtdAreas);
            player.sendMessage(ChatColor.GREEN + "CLAIMS COMPLETOS - " + ChatColor.GOLD + qtdCompletos);
            return true;
        }
    }

}
