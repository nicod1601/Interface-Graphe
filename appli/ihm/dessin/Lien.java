package appli.ihm.dessin;

import java.awt.*;

public class Lien
{
    private Cercle c1;
    private Cercle c2;
    private double distance;

    public Lien(Cercle c1, Cercle c2, double distance)
    {
        this.c1 = c1;
        this.c2 = c2;
        this.distance = distance;
    }

    public void dessiner(Graphics2D g)
    {
        g.drawLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
        
        int mx = (c1.getX() + c2.getX()) / 2;
        int my = (c1.getY() + c2.getY()) / 2;
        g.drawString(String.valueOf(distance), mx, my);
    }
    
    public String getSommet() { return c2.getNom(); }
    public double getDistance() { return distance; }
}