package com.covoiturage.algorithms;

import com.covoiturage.models.Graph;
import com.covoiturage.models.Noeud;
import com.covoiturage.models.PathResult;

import java.util.*;

public class TestOptimisation {

    public static void main(String[] args) {
        System.out.println("=== TEST DES ALGORITHMES D'OPTIMISATION ===\n");

        // 1. Créer un graphe de test
        Graph graph = creerGrapheTest();
        System.out.println("✅ Graphe créé avec " + graph.getAllNodes().size() + " nœuds");

        // 2. Tester l'algorithme de plus court chemin
        testerPlusCourtChemin(graph);

        // 3. Tester l'algorithme du plus proche voisin
        testerPlusProcheVoisin(graph);

        // 4. Tester le recuit simulé
        testerRecuitSimule(graph);

        // 5. Tester le gestionnaire d'optimisation complet
        testerGestionnaireOptimisation(graph);

        System.out.println("\n=== TESTS TERMINÉS ===");
    }

    private static Graph creerGrapheTest() {
        Graph graph = new Graph();

        // Ajouter les dépôts (garages)
        graph.addNode(new Noeud("depot_nord", 48.8566, 2.3522, "Dépôt Nord"));
        graph.addNode(new Noeud("depot_sud", 48.8566, 2.3622, "Dépôt Sud"));

        // Ajouter les points d'utilisateurs
        graph.addNode(new Noeud("user1", 48.8575, 2.3514, "Utilisateur 1 - Châtelet"));
        graph.addNode(new Noeud("user2", 48.8550, 2.3530, "Utilisateur 2 - Notre-Dame"));
        graph.addNode(new Noeud("user3", 48.8580, 2.3540, "Utilisateur 3 - République"));
        graph.addNode(new Noeud("user4", 48.8530, 2.3500, "Utilisateur 4 - Saint-Michel"));
        graph.addNode(new Noeud("user5", 48.8600, 2.3550, "Utilisateur 5 - Bastille"));

        // Ajouter des liaisons (distances en km)
        // Depuis depot_nord
        graph.addEdge("depot_nord", "user1", 1.2);
        graph.addEdge("depot_nord", "user3", 1.8);
        graph.addEdge("depot_nord", "user5", 2.1);

        // Depuis depot_sud
        graph.addEdge("depot_sud", "user2", 0.9);
        graph.addEdge("depot_sud", "user4", 1.1);

        // Liaisons entre utilisateurs
        graph.addEdge("user1", "user2", 2.0);
        graph.addEdge("user1", "user3", 1.5);
        graph.addEdge("user2", "user4", 1.2);
        graph.addEdge("user3", "user5", 1.0);
        graph.addEdge("user4", "user1", 2.3);
        graph.addEdge("user5", "user3", 1.1);

        // Liaisons bidirectionnelles
        graph.addEdge("user2", "user1", 2.0);
        graph.addEdge("user3", "user1", 1.5);
        graph.addEdge("user4", "user2", 1.2);
        graph.addEdge("user5", "user3", 1.0);
        graph.addEdge("user1", "user4", 2.3);
        graph.addEdge("user3", "user5", 1.1);

        return graph;
    }

    private static void testerPlusCourtChemin(Graph graph) {
        System.out.println("\n--- TEST PLUS COURT CHEMIN ---");

        // Test 1: Chemin direct existant
        PathResult result1 = PlusCourtcChemin.findShortestPath(graph, "depot_nord", "user1");
        System.out.println("Depot Nord → User1: " +
                (result1.isPathExists() ? result1.getTotalDistance() + " km" : "Aucun chemin"));

        // Test 2: Chemin avec intermédiaires
        PathResult result2 = PlusCourtcChemin.findShortestPath(graph, "depot_nord", "user4");
        System.out.println("Depot Nord → User4: " +
                (result2.isPathExists() ? result2.getTotalDistance() + " km" : "Aucun chemin"));

        // Test 3: Même nœud
        PathResult result3 = PlusCourtcChemin.findShortestPath(graph, "user1", "user1");
        System.out.println("User1 → User1: " +
                (result3.isPathExists() ? "0 km (même nœud)" : "Erreur"));

        // Test 4: Chemin inexistant (devrait être géré)
        try {
            PathResult result4 = PlusCourtcChemin.findShortestPath(graph, "depot_nord", "node_inexistant");
            System.out.println("Chemin inexistant: " + (result4.isPathExists() ? "BUG" : "Correctement géré"));
        } catch (Exception e) {
            System.out.println("Chemin inexistant: Exception attrapée - " + e.getMessage());
        }
    }

