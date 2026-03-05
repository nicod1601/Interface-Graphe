package appli.ihm;

import appli.Controleur;
import appli.ihm.dessin.*;
import appli.metier.Sommet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;

public class Graphe extends JPanel
{
	private Controleur ctrl;

	private ArrayList<Sommet>              sommetsObjet;
	private ArrayList<Cercle>              sommets;
	private ArrayList<appli.ihm.dessin.Lien> liens;

	public Graphe(Controleur ctrl)
	{
		this.ctrl         = ctrl;
		this.sommets      = new ArrayList<>();
		this.liens        = new ArrayList<>();
		this.sommetsObjet = this.ctrl.getSommets();

		buildGraph();
	}

	private void buildGraph()
	{
		if (this.sommets == null) this.sommets = new ArrayList<>();
		if (this.liens   == null) this.liens   = new ArrayList<>();
		this.sommets.clear();
		this.liens.clear();

		if (this.sommetsObjet == null || this.sommetsObjet.isEmpty())
			return;

		// ── 1. Calcul des niveaux (BFS) ──────────────────────────────────────
		HashMap<String, Integer> niveaux = calculerNiveaux();

		// ── 2. Comptage des nœuds par niveau pour répartition verticale ──────
		HashMap<Integer, Integer> tailleNiveau = new HashMap<>();
		for (Sommet s : this.sommetsObjet)
		{
			int niv = niveaux.getOrDefault(s.getNom(), 0);
			tailleNiveau.put(niv, tailleNiveau.getOrDefault(niv, 0) + 1);
		}

		int nbNiveaux    = tailleNiveau.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
		int maxParNiveau = tailleNiveau.values().stream().mapToInt(i -> i).max().orElse(1);

		int espaceH = 150;
		int espaceV = 100;
		int rayon   = 20;

		// ── 3. Taille totale du graphe ────────────────────────────────────────
		int largeurGraphe = (nbNiveaux - 1) * espaceH;
		int hauteurGraphe = (maxParNiveau - 1) * espaceV;

		// ── 4. Calcul du décalage pour centrer dans le panel ─────────────────
		//    (fait au moment du dessin via getWidth/getHeight)
		HashMap<Integer, Integer> compteurParNiveau = new HashMap<>();
		HashMap<String, int[]>    positions         = new HashMap<>();

		for (int cpt = 0; cpt < this.sommetsObjet.size(); cpt++)
		{
			Sommet s        = this.sommetsObjet.get(cpt);
			String nom      = s.getNom();
			int    niveau   = niveaux.getOrDefault(nom, 0);
			int    posV     = compteurParNiveau.getOrDefault(niveau, 0);
			compteurParNiveau.put(niveau, posV + 1);

			int nbDansNiveau = tailleNiveau.getOrDefault(niveau, 1);

			// Centre vertical du niveau
			int hauteurNiveau = (nbDansNiveau - 1) * espaceV;
			int offsetV       = (hauteurGraphe - hauteurNiveau) / 2;

			int x = niveau * espaceH;
			int y = offsetV + posV * espaceV;

			positions.put(nom, new int[]{x, y});
			Cercle cercle = new Cercle(x, y, rayon, nom);
			this.sommets.add(cercle);
		}

		// ── 5. Création des liens ─────────────────────────────────────────────
		for (int cpt = 0; cpt < this.sommetsObjet.size(); cpt++)
		{
			Sommet sommetMetier = this.sommetsObjet.get(cpt);
			for (appli.metier.Lien lienMetier : sommetMetier.getLiens())
			{
				if (lienMetier.getNom() == null || lienMetier.getNom().trim().isEmpty())
					continue;

				int indexCible = -1;
				for (int i = 0; i < this.sommetsObjet.size(); i++)
				{
					if (this.sommetsObjet.get(i).getNom().equals(lienMetier.getNom()))
					{
						indexCible = i;
						break;
					}
				}

				if (indexCible != -1)
				{
					appli.ihm.dessin.Lien lienDessin = new appli.ihm.dessin.Lien(
						this.sommets.get(cpt),
						this.sommets.get(indexCible),
						lienMetier.getDistance()
					);
					this.liens.add(lienDessin);
				}
			}
		}

		// Stocker la taille du graphe pour le centrage au paintComponent
		this.largeurGraphe = largeurGraphe;
		this.hauteurGraphe = hauteurGraphe;
	}

	// Taille réelle du graphe (sans marges), utilisée pour centrer
	private int largeurGraphe = 0;
	private int hauteurGraphe = 0;

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (this.sommets.isEmpty()) return;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);

		int rayon  = 20;
		int margeX = rayon + 10;
		int margeY = rayon + 10;

		// Décalage pour centrer dans le panel courant
		int offsetX = (getWidth()  - largeurGraphe) / 2 - margeX;
		int offsetY = (getHeight() - hauteurGraphe) / 2 - margeY;

		// Translater le contexte graphique
		g2d.translate(offsetX, offsetY);

		for (appli.ihm.dessin.Lien lien : this.liens)
			lien.dessiner(g2d);

		for (Cercle cercle : this.sommets)
			cercle.dessiner(g2d);

		g2d.translate(-offsetX, -offsetY);
	}

	private HashMap<String, Integer> calculerNiveaux()
	{
		HashMap<String, Integer> niveaux = new HashMap<>();
		HashSet<String>          visites = new HashSet<>();

		if (this.sommetsObjet.isEmpty()) return niveaux;

		Sommet        racine = this.sommetsObjet.get(0);
		Queue<String> file   = new LinkedList<>();
		file.add(racine.getNom());
		niveaux.put(racine.getNom(), 0);
		visites.add(racine.getNom());

		while (!file.isEmpty())
		{
			String nomCourant    = file.poll();
			int    niveauCourant = niveaux.get(nomCourant);

			Sommet sommetCourant = null;
			for (Sommet s : this.sommetsObjet)
			{
				if (s.getNom().equals(nomCourant)) { sommetCourant = s; break; }
			}

			if (sommetCourant != null)
			{
				for (appli.metier.Lien lien : sommetCourant.getLiens())
				{
					String nomVoisin = lien.getNom();
					if (nomVoisin != null && !nomVoisin.trim().isEmpty() && !visites.contains(nomVoisin))
					{
						niveaux.put(nomVoisin, niveauCourant + 1);
						visites.add(nomVoisin);
						file.add(nomVoisin);
					}
				}
			}
		}

		for (Sommet s : this.sommetsObjet)
			if (!niveaux.containsKey(s.getNom()))
				niveaux.put(s.getNom(), 0);

		return niveaux;
	}

	public void actualiser()  { repaint(); }

	public void fichier()
	{
		this.sommetsObjet = this.ctrl.getSommets();
		buildGraph();
		this.revalidate();
		this.repaint();
	}

	public void sauvegarder(ArrayList<Sommet> liste)
	{
		this.sommetsObjet = liste;
		buildGraph();
		this.actualiser();
	}

	public void voirChemin(ArrayList<String> chemin)
	{
		for (Cercle c : this.sommets)              { c.setSelectionne(false); c.setVisite(false); }
		for (appli.ihm.dessin.Lien l : this.liens)   l.setEnChemin(false);

		for (Cercle c : this.sommets)
			if (chemin.contains(c.getNom())) c.setSelectionne(true);

		for (int i = 0; i < chemin.size() - 1; i++)
		{
			String de = chemin.get(i), vers = chemin.get(i + 1);
			for (appli.ihm.dessin.Lien l : this.liens)
				if (l.getSource().equals(de) && l.getSommet().equals(vers))
					l.setEnChemin(true);
		}
		repaint();
	}
}