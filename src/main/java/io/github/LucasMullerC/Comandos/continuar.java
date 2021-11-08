package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Util.Mensagens;

public class continuar implements CommandExecutor {
    Aplicantes A;
    BTEBrasilSystem plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("continuar")) {
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            if (GerenciarListas.getAplicante(id.toString()) == null) {
                player.sendMessage(ChatColor.GOLD + Mensagens.NenhumaAplicacao);
                return true;
            } else {
                A = GerenciarListas.getAplicante(id.toString());
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                player.sendMessage(ChatColor.GOLD + Mensagens.AppRemind);
                if (A.getSeccao().equals("a")) {
                    Location l = GerenciarListas.getZona(A.getZona()).getla();
                    player.teleport(l);
                    return true;
                } else if (A.getSeccao().equals("b")) {
                    Location l = GerenciarListas.getZona(A.getZona()).getlb();
                    player.teleport(l);
                    return true;
                } else if (A.getSeccao().equals("c")) {
                    Location l = GerenciarListas.getZona(A.getZona()).getlc();
                    player.teleport(l);
                    return true;
                } else if (A.getSeccao().equals("d")) {
                    Location l = GerenciarListas.getZona(A.getZona()).getld();
                    player.teleport(l);
                    return true;
                }
            }

        }
        return false;
    }

}