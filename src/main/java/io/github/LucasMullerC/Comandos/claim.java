package io.github.LucasMullerC.Comandos;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Gerencia.Claim;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Prompts.ClaimPrompt;
import io.github.LucasMullerC.Util.ClaimUtils;
import io.github.LucasMullerC.Util.Mensagens;

public class claim implements CommandExecutor {
    BTEBrasilSystem plugin;
    Builders B;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Builder builder = new Builder();
        Regioes regioes = new Regioes();
        if (args.length == 0) {
            if (sender instanceof ConsoleCommandSender) {
                builder.MontarMapa();
                System.out.println("Mapa Gerado!");
                return true;
            } else {
                Player player = (Player) sender;
                player.chat("/claimmenu");
            }
            return true;
        }
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(id);
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();

        if (discordId == null) {
            player.sendMessage(ChatColor.GOLD + "Ops! Aparentemente você não está linkado...");
            player.sendMessage(ChatColor.GOLD + "Digite /link Nick_Discord");
            return true;
        }
        if (player.hasPermission("group.b_br") == false && player.hasPermission("group.builder_not") == false) {
            player.sendMessage(ChatColor.GOLD + "Aparentemente você não é um construtor...");
            player.sendMessage(ChatColor.GOLD + "Inicie a aplicação para participar!");
            return true;
        }
        ConversationFactory cf = new ConversationFactory(plugin);
        // Verifica se o Builder já está na lista
        builder.addBuilder(id.toString(), discordId);
        if (args[0].equalsIgnoreCase("add")) {
            B = builder.getBuilder(id.toString());
            if (CalcLimite(B.getTier(), player) == false || player.hasPermission("group.nolimit")) {
                String pontos = regioes.getSelection(player, B.getTier());
                if (pontos.length() == 1) {
                    switch (pontos) {
                        case "1":
                            player.sendMessage(ChatColor.RED + Mensagens.AreasIntersec);
                            player.sendMessage(ChatColor.GOLD + Mensagens.LinkMapa);
                            return true;
                        case "2":
                            player.sendMessage(ChatColor.RED + Mensagens.AreasLimite3);
                            player.sendMessage(ChatColor.GOLD + Mensagens.AreasLimite2);
                            return true;
                        case "3":
                            player.sendMessage(ChatColor.RED + Mensagens.AreasSelecao);
                            return true;
                    }
                } else {
                    Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, pontos, "").Add).withLocalEcho(true)
                            .buildConversation(player);
                    conv.begin();
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + Mensagens.AreasLimite1);
                player.sendMessage(ChatColor.GOLD + Mensagens.AreasLimite2);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("abandonar") || args[0].equalsIgnoreCase("remover")) {

            Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").remover).withLocalEcho(true)
                    .buildConversation(player);
            conv.begin();
            return true;
        } else if (args[0].equalsIgnoreCase("completo")) {
            Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").completoStart).withLocalEcho(true)
                    .buildConversation(player);
            conv.begin();
            return true;
        } else if (args[0].equalsIgnoreCase("equipe")) {
            if (args[1].equalsIgnoreCase("add")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").equipe).withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();
                return true;
            } else if (args[1].equalsIgnoreCase("remover")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").equipeRemover)
                        .withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();
                return true;
            } else if (args[1].equalsIgnoreCase("sair")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").sair).withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();
                return true;
            } else {
                player.sendMessage(ChatColor.RED + Mensagens.ClaimCommand1);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("editar")) {
            Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").nome).withLocalEcho(true)
                    .buildConversation(player);
            conv.begin();
            return true;

            /*
             * Metodo editar antigo
             * if (args.length == 1) {
             * Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0",
             * "").editar).withLocalEcho(true)
             * .buildConversation(player);
             * conv.begin();
             * return true;
             * } else if (args[1].equalsIgnoreCase("nome")) {
             * Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0",
             * "").nome).withLocalEcho(true)
             * .buildConversation(player);
             * conv.begin();
             * return true;
             * }
             */
        } else if (args[0].equalsIgnoreCase("img")) {
            if (args[1].equalsIgnoreCase("add")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").img).withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();
                return true;
            } else if (args[1].equalsIgnoreCase("remover")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, "0", "").imgremover).withLocalEcho(true)
                        .buildConversation(player);
                conv.begin();
                return true;

            }
        } else if (args[0].equalsIgnoreCase("info")) {
            int claimsCompletosNum, claimEmProgressoNum;
            ArrayList<Areas> completedClaims = new ArrayList<>();
            ArrayList<Areas> notcompletedClaims = new ArrayList<>();
            ArrayList<Pendentes> playerPendente = new ArrayList<>();
            Claim claim = new Claim();

            claimEmProgressoNum = claim.getAreaQtdByPlayerNum(player.getUniqueId().toString());
            claimsCompletosNum = claim.getAreaCompletaQtdByPlayerNum(player.getUniqueId().toString());
            notcompletedClaims = claim.getAreaNotCompletedByPlayerUuid(player.getUniqueId().toString());
            completedClaims = claim.getAreaCompletedByPlayerUuid(player.getUniqueId().toString());
            playerPendente = claim.getPendentePlayer(B.getUUID());

            player.sendMessage(ChatColor.DARK_BLUE + "CLAIMS EM CONSTRUÇÃO - " + ChatColor.GOLD + claimEmProgressoNum);
            ClaimUtils.printAreasMinecraft(notcompletedClaims, player);
            if (!playerPendente.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + "CLAIMS EM ANÁLISE - " + ChatColor.GOLD + playerPendente.size());
                ClaimUtils.printPendentesMinecraft(playerPendente, player);
            }
            player.sendMessage(ChatColor.GREEN + "CLAIMS COMPLETOS - " + ChatColor.GOLD + claimsCompletosNum);
            ClaimUtils.printAreasMinecraft(completedClaims, player);
        } else if (args[0].equalsIgnoreCase("addcompleto")) {
            if (player.hasPermission("btebrasil.addcompleto")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.RED + Mensagens.InfUUID);
                } else {
                    String pontos = regioes.getSelection(player, 0);
                    if (pontos.length() == 1) {
                        switch (pontos) {
                            case "1":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasIntersec);
                                player.sendMessage(ChatColor.GOLD + Mensagens.LinkMapa);
                                return true;
                            case "2":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasLimite3);
                                player.sendMessage(ChatColor.GOLD + Mensagens.AreasLimite2);
                                return true;
                            case "3":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasSelecao);
                                return true;
                        }
                    } else {
                        builder.addBuilder(args[1], "nulo");
                        Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, pontos, args[1]).addcompleto)
                                .withLocalEcho(true)
                                .buildConversation(player);
                        conv.begin();
                        return true;
                    }
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("addconstrucao")) {
            if (player.hasPermission("btebrasil.addcompleto")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.RED + Mensagens.InfUUID);
                } else {
                    String pontos = regioes.getSelection(player, 0);
                    if (pontos.length() == 1) {
                        switch (pontos) {
                            case "1":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasIntersec);
                                player.sendMessage(ChatColor.GOLD + Mensagens.LinkMapa);
                                return true;
                            case "2":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasLimite3);
                                player.sendMessage(ChatColor.GOLD + Mensagens.AreasLimite2);
                                return true;
                            case "3":
                                player.sendMessage(ChatColor.RED + Mensagens.AreasSelecao);
                                return true;
                        }
                    } else {
                        builder.addBuilder(args[1], "nulo");
                        Conversation conv = cf.withFirstPrompt(new ClaimPrompt(player, pontos, args[1]).addconstrucao)
                                .withLocalEcho(true)
                                .buildConversation(player);
                        conv.begin();
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean CalcLimite(int Tier, Player player) {
        UUID id = player.getUniqueId();
        int Limite = 3;
        Claim claim = new Claim();

        if (Tier >= 1 && Tier <= 3) {
            Limite = 3;
        } else if (Tier >= 4 && Tier <= 6) {
            Limite = 4;
        } else if (Tier >= 7 && Tier <= 9) {
            Limite = 5;
        } else if (Tier >= 10 && Tier <= 12) {
            Limite = 6;
        } else if (Tier >= 13 && Tier <= 15) {
            Limite = 7;
        } else {
            Limite = Tier - 7;
        }

        if (player.hasPermission("group.apoiador")) {
            Limite = Limite + 10;
        }
        return claim.getAreaQtdByPlayer(id.toString(), Limite);

    }

}
