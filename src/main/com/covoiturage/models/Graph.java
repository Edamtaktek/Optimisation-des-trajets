package com.covoiturage.models;

import java.util.*;

/**
 * Représente le réseau routier utilisé pour l'optimisation.
 */
public class Graph {
    private final Map<String, Noeud> nodes;
    private final Map<String, List<Liaison>> adjacencyList;

    public Graph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addNode(Noeud node) {
        Objects.requireNonNull(node, "node");
        nodes.put(node.getId(), node);
        adjacencyList.computeIfAbsent(node.getId(), k -> new ArrayList<>());
    }

    public void addEdge(String fromNodeId, String toNodeId, double distance) {
        addEdge(fromNodeId, toNodeId, distance, Double.NaN);
    }

    public void addEdge(String fromNodeId, String toNodeId, double distance, double time) {
        validateNodeExists(fromNodeId);
        validateNodeExists(toNodeId);
        Liaison liaison = new Liaison(fromNodeId, toNodeId, distance, time);
        adjacencyList.computeIfAbsent(fromNodeId, k -> new ArrayList<>()).add(liaison);
    }

    public List<Liaison> getEdges(String nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    public Noeud getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public Collection<Noeud> getAllNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    public boolean containsNode(String nodeId) {
        return nodes.containsKey(nodeId);
    }

    private void validateNodeExists(String nodeId) {
        if (!nodes.containsKey(nodeId)) {
            throw new IllegalArgumentException("Le nœud " + nodeId + " est introuvable.");
        }
    }
}
