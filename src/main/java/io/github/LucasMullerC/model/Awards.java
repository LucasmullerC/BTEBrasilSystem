package io.github.LucasMullerC.model;

public class Awards implements Comparable<Awards>{
    String ID,Name,URL;
    double Points;

    public Awards() {
    }
    public Awards (String A){
        this.ID = A; 
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }
    public double getPoints() {
        return Points;
    }

    public void setPoints(double Points) {
        this.Points = Points;
    }

    @Override
	public int compareTo(Awards G) {
		return this.ID.compareTo(G.getID());
	}

    
}
