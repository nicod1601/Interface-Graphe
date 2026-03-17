package appli.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Implémentation de l'algorithme de Dijkstra pour le calcul du plus court chemin.
 * <p>
 * Utilise une file de priorité ({@link PriorityQueue}) pour traiter en premier
 * le sommet le plus proche de la source.
 * <p>
 * <b>Contrainte :</b> toutes les distances doivent être positives ou nulles.
 * <p>
 * Le sommet <b>source</b> est toujours le premier sommet de la liste fournie au constructeur.
 *
 * @author Nicolas D. & Marta AN.
 */
public class Dijikstra
{
	// ── Nœud interne pour la file de priorité ─────────────────────────────────

	/**
	 * Nœud utilisé dans la {@link PriorityQueue}.
	 * Comparable par distance croissante.
	 */
	static class Noeud implements Comparable<Noeud>
	{
		/** Nom du sommet. */
		final String nom;

		/** Distance depuis la source au moment de l'insertion dans la file. */
		final int distance;

		Noeud(String nom, int distance)
		{
			this.nom      = nom;
			this.distance = distance;
		}

		/** Ordonne par distance croissante (priorité aux nœuds les plus proches). */
		@Override
		public int compareTo(Noeud o)
		{
			return Integer.compare(this.distance, o.distance);
		}
	}

	// ── Classe interne : état d'une étape pour l'affichage ────────────────────

	/**
	 * Snapshot des distances à une étape donnée de l'algorithme.
	 * Utilisé par {@link #calculerDistancesEtapes} pour alimenter le tableau de bord.
	 */
	public static class EtapeDijkstra
	{
		/** Libellé de l'étape (ex. "Initialisation", "Étape 1"). */
		public final String label;

		/** Distances connues à cet instant. */
		public final HashMap<String, Integer> distances;

		/**
		 * @param label     Libellé de l'étape.
		 * @param distances Copie des distances à cet instant.
		 */
		public EtapeDijkstra(String label, HashMap<String, Integer> distances)
		{
			this.label     = label;
			this.distances = distances;
		}
	}

	// ── Champs d'instance ─────────────────────────────────────────────────────

	/** Sommets du graphe à traiter. */
	private final ArrayList<Sommet> sommets;

	/**
	 * Distance minimale connue depuis la source jusqu'à chaque sommet.
	 * {@link Integer#MAX_VALUE} = non atteint.
	 */
	private HashMap<String, Integer> distances;

	/**
	 * Prédécesseur de chaque sommet dans le chemin optimal.
	 * Permet de reconstruire le chemin par remontée.
	 */
	private HashMap<String, String> predecesseurs;

	/** File de priorité sur les nœuds (distance croissante). */
	private PriorityQueue<Noeud> queue;

	// ── Constructeur ──────────────────────────────────────────────────────────

	/**
	 * Initialise et exécute Dijkstra sur la liste de sommets fournie.
	 * Le premier sommet est utilisé comme source (distance = 0).
	 *
	 * @param sommets Liste des sommets du graphe (non nulle, non vide).
	 */
	public Dijikstra(ArrayList<Sommet> sommets)
	{
		this.sommets      = sommets;
		this.distances    = new HashMap<>();
		this.predecesseurs = new HashMap<>();
		this.queue        = new PriorityQueue<>();
		calculCheminCourt();
	}

	// ── Algorithme principal ──────────────────────────────────────────────────

	/**
	 * Exécute l'algorithme de Dijkstra depuis le premier sommet de la liste.
	 * <ol>
	 *   <li>Initialise toutes les distances à l'infini, sauf la source à 0.</li>
	 *   <li>Extrait itérativement le nœud de plus faible distance et relaxe ses voisins.</li>
	 * </ol>
	 * Les exceptions sont catchées et loguées pour ne pas bloquer l'IHM.
	 */
	private void calculCheminCourt()
	{
		if (this.sommets == null || this.sommets.isEmpty()) return;

		try
		{
			initialiser(this.sommets.get(0).getNom());

			while (!this.queue.isEmpty())
			{
				Noeud courant = this.queue.poll();

				Integer distCourante = this.distances.get(courant.nom);
				if (distCourante == null) continue;

				// Nœud périmé dans la file (une meilleure distance a été trouvée depuis)
				if (courant.distance > distCourante) continue;

				Sommet sommet = trouverSommet(courant.nom);
				if (sommet != null)
					for (Lien lien : sommet.getLiens())
						relaxer(courant.nom, lien.getNom(), lien.getDistance());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Initialise les structures de données avant le lancement de l'algorithme.
	 * Toutes les distances à l'infini, la source à 0.
	 *
	 * @param source Nom du sommet de départ.
	 */
	private void initialiser(String source)
	{
		for (Sommet s : this.sommets)
		{
			this.distances.put(s.getNom(), Integer.MAX_VALUE);
			this.predecesseurs.put(s.getNom(), null);
		}

		if (!this.distances.containsKey(source)) return;

		this.distances.put(source, 0);
		this.queue.add(new Noeud(source, 0));
	}

	/**
	 * Tente d'améliorer la distance connue vers le sommet {@code v} en passant par {@code u}.
	 * Si la nouvelle distance est meilleure, met à jour la map et réinsère dans la file.
	 *
	 * @param u     Sommet courant (déjà traité).
	 * @param v     Sommet voisin (candidat à la relaxation).
	 * @param poids Poids de l'arête u → v.
	 */
	private void relaxer(String u, String v, int poids)
	{
		try
		{
			Integer distU = this.distances.get(u);
			Integer distV = this.distances.get(v);

			if (distU == null || distV == null)        return;
			if (distU == Integer.MAX_VALUE)            return; // Source non encore atteinte

			int nouvelleDistance = distU + poids;
			if (nouvelleDistance < distV)
			{
				this.distances.put(v, nouvelleDistance);
				this.predecesseurs.put(v, u);
				this.queue.add(new Noeud(v, nouvelleDistance));
			}
		}
		catch (Exception e)
		{
			System.err.println("Erreur Dijkstra relaxer(" + u + " → " + v + ") : " + e.getMessage());
		}
	}

	// ── Calcul avec capture des étapes (pour l'affichage du tableau) ──────────

	/**
	 * Exécute Dijkstra depuis {@code depart} et capture l'état des distances
	 * après chaque extraction de la file.
	 * <p>
	 * Utilisé par {@link appli.ihm.PanelDistance} pour afficher le tableau pas à pas.
	 *
	 * @param depart  Nom du sommet source.
	 * @param arrivee Nom du sommet cible (non utilisé pour le calcul, conservé pour extensibilité).
	 * @return Liste ordonnée des étapes (initialisation puis une étape par extraction).
	 */
	public ArrayList<EtapeDijkstra> calculerDistancesEtapes(String depart, String arrivee)
	{
		ArrayList<EtapeDijkstra>  etapes       = new ArrayList<>();
		HashMap<String, Integer>  dist         = new HashMap<>();
		HashMap<String, String>   predecess    = new HashMap<>();
		PriorityQueue<Noeud>      filePriorite = new PriorityQueue<>();

		// Initialisation
		for (Sommet s : this.sommets)
		{
			dist.put(s.getNom(), Integer.MAX_VALUE);
			predecess.put(s.getNom(), null);
		}
		dist.put(depart, 0);
		filePriorite.add(new Noeud(depart, 0));
		etapes.add(new EtapeDijkstra("Initialisation", new HashMap<>(dist)));

		// Boucle principale
		while (!filePriorite.isEmpty())
		{
			Noeud courant = filePriorite.poll();

			Integer distCourante = dist.get(courant.nom);
			if (distCourante == null)                  continue;
			if (courant.distance > distCourante)       continue; // Entrée périmée

			Sommet sommet = trouverSommet(courant.nom);
			if (sommet != null)
			{
				for (Lien lien : sommet.getLiens())
				{
					String voisin          = lien.getNom();
					int    nouvelleDist    = distCourante + lien.getDistance();

					if (nouvelleDist < dist.get(voisin))
					{
						dist.put(voisin, nouvelleDist);
						predecess.put(voisin, courant.nom);
						filePriorite.add(new Noeud(voisin, nouvelleDist));
					}
				}
			}

			// Capturer l'état après chaque extraction
			etapes.add(new EtapeDijkstra("Étape " + etapes.size(), new HashMap<>(dist)));
		}

		return etapes;
	}

	// ── Reconstruction du chemin ──────────────────────────────────────────────

	/**
	 * Reconstruit le chemin le plus court depuis la source jusqu'à {@code destination}
	 * en remontant la chaîne de prédécesseurs.
	 *
	 * @param destination Nom du sommet d'arrivée.
	 * @return Liste ordonnée (source → destination) des noms de sommets,
	 *         ou liste vide si la destination est inaccessible ou inconnue.
	 */
	public ArrayList<String> getChemin(String destination)
	{
		ArrayList<String> chemin = new ArrayList<>();

		if (destination == null || !this.distances.containsKey(destination)) return chemin;
		if (this.distances.get(destination) == Integer.MAX_VALUE)            return chemin;

		String courant = destination;
		while (courant != null)
		{
			chemin.add(0, courant);
			courant = this.predecesseurs.get(courant);
		}

		return chemin;
	}

	// ── Accesseurs ────────────────────────────────────────────────────────────

	/**
	 * Retourne la map des distances finales calculées depuis la source.
	 *
	 * @return Map nom de sommet → distance minimale.
	 */
	public HashMap<String, Integer> getDistance()
	{
		return this.distances;
	}

	/**
	 * Retourne la map des prédécesseurs dans l'arbre des plus courts chemins.
	 *
	 * @return Map nom de sommet → nom de son prédécesseur.
	 */
	public HashMap<String, String> getPrecedent()
	{
		return this.predecesseurs;
	}

	// ── Utilitaire privé ──────────────────────────────────────────────────────

	/**
	 * Recherche un sommet dans la liste par son nom.
	 *
	 * @param nom Nom recherché.
	 * @return Sommet trouvé, ou {@code null} s'il est absent.
	 */
	private Sommet trouverSommet(String nom)
	{
		for (Sommet s : this.sommets)
			if (s.getNom().equals(nom)) return s;
		return null;
	}
}
