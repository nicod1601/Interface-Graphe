package appli.metier;

import java.util.*;

public class BellmanFordMetier {

	public static void bellmanFord(List<Sommet> graph, String sourceName) {
		int V = graph.size();
		Map<String, Integer> d = new HashMap<>();

		// Initialisation
		for (Sommet s : graph) {
			d.put(s.getNom(), Integer.MAX_VALUE);
		}
		d.put(sourceName, 0);

		// Relaxation |V|-1 fois
		for (int i = 1; i <= V - 1; i++) {
			for (Sommet u : graph) {
				int du = d.get(u.getNom());
				if (du == Integer.MAX_VALUE) continue; // sommet non atteint encore

				for (Lien lien : u.getLiens()) {
					String vName = lien.getNom();
					int weight = lien.getDistance();
					if (d.get(vName) > du + weight) {
						d.put(vName, du + weight);
					}
				}
			}
		}

		// Vérification des cycles négatifs
		for (Sommet u : graph) {
			int du = d.get(u.getNom());
			if (du == Integer.MAX_VALUE) continue;

			for (Lien lien : u.getLiens()) {
				String vName = lien.getNom();
				int weight = lien.getDistance();
				if (d.get(vName) > du + weight) {
					System.out.println("Cycle négatif détecté !");
					return;
				}
			}
		}

		// Affichage des distances
		System.out.println("Distances depuis " + sourceName + " :");
		for (Sommet s : graph) {
			System.out.println(s.getNom() + " -> " + d.get(s.getNom()));
		}
	}

	public static void main(String[] args) 
	{
		// Création du graphe
		Sommet A = new Sommet("A");
		Sommet B = new Sommet("B");
		Sommet C = new Sommet("C");
		Sommet D = new Sommet("D");

		A.getLiens().add(new Lien("B", 1));
		A.getLiens().add(new Lien("C", 4));
		B.getLiens().add(new Lien("C", 2));
		B.getLiens().add(new Lien("D", 6));
		C.getLiens().add(new Lien("D", 3));

		List<Sommet> graph = Arrays.asList(A, B, C, D);

		bellmanFord(graph, "A");
	}
}
