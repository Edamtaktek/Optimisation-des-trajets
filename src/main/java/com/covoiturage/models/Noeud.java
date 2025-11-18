package java.com.covoiturage.models;

import java.util.Objects;

/**
 * Représente un lieu physique (intersection, point de prise en charge, etc.).
 */
public class Noeud {
    private String id;
    private double latitude;
    private double longitude;
    private String name;

    public Noeud() {
    }

    public Noeud(String id, double latitude, double longitude) {
        this(id, latitude, longitude, null);
    }

    public Noeud(String id, double latitude, double longitude, String name) {
        this.id = Objects.requireNonNull(id, "id");
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Noeud{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                (name != null ? ", nom='" + name + '\'' : "") +
                '}';
    }
}
