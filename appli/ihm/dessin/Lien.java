package appli.ihm.dessin;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Lien
{
	private Cercle  c1;
	private Cercle  c2;
	private double  distance;
	private boolean enChemin;

	public Lien(Cercle c1, Cercle c2, double distance)
	{
		this.c1       = c1;
		this.c2       = c2;
		this.distance = distance;
		this.enChemin = false;
	}

	public void dessiner(Graphics2D g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int x1 = c1.getX(), y1 = c1.getY();
		int x2 = c2.getX(), y2 = c2.getY();
		int r1 = c1.getRayon(), r2 = c2.getRayon();

		double dx  = x2 - x1, dy = y2 - y1;
		double len = Math.sqrt(dx * dx + dy * dy);
		if (len < 1) { g2.dispose(); return; }
		double ux = dx / len, uy = dy / len;

		int sx = (int)(x1 + ux * (r1 + 3));
		int sy = (int)(y1 + uy * (r1 + 3));
		int ex = (int)(x2 - ux * (r2 + 8));
		int ey = (int)(y2 - uy * (r2 + 8));

		// ── Couleurs selon état ───────────────────────────────────────────────
		Color couleurLigne, couleurFleche, couleurLabel;
		float epaisseur;

		if (this.enChemin)
		{
			couleurLigne  = new Color(  0, 210, 190);
			couleurFleche = new Color(  0, 230, 210);
			couleurLabel  = new Color(  0, 210, 190);
			epaisseur     = 2.4f;

			// Halo sous le lien actif
			g2.setColor(new Color(0, 210, 190, 30));
			g2.setStroke(new BasicStroke(epaisseur + 6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.drawLine(sx, sy, ex, ey);
		}
		else
		{
			couleurLigne  = new Color( 45,  62,  90);
			couleurFleche = new Color( 60,  80, 110);
			couleurLabel  = new Color( 80, 110, 160);
			epaisseur     = 1.2f;
		}

		// ── Ligne ─────────────────────────────────────────────────────────────
		g2.setColor(couleurLigne);
		g2.setStroke(new BasicStroke(epaisseur, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(sx, sy, ex, ey);

		// ── Flèche ────────────────────────────────────────────────────────────
		double angle = Math.atan2(dy, dx);
		AffineTransform old = g2.getTransform();
		g2.translate(ex + ux * 4, ey + uy * 4);
		g2.rotate(angle);
		int arrowSize = 8;
		Polygon arrow = new Polygon();
		arrow.addPoint(0, 0);
		arrow.addPoint(-arrowSize, -arrowSize / 2);
		arrow.addPoint(-arrowSize,  arrowSize / 2);
		g2.setColor(couleurFleche);
		g2.fill(arrow);
		g2.setTransform(old);

		// ── Label distance ────────────────────────────────────────────────────
		if (distance > 0)
		{
			int mx = (sx + ex) / 2;
			int my = (sy + ey) / 2;

			String label = String.valueOf((int) distance);
			Font   font  = new Font("Consolas", Font.BOLD, 11);
			g2.setFont(font);
			FontMetrics fm  = g2.getFontMetrics();
			int tw  = fm.stringWidth(label);
			int th  = fm.getAscent();
			int pad = 3;

			// Fond du badge
			g2.setColor(new Color(16, 20, 30, 220));
			g2.fillRoundRect(mx - tw / 2 - pad, my - th - 1, tw + pad * 2, th + pad, 4, 4);

			// Bordure du badge
			g2.setColor(this.enChemin
				? new Color(0, 210, 190, 160)
				: new Color(45, 62, 90));
			g2.setStroke(new BasicStroke(0.8f));
			g2.drawRoundRect(mx - tw / 2 - pad, my - th - 1, tw + pad * 2, th + pad, 4, 4);

			// Texte
			g2.setColor(couleurLabel);
			g2.drawString(label, mx - tw / 2, my + 1);
		}

		g2.dispose();
	}

	public String getSommet()            { return c2.getNom(); }
	public double getDistance()          { return distance;    }
	public String getSource()            { return c1.getNom(); }
	public void   setEnChemin(boolean b) { this.enChemin = b; }
}
