package com.covoiturage.models;

import java.util.Objects;

/**
 * Représente une liaison dirigée entre deux nœuds du graphe.
 */
public class Liaison {
    private String fromNodeId;
    private String toNodeId;
    private double distance;
    private double time;

    public Liaison() {
        this.time = Double.NaN;
    }

    public Liaison(String fromNodeId, String toNodeId, double distance) {
        this(fromNodeId, toNodeId, distance, Double.NaN);
    }

    public Liaison(String fromNodeId, String toNodeId, double distance, double time) {
        this.fromNodeId = Objects.requireNonNull(fromNodeId, "fromNodeId");
        this.toNodeId = Objects.requireNonNull(toNodeId, "toNodeId");
        if (distance <= 0) {
            throw new IllegalArgumentException("la distance doit être positive");
        }
        this.distance = distance;
        this.time = time;
    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = Objects.requireNonNull(fromNodeId, "fromNodeId");
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(String toNodeId) {
        this.toNodeId = Objects.requireNonNull(toNodeId, "toNodeId");
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("la distance doit être positive");
        }
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
