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
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Prompts.AnalisarPrompt;
import io.github.LucasMullerC.Util.Mensagens;

public class analisar implements CommandExecutor {
    Aplicantes A;
    Pendentes P;
    Zonas Zn;
    BTEBrasilSystem plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "/analisar [app|claim]");
            return true;
        } else if (args[0].equalsIgnoreCase("claim") && player.hasPermission("group.reviewer")) {
            ConfirmarClaim(args, player);
            return true;
        } else if (args[0].equalsIgnoreCase("app")) {
            String time = Sistemas.VerificarEquipe(player);
            ConfirmarAplicacao(args, player, time);
            return true;
        } else {
            player.sendMessage(ChatColor.GOLD + Mensagens.Analisar404);
            player.sendMessage(ChatColor.GOLD + Mensagens.Analisar4041);
            return true;
        }
    }

    private static void ConfirmarClaim(String[] comando, Player player) {
        Pendentes P = GerenciarListas.getPendenteClaimAnalisar(player.getUniqueId().toString());
        World w = player.getWorld();
        if (P != null) {
            Areas Ar = GerenciarListas.getArea(P.getArea());
            String discordId = DiscordSRV.getPlugin().getAccountLinkManager()
                    .getDiscordId(UUID.fromString(Ar.getPlayer()));
            OfflinePlayer Dono = Bukkit.getOfflinePlayer(UUID.fromString(Ar.getPlayer()));

            String msg = "";
            for (int i = 1; i < comando.length; i++) {
                msg += comando[i] + " ";
            }
            msg = msg.trim();
            String[] arrayValores = msg.split(" ");
            if (arrayValores[0].equalsIgnoreCase("confirmar")) {
                BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
                ConversationFactory cf = new ConversationFactory(plugin);
                Conversation conv = cf.withFirstPrompt(new AnalisarPrompt(Ar, Dono, P, player.getWorld()).Builds)
                        .withLocalEcho(true).buildConversation(player);
                conv.begin();
                
            } else if (arrayValores[0].equalsIgnoreCase("recusar")) {
                String motivo = "";
                for (int i = 1; i < arrayValores.length; i++) {
                    motivo += arrayValores[i] + " ";
                }
                motivo = motivo.trim();
                //Regioes.addPermissaoWG(Ar.getClaim(), player, player.getUniqueId()); Adiciona permissões novamente
                DiscordPonte.sendMessage(GerenciarListas.getBuilder(Dono.getUniqueId().toString()).getDiscord(),
                        Mensagens.ClaimRecusada1 + Ar.getClaim() + Mensagens.ClaimRecusada2 + motivo
                                + Mensagens.ClaimRecusada3);
                if (!Ar.getParticipantes().equals("nulo")) {
                    String[] parts = Ar.getParticipantes().split(",");
                    for (int i = 0; i < parts.length; i++) {
                        Regioes.addPermissaoWG(Ar.getClaim(), player, UUID.fromString(parts[i]));
                        DiscordPonte.sendMessage(GerenciarListas.getBuilder(parts[i]).getDiscord(),
                                Mensagens.ClaimRecusada1 + Ar.getClaim() + Mensagens.ClaimRecusada2 + motivo
                                        + Mensagens.ClaimRecusada3);
                    }
                }
                GerenciarListas.RemoverPendenteClaim(Ar.getClaim());
                player.sendMessage(ChatColor.GOLD + Mensagens.ClaimRecusada4);

            } else {
                player.chat("/region select "+Ar.getClaim());
                player.sendMessage(Mensagens.AnaliseClaim1 + P.getArea() + Mensagens.VoceAnalisa2
                        + DiscordPonte.GetDiscordName(discordId));
                Location L = Sistemas.getLocation(Ar.getPontos(), w);
                player.teleport(L);
                player.sendMessage(ChatColor.GOLD + Mensagens.Analisar1);
                player.sendMessage(ChatColor.GOLD + Mensagens.Analisar2);
            }
            
        } else {
            player.sendMessage(ChatColor.GOLD + Mensagens.NotClaim);
        }
    }

    private void ConfirmarAplicacao(String[] comando, Player player, String time) {
        World w = player.getWorld();
        Boolean pass = false;
        String[] times = time.split(",");
        // Verifica se existe Aplicações
        for (int b = 0; b < times.length; b++) {
            if (GerenciarListas.getPendenteAplicacao(times[b]) != null) {
                pass = true;
                time = times[b];
                break;
            } else {
                pass = false;
            }
        }

        // Analisar Aplicações
        if (pass == true) {
            WorldGuardPlugin WGplugin = WGBukkit.getPlugin();
            RegionContainer container = WGplugin.getRegionContainer();
            RegionManager regions = container.get(w);

            P = GerenciarListas.getPendenteAplicacao(time);
            A = GerenciarListas.getAplicante(P.getUUID());
            OfflinePlayer pa = Bukkit.getOfflinePlayer(UUID.fromString(A.getUUID()));
            player.sendMessage(Mensagens.VoceAnalisa + pa.getName() + Mensagens.VoceAnalisa2
                    + DiscordPonte.GetDiscordName(A.getDiscord()));
            Zn = GerenciarListas.getZona(A.getZona());
            String msg = "";
            for (int i = 1; i < comando.length; i++) {
                msg += comando[i] + " ";
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
                GerenciarListas.RemoverPendenteAplicacao(A.getUUID());
                GerenciarListas.RemoverZona(Zn);
                // Adiciona Cargo no Discord
                DiscordPonte.addCargo(A.getUUID(), A.getTime(), A.getDiscord());
                // Teleporta Aplicante para o Spawn
                if (pa.isOnline() == true) {
                    Player app = Bukkit.getPlayer(UUID.fromString(A.getUUID()));
                    Regioes.RemovePermissao(app, A.getZona());
                    Location l = new Location(w, -1163, 80, 300);
                    app.teleport(l);
                    app.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    PlayerInventory inventoryp = app.getInventory();
                    inventoryp.clear();
                }
                // Remover Zona
                player.sendMessage(ChatColor.RED + Mensagens.ZonaDel);
                Regioes.removeRegion(w, Zn);
                player.sendMessage(ChatColor.GREEN + Mensagens.ZonaDel1);
                // Teleporta Aplicador
                Location l = new Location(w, -1163, 80, 300);
                player.teleport(l);
                player.sendMessage(ChatColor.GOLD + Mensagens.AppAprov);
                DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppAprovBuilder);
            } else if (arrayValores[0].equalsIgnoreCase("recusar")) {
                String motivo = "";
                for (int i = 1; i < arrayValores.length; i++) {
                    motivo += arrayValores[i] + " ";
                }
                motivo = motivo.trim();
                GerenciarListas.RemoverPendenteAplicacao(A.getUUID());
                DiscordPonte.sendMessage(A.getDiscord(), Mensagens.AppRecusada1 + motivo + Mensagens.AppRecusada2);
                Location l = new Location(w, -1163, 80, 300);
                if (pa.isOnline() == true) {
                    Regioes.AddPermissao(pa.getPlayer(), A.getZona());
                }
                player.teleport(l);
                player.sendMessage(ChatColor.GOLD + Mensagens.AppRecusada3);
            } else {

                Location l = GerenciarListas.getZona(A.getZona()).getld();
                player.teleport(l);
                player.setGameMode(GameMode.CREATIVE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                player.sendMessage(ChatColor.GOLD + Mensagens.Analisar1);
                player.sendMessage(ChatColor.GOLD + Mensagens.Analisar2);
            }
        } else {
            player.sendMessage(ChatColor.GOLD + Mensagens.NotAnalisar);
        }

    }
}
