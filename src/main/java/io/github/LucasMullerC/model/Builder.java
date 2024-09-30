package io.github.LucasMullerC.model;

public class Builder implements Comparable<Builder> {
    private String UUID,Discord,Awards,Featured;
    private Integer Tier,Builds;
    private double Points;

    public Builder() {
    }
    public Builder (String A){
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
    public double getPoints() {
        return Points;
    }
    public void setPoints(double Points) {
        this.Points = Points;
    }
    public String getFeatured() {
        return Featured;
    }
    public void setFeatured(String Featured) {
        this.Featured = Featured;
    }
    @Override
	public int compareTo(Builder G) {
		return this.UUID.compareTo(G.getUUID());
	}
}
