package appli.ihm;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class Theme
{
	// ── Palette — Dark Graph Theory ───────────────────────────────────────────
	public static final Color BACKGROUND  = new Color( 22,  28,  42);
	public static final Color PANEL       = new Color( 30,  38,  56);
	public static final Color PANEL_ALT   = new Color( 42,  54,  80);
	// Fond dédié à l'éditeur de texte — plus clair pour la lisibilité
	public static final Color EDITOR_BG   = new Color( 26,  34,  52);
	public static final Color ACCENT      = new Color(  0, 200, 180);
	public static final Color ACCENT_SOFT = new Color(  0, 200, 180,  25);
	public static final Color SUCCESS     = new Color( 60, 220, 130);
	public static final Color DANGER      = new Color(255,  90,  90);
	public static final Color WARNING     = new Color(255, 190,  60);
	public static final Color TEXT        = new Color(220, 228, 242);
	public static final Color TEXT_MUTED  = new Color(140, 158, 190);
	public static final Color BORDER      = new Color( 52,  66,  95);
	public static final Color HEADER_BG   = new Color( 26,  33,  50);
	public static final Color SELECT_BG   = new Color(  0, 200, 180,  35);
	public static final Color SELECT_FG   = new Color(  0, 230, 210);

	// ── Polices — inchangées pour garder les emojis ───────────────────────────
	public static final Font FONT_NORMAL = loadSans(Font.PLAIN,  13);
	public static final Font FONT_BOLD   = loadSans(Font.BOLD,   13);
	public static final Font FONT_TITLE  = loadSans(Font.BOLD,   14);
	public static final Font FONT_SMALL  = loadSans(Font.BOLD,   11);
	public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	private static Font loadSans(int style, int size)
	{
		for (String name : new String[]{"Segoe UI Emoji", "SF Pro Text", "Helvetica Neue", "Arial"})
		{
			Font f = new Font(name, style, size);
			if (!f.getFamily().equals(Font.DIALOG)) return f;
		}
		return new Font(Font.SANS_SERIF, style, size);
	}

	// ── Bordures ──────────────────────────────────────────────────────────────
	public static Border borderCard()
	{
		return BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER, 1),
			BorderFactory.createEmptyBorder(14, 14, 14, 14)
		);
	}

	public static Border borderInput()
	{
		return BorderFactory.createLineBorder(BORDER, 1);
	}

	// ── Composants ────────────────────────────────────────────────────────────

	public static JLabel label(String text)
	{
		JLabel l = new JLabel(text);
		l.setFont(FONT_NORMAL);
		l.setForeground(TEXT_MUTED);
		return l;
	}

	public static JLabel labelTitle(String text)
	{
		JLabel l = new JLabel(text);
		l.setFont(FONT_TITLE);
		l.setForeground(ACCENT);
		return l;
	}

	/**
	 * Bouton avec fond semi-transparent + bordure colorée.
	 * paintComponent manuel pour éviter le bug Metal L&F avec les couleurs alpha.
	 */
	public static JButton button(String text, Color color)
	{
		final Color borderCol = new Color(color.getRed(), color.getGreen(), color.getBlue(), 140);
		final Color bgNormal  = new Color(color.getRed(), color.getGreen(), color.getBlue(),  28);
		final Color bgHover   = new Color(color.getRed(), color.getGreen(), color.getBlue(),  60);
		final boolean[] hovered = {false};

		JButton btn = new JButton(text)
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(hovered[0] ? bgHover : bgNormal);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				g2.setColor(borderCol);
				g2.setStroke(new BasicStroke(1.0f));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				g2.dispose();
				super.paintComponent(g);
			}
		};

		btn.setFont(FONT_BOLD);
		btn.setForeground(color);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setOpaque(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

		btn.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e) { hovered[0] = true;  btn.repaint(); }
			@Override public void mouseExited (MouseEvent e) { hovered[0] = false; btn.repaint(); }
		});

		return btn;
	}

	/**
	 * Badge de mode (lecture seule) — même rendu manuel que button()
	 * pour éviter le bug Metal avec les couleurs alpha.
	 */
	public static JButton modeBadge(String text, Color color)
	{
		final Color[] currentColor = {color};

		JButton badge = new JButton(text)
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				Color c      = currentColor[0];
				Color bgCol  = new Color(c.getRed(), c.getGreen(), c.getBlue(), 28);
				Color borCol = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);

				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(bgCol);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				g2.setColor(borCol);
				g2.setStroke(new BasicStroke(1.0f));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				g2.dispose();
				super.paintComponent(g);
			}
		};

		badge.setFont(FONT_SMALL);
		badge.setForeground(color);
		badge.setContentAreaFilled(false);
		badge.setBorderPainted(false);
		badge.setFocusPainted(false);
		badge.setOpaque(false);
		badge.setEnabled(false);
		badge.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		badge.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 12));
		badge.setPreferredSize(new Dimension(150, 26));

		// Stocker la référence pour que updateModeBadge puisse changer la couleur
		badge.putClientProperty("themeColorRef", currentColor);

		return badge;
	}

	/**
	 * Met à jour la couleur d'un badge créé par modeBadge() sans recréer le composant.
	 * À utiliser dans Menu.updateModeBadge() à la place de l'ancienne logique.
	 */
	public static void updateBadgeColor(JButton badge, String text, Color color)
	{
		Object ref = badge.getClientProperty("themeColorRef");
		if (ref instanceof Color[])
			((Color[]) ref)[0] = color;
		badge.setText(text);
		badge.setForeground(color);
		badge.repaint();
	}

	public static JPanel panel()
	{
		JPanel p = new JPanel();
		p.setBackground(PANEL);
		return p;
	}

	public static JPanel panelBackground()
	{
		JPanel p = new JPanel();
		p.setBackground(BACKGROUND);
		return p;
	}

	public static void styleTable(JTable table)
	{
		table.setFont(FONT_NORMAL);
		table.setRowHeight(42);
		table.setShowGrid(false);
		table.setShowHorizontalLines(true);
		table.setGridColor(BORDER);
		table.setSelectionBackground(SELECT_BG);
		table.setSelectionForeground(SELECT_FG);
		table.setIntercellSpacing(new Dimension(10, 0));
		table.setBackground(PANEL);
		table.setForeground(TEXT);

		var header = table.getTableHeader();
		header.setFont(FONT_BOLD);
		header.setBackground(HEADER_BG);
		header.setForeground(ACCENT);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
		header.setReorderingAllowed(false);
	}

	/** Aucun MouseListener — le highlight est géré par le L&F. */
	public static JMenu menu(String text)
	{
		JMenu m = new JMenu(text);
		m.setFont(FONT_NORMAL);
		// Couleur explicite pour être sûr que Metal ne la remplace pas
		m.setForeground(TEXT);
		m.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
		return m;
	}

	/** Aucun MouseListener — le highlight est géré par le L&F. */
	public static JMenuItem menuItem(String text, Color accent)
	{
		JMenuItem item = new JMenuItem(text);
		item.setFont(FONT_NORMAL);
		item.setForeground(TEXT);
		item.setBackground(PANEL);
		item.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
		return item;
	}
}