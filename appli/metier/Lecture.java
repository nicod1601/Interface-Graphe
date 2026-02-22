package appli.metier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class Lecture
{
	private String lien;
	private ArrayList<String> sommets;
	private ArrayList<Sommet> sommetsObjet;
	private String document;

	public Lecture()
	{
		this.sommets      = new ArrayList<>();
		this.sommetsObjet = new ArrayList<>();
		this.lien         = "";
		this.document     = "";

		this.lectureXML();
	}

	public void lectureXML()
	{

		if(this.lien.equals(""))
		{
			return;
		}
		else
		{
			File file = new File(this.lien);
			
			try
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder        builder = factory.newDocumentBuilder();
				Document 			   doc     = (Document) builder.parse(file);

				NodeList sommetsNodeList = doc.getElementsByTagName("sommet");
				
				for(int cpt = 0; cpt < sommetsNodeList.getLength(); cpt++)
				{
					Element elt = (Element) sommetsNodeList.item(cpt);

					String nom        = elt.getAttribute("nom");
					String lien       = elt.getAttribute("lien");
					int distance      = Integer.parseInt(elt.getAttribute("distance"));

					this.document = this.document + nom + " --> " + lien + " " + distance + "\n";


					if (this.sommets.contains(nom) == false)
					{
						this.sommets.add(nom);
						this.sommetsObjet.add(new Sommet(nom));

						for (int cpt1 = 0; cpt1 < this.sommets.size(); cpt1++)
						{
							if (this.sommets.get(cpt1).equals(nom))
							{

								this.sommetsObjet.get(cpt1).getLiens().add(new Lien(lien, distance));
								break;
							}
						}

					} 
					else
					{
						for (int cpt2 = 0; cpt2 < this.sommets.size(); cpt2++)
						{
							if (this.sommets.get(cpt2).equals(nom))
							{
								this.sommetsObjet.get(cpt2).getLiens().add(new Lien(lien, distance));
							}
						}
					}

				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Erreur lors de la lecture du fichier XML : " + e.getMessage());
				return;
			}
		}
	}


	public ArrayList<Sommet> getSommets()
	{
		return sommetsObjet;
	}

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
		this.lectureXML();
	}
}
