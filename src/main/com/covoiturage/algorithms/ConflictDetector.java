package com.covoiturage.algorithms;

import com.covoiturage.models.Graph;
import com.covoiturage.models.PathResult;
import java.util.*;

public final class ConflictDetector {

    private ConflictDetector() {}

    public static List<String> detecterConflits(
            Graph graph,
            Map<String, List<String>> assignments,
            Map<String, Integer> capacites) {

        List<String> conflits = new ArrayList<>();

        // 1. Vérifier les capacités des véhicules
        for (Map.Entry<String, List<String>> entry : assignments.entrySet()) {
            String vehicule = entry.getKey();
            List<String> route = entry.getValue();
            Integer capaciteMax = capacites.get(vehicule);

            if (capaciteMax == null) {
                conflits.add("CAPACITE_INCONNUE: Véhicule " + vehicule + " n'a pas de capacité définie");
                continue;
            }

            int passagersActuels = route.size() - 1; // -1 pour le dépôt

            if (passagersActuels > capaciteMax) {
                conflits.add("CAPACITE_DEPASSEE: Véhicule " + vehicule + " a " +
                        passagersActuels + " passagers pour une capacité de " + capaciteMax);
            }
        }

        // 2. Vérifier les doublons d'utilisateurs
        Set<String> utilisateursAssignes = new HashSet<>();
        for (List<String> route : assignments.values()) {
            for (int i = 1; i < route.size(); i++) { // Commencer à 1 pour sauter le dépôt
                String utilisateur = route.get(i);
                if (!utilisateursAssignes.add(utilisateur)) {
                    conflits.add("UTILISATEUR_DUPLIQUE: " + utilisateur + " assigné à multiple véhicules");
                }
            }
        }

        // 3. Vérifier les chemins inexistants
        for (Map.Entry<String, List<String>> entry : assignments.entrySet()) {
            List<String> route = entry.getValue();
            for (int i = 0; i < route.size() - 1; i++) {
                String from = route.get(i);
                String to = route.get(i + 1);

                if (!graph.containsNode(from) || !graph.containsNode(to)) {
                    conflits.add("NOEUD_INEXISTANT: Chemin de " + from + " à " + to + " impossible");
                    continue;
                }

                PathResult path = PlusCourtcChemin.findShortestPath(graph, from, to);
                if (!path.isPathExists()) {
                    conflits.add("CHEMIN_IMPOSSIBLE: Aucun chemin trouvé de " + from + " à " + to);
                }
            }
        }

        return conflits;
    }

    public static boolean verifierContraintesTemps(
            Graph graph,
            Map<String, List<String>> assignments,
            Map<String, String> horairesUtilisateurs) {

        // Implémentation simplifiée des contraintes temporelles
        for (List<String> route : assignments.values()) {
            if (route.size() > 1) {
                double tempsTotal = estimerTempsTrajet(graph, route);
                if (tempsTotal > 120.0) { // 2 heures max par exemple
                    return false;
                }
            }
        }
        return true;
    }

    private static double estimerTempsTrajet(Graph graph, List<String> route) {
        double tempsTotal = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            PathResult path = PlusCourtcChemin.findShortestPath(graph, route.get(i), route.get(i + 1));
            if (path.isPathExists()) {
                // Utiliser le temps calculé ou estimer à partir de la distance
                double distance = path.getTotalDistance();
                double vitesseMoyenne = 40.0; // km/h en ville
                tempsTotal += (distance / vitesseMoyenne) * 60; // Conversion en minutes
            }
        }
        return tempsTotal;
    }

    // Méthode utilitaire pour vérifier si une solution est valide
    public static boolean solutionEstValide(
            Graph graph,
            Map<String, List<String>> assignments,
            Map<String, Integer> capacites) {

        List<String> conflits = detecterConflits(graph, assignments, capacites);
        return conflits.isEmpty();
    }

    // Méthode pour générer un rapport détaillé
    public static String genererRapportConflits(
            Graph graph,
            Map<String, List<String>> assignments,
            Map<String, Integer> capacites) {

        List<String> conflits = detecterConflits(graph, assignments, capacites);

        if (conflits.isEmpty()) {
            return "✅ Aucun conflit détecté - Solution valide";
        }

        StringBuilder rapport = new StringBuilder();
        rapport.append("❌ ").append(conflits.size()).append(" conflit(s) détecté(s):\n");
        for (String conflit : conflits) {
            rapport.append("   • ").append(conflit).append("\n");
        }
        return rapport.toString();
    }
}