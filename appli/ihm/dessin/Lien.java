package appli.ihm.dessin;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Lien
{
	private Cercle c1;
	private Cercle c2;
	private double distance;

	public Lien(Cercle c1, Cercle c2, double distance)
	{
		this.c1       = c1;
		this.c2       = c2;
		this.distance = distance;
	}

	public void dessiner(Graphics2D g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int x1 = c1.getX(), y1 = c1.getY();
		int x2 = c2.getX(), y2 = c2.getY();
		int r1 = c1.getRayon(), r2 = c2.getRayon();

		// Vecteur normalisé
		double dx  = x2 - x1, dy = y2 - y1;
		double len = Math.sqrt(dx * dx + dy * dy);
		if (len < 1) { g2.dispose(); return; }
		double ux = dx / len, uy = dy / len;

		// Points d'attache sur les bords des cercles
		int sx = (int)(x1 + ux * (r1 + 3));
		int sy = (int)(y1 + uy * (r1 + 3));
		int ex = (int)(x2 - ux * (r2 + 8));  // décalage pour la pointe de flèche
		int ey = (int)(y2 - uy * (r2 + 8));

		// ── Ligne ────────────────────────────────────────────────────────────
		g2.setColor(new Color(180, 180, 180));
		g2.setStroke(new BasicStroke(1.2f));
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
		g2.setColor(new Color(150, 150, 150));
		g2.fill(arrow);
		g2.setTransform(old);

		// ── Badge distance ────────────────────────────────────────────────────
		if (distance > 0)
		{
			int mx = (sx + ex) / 2;
			int my = (sy + ey) / 2;

			String label = String.valueOf((int) distance);
			Font font = new Font("Segoe UI", Font.BOLD, 11);
			g2.setFont(font);
			FontMetrics fm = g2.getFontMetrics();
			int tw  = fm.stringWidth(label);
			int th  = fm.getAscent();
			int pad = 3;

			// Fond blanc avec bordure légère
			g2.setColor(Color.WHITE);
			g2.fillRoundRect(mx - tw / 2 - pad, my - th - 1, tw + pad * 2, th + pad, 4, 4);
			g2.setColor(new Color(200, 200, 200));
			g2.setStroke(new BasicStroke(0.8f));
			g2.drawRoundRect(mx - tw / 2 - pad, my - th - 1, tw + pad * 2, th + pad, 4, 4);

			// Texte en bleu
			g2.setColor(new Color(0, 120, 212));
			g2.drawString(label, mx - tw / 2, my + 1);
		}

		g2.dispose();
	}

	public String getSommet()   { return c2.getNom(); }
	public double getDistance() { return distance;    }
}