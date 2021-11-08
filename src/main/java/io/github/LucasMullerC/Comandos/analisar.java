package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.Mensagens;

public class analisar implements CommandExecutor {
    Aplicantes A;
    Zonas Zn;
    BTEBrasilSystem plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        World w = player.getWorld();
        WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
        RegionContainer container = WGplugin.getRegionContainer();
        RegionManager regions = container.get(w);
        if (command.getName().equalsIgnoreCase("analisar")) {
            String time = GerenciarListas.getEquipe(id.toString()).getTime();
            String[] times = time.split(",");
            Boolean pass = false;
            for (int b = 0; b < times.length; b++) {
                if (GerenciarListas.getPendente(times[b]) != null) {
                    pass = true;
                    time = times[b];
                    break;
                } else {
                    pass = false;
                }
            }
            if (pass == true) {
                A = GerenciarListas.getPendente(time);
                OfflinePlayer pa = Bukkit.getOfflinePlayer(UUID.fromString(A.getUUID()));
                player.sendMessage(Mensagens.VoceAnalisa + pa.getName() + Mensagens.VoceAnalisa2
                        + DiscordPonte.GetDiscordName(A.getDiscord()));
                Zn = GerenciarListas.getZona(A.getZona());
                String msg = "";
                for (int i = 0; i < args.length; i++) {
                    msg += args[i] + " ";
                }
                msg = msg.trim();
                String[] arrayValores = msg.split(" ");
                if (arrayValores[0].equalsIgnoreCase("confirmar")) {
                    // Remove regiões
                    regions.removeRegion("apply" + A.getZona() + "d");
                    regions.removeRegion("apply" + A.getZona() + "c");
                    regions.removeRegion("apply" + A.getZona() + "b");
                    regions.removeRegion("apply" + A.getZona() + "a");
                    // Remove Listas
                    Zn = GerenciarListas.getZona(A.getZona());
                    GerenciarListas.RemoverAplicante(A.getUUID());
                    GerenciarListas.RemoverPendente(A);
                    GerenciarListas.RemoverZona(Zn);
                    // Adiciona Cargo no Discord
                    DiscordPonte.addCargo(A.getUUID(), A.getTime(), A.getDiscord());
                    // Teleporta Aplicante para o Spawn
                    if (pa.isOnline() == true) {
                        Player app = Bukkit.getPlayer(UUID.fromString(A.getUUID()));
                        Sistemas.RemovePermissao(app, A.getZona());
                        Location l = new Location(w, -1163, 80, 300);
                        app.teleport(l);
                        app.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        PlayerInventory inventoryp = app.getInventory();
                        inventoryp.clear();
                    }
                    // Remover Zona
                    player.sendMessage(ChatColor.RED + Mensagens.ZonaDel);
                    Sistemas.removeRegion(w, Zn);
                    player.sendMessage(ChatColor.GREEN + Mensagens.ZonaDel1);
                    // Teleporta Aplicador
                    Location l = new Location(w, -1163, 80, 300);
                    player.teleport(l);
                    player.sendMessage(ChatColor.GOLD + Mensagens.AppAprov);
                    DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppAprovBuilder);
                    return true;
                } else if (arrayValores[0].equalsIgnoreCase("recusar")) {
                    String motivo = "";
                    for (int i = 1; i < arrayValores.length; i++) {
                        motivo += arrayValores[i] + " ";
                    }
                    motivo = motivo.trim();
                    GerenciarListas.RemoverPendente(A);
                    DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppRecusada1 + motivo + Mensagens.AppRecusada2);
                    Location l = new Location(w, -1163, 80, 300);
                    if (pa.isOnline() == true) {
                        Sistemas.AddPermissao(pa.getPlayer(), A.getZona());
                    }
                    player.teleport(l);
                    player.sendMessage(ChatColor.GOLD + Mensagens.AppRecusada3);
                    return true;
                } else {

                    Location l = GerenciarListas.getZona(A.getZona()).getld();
                    player.teleport(l);
                    player.setGameMode(GameMode.CREATIVE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                    player.sendMessage(ChatColor.GOLD + Mensagens.Analisar1);
                    player.sendMessage(ChatColor.GOLD + Mensagens.Analisar2);
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.GOLD + Mensagens.NotAnalisar);
                return true;
            }
        }
        return false;
    }

}
