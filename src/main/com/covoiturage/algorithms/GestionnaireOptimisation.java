package com.covoiturage.algorithms;

import com.covoiturage.models.Graph;
import com.covoiturage.models.PathResult;
import java.util.*;

public final class GestionnaireOptimisation {

    private GestionnaireOptimisation() {}

    public static Map<String, List<String>> optimiserCovoiturage(
            Graph graph,
            List<String> pointsUtilisateurs,
            List<String> depotsVehicules,
            Map<String, Integer> capacitesVehicules) {

        Map<String, List<String>> assignments = new HashMap<>();

        // Initialiser les véhicules avec leurs dépôts
        for (String depot : depotsVehicules) {
            assignments.put(depot, new ArrayList<>(List.of(depot)));
        }

        System.out.println("🔍 Début de l'assignation - " + pointsUtilisateurs.size() + " utilisateurs à assigner");

        // Assigner les utilisateurs aux véhicules disponibles
        for (String utilisateur : pointsUtilisateurs) {
            String meilleurVehicule = trouverVehiculeOptimal(graph, assignments, utilisateur, capacitesVehicules);
            if (meilleurVehicule != null) {
                assignments.get(meilleurVehicule).add(utilisateur);
                System.out.println("✅ Assigné " + utilisateur + " à " + meilleurVehicule);
            } else {
                System.out.println("❌ Impossible d'assigner " + utilisateur + " - capacité insuffisante");
            }
        }

        // Afficher l'état avant optimisation
        System.out.println("📊 Avant optimisation:");
        for (String depot : depotsVehicules) {
            List<String> route = assignments.get(depot);
            int passagers = route.size() - 1;
            System.out.println("   " + depot + ": " + passagers + " passagers - " + route);
        }

        // Optimiser chaque route
        for (String depot : depotsVehicules) {
            List<String> route = assignments.get(depot);
            if (route.size() > 1) { // Au moins 1 passager
                List<String> pointsAVisiter = new ArrayList<>(route.subList(1, route.size()));
                System.out.println("🔄 Optimisation de " + depot + " avec points: " + pointsAVisiter);

                RecuitSimule.OptimizationResult result = RecuitSimule.optimizeRoute(graph, depot, pointsAVisiter);
                if (result.isFeasible()) {
                    assignments.put(depot, result.getRoute());
                    System.out.println("✅ Route optimisée pour " + depot + ": " + result.getRoute());
                }
            }
        }

        return assignments;
    }

    private static String trouverVehiculeOptimal(Graph graph, Map<String, List<String>> assignments,
                                                 String utilisateur, Map<String, Integer> capacites) {
        String meilleurVehicule = null;
        double meilleurCout = Double.MAX_VALUE;

        for (String depot : assignments.keySet()) {
            List<String> routeActuelle = assignments.get(depot);
            int capaciteMax = capacites.getOrDefault(depot, Integer.MAX_VALUE);
            int passagersActuels = routeActuelle.size() - 1;

            System.out.println("   🔍 Vérification " + depot + ": " + passagersActuels + "/" + capaciteMax + " passagers");

            // Vérifier capacité
            if (passagersActuels >= capaciteMax) {
                System.out.println("      ❌ Capacité dépassée pour " + depot);
                continue;
            }

            // Calculer coût d'insertion
            double cout = calculerCoutInsertion(graph, routeActuelle, utilisateur);
            System.out.println("      💰 Coût d'insertion pour " + utilisateur + ": " + cout + " km");

            if (cout < meilleurCout && cout < Double.MAX_VALUE) {
                meilleurCout = cout;
                meilleurVehicule = depot;
                System.out.println("      🎯 Nouveau meilleur véhicule: " + depot);
            }
        }

        return meilleurVehicule;
    }

    private static double calculerCoutInsertion(Graph graph, List<String> route, String utilisateur) {
        if (route.size() == 1) {
            // Seulement le dépôt - aller directement à l'utilisateur
            PathResult aller = PlusCourtcChemin.findShortestPath(graph, route.get(0), utilisateur);
            if (aller == null || !aller.isPathExists()) {
                return Double.MAX_VALUE;
            }
            return aller.getTotalDistance();
        }

        double meilleurCout = Double.MAX_VALUE;

        for (int i = 1; i <= route.size(); i++) {
            String prev = route.get(i - 1);
            String next = (i < route.size()) ? route.get(i) : route.get(0); // Retour au dépôt si fin

            PathResult pathOriginal = PlusCourtcChemin.findShortestPath(graph, prev, next);
            PathResult pathToUser = PlusCourtcChemin.findShortestPath(graph, prev, utilisateur);
            PathResult pathFromUser = PlusCourtcChemin.findShortestPath(graph, utilisateur, next);

            // Vérifications de sécurité
            if (pathOriginal == null || !pathOriginal.isPathExists() ||
                    pathToUser == null || !pathToUser.isPathExists() ||
                    pathFromUser == null || !pathFromUser.isPathExists()) {
                continue;
            }

            double distanceOriginale = pathOriginal.getTotalDistance();
            double nouvelleDistance = pathToUser.getTotalDistance() + pathFromUser.getTotalDistance();
            double coutInsertion = nouvelleDistance - distanceOriginale;

            if (coutInsertion < meilleurCout) {
                meilleurCout = coutInsertion;
            }
        }

        return meilleurCout;
    }
}