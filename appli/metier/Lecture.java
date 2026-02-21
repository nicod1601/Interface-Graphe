package appli.metier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lecture
{
	private String lien;
	private ArrayList<String> sommets;
	private ArrayList<Sommet> sommetsObjet;
	private String document;

	public Lecture()
	{
		this.sommets = new ArrayList<>();
		this.sommetsObjet = new ArrayList<>();
		this.lien = "";
		this.document = "";

		this.lecture();
	}

	public void lecture()
	{
		if(this.lien.equals(""))
		{
			return;
		}

		try
		{
			Scanner sc = new Scanner(new File(this.lien));

			while (sc.hasNextLine())
			{
				String line = sc.nextLine();

				this.document += line + "\n";

				String[] parts = line.split("\\|");

				String nom = parts[0].trim();
				String lienSommet = parts[1].trim();
				int distance = Integer.parseInt(parts[2].trim());

				// ajout du sommet à la liste des sommets
				if (this.sommets.contains(nom) == false)
				{
					this.sommets.add(nom);
					this.sommetsObjet.add(new Sommet(nom));

					for (int cpt = 0; cpt < this.sommets.size(); cpt++)
					{
						if (this.sommets.get(cpt).equals(nom))
						{

							this.sommetsObjet.get(cpt).getLiens().add(new Lien(lienSommet, distance));
						}
					}

				} 
				else
				{
					for (int cpt = 0; cpt < this.sommets.size(); cpt++)
					{
						if (this.sommets.get(cpt).equals(nom))
						{
							this.sommetsObjet.get(cpt).getLiens().add(new Lien(lienSommet, distance));
						}
					}
				}

			}
			

		} catch (FileNotFoundException e) {
		}
	}

	public ArrayList<Sommet> getSommets()
	{
		return sommetsObjet;
	}

	/**
	 * Remplace la liste interne des sommets par une nouvelle liste fournie.
	 * Utilisé pour mettre à jour les données après édition.
	 */
	public void setSommets(ArrayList<Sommet> nouvellesSommets)
	{
		if (nouvellesSommets == null)
			this.sommetsObjet = new ArrayList<>();
		else
			this.sommetsObjet = nouvellesSommets;

	}

	public String getDocument()
	{
		return document;
	}

	public void setLien(String lien)
	{
		this.lien = lien;
		this.lecture();
	}
}
