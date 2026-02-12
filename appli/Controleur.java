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
        if(mode.equals("Dijikstra"))
        {
            System.out.println("Dijikstra");
        }
        
        if(mode.equals("Bellman-Ford"))
        {
            System.out.println("Bellman-Ford");
        }
    }

    public static void main(String[] args) 
    {
        new Controleur();
    }

}
