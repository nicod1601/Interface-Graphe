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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Panneau du menu principal de l'application, gère les menus, boutons et actions utilisateur.
 * Permet l'ouverture/sauvegarde de fichiers, le changement de mode et l'affichage du chemin.
 * @author Nicolas D. & Marta AN.
 */

public class Menu extends JPanel implements ActionListener
{
	private JMenuBar menuBar;
	private JMenu menuFichier;
	private JMenu menuEdition;
	private JMenu menuMode;
	private JButton btnMode;
	private JButton btnVoir;

	private Controleur ctrl;
	private Appli appli;

	/**
	 * Constructeur du panneau Menu.
	 * @param ctrl Le contrôleur de l'application.
	 * @param appli L'instance principale de l'application.
	 */
	public Menu(Controleur ctrl, Appli appli)
	{
		this.ctrl = ctrl;
		this.appli = appli;
		this.setLayout(new BorderLayout());
		this.setBackground(Theme.PANEL);
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 0, Theme.BORDER),
				BorderFactory.createEmptyBorder(6, 12, 6, 12)));

		// ── Panel gauche : menus ──────────────────────────────────────────────
		JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelMenu.setBackground(Theme.PANEL);

		this.menuBar = new JMenuBar();
		this.menuBar.setBackground(Theme.PANEL);
		this.menuBar.setBorder(BorderFactory.createEmptyBorder());
		this.menuBar.setOpaque(true);

		this.menuFichier = Theme.menu("📁 Fichier");
		this.menuFichier.add(Theme.menuItem("📄 Ouvrir Fichier", Theme.ACCENT));
		this.menuFichier.add(Theme.menuItem("💾 Sauvegarder", Theme.SUCCESS));
		this.menuFichier.add(Theme.menuItem("💾 Enregistrer sous", Theme.SUCCESS));
		this.menuFichier.addSeparator();
		this.menuFichier.add(Theme.menuItem("❌ Quitter", Theme.DANGER));

		this.menuEdition = Theme.menu("✏️ Éditer");
		this.menuEdition.add(Theme.menuItem("✏️ Editer", Theme.ACCENT));

		this.menuMode = Theme.menu("💡 Modes");
		this.menuMode.add(Theme.menuItem("1️⃣ Dijkstra", Theme.SUCCESS));
		this.menuMode.add(Theme.menuItem("2️⃣ Bellman-Ford", Theme.WARNING));
		this.menuMode.add(Theme.menuItem("3️⃣ Désactiver", Theme.TEXT_MUTED));

		this.menuBar.add(this.menuFichier);
		this.menuBar.add(this.menuEdition);
		this.menuBar.add(this.menuMode);
		panelMenu.add(this.menuBar);

		// ── Panel droit : badge de mode ───────────────────────────────────────
		JPanel panelMode = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panelMode.setBackground(Theme.PANEL);

		this.btnVoir = Theme.button("👁 Voir chemin", Theme.ACCENT);
		this.btnVoir.setEnabled(false);
		panelMode.add(this.btnVoir);
		this.btnVoir.addActionListener(this);

		JLabel lblModeLabel = Theme.label("Mode actuel :");
		panelMode.add(lblModeLabel);

		this.btnMode = Theme.modeBadge("Aucun", Theme.TEXT_MUTED);
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
	}

	/**
	 * Gère les actions des menus et boutons (ouvrir, sauvegarder, modes, etc.).
	 * @param e L'événement d'action déclenché.
	 */
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand()) {
			case "📄 Ouvrir Fichier":
				this.ouvrirFichier();
				this.updateModeBadge("Aucun", Theme.TEXT_MUTED);
				this.btnVoir.setEnabled(false);
				this.appli.voirChemin(new ArrayList<>());
				break;
			case "💾 Enregistrer sous":
				this.enregistrerSous();
				this.afficherToast("✅ Fichier enregistré sous ");
				break;
			case "💾 Sauvegarder":
				this.ctrl.sauvegarderXML();
				this.afficherToast("✅ Fichier sauvegardé !");
				break;
			case "❌ Quitter":
				System.exit(0);
				break;
			case "✏️ Editer":
				this.appli.afficher("Edit");
				break;
			case "1️⃣ Dijkstra":
				this.updateModeBadge("Dijkstra", Theme.SUCCESS);
				this.btnVoir.setEnabled(true);
				this.ctrl.Mode("Dijkstra");
				this.appli.afficherTabMods();
				break;
			case "2️⃣ Bellman-Ford":
				this.updateModeBadge("Bellman-Ford", Theme.WARNING);
				this.btnVoir.setEnabled(true);
				this.ctrl.Mode("Bellman-Ford");
				break;
			case "3️⃣ Désactiver":
				this.updateModeBadge("Aucun", Theme.TEXT_MUTED);
				this.btnVoir.setEnabled(false);
				this.appli.voirChemin(new ArrayList<>());
				this.appli.desactiverTabDistance();
				break;
			case "👁 Voir chemin":
				System.out.println("DEBUG: 'Voir chemin' button clicked");
				ArrayList<String> chemin = this.ctrl.getCheminCourt();
				System.out.println("DEBUG: Path returned: " + chemin);
				System.out.println("DEBUG: Path size: " + chemin.size());
				this.appli.voirChemin(chemin);
				break;
		}
	}

	/**
	 * Ouvre un sélecteur de fichier pour charger un graphe depuis un fichier XML.
	 */
	public void ouvrirFichier()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./appli/donnee"));
		chooser.setDialogTitle("Sélectionner un fichier de graphe");
		chooser.setApproveButtonText("Ouvrir");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String nomFichier = "" + chooser.getSelectedFile();
			this.ctrl.getSommets().clear();
			this.ctrl.setLiens(nomFichier);
			this.appli.afficher("Graphe");
			this.appli.fichier();
		}
	}


	/**
	 * Ouvre un sélecteur de fichier pour enregistrer le graphe sous un nouveau nom.
	 */
	public void enregistrerSous()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./appli/donnee"));
		chooser.setDialogTitle("Enregistrer sous...");
		chooser.setApproveButtonText("Enregistrer");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String chemin = chooser.getSelectedFile().getAbsolutePath();

			if (!chemin.toLowerCase().endsWith(".xml"))
			{
				chemin += ".xml";
			}

			this.ctrl.setLienSeulement(chemin);
			this.ctrl.sauvegarderXML();
			System.out.println("Enregistré sous : " + chemin);
		}
	}


	private void afficherToast(String message)
	{
		JDialog toast = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this));
		toast.setUndecorated(true);
		toast.setSize(280, 50);
		toast.setAlwaysOnTop(true);

		// Position en bas à droite de la frame
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		int x = frame.getX() + frame.getWidth()  - 300;
		int y = frame.getY() + frame.getHeight() - 80;
		toast.setLocation(x, y);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Theme.PANEL);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Theme.ACCENT, 1),
			BorderFactory.createEmptyBorder(10, 16, 10, 16)
		));

		JLabel label = new JLabel(message);
		label.setFont(Theme.FONT_BOLD);
		label.setForeground(Theme.ACCENT);
		panel.add(label, BorderLayout.CENTER);

		toast.setContentPane(panel);
		toast.setVisible(true);

		// Disparait après 2 secondes
		Timer timer = new Timer(2000, e -> toast.dispose());
		timer.setRepeats(false);
		timer.start();
	}

	private void updateModeBadge(String nom, java.awt.Color color) {
		Theme.updateBadgeColor(this.btnMode, nom, color);
	}
}