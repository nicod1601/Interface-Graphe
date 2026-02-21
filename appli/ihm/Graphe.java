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

	private ArrayList<Sommet> sommetsObjet;
	private ArrayList<Cercle> sommets;
	private ArrayList<appli.ihm.dessin.Lien> liens;


	public Graphe(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.sommets = new ArrayList<>();
		this.liens = new ArrayList<>();
		this.sommetsObjet = this.ctrl.getSommets();

		/* Construire la représentation graphique à partir des données */
		buildGraph();
	}

	/**
	 * Construit les cercles et liens à partir de `sommetsObjet`.
	 */
	private void buildGraph()
	{
		// Réinitialiser les listes de dessins
		if(this.sommets == null) this.sommets = new ArrayList<>();
		if(this.liens == null) this.liens = new ArrayList<>();
		this.sommets.clear();
		this.liens.clear();

		if(this.sommetsObjet == null || this.sommetsObjet.isEmpty())
			return;

		// Calculer les niveaux de profondeur pour chaque sommet (BFS)
		HashMap<String, Integer> niveaux = calculerNiveaux();
		HashMap<Integer, Integer> compteurParNiveau = new HashMap<>();

		int espaceHorizontal = 150;
		int espaceVertical = 100;
		int margeGauche = 100;
		int margeHaut = 100;

		// Création des cercles pour chaque sommet
		for(int cpt = 0; cpt < this.sommetsObjet.size(); cpt++)
		{
			Sommet sommetMetier = this.sommetsObjet.get(cpt);
			String nomSommet = sommetMetier.getNom();

			// Récupérer le niveau de profondeur
			int niveau = niveaux.getOrDefault(nomSommet, 0);

			// Compter combien de sommets au même niveau
			int positionDansNiveau = compteurParNiveau.getOrDefault(niveau, 0);
			compteurParNiveau.put(niveau, positionDansNiveau + 1);

			// Calculer les coordonnées
			int x = margeGauche + (niveau * espaceHorizontal);
			int y = margeHaut + (positionDansNiveau * espaceVertical);

			Cercle cercle = new Cercle(x, y, 20, nomSommet);
			this.sommets.add(cercle);
		}

		// Création des liens entre sommets
		for(int cpt = 0; cpt < this.sommetsObjet.size(); cpt++)
		{
			Sommet sommetMetier = this.sommetsObjet.get(cpt);

			for(appli.metier.Lien lienMetier : sommetMetier.getLiens())
			{
				// Ignorer les liens vides
				if(lienMetier.getNom() == null || lienMetier.getNom().trim().isEmpty())
					continue;

				int indexCible = -1;
				for(int i = 0; i < this.sommetsObjet.size(); i++)
				{
					if(this.sommetsObjet.get(i).getNom().equals(lienMetier.getNom()))
					{
						indexCible = i;
						break;
					}
				}

				if(indexCible != -1)
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
	}

	/**
	 * Calcule le niveau de profondeur de chaque sommet avec un parcours BFS
	 */
	private HashMap<String, Integer> calculerNiveaux()
	{
		HashMap<String, Integer> niveaux = new HashMap<>();
		HashSet<String> visites = new HashSet<>();
		
		if(this.sommetsObjet.isEmpty())
			return niveaux;
		
		// Partir du premier sommet comme racine
		Sommet racine = this.sommetsObjet.get(0);
		Queue<String> file = new LinkedList<>();
		file.add(racine.getNom());
		niveaux.put(racine.getNom(), 0);
		visites.add(racine.getNom());
		
		while(!file.isEmpty())
		{
			String nomCourant = file.poll();
			int niveauCourant = niveaux.get(nomCourant);
			
			// Trouver le sommet correspondant
			Sommet sommetCourant = null;
			for(Sommet s : this.sommetsObjet)
			{
				if(s.getNom().equals(nomCourant))
				{
					sommetCourant = s;
					break;
				}
			}
			
			if(sommetCourant != null)
			{
				// Parcourir les voisins
				for(appli.metier.Lien lien : sommetCourant.getLiens())
				{
					String nomVoisin = lien.getNom();
					if(nomVoisin != null && !nomVoisin.trim().isEmpty() && !visites.contains(nomVoisin))
					{
						niveaux.put(nomVoisin, niveauCourant + 1);
						visites.add(nomVoisin);
						file.add(nomVoisin);
					}
				}
			}
		}
		
		// Sommets non visités (graphe non connexe)
		for(Sommet s : this.sommetsObjet)
		{
			if(!niveaux.containsKey(s.getNom()))
			{
				niveaux.put(s.getNom(), 0);
			}
		}
		
		return niveaux;
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		

		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
							RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(appli.ihm.dessin.Lien lien : this.liens)
		{
			lien.dessiner(g2d);
		}
		
		for(Cercle cercle : this.sommets)
		{
			cercle.dessiner(g2d);
		}
	}
		
	/**
	 * Méthode pour rafraîchir l'affichage après modification
	 */
	public void actualiser()
	{
		repaint();
	}

	public void fichier()
	{
		this.sommetsObjet = this.ctrl.getSommets();

		// Reconstruire la représentation graphique puis rafraîchir
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
}