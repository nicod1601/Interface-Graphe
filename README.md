# Interface-Graphe

Une application graphique Java pour la visualisation et l'analyse de graphes avec implémentation d'algorithmes classiques.

## 📋 Description

Interface-Graphe est une application desktop développée en Java qui permet de :
- Visualiser et manipuler des graphes
- Implémenter des algorithmes de recherche de plus court chemin
- Charger/éditer des graphes depuis des fichiers XML
- Afficher les résultats avec une interface graphique

## 🏗️ Architecture du Projet

### Structure des Dossiers

```
appli/
├── Controleur.java          # Contrôleur principal de l'application
├── donnee/                  # Données
│   └── graphe1.xml         # Fichier de graphe exemple
├── ihm/                     # Interface Homme-Machine
│   ├── Appli.java          # Classe principale de l'application
│   ├── Edit.java           # Éditeur de graphe
│   ├── Graphe.java         # Gestion de l'affichage du graphe
│   ├── GrapheCopie.java    # Copie du graphe
│   ├── Menu.java           # Barre de menu
│   ├── Theme.java          # Gestion des thèmes
│   └── dessin/             # Composants de dessin
│       ├── Cercle.java     # Dessin des sommets
│       └── Lien.java       # Dessin des arêtes
└── metier/                 # Logique métier
    ├── Dijikstra.java      # Algorithme de Dijkstra
    ├── BellmanFordMetier.java  # Algorithme de Bellman-Ford
    ├── Lecture.java        # Lecture des fichiers graphe
    ├── Lien.java           # Classe modèle pour les arêtes
    └── Sommet.java         # Classe modèle pour les sommets
```

## 🔧 Installation et Compilation

### Prérequis
- Java JDK 8 ou supérieur

### Compilation
Windows :
```batch
run.bat
```

Ou manuellement :
```bash
javac -cp appli appli/Controleur.java
javac -cp appli appli/**/*.java
```

## ▶️ Lancement

Windows :
```batch
run.bat
```

Ou directement :
```bash
java -cp bin appli.ihm.Appli
```

## 📚 Fonctionnalités

### Algorithmes Implémentés
- **Dijkstra** : Recherche du plus court chemin (graphes non-orientés)
- **Bellman-Ford** : Recherche du plus court chemin (graphes avec poids négatifs)

### Interface Utilisateur
- Visualisation graphique des graphes
- Édition interactive des sommets et arêtes
- Système de thèmes personnalisables
- Menu de navigation

## 📁 Format des Données

Les graphes sont stockés au format XML :
```xml
<graphe>
  <sommet id="1">...</sommet>
  <lien source="1" destination="2">...</lien>
</graphe>
```

## 🔍 Classes Principales

- **Appli.java** : Point d'entrée de l'application
- **Controleur.java** : Coordonne les interactions entre IHM et métier
- **Sommet.java** : Modèle pour les nœuds du graphe
- **Lien.java** : Modèle pour les arêtes du graphe
- **Dijikstra.java** / **BellmanFordMetier.java** : Implémentations des algorithmes

## 📝 Historique des Versions

Les versions antérieures du projet sont stockées dans le dossier `version/construction/`

## 🐛 Notes

Ce projet est en phase de développement et peut contenir des défauts.

## 👤 Auteur

Projet personnel

---

**Dernière mise à jour** : Février 2026