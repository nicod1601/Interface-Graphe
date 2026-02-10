package metier;
import java.util.ArrayList;

public class Sommet
{
    private String nom;
    private ArrayList<String> liens;

    public Sommet(String nom, ArrayList<String> liens)
    {
        this.nom = nom;
        this.liens = liens;
    }

    public Sommet(String nom)
    {
        this.nom = nom;
        this.liens = new ArrayList<String>();
    }

    /*
        GETTERS 
    */

    public String getNom()
    {
        return nom;
    }

    public ArrayList<String> getLiens()
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

    public void setLiens(ArrayList<String> liens)
    {
        this.liens = liens;
    }
}