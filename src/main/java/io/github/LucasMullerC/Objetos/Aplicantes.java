package io.github.LucasMullerC.Objetos;

import java.time.LocalDate;

public class Aplicantes implements Comparable<Aplicantes> {
	private String UUID, Discord, Time, Seccao, Zona, deadline;

	public Aplicantes(String U) {
		this.UUID = U;
	}

	public String getUUID() {
		return this.UUID;
	}

	public void setTime(String T) {
		this.Time = T;
	}

	public String getTime() {
		return this.Time;
	}

	public void setDiscord(String Disc) {
		this.Discord = Disc;
	}

	public String getDiscord() {
		return this.Discord;
	}

	public void setZona(String Z) {
		this.Zona = Z;
	}

	public String getZona() {
		return this.Zona;
	}

	public void setSeccao(String Sec) {
		this.Seccao = Sec;
	}

	public String getSeccao() {
		return this.Seccao;
	}
	public void setDeadline(String Dead) {
		this.deadline = Dead;
	}

	public String getDeadLine() {
		return this.deadline;
	}

	@Override
	public String toString() {
		return "A Sua aplicação ainda está em andamento...";
	}

	@Override
	public int compareTo(Aplicantes G) {
		return this.UUID.compareTo(G.getUUID());
	}

}
