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
	private Edit edit;
	private ArrayList<Cercle> sommets;
	private ArrayList<appli.ihm.dessin.Lien> liens;

	public GrapheCopie(Edit edit)
	{
		this.edit = edit;
		this.sommets = new ArrayList<>();
		this.liens = new ArrayList<>();
		
		// Définir une taille préférée et une couleur de fond
		this.setPreferredSize(new java.awt.Dimension(600, 300));
		this.setBackground(Color.WHITE);
	}

	public void actualiser()
	{
		
		// Vider les listes
		this.sommets.clear();
		this.liens.clear();
		
		ArrayList<Sommet> sommetsMetier = this.edit.getSommets();
		
		if(sommetsMetier != null && !sommetsMetier.isEmpty())
		{
			
			// Calculer les niveaux de profondeur pour chaque sommet (BFS)
			HashMap<String, Integer> niveaux = calculerNiveaux(sommetsMetier);
			HashMap<Integer, Integer> compteurParNiveau = new HashMap<>();
			
			int espaceHorizontal = 100;  // Espace entre colonnes
			int espaceVertical = 60;     // Espace entre lignes
			int margeGauche = 50;
			int margeHaut = 50;
			
			// Création des cercles pour chaque sommet
			for(int cpt = 0; cpt < sommetsMetier.size(); cpt++)
			{
				Sommet sommetMetier = sommetsMetier.get(cpt);
				String nomSommet = sommetMetier.getNom();
				
				// Récupérer le niveau de profondeur
				int niveau = niveaux.getOrDefault(nomSommet, 0);
				
				// Compter combien de sommets au même niveau
				int positionDansNiveau = compteurParNiveau.getOrDefault(niveau, 0);
				compteurParNiveau.put(niveau, positionDansNiveau + 1);
				
				// Calculer les coordonnées
				int x = margeGauche + (niveau * espaceHorizontal);
				int y = margeHaut + (positionDansNiveau * espaceVertical);
				
				Cercle cercle = new Cercle(x, y, 15, nomSommet);
				this.sommets.add(cercle);
			}

			// Création des liens entre sommets
			for(int cpt = 0; cpt < sommetsMetier.size(); cpt++)
			{
				Sommet sommetMetier = sommetsMetier.get(cpt);
				
				for(appli.metier.Lien lienMetier : sommetMetier.getLiens())
				{
					// Ignorer les liens vides
					if(lienMetier.getNom() == null || lienMetier.getNom().trim().isEmpty())
						continue;
					
					int indexCible = -1;
					for(int i = 0; i < sommetsMetier.size(); i++)
					{
						if(sommetsMetier.get(i).getNom().equals(lienMetier.getNom()))
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
		
		// Forcer le redessin
		this.repaint();
	}

	/**
	 * Calcule le niveau de profondeur de chaque sommet avec un parcours BFS
	 */
	private HashMap<String, Integer> calculerNiveaux(ArrayList<Sommet> sommetsMetier)
	{
		HashMap<String, Integer> niveaux = new HashMap<>();
		HashSet<String> visites = new HashSet<>();
		
		if(sommetsMetier.isEmpty())
			return niveaux;
		
		// Partir du premier sommet comme racine
		Sommet racine = sommetsMetier.get(0);
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
			for(Sommet s : sommetsMetier)
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
		for(Sommet s : sommetsMetier)
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
		
		// Dessiner les liens
		for(appli.ihm.dessin.Lien lien : this.liens)
		{
			lien.dessiner(g2d);
		}
		
		// Dessiner les cercles
		for(Cercle cercle : this.sommets)
		{
			cercle.dessiner(g2d);
		}
	}
}