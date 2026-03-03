package appli.metier;

import java.util.ArrayList;

public class BellmanFord
{
	private ArrayList<Sommet> cheminCourt;
	private int distance;
	private Lecture lecture;
	private ArrayList<Sommet> lstTest;

	public BellmanFord(Lecture lecture)
	{
		this.cheminCourt = new ArrayList<>();
		this.lecture = lecture;

		this.calculCheminCourt();
	}

	public BellmanFord(ArrayList<Sommet> lst)
	{
		this.cheminCourt = new ArrayList<>();
		this.lecture = null;

		this.lstTest = lst;

		this.calculCheminCourt();
	}


	public void calculCheminCourt()
	{
		if(this.lecture != null)
		{
			ArrayList<Sommet> sommets = this.lecture.getSommets();
		}
		else
		{
			ArrayList<Sommet> sommets = this.lstTest;
		}

		this.cheminCourt.add(sommets.get(0));

		for(Sommet c : this.cheminCourt)
		{
			if(c.getLiens().size() == 1)
			{
				Lien lien = c.getLiens().get(0);
				for(Sommet l : sommets)
				{
					if (lien.getNom().equals(c.getNom()) && ! this.cheminCourt.contains(lien))
					{
						this.cheminCourt.add(l);
					}
				}
			}
			else
			{
				ArrayList<Lien> lstLien = c.getLiens();
				int dist = lstLien.get(0).getDistance();
				String nomSommet = lstLien.get(0).getNom();

				for(Lien l : lstLien)
				{
					if(l.getDistance() < dist)
					{
						dist = l.getDistance();
						nomSommet = l.getNom();
					}


					for(Sommet s : sommets)
					{
						if (s.getNom().equals(c.getNom()) && !this.cheminCourt.contains(s))
						{
							this.cheminCourt.add(s);
						}
					}
				}
			}
		}
	}

	public ArrayList<Sommet> getCheminCourt()
	{
		return this.cheminCourt;
	}

	public static void main(String[] args)
	{
		// Création des sommets
		Sommet A = new Sommet("A");
		Sommet B = new Sommet("B");
		Sommet C = new Sommet("C");
		Sommet D = new Sommet("D");

		// Création des liens
		A.ajouterLien(new Lien("B", 5));
		A.ajouterLien(new Lien("C", 2));

		B.ajouterLien(new Lien("C", 1));
		B.ajouterLien(new Lien("D", 4));

		C.ajouterLien(new Lien("D", 7));

		// Liste des sommets
		ArrayList<Sommet> sommets = new ArrayList<>();
		sommets.add(A);
		sommets.add(B);
		sommets.add(C);
		sommets.add(D);

		// Affichage du graphe
		for (Sommet s : sommets)
		{
			System.out.println("Sommet " + s.getNom() + " : " + s.getLiens());
		}

		// Test BellmanFord
		BellmanFord bf = new BellmanFord();

		System.out.println("Test terminé.");
	}
}
