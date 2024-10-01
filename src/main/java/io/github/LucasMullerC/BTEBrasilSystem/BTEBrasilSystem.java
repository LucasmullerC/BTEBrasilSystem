package io.github.LucasMullerC.BTEBrasilSystem;

import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.commands.application;
import io.github.LucasMullerC.commands.back;
import io.github.LucasMullerC.commands.cancel;
import io.github.LucasMullerC.commands.completed;
import io.github.LucasMullerC.commands.map;
import io.github.LucasMullerC.commands.next;
import io.github.LucasMullerC.commands.previous;
import io.github.LucasMullerC.discord.DiscordSrvListener;
import io.github.LucasMullerC.listeners.PlayerJoinListener;
import io.github.LucasMullerC.listeners.PlayerMoveListener;

public class BTEBrasilSystem extends JavaPlugin {
	private static BTEBrasilSystem instance;
	private DiscordSrvListener discordsrvListener = new DiscordSrvListener(this);

	@Override
	public void onEnable() {
		instance = this;
		// Comandos
		getCommand("aplicacao").setExecutor(new application());
		getCommand("cancelar").setExecutor(new cancel());
		getCommand("proximo").setExecutor(new next());
		getCommand("anterior").setExecutor(new previous());
		getCommand("continuar").setExecutor(new back());
		getCommand("completo").setExecutor(new completed());
		getCommand("map").setExecutor(new map());
		/*
		getCommand("time").setExecutor(new time());
		getCommand("tag").setExecutor(new tag());
		getCommand("analisar").setExecutor(new analisar());
		getCommand("salvar").setExecutor(new salvar());
		getCommand("link").setExecutor(new link());
		getCommand("deslink").setExecutor(new deslink());
		getCommand("perfil").setExecutor(new perfil());
		getCommand("claim").setExecutor(new claim());
		getCommand("status").setExecutor(new status());
		*/
		// Inicializa Listener
		DiscordSRV.api.subscribe(discordsrvListener);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

		// Inicializou sem problemas.
		getLogger().info("BTEBrasilSystem Ativado! | BTEBrasilSystem Activated!");
	}

	@Override
	public void onDisable() {
		//DiscordSRV.api.unsubscribe(discordsrvListener);
		getLogger().info("BTEBrasilSystem Desativado! | BTEBrasilSystem Deactivated!");
	}

	public static BTEBrasilSystem getPlugin() {
		return instance;
	}
}
