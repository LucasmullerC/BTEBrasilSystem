package io.github.LucasMullerC.Objetos;

public class Areas implements Comparable<Areas>{
    private String Claim,Nome,Pontos,Player,Participantes,Imgs,Status;
    private Integer Builds;

    public Areas (String A){
        this.Claim = A; 
    }

    public String getClaim() {
        return Claim;
    }

    public String getNome() {
        return Nome;
    }


    public void setNome(String nome) {
        Nome = nome;
    }


    public String getPontos() {
        return Pontos;
    }


    public void setPontos(String pontos) {
        Pontos = pontos;
    }


    public String getPlayer() {
        return Player;
    }


    public void setPlayer(String player) {
        Player = player;
    }


    public String getImgs() {
        return Imgs;
    }


    public void setImgs(String imgs) {
        Imgs = imgs;
    }


    public String getStatus() {
        return Status;
    }


    public void setStatus(String status) {
        Status = status;
    }
    @Override
	public int compareTo(Areas A) {
		return this.Claim.compareTo(A.getClaim());
	}

    public String getParticipantes() {
        return Participantes;
    }

    public void setParticipantes(String participantes) {
        Participantes = participantes;
    }

    public Integer getBuilds() {
        return Builds;
    }

    public void setBuilds(Integer builds) {
        Builds = builds;
    }
    
}
