package io.github.LucasMullerC.BTEBrasilSystem;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.LucasMullerC.listeners.PlayerMoveListener;

public class BTEBrasilSystem extends JavaPlugin {
	private static BTEBrasilSystem instance;
	//private DiscordSrvListener discordsrvListener = new DiscordSrvListener(this);

	@Override
	public void onEnable() {
		instance = this;
		/*
		// Comandos
		getCommand("aplicacao").setExecutor(new aplicacao());
		getCommand("anterior").setExecutor(new anterior());
		getCommand("proximo").setExecutor(new proximo());
		getCommand("continuar").setExecutor(new continuar());
		getCommand("time").setExecutor(new time());
		getCommand("completo").setExecutor(new completo());
		getCommand("tag").setExecutor(new tag());
		getCommand("analisar").setExecutor(new analisar());
		getCommand("cancelar").setExecutor(new cancelar());
		getCommand("salvar").setExecutor(new salvar());
		getCommand("link").setExecutor(new link());
		getCommand("deslink").setExecutor(new deslink());
		getCommand("perfil").setExecutor(new perfil());
		getCommand("claim").setExecutor(new claim());
		getCommand("status").setExecutor(new status());
		// Inicializa Listener
		DiscordSRV.api.subscribe(discordsrvListener);
		*/
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		//getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

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