    private static void testerPlusProcheVoisin(Graph graph) {
        System.out.println("\n--- TEST PLUS PROCHE VOISIN ---");

        List<String> pointsAVisiter = Arrays.asList("user1", "user2", "user3", "user4", "user5");

        // Test depuis depot_nord
        List<String> routeNord = NearestNeighbor.findRoute(graph, "depot_nord", pointsAVisiter);
        System.out.println("Route depuis Depot Nord: " + routeNord);
        double distanceNord = calculerDistanceRoute(graph, routeNord);
        System.out.println("Distance totale: " + distanceNord + " km");

        // Test depuis depot_sud
        List<String> routeSud = NearestNeighbor.findRoute(graph, "depot_sud", pointsAVisiter);
        System.out.println("Route depuis Depot Sud: " + routeSud);
        double distanceSud = calculerDistanceRoute(graph, routeSud);
        System.out.println("Distance totale: " + distanceSud + " km");
    }

    private static void testerRecuitSimule(Graph graph) {
        System.out.println("\n--- TEST RECUIT SIMULÉ ---");

        List<String> pointsAVisiter = Arrays.asList("user1", "user2", "user3", "user4", "user5");

        // Solution initiale
        List<String> routeInitiale = NearestNeighbor.findRoute(graph, "depot_nord", pointsAVisiter);
        double distanceInitiale = calculerDistanceRoute(graph, routeInitiale);
        System.out.println("Solution initiale: " + routeInitiale);
        System.out.println("Distance initiale: " + distanceInitiale + " km");

        // Optimisation
        RecuitSimule.OptimizationResult result = RecuitSimule.optimizeRoute(graph, "depot_nord", pointsAVisiter);
        System.out.println("Solution optimisée: " + result.getRoute());
        System.out.println("Distance optimisée: " + result.getTotalDistance() + " km");
        System.out.println("Amélioration: " + (distanceInitiale - result.getTotalDistance()) + " km");

        if (result.isFeasible()) {
            System.out.println("✅ Solution réalisable");
        } else {
            System.out.println("❌ Solution non réalisable");
        }
    }

    private static void testerGestionnaireOptimisation(Graph graph) {
        System.out.println("\n--- TEST GESTIONNAIRE OPTIMISATION ---");

        // Configuration du test
        List<String> utilisateurs = Arrays.asList("user1", "user2", "user3", "user4", "user5");
        List<String> depots = Arrays.asList("depot_nord", "depot_sud");

        Map<String, Integer> capacites = new HashMap<>();
        capacites.put("depot_nord", 3);  // Véhicule nord: 3 places
        capacites.put("depot_sud", 2);   // Véhicule sud: 2 places

        System.out.println("Configuration:");
        System.out.println(" - Utilisateurs: " + utilisateurs);
        System.out.println(" - Dépôts: " + depots);
        System.out.println(" - Capacités: " + capacites);

        // Exécution de l'optimisation
        Map<String, List<String>> resultats = GestionnaireOptimisation.optimiserCovoiturage(
                graph, utilisateurs, depots, capacites);

        // Affichage des résultats
        System.out.println("\nRÉSULTATS DE L'OPTIMISATION:");
        for (Map.Entry<String, List<String>> entry : resultats.entrySet()) {
            String depot = entry.getKey();
            List<String> route = entry.getValue();
            double distance = calculerDistanceRoute(graph, route);
            int passagers = route.size() - 1; // -1 pour le dépôt

            System.out.println("🚗 Véhicule " + depot + ":");
            System.out.println("   - Route: " + route);
            System.out.println("   - Passagers: " + passagers + "/" + capacites.get(depot));
            System.out.println("   - Distance: " + distance + " km");
            System.out.println("   - Taux remplissage: " + (passagers * 100 / capacites.get(depot)) + "%");
        }

        // Statistiques globales
        double distanceTotale = resultats.values().stream()
                .mapToDouble(route -> calculerDistanceRoute(graph, route))
                .sum();
        int totalPassagers = resultats.values().stream()
                .mapToInt(route -> route.size() - 1)
                .sum();

        System.out.println("\n📊 STATISTIQUES GLOBALES:");
        System.out.println("   - Distance totale: " + distanceTotale + " km");
        System.out.println("   - Nombre total de passagers: " + totalPassagers);
        System.out.println("   - Nombre de véhicules utilisés: " + resultats.size());
    }

    // Méthode utilitaire pour calculer la distance d'une route
    private static double calculerDistanceRoute(Graph graph, List<String> route) {
        if (route.size() < 2) return 0.0;

        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            PathResult path = PlusCourtcChemin.findShortestPath(graph, route.get(i), route.get(i + 1));
            if (path != null && path.isPathExists()) {
                totalDistance += path.getTotalDistance();
            }
        }
        return totalDistance;
    }
}