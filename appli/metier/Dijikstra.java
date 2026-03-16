package appli.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Dijikstra
{
	private ArrayList<Sommet> sommets;
	private HashMap<String, Integer> distances;
	private HashMap<String, String> predecesseurs;
	private PriorityQueue<Noeud> queue;

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
			System.out.println("ERROR Dijikstra: Empty sommets");
			return;
		}

		try {
			String debut = this.sommets.get(0).getNom();
			System.out.println("DEBUG Dijikstra: Starting from " + debut);
			initialiser(debut);

			while (!queue.isEmpty())
			{
				Noeud courant = queue.poll();

				Integer currentDist = distances.get(courant.nom);
				if (currentDist == null)
				{
					System.out.println("ERROR Dijikstra: Unknown vertex " + courant.nom);
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
			System.out.println("DEBUG Dijikstra: Calculation complete. Distances: " + distances);
		} catch (Exception e) {
			System.out.println("ERROR Dijikstra calculCheminCourt: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void initialiser(String debut)
	{
		System.out.println("DEBUG Dijikstra initialiser: Initializing from " + debut);
		System.out.println("DEBUG Dijikstra: Number of vertices = " + sommets.size());
		
		for (Sommet s : sommets)
		{
			distances.put(s.getNom(), Integer.MAX_VALUE);
			predecesseurs.put(s.getNom(), null);
		}
		
		if (!distances.containsKey(debut))
		{
			System.out.println("ERROR Dijikstra: Start vertex " + debut + " not in distances map!");
			System.out.println("Available vertices: " + distances.keySet());
			return;
		}
		
		distances.put(debut, 0);
		queue.add(new Noeud(debut, 0));
		System.out.println("DEBUG Dijikstra: Initialization complete. Start distance = 0");
	}

	private void relaxer(String u, String v, int poids)
	{
		try {
			Integer distU = distances.get(u);
			Integer distV = distances.get(v);

			if (distU == null)
			{
				System.out.println("ERROR Dijikstra relaxer: Source vertex " + u + " not found");
				return;
			}

			if (distV == null)
			{
				System.out.println("ERROR Dijikstra relaxer: Destination vertex " + v + " not found");
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

	public ArrayList<String> getChemin(String destination)
	{
		ArrayList<String> chemin = new ArrayList<>();

		if (destination == null || distances == null || !distances.containsKey(destination))
		{
			System.out.println("Dijikstra: Destination invalide ou introuvable");
			return chemin;
		}

		if (distances.get(destination) == Integer.MAX_VALUE)
		{
			System.out.println("Dijikstra: Pas de chemin vers " + destination);
			return chemin;
		}

		String courant = destination;
		while (courant != null)
		{
			chemin.add(0, courant);
			courant = predecesseurs.get(courant);
		}

		System.out.println("Dijikstra: Distance = " + distances.get(destination) + ", Chemin = " + chemin);
		return chemin;
	}

	public HashMap<String, Integer> getDistance()
	{
		return distances;
	}

	public HashMap<String, String> getPrecedent()
	{
		return predecesseurs;
	}

	private class Noeud implements Comparable<Noeud>
	{
		String nom;
		int distance;

		Noeud(String nom, int distance)
		{
			this.nom = nom;
			this.distance = distance;
		}

		public int compareTo(Noeud autre)
		{
			return Integer.compare(this.distance, autre.distance);
		}
	}
}
