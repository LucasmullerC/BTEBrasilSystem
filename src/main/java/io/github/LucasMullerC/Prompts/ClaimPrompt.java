package io.github.LucasMullerC.Prompts;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Gerencia.Claim;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Util.Mensagens;

public class ClaimPrompt {
    Player player;
    OfflinePlayer Amigo;
    String pontos, Builds, Uid, Claimid;
    String[] participante;
    static Areas A;
    int size, cont = 0;
    Builder builder = new Builder();
    Sistemas sistemas = new Sistemas();
    Claim claim = new Claim();
    Regioes regioes = new Regioes();

    public ClaimPrompt(Player player, String pontos, String Uid) {
        this.player = player;
        this.pontos = pontos;
        this.Uid = Uid;

    }

    // CLAIM ADD
    public Prompt Add = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Nome do local: (Nome da cidade,Bairro ou Ponto de referencia)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            // Gera o ID da região
            String id;
            UUID idp = player.getUniqueId();
            Integer cont = 0;
            do {
                cont++;
                id = Normalizer.normalize(input, Normalizer.Form.NFD);
                id = id.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                id = id.replaceAll(" ", "") + cont.toString();
                id = id.replaceAll("[\\[\\](){}]", "");
                id = id.toLowerCase();
            } while (claim.getArea(id) != null);
            // Adiciona na lista
            claim.addArea(input, id, pontos, idp.toString());
            regioes.AddClaimGuard(pontos, id, player);
            // Avisos
            context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas1 + ChatColor.GREEN + id);
            context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas2);
            return END_OF_CONVERSATION;
        }
    };

    // CLAIM REMOVER
    public Prompt remover = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim para ser abandonado:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (player.hasPermission("btebrasil.adm")) {
                Areas A = claim.getArea(input);
                regioes.RemoveClaim(A, player);
                context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ClaimRemoved);
                return END_OF_CONVERSATION;
            }
            if (VefClaim(player, context, input, false) == true) {
                Areas A = claim.getArea(input);
                regioes.RemoveClaim(A, player);
                context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ClaimRemoved);
                return END_OF_CONVERSATION;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM COMPLETO
    public Prompt completoStart = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim para ser completado:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {

            if (VefClaim(player, context, input, false) == true) {
                A = claim.getArea(input);
                // Verifica se já não foi enviado
                if (claim.getPendenteClaim(input) == null) {
                    return Completo;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AnalisePend);
                    return END_OF_CONVERSATION;
                }
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM COMPLETO 2
    Prompt Completo = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Quantas construções aproximadamente você fez dentro da sua área?";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                Builds = input;
                if (A.getParticipantes().equals("nulo")) {
                    Finalizar(player, A, Builds);
                    context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ClaimCompleto);
                    return END_OF_CONVERSATION;
                } else {
                    participante = A.getParticipantes().split(",");
                    size = participante.length;
                    Amigo = Bukkit.getOfflinePlayer(UUID.fromString(participante[cont]));
                    return BuildsPlayers;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return Completo;
            }
        }
    };
    // CLAIM COMPLETO PARTICIPANTES
    Prompt BuildsPlayers = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Quantas construções aproximadamente " + Amigo.getName()
                    + " fez dentro da sua área?" + ChatColor.RED
                    + " (Mentir sobre a quantitade de construções dos participantes resultará na perda dos seus pontos)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                Integer num = Integer.parseInt(input);
                // Verifica se num é maior q 0
                if (num >= 0) {
                    Builds += "," + input;
                    cont++;
                    if (cont < size) {
                        Amigo = Bukkit.getOfflinePlayer(UUID.fromString(participante[cont]));
                        return BuildsPlayers;
                    } else {
                        Finalizar(player, A, Builds);
                        context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ClaimCompleto);
                        return END_OF_CONVERSATION;
                    }
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MaiorZero);
                    return BuildsPlayers;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return BuildsPlayers;
            }
        }
    };

    // FINALIZAR COMPLETO
    private void Finalizar(Player player, Areas A, String builds) {
        String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        claim.addPendenteClaim(A.getClaim(), builds);
        // Regioes.CompleteClaim(A.getClaim(), player, A.getParticipantes()); Remove as
        // permissões
        DiscordPonte.AnalisarClaim(A.getClaim(), discordId);
    }

    // CLAIM EQUIPE ADD
    public Prompt equipe = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você quer adicionar um participante:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (VefClaim(player, context, input, false) == true) {
                return equipe2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM EQUIPE ADD 2
    Prompt equipe2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Nick do jogador que você quer adicionar na equipe:" + ChatColor.RED
                    + " (O jogador deve estar Online)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            Player convite = Bukkit.getPlayer(input);
            // Se convite não for nulo
            if (convite != null) {
                String id = convite.getUniqueId().toString();
                String idp = player.getUniqueId().toString();
                String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(convite.getUniqueId());
                if (!idp.equals(id)) {
                    // Verifica se o participante é construtor
                    if (sistemas.VerificarBuilder(convite) == true) {
                        builder.addBuilder(id.toString(), discordId);
                        String pp = A.getParticipantes();
                        if (pp.equals("nulo")) {
                            claim.setParticipante(A.getClaim(), id);
                            regioes.addPermissaoWG(A.getClaim(), convite, convite.getUniqueId());
                            context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.Equipe1);
                            return END_OF_CONVERSATION;
                        } else {
                            participante = pp.split(",");
                            if (!Arrays.stream(participante).anyMatch(id::equals)) {
                                claim.setParticipante(A.getClaim(), id);
                                regioes.addPermissaoWG(A.getClaim(), convite, convite.getUniqueId());
                                context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.Equipe1);
                                return END_OF_CONVERSATION;
                            } else {
                                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.Equipe2);
                                return END_OF_CONVERSATION;
                            }
                        }
                    } else {
                        context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.EquipeNotBuilder);
                        return END_OF_CONVERSATION;
                    }
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MuyMaloEasterEgg);
                    return END_OF_CONVERSATION;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.PlayerOn);
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM EQUIPE REMOVER
    public Prompt equipeRemover = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você quer remover um participante:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (VefClaim(player, context, input, false) == true) {
                return equipeRemover2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM EQUIPE REMOVER 2
    Prompt equipeRemover2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Nick do jogador que você quer remover da equipe:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            OfflinePlayer name = Bukkit.getOfflinePlayer(input);
            OfflinePlayer convite = Bukkit.getOfflinePlayer(name.getUniqueId());
            // Se convite não for nulo
            if (convite != null) {
                String id = convite.getUniqueId().toString();
                String idp = player.getUniqueId().toString();
                if (!idp.equals(id)) {
                    participante = A.getParticipantes().split(",");
                    if (!Arrays.stream(participante).anyMatch(id::equals)) {
                        context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.EquipeNotFound);
                        return END_OF_CONVERSATION;
                    } else {
                        claim.unsetParticipante(A.getClaim(), id);
                        regioes.removePermissaoWG(A.getClaim(), player, convite.getUniqueId());
                        context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.EquipeRemove);
                        return END_OF_CONVERSATION;
                    }
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MuyMaloEasterEgg);
                    return END_OF_CONVERSATION;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.PlayerNotFound);
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM SAIR
    public Prompt sair = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você deseja abandonar:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (VefClaim(player, context, input, false) == true) {
                A = claim.getArea(input);
                UUID id = player.getUniqueId();
                String[] Parts = A.getParticipantes().split(",");
                for (int i = 0; i < Parts.length; i++) {
                    if (Parts[i].equals(id.toString())) {
                        claim.unsetParticipante(A.getClaim(), id.toString());
                        regioes.removePermissaoWG(A.getClaim(), player, id);
                        context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.RemovidoEquipe);
                        return END_OF_CONVERSATION;
                    }
                }
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.EquipeNotFound2);
                return END_OF_CONVERSATION;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM EDITAR
    public Prompt editar = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você deseja editar:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (VefClaim(player, context, input, true) == true) {
                A = claim.getArea(input);
                if (A.getStatus().equals("T")) {
                    claim.EditarArea(input);
                    regioes.addPermissaoWG(input, player, player.getUniqueId());
                    if (!A.getParticipantes().equals("nulo")) {
                        String[] Parts = A.getParticipantes().split(",");
                        for (int i = 0; i < Parts.length; i++) {
                            regioes.addPermissaoWG(input, player, UUID.fromString(Parts[i]));
                        }
                    }
                    DiscordPonte.EditarMsg(A);
                    context.getForWhom()
                            .sendRawMessage(ChatColor.GREEN + Mensagens.OClaim + input + Mensagens.ClaimEditar);
                    return END_OF_CONVERSATION;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.NotFinalizada);
                    return END_OF_CONVERSATION;
                }
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM IMG
    public Prompt img = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você deseja adicionar uma imagem:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (VefClaim(player, context, input, true) == true) {
                A = claim.getArea(input);
                return img2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    public Prompt img2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Link da imagem: (Faça o upload neste site:" + ChatColor.BLUE + "https://imgur.com/"
                    + ChatColor.RED + " E utilize o link direto!" + ChatColor.WHITE + ")";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            try {
                URL url = new URL(input);
                claim.setImg(A.getClaim(), input);
                DiscordPonte.ImgAdicionada(A, input);
                context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ImgAdd);
                return END_OF_CONVERSATION;
            } catch (MalformedURLException e) {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.LinkImg);
                return END_OF_CONVERSATION;
            }
        }
    };

    // CLAIM IMG REMOVER
    public Prompt imgremover = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você deseja remover uma imagem:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (player.hasPermission("btebrasil.lider")) {
                A = claim.getArea(input);
                return imgremover2;
            }
            if (VefClaim(player, context, input, true) == true) {
                A = claim.getArea(input);
                return imgremover2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    Prompt imgremover2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Link da imagem que você quer remover:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            try {
                URL url = new URL(input);

                String[] imgs = A.getImgs().split(",");
                if (!Arrays.stream(imgs).anyMatch(input::equals)) {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.ImgNotFind);
                    return END_OF_CONVERSATION;
                } else {
                    claim.unsetImg(A.getClaim(), input);
                    context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ImgRemovida);
                    return END_OF_CONVERSATION;
                }
            } catch (MalformedURLException e) {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.LinkImg);
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM EDITAR NOME
    public Prompt nome = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "ID do claim que você quer editar o nome:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (player.hasPermission("btebrasil.adm")) {
                A = claim.getArea(input);
                return nome2;
            } else if (VefClaim(player, context, input, true) == true) {
                A = claim.getArea(input);
                return nome2;
            } else {
                return END_OF_CONVERSATION;
            }
        }
    };
    // CLAIM EDITAR NOME 2
    Prompt nome2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Novo nome para este Claim:";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            claim.setNome(A.getClaim(), input);
            context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.NomeClaim);
            return END_OF_CONVERSATION;
        }
    };

    // ADD COMPLETO (ADMINS ONLY)
    public Prompt addcompleto = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Nome do local: (Nome da cidade,Bairro ou Ponto de referencia)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            // Gera o ID da região
            String id;
            Integer cont = 0;
            do {
                cont++;
                id = Normalizer.normalize(input, Normalizer.Form.NFD);
                id = id.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                id = id.replaceAll(" ", "") + cont.toString();
                id = id.replaceAll("[\\[\\](){}]", "");
                id = id.toLowerCase();
            } while (claim.getArea(id) != null);
            // Adiciona na lista
            claim.addArea(input, id, pontos, Uid);
            regioes.AddClaimGuard(pontos, id, player);
            Claimid = id;
            return addcompleto3;
        }
    };
    // ADD COMPLETO (ADMINS ONLY) 2
    public Prompt addcompleto2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD
                    + "Digite o UUID de algum participante deste claim: (Digite 0 para parar) (Site:" + ChatColor.BLUE
                    + "https://mcuuid.net/)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (input.equals("0")) {
                claim.setBuilds(Claimid, Integer.parseInt(Builds));
                claim.CompletarArea(Claimid);
                // Avisos
                context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas1 + ChatColor.GREEN + Claimid);
                context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas2);
                return END_OF_CONVERSATION;
            } else if (UUID.fromString(input) != null) {
                A = claim.getArea(Claimid);
                participante = A.getParticipantes().split(",");
                if (!Arrays.stream(participante).anyMatch(Claimid::equals)) {
                    builder.addBuilder(input, "nulo");
                    claim.setParticipante(A.getClaim(), input);
                    Uid = input;
                    context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.Equipe1);
                    return addcompleto3;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.Equipe2);
                    return addcompleto2;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.InfUUID2);
                return addcompleto2;
            }
        }
    };

    // ADD COMPLETO (ADMINS ONLY) 3
    public Prompt addcompleto3 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD
                    + "Digite a quantidade de builds que este jogador fez neste claim: (Se não souber digite 0)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                if (Builds == null) {
                    Builds = input;
                } else {
                    Integer total = Integer.parseInt(input) + Integer.parseInt(Builds);
                    Builds = total.toString();
                }
                builder.setPontos(Uid, Integer.parseInt(input));
                builder.setBuildsBuilder(Uid, Integer.parseInt(input));
                return addcompleto2;
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return addcompleto3;
            }
        }
    };

    // ADD CONSTRUCAO (ADMINS ONLY)
    public Prompt addconstrucao = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Nome do local: (Nome da cidade,Bairro ou Ponto de referencia)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            // Gera o ID da região
            String id;
            Integer cont = 0;
            do {
                cont++;
                id = Normalizer.normalize(input, Normalizer.Form.NFD);
                id = id.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                id = id.replaceAll(" ", "") + cont.toString();
                id = id.replaceAll("[\\[\\](){}]", "");
                id = id.toLowerCase();
            } while (claim.getArea(id) != null);
            // Adiciona na lista
            claim.addArea(input, id, pontos, Uid);
            regioes.AddClaimGuard(pontos, id, player);
            Claimid = id;
            return addconstrucao2;
        }
    };
    // ADD CONSTRUCAO (ADMINS ONLY) 2
    public Prompt addconstrucao2 = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD
                    + "Digite o UUID de algum participante deste claim: (Digite 0 para parar) (Site:" + ChatColor.BLUE
                    + "https://mcuuid.net/)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (input.equals("0")) {
                // Avisos
                context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas1 + ChatColor.GREEN + Claimid);
                context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.AddAreas2);
                return END_OF_CONVERSATION;
            } else if (UUID.fromString(input) != null) {
                A = claim.getArea(Claimid);
                participante = A.getParticipantes().split(",");
                if (!Arrays.stream(participante).anyMatch(Claimid::equals)) {
                    builder.addBuilder(input, "nulo");
                    claim.setParticipante(A.getClaim(), input);
                    Uid = input;
                    context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.Equipe1);
                    return addconstrucao2;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.Equipe2);
                    return addconstrucao2;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.InfUUID2);
                return addconstrucao2;
            }
        }
    };

    private boolean VefClaim(Player player, ConversationContext context, String input, Boolean edit) {
        A = claim.getArea(input);
        UUID id = player.getUniqueId();
        // Se o Claim existir
        if (A != null) {
            // Se area for do Player
            if (A.getPlayer().equals(id.toString())) {
                // Verifica se já foi completa
                if (A.getStatus().equals("F") || edit == true) {
                    return true;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.GOLD + Mensagens.ClaimCompleto2);
                    return false;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.ClaimPlayer);
                return false;
            }

        } else {
            context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.ClaimNotFound);
            return false;
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
