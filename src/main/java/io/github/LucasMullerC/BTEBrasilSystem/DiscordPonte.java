package io.github.LucasMullerC.BTEBrasilSystem;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DiscordPonte {
	static JDA bot;
	static TextChannel chatne, chatsp, chatmg, chatco, chatsul, chatrj;

	public DiscordPonte(JDA bot) {
		this.bot = bot;
		chatne = bot.getTextChannelById("809900341436350484"); // mudar IDs!!!
		chatsp = bot.getTextChannelById("701909180054765609");
		chatsul = bot.getTextChannelById("782698775356375090");
		chatco = bot.getTextChannelById("796466946001993749");
		chatmg = bot.getTextChannelById("812707639200055327");
		chatrj = bot.getTextChannelById("781654144108331038");
	}

	public static String CheckDiscord(String nome) {
		Guild guild = bot.getGuildById("715528474655326238");
		User Builder = bot.getUserByTag(nome);
		if (Builder != null) {
			if (Builder.getAsTag().equals(nome)) {
				Member member = guild.getMemberById(Builder.getId());
				if (member == null) {
					return "nulo";
				} else {
					return Builder.getId();
				}
			} else {
				return "nulo";
			}
		} else {
			return "nulo";
		}
	}
	public static String CheckDiscordSudeste(String nome,String Server) {
		Guild guild = bot.getGuildById(Server); //ID do discord do time
		User Builder = bot.getUserByTag(nome);
		if (Builder != null) {
			if (Builder.getAsTag().equals(nome)) {
				Member member = guild.getMemberById(Builder.getId());
				if (member == null) {
					return "nulo";
				} else {
					return Builder.getId();
				}
			} else {
				return "nulo";
			}
		} else {
			return "nulo";
		}
	}
	public static Boolean getCargoSudeste(final String id,String Server,String Cargo) {  //sudeste
		final Guild guild = DiscordPonte.bot.getGuildById(Server);  //id do discord 
		final Role role = guild.getRoleById(Cargo);                //cargo
		final Member member = guild.getMemberById(id);
		if (member != null) {
			if (member.getRoles().contains(role)) {
				return true;
			}
			return false;
		}
		return false;
	}
	public static void addCargo(String UUID, String time, String Discord) {
		String Tbot = null;
		Guild guild = bot.getGuildById("715528474655326238"); // BTE BR/PT
		Role role = guild.getRoleById("721861941517353021"); // Construtor br/pt
		Member member = guild.getMemberById(Discord);
		guild.addRoleToMember(member, role).queue(); // add BR/PT
		if (time.equals("ne")) {
			Guild guildT = bot.getGuildById("735561960254341121");
			Role roleT = guildT.getRoleById("736591730748686457");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "780733281938571265";
		} else if (time.equals("sp")) {
			Guild guildT = bot.getGuildById("695813656490803223");
			Role roleT = guildT.getRoleById("696887529944645716");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "792809431674781696";
		} else if (time.equals("sul")) {
			Guild guildT = bot.getGuildById("782652814831779850");
			Role roleT = guildT.getRoleById("782653956157472820");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "794050690686255124";
		} else if (time.equals("co")) {
			Guild guildT = bot.getGuildById("796238091207180338");
			Role roleT = guildT.getRoleById("796465391894790154");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "nulo";
		} else if (time.equals("mg")) {
			Guild guildT = bot.getGuildById("701801798154846298");
			Role roleT = guildT.getRoleById("701816986081951895");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "nulo";
		} else if (time.equals("es")) {
			Guild guildT = bot.getGuildById("801618489600901161");
			Role roleT = guildT.getRoleById("801623044812177418");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "nulo";
		}else if (time.equals("rj")) {
			Guild guildT = bot.getGuildById("738444755330924625");
			Role roleT = guildT.getRoleById("776573196097814551");
			Member memberT = guildT.getMemberById(Discord);
			guildT.addRoleToMember(memberT, roleT).queue();
			Tbot = "nulo";
		}
	}

	public static void removerCargo(String UUID, String time, String Discord) {
		String Tbot = null;
		Guild guild = bot.getGuildById("715528474655326238"); // BTE BR/PT
		Role role = guild.getRoleById("721861941517353021"); // Construtor br/pt
		Member member = guild.getMemberById(Discord);
		guild.removeRoleFromMember(member, role).queue(); // add BR/PT
		if (time.equals("ne")) {
			Guild guildT = bot.getGuildById("735561960254341121");
			Role roleT = guildT.getRoleById("736591730748686457");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "780733281938571265";
		} else if (time.equals("sp")) {
			Guild guildT = bot.getGuildById("695813656490803223");
			Role roleT = guildT.getRoleById("696887529944645716");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "792809431674781696";
		} else if (time.equals("sul")) {
			Guild guildT = bot.getGuildById("782652814831779850");
			Role roleT = guildT.getRoleById("782653956157472820");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "794050690686255124";
		} else if (time.equals("co")) {
			Guild guildT = bot.getGuildById("796238091207180338");
			Role roleT = guildT.getRoleById("796465391894790154");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "nulo";
		} else if (time.equals("mg")) {
			Guild guildT = bot.getGuildById("701801798154846298");
			Role roleT = guildT.getRoleById("701816986081951895");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "nulo";
		}else if (time.equals("es")) {
			Guild guildT = bot.getGuildById("801618489600901161");
			Role roleT = guildT.getRoleById("801625498026377226");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "nulo";
		}else if (time.equals("rj")) {
			Guild guildT = bot.getGuildById("738444755330924625");
			Role roleT = guildT.getRoleById("776573196097814551");
			Member memberT = guildT.getMemberById(Discord);
			guildT.removeRoleFromMember(memberT, roleT).queue();
			Tbot = "nulo";
		}
	}

	public static void sendMessage(String id, String content) { // msg no privado
		User user = bot.getUserById(id);
		user.openPrivateChannel().flatMap(channel -> channel.sendMessage(content)).queue();
	}

	public static void AnalisarReserva(String time) {
		if (time.equals("ne")) {
			chatne.sendMessage(
					"<@&812638293061271582> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		} else if (time.equals("sp")) {
			chatsp.sendMessage(
					"<@&725444191445975050> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		} else if (time.equals("sul")) {
			chatsul.sendMessage(
					"<@&782653746747539506> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		} else if (time.equals("co")) {
			chatco.sendMessage(
					"<@&796455369777872937> Geral Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		} else if (time.equals("mg")) {
			chatmg.sendMessage(
					"<@&812707020993462293> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		}else if (time.equals("es")) {
			chatmg.sendMessage(
					"<@&801622384460955679> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
					.queue();
		}
	else if (time.equals("rj")) {
		chatrj.sendMessage(
				"<@&822515371528814632> Existem aplicações para serem analisadas, Se estiver disponivel entre na network e digite **/analisar**")
				.queue();
	}
	}
}
