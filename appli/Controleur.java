package appli;

import appli.ihm.Appli;
import appli.metier.Lecture;
import appli.metier.Sommet;
import java.util.ArrayList;

/**
 * Contrôleur principal de l'application (pattern MVC).
 * Fait le lien entre la vue ({@link Appli}) et le modèle ({@link Lecture}).
 * Toutes les interactions entre l'IHM et la logique métier passent par cette classe.
 *
 * @author Nicolas D. & Marta AN.
 */
public class Controleur
{
	/** Modèle : gère la lecture/écriture XML et les algorithmes de chemin. */
	private Lecture lecture;

	/** Vue principale : fenêtre et panneaux de l'application. */
	private Appli appli;

	// ── Constructeur ──────────────────────────────────────────────────────────

	/**
	 * Initialise le contrôleur, instancie le modèle et la vue.
	 */
	public Controleur()
	{
		this.lecture = new Lecture();
		this.appli   = new Appli(this);
	}

	// ── Délégation vers le modèle ─────────────────────────────────────────────

	/**
	 * Retourne la liste de tous les sommets du graphe.
	 *
	 * @return Liste des sommets ({@link Sommet}).
	 */
	public ArrayList<Sommet> getSommets()
	{
		return this.lecture.getSommets();
	}

	/**
	 * Remplace la liste des sommets dans le modèle.
	 *
	 * @param liste Nouvelle liste de sommets à utiliser.
	 */
	public void setSommets(ArrayList<Sommet> liste)
	{
		this.lecture.setSommets(liste);
	}

	/**
	 * Charge un nouveau fichier XML de graphe (efface les données actuelles).
	 *
	 * @param lien Chemin absolu ou relatif vers le fichier XML.
	 */
	public void setLiens(String lien)
	{
		this.lecture.setLien(lien);
	}

	/**
	 * Retourne le document textuel représentant les liens du graphe chargé.
	 *
	 * @return Chaîne de caractères décrivant les arêtes (ex. "A --> B 5").
	 */
	public String getDocument()
	{
		return this.lecture.getDocument();
	}

	/**
	 * Active un algorithme de calcul de chemin court.
	 *
	 * @param mode Nom de l'algorithme : {@code "Dijkstra"} ou {@code "Bellman-Ford"}.
	 */
	public void Mode(String mode)
	{
		this.lecture.Mode(mode);
	}

	/**
	 * Retourne le chemin le plus court calculé par l'algorithme actif.
	 *
	 * @return Liste ordonnée des noms de sommets formant le chemin, ou liste vide.
	 */
	public ArrayList<String> getCheminCourt()
	{
		return this.lecture.getCheminCourt();
	}

	/**
	 * Sauvegarde l'état actuel du graphe dans le fichier XML courant.
	 */
	public void sauvegarderXML()
	{
		this.lecture.sauvegarderXML();
	}

	/**
	 * Modifie uniquement le chemin du fichier de sauvegarde, sans recharger le graphe.
	 * Utilisé pour « Enregistrer sous ».
	 *
	 * @param lien Nouveau chemin de fichier.
	 */
	public void setLienSeulement(String lien)
	{
		this.lecture.setLienSeulement(lien);
	}

	// ── Point d'entrée ────────────────────────────────────────────────────────

	/**
	 * Point d'entrée de l'application.
	 *
	 * @param args Arguments de la ligne de commande (non utilisés).
	 */
	public static void main(String[] args)
	{
		new Controleur();
	}
}
