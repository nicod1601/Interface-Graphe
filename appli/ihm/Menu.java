package appli.ihm;

import appli.Controleur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Menu extends JPanel implements ActionListener
{
	private JMenuBar menuBar;
	private JMenu    menuFichier;
	private JMenu    menuEdition;
	private JMenu    menuMode;
	private JMenu    menuOptions;
	private JButton  btnMode;
	private JButton  btnVoir;

	private Controleur ctrl;
	private Appli      appli;

	public Menu(Controleur ctrl, Appli appli)
	{
		this.ctrl  = ctrl;
		this.appli = appli;
		this.setLayout(new BorderLayout());
		this.setBackground(Theme.PANEL);
		this.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
			BorderFactory.createEmptyBorder(6, 12, 6, 12)
		));

		// ── Panel gauche : menus ──────────────────────────────────────────────
		JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelMenu.setBackground(Theme.PANEL);

		this.menuBar = new JMenuBar();
		this.menuBar.setBackground(Theme.PANEL);
		this.menuBar.setBorder(BorderFactory.createEmptyBorder());
		this.menuBar.setOpaque(true);

		this.menuFichier = Theme.menu("📁 Fichier");
		this.menuFichier.add(Theme.menuItem("📄 Ouvrir Fichier"  , Theme.ACCENT));
		this.menuFichier.add(Theme.menuItem("💾 Sauvegarder"     , Theme.SUCCESS));
		this.menuFichier.add(Theme.menuItem("💾 Enregistrer sous", Theme.SUCCESS));
		this.menuFichier.addSeparator();
		this.menuFichier.add(Theme.menuItem("❌ Quitter",        Theme.DANGER));

		this.menuEdition = Theme.menu("✏️ Éditer");
		this.menuEdition.add(Theme.menuItem("✏️ Editer",         Theme.ACCENT));

		this.menuMode = Theme.menu("💡 Modes");
		this.menuMode.add(Theme.menuItem("1️⃣ Dijkstra",           Theme.SUCCESS));
		this.menuMode.add(Theme.menuItem("2️⃣ Bellman-Ford",       Theme.WARNING));
		this.menuMode.add(Theme.menuItem("3️⃣ Désactiver",         Theme.TEXT_MUTED));

		this.menuOptions = Theme.menu("💡 Paramètres");
			this.menuOptions.add(Theme.menuItem("⚙️ Thème", Theme.ACCENT));

		this.menuBar.add(this.menuFichier);
		this.menuBar.add(this.menuEdition);
		this.menuBar.add(this.menuMode);
		this.menuBar.add(this.menuOptions);
		panelMenu.add(this.menuBar);

		// ── Panel droit : badge de mode ───────────────────────────────────────
		JPanel panelMode = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panelMode.setBackground(Theme.PANEL);

		this.btnVoir = Theme.button("👁 Voir chemin", Theme.ACCENT);
		this.btnVoir.setEnabled(false);
		panelMode.add(this.btnVoir);
		this.btnVoir.addActionListener(this);

		JLabel lblModeLabel = Theme.label("Mode actuel :");
		lblModeLabel.setForeground(Theme.TEXT_MUTED);

		this.btnMode = Theme.modeBadge("Aucun", Theme.TEXT_MUTED);

		panelMode.add(lblModeLabel);
		panelMode.add(this.btnMode);

		this.add(panelMenu, BorderLayout.WEST);
		this.add(panelMode, BorderLayout.EAST);

		this.attachMenuListeners();
	}

	private void attachMenuListeners()
	{
		for (int i = 0; i < this.menuFichier.getItemCount(); i++)
			if (this.menuFichier.getItem(i) != null)
				this.menuFichier.getItem(i).addActionListener(this);

		for (int i = 0; i < this.menuEdition.getItemCount(); i++)
			if (this.menuEdition.getItem(i) != null)
				this.menuEdition.getItem(i).addActionListener(this);

		for (int i = 0; i < this.menuMode.getItemCount(); i++)
			if (this.menuMode.getItem(i) != null)
				this.menuMode.getItem(i).addActionListener(this);

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
				this.ctrl.sauvegarderXML();
				break;
			case "❌ Quitter":
				System.exit(0);
				break;
			case "✏️ Editer":
				this.appli.afficher("Edit");
				break;
			case "⚙️ Dijkstra":
				this.updateModeBadge("Dijkstra",    Theme.SUCCESS);
				this.ctrl.Mode("Dijikstra");
				break;
			case "2️⃣ Bellman-Ford":
				this.updateModeBadge("Bellman-Ford", Theme.WARNING);
				this.btnVoir.setEnabled(true);
				this.ctrl.Mode("Bellman-Ford");
				break;
			case "⚙️ Désactiver":
				this.updateModeBadge("Aucun", Theme.TEXT_MUTED);
				break;
			case "👁 Voir chemin":
				ArrayList<String> chemin = this.ctrl.getCheminCourt();
				this.appli.voirChemin(chemin);
				break;
		}
	}

	public void ouvrirFichier()
	{

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./appli/donnee"));
		chooser.setDialogTitle("Sélectionner un fichier de graphe");
		chooser.setApproveButtonText("Ouvrir");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String nomFichier = "" + chooser.getSelectedFile();
			this.ctrl.getSommets().clear();
			this.ctrl.setLiens(nomFichier);
			this.appli.afficher("Graphe");
			this.appli.fichier();
		}
	}

	private void updateModeBadge(String nom, java.awt.Color color)
	{
		this.btnMode.setText(nom);
		this.btnMode.setForeground(color);
		this.btnMode.setBackground(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 18));
		this.btnMode.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 80), 1, true),
			BorderFactory.createEmptyBorder(3, 12, 3, 12)
		));
		this.btnMode.repaint();
	}
}