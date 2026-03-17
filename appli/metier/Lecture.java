package appli.metier;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * Couche modèle de l'application (pattern MVC).
 * <p>
 * Responsabilités :
 * <ul>
 *   <li>Lecture et écriture du graphe au format XML.</li>
 *   <li>Stockage de la liste des sommets ({@link Sommet}) et de leurs liens ({@link Lien}).</li>
 *   <li>Instanciation et délégation aux algorithmes {@link Dijikstra} et {@link BellmanFord}.</li>
 *   <li>Calcul et restitution du plus court chemin selon l'algorithme actif.</li>
 * </ul>
 *
 * @author Nicolas D. & Marta AN.
 */
public class Lecture
{
	/** Chemin vers le fichier XML courant (lecture et sauvegarde). */
	private String lien;

	/** Noms des sommets déjà chargés (anti-doublons lors du parsing). */
	private ArrayList<String> sommets;

	/** Objets sommets avec leurs listes de liens. */
	private ArrayList<Sommet> sommetsObjet;

	/** Représentation textuelle du graphe chargé (ex. "A --> B 5\n"). */
	private String document;

	/** Algorithme actif : {@code "Dijkstra"}, {@code "Bellman-Ford"} ou chaîne vide. */
	private String modeSelectionne;

	/** Instance de Dijkstra (null si le mode n'est pas actif). */
	private Dijikstra dijikstra;

	/** Instance de Bellman-Ford (null si le mode n'est pas actif). */
	private BellmanFord bellmanFord;

	// ── Constructeur ──────────────────────────────────────────────────────────

	/**
	 * Initialise le modèle avec des collections vides.
	 * Appelle {@link #lectureXML()} qui ne fait rien si {@link #lien} est vide.
	 */
	public Lecture()
	{
		this.sommets         = new ArrayList<>();
		this.sommetsObjet    = new ArrayList<>();
		this.lien            = "";
		this.document        = "";
		this.dijikstra       = null;
		this.bellmanFord     = null;
		this.modeSelectionne = "";
		lectureXML();
	}

	// ── Lecture XML ───────────────────────────────────────────────────────────

