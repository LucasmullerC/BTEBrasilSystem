package io.github.LucasMullerC.Objetos;

public class Builders implements Comparable<Builders> {
    private String UUID,Discord,Awards,Destaque;
    private Integer Tier,Builds;
    private double Pontos;

    public Builders (String A){
        this.UUID = A; 
    }
    public String getUUID() {
        return UUID;
    }
    public void setUUID(String uUID) {
        UUID = uUID;
    }
    public String getDiscord() {
        return Discord;
    }
    public void setDiscord(String discord) {
        Discord = discord;
    }
    public String getAwards() {
        return Awards;
    }
    public void setAwards(String awards) {
        Awards = awards;
    }
    public Integer getTier() {
        return Tier;
    }
    public void setTier(Integer tier) {
        Tier = tier;
    }
    public Integer getBuilds() {
        return Builds;
    }
    public void setBuilds(Integer builds) {
        Builds = builds;
    }
    public double getPontos() {
        return Pontos;
    }
    public void setPontos(double pontos) {
        Pontos = pontos;
    }
    public String getDestaque() {
        return Destaque;
    }
    public void setDestaque(String destaque) {
        Destaque = destaque;
    }
    @Override
	public int compareTo(Builders G) {
		return this.UUID.compareTo(G.getUUID());
	}
}
