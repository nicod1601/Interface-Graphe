package appli.ihm;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Theme
{
	// ── Palette ─────────────────────────────────────────────────────────────
	public static final Color BACKGROUND  = new Color(243, 243, 243);
	public static final Color PANEL       = new Color(255, 255, 255);
	public static final Color PANEL_ALT   = new Color(248, 248, 248);
	public static final Color ACCENT      = new Color(  0, 120, 212);
	public static final Color ACCENT_SOFT = new Color(  0, 120, 212,  25);
	public static final Color SUCCESS     = new Color( 22, 163,  74);
	public static final Color DANGER      = new Color(205,  53,  53);
	public static final Color WARNING     = new Color(202, 130,   0);
	public static final Color TEXT        = new Color( 30,  30,  30);
	public static final Color TEXT_MUTED  = new Color(120, 120, 120);
	public static final Color BORDER      = new Color(220, 220, 220);
	public static final Color HEADER_BG   = new Color(248, 248, 248);
	public static final Color SELECT_BG   = new Color(  0, 120, 212,  20);
	public static final Color SELECT_FG   = new Color(  0,  80, 160);

	// ── Polices ───────────────────────────────────────────────────────────────
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
		l.setForeground(TEXT);
		return l;
	}

	public static JLabel labelTitle(String text)
	{
		JLabel l = new JLabel(text);
		l.setFont(FONT_TITLE);
		l.setForeground(TEXT);
		return l;
	}

	public static JButton button(String text, Color bg)
	{
		JButton btn = new JButton(text);
		btn.setFont(FONT_BOLD);
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setOpaque(true);
		btn.setBorderPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
		btn.addMouseListener(new java.awt.event.MouseAdapter()
		{
			final Color hover = bg.darker();
			public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
			public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg);    }
		});
		return btn;
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
		table.setRowHeight(30);
		table.setShowGrid(false);
		table.setShowHorizontalLines(true);
		table.setGridColor(new Color(235, 235, 235));
		table.setSelectionBackground(SELECT_BG);
		table.setSelectionForeground(SELECT_FG);
		table.setIntercellSpacing(new Dimension(10, 0));
		table.setBackground(PANEL);
		table.setForeground(TEXT);

		var header = table.getTableHeader();
		header.setFont(FONT_BOLD);
		header.setBackground(HEADER_BG);
		header.setForeground(TEXT_MUTED);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
		header.setReorderingAllowed(false);
	}

	/** Aucun MouseListener — le highlight est géré par le L&F. */
	public static JMenu menu(String text)
	{
		JMenu m = new JMenu(text);
		m.setFont(FONT_NORMAL);
		m.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
		return m;
	}

	/** Aucun MouseListener — le highlight est géré par le L&F. */
	public static JMenuItem menuItem(String text, Color accent)
	{
		JMenuItem item = new JMenuItem(text);
		item.setFont(FONT_NORMAL);
		item.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
		return item;
	}

	public static JButton modeBadge(String text, Color color)
	{
		JButton badge = new JButton(text);
		badge.setFont(FONT_SMALL);
		badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 18));
		badge.setForeground(color);
		badge.setFocusPainted(false);
		badge.setContentAreaFilled(true);
		badge.setOpaque(true);
		badge.setEnabled(false);
		badge.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		badge.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				new Color(color.getRed(), color.getGreen(), color.getBlue(), 80), 1, true),
			BorderFactory.createEmptyBorder(3, 12, 3, 12)
		));
		badge.setPreferredSize(new Dimension(130, 26));
		return badge;
	}

	/**
	 * À appeler EN TOUT PREMIER dans main() ou dans le constructeur Appli,
	 * avant même new Appli().
	 *
	 * On force Metal (L&F Java pur) pour avoir un rendu de menus stable sur
	 * Windows — le L&F système Windows intercepte le dessin des menus et
	 * ignore une grande partie des clés UIManager, ce qui causait le bug
	 * d'écriture superposée.
	 */
	public static void applyGlobalLookAndFeel()
	{
		try
		{
			// Metal = L&F Java pur, rendu 100% contrôlé, pas d'interférence Windows
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (Exception e)
		{
			// Si Metal n'est pas dispo (très rare), on reste sur le L&F par défaut
			System.err.println("Metal L&F non disponible : " + e.getMessage());
		}

		// ── Menus ─────────────────────────────────────────────────────────────
		UIManager.put("Menu.background",              PANEL);
		UIManager.put("Menu.foreground",              TEXT);
		UIManager.put("Menu.font",                    FONT_NORMAL);
		UIManager.put("Menu.selectionBackground",     new Color(0, 120, 212, 40));
		UIManager.put("Menu.selectionForeground",     ACCENT);
		UIManager.put("Menu.border",                  BorderFactory.createEmptyBorder(4, 10, 4, 10));
		UIManager.put("Menu.borderPainted",           false);

		UIManager.put("PopupMenu.background",         PANEL);
		UIManager.put("PopupMenu.border",             BorderFactory.createLineBorder(BORDER, 1));

		UIManager.put("MenuItem.background",          PANEL);
		UIManager.put("MenuItem.foreground",          TEXT);
		UIManager.put("MenuItem.font",                FONT_NORMAL);
		UIManager.put("MenuItem.selectionBackground", new Color(0, 120, 212, 40));
		UIManager.put("MenuItem.selectionForeground", ACCENT);
		UIManager.put("MenuItem.border",              BorderFactory.createEmptyBorder(7, 16, 7, 16));
		UIManager.put("MenuItem.borderPainted",       false);

		UIManager.put("MenuBar.background",           PANEL);
		UIManager.put("MenuBar.border",               BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

		UIManager.put("Separator.foreground",         BORDER);
		UIManager.put("Separator.background",         PANEL);

		// ── Scrollbars ────────────────────────────────────────────────────────
		UIManager.put("ScrollBar.width",              8);
		UIManager.put("ScrollBar.background",         BACKGROUND);
		UIManager.put("ScrollBar.thumb",              new Color(200, 200, 200));
		UIManager.put("ScrollBar.thumbDarkShadow",    BACKGROUND);
		UIManager.put("ScrollBar.thumbShadow",        BACKGROUND);
		UIManager.put("ScrollBar.thumbHighlight",     new Color(170, 170, 170));
		UIManager.put("ScrollBar.track",              BACKGROUND);

		// ── Éditeur ───────────────────────────────────────────────────────────
		UIManager.put("EditorPane.background",        PANEL_ALT);
		UIManager.put("EditorPane.foreground",        TEXT);
		UIManager.put("EditorPane.caretForeground",   ACCENT);
		UIManager.put("EditorPane.font",              FONT_MONO);

		// ── Table ─────────────────────────────────────────────────────────────
		UIManager.put("Table.background",             PANEL);
		UIManager.put("Table.foreground",             TEXT);
		UIManager.put("Table.selectionBackground",    SELECT_BG);
		UIManager.put("Table.selectionForeground",    SELECT_FG);
		UIManager.put("TableHeader.background",       HEADER_BG);
		UIManager.put("TableHeader.foreground",       TEXT_MUTED);

		// ── Fond général ──────────────────────────────────────────────────────
		UIManager.put("Panel.background",             BACKGROUND);
		UIManager.put("Viewport.background",          PANEL);
		UIManager.put("ScrollPane.background",        BACKGROUND);

		// ── Tooltip ───────────────────────────────────────────────────────────
		UIManager.put("ToolTip.background",           new Color(255, 255, 220));
		UIManager.put("ToolTip.foreground",           TEXT);
		UIManager.put("ToolTip.border",               BorderFactory.createLineBorder(BORDER, 1));
	}
}