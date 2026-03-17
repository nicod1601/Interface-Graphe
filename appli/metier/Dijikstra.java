package appli.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Implémentation de l'algorithme de Dijkstra pour calculer les plus courts chemins dans un graphe.
 * Calcule les distances minimales depuis le premier sommet vers tous les autres.
 * @author Nicolas D. & Marta AN.
 */

public class Dijikstra
{

	/**
	 * Classe pour représenter une étape de Dijkstra.
	 */
	public static class EtapeDijkstra {
		public String label;
		public HashMap<String, Integer> distances;
		public EtapeDijkstra(String label, HashMap<String, Integer> distances) {
			this.label = label;
			this.distances = distances;
		}
	}

	/**
	 * Calcule les distances étape par étape depuis un sommet de départ.
	 * Retourne une liste d'objets EtapeDijkstra pour affichage du tableau.
	 */
	public ArrayList<EtapeDijkstra> calculerDistancesEtapes(String depart, String arrivee) {
		ArrayList<EtapeDijkstra> etapes = new ArrayList<>();
		HashMap<String, Integer> distances = new HashMap<>();
		HashMap<String, String> predecesseurs = new HashMap<>();
		PriorityQueue<Noeud> queue = new PriorityQueue<>();

		for (Sommet s : this.sommets) {
			distances.put(s.getNom(), Integer.MAX_VALUE);
			predecesseurs.put(s.getNom(), null);
		}
		distances.put(depart, 0);
		queue.add(new Noeud(depart, 0));
		etapes.add(new EtapeDijkstra("Initialisation", new HashMap<>(distances)));

		while (!queue.isEmpty()) {
			Noeud courant = queue.poll();
			Integer currentDist = distances.get(courant.nom);
			if (currentDist == null) continue;
			if (courant.distance > currentDist) continue;

			Sommet sommet = trouverSommet(courant.nom);
			if (sommet != null) {
				for (Lien lien : sommet.getLiens()) {
					String voisin = lien.getNom();
					int nouvelleDistance = currentDist + lien.getDistance();
					if (nouvelleDistance < distances.get(voisin)) {
						distances.put(voisin, nouvelleDistance);
						predecesseurs.put(voisin, courant.nom);
						queue.add(new Noeud(voisin, nouvelleDistance));
					}
				}
			}
			etapes.add(new EtapeDijkstra("Étape " + (etapes.size()), new HashMap<>(distances)));
		}
		return etapes;
	}

	// Version static pour Noeud
	static class Noeud implements Comparable<Noeud> {
		String nom;
		int distance;
		Noeud(String nom, int distance) {
			this.nom = nom;
			this.distance = distance;
		}
		public int compareTo(Noeud o) {
			return Integer.compare(this.distance, o.distance);
		}
	}

	private ArrayList<Sommet> sommets;
	private HashMap<String, Integer> distances;
	private HashMap<String, String> predecesseurs;
	private PriorityQueue<Noeud> queue;

	/**
	 * Constructeur de Dijikstra. Initialise et calcule les plus courts chemins.
	 * @param sommets La liste des sommets du graphe.
	 */
	public Dijikstra(ArrayList<Sommet> sommets)
	{
		this.sommets = sommets;
		this.distances = new HashMap<>();
		this.predecesseurs = new HashMap<>();
		this.queue = new PriorityQueue<>();
		this.calculCheminCourt();
	}

	private void calculCheminCourt()
	{
		if (this.sommets == null || this.sommets.isEmpty())
		{
			return;
		}

		try {
			String debut = this.sommets.get(0).getNom();
			initialiser(debut);

			while (!queue.isEmpty())
			{
				Noeud courant = queue.poll();

				Integer currentDist = distances.get(courant.nom);
				if (currentDist == null)
				{
					continue;
				}

				if (courant.distance > currentDist)
					continue;

				Sommet sommet = trouverSommet(courant.nom);
				if (sommet != null)
				{
					for (Lien lien : sommet.getLiens())
					{
						relaxer(courant.nom, lien.getNom(), lien.getDistance());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialiser(String debut)
	{
		
		for (Sommet s : sommets)
		{
			distances.put(s.getNom(), Integer.MAX_VALUE);
			predecesseurs.put(s.getNom(), null);
		}
		
		if (!distances.containsKey(debut))
		{
			return;
		}
		
		distances.put(debut, 0);
		queue.add(new Noeud(debut, 0));
	}

	private void relaxer(String u, String v, int poids)
	{
		try {
			Integer distU = distances.get(u);
			Integer distV = distances.get(v);

			if (distU == null)
			{
				return;
			}

			if (distV == null)
			{
				return;
			}

			int nouveauDistance = distU + poids;

			if (distU != Integer.MAX_VALUE && nouveauDistance < distV)
			{
				distances.put(v, nouveauDistance);
				predecesseurs.put(v, u);
				queue.add(new Noeud(v, nouveauDistance));
			}
		} catch (Exception e) {
			System.out.println("ERROR Dijikstra relaxer(" + u + "," + v + "): " + e.getMessage());
		}
	}

	private Sommet trouverSommet(String nom)
	{
		for (Sommet s : sommets)
		{
			if (s.getNom().equals(nom))
				return s;
		}
		return null;
	}

	/**
	 * Retourne le chemin le plus court vers la destination spécifiée.
	 * @param destination Le nom du sommet de destination.
	 * @return La liste des noms de sommets formant le chemin, ou une liste vide si inaccessible.
	 */
	public ArrayList<String> getChemin(String destination)
	{
		ArrayList<String> chemin = new ArrayList<>();

		if (destination == null || distances == null || !distances.containsKey(destination))
		{
			return chemin;
		}

		if (distances.get(destination) == Integer.MAX_VALUE)
		{
			return chemin;
		}

		String courant = destination;
		while (courant != null)
		{
			chemin.add(0, courant);
			courant = predecesseurs.get(courant);
		}

		return chemin;
	}

	/**
	 * Retourne la map des distances calculées pour chaque sommet.
	 * @return La map associant nom de sommet à distance.
	 */
	public HashMap<String, Integer> getDistance()
	{
		return distances;
	}

	/**
	 * Retourne la map des prédécesseurs pour reconstruire les chemins.
	 * @return La map associant nom de sommet à son prédécesseur.
	 */
	public HashMap<String, String> getPrecedent()
	{
		return predecesseurs;
	}

	// ...existing code...

	
}
