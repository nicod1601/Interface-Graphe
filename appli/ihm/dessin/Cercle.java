package appli.ihm.dessin;

import java.awt.*;

public class Cercle 
{
    private int x;
    private int y;
    private int rayon;
    private String nom;

    public Cercle(int x, int y, int rayon, String nom)
    {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.nom = nom;
    }

    public void dessiner(Graphics2D g)
    {
        // Dessin du cercle (non rempli)
        g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

        // Dessin du nom au centre
        FontMetrics fm = g.getFontMetrics();
        int largeurTexte = fm.stringWidth(nom);
        int hauteurTexte = fm.getAscent();

        g.drawString(nom, x - largeurTexte / 2, y + hauteurTexte / 2);
    }

    // Getters utiles pour Lien
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getNom()
    {
        return nom;
    }
}