	/**
	 * Parse le fichier XML pointé par {@link #lien} et remplit {@link #sommetsObjet}.
	 * <p>
	 * Format attendu :
	 * <pre>
	 * &lt;graphe&gt;
	 *   &lt;sommet nom="A"&gt;
	 *     &lt;lien destination="B" distance="5"/&gt;
	 *   &lt;/sommet&gt;
	 * &lt;/graphe&gt;
	 * </pre>
	 * Sans effet si {@link #lien} est vide.
	 */
	public void lectureXML()
	{
		if (this.lien.isEmpty()) return;

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        builder = factory.newDocumentBuilder();
			Document               doc     = builder.parse(new File(this.lien));

			NodeList noeuds = doc.getElementsByTagName("sommet");

			for (int i = 0; i < noeuds.getLength(); i++)
			{
				Element sommetElt = (Element) noeuds.item(i);
				String  nom       = sommetElt.getAttribute("nom");

				// Éviter les doublons de sommets
				if (!this.sommets.contains(nom))
				{
					this.sommets.add(nom);
					this.sommetsObjet.add(new Sommet(nom));
				}

				int index = this.sommets.indexOf(nom);

				NodeList liensNodeList = sommetElt.getElementsByTagName("lien");
				for (int j = 0; j < liensNodeList.getLength(); j++)
				{
					Element lienElt     = (Element) liensNodeList.item(j);
					String  destination = lienElt.getAttribute("destination");
					int     distance    = Integer.parseInt(lienElt.getAttribute("distance"));

					// Mise à jour de la représentation textuelle
					this.document += nom + " --> " + destination + " " + distance + "\n";

					this.sommetsObjet.get(index).getLiens().add(new Lien(destination, distance));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Erreur lors de la lecture XML : " + e.getMessage());
		}
	}

	// ── Sauvegarde XML ────────────────────────────────────────────────────────

	/**
	 * Sauvegarde {@link #sommetsObjet} dans le fichier pointé par {@link #lien}.
	 * Crée les dossiers intermédiaires si nécessaire.
	 * <p>
	 * Le fichier est écrit avec une indentation de 4 espaces et l'encodage UTF-8.
	 */
	public void sauvegarderXML()
	{
		try
		{
			File fichier = new File(this.lien);
			File dossier = fichier.getParentFile();
			if (dossier != null && !dossier.exists()) dossier.mkdirs();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        builder = factory.newDocumentBuilder();
			Document               doc     = builder.newDocument();

			Element root = doc.createElement("graphe");
			doc.appendChild(root);

			for (Sommet sommet : this.sommetsObjet)
			{
				Element sommetElt = doc.createElement("sommet");
				sommetElt.setAttribute("nom", sommet.getNom());
				root.appendChild(sommetElt);

				for (Lien lien : sommet.getLiens())
				{
					Element lienElt = doc.createElement("lien");
					lienElt.setAttribute("destination", lien.getNom());
					lienElt.setAttribute("distance",    String.valueOf(lien.getDistance()));
					sommetElt.appendChild(lienElt);
				}
			}

			// Écriture formatée (indentation 4 espaces, UTF-8)
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,   "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(doc), new StreamResult(fichier));
		}
		catch (Exception e)
		{
			System.err.println("Erreur lors de la sauvegarde XML : " + e.getMessage());
			e.printStackTrace();
		}
	}

	// ── Algorithmes ───────────────────────────────────────────────────────────

	/**
	 * Active l'algorithme de chemin court demandé et lance le calcul.
	 * L'algorithme précédemment actif est remplacé.
	 *
	 * @param mode {@code "Dijkstra"} ou {@code "Bellman-Ford"}.
	 */
	public void Mode(String mode)
	{
		this.modeSelectionne = mode;

		if (mode.equals("Dijkstra"))
		{
			try
			{
				this.dijikstra = new Dijikstra(this.sommetsObjet);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (mode.equals("Bellman-Ford"))
		{
			this.bellmanFord = new BellmanFord(this.sommetsObjet);
		}
	}

	/**
	 * Retourne le plus court chemin calculé par l'algorithme actif,
	 * du premier sommet au dernier sommet de la liste.
	 *
	 * @return Liste ordonnée des noms de sommets (source → destination),
	 *         ou liste vide si aucun algorithme actif, liste vide ou destination invalide.
	 */
	public ArrayList<String> getCheminCourt()
	{
		if (this.sommetsObjet == null || this.sommetsObjet.isEmpty())
			return new ArrayList<>();

		String destination = this.sommetsObjet.get(this.sommetsObjet.size() - 1).getNom();
		if (destination == null || destination.trim().isEmpty())
			return new ArrayList<>();

		if (this.modeSelectionne.equals("Dijkstra") && this.dijikstra != null)
			return this.dijikstra.getChemin(destination);

		if (this.modeSelectionne.equals("Bellman-Ford") && this.bellmanFord != null)
			return this.bellmanFord.getChemin(destination);

		return new ArrayList<>();
	}

	// ── Accesseurs ────────────────────────────────────────────────────────────

	/**
	 * Retourne la liste des sommets chargés.
	 *
	 * @return Liste des sommets.
	 */
	public ArrayList<Sommet> getSommets()
	{
		return this.sommetsObjet;
	}

	/**
	 * Remplace la liste des sommets (utilisé après une édition manuelle).
	 *
	 * @param nouvellesSommets Nouvelle liste à utiliser.
	 */
	public void setSommets(ArrayList<Sommet> nouvellesSommets)
	{
		this.sommetsObjet = nouvellesSommets;
	}

	/**
	 * Retourne la représentation textuelle du graphe chargé.
	 *
	 * @return Chaîne « A --> B 5\n... ».
	 */
	public String getDocument()
	{
		return this.document;
	}

	/**
	 * Charge un nouveau fichier XML : vide les données actuelles puis relit le fichier.
	 *
	 * @param lien Chemin vers le fichier XML.
	 */
	public void setLien(String lien)
	{
		this.lien = lien;
		this.sommets.clear();
		this.sommetsObjet.clear();
		this.document = "";
		lectureXML();
	}

	/**
	 * Change le chemin du fichier de sauvegarde <b>sans</b> recharger les données.
	 * Utilisé pour « Enregistrer sous ».
	 *
	 * @param lien Nouveau chemin de fichier.
	 */
	public void setLienSeulement(String lien)
	{
		this.lien = lien;
	}
}
