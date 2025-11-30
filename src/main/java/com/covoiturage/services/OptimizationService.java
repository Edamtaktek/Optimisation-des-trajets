package com.covoiturage.services;

import com.covoiturage.algorithms.GestionnaireOptimisation;
import com.covoiturage.algorithms.ConflictDetector;
import com.covoiturage.algorithms.CalculDistance;
import com.covoiturage.models.Graph;
import com.covoiturage.models.Noeud;
import com.covoiturage.models.User;
import com.covoiturage.models.Vehicle;

import java.util.*;
import java.util.concurrent.*;

import static com.covoiturage.algorithms.CalculDistance.calculateDistance;

public class OptimizationService {

    public static class JobStatus {
        public String jobId;
        public String status; // PENDING, RUNNING, DONE, ERROR
        public String message;
        public Map<String, List<String>> result;
        public List<String> conflicts;
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors()/2)
    );
    private final Map<String, Future<JobStatus>> jobs = new ConcurrentHashMap<>();

    public String startOptimization(List<User> users, List<Vehicle> vehicles) {
        String jobId = UUID.randomUUID().toString();

        Callable<JobStatus> task = () -> {
            JobStatus js = new JobStatus();
            js.jobId = jobId;
            js.status = "RUNNING";

            try {
                // 1. Convertir les données en format compatible avec vos algorithmes
                Graph graph = createGraphFromUsersAndVehicles(users, vehicles);
                List<String> userPoints = extractUserPoints(users);
                List<String> vehicleDepots = extractVehicleDepots(vehicles, users);
                Map<String, Integer> capacities = extractCapacities(vehicles);

                // 2. Utiliser VOS algorithmes existants
                Map<String, List<String>> optimizedRoutes =
                        GestionnaireOptimisation.optimiserCovoiturage(
                                graph, userPoints, vehicleDepots, capacities
                        );

                // 3. Détecter les conflits
                js.conflicts = ConflictDetector.detecterConflits(
                        graph, optimizedRoutes, capacities
                );

                js.result = optimizedRoutes;
                js.status = "DONE";
                js.message = "Optimisation terminée avec " + optimizedRoutes.size() + " véhicules";

            } catch (Exception e) {
                js.status = "ERROR";
                js.message = "Erreur lors de l'optimisation: " + e.getMessage();
                e.printStackTrace();
            }
            return js;
        };

        Future<JobStatus> future = executor.submit(task);
        jobs.put(jobId, future);
        return jobId;
    }

    public JobStatus getStatus(String jobId) {
        Future<JobStatus> future = jobs.get(jobId);
        if (future == null) {
            JobStatus js = new JobStatus();
            js.jobId = jobId;
            js.status = "ERROR";
            js.message = "Job introuvable";
            return js;
        }

        if (!future.isDone()) {
            JobStatus js = new JobStatus();
            js.jobId = jobId;
            js.status = "RUNNING";
            js.message = "Optimisation en cours...";
            return js;
        }

        try {
            return future.get(50, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            JobStatus js = new JobStatus();
            js.jobId = jobId;
            js.status = "RUNNING";
            js.message = "Presque terminé...";
            return js;
        } catch (Exception e) {
            JobStatus js = new JobStatus();
            js.jobId = jobId;
            js.status = "ERROR";
            js.message = "Erreur: " + e.getMessage();
            return js;
        }
    }

    // Méthodes utilitaires pour la conversion des données
    private Graph createGraphFromUsersAndVehicles(List<User> users, List<Vehicle> vehicles) {
        Graph graph = new Graph();

        // Ajouter les dépôts des véhicules (utiliser l'adresse du conducteur comme dépôt)
        for (Vehicle vehicle : vehicles) {
            // Trouver le conducteur (User) de ce véhicule
            User driver = findDriver(vehicle, users);
            if (driver != null) {
                String depotId = "depot_" + vehicle.getId();
                // Pour la démo, on génère des coordonnées aléatoires basées sur l'ID
                double[] coords = generateCoordinatesFromId(driver.getId());
                graph.addNode(new Noeud(
                        depotId,
                        coords[0], // latitude
                        coords[1], // longitude
                        "Dépôt " + vehicle.getModel()
                ));
            }
        }

        // Ajouter les points de départ des utilisateurs
        for (User user : users) {
            String startPointId = "user_start_" + user.getId();
            double[] startCoords = generateCoordinatesFromId(user.getId());
            graph.addNode(new Noeud(
                    startPointId,
                    startCoords[0] + 0.001, // Léger décalage
                    startCoords[1] + 0.001,
                    "Départ " + user.getName()
            ));

            String endPointId = "user_end_" + user.getId();
            double[] endCoords = generateCoordinatesFromId(user.getId() + 1000); // Différent du départ
            graph.addNode(new Noeud(
                    endPointId,
                    endCoords[0] - 0.001,
                    endCoords[1] - 0.001,
                    "Arrivée " + user.getName()
            ));
        }

        // Calculer et ajouter les liaisons
        addAllPossibleEdges(graph);

        return graph;
    }

    private User findDriver(Vehicle vehicle, List<User> users) {
        return users.stream()
                .filter(user -> user.getId().equals(vehicle.getDriverId()))
                .findFirst()
                .orElse(null);
    }

    private double[] generateCoordinatesFromId(Long id) {
        // Générer des coordonnées déterministes basées sur l'ID pour la démo
        // En production, vous utiliserez de vraies géocodes depuis les adresses
        Random random = new Random(id);
        double lat = 48.85 + (random.nextDouble() - 0.5) * 0.1; // Autour de Paris
        double lon = 2.35 + (random.nextDouble() - 0.5) * 0.1;
        return new double[]{lat, lon};
    }

    private void addAllPossibleEdges(Graph graph) {
        List<Noeud> allNodes = new ArrayList<>(graph.getAllNodes());

        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = i + 1; j < allNodes.size(); j++) {
                Noeud from = allNodes.get(i);
                Noeud to = allNodes.get(j);

                // CORRECTION : Appeler la méthode avec les coordonnées directement
                double distance = CalculDistance.calculateDistance(
                        from.getLatitude(), from.getLongitude(),
                        to.getLatitude(), to.getLongitude()
                );

                graph.addEdge(from.getId(), to.getId(), distance);
                graph.addEdge(to.getId(), from.getId(), distance); // Bidirectionnel
            }
        }
    }
    private List<String> extractUserPoints(List<User> users) {
        List<String> points = new ArrayList<>();
        for (User user : users) {
            points.add("user_start_" + user.getId()); // Points de départ seulement
            // Pour une optimisation complète, vous pourriez inclure les points d'arrivée
        }
        return points;
    }

    private List<String> extractVehicleDepots(List<Vehicle> vehicles, List<User> users) {
        List<String> depots = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            User driver = findDriver(vehicle, users);
            if (driver != null) {
                depots.add("depot_" + vehicle.getId());
            }
        }
        return depots;
    }

    private Map<String, Integer> extractCapacities(List<Vehicle> vehicles) {
        Map<String, Integer> capacities = new HashMap<>();
        for (Vehicle vehicle : vehicles) {
            capacities.put("depot_" + vehicle.getId(), vehicle.getCapacity());
        }
        return capacities;
    }

    // Méthode pour nettoyer les jobs terminés
    public void cleanupCompletedJobs() {
        jobs.entrySet().removeIf(entry -> {
            try {
                return entry.getValue().isDone();
            } catch (Exception e) {
                return true;
            }
        });
    }

    // Méthode pour arrêter le service proprement
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}