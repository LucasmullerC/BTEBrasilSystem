package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.Gerencia.Aplicar;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.Mensagens;

public class cancelar implements CommandExecutor {
    Aplicantes A;
    Pendentes AP;
    Zonas Zn;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        World world = player.getWorld();
        com.sk89q.worldedit.entity.Player playerbukkit = BukkitAdapter.adapt(player);
        com.sk89q.worldedit.world.World w = playerbukkit.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(w);
        Aplicar aplicar = new Aplicar();
        Regioes regioes = new Regioes();
        if (args.length == 0) {
            if (aplicar.getAplicante(id.toString()).getZona().equals("nulo")
                    || aplicar.getAplicante(id.toString()) == null) {
                player.sendMessage(ChatColor.GOLD + Mensagens.NenhumaAplicacao);
                return true;
            } else {
                A = aplicar.getAplicante(id.toString());
                Zn = aplicar.getZona(A.getZona());
                // Remove regiões
                regions.removeRegion("apply" + A.getZona() + "d");
                regions.removeRegion("apply" + A.getZona() + "c");
                regions.removeRegion("apply" + A.getZona() + "b");
                regions.removeRegion("apply" + A.getZona() + "a");
                // Remove Listas
                aplicar.RemoverAplicante(A.getUUID());
                aplicar.RemoverZona(Zn);
                if (aplicar.getPendentebyNameAplicacao(A.getUUID()) != null) {
                    aplicar.RemoverPendenteAplicacao(A.getUUID());
                }
                // Remover Zona
                player.sendMessage(ChatColor.RED + Mensagens.ZonaDel);
                regioes.removeRegion(player, Zn);
                player.sendMessage(ChatColor.GREEN + Mensagens.ZonaDel1);
                // Teleporta Player
                Location l = new Location(world, -1163, 80, 300);
                player.teleport(l);
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                PlayerInventory inventory = player.getInventory();
                inventory.clear();
                regioes.RemovePermissao(player, A.getZona());
                return true;
            }
        } else if (args[0] != null) {
            if (player.hasPermission("btebrasil.lider")) {
                if (aplicar.getAplicante(args[0]) == null) {
                    player.sendMessage(ChatColor.GOLD + Mensagens.NenhumaAplicacaoPlayer);
                    return true;
                } else if (aplicar.getAplicante(args[0]).getZona().equals("nulo")) {
                    player.sendMessage(ChatColor.GOLD + Mensagens.NenhumaAplicacaoPlayer);
                    return true;
                } else {
                    A = aplicar.getAplicante(args[0]);
                    Zn = aplicar.getZona(A.getZona());
                    // Remove regiões
                    regions.removeRegion("apply" + A.getZona() + "d");
                    regions.removeRegion("apply" + A.getZona() + "c");
                    regions.removeRegion("apply" + A.getZona() + "b");
                    regions.removeRegion("apply" + A.getZona() + "a");
                    // Remove Listas
                    aplicar.RemoverAplicante(A.getUUID());
                    aplicar.RemoverZona(Zn);
                    if (aplicar.getPendentebyNameAplicacao(A.getUUID()) != null) {
                        aplicar.RemoverPendenteAplicacao(A.getUUID());
                    }
                    // Remover Zona
                    player.sendMessage(ChatColor.RED + Mensagens.ZonaDel);
                    regioes.removeRegion(player, Zn);
                    player.sendMessage(ChatColor.GREEN + Mensagens.ZonaDel1);
                    // Teleporta Player
                    Location l = new Location(world, -1163, 80, 300);
                    player.teleport(l);
                    OfflinePlayer pa = Bukkit.getOfflinePlayer(UUID.fromString(A.getUUID()));
                    if (pa.isOnline() == true) {
                        Player app = Bukkit.getPlayer(UUID.fromString(A.getUUID()));
                        app.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        PlayerInventory inventory = app.getInventory();
                        inventory.clear();
                        app.teleport(l);
                        regioes.RemovePermissao(app, A.getZona());
                    }
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + Mensagens.Perm1);
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + Mensagens.ErroCancelar);
            return true;
        }
    }

}
