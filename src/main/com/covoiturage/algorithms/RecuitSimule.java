package com.covoiturage.algorithms;

import com.covoiturage.models.Graph;
import com.covoiturage.models.PathResult;

import java.util.*;

public final class RecuitSimule {

    private RecuitSimule() {}

    public static OptimizationResult optimizeRoute(Graph graph, String startNodeId,
                                                   List<String> mustVisitNodes) {
        // Solution initiale avec Nearest Neighbor
        List<String> currentSolution = NearestNeighbor.findRoute(graph, startNodeId, mustVisitNodes);
        double currentCost = calculateRouteDistance(graph, currentSolution);

        List<String> bestSolution = new ArrayList<>(currentSolution);
        double bestCost = currentCost;

        double temperature = 1000.0;
        final double coolingRate = 0.003;

        Random random = new Random();

        for (int iteration = 0; iteration < 1000 && temperature > 1.0; iteration++) {
            List<String> newSolution = generateNeighbor(currentSolution);
            double newCost = calculateRouteDistance(graph, newSolution);

            if (acceptanceProbability(currentCost, newCost, temperature) > random.nextDouble()) {
                currentSolution = newSolution;
                currentCost = newCost;
            }

            if (newCost < bestCost) {
                bestSolution = new ArrayList<>(newSolution);
                bestCost = newCost;
            }

            temperature *= 1 - coolingRate;
        }

        return new OptimizationResult(bestSolution, bestCost, bestCost < Double.POSITIVE_INFINITY);
    }

    private static List<String> generateNeighbor(List<String> currentRoute) {
        List<String> neighbor = new ArrayList<>(currentRoute);
        Random random = new Random();

        if (neighbor.size() > 2) {
            // CORRECTION : Éviter de modifier le premier nœud (départ)
            int i = random.nextInt(neighbor.size() - 1) + 1;
            int j = random.nextInt(neighbor.size() - 1) + 1;
            if (i != j) {
                Collections.swap(neighbor, i, j);
            }
        }
        return neighbor;
    }

    private static double acceptanceProbability(double currentCost, double newCost, double temperature) {
        return (newCost < currentCost) ? 1.0 : Math.exp((currentCost - newCost) / temperature);
    }

    private static double calculateRouteDistance(Graph graph, List<String> route) {
        if (route.size() < 2) return 0.0;

        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            PathResult path = PlusCourtcChemin.findShortestPath(graph, route.get(i), route.get(i + 1));
            // AJOUT : Vérifications de sécurité comme dans NearestNeighbor
            if (path != null && path.isPathExists() &&
                    !Double.isInfinite(path.getTotalDistance()) &&
                    !Double.isNaN(path.getTotalDistance())) {
                totalDistance += path.getTotalDistance();
            } else {
                // Retourner une grande distance si chemin invalide
                return Double.POSITIVE_INFINITY;
            }
        }
        return totalDistance;
    }

    public static class OptimizationResult {
        private final List<String> route;
        private final double totalDistance;
        private final boolean feasible;

        public OptimizationResult(List<String> route, double totalDistance, boolean feasible) {
            this.route = Collections.unmodifiableList(new ArrayList<>(route));
            this.totalDistance = totalDistance;
            this.feasible = feasible;
        }

        public List<String> getRoute() { return route; }
        public double getTotalDistance() { return totalDistance; }
        public boolean isFeasible() { return feasible; }
    }
}