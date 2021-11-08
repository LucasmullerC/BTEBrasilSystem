package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.Mensagens;

public class proximo implements CommandExecutor {
    Aplicantes A;
    Zonas Zn;
    BTEBrasilSystem plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        if (GerenciarListas.getAplicante(id.toString()) == null) {
            player.sendMessage(ChatColor.GOLD + Mensagens.AppProx);
            return true;
        } else {
            A = GerenciarListas.getAplicante(id.toString());
            Zn = GerenciarListas.getZona(A.getZona());
            if (A.getSeccao().equals("a")) {
                A.setSeccao("b");
                Zn.seta(false);
                Zn.setb(true);
                Location l = Zn.getlb();
                player.teleport(l);
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GOLD + Mensagens.AppSec2);
                        DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppSec2);
                    }
                }, 40L);
                return true;
            } else if (A.getSeccao().equals("b")) {
                A.setSeccao("c");
                Zn.setb(false);
                Zn.setc(true);
                Location l = Zn.getlc();
                player.teleport(l);
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GOLD + Mensagens.AppSec3);
                        player.sendMessage(ChatColor.YELLOW + Mensagens.AppTerr);
                        DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppSec3);
                    }
                }, 40L);
                return true;
            } else if (A.getSeccao().equals("c")) {
                A.setSeccao("d");
                Zn.setc(false);
                Zn.setd(true);
                Location l = Zn.getld();
                player.teleport(l);
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GOLD + Mensagens.AppSec4);
                        player.sendMessage(ChatColor.GOLD + Mensagens.GoogleMaps);
                        player.sendMessage(ChatColor.GOLD + Mensagens.AppSec5);
                        DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppSec4);
                        DiscordPonte.sendMessage(A.getDiscord(), Mensagens.GoogleMaps);
                        DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppSec5);
                    }
                }, 40L);
                return true;
            } else if (A.getSeccao().equals("d")) { // ultimo
                player.sendMessage(ChatColor.GOLD + Mensagens.AppCont);
                return true;
            }
        }
        return false;
    }

}
