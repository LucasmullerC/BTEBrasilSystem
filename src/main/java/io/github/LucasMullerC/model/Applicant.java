package io.github.LucasMullerC.model;

import io.github.LucasMullerC.annotation.FieldOrder;

public class Applicant implements Comparable<Applicant> {
	@FieldOrder(1)
	private String UUID;

	@FieldOrder(2)
	private String Discord;

	@FieldOrder(3)
	private String getZone;

	@FieldOrder(4)
	private String Section;
	
	@FieldOrder(5)
	private String deadline;

	public Applicant() {
    }
	public Applicant(String U) {
		this.UUID = U;
	}

	public String getUUID() {
		return this.UUID;
	}

	public void setDiscord(String Disc) {
		this.Discord = Disc;
	}

	public String getDiscord() {
		return this.Discord;
	}

	public void setgetZone(String Z) {
		this.getZone = Z;
	}

	public String getgetZone() {
		return this.getZone;
	}

	public void setSection(String Sec) {
		this.Section = Sec;
	}

	public String getSection() {
		return this.Section;
	}

	public void setDeadline(String Dead) {
		this.deadline = Dead;
	}

	public String getDeadLine() {
		return this.deadline;
	}

	@Override
	public int compareTo(Applicant G) {
		return this.UUID.compareTo(G.getUUID());
	}

}
