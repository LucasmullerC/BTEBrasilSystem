package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.BTEBrasilSystem.DiscordPonte;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Util.Mensagens;

public class completo implements CommandExecutor {
    Aplicantes A;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        UUID id = player.getUniqueId();
        A = GerenciarListas.getAplicante(id.toString());
        if (GerenciarListas.getPendentebyName(A.getUUID()) == null) {
            if (A.getSeccao().equals("d")) {
                GerenciarListas.addPendente(id.toString());
                DiscordPonte.AnalisarReserva(A.getTime());
                Sistemas.RemovePermissao(player, A.getZona());
                player.sendMessage(ChatColor.GOLD + Mensagens.AppEnviada);
                return true;
            } else {
                player.sendMessage(ChatColor.YELLOW + Mensagens.NotCompleto);
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + Mensagens.AnalisePend);
            return true;
        }
    }

}
