package appli.ihm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Theme
{
    // ── Couleurs ────────────────────────────────────────────────────────────
    public static final Color BACKGROUND  = new Color(245, 247, 250);
    public static final Color PANEL       = Color.WHITE;
    public static final Color ACCENT      = new Color(59, 130, 246);
    public static final Color SUCCESS     = new Color(34, 197, 94);
    public static final Color DANGER      = new Color(239, 68, 68);
    public static final Color WARNING     = new Color(251, 146, 60);
    public static final Color TEXT        = new Color(31, 41, 55);
    public static final Color TEXT_MUTED  = new Color(107, 114, 128);
    public static final Color BORDER      = new Color(229, 231, 235);
    public static final Color HEADER_BG   = new Color(249, 250, 251);
    public static final Color SELECT_BG   = new Color(219, 234, 254);

    // ── Polices ─────────────────────────────────────────────────────────────
    public static final Font FONT_NORMAL  = new Font("Segoe UI Emoji", Font.PLAIN,  13);
    public static final Font FONT_BOLD    = new Font("Segoe UI Emoji", Font.BOLD,   13);
    public static final Font FONT_TITLE   = new Font("Segoe UI Emoji", Font.BOLD,   16);
    public static final Font FONT_SMALL   = new Font("Segoe UI Emoji", Font.BOLD,   11);

    // ── Bordures ────────────────────────────────────────────────────────────
    public static Border borderCard()
    {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    public static Border borderInput()
    {
        return BorderFactory.createLineBorder(BORDER, 1);
    }

    // ── Fabriques ────────────────────────────────────────────────────────────

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
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg); }
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
        table.setRowHeight(32);
        table.setShowGrid(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(SELECT_BG);
        table.setSelectionForeground(TEXT);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setBackground(PANEL);

        var header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
    }

    public static JMenu menu(String text)
    {
        JMenu m = new JMenu(text);
        m.setFont(FONT_BOLD);
        m.setForeground(TEXT);
        m.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        m.setOpaque(false);
        m.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { m.setOpaque(true);  m.setBackground(HEADER_BG); }
            public void mouseExited (java.awt.event.MouseEvent e) { m.setOpaque(false); }
        });
        return m;
    }

    public static JMenuItem menuItem(String text, Color accent)
    {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FONT_NORMAL);
        item.setForeground(TEXT);
        item.setBackground(PANEL);
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        item.setOpaque(true);
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { item.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20)); item.setForeground(accent); }
            public void mouseExited (java.awt.event.MouseEvent e) { item.setBackground(PANEL); item.setForeground(TEXT); }
        });
        return item;
    }

    public static JButton modeBadge(String text, Color bg)
    {
        JButton badge = new JButton(text);
        badge.setFont(FONT_SMALL);
        badge.setBackground(bg);
        badge.setForeground(Color.WHITE);
        badge.setFocusPainted(false);
        badge.setBorderPainted(false);
        badge.setContentAreaFilled(true);
        badge.setOpaque(true);
        badge.setEnabled(false);
        badge.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        badge.setPreferredSize(new Dimension(120, 28));
        return badge;
    }
}
