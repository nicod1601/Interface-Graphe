package appli.ihm;

import appli.ihm.dessin.Cercle;
import appli.ihm.dessin.Lien;
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

public class GrapheCopie extends JPanel
{
	private static final int DOT_STEP = 22;

	private Edit              edit;
	private ArrayList<Cercle> sommets;
	private ArrayList<Lien>   liens;

	private int largeurGraphe = 0;
	private int hauteurGraphe = 0;

	public GrapheCopie(Edit edit)
	{
		this.edit    = edit;
		this.sommets = new ArrayList<>();
		this.liens   = new ArrayList<>();
		this.setPreferredSize(new java.awt.Dimension(600, 300));
	}

	public void actualiser()
	{
		this.sommets.clear();
		this.liens.clear();

		ArrayList<Sommet> tous = this.edit.getSommets();
		if (tous == null || tous.isEmpty()) { repaint(); return; }

		// ── Filtrer les sommets sans nom ──────────────────────────────────────
		ArrayList<Sommet> valides = new ArrayList<>();
		for (Sommet s : tous)
			if (s.getNom() != null && !s.getNom().trim().isEmpty())
				valides.add(s);

		if (valides.isEmpty()) { repaint(); return; }

		HashMap<String, Integer> niveaux = calculerNiveaux(valides);

		HashMap<Integer, Integer> tailleNiveau = new HashMap<>();
		for (Sommet s : valides)
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

		HashMap<Integer, Integer> compteurParNiveau = new HashMap<>();

		for (int cpt = 0; cpt < valides.size(); cpt++)
		{
			Sommet s      = valides.get(cpt);
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

		for (int cpt = 0; cpt < valides.size(); cpt++)
		{
			Sommet sommetMetier = valides.get(cpt);
			for (appli.metier.Lien lienMetier : sommetMetier.getLiens())
			{
				String nomCible = lienMetier.getNom();
				// Ignorer les liens vers un sommet sans nom
				if (nomCible == null || nomCible.trim().isEmpty()) continue;

				int indexCible = -1;
				for (int i = 0; i < valides.size(); i++)
					if (valides.get(i).getNom().equals(nomCible)) { indexCible = i; break; }

				if (indexCible != -1)
					this.liens.add(new Lien(this.sommets.get(cpt), this.sommets.get(indexCible), lienMetier.getDistance()));
			}
		}

		repaint();
	}

	private HashMap<String, Integer> calculerNiveaux(ArrayList<Sommet> valides)
	{
		HashMap<String, Integer> niveaux = new HashMap<>();
		HashSet<String>          visites = new HashSet<>();

		Sommet        racine = valides.get(0);
		Queue<String> file   = new LinkedList<>();
		file.add(racine.getNom());
		niveaux.put(racine.getNom(), 0);
		visites.add(racine.getNom());

		while (!file.isEmpty())
		{
			String nomCourant    = file.poll();
			int    niveauCourant = niveaux.get(nomCourant);

			Sommet sommetCourant = null;
			for (Sommet s : valides)
				if (s.getNom().equals(nomCourant)) { sommetCourant = s; break; }

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

		for (Sommet s : valides)
			if (!niveaux.containsKey(s.getNom()))
				niveaux.put(s.getNom(), 0);

		return niveaux;
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// ── Fond dot-grid ─────────────────────────────────────────────────────
		g2d.setColor(Theme.BACKGROUND);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Theme.BORDER);
		for (int x = DOT_STEP; x < getWidth(); x += DOT_STEP)
			for (int y = DOT_STEP; y < getHeight(); y += DOT_STEP)
				g2d.fillOval(x - 1, y - 1, 2, 2);

		if (this.sommets.isEmpty()) return;

		int rayon  = 15;
		int margeX = rayon + 10;
		int margeY = rayon + 10;

		int offsetX = (getWidth()  - largeurGraphe) / 2 - margeX;
		int offsetY = (getHeight() - hauteurGraphe) / 2 - margeY;

		g2d.translate(offsetX, offsetY);

		for (Lien lien : this.liens)       lien.dessiner(g2d);
		for (Cercle cercle : this.sommets) cercle.dessiner(g2d);

		g2d.translate(-offsetX, -offsetY);
	}
}