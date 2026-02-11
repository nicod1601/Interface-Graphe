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
            System.out.println("Aucun lien n'est défini");
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

                System.out.println(nom + " -> " + lienSommet + " (" + distance + ")");

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

            for (int cpt = 0; cpt < this.sommets.size(); cpt++) 
            {
                System.out.print(this.sommets.get(cpt) + " ");
            }

            System.out.println();

            for (int cpt = 0; cpt < this.sommetsObjet.size(); cpt++) 
            {
                System.out.print(this.sommetsObjet.get(cpt).getNom() + " : ");
                for (Lien lien : this.sommetsObjet.get(cpt).getLiens())
                {
                    System.out.print(lien + " ");
                }

                System.out.println();
            }
            
            System.out.println();

        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable");
        }
    }

    public ArrayList<Sommet> getSommets()
    {
        return sommetsObjet;
    }

    public String getDocument()
    {
        System.out.println("Document : \n" + document);
        return document;
    }

    public void setLien(String lien)
    {
        this.lien = lien;
        this.lecture();
    }
}
