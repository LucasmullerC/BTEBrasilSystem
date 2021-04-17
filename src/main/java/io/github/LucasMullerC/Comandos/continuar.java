package io.github.LucasMullerC.Comandos;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Aplicantes;

public class continuar implements CommandExecutor {
	Aplicantes A;
	BTEBrasilSystem plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("continuar")) {
			Player player = (Player) sender;
			UUID id = player.getUniqueId();
			if (Sistemas.getAplicante(id.toString()) == null) {
				player.sendMessage(ChatColor.GOLD + "Você não tem nenhuma aplicação em andamento.");
				return true;
			} else {
				A = Sistemas.getAplicante(id.toString());
				player.setGameMode(GameMode.CREATIVE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
				player.getInventory().addItem(new ItemStack(Material.WOOD_AXE, 1));
				player.sendMessage(ChatColor.GOLD + "Continuando aplicação! Boa sorte!");
				player.sendMessage(ChatColor.GOLD + "Lembre-se de seguir as instruções dos videos!");
				World w = player.getWorld();
				if (A.getSeccao().equals("a")) {
					Location l = Sistemas.getZona(A.getZona()).getla();
			        player.teleport(l);
					return true;
				} else if (A.getSeccao().equals("b")) {
					Location l = Sistemas.getZona(A.getZona()).getlb();
			        player.teleport(l);
					return true;
				} else if (A.getSeccao().equals("c")) {
					Location l = Sistemas.getZona(A.getZona()).getlc();
			        player.teleport(l);
					return true;
				} else if (A.getSeccao().equals("d")) {
					Location l = Sistemas.getZona(A.getZona()).getld();
			        player.teleport(l);
					return true;
				}
			}

		}
		return false;
	}

}
