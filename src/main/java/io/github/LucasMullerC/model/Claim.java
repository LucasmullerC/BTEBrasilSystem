package io.github.LucasMullerC.model;

import io.github.LucasMullerC.annotation.FieldOrder;

public class Claim implements Comparable<Claim>{
    @FieldOrder(1)
    private String Claim;

    @FieldOrder(2)
    private String Name;

    @FieldOrder(3)
    private String Points;

    @FieldOrder(4)
    private String Player;

    @FieldOrder(5)
    private String Image;

    @FieldOrder(6)
    private String Status;

    @FieldOrder(7)
    private String Participants;

    @FieldOrder(8)
    private Integer Builds;

    @FieldOrder(9)
    private boolean event = false;

    @FieldOrder(10)
    private String Award = "nulo";

    @FieldOrder(11)
    private int difficulty = 0;
    public Claim() {
    }
    public Claim (String A){
        this.Claim = A; 
    }

    public String getClaim() {
        return Claim;
    }

    public String getName() {
        return Name;
    }


    public void setName(String Name) {
        this.Name = Name;
    }


    public String getPoints() {
        return Points;
    }


    public void setPoints(String Points) {
        this.Points = Points;
    }


    public String getPlayer() {
        return Player;
    }


    public void setPlayer(String player) {
        this.Player = player;
    }


    public String getImage() {
        return Image;
    }


    public void setImage(String Image) {
        this.Image = Image;
    }


    public String getStatus() {
        return Status;
    }


    public void setStatus(String status) {
        this.Status = status;
    }

    public String getParticipants() {
        return Participants;
    }

    public void setParticipants(String Participants) {
        this.Participants = Participants;
    }

    public Integer getBuilds() {
        return Builds;
    }

    public void setBuilds(Integer builds) {
        Builds = builds;
    }

    public boolean isEvent() {
        return event;
    }
    public void setEvent(boolean event) {
        this.event = event;
    }

    public String getAward() {
        return Award;
    }
    public void setAward(String award) {
        Award = award;
    }

    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

        @Override
	public int compareTo(Claim A) {
		return this.Claim.compareTo(A.getClaim());
	}
}
