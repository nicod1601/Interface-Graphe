package appli.ihm;

import appli.Controleur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Menu extends JPanel implements ActionListener
{
    // Couleurs modernes
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color MENU_BG = new Color(249, 250, 251);
    private static final Color ACCENT_COLOR = new Color(59, 130, 246);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(251, 146, 60);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color MODE_INACTIVE = new Color(107, 114, 128);

    private JMenuBar menuBar;
    private JMenu menuFichier;
    private JMenu menuEdition;
    private JMenu menuOptions;

    private JButton btnMode;
    private JLabel lblModeLabel;

    private Controleur ctrl;
    private Appli appli;
    
    public Menu(Controleur ctrl, Appli appli)
    {
        this.ctrl = ctrl;
        this.appli = appli;
        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Panel gauche pour le menu
        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelMenu.setBackground(BACKGROUND_COLOR);

        // Panel droit pour le mode
        JPanel panelMode = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelMode.setBackground(BACKGROUND_COLOR);

        /*
        * Menu Bar :
        */

        this.menuBar = new JMenuBar();
        this.menuBar.setBackground(BACKGROUND_COLOR);
        this.menuBar.setBorder(BorderFactory.createEmptyBorder());

        // Configuration du menu Fichier
        this.menuFichier = createStyledMenu("📁 Fichier");
        this.menuFichier.add(createStyledMenuItem("📄 Ouvrir Fichier", ACCENT_COLOR));
        this.menuFichier.add(createStyledMenuItem("💾 Enregistrer", SUCCESS_COLOR));
        this.menuFichier.addSeparator();
        this.menuFichier.add(createStyledMenuItem("❌ Quitter", DANGER_COLOR));

        // Configuration du menu Edition
        this.menuEdition = createStyledMenu("✏️ Éditer");
        this.menuEdition.add(createStyledMenuItem("✏️ Editer", ACCENT_COLOR));

        // Configuration du menu Options
        this.menuOptions = createStyledMenu("⚙️ Options");
        this.menuOptions.add(createStyledMenuItem("⚙️ Dijikstra", SUCCESS_COLOR));
        this.menuOptions.add(createStyledMenuItem("⚙️ BellmanFordMetier", WARNING_COLOR));
        this.menuOptions.add(createStyledMenuItem("⚙️ Désactiver", MODE_INACTIVE));

        // Ajout des menus à la barre
        this.menuBar.add(this.menuFichier);
        this.menuBar.add(this.menuEdition);
        this.menuBar.add(this.menuOptions);

        panelMenu.add(this.menuBar);

        /*
        * Indicateur de Mode :
        */

        // Label "Mode actuel:"
        this.lblModeLabel = new JLabel("Mode actuel:");
        this.lblModeLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        this.lblModeLabel.setForeground(new Color(107, 114, 128));

        // Bouton/Badge de mode
        this.btnMode = createModeBadge("Aucun", MODE_INACTIVE);

        panelMode.add(this.lblModeLabel);
        panelMode.add(this.btnMode);

        // Ajout des panels au menu principal
        this.add(panelMenu, BorderLayout.WEST);
        this.add(panelMode, BorderLayout.EAST);

        /*
        * Actions des menus
        */
        attachMenuListeners();
    }

    /**
     * Crée un menu stylisé
     */
    private JMenu createStyledMenu(String text)
    {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        menu.setForeground(TEXT_COLOR);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menu.setOpaque(false);
        
        // Effet hover
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu.setOpaque(true);
                menu.setBackground(MENU_BG);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu.setOpaque(false);
            }
        });
        
        return menu;
    }

    /**
     * Crée un item de menu stylisé avec couleur
     */
    private JMenuItem createStyledMenuItem(String text, Color accentColor)
    {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        item.setForeground(TEXT_COLOR);
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        item.setOpaque(true);
        
        // Effet hover avec la couleur d'accent
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(
                    accentColor.getRed(), 
                    accentColor.getGreen(), 
                    accentColor.getBlue(), 
                    20
                ));
                item.setForeground(accentColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
                item.setForeground(TEXT_COLOR);
            }
        });
        
        return item;
    }

    /**
     * Crée un badge de mode stylisé
     */
    private JButton createModeBadge(String text, Color bgColor)
    {
        JButton badge = new JButton(text);
        badge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        badge.setBackground(bgColor);
        badge.setForeground(Color.WHITE);
        badge.setFocusPainted(false);
        badge.setBorderPainted(false);
        badge.setContentAreaFilled(true);
        badge.setOpaque(true);
        badge.setEnabled(false);
        badge.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        badge.setPreferredSize(new Dimension(120, 28));
        
        return badge;
    }

    /**
     * Met à jour le badge de mode avec animation
     */
    private void updateModeBadge(String modeName, Color color)
    {
        this.btnMode.setText(modeName);
        this.btnMode.setBackground(color);
        this.btnMode.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        
        // Petit effet de "flash" pour attirer l'attention
        new Thread(() -> {
            try {
                Color original = color;
                for (int i = 0; i < 2; i++) {
                    this.btnMode.setBackground(color.brighter());
                    Thread.sleep(100);
                    this.btnMode.setBackground(original);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Attache les listeners aux items de menu
     */
    private void attachMenuListeners()
    {
        for (int i = 0; i < this.menuFichier.getItemCount(); i++)
        {
            if (this.menuFichier.getItem(i) != null)
            {
                this.menuFichier.getItem(i).addActionListener(this);
            }
        }

        for (int i = 0; i < this.menuEdition.getItemCount(); i++)
        {
            if (this.menuEdition.getItem(i) != null)
            {
                this.menuEdition.getItem(i).addActionListener(this);
            }
        }

        for (int i = 0; i < this.menuOptions.getItemCount(); i++)
        {
            if (this.menuOptions.getItem(i) != null)
            {
                this.menuOptions.getItem(i).addActionListener(this);
            }
        }
    }

    public void actionPerformed(ActionEvent e) 
    {
        switch (e.getActionCommand())
        {
            case "📄 Ouvrir Fichier":
                this.OuvrerFichier();
                break;
                
            case "💾 Enregistrer":
                System.out.println("Enregistrer");
                // TODO: Implémenter la sauvegarde
                break;
                
            case "❌ Quitter":
                System.out.println("Quitter");
                System.exit(0);
                break;
                
            case "✏️ Editer":
                this.appli.afficher("Edit");
                break;
                
            case "⚙️ Dijikstra":
                System.out.println("Mode Dijikstra activé");
                updateModeBadge("Dijkstra", SUCCESS_COLOR);
                this.ctrl.Mode("Dijikstra");
                break;
                
            case "⚙️ BellmanFordMetier":
                System.out.println("Mode Bellman-Ford activé");
                updateModeBadge("Bellman-Ford", WARNING_COLOR);
                this.ctrl.Mode("Bellman-Ford");
                break;
                
            case "⚙️ Désactiver":
                System.out.println("Mode désactivé");
                updateModeBadge("Aucun", MODE_INACTIVE);
                break;
        }
    }

    public void OuvrerFichier()
    {
        System.out.println("|-----Ouvrir Fichier-----|");

        // Configuration du FileChooser avec un style moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("./appli/donnee"));
        chooser.setDialogTitle("Sélectionner un fichier de graphe");
        chooser.setApproveButtonText("Ouvrir");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
        int resultat = chooser.showOpenDialog(this);

        if (resultat == JFileChooser.APPROVE_OPTION) 
        {
            File fichierSelectionne = chooser.getSelectedFile();
            String nomFichier = "" + fichierSelectionne;
            System.out.println("Fichier sélectionné : " + nomFichier);
            
            this.ctrl.getSommets().clear();
            this.ctrl.setLiens(nomFichier);
            this.appli.afficher("Graphe");
            this.appli.fichier();
            
            System.out.println("Graphe chargé avec succès !");
        }
        else
        {
            System.out.println("Ouverture de fichier annulée");
        }
    }
}