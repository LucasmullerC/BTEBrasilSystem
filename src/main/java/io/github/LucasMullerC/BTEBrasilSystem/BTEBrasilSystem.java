package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.commands.PluginSlashCommand;
import github.scarsz.discordsrv.api.commands.SlashCommand;
import github.scarsz.discordsrv.api.commands.SlashCommandProvider;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.events.interaction.SlashCommandEvent;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.OptionType;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.CommandData;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.commands.build.SubcommandData;
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
import io.github.LucasMullerC.discord.commands.AwardActions;
import io.github.LucasMullerC.discord.commands.BuildsActions;
import io.github.LucasMullerC.discord.commands.ClaimActions;
import io.github.LucasMullerC.discord.commands.Conquistas;
import io.github.LucasMullerC.discord.commands.PointsActions;
import io.github.LucasMullerC.discord.commands.Ranks;
import io.github.LucasMullerC.discord.commands.CreateAward;
import io.github.LucasMullerC.discord.commands.Destacar;
import io.github.LucasMullerC.discord.commands.DiscordClaim;
import io.github.LucasMullerC.discord.commands.DiscordProfile;
import io.github.LucasMullerC.discord.commands.FindColor;
import io.github.LucasMullerC.discord.commands.Leaderboard;
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
				//perfil
				new PluginSlashCommand(this, new CommandData("perfil", MessageUtils.getMessagePT("slashprofile"))
				.addOption(OptionType.MENTIONABLE, "mention", MessageUtils.getMessagePT("profiledescription2"), false)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("profiledescription3"), false)),

				//destacar
				new PluginSlashCommand(this, new CommandData("destacar", MessageUtils.getMessagePT("slashdestacar"))
				.addOption(OptionType.STRING, "awardid", MessageUtils.getMessagePT("slashcreateawarddescription1"), true)),

				//conquistas
				new PluginSlashCommand(this, new CommandData("conquistas", MessageUtils.getMessagePT("slashawards"))
				.addOption(OptionType.NUMBER, "page", MessageUtils.getMessagePT("slashawardsdescription1"), false)
				.addOption(OptionType.MENTIONABLE, "mention", MessageUtils.getMessagePT("awardsdescription2"), false)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("awardsdescription3"), false)),

				//claim
				new PluginSlashCommand(this, new CommandData("claim", MessageUtils.getMessagePT("slashclaim"))
				.addOption(OptionType.NUMBER, "page", MessageUtils.getMessagePT("slashawardsdescription1"), false)
				.addOption(OptionType.STRING, "claimid", MessageUtils.getMessagePT("claimdescription4"), false)
				.addOption(OptionType.MENTIONABLE, "mention", MessageUtils.getMessagePT("claimdescription2"), false)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("claimdescription3"), false)),

				//lb
				new PluginSlashCommand(this, new CommandData("lb", MessageUtils.getMessagePT("slashlb"))
				.addOption(OptionType.NUMBER, "page", MessageUtils.getMessagePT("slashawardsdescription1"), false)),

				//ranks
				new PluginSlashCommand(this, new CommandData("ranks", MessageUtils.getMessagePT("slashranks"))),
				
				//findcolor
				new PluginSlashCommand(this,new CommandData("findcolor", MessageUtils.getMessagePT("slashfindcolortitle"))
				.addSubcommands(new SubcommandData("image",MessageUtils.getMessagePT("slashfindcolorimagedescription")))
				.addSubcommands(new SubcommandData("code",MessageUtils.getMessagePT("slashfindcolorcodedescription"))
				.addOption(OptionType.STRING, "code", MessageUtils.getMessagePT("slashfindcolorcodedescription2"),true))),

				//ADMIN COMMANDS
				//buildactions
				new PluginSlashCommand(this, new CommandData("buildsactions", MessageUtils.getMessagePT("slashaddbuilds"))
				.addOption(OptionType.INTEGER, "builds", MessageUtils.getMessagePT("slashaddbuildsdescription"), true)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("slashaddbuildsdescription2"), true)
				.addOption(OptionType.BOOLEAN, "remover", MessageUtils.getMessagePT("slashaddbuildsdescription3"), true)),

				//pointsactions
				new PluginSlashCommand(this, new CommandData("pointsactions", MessageUtils.getMessagePT("slashaddpoints"))
				.addOption(OptionType.INTEGER, "pontos", MessageUtils.getMessagePT("slashaddpointsdescription"), true)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("slashaddbuildsdescription2"), true)
				.addOption(OptionType.BOOLEAN, "remover", MessageUtils.getMessagePT("slashaddpointsdescription2"), true)),

				//awardactions
				new PluginSlashCommand(this, new CommandData("awardactions", MessageUtils.getMessagePT("slashawardactions"))
				.addOption(OptionType.STRING, "awardid", MessageUtils.getMessagePT("slashcreateawarddescription1"), true)
				.addOption(OptionType.STRING, "userid", MessageUtils.getMessagePT("slashaddbuildsdescription2"), true)
				.addOption(OptionType.BOOLEAN, "remover", MessageUtils.getMessagePT("slashawardactionsdescription"), true)),

				//claimactions
				new PluginSlashCommand(this, new CommandData("claimactions", MessageUtils.getMessagePT("slashclaimactions"))
				.addOption(OptionType.STRING, "claimid", MessageUtils.getMessagePT("slashclaimactionsdescription"), true)
				.addOption(OptionType.BOOLEAN, "event", MessageUtils.getMessagePT("slashclaimactionsdescription2"), true)
				.addOption(OptionType.STRING, "awardid", MessageUtils.getMessagePT("slashcreateawarddescription1"), true)
				.addOption(OptionType.BOOLEAN, "remover", MessageUtils.getMessagePT("slashclaimactionsdescription3"), true)),

				//createaward
				new PluginSlashCommand(this, new CommandData("createaward", MessageUtils.getMessagePT("slashcreateaward"))
				.addOption(OptionType.INTEGER, "pontos", MessageUtils.getMessagePT("slashaddpointsdescription"), true)
				.addOption(OptionType.STRING, "awardid", MessageUtils.getMessagePT("slashcreateawarddescription1"), true)
				.addOption(OptionType.STRING, "url", MessageUtils.getMessagePT("slashcreateawarddescription2"), true)
				.addOption(OptionType.STRING, "nome", MessageUtils.getMessagePT("slashcreateawarddescription3"), true))
		));
	}
	@SlashCommand(path= "findcolor/code")
	@SlashCommand(path= "findcolor/image")
	public void findcolorCommand(SlashCommandEvent event){
		FindColor findColorCommand;
        try {
            findColorCommand = new FindColor(this);
			findColorCommand.onSlashCommandInteraction(event);
        } catch (URISyntaxException | IOException e) {
            System.out.println("Plugin starting stopped. FindColor command startup failed.");
			event.reply(MessageUtils.getMessagePT("ErrorGeneric")).queue();
        }
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

		event.replyEmbeds(messageEmbed).queue();
	}

	@SlashCommand(path = "destacar")
	public void destacarCommand(SlashCommandEvent event) {
		String awardID = event.getOption("awardid").getAsString();
		User user = event.getUser();

		Destacar destacar = new Destacar();
		String response = destacar.getCommand(user, awardID);
		event.reply(response).queue();
	}

	@SlashCommand(path = "conquistas")
	public void conquistasCommand(SlashCommandEvent event) {
		int page = 1;
		if(event.getOption("page") != null){
			page = (int)event.getOption("page").getAsDouble();
		}
		User mention = null;
		
		if(event.getOption("userid") != null){
			mention = DiscordUtil.getUserById(event.getOption("userid").getAsString());
		} else if(event.getOption("mention") != null){
			mention = event.getOption("mention").getAsUser();
		}

		Conquistas conquistas = new Conquistas();
		MessageEmbed messageEmbed =conquistas.getCommand(event.getUser(), page, mention);
		//Button awardButton = new ButtonImpl("allawards", "Todas as Conquistas", ButtonStyle.PRIMARY, false,null);
		//TODO: Botão de proxima página / anterior?
		event.replyEmbeds(messageEmbed).queue();
	}

	@SlashCommand(path = "claim")
	public void claimCommand(SlashCommandEvent event) {
		int page = 1;
		String claimid = null;
		User mention = null;
		if(event.getOption("page") != null){
			page = (int)event.getOption("page").getAsDouble();
		}
		if(event.getOption("claimid") != null){
			claimid = event.getOption("claimid").getAsString();
		}
		if(event.getOption("userid") != null){
			mention = DiscordUtil.getUserById(event.getOption("userid").getAsString());
		} else if(event.getOption("mention") != null){
			mention = event.getOption("mention").getAsUser();
		}

		DiscordClaim discordClaim = new DiscordClaim();
		MessageEmbed messageEmbed =discordClaim.getCommand(event.getUser(), page, mention,claimid);
		event.replyEmbeds(messageEmbed).queue();
	}

	@SlashCommand(path = "lb")
	public void lbCommand(SlashCommandEvent event) {
		int page = 1;
		if(event.getOption("page") != null){
			page = (int)event.getOption("page").getAsDouble();
		}

		Leaderboard leaderboard = new Leaderboard();
		MessageEmbed messageEmbed =leaderboard.getCommand(page);
		event.replyEmbeds(messageEmbed).queue();
	}

	@SlashCommand(path = "ranks")
	public void ranksCommand(SlashCommandEvent event) {
		Ranks ranks = new Ranks();
		MessageEmbed messageEmbed = ranks.getCommand();
		event.replyEmbeds(messageEmbed).queue();
	}

	//ADMIN COMMANDS
	@SlashCommand(path = "buildsactions")
	public void buildsactionsCommand(SlashCommandEvent event) {
		List<Role> roles = event.getMember().getRoles();
		int builds = Integer.parseInt(event.getOption("builds").getAsString());
		boolean remove = event.getOption("remover").getAsBoolean();

		BuildsActions addBuilds = new BuildsActions();
		String response = addBuilds.getCommand(roles, builds, event.getOption("userid").getAsString(),remove);
		event.reply(response).queue();
	}

	@SlashCommand(path = "pointsactions")
	public void pointsactionsCommand(SlashCommandEvent event) {
		List<Role> roles = event.getMember().getRoles();
		int points = Integer.parseInt(event.getOption("pontos").getAsString());
		boolean remove = event.getOption("remover").getAsBoolean();

		PointsActions addPoints = new PointsActions();
		String response = addPoints.getCommand(roles, points, event.getOption("userid").getAsString(),remove);
		event.reply(response).queue();
	}

	@SlashCommand(path = "awardactions")
	public void awardactionsCommand(SlashCommandEvent event) {
		List<Role> roles = event.getMember().getRoles();
		String awardID = event.getOption("awardid").getAsString();
		boolean remove = event.getOption("remover").getAsBoolean();

		AwardActions awardActions = new AwardActions();
		String response = awardActions.getCommand(roles, awardID, event.getOption("userid").getAsString(), remove);
		event.reply(response).queue();
	}

	@SlashCommand(path = "claimactions")
	public void claimactionsCommand(SlashCommandEvent event) {
		List<Role> roles = event.getMember().getRoles();
		String claimId = event.getOption("claimid").getAsString();
		String awardID = event.getOption("awardid").getAsString();
		boolean isEvent = event.getOption("event").getAsBoolean();
		boolean remove = event.getOption("remover").getAsBoolean();
		
		ClaimActions claimActions = new ClaimActions();
		String response = claimActions.getCommand(roles, awardID, claimId, isEvent, remove);
		event.reply(response).queue();
	}

	@SlashCommand(path = "createaward")
	public void createawardCommand(SlashCommandEvent event) {
		List<Role> roles = event.getMember().getRoles();
		int points = Integer.parseInt(event.getOption("pontos").getAsString());
		String awardID = event.getOption("awardid").getAsString();
		String url = event.getOption("url").getAsString();
		String name = event.getOption("nome").getAsString();

		CreateAward createAward = new CreateAward();
		String response = createAward.getCommand(roles, points, awardID, url, name);
		event.reply(response).queue();
	}
}
