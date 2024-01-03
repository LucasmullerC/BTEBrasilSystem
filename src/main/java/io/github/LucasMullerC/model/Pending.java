package io.github.LucasMullerC.model;

public class Pending implements Comparable<Pending> {
    String UUID, ApplicationZones, Builds;
    Boolean isFinished;

    public Pending(String U) {
        this.UUID = U;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getApplicationZones() {
        return ApplicationZones;
    }

    public void setApplicationZones(String ApplicationZones) {
        this.ApplicationZones = ApplicationZones;
    }

    public String getBuilds() {
        return Builds;
    }

    public void setBuilds(String builds) {
        Builds = builds;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public int compareTo(Pending G) {
        return this.UUID.compareTo(G.getUUID());
    }
}
