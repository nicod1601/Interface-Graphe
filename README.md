# Interface-Graphe

**Interface-Graphe** est une application desktop Java qui permet de créer, visualiser et analyser des graphes (nœuds/arêtes) à l’aide d’algorithmes classiques de plus court chemin.

---

## 📌 Présentation du projet

L’objectif de ce projet est de proposer une interface simple et responsive pour **créer des graphes**, **modifier leurs arêtes**, et **visualiser les résultats** des algorithmes de plus court chemin.

- Un éditeur de graphe (tableau + vue graphique)
- Un rendu visuel interactif (sommets, liens, distances)
- Une gestion de thèmes pour un rendu moderne et lisible
- Des algorithmes de base (Dijkstra / Bellman-Ford)
- Un format de fichier XML simple pour enregistrer/charger les graphes

---

## 🚀 Comment ça marche ?

### 1) Lancer l’application

- Sur Windows (recommandé) :
  ```batch
  run.bat
  ```

- En ligne de commande (depuis la racine du projet) :
  ```bash
  java -cp bin appli.ihm.Appli
  ```


### 2) Chargez un graphe existant

Les fichiers de graphes sont situés dans `appli/donnee/` ou `bin/appli/donnee/`.

- Le format est XML.
- Chaque `<sommet>` représente un nœud avec un lien et une distance.

Exemple :
```xml
<graphe>

    <sommet nom="A">
        <lien destination="B" distance="4"/>
        <lien destination="C" distance="2"/>
    </sommet>

    <sommet nom="B">
        <lien destination="C" distance="5"/>
        <lien destination="D" distance="10"/>
    </sommet>

    <sommet nom="C">
        <lien destination="E" distance="3"/>
    </sommet>

    <sommet nom="D">
        <lien destination="F" distance="11"/>
    </sommet>

    <sommet nom="E">
        <lien destination="D" distance="4"/>
    </sommet>

    <sommet nom="F">
    </sommet>

</graphe>
```

### 3) Modifier un graphe via l’interface

- Utiliser l’onglet **Édition** pour ajouter/supprimer des lignes (sommets + liens + distances).
- Les changements se reflètent automatiquement dans le graphe graphique.
- Le bouton **Appliquer** met à jour la structure interne utilisée par les algorithmes.

### 4) Visualiser le résultat des algorithmes

L’application affiche le graphe et ses arêtes. Les algorithmes de calcul de plus court chemin (Dijkstra / Bellman-Ford) peuvent être lancés via le menu (ou bouton existant selon version).

---

## 🧩 Architecture du projet

```
appli/
├── Controleur.java          # Coordonne l’IHM et la logique métier
├── donnee/                  # Fichiers XML de graphes exemples
├── ihm/                     # Interface graphique (Swing)
│   ├── Appli.java          # Point d’entrée
│   ├── Edit.java           # Edition de graphe (tableau + visualisation)
│   ├── Graphe.java         # Affichage graphique du graphe
│   ├── GrapheCopie.java    # Copie du graphe pour l’affichage
│   ├── Menu.java           # Barre de menu
│   ├── Theme.java          # Thème / styles de l’interface
│   └── dessin/             # Composants de dessin (sommets/arêtes)
│       ├── Cercle.java
│       └── Lien.java
└── metier/                 # Logique métier / algorithmes
    ├── Dijikstra.java      # Algorithme de Dijkstra
    ├── BellmanFordMetier.java # Algorithme de Bellman-Ford
    ├── Lecture.java        # Lecture d’un fichier XML en objet
    ├── Lien.java           # Modèle d’arête
    └── Sommet.java         # Modèle de nœud
```

---

## 🛠️ Comment contribuer / développer

### Compilation

- Sous Windows :
  ```batch
  run.bat
  ```

- Compilation manuelle (dans racine projet) :
  ```bash
  javac -cp appli appli/**/*.java
  ```

### Exécution (après compilation)

```bash
java -cp bin appli.ihm.Appli
```

---

## 📌 Conseils d’utilisation

- Pour ajouter un lien, insérer une nouvelle ligne dans le tableau puis cliquer sur **Appliquer**.
- Pour modifier une distance, éditer la cellule correspondante, puis **Appliquer**.
- En cas de valeurs invalides (distance négative, cellule vide), l’application remettra la valeur à 0 et affichera l’état dans le tableau.

---

## 🧠 Où trouver les versions antérieures

Les versions historiques sont dans `version/construction/`.

---

✏️ *Ce README est destiné à faciliter la prise en main et à décrire rapidement le fonctionnement du projet.*
