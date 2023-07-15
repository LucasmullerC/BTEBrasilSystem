package io.github.LucasMullerC.Objetos;

public class Pendentes implements Comparable<Pendentes> {
    String UUID, Area, Builds;
    Boolean App;

    public Pendentes(String U) {
        this.UUID = U;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public Boolean getApp() {
        return App;
    }

    public void setApp(Boolean app) {
        App = app;
    }

    public String getBuilds() {
        return Builds;
    }

    public void setBuilds(String builds) {
        Builds = builds;
    }

    @Override
    public int compareTo(Pendentes G) {
        return this.UUID.compareTo(G.getUUID());
    }
}
