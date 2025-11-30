# 🧠 Module d'Algorithmes d'Optimisation

Ce module implémente le cœur intelligent du système de covoiturage, combinant algorithmes de graphes, heuristiques de tournées et méta-heuristiques pour résoudre le problème complexe d'optimisation multi-véhicules avec contraintes de capacité.

```
 Équipe Algorithmes
- Khadija Kriaa : NearestNeighbor, RecuitSimulé, GestionnaireOptimisation, ConflictDetector, modèles User/Vehicle/Trip
- Adam Taktek : CalculDistance, PlusCourtChemin, modèles Graph/Noeud/Liaison
- Collaboration : Tests d'intégration, optimisation globale, architecture système
```

## Algorithmes Implémentés par Khadija Kriaa
```
📁 algorithms/
├──  NearestNeighbor.java          # Construction intelligente de routes initiales
├──  RecuitSimule.java             # Optimisation globale par méta-heuristique
├──  GestionnaireOptimisation.java # Orchestration et répartition véhicules/utilisateurs
└──  ConflictDetector.java         # Validation et détection de conflits

📁 models/
├── 👤 User.java                     # Entité utilisateur avec préférences
├── 🚗 Vehicle.java                  # Entité véhicule avec capacités
└── 🗺️ Trip.java                     # Entité trajet optimisé
```

## Algorithmes Implémentés par Adam Taktek
```
📁 algorithms/
├── CalculDistance.java           # Formule Haversine pour distances géodésiques
└── ️PlusCourtChemin.java          # Algorithme Dijkstra pour plus courts chemins

📁 models/
├── 🌐 Graph.java                    # Réseau routier modélisé
├── 📌 Noeud.java                    # Points géographiques
└── 🔗 Liaison.java                  # Connexions entre nœuds
```

## 🔄 Workflow de Collaboration

### **Phase 1 : Fondations Algorithmiques Adam Taktek**
```java
// Infrastructure de base pour les calculs géospatiaux
CalculDistance.calculateDistance()    // Calcul précis des distances terrestres
PlusCourtChemin.findShortestPath()    // Algorithme Dijkstra optimisé
Graph, Noeud, Liaison                 // Structures de données fondamentales
```

### **Phase 2 : Optimisation Avancée Khadija Kriaa**
```java
// Intelligence décisionnelle et optimisation
NearestNeighbor.findRoute()           // Construction heuristique de routes
RecuitSimule.optimizeRoute()          // Optimisation globale par recuit simulé
GestionnaireOptimisation.optimiserCovoiturage() // Répartition intelligente
ConflictDetector.detecterConflits()   # Validation et contrôle qualité
```

### **Phase 3 : Intégration et Tests (Équipe)**
```java
// Validation complète du système
TestOptimisation.main()               # Tests d'intégration complets
OptimizationService                   # Service asynchrone unifié
Modèles métier User/Vehicle/Trip      # Entités fonctionnelles
```

## 📊 Résultats de l'Équipe

### **Performance Combinée**
```
Scénario de test : 5 utilisateurs, 2 véhicules (capacités 3+2)

📍 CalculDistance → Distances géodésiques précises
🗺️ PlusCourtChemin → Chemins optimaux garantis
🎯 NearestNeighbor → Route initiale : 9.4 km
🔥 RecuitSimulé → Optimisation : 7.8 km (-1.6 km, -17%)
🤖 Gestionnaire → Répartition : 100% remplissage
✅ ConflictDetector → Aucun conflit détecté

📈 RÉSULTAT FINAL : 5.8 km total - Solution optimale et validée
```

## 🎯 Points Forts de l'Approche Équipe

### **Expertises Combinées**
- **Adam Taktek** : Algorithmes exacts, théorie des graphes, précision mathématique, modélisation spatiale
- **Khadija Kriaa** : Heuristiques avancées, optimisation stochastique, logique métier, validation système
- **🏆 Ensemble** : Couverture complète du spectre algorithmique

### **Décisions Techniques Partagées**
1. **✅ Choix Dijkstra** - Garantie d'optimalité des chemins 
2. **✅ Recuit Simulé** - Optimisation globale efficace 
3. **✅ Architecture Modulaire** - Extensibilité et maintenance 
4. **✅ Validation Systématique** - Robustesse production 




## 🏆 Bilan de Collaboration

**Cette approche partagée a permis de :**

- **Accélérer le développement** grâce à la spécialisation et parallélisation
- **Maximiser la qualité** via l'expertise complémentaire des membres
- **Renforcer la cohésion** d'équipe et la communication technique
- **Élargir les compétences** mutuelles par partage de connaissances

*📅 Dernière mise à jour : 30/11/2025*  
*👥 Équipe Algorithmes : Khadija Kriaa + Adam Taktek*  
*🏷️ Version : 1.0 - Module Algorithmes Complet*
```


