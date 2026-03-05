package appli.ihm;

import appli.ihm.dessin.*;
import appli.metier.Sommet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;

public class GrapheCopie extends JPanel
{
	private Edit                           edit;
	private ArrayList<Cercle>              sommets;
	private ArrayList<appli.ihm.dessin.Lien> liens;

	private int largeurGraphe = 0;
	private int hauteurGraphe = 0;

	public GrapheCopie(Edit edit)
	{
		this.edit    = edit;
		this.sommets = new ArrayList<>();
		this.liens   = new ArrayList<>();

		this.setPreferredSize(new java.awt.Dimension(600, 300));
		this.setBackground(Color.WHITE);
	}

	public void actualiser()
	{
		this.sommets.clear();
		this.liens.clear();

		ArrayList<Sommet> sommetsMetier = this.edit.getSommets();

		if (sommetsMetier == null || sommetsMetier.isEmpty())
		{
			repaint();
			return;
		}

		// ── 1. Niveaux BFS ───────────────────────────────────────────────────
		HashMap<String, Integer> niveaux = calculerNiveaux(sommetsMetier);

		// ── 2. Taille par niveau ─────────────────────────────────────────────
		HashMap<Integer, Integer> tailleNiveau = new HashMap<>();
		for (Sommet s : sommetsMetier)
		{
			int niv = niveaux.getOrDefault(s.getNom(), 0);
			tailleNiveau.put(niv, tailleNiveau.getOrDefault(niv, 0) + 1);
		}

		int nbNiveaux    = tailleNiveau.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
		int maxParNiveau = tailleNiveau.values().stream().mapToInt(i -> i).max().orElse(1);

		int espaceH = 100;
		int espaceV = 60;

		this.largeurGraphe = (nbNiveaux - 1) * espaceH;
		this.hauteurGraphe = (maxParNiveau - 1) * espaceV;

		// ── 3. Placement centré verticalement par niveau ─────────────────────
		HashMap<Integer, Integer> compteurParNiveau = new HashMap<>();

		for (int cpt = 0; cpt < sommetsMetier.size(); cpt++)
		{
			Sommet s      = sommetsMetier.get(cpt);
			String nom    = s.getNom();
			int    niveau = niveaux.getOrDefault(nom, 0);
			int    posV   = compteurParNiveau.getOrDefault(niveau, 0);
			compteurParNiveau.put(niveau, posV + 1);

			int nbDansNiveau  = tailleNiveau.getOrDefault(niveau, 1);
			int hauteurNiveau = (nbDansNiveau - 1) * espaceV;
			int offsetV       = (this.hauteurGraphe - hauteurNiveau) / 2;

			int x = niveau * espaceH;
			int y = offsetV + posV * espaceV;

			this.sommets.add(new Cercle(x, y, 15, nom));
		}

		// ── 4. Liens ─────────────────────────────────────────────────────────
		for (int cpt = 0; cpt < sommetsMetier.size(); cpt++)
		{
			Sommet sommetMetier = sommetsMetier.get(cpt);
			for (appli.metier.Lien lienMetier : sommetMetier.getLiens())
			{
				if (lienMetier.getNom() == null || lienMetier.getNom().trim().isEmpty())
					continue;

				int indexCible = -1;
				for (int i = 0; i < sommetsMetier.size(); i++)
				{
					if (sommetsMetier.get(i).getNom().equals(lienMetier.getNom()))
					{
						indexCible = i;
						break;
					}
				}

				if (indexCible != -1)
				{
					this.liens.add(new appli.ihm.dessin.Lien(
						this.sommets.get(cpt),
						this.sommets.get(indexCible),
						lienMetier.getDistance()
					));
				}
			}
		}

		repaint();
	}

	private HashMap<String, Integer> calculerNiveaux(ArrayList<Sommet> sommetsMetier)
	{
		HashMap<String, Integer> niveaux = new HashMap<>();
		HashSet<String>          visites = new HashSet<>();

		if (sommetsMetier.isEmpty()) return niveaux;

		Sommet        racine = sommetsMetier.get(0);
		Queue<String> file   = new LinkedList<>();
		file.add(racine.getNom());
		niveaux.put(racine.getNom(), 0);
		visites.add(racine.getNom());

		while (!file.isEmpty())
		{
			String nomCourant    = file.poll();
			int    niveauCourant = niveaux.get(nomCourant);

			Sommet sommetCourant = null;
			for (Sommet s : sommetsMetier)
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

		for (Sommet s : sommetsMetier)
			if (!niveaux.containsKey(s.getNom()))
				niveaux.put(s.getNom(), 0);

		return niveaux;
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							 RenderingHints.VALUE_ANTIALIAS_ON);

		// ── Fond dot-grid ─────────────────────────────────────────────────────
		g2d.setColor(new Color(10, 13, 20));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(new Color(38, 52, 78));
		for (int x = 22; x < getWidth(); x += 22)
			for (int y = 22; y < getHeight(); y += 22)
				g2d.fillOval(x - 1, y - 1, 2, 2);

		if (this.sommets.isEmpty()) return;

		int rayon  = 15;
		int margeX = rayon + 10;
		int margeY = rayon + 10;

		int offsetX = (getWidth()  - largeurGraphe) / 2 - margeX;
		int offsetY = (getHeight() - hauteurGraphe) / 2 - margeY;

		g2d.translate(offsetX, offsetY);

		for (appli.ihm.dessin.Lien lien : this.liens)
			lien.dessiner(g2d);

		for (Cercle cercle : this.sommets)
			cercle.dessiner(g2d);

		g2d.translate(-offsetX, -offsetY);
	}
}
