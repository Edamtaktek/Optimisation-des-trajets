package com.covoiturage.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stocke le résultat d'un calcul de plus court chemin.
 */
public class PathResult {
    private List<String> pathNodeIds;
    private double totalDistance;
    private double totalTime;
    private boolean pathExists;

    public PathResult() {
        this.pathNodeIds = new ArrayList<>();
    }

    public PathResult(List<String> pathNodeIds, double totalDistance, double totalTime, boolean pathExists) {
        this.pathNodeIds = new ArrayList<>(Objects.requireNonNull(pathNodeIds, "pathNodeIds"));
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.pathExists = pathExists;
    }

    public List<String> getPathNodeIds() {
        return Collections.unmodifiableList(pathNodeIds);
    }

    public void setPathNodeIds(List<String> pathNodeIds) {
        this.pathNodeIds = new ArrayList<>(Objects.requireNonNull(pathNodeIds, "pathNodeIds"));
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isPathExists() {
        return pathExists;
    }

    public void setPathExists(boolean pathExists) {
        this.pathExists = pathExists;
    }

    @Override
    public String toString() {
        return "RésultatChemin{" +
                "nœuds=" + pathNodeIds +
                ", distanceTotale=" + totalDistance +
                ", tempsTotal=" + totalTime +
                ", cheminExiste=" + pathExists +
                '}';
    }
}
