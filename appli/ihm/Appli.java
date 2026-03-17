package appli.ihm;

import appli.Controleur;
import appli.metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Classe principale de l'application, gère la fenêtre et les différents panneaux (Menu, Edit, Graphe).
 * C'est le point d'entrée de l'interface graphique.
 * @author Nicolas D. & Marta AN.
 */

public class Appli extends JFrame
{
	private Menu       menu;
	private Controleur ctrl;
	private Edit       edit;
	private Graphe     graphe;
	private PanelDistance tabDistance;

	public Appli(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("Interface Graphe");
		Image icon = Toolkit.getDefaultToolkit().getImage("./appli/logo.png");
		this.setIconImage(icon);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,610);
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.getContentPane().setBackground(Theme.BACKGROUND);
		

		this.menu   = new Menu(this.ctrl, this);
		this.edit   = null;
		this.graphe = null;
		this.tabDistance = null;

		this.add(this.menu, BorderLayout.NORTH);

		//this.setUndecorated(true);
		this.setVisible(true);
	}

	/**
	 * Affiche les différents panneaux de l'interface selon le nom spécifié.
	 * @param nom Le nom du panneau à afficher ("Graphe", "Edit", "Annuler", "Appliquer").
	 */
	public void afficher(String nom)
	{
		switch (nom)
		{
			case "Graphe":
				if (this.edit   != null) { this.edit.setVisible(false);   this.remove(this.edit);   }
				if (this.graphe != null) { this.remove(this.graphe); }
				this.graphe = new Graphe(this.ctrl);
				this.add(this.graphe, BorderLayout.CENTER);
				this.graphe.setVisible(true);
				break;

			case "Edit":
				if (this.graphe != null) { this.graphe.setVisible(false); this.remove(this.graphe); }
				if (this.edit   != null) { this.remove(this.edit); }
				this.edit = new Edit(this.ctrl, this);
				this.add(this.edit, BorderLayout.CENTER);
				this.edit.setVisible(true);
				this.edit.setDocument(this.ctrl.getDocument());
				break;

			case "Annuler":
			case "Appliquer":
				if (this.edit   != null) { this.edit.setVisible(false);   this.remove(this.edit);   }
				if (this.graphe != null) { this.remove(this.graphe); }
				this.graphe = new Graphe(this.ctrl);
				this.add(this.graphe, BorderLayout.CENTER);
				this.graphe.setVisible(true);
				break;
		}

		this.revalidate();
		this.repaint();
	}

	/**
	 * Gère l'ouverture ou la sauvegarde d'un fichier via le panneau graphe.
	 */
	public void fichier()
	{
		this.graphe.fichier();
		this.revalidate();
		this.repaint();
	}

	/**
	 * Applique les modifications de la liste de sommets au contrôleur et sauvegarde dans le graphe.
	 * @param liste La liste des sommets à appliquer.
	 */
	public void appliquer(ArrayList<Sommet> liste)
	{
		if (this.ctrl   != null) this.ctrl.setSommets(liste);
		if (this.graphe != null) this.graphe.sauvegarder(liste);
		this.revalidate();
		this.repaint();
	}

	/**
	 * Affiche le chemin calculé dans le panneau graphe.
	 * @param chemin La liste des éléments du chemin à afficher.
	 */
	public void voirChemin(ArrayList<String> chemin)
	{
		if (this.graphe != null)
		{
			this.graphe.voirChemin(chemin);
			this.revalidate();
			this.repaint();
		}
	}

	public void desactiverTabDistance()
	{
		if (this.tabDistance != null)
		{
			this.tabDistance.setVisible(false);
			this.remove(this.tabDistance);
			this.tabDistance = null;
			this.revalidate();
			this.repaint();
		}
	}

	public void afficherTabMods()
	{
		if (this.tabDistance != null) { this.tabDistance.setVisible(false);   this.remove(this.tabDistance);   }
		this.tabDistance = new PanelDistance(this.ctrl);
		this.add(this.tabDistance, BorderLayout.EAST);
		this.tabDistance.setVisible(true);
		this.revalidate();
		this.repaint();
	}
}