package io.github.LucasMullerC.Objetos;

import org.bukkit.Location;

public class Zonas implements Comparable<Zonas> {
	private String Zona, Nome;
	private Location la,lb,lc,ld;
	Boolean ocupado, a, b, c, d;

	public Zonas(String A) {
		this.Zona = A;
	}

	public String getZona() {
		return this.Zona;
	}

	public void setNome(String N) {
		this.Nome = N;
	}

	public String getNome() {
		return this.Nome;
	}

	public void setOcupado(Boolean O) {
		this.ocupado = O;
	}

	public Boolean getOcupado() {
		return this.ocupado;
	}

	public void seta(Boolean A) {
		this.a = A;
	}

	public Boolean geta() {
		return this.a;
	}

	public void setb(Boolean B) {
		this.b = B;
	}

	public Boolean getb() {
		return this.b;
	}

	public void setc(Boolean C) {
		this.c = C;
	}

	public Boolean getc() {
		return this.c;
	}

	public void setd(Boolean D) {
		this.d = D;
	}

	public Boolean getd() {
		return this.d;
	}
	public void setla(Location A) {
		this.la = A;
	}
	public Location getla() {
		return this.la;
	}
	public void setlb(Location B) {
		this.lb = B;
	}
	public Location getlb() {
		return this.lb;
	}
	public void setlc(Location C) {
		this.lc = C;
	}
	public Location getlc() {
		return this.lc;
	}
	public void setld(Location D) {
		this.ld = D;
	}
	public Location getld() {
		return this.ld;
	}

	@Override
	public int compareTo(Zonas Z) {
		return this.Zona.compareTo(Z.getZona());
	}

}
