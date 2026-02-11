package appli.ihm;

import appli.Controleur;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Appli extends JFrame
{
    private Menu menu;
    private Controleur ctrl;
    private Edit edit;
    private Graphe graphe;

    public Appli(Controleur ctrl)
    {
        this.ctrl = ctrl;
        this.setTitle("Interface Graphe");
        Image icon = Toolkit.getDefaultToolkit().getImage("./appli/logo.png");
        this.setIconImage(icon);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        /* Création des composants */
        this.menu = new Menu(this.ctrl, this);
        this.edit = null;
        this.graphe = null;

        /* Positionnement des composants */
        this.add(this.menu, BorderLayout.NORTH);
        
        this.setVisible(true);
    }

    public void afficher(String nom)
    {
        switch(nom)
        {
            case "Graphe":
                // Cacher et retirer l'éditeur s'il existe
                if(this.edit != null)
                {
                    this.edit.setVisible(false);
                    this.remove(this.edit);
                }
                
                // Retirer l'ancien graphe s'il existe
                if(this.graphe != null)
                {
                    this.remove(this.graphe);
                }
                
                // Créer un nouveau graphe avec les données actuelles
                this.graphe = new Graphe(this.ctrl);
                this.add(this.graphe, BorderLayout.CENTER);
                this.graphe.setVisible(true);
                break;
                
            case "Edit":
                // Cacher et retirer le graphe s'il existe
                if(this.graphe != null)
                {
                    this.graphe.setVisible(false);
                    this.remove(this.graphe);
                }
                
                // Retirer l'ancien éditeur s'il existe
                if(this.edit != null)
                {
                    this.remove(this.edit);
                }
                
                // Créer un nouvel éditeur avec les données actuelles
                this.edit = new Edit(this.ctrl, this);
                this.add(this.edit, BorderLayout.CENTER);
                this.edit.setVisible(true);
                this.edit.setDocument(this.ctrl.getDocument());
                break;
                
            case "Annuler":
                // Même comportement que "Graphe" : fermer edit et afficher graphe
                if(this.edit != null)
                {
                    this.edit.setVisible(false);
                    this.remove(this.edit);
                }
                
                if(this.graphe != null)
                {
                    this.remove(this.graphe);
                }
                
                this.graphe = new Graphe(this.ctrl);
                this.add(this.graphe, BorderLayout.CENTER);
                this.graphe.setVisible(true);
                break;
        }
        
        // Forcer le rafraîchissement de l'interface
        this.revalidate();
        this.repaint();
    }
}