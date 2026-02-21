package appli.ihm;

import appli.Controleur;
import appli.metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Appli extends JFrame
{
	private Menu       menu;
	private Controleur ctrl;
	private Edit       edit;
	private Graphe     graphe;

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

		this.add(this.menu, BorderLayout.NORTH);
		this.setVisible(true);
	}

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
			case "Sauvegarder":
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

	public void fichier()
	{
		this.graphe.fichier();
		this.revalidate();
		this.repaint();
	}

	public void sauvegarder(ArrayList<Sommet> liste)
	{
		if (this.ctrl   != null) this.ctrl.setSommets(liste);
		if (this.graphe != null) this.graphe.sauvegarder(liste);
		this.revalidate();
		this.repaint();
	}
}