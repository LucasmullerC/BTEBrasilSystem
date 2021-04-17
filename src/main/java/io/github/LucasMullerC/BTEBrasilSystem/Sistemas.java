package io.github.LucasMullerC.BTEBrasilSystem;

import java.util.ArrayList;

import javax.print.attribute.HashPrintJobAttributeSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.Objetos.Aplicantes;
import io.github.LucasMullerC.Objetos.Jogadores;
import io.github.LucasMullerC.Objetos.Zonas;
import io.github.LucasMullerC.Util.ListaAplicar;
import io.github.LucasMullerC.Util.ListaJogadores;
import io.github.LucasMullerC.Util.ListaZonas;

public class Sistemas {
	public static ListaAplicar aplicante;
	public static ListaAplicar pendente;
	public static ListaJogadores jogador;
	public static ListaJogadores equipe;
	public static ListaZonas zonas;
	static Aplicantes A;
	static Jogadores J;
	static Zonas Zn;

	public static Aplicantes addAplicante(String UUID, String Time,String Discord) {
		if (getAplicantePos(UUID) != null) {
			return getAplicantePos(UUID);
		} else {
			A = new Aplicantes(UUID);
			A.setDiscord(Discord);
			A.setTime(Time);
			A.setSeccao("nulo");
			A.setZona("nulo");
			A.setDeadline("nulo");
			aplicante.add(A);
			return A;
		}
	}
	public static void addZonas(Player player) {
		World w = player.getWorld();
		ArrayList<Zonas> z = zonas.getValues();
		Zonas zn;
		if (z.isEmpty() == true) {
			zn = new Zonas("1");
		}
		else {
			Integer pos = z.size()+1;
			zn = new Zonas(String.valueOf(pos));
		}
		zn.setNome("nulo");
		zn.setOcupado(false);
		zn.seta(false);
		zn.setb(false);
		zn.setc(false);
		zn.setd(false);
		Location L = player.getLocation();
		Double X= L.getX();
		Double Y= L.getY();
		Double Z= L.getZ();
		zn.setla(L);
		Location lb = new Location(w, X - 5, Y, Z);
		X = X-5;
		zn.setlb(lb);
		Location lc = new Location(w, X, 38, Z - 5);
		Z = Z-5;
		zn.setlc(lc);
		Location ld = new Location(w, X + 5, Y, Z);
		X = X+5;
		zn.setld(ld);	
		zonas.add(zn);
	}


