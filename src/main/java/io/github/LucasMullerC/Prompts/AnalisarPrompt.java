package io.github.LucasMullerC.Prompts;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.Regioes;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Gerencia.Builder;
import io.github.LucasMullerC.Gerencia.Claim;
import io.github.LucasMullerC.Objetos.Areas;
import io.github.LucasMullerC.Objetos.Pendentes;
import io.github.LucasMullerC.Util.Mensagens;

public class AnalisarPrompt {
    Areas A;
    Pendentes Ap;
    OfflinePlayer player, Amigo;
    World w;
    double Pontos = 0, total = 0;
    int cont = 0, size;
    String[] build, participante;

    public AnalisarPrompt(Areas a, OfflinePlayer player, Pendentes ap, World W) {
        A = a;
        Ap = ap;
        build = Ap.getBuilds().split(",");
        size = build.length;
        participante = A.getParticipantes().split(",");
        this.player = player;
        this.w = W;
    }

    public Prompt Builds = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            if (cont == 0) {
                return ChatColor.BOLD + player.getName() + " fez " + build[cont] + " construções dentro dessa área."
                        + ChatColor.RED + "Essa informação está correta? (Se sim digite a mesma quantidade)";
            } else {
                Amigo = Bukkit.getOfflinePlayer(UUID.fromString(participante[cont - 1]));
                return ChatColor.BOLD + Amigo.getName() + " fez " + build[cont] + " construções dentro dessa área."
                        + ChatColor.RED + "Essa informação está correta? (Se sim digite a mesma quantidade)";
            }
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                Integer num = Integer.parseInt(input);
                // Verifica se num é maior ou igual q 0
                if (num >= 0) {
                    build[cont] = input;
                    cont++;
                    if (cont < size) {
                        return Builds;
                    } else {
                        cont = 0;
                        return Especial;
                    }
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MaiorZero);
                    return Builds;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return Builds;
            }
        }
    };

    Prompt Especial = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD
                    + "Quantas Construções muito complexas, Marcos ou Pontos Turisticos foram feitas nesta área?";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                Integer num = Integer.parseInt(input);

                // Verifica se num é maior q 0
                if (num > 0) {
                    Pontos = (num * 5) + Pontos; // Complexas valem 5 pontos
                    return Pesos;
                } else {
                    return Pesos;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return Especial;
            }
        }
    };

    Prompt Pesos = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "Qual peso você dá para este claim?" + ChatColor.RED
                    + " (Menores pesos = Mais pontos)";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (isNumeric(input) == true) {
                Integer num = Integer.parseInt(input);
                // Verifica se num é maior q 0
                if (num >= 2) {
                    Regioes regioes = new Regioes();
                    total = regioes.getDistancia(A.getClaim(), w, num);
                    return Confirmar;
                } else {
                    context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MaiorDois);
                    return Pesos;
                }
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.MustBeNumber);
                return Pesos;
            }
        }
    };
    Prompt Confirmar = new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.BOLD + "De acordo com seu peso serão ganhos " + ChatColor.BLUE + String.valueOf(total)
                    + " pontos." + ChatColor.RED + " VOCÊ TEM CERTEZA? 'S' - SIM / 'N' - NÃO";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            if (input.equalsIgnoreCase("S")) {
                Builder builder = new Builder();
                Sistemas sistemas = new Sistemas();
                Claim claim = new Claim();
                UUID id = player.getUniqueId();
                Pontos = total + Pontos;
                int totalbuilds = 0;
                for (int i = 0; i < size; i++) {
                    String discordId = "";
                    double ptotal = Integer.parseInt(build[i]) + Pontos;
                    if (i == 0) {
                        discordId = builder.getBuilder(id.toString()).getDiscord();
                        builder.setBuildsBuilder(A.getPlayer(), Integer.parseInt(build[i]));
                        builder.setPontos(A.getPlayer(), ptotal);
                        sistemas.CheckRank(id.toString());
                        sistemas.CheckAwardBuilds(id.toString());
                    } else {
                        discordId = builder.getBuilder(participante[i - 1]).getDiscord();
                        builder.setBuildsBuilder(participante[i - 1], Integer.parseInt(build[i]));
                        builder.setPontos(participante[i - 1], ptotal);
                        sistemas.CheckRank(participante[i - 1]);
                        sistemas.CheckAwardBuilds(participante[i - 1]);
                    }
                    totalbuilds = Integer.parseInt(build[i]) + totalbuilds;
                    if (!discordId.equals("nulo")) {
                        DiscordPonte.sendMessage(discordId,
                                Mensagens.ClaimAprovadoDiscord1 + "**" + A.getClaim() + "**"
                                        + Mensagens.ClaimAprovadoDiscord2 + Mensagens.Seta + build[i]
                                        + Mensagens.ClaimAprovadoDiscord3 + Mensagens.Seta + String.valueOf(ptotal)
                                        + Mensagens.ClaimAprovadoDiscord4);
                    }
                }
                claim.setBuilds(A.getClaim(), totalbuilds);
                claim.CompletarArea(A.getClaim());
                claim.RemoverPendenteClaim(A.getClaim());
                context.getForWhom().sendRawMessage(ChatColor.GREEN + Mensagens.ClaimAprovado);
                return END_OF_CONVERSATION;
            } else if (input.equalsIgnoreCase("N")) {
                return Pesos;
            } else {
                context.getForWhom().sendRawMessage(ChatColor.RED + Mensagens.Certeza);
                return Confirmar;
            }
        }
    };

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
