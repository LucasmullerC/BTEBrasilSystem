package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.commands.PluginSlashCommand;
import github.scarsz.discordsrv.api.commands.SlashCommand;
import github.scarsz.discordsrv.api.commands.SlashCommandProvider;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.OptionType;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.Button;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.ButtonStyle;
import github.scarsz.discordsrv.dependencies.jda.internal.interactions.ButtonImpl;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.commands.analyse;
import io.github.LucasMullerC.commands.application;
import io.github.LucasMullerC.commands.back;
import io.github.LucasMullerC.commands.cancel;
import io.github.LucasMullerC.commands.claim;
import io.github.LucasMullerC.commands.completed;
import io.github.LucasMullerC.commands.map;
import io.github.LucasMullerC.commands.next;
import io.github.LucasMullerC.commands.previous;
import io.github.LucasMullerC.commands.profile;
import io.github.LucasMullerC.commands.status;
import io.github.LucasMullerC.commands.tag;
import io.github.LucasMullerC.commands.team;
import io.github.LucasMullerC.discord.DiscordSrvListener;
import io.github.LucasMullerC.discord.commands.DiscordProfile;
import io.github.LucasMullerC.listeners.PlayerJoinListener;
import io.github.LucasMullerC.listeners.PlayerMoveListener;
import io.github.LucasMullerC.util.MessageUtils;

public class BTEBrasilSystem extends JavaPlugin implements SlashCommandProvider{
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
		getCommand("claim").setExecutor(new claim());
		getCommand("time").setExecutor(new team());
		getCommand("tag").setExecutor(new tag());
		getCommand("analisar").setExecutor(new analyse());
		getCommand("perfil").setExecutor(new profile());
		getCommand("status").setExecutor(new status());

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

	@Override
    public Set<PluginSlashCommand> getSlashCommands() {
        return new HashSet<>(Arrays.asList(
                new PluginSlashCommand(this, new CommandData("perfil", MessageUtils.getMessageConsole("slashprofile"))
				.addOption(OptionType.MENTIONABLE, "mention", MessageUtils.getMessageConsole("profiledescription2"), false)
				.addOption(OptionType.INTEGER, "userid", MessageUtils.getMessageConsole("profiledescription3"), false))
        ));
    }

    @SlashCommand(path = "perfil")
    public void perfilCommand(SlashCommandEvent event) {
		User mention = null;

		if(event.getOption("userid") != null){
			mention = DiscordUtil.getUserById(event.getOption("userid").getAsString());
		} else if(event.getOption("mention") != null){
			mention = event.getOption("mention").getAsUser();
		}
		DiscordProfile discordProfile = new DiscordProfile();
		MessageEmbed messageEmbed = discordProfile.getCommand(event.getUser(), mention);
		Button button = new ButtonImpl("award", "Conquistas", ButtonStyle.SECONDARY, false,null);
		event.replyEmbeds(messageEmbed).addActionRow(button).queue();
    }
}
