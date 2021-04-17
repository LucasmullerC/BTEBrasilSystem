package io.github.LucasMullerC.BTEBrasilSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.LucasMullerC.Objetos.Jogadores;

public class Eventos implements Listener {
	Sistemas S;
    Jogadores G;
    BTEBrasilSystem plugin;
    public Eventos(BTEBrasilSystem plugin) {
        this.plugin = plugin;
    }
	@EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
		S.equipe.save();
		S.aplicante.save();
		S.pendente.save();
		S.jogador.save();
		S.zonas.save();
    	final Player p = event.getPlayer();
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
            	Cargos.gerenciarcargo(p);
            }
        }, 20L);
    }
	@EventHandler
	public void onQuitEvent(PlayerQuitEvent event) {
		S.equipe.save();
		S.aplicante.save();
		S.pendente.save();
		S.jogador.save();
		S.zonas.save();
	}

}
