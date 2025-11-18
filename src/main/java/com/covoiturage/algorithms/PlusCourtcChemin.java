package java.com.covoiturage.algorithms;

import java.com.covoiturage.models.Noeud;

import java.com.covoiturage.models.Liaison;
import java.com.covoiturage.models.Graph;
import java.com.covoiturage.models.NoeudDistance;
import java.com.covoiturage.models.PathResult;
import java.util.*;

/**
 * Implémente l'algorithme de Dijkstra pour l'optimisation des trajets.
 */
public final class PlusCourtcChemin {

    private PlusCourtcChemin() {

    }

    public static PathResult findShortestPath(Graph graph, String startNodeId, String endNodeId) {
        Objects.requireNonNull(graph, "graph");
        validateNode(graph, startNodeId, "départ");
        validateNode(graph, endNodeId, "arrivée");

        if (startNodeId.equals(endNodeId)) {
            return new PathResult(List.of(startNodeId), 0.0, 0.0, true);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<NoeudDistance> queue = new PriorityQueue<>();

        for (Noeud node : graph.getAllNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }
        distances.put(startNodeId, 0.0);
        queue.add(new NoeudDistance(startNodeId, 0.0));

        while (!queue.isEmpty()) {
            NoeudDistance current = queue.poll();
            if (!visited.add(current.getNodeId())) {
                continue;
            }

            if (current.getNodeId().equals(endNodeId)) {
                return buildResult(startNodeId, endNodeId, distances, previous, graph);
            }

            for (Liaison liaison : graph.getEdges(current.getNodeId())) {
                double newDistance = distances.get(current.getNodeId()) + liaison.getDistance();
                if (newDistance < distances.get(liaison.getToNodeId())) {
                    distances.put(liaison.getToNodeId(), newDistance);
                    previous.put(liaison.getToNodeId(), current.getNodeId());
                    queue.add(new NoeudDistance(liaison.getToNodeId(), newDistance));
                }
            }
        }

        return new PathResult(Collections.emptyList(), Double.POSITIVE_INFINITY, Double.NaN, false);
    }

    private static PathResult buildResult(String startNodeId,
                                          String endNodeId,
                                          Map<String, Double> distances,
                                          Map<String, String> previous,
                                          Graph graph) {
        List<String> path = new ArrayList<>();
        String current = endNodeId;
        double totalTime = 0.0;

        while (current != null) {
            path.add(current);
            String parent = previous.get(current);
            if (parent != null) {
                totalTime += estimateTravelTime(graph.getNode(parent), graph.getNode(current));
            }
            current = parent;
        }
        Collections.reverse(path);
        return new PathResult(path, distances.get(endNodeId), totalTime, true);
    }

    private static double estimateTravelTime(Noeud from, Noeud to) {
        if (from == null || to == null) {
            return Double.NaN;
        }
        // Heuristique simple avec une vitesse moyenne de 40 km/h.
        double distance = CalculDistance.calculateDistance(
                from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());
        double averageSpeedKmh = 40.0;
        return (distance / averageSpeedKmh) * 60.0;
    }

    private static void validateNode(Graph graph, String nodeId, String descriptor) {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du nœud de " + descriptor + " est requis");
        }
        if (!graph.containsNode(nodeId)) {
            throw new IllegalArgumentException("Le graphe ne contient pas le nœud de " + descriptor + " : " + nodeId);
        }
    }
}
