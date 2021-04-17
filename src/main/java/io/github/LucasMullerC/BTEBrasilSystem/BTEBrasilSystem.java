package io.github.LucasMullerC.BTEBrasilSystem;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import io.github.LucasMullerC.Comandos.addequipe;
import io.github.LucasMullerC.Comandos.addplayer;
import io.github.LucasMullerC.Comandos.addzona;
import io.github.LucasMullerC.Comandos.analisar;
import io.github.LucasMullerC.Comandos.aplicar;
import io.github.LucasMullerC.Comandos.cancelar;
import io.github.LucasMullerC.Comandos.completo;
import io.github.LucasMullerC.Comandos.continuar;
import io.github.LucasMullerC.Comandos.id;
import io.github.LucasMullerC.Comandos.link;
import io.github.LucasMullerC.Comandos.perfil;
import io.github.LucasMullerC.Comandos.removerequipe;
import io.github.LucasMullerC.Comandos.removerplayer;
import io.github.LucasMullerC.Comandos.salvar;
import io.github.LucasMullerC.Comandos.tag;
import io.github.LucasMullerC.Comandos.time;
import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaJogadores;
import io.github.LucasMullerC.Util.ListaZonas;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public final class BTEBrasilSystem extends JavaPlugin {
	Sistemas S;
	private static BTEBrasilSystem instance;

	@Override
	public void onEnable() {
		instance = this;
		getCommand("id").setExecutor(new id());
		getCommand("aplicar").setExecutor(new aplicar());
		getCommand("continuar").setExecutor(new continuar());
		getCommand("completo").setExecutor(new completo());
		getCommand("addequipe").setExecutor(new addequipe());
		getCommand("removerplayer").setExecutor(new removerplayer());
		getCommand("removerequipe").setExecutor(new removerequipe());
		getCommand("addplayer").setExecutor(new addplayer());
		getCommand("analisar").setExecutor(new analisar());
		getCommand("cancelar").setExecutor(new cancelar());
		getCommand("link").setExecutor(new link());
		getCommand("time").setExecutor(new time());
		getCommand("perfil").setExecutor(new perfil());
		getCommand("salvar").setExecutor(new salvar());
		getCommand("addzona").setExecutor(new addzona());
		getCommand("tag").setExecutor(new tag());
		this.getServer().getPluginManager().registerEvents((Listener)new Eventos(this), (Plugin)this);
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		S.aplicante = new ListaAplicar(new File(pluginFolder + File.separator + "aplicantes.txt"));
		S.pendente = new ListaAplicar(new File(pluginFolder + File.separator + "pendente.txt"));
		S.jogador = new ListaJogadores(new File(pluginFolder + File.separator + "jogadores.txt"));
		S.zonas = new ListaZonas(new File(pluginFolder + File.separator + "zonas.txt"));
		S.equipe = new ListaJogadores(new File(pluginFolder + File.separator + "equipes.txt"));
		S.equipe.load();
		S.aplicante.load();
		S.pendente.load();
		S.jogador.load();
		S.zonas.load();
		try {
			JDA jda = JDABuilder.createDefault("ODAzMjY2OTc2Nzc5ODYyMDc3.YA7Sqw.NKJstUV-rCKsnhORitnERUuP4hU")
					.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking
															// for all guilds
					.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
			jda.awaitReady();
			System.out.println("Finished Building JDA!");
			DiscordPonte DiscordActs = new DiscordPonte(jda);

		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getLogger().info("BTEBrasilSystem Ativado!");
      /*  Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
            	if (LocalDate.now().getDayOfWeek().toString().equals("SUNDAY")) {
            		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "O vencedor da semana será anunciado em breve!");
            		
            	}
            }
        }, 20, 24000L); */
	/*  BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
           @Override
            public void run() {
        		LocalDate dia = LocalDate.now(); // x = 10
        		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        		String data = dia.format(formatter);
            	for (Aplicantes d : Sistemas.aplicante.getValues()) {
            		if (data.equals(d.getDeadLine())) {
            			Sistemas.DeadLine(d);
            		}
            	}
            }
        }, 0L, 24000L);  */
	}

	@Override
	public void onDisable() {
		S.equipe.save();
		S.aplicante.save();
		S.pendente.save();
		S.jogador.save();
		S.zonas.save();
		getLogger().info("BTEBrasilSystem Desativado!");
	}

	public static BTEBrasilSystem getPlugin() {
		return instance;
	}

	public void tpZona(Player p, int x, int y, int z) {
		World w = p.getWorld();
		Location l = new Location(w, x, y, z);
		p.teleport(l);
	}
}
