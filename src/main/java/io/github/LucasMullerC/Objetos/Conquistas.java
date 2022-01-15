package io.github.LucasMullerC.Objetos;

public class Conquistas implements Comparable<Conquistas>{
    String ID,Nome,URL;
    double Pontos;
    public Conquistas (String A){
        this.ID = A; 
    }

    public String getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }
    public double getPontos() {
        return Pontos;
    }

    public void setPontos(double pontos) {
        Pontos = pontos;
    }

    @Override
	public int compareTo(Conquistas G) {
		return this.ID.compareTo(G.getID());
	}

    
}
