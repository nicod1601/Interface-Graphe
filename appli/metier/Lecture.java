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

public class Lecture
{
	private String lien;
	private ArrayList<String> sommets;
	private ArrayList<Sommet> sommetsObjet;
	private String document;

	private String modeSelectionne;
	private Dijikstra dijikstra;
	private BellmanFord bellmanFord;

	public Lecture()
	{
		this.sommets         = new ArrayList<>();
		this.sommetsObjet    = new ArrayList<>();
		this.lien            = "";
		this.document        = "";
		this.dijikstra       = null;
		this.bellmanFord     = null;
		this.modeSelectionne = "";

		this.lectureXML();
	}

	public void lectureXML()
	{
		if (this.lien.equals(""))
		{
			return;
		}

		File file = new File(this.lien);

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			NodeList sommetsNodeList = doc.getElementsByTagName("sommet");

			for (int cpt = 0; cpt < sommetsNodeList.getLength(); cpt++)
			{
				Element sommetElt = (Element) sommetsNodeList.item(cpt);

				String nom = sommetElt.getAttribute("nom");

				if (!this.sommets.contains(nom))
				{
					this.sommets.add(nom);
					this.sommetsObjet.add(new Sommet(nom));
				}

				int index = this.sommets.indexOf(nom);

				NodeList liensNodeList = sommetElt.getElementsByTagName("lien");

				for (int i = 0; i < liensNodeList.getLength(); i++)
				{
					Element lienElt = (Element) liensNodeList.item(i);

					String destination = lienElt.getAttribute("destination");
					int distance = Integer.parseInt(lienElt.getAttribute("distance"));

					this.document = this.document + nom + " --> " + destination + " " + distance + "\n";

					this.sommetsObjet.get(index).getLiens().add(new Lien(destination, distance));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Erreur lors de la lecture du fichier XML : " + e.getMessage());
		}
	}

	public void sauvegarderXML()
	{
		try
		{
			File dir = new File(this.lien);

			File dossier = dir.getParentFile();
			if (dossier != null && !dossier.exists())
			{
				dossier.mkdirs();
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

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
					lienElt.setAttribute("distance", String.valueOf(lien.getDistance()));
					sommetElt.appendChild(lienElt);
				}
			}

			// Écriture formatée
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(new File(this.lien)));


			//information
			System.out.println("Fichier XML sauvegardé avec succès : " + this.lien);
			System.out.println("Contenu du graphe :");
			for (Sommet s : this.sommetsObjet)
			{
				System.out.println("Sommet : " + s.getNom());
				for (Lien l : s.getLiens())
				{
					System.out.println("  Lien : " + l.getNom() + " (" + l.getDistance() + ")");
				}
			}
		}
		catch (Exception ex)
		{
			System.err.println("Erreur lors de la sauvegarde du fichier XML : " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}


	public ArrayList<Sommet> getSommets()
	{
		return sommetsObjet;
	}

	public void setSommets(ArrayList<Sommet> nouvellesSommets)
	{
		this.sommetsObjet = nouvellesSommets;

		System.out.println("Sommets mis à jour :");
		for (Sommet s : this.sommetsObjet) {
			System.out.println("  - " + s.getNom());
			for (Lien l : s.getLiens()) {
				System.out.println("    - " + l.getNom() + " (" + l.getDistance() + ")");
			}
		}

		System.out.println("Sommets mis à jour dans Lecture.");
		System.out.println("Lien : " + this.lien);
	}

	public String getDocument()
	{
		return document;
	}

	public void setLienSeulement(String lien)
	{
		this.lien = lien;
	}

	public void setLien(String lien)
	{
		this.lien = lien;
		this.sommets.clear();
		this.sommetsObjet.clear();
		this.document = "";
		this.lectureXML();
	}

	public void Mode(String mode)
	{
		System.out.println("Mode : " + mode);
		System.out.println("DEBUG: Sommets objet = " + this.sommetsObjet);
		System.out.println("DEBUG: Sommets size = " + (this.sommetsObjet != null ? this.sommetsObjet.size() : 0));

		this.modeSelectionne = mode;

		if (mode.equals("Dijkstra"))
		{
			System.out.println("DEBUG: Creating Dijikstra instance...");
			try {
				this.dijikstra = new Dijikstra(this.sommetsObjet);
				System.out.println("DEBUG: Dijikstra initialized successfully");
			} catch (Exception e) {
				System.out.println("ERROR: Failed to initialize Dijikstra: " + e.getMessage());
				e.printStackTrace();
			}
		}
		else if (mode.equals("Bellman-Ford"))
		{
			System.out.println("DEBUG: Creating BellmanFord instance...");
			this.bellmanFord = new BellmanFord(this.sommetsObjet);
			System.out.println("DEBUG: BellmanFord initialized successfully");
		}
	}

	public ArrayList<String> getCheminCourt()
	{
		if (this.sommetsObjet == null || this.sommetsObjet.isEmpty())
			return new ArrayList<>();

		String destination = this.sommetsObjet.get(this.sommetsObjet.size() - 1).getNom();

		if (destination == null || destination.trim().isEmpty())
			return new ArrayList<>();

		if (this.modeSelectionne.equals("Dijkstra") && this.dijikstra != null)
		{
			ArrayList<String> chemin = this.dijikstra.getChemin(destination);
			System.out.println("Dijkstra chemin: " + chemin);
			return chemin;
		}
		else if (this.modeSelectionne.equals("Bellman-Ford") && this.bellmanFord != null)
		{
			ArrayList<String> chemin = this.bellmanFord.getChemin(destination);
			System.out.println("Bellman-Ford chemin: " + chemin);
			return chemin;
		}
		else
		{
			System.out.println("Pas d'algorithme sélectionné ou initialisé");
			return new ArrayList<>();
		}
	}
}
