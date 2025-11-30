package com.covoiturage.models;

/**
 * Valeur utilitaire utilisée par la file de priorité durant l'algorithme de Dijkstra.
 */
public class NoeudDistance implements Comparable<NoeudDistance> {
    private final String nodeId;
    private final double distance;

    public NoeudDistance(String nodeId, double distance) {
        this.nodeId = nodeId;
        this.distance = distance;
    }

    public String getNodeId() {
        return nodeId;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(NoeudDistance other) {
        return Double.compare(this.distance, other.distance);
    }
}
