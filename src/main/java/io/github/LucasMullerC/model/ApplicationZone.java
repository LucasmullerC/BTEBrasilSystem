package io.github.LucasMullerC.model;

import org.bukkit.Location;

public class ApplicationZone implements Comparable<ApplicationZone> {
	private String ApplicationZone, Name;
	private Location LocationA,LocationB,LocationC,LocationD;
	Boolean isBusy, IsAtZoneA, IsAtZoneb, IsAtZonec, IsAtZoned;

	public ApplicationZone() {
	}

	public ApplicationZone(String applicationZone) {
		ApplicationZone = applicationZone;
	}

	public String getApplicationZone() {
		return ApplicationZone;
	}

	public void setApplicationZone(String applicationZone) {
		ApplicationZone = applicationZone;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public Boolean getIsAtZoned() {
		return IsAtZoned;
	}

	public void setIsAtZoned(Boolean isAtZoned) {
		IsAtZoned = isAtZoned;
	}

	@Override
	public int compareTo(ApplicationZone Z) {
		return this.ApplicationZone.compareTo(Z.getApplicationZone());
	}

}
