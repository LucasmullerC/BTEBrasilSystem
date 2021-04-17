package io.github.LucasMullerC.Objetos;

public class Jogadores implements Comparable<Jogadores> {
	private String UUID, Discord, Time;
	private boolean Cargo;

	public Jogadores(String U) {
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
	public void setCargo(boolean CA) {
        this.Cargo = CA;
    }
    
    public boolean getCargo() {
        return this.Cargo;
    }

	@Override
	public String toString() {
		return "Você é um construtor do: " + this.Time + "\r\n" + "You are a Builder from: " + this.Time;
	}

	@Override
	public int compareTo(Jogadores G) {
		return this.UUID.compareTo(G.getUUID());
	}

}
