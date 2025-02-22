package io.github.LucasMullerC.model;

import org.bukkit.Location;

import io.github.LucasMullerC.annotation.FieldOrder;

public class ApplicationZone implements Comparable<ApplicationZone> {
	@FieldOrder(1)
	private String ApplicationZone;

	@FieldOrder(2)
	private String Name;

	@FieldOrder(3)
	Boolean isBusy;

	@FieldOrder(4)
	Boolean IsAtZoneA;

	@FieldOrder(5)
	Boolean IsAtZoneb;
	
	@FieldOrder(6)
	Boolean IsAtZonec;
	
	@FieldOrder(7)
	Boolean IsAtZoned;

	@FieldOrder(8)
	private Location LocationA;

	@FieldOrder(9)
	private Location LocationB;

	@FieldOrder(10)
	private Location LocationC;

	@FieldOrder(11)
	private Location LocationD;

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
