package com.covoiturage.algorithms;

import com.covoiturage.models.Graph;
import com.covoiturage.models.PathResult;

import java.util.*;

public final class NearestNeighbor {

    private NearestNeighbor() {} // Constructeur privé pour classe utilitaire

    public static List<String> findRoute(Graph graph, String startNodeId, List<String> mustVisitNodes) {
        List<String> route = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        String current = startNodeId;

        route.add(current);
        visited.add(current);

        List<String> toVisit = new ArrayList<>();
        for (String nodeId : mustVisitNodes) {
            if (graph.containsNode(nodeId) && !nodeId.equals(startNodeId)) {
                toVisit.add(nodeId);
            }
        }

        while (!toVisit.isEmpty()) {
            String nearest = findNearestNode(graph, current, toVisit);
            if (nearest == null) break;

            route.add(nearest);
            visited.add(nearest);
            toVisit.remove(nearest);
            current = nearest;
        }

        return route;
    }

    private static String findNearestNode(Graph graph, String fromNodeId, List<String> candidates) {
        String nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (String candidate : candidates) {
            PathResult path = PlusCourtcChemin.findShortestPath(graph, fromNodeId, candidate);

            // Vérifications en cascade pour éviter toute exception
            if (path == null) {
                continue; // Chemin non calculable
            }

            if (!path.isPathExists()) {
                continue; // Aucun chemin trouvé
            }

            if (Double.isInfinite(path.getTotalDistance()) || Double.isNaN(path.getTotalDistance())) {
                continue; // Distance invalide
            }

            if (path.getTotalDistance() < minDistance) {
                minDistance = path.getTotalDistance();
                nearest = candidate;
            }
        }
        return nearest;
    }}