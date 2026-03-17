package appli.metier;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implémentation de l'algorithme de Bellman-Ford pour le calcul du plus court chemin.
 * <p>
 * Contrairement à Dijkstra, Bellman-Ford tolère les arêtes de poids négatif.
 * Il effectue {@code n-1} relaxations (n = nombre de sommets) et s'arrête prématurément
 * si aucun changement n'est détecté entre deux itérations.
 * <p>
 * Le sommet <b>source</b> est toujours le premier sommet de la liste fournie.
 *
 * @author Nicolas D. & Marta AN.
 */
public class BellmanFord
{
	/**
	 * Distance minimale connue depuis la source jusqu'à chaque sommet.
	 * La valeur {@link Integer#MAX_VALUE} signifie « non atteint ».
	 */
	private HashMap<String, Integer> distance;

	/**
	 * Pour chaque sommet, nom de son prédécesseur dans le chemin optimal.
	 * Permet de reconstruire le chemin par remontée.
	 */
	private HashMap<String, String> precedent;

	/** Liste des sommets du graphe à traiter. */
	private ArrayList<Sommet> lstTest;

	// ── Constructeur ──────────────────────────────────────────────────────────

	/**
	 * Initialise et exécute Bellman-Ford sur la liste de sommets fournie.
	 * Le premier sommet de la liste est utilisé comme source (distance = 0).
	 *
	 * @param lst Liste des sommets du graphe (non nulle, non vide).
	 */
	public BellmanFord(ArrayList<Sommet> lst)
	{
		this.distance  = new HashMap<>();
		this.precedent = new HashMap<>();
		this.lstTest   = lst;
		this.calculCheminCourt();
	}

	// ── Algorithme ────────────────────────────────────────────────────────────

	/**
	 * Exécute l'algorithme de Bellman-Ford.
	 * <ol>
	 *   <li>Initialise toutes les distances à {@link Integer#MAX_VALUE}, sauf la source à 0.</li>
	 *   <li>Effectue jusqu'à {@code n-1} passes de relaxation sur toutes les arêtes.</li>
	 *   <li>S'arrête tôt si aucun changement n'est observé (convergence anticipée).</li>
	 * </ol>
	 */
	public void calculCheminCourt()
	{
		ArrayList<Sommet> sommets = this.lstTest;

		// Initialisation : toutes les distances à l'infini
		for (Sommet s : sommets)
			this.distance.put(s.getNom(), Integer.MAX_VALUE);

		// La source (premier sommet) est à distance 0
		this.distance.put(sommets.get(0).getNom(), 0);

		// n-1 passes de relaxation
		for (int i = 0; i < sommets.size() - 1; i++)
		{
			boolean changement = false;

			for (Sommet s : sommets)
			{
				int distSource = this.distance.get(s.getNom());

				// On ne peut relaxer depuis un sommet non encore atteint
				if (distSource == Integer.MAX_VALUE) continue;

				for (Lien l : s.getLiens())
				{
					int nouvelleDistance = distSource + l.getDistance();
					if (nouvelleDistance < this.distance.get(l.getNom()))
					{
						this.distance.put(l.getNom(), nouvelleDistance);
						this.precedent.put(l.getNom(), s.getNom());
						changement = true;
					}
				}
			}

			// Convergence anticipée : si aucune relaxation, inutile de continuer
			if (!changement) break;
		}
	}

	// ── Reconstruction du chemin ──────────────────────────────────────────────

	/**
	 * Reconstruit le chemin depuis la source jusqu'à {@code destination}
	 * en remontant la chaîne de prédécesseurs.
	 *
	 * @param destination Nom du sommet d'arrivée.
	 * @return Liste ordonnée des noms de sommets (source en tête, destination en queue),
	 *         ou une liste vide si la destination est inaccessible.
	 */
	public ArrayList<String> getChemin(String destination)
	{
		ArrayList<String> chemin  = new ArrayList<>();
		String            courant = destination;

		while (courant != null)
		{
			chemin.add(0, courant);               // Insertion en tête pour avoir source → dest
			courant = this.precedent.get(courant);
		}

		return chemin;
	}

	// ── Accesseurs ────────────────────────────────────────────────────────────

	/**
	 * Retourne la map des distances finales calculées.
	 *
	 * @return Map nom de sommet → distance depuis la source.
	 */
	public HashMap<String, Integer> getDistance()
	{
		return distance;
	}

	/**
	 * Retourne la map des prédécesseurs dans l'arbre des plus courts chemins.
	 *
	 * @return Map nom de sommet → nom de son prédécesseur.
	 */
	public HashMap<String, String> getPrecedent()
	{
		return precedent;
	}
}
