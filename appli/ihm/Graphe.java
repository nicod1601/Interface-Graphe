package appli.ihm;

import appli.Controleur;
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
import javax.swing.SwingUtilities;

/**
 * Panneau pour afficher et visualiser le graphe avec ses sommets et liens.
 * Gère le rendu graphique et l'animation des chemins.
 * @author Nicolas D. & Marta AN.
 */

public class Graphe extends JPanel
{
	private static final int DOT_STEP = 22;

	private Controleur        ctrl;
	private ArrayList<Sommet> sommetsObjet;
	private ArrayList<Cercle> sommets;
	private ArrayList<Lien>   liens;

	private int largeurGraphe = 0;
	private int hauteurGraphe = 0;

	/**
	 * Constructeur du panneau Graphe.
	 * @param ctrl Le contrôleur de l'application.
	 */
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
		this.sommets.clear();
		this.liens.clear();

		if (this.sommetsObjet == null || this.sommetsObjet.isEmpty())
			return;

		// ── Filtrer les sommets sans nom ──────────────────────────────────────
		ArrayList<Sommet> valides = new ArrayList<>();
		for (Sommet s : this.sommetsObjet)
			if (s.getNom() != null && !s.getNom().trim().isEmpty())
				valides.add(s);

		if (valides.isEmpty()) return;

		HashMap<String, Integer> niveaux = calculerNiveaux(valides);

		HashMap<Integer, Integer> tailleNiveau = new HashMap<>();
		for (Sommet s : valides)
		{
			int niv = niveaux.getOrDefault(s.getNom(), 0);
			tailleNiveau.put(niv, tailleNiveau.getOrDefault(niv, 0) + 1);
		}

		int nbNiveaux    = tailleNiveau.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
		int maxParNiveau = tailleNiveau.values().stream().mapToInt(i -> i).max().orElse(1);

		int espaceH = 150;
		int espaceV = 100;
		int rayon   = 20;

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

			this.sommets.add(new Cercle(x, y, rayon, nom));
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

	@Override
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

		int rayon  = 20;
		int margeX = rayon + 10;
		int margeY = rayon + 10;

		int offsetX = (getWidth()  - largeurGraphe) / 2 - margeX;
		int offsetY = (getHeight() - hauteurGraphe) / 2 - margeY;

		g2d.translate(offsetX, offsetY);

		for (Lien lien : this.liens)   lien.dessiner(g2d);
		for (Cercle cercle : this.sommets) cercle.dessiner(g2d);

		g2d.translate(-offsetX, -offsetY);
	}

	/**
	 * Actualise l'affichage du graphe en redessinant le panneau.
	 */
	public void actualiser() { repaint(); }

	/**
	 * Recharge les données des sommets depuis le contrôleur et reconstruit le graphe.
	 */
	public void fichier()
	{
		this.sommetsObjet = this.ctrl.getSommets();
		buildGraph();
		this.revalidate();
		this.repaint();
	}

	/**
	 * Sauvegarde la liste des sommets et met à jour le graphe.
	 * @param liste La nouvelle liste des sommets.
	 */
	public void sauvegarder(ArrayList<Sommet> liste)
	{
		this.sommetsObjet = liste;
		buildGraph();
		this.actualiser();
	}

	/**
	 * Affiche le chemin calculé en animant les sommets et liens sélectionnés.
	 * @param chemin La liste des noms des sommets formant le chemin.
	 */
	public void voirChemin(ArrayList<String> chemin)
	{
		for (Cercle c : this.sommets) { c.setSelectionne(false); c.setVisite(false); }
		for (Lien l : this.liens)       l.setEnChemin(false);
		repaint();

		new Thread(() -> {
			for (int i = 0; i < chemin.size(); i++)
			{
				final int index = i;
				SwingUtilities.invokeLater(() -> {
					String nomActuel = chemin.get(index);
					for (Cercle c : this.sommets)
						if (c.getNom().equals(nomActuel)) c.setSelectionne(true);

					if (index < chemin.size() - 1)
					{
						String de = chemin.get(index), vers = chemin.get(index + 1);
						for (Lien l : this.liens)
							if (l.getSource().equals(de) && l.getSommet().equals(vers))
								l.setEnChemin(true);
					}
					repaint();
				});

				try
				{
					Thread.sleep(1500);
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
					return;
				}
			}
		}).start();
	}
}