package appli.ihm.dessin;

import java.awt.*;

public class Cercle
{
	private int    x;
	private int    y;
	private int    rayon;
	private String nom;

	// États visuels (pour coloration Dijkstra / sélection future)
	private boolean selectionne = false;
	private boolean visite      = false;

	public Cercle(int x, int y, int rayon, String nom)
	{
		this.x     = x;
		this.y     = y;
		this.rayon = rayon;
		this.nom   = nom;
	}

	public void dessiner(Graphics2D g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// ── Couleurs selon état ───────────────────────────────────────────────
		Color fill, border, textColor;

		if (selectionne)
		{
			fill      = new Color(0, 120, 212);   // bleu plein
			border    = new Color(0,  80, 160);
			textColor = Color.WHITE;
		}
		else if (visite)
		{
			fill      = new Color(0, 120, 212, 30);  // bleu très léger
			border    = new Color(0, 120, 212);
			textColor = new Color(0, 80, 160);
		}
		else
		{
			fill      = Color.WHITE;
			border    = new Color(180, 180, 180);
			textColor = new Color(30, 30, 30);
		}

		// ── Fond ─────────────────────────────────────────────────────────────
		g2.setColor(fill);
		g2.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

		// ── Bordure ───────────────────────────────────────────────────────────
		g2.setColor(border);
		g2.setStroke(new BasicStroke(1.8f));
		g2.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

		// ── Nom centré ────────────────────────────────────────────────────────
		Font font = new Font("Segoe UI", Font.BOLD, Math.max(9, rayon - 5));
		g2.setFont(font);
		g2.setColor(textColor);
		FontMetrics fm = g2.getFontMetrics();
		String label = nom.length() > 5 ? nom.substring(0, 5) : nom;
		int tw = fm.stringWidth(label);
		int th = fm.getAscent();
		g2.drawString(label, x - tw / 2, y + th / 2 - 1);

		g2.dispose();
	}

	public int    getX()     { return x; }
	public int    getY()     { return y; }
	public String getNom()   { return nom; }
	public int    getRayon() { return rayon; }

	public void setSelectionne(boolean b) { this.selectionne = b; }
	public void setVisite(boolean b)      { this.visite      = b; }
}