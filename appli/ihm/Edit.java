package appli.ihm;

import appli.Controleur;
import appli.metier.Lien;
import appli.metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Edit extends JPanel implements ActionListener
{
    private Controleur ctrl;

    // Couleurs modernes
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(59, 130, 246);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color HEADER_BG = new Color(249, 250, 251);

    /*Panel Document */
    private JEditorPane txtDocument;

    /*Panel Tableau */
    private JTable table;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnAnnuler;
    private JButton btnRafraichir;
    private JButton btnSauvegarde;
    private DefaultTableModel tableModel;
    private Appli appli;

    /*panel graphe */
    private GrapheCopie graphe;
    private ArrayList<Sommet> sommets;

    public Edit(Controleur ctrl, Appli appli)
    {
        this.ctrl = ctrl;
        this.appli = appli;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        /*Panel Document */
        JPanel panelDocument = createStyledPanel();
        panelDocument.setLayout(new BorderLayout(0, 10));
        panelDocument.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblDocument = createStyledLabel("📄 Document", 16, Font.BOLD);
        this.txtDocument = new JEditorPane();
        this.txtDocument.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        this.txtDocument.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollDocument = new JScrollPane(this.txtDocument);
        scrollDocument.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        panelDocument.add(lblDocument, BorderLayout.NORTH);
        panelDocument.add(scrollDocument, BorderLayout.CENTER);

        // Définir une largeur préférée pour le panel document
        panelDocument.setPreferredSize(new Dimension(350, 0));

        /*Panel Graphe */
        this.sommets = new ArrayList<>();
        this.graphe = new GrapheCopie(this);
        this.graphe.setBackground(PANEL_COLOR);
        this.graphe.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        /*Panel Tableau */
        JPanel panelTableau = createStyledPanel();
        panelTableau.setLayout(new BorderLayout(0, 10));
        panelTableau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // En-tête du tableau avec titre et boutons
        JPanel panelTableauNorth = new JPanel(new BorderLayout(10, 0));
        panelTableauNorth.setBackground(PANEL_COLOR);

        JLabel lblTableau = createStyledLabel("📊 Gestion des Sommets", 16, Font.BOLD);

        // Panel pour les boutons d'action
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBtn.setBackground(PANEL_COLOR);

        this.btnRafraichir = createStyledButton("🔄 Rafraîchir", ACCENT_COLOR);
        this.btnAnnuler = createStyledButton("↩️ Annuler", new Color(107, 114, 128));
        this.btnSupprimer = createStyledButton("➖ Supprimer", DANGER_COLOR);
        this.btnAjouter = createStyledButton("➕ Ajouter", SUCCESS_COLOR);
        this.btnSauvegarde = createStyledButton("✅ Sauvegarder", SUCCESS_COLOR);

        panelBtn.add(this.btnRafraichir);
        panelBtn.add(this.btnAnnuler);
        panelBtn.add(this.btnSauvegarde);
        panelBtn.add(this.btnSupprimer);
        panelBtn.add(this.btnAjouter);

        panelTableauNorth.add(lblTableau, BorderLayout.WEST);
        panelTableauNorth.add(panelBtn, BorderLayout.EAST);

        // Configuration du tableau
        String[] colonne = {"Sommet", "Liens", "Distance"};
        this.tableModel = new DefaultTableModel(colonne, 0);
        this.table = new JTable(this.tableModel);

        // Style du tableau
        this.table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        this.table.setRowHeight(32);
        this.table.setShowGrid(true);
        this.table.setGridColor(BORDER_COLOR);
        this.table.setSelectionBackground(new Color(219, 234, 254));
        this.table.setSelectionForeground(TEXT_COLOR);
        this.table.setIntercellSpacing(new Dimension(10, 5));

        // Style de l'en-tête du tableau
        JTableHeader header = this.table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        JScrollPane scrollTable = new JScrollPane(this.table);
        scrollTable.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollTable.getViewport().setBackground(PANEL_COLOR);

        panelTableau.add(panelTableauNorth, BorderLayout.NORTH);
        panelTableau.add(scrollTable, BorderLayout.CENTER);

        /*Panel TabGraphe */
        JPanel panelTabGraph = new JPanel(new GridLayout(2, 1, 0, 10));
        panelTabGraph.setBackground(BACKGROUND_COLOR);
        panelTabGraph.add(panelTableau);
        panelTabGraph.add(this.graphe);

        /*Position des composants */
        this.add(panelDocument, BorderLayout.WEST);
        this.add(panelTabGraph, BorderLayout.CENTER);

        /*Activation */
        this.btnAjouter.addActionListener(this);
        this.btnSupprimer.addActionListener(this);
        this.btnAnnuler.addActionListener(this);
        this.btnRafraichir.addActionListener(this);
        this.btnSauvegarde.addActionListener(this);

        this.setVisible(false);
    }

    /**
     * Crée un panel stylisé avec fond blanc
     */
    private JPanel createStyledPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        return panel;
    }

    /**
     * Crée un label stylisé avec police personnalisée
     */
    private JLabel createStyledLabel(String text, int fontSize, int fontStyle)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Emoji", fontStyle, fontSize));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * Crée un bouton stylisé avec couleur personnalisée
     */
    private JButton createStyledButton(String text, Color bgColor)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setMargin(new Insets(8, 16, 8, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "➕ Ajouter":
                System.out.println("Ajouter");
                this.modifierTableau("+");
                break;
            case "➖ Supprimer":
                System.out.println("Supprimer");
                this.modifierTableau("-");
                break;
            case "↩️ Annuler":
                System.out.println("Annuler");
                this.appli.afficher("Annuler");
                break;
            case "🔄 Rafraîchir":
                this.rafraichir();
                break;
            case"✅ Sauvegarder":
                this.sauvegarder();
                break;
        }
    }

    public void setDocument(String document)
    {
        this.txtDocument.setText(document);
        this.creationTableau();
    }

    public void creationTableau()
    {
        this.sommets = new ArrayList<>();
        this.sommets = this.ctrl.getSommets();
        this.graphe.actualiser();
        this.tableModel.setRowCount(0);
        for (Sommet sommet : sommets)
        {
            if (sommet.getLiens().isEmpty())
            {
                // Sommet terminal (ex: F) : afficher quand même une ligne vide
                Object[] row = {sommet.getNom(), "", 0};
                this.tableModel.addRow(row);
            }
            else
            {
                for(Lien lien : sommet.getLiens())
                {
                    Object[] row = {sommet.getNom(), lien.getNom(), lien.getDistance()};
                    this.tableModel.addRow(row);
                }
            }
        }
    }

    public void modifierTableau(String action)
    {
        if (action.equals("+"))
        {
            Object[] row = {"", "", 0};
            this.tableModel.addRow(row);
        }

        if (action.equals("-") && this.table.getSelectedRow() != -1)
        {
            this.tableModel.removeRow(this.table.getSelectedRow());
            this.table.revalidate();
            this.table.repaint();
        }
    }

    public void rafraichir()
    {
        this.sommets = new ArrayList<>();

        // Récupérer tous les éléments du tableau
        for (int i = 0; i < this.tableModel.getRowCount(); i++)
        {
            String nomSommet = (String) this.tableModel.getValueAt(i, 0);
            String nomLien = (String) this.tableModel.getValueAt(i, 1);
            Object distObj = this.tableModel.getValueAt(i, 2);

            int distance = 0;
            if (distObj != null)
            {
                if (distObj instanceof Integer)
                {
                    distance = (Integer) distObj;
                }
                else if (distObj instanceof String)
                {
                    try
                    {
                        distance = Integer.parseInt((String) distObj);
                    }
                    catch (NumberFormatException e)
                    {
                        distance = 0;
                    }
                }
            }

            // Trouver ou créer le sommet
            Sommet sommet = null;
            for (Sommet s : this.sommets)
            {
                if (s.getNom().equals(nomSommet))
                {
                    sommet = s;
                    break;
                }
            }

            if (sommet == null)
            {
                sommet = new Sommet(nomSommet);
                this.sommets.add(sommet);
            }

            // Ajouter le lien au sommet
            if (!nomLien.isEmpty())
            {
                Lien lien = new Lien(nomLien, distance);
                sommet.ajouterLien(lien);
            }
        }

        // Mettre à jour le graphe
        this.graphe.actualiser();
        this.table.revalidate();
        this.table.repaint();
    }

    public void sauvegarder()
    {
        this.appli.sauvegarder(this.sommets);

        for(Sommet s : this.sommets)
        {
            System.out.println("Sommet : " + s.getNom() + s.getLiens());
        }



        this.appli.afficher("Sauvegarder");
    }

    public ArrayList<Sommet> getSommets()
    {
        return this.sommets;
    }
}