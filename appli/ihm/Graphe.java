package appli.ihm;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import appli.Controleur;
import appli.ihm.dessin.*;
import appli.metier.Sommet;

import java.util.ArrayList;

public class Graphe extends JPanel
{
    private Controleur ctrl;

    private ArrayList<Cercle> sommets;
    private ArrayList<appli.ihm.dessin.Lien> liens;


    public Graphe(Controleur ctrl)
    {
        this.ctrl = ctrl;
        
        // Initialisation des listes
        this.sommets = new ArrayList<>();
        this.liens = new ArrayList<>();
        
        System.out.println("=== DEBUG GRAPHE ===");
        System.out.println("Sommets métier: " + this.ctrl.getSommets());
        
        /* Création des composants */
        if(this.ctrl.getSommets() != null && !this.ctrl.getSommets().isEmpty())
        {
            System.out.println("Nombre de sommets: " + this.ctrl.getSommets().size());
            
            // Création des cercles pour chaque sommet
            for(int cpt = 0; cpt < this.ctrl.getSommets().size(); cpt++)
            {
                Sommet sommetMetier = this.ctrl.getSommets().get(cpt);
                Cercle cercle = new Cercle(cpt * 100 + 100, cpt * 100 + 100, 20, sommetMetier.getNom());
                this.sommets.add(cercle);
                System.out.println("Cercle créé: " + sommetMetier.getNom() + " à (" + (cpt * 100 + 100) + "," + (cpt * 100 + 100) + ")");
            }

            System.out.println("Nombre de cercles créés: " + this.sommets.size());

            // Création des liens entre sommets
            for(int cpt = 0; cpt < this.ctrl.getSommets().size(); cpt++)
            {
                Sommet sommetMetier = this.ctrl.getSommets().get(cpt);
                
                for(appli.metier.Lien lienMetier : sommetMetier.getLiens())
                {
                    int indexCible = -1;
                    for(int i = 0; i < this.ctrl.getSommets().size(); i++)
                    {
                        if(this.ctrl.getSommets().get(i).getNom().equals(lienMetier.getNom()))
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
                        System.out.println("Lien créé: " + sommetMetier.getNom() + " -> " + lienMetier.getNom());
                    }
                }
            }
            
            System.out.println("Nombre de liens créés: " + this.liens.size());
        }
        else
        {
            System.out.println("AUCUN SOMMET DISPONIBLE !");
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        System.out.println("paintComponent appelé ! Cercles: " + this.sommets.size() + ", Liens: " + this.liens.size());
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(appli.ihm.dessin.Lien lien : this.liens)
        {
            lien.dessiner(g2d);
        }
        
        for(Cercle cercle : this.sommets)
        {
            cercle.dessiner(g2d);
        }
    }
        
    /**
     * Méthode pour rafraîchir l'affichage après modification
     */
    public void actualiser()
    {
        repaint();
    }
}