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
					return;
				}
			}
		}
	}
}
