package io.github.LucasMullerC.model;

public class Pending implements Comparable<Pending> {
    String UUID, regionId, builds;
    Boolean isApplication;

    public Pending(String U) {
        this.UUID = U;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getregionId() {
        return regionId;
    }

    public void setregionId(String regionId) {
        this.regionId = regionId;
    }

    public String getbuilds() {
        return builds;
    }

    public void setbuilds(String builds) {
        this.builds = builds;
    }

    public Boolean getisApplication() {
        return isApplication;
    }

    public void setisApplication(Boolean isApplication) {
        this.isApplication = isApplication;
    }

    @Override
    public int compareTo(Pending G) {
        return this.UUID.compareTo(G.getUUID());
    }
}
