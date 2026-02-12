package appli.ihm;

import appli.Controleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Menu extends JPanel implements ActionListener
{
    private JMenuBar menuBar;
    private JMenu menuFichier;
    private JMenu menuEdition;
    private JMenu menuOptions;
    private JButton btnMode;

    private Controleur ctrl;
    private Appli appli;

    public Menu(Controleur ctrl, Appli appli)
    {
        this.ctrl  = ctrl;
        this.appli = appli;
        this.setLayout(new BorderLayout());
        this.setBackground(Theme.PANEL);
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        /* ── Panel gauche : menus ── */
        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelMenu.setBackground(Theme.PANEL);

        this.menuBar = new JMenuBar();
        this.menuBar.setBackground(Theme.PANEL);
        this.menuBar.setBorder(BorderFactory.createEmptyBorder());

        this.menuFichier = Theme.menu("📁 Fichier");
        this.menuFichier.add(Theme.menuItem("📄 Ouvrir Fichier", Theme.ACCENT));
        this.menuFichier.add(Theme.menuItem("💾 Enregistrer",    Theme.SUCCESS));
        this.menuFichier.addSeparator();
        this.menuFichier.add(Theme.menuItem("❌ Quitter",        Theme.DANGER));

        this.menuEdition = Theme.menu("✏️ Éditer");
        this.menuEdition.add(Theme.menuItem("✏️ Editer",         Theme.ACCENT));

        this.menuOptions = Theme.menu("⚙️ Options");
        this.menuOptions.add(Theme.menuItem("⚙️ Dijikstra",      Theme.SUCCESS));
        this.menuOptions.add(Theme.menuItem("⚙️ BellmanFordMetier", Theme.WARNING));
        this.menuOptions.add(Theme.menuItem("⚙️ Désactiver",     Theme.TEXT_MUTED));

        this.menuBar.add(this.menuFichier);
        this.menuBar.add(this.menuEdition);
        this.menuBar.add(this.menuOptions);
        panelMenu.add(this.menuBar);

        /* ── Panel droit : badge de mode ── */
        JPanel panelMode = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelMode.setBackground(Theme.PANEL);

        JLabel lblModeLabel = Theme.label("Mode actuel:");
        this.btnMode = Theme.modeBadge("Aucun", Theme.TEXT_MUTED);

        panelMode.add(lblModeLabel);
        panelMode.add(this.btnMode);

        this.add(panelMenu, BorderLayout.WEST);
        this.add(panelMode, BorderLayout.EAST);

        attachMenuListeners();
    }

    private void updateModeBadge(String nom, java.awt.Color color)
    {
        this.btnMode.setText(nom);
        this.btnMode.setBackground(color);
        this.btnMode.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1, true),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        new Thread(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    this.btnMode.setBackground(color.brighter());
                    Thread.sleep(100);
                    this.btnMode.setBackground(color);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();
    }

    private void attachMenuListeners()
    {
        for (int i = 0; i < this.menuFichier.getItemCount(); i++)
            if (this.menuFichier.getItem(i) != null)
                this.menuFichier.getItem(i).addActionListener(this);

        for (int i = 0; i < this.menuEdition.getItemCount(); i++)
            if (this.menuEdition.getItem(i) != null)
                this.menuEdition.getItem(i).addActionListener(this);

        for (int i = 0; i < this.menuOptions.getItemCount(); i++)
            if (this.menuOptions.getItem(i) != null)
                this.menuOptions.getItem(i).addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "📄 Ouvrir Fichier":
                this.ouvrirFichier();
                break;
            case "💾 Enregistrer":
                System.out.println("Enregistrer");
                break;
            case "❌ Quitter":
                System.exit(0);
                break;
            case "✏️ Editer":
                this.appli.afficher("Edit");
                break;
            case "⚙️ Dijikstra":
                updateModeBadge("Dijkstra",    Theme.SUCCESS);
                this.ctrl.Mode("Dijikstra");
                break;
            case "⚙️ BellmanFordMetier":
                updateModeBadge("Bellman-Ford", Theme.WARNING);
                this.ctrl.Mode("Bellman-Ford");
                break;
            case "⚙️ Désactiver":
                updateModeBadge("Aucun",        Theme.TEXT_MUTED);
                break;
        }
    }

    public void ouvrirFichier()
    {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { e.printStackTrace(); }

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("./appli/donnee"));
        chooser.setDialogTitle("Sélectionner un fichier de graphe");
        chooser.setApproveButtonText("Ouvrir");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            String nomFichier = "" + chooser.getSelectedFile();
            System.out.println("Fichier sélectionné : " + nomFichier);
            this.ctrl.getSommets().clear();
            this.ctrl.setLiens(nomFichier);
            this.appli.afficher("Graphe");
            this.appli.fichier();
        }
    }
}
