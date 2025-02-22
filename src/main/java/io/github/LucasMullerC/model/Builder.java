package io.github.LucasMullerC.model;

import io.github.LucasMullerC.annotation.FieldOrder;

public class Builder implements Comparable<Builder> {
    @FieldOrder(1)
    private String UUID;

    @FieldOrder(2)
    private String Discord;

    @FieldOrder(3)
    private Integer Tier;

    @FieldOrder(4)
    private double Points;

    @FieldOrder(5)
    private Integer Builds;
    
    @FieldOrder(6)
    private String Awards;

    @FieldOrder(7)
    private String Featured;

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
