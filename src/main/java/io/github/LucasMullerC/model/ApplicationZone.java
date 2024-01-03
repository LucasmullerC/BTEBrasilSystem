package io.github.LucasMullerC.model;

import org.bukkit.Location;

public class ApplicationZone implements Comparable<ApplicationZone> {
	private String ApplicationZone, Name;
	private Location LocationA,LocationB,LocationC,LocationD;
	Boolean isBusy, IsAtZoneA, IsAtZoneb, IsAtZonec, IsAtZoned;

	public Boolean getIsAtZoned() {
		return IsAtZoned;
	}

	public void setIsAtZoned(Boolean isAtZoned) {
		IsAtZoned = isAtZoned;
	}

	public ApplicationZone(String A) {
		this.ApplicationZone = A;
	}

	public String getApplicationZone() {
		return this.ApplicationZone;
	}

	public void setName(String N) {
		this.Name = N;
	}

	public String getName() {
		return this.Name;
	}
	public Location getLocationA() {
		return LocationA;
	}

	public void setLocationA(Location locationA) {
		LocationA = locationA;
	}

	public Location getLocationB() {
		return LocationB;
	}

	public void setLocationB(Location locationB) {
		LocationB = locationB;
	}

	public Location getLocationC() {
		return LocationC;
	}

	public void setLocationC(Location locationC) {
		LocationC = locationC;
	}

	public Location getLocationD() {
		return LocationD;
	}

	public void setLocationD(Location locationD) {
		LocationD = locationD;
	}
	public Boolean getIsBusy() {
		return isBusy;
	}

	public void setIsBusy(Boolean isBusy) {
		this.isBusy = isBusy;
	}

	public Boolean getIsAtZoneA() {
		return IsAtZoneA;
	}

	public void setIsAtZoneA(Boolean isAtZoneA) {
		IsAtZoneA = isAtZoneA;
	}

	public Boolean getIsAtZoneb() {
		return IsAtZoneb;
	}

	public void setIsAtZoneb(Boolean isAtZoneb) {
		IsAtZoneb = isAtZoneb;
	}

	public Boolean getIsAtZonec() {
		return IsAtZonec;
	}

	public void setIsAtZonec(Boolean isAtZonec) {
		IsAtZonec = isAtZonec;
	}

	@Override
	public int compareTo(ApplicationZone Z) {
		return this.ApplicationZone.compareTo(Z.getApplicationZone());
	}

}
