package appli;

import appli.ihm.Appli;
import appli.metier.Lecture;
import appli.metier.Sommet;
import java.util.ArrayList;

public class Controleur 
{
	private Lecture lecture;
	private Appli appli;

	public Controleur()
	{
		this.lecture = new Lecture();
		this.appli = new Appli(this);
	}

	public ArrayList<Sommet> getSommets()
	{
		return this.lecture.getSommets();
	}

	public void setSommets(ArrayList<Sommet> liste)
	{
		this.lecture.setSommets(liste);
	}

	public void setLiens(String lien)
	{
		this.lecture.setLien(lien);
	}

	public String getDocument()
	{
		return this.lecture.getDocument();
	}

	public void Mode(String mode)
	{
		this.lecture.Mode(mode);
	}

	public ArrayList<String> getCheminCourt()
	{
		return this.lecture.getCheminCourt();
	}

	public void sauvegarderXML()
	{
		this.lecture.sauvegarderXML();
	}

	public void setLienSeulement(String lien)
	{
		this.lecture.setLienSeulement(lien);
	}

	public static void main(String[] args) 
	{
		new Controleur();
	}

}
