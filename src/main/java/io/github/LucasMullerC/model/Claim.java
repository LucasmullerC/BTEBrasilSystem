package io.github.LucasMullerC.model;

public class Claim implements Comparable<Claim>{
    private String Claim,Name,Points,Player,Participants,Image,Status;
    private Integer Builds;

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

        @Override
	public int compareTo(Claim A) {
		return this.Claim.compareTo(A.getClaim());
	}
    
}