	public static Jogadores addJogador(String UUID, String Time, String Discord) {
		if (getJogadoresPos(UUID) != null) {
			J = getJogadoresPos(UUID);
			return J;
		} else {
			J = new Jogadores(UUID);
			J.setDiscord(Discord);
			J.setTime("nulo");
			jogador.add(J);
			return J;
		}
	}
	public static void DeadLine (Aplicantes d) {
		Zn = Sistemas.getZona(d.getZona());
		Zn.seta(false);
		Zn.setb(false);
		Zn.setc(false);
		Zn.setd(false);
		Zn.setNome("nulo");
		Zn.setOcupado(false);
		Sistemas.RemoverAplicante(d.getUUID());
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				"region removeowner apply" + A.getZona() + A.getSeccao() + " " + A.getUUID() + " -w world");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + A.getUUID() + " permission unset worldedit.* worldguard:region=apply"
						+ Zn.getZona() + A.getSeccao());
		String txt = "A área "+Zn.getZona()+" foi cancelada automaticamente. Resete a região";
		DiscordPonte.sendMessage("235924471993597952",txt);
		
	}
	 public static String getStringLocation(final Location l) {
	        if (l == null) {
	            return "";
	        }
	        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
	    }
	 public static Location getLocationString(final String s) {
	        if (s == null || s.trim() == "") {
	            return null;
	        }
	        final String[] parts = s.split(":");
	        if (parts.length == 4) {
	            final World w = Bukkit.getServer().getWorld(parts[0]);
	            final int x = Integer.parseInt(parts[1]);
	            final int y = Integer.parseInt(parts[2]);
	            final int z = Integer.parseInt(parts[3]);
	            return new Location(w, (double)x, (double)y, (double)z);
	        }
	        return null;
	    }

	public static Boolean addEquipe(String UUID, String Time) {
		if (equipe.getValues().isEmpty() == true) {
			J = new Jogadores(UUID);
			J.setDiscord("nulo");
			J.setTime(Time);
			equipe.add(J);
			return true;
		}
		else {
			if (getEquipePos(UUID) != null) {
				String T;
				J = getEquipePos(UUID);
				T = J.getTime();
				if (T.equals(Time)) {
					return false;
				}
				else {
					T = T+","+Time;
					J.setTime(T);
					return true;
				}
				
			} else {
				J = new Jogadores(UUID);
				J.setDiscord("nulo");
				J.setTime(Time);
				equipe.add(J);
				return true;
			}
		}
	}

	public static Boolean RemoverEquipe(String UUID) {
		if (getEquipePos(UUID) != null) {
			J = getEquipePos(UUID);
			equipe.remove(J);
			return true;
		} else {
			return false;
		}
	}

	public static void RemoverAplicante(String UUID) {
		A = getAplicantePos(UUID);
		aplicante.remove(A);
	}
	public static void RemoverJogador(String UUID) {
		J = getJogadoresPos(UUID);
		jogador.remove(J);
	}

	public static void RemoverPendente(Aplicantes p) {
		pendente.remove(p);
	}

	public static String getPlayerbyname(String UUID) {
		if (Bukkit.getPlayer(UUID) != null) {
			return Bukkit.getPlayer(UUID).getName();
		} else {
			return "falso";
		}
	}

	public static void addPendente(String UUID) {
		A = getAplicantePos(UUID);
		pendente.add(A);
	}

	public static Jogadores getJogador(String UUID) {
		return getJogadoresPos(UUID);
	}

	public static Aplicantes getAplicante(String UUID) {
		return getAplicantePos(UUID);
	}

	public static Aplicantes getPendente(String time) {
		for (Aplicantes d : pendente.getValues()) {
			if (d.getTime() != null && d.getTime().contains(time)) {
				return d;
			}
		}
		return null;
	}
	public static Aplicantes getPendentebyName(String time) {
		for (Aplicantes d : pendente.getValues()) {
			if (d.getUUID() != null && d.getUUID().contains(time)) {
				return d;
			}
		}
		return null;
	}

	public static Jogadores getEquipe(String UUID) {
		return getEquipePos(UUID);
	}

	public static Zonas getZona(String UUID) {
		return getZonaPos(UUID);
	}

	public static ArrayList<Jogadores> getListaJogador() {
		return jogador.getValues();
	}

	public static ArrayList<Aplicantes> getListaAplicante() {
		return aplicante.getValues();
	}

	public static ArrayList<Zonas> getListaZonas() {
		return zonas.getValues();
	}
    public static Jogadores getDiscord(final String Discord) {
        return getDiscordPos(Discord);
    }
	private static Aplicantes getAplicantePos(String search) {
		for (Aplicantes d : aplicante.getValues()) {
			if (d.getUUID() != null && d.getUUID().contains(search)) {
				return d;
			}
		}
		return null;
	}
	private static Jogadores getDiscordPos(String search) {
		for (Jogadores d : jogador.getValues()) {
			if (d.getDiscord() != null && d.getDiscord().contains(search)) {
				return d;
			}
		}
		return null;
	}

	private static Jogadores getJogadoresPos(String search) {
		for (Jogadores d : jogador.getValues()) {
			if (d.getUUID() != null && d.getUUID().contains(search)) {
				return d;
			}
		}
		return null;
	}

	private static Jogadores getEquipePos(String search) {
		for (Jogadores d : equipe.getValues()) {
			if (d.getUUID() != null && d.getUUID().contains(search)) {
				return d;
			}
		}
		return null;
	}

	private static Aplicantes getPendentePos(String search) {
		for (Aplicantes d : pendente.getValues()) {
			if (d.getUUID() != null && d.getUUID().contains(search)) {
				return d;
			}
		}
		return null;
	}

	private static Zonas getZonaPos(String search) {
		for (Zonas d : zonas.getValues()) {
			if (d.getZona() != null && d.getZona().contains(search)) {
				return d;
			}
		}
		return null;
	}

}
