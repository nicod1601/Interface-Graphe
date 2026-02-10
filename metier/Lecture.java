package metier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Lecture 
{
    private String lien;
    private ArrayList<Sommet> sommets;

    public Lecture()
    {
        Scanner clavier = new Scanner(System.in);
        System.out.println("Entrez le lien du fichier à lire : ");
        this.lien = clavier.nextLine();

        try
        {
            Scanner sc = new Scanner(new File(this.lien));

            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                System.out.println(line);

                String[] parts = line.split("|");
                
                String nom      = parts[0];
                String liens    = parts[1];
                String distance = parts[2];

                if(this.isValide(nom))
                {
                    this.sommets.add(new Sommet(nom));








                }
                else
                {
                    for(int cpt = 0; cpt < this.sommets.size(); cpt++)
                    {
                        if(this.sommets.get(cpt).getNom().equals(nom))
                        {
                            
                        }
                    }
                }



            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Fichier introuvable");
        }

    }

    public boolean isValide(String nom)
    {
        if(this.sommets.size() == 0)
        {
            return true;
        }
        else
        {
            for(int cpt = 0; cpt < this.sommets.size(); cpt++)
            {
                if(this.sommets.get(cpt).getNom().equals(nom))
                {
                    return false;
                }
            }

            return true;
        }
    }
    
}
