package io.github.LucasMullerC.Comandos;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.Gerencia.Aplicar;
import io.github.LucasMullerC.Util.Mensagens;

public class aplicacao implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        Aplicar aplicar = new Aplicar();
        if (player.hasPermission("group.b_br") || player.hasPermission("group.b_sp")
                || player.hasPermission("group.b_co") || player.hasPermission("group.b_es")
                || player.hasPermission("group.b_mg") || player.hasPermission("group.b_ne")
                || player.hasPermission("group.b_norte") || player.hasPermission("group.b_rj")
                || player.hasPermission("group.b_sul") || player.hasPermission("group.builder_not") == true) { // Já é
            // construtor
            // no
            // time
            player.sendMessage(ChatColor.RED + Mensagens.Isconstrutor);
            return true;
        } else { // Se ele não for construtor no time
            if (aplicar.getAplicante(id.toString()) != null) { // Se ele já for Aplicante
                player.sendMessage(ChatColor.RED + Mensagens.AplicacaoAndamento);
                player.sendMessage(ChatColor.GOLD + Mensagens.AplicacaoAndamento2);
                return true;
            } else { // Ele não é Aplicante
                if (discordId == null) { // Não está linkado
                    player.sendMessage(ChatColor.RED + Mensagens.Link1);
                    player.sendMessage(
                            ChatColor.YELLOW + Mensagens.Link2 + ChatColor.BLUE + Mensagens.InviteDiscord);
                    player.sendMessage(ChatColor.YELLOW + Mensagens.Link3 + ChatColor.BLUE + Mensagens.Link4
                            + ChatColor.YELLOW + Mensagens.Link5);
                    return true;
                } else {
                    if (DiscordPonte.CheckDiscord(discordId) == true) {
                        IniciarAplicacao(player, discordId, aplicar); // Aplicacão Iniciada
                        return true;

                    } else {// Se ele não estiver no Discor do time
                        player.sendMessage(ChatColor.YELLOW + Mensagens.NotDiscord);
                        return true;
                    }
                }

            }
        }
    }

    public void IniciarAplicacao(Player player, String Discord, Aplicar aplicar) {
        aplicar.addZonas(player, Discord);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
        player.getInventory().addItem(new ItemStack(Material.getMaterial("WOOD_AXE"), 1));
        player.sendMessage(ChatColor.GREEN + Mensagens.AppInit);
        player.sendMessage(ChatColor.GOLD + Mensagens.AppSec1);
        DiscordPonte.sendMessage(Discord, Mensagens.AppSec1);
    }

}
