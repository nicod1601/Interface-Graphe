package appli.metier;
import java.util.ArrayList;

public class Sommet
{
    private String nom;
    private ArrayList<Lien> liens;

    public Sommet(String nom, ArrayList<Lien> liens)
    {
        this.nom = nom;
        this.liens = liens;
    }

    public Sommet(String nom)
    {
        this.nom = nom;
        this.liens = new ArrayList<Lien>();
    }

    /*
        GETTERS 
    */

    public String getNom()
    {
        return nom;
    }

    public ArrayList<Lien> getLiens()
    {
        return liens;
    }

    /*
        SETTERS
    */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setLiens(ArrayList<Lien> liens)
    {
        this.liens = liens;
    }
}