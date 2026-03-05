package appli.ihm;

import appli.Controleur;
import appli.metier.Lien;
import appli.metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Edit extends JPanel implements ActionListener
{
	private Controleur ctrl;

	/* Panel Document */
	private JEditorPane txtDocument;

	/* Panel Tableau */
	private JTable             table;
	private JButton            btnAjouter;
	private JButton            btnSupprimer;
	private JButton            btnAnnuler;
	private JButton            btnRafraichir;
	private JButton            btnSauvegarde;
	private DefaultTableModel  tableModel;
	private Appli              appli;

	/* Panel graphe */
	private GrapheCopie       graphe;
	private ArrayList<Sommet> sommets;

	public Edit(Controleur ctrl, Appli appli)
	{
		this.ctrl  = ctrl;
		this.appli = appli;
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(Theme.BACKGROUND);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		/* ── Panel Document ── */
		JPanel panelDocument = Theme.panel();
		panelDocument.setLayout(new BorderLayout(0, 10));
		panelDocument.setBorder(Theme.borderCard());

		this.txtDocument = new JEditorPane();
		this.txtDocument.setFont(Theme.FONT_NORMAL);
		this.txtDocument.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollDocument = new JScrollPane(this.txtDocument);
		scrollDocument.setBorder(Theme.borderInput());

		panelDocument.add(Theme.labelTitle("📄 Document"), BorderLayout.NORTH);
		panelDocument.add(scrollDocument, BorderLayout.CENTER);
		panelDocument.setPreferredSize(new Dimension(350, 0));

		/* ── Panel Graphe ── */
		this.sommets = new ArrayList<>();
		this.graphe  = new GrapheCopie(this);
		this.graphe.setBackground(Theme.PANEL);
		this.graphe.setBorder(Theme.borderCard());

		/* ── Panel Tableau ── */
		JPanel panelTableau = Theme.panel();
		panelTableau.setLayout(new BorderLayout(0, 10));
		panelTableau.setBorder(Theme.borderCard());

		// En-tête : titre + boutons
		JPanel panelTableauNorth = Theme.panel();
		panelTableauNorth.setLayout(new BorderLayout(10, 0));

		JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panelBtn.setBackground(Theme.PANEL);

		this.btnRafraichir = Theme.button("🔄 Rafraîchir", Theme.ACCENT);
		this.btnAnnuler    = Theme.button("↩️ Annuler",    Theme.TEXT_MUTED);
		this.btnSupprimer  = Theme.button("➖ Supprimer",  Theme.DANGER);
		this.btnAjouter    = Theme.button("➕ Ajouter",    Theme.SUCCESS);
		this.btnSauvegarde = Theme.button("✅ Sauvegarder",Theme.SUCCESS);

		panelBtn.add(this.btnRafraichir);
		panelBtn.add(this.btnAnnuler);
		panelBtn.add(this.btnSauvegarde);
		panelBtn.add(this.btnSupprimer);
		panelBtn.add(this.btnAjouter);

		panelTableauNorth.add(Theme.labelTitle("📊 Gestion des Sommets"), BorderLayout.WEST);
		panelTableauNorth.add(panelBtn, BorderLayout.EAST);

		// Tableau
		String[] colonne = {"Sommet", "Liens", "Distance"};
		this.tableModel = new DefaultTableModel(colonne, 0);
		this.table      = new JTable(this.tableModel);
		Theme.styleTable(this.table);

		// ── Validation de la colonne Distance (>= 0 uniquement) ─────────────
		this.table.getColumnModel().getColumn(2).setCellEditor(new DistanceEditor());

		JScrollPane scrollTable = new JScrollPane(this.table);
		scrollTable.setBorder(Theme.borderInput());
		scrollTable.getViewport().setBackground(Theme.PANEL);

		panelTableau.add(panelTableauNorth, BorderLayout.NORTH);
		panelTableau.add(scrollTable,       BorderLayout.CENTER);

		/* ── Panel TabGraphe ── */
		JPanel panelTabGraph = Theme.panelBackground();
		panelTabGraph.setLayout(new GridLayout(2, 1, 0, 10));
		panelTabGraph.add(panelTableau);
		panelTabGraph.add(this.graphe);

		/* ── Positionnement ── */
		this.add(panelDocument, BorderLayout.WEST);
		this.add(panelTabGraph, BorderLayout.CENTER);

		/* ── Listeners ── */
		this.btnAjouter.addActionListener(this);
		this.btnSupprimer.addActionListener(this);
		this.btnAnnuler.addActionListener(this);
		this.btnRafraichir.addActionListener(this);
		this.btnSauvegarde.addActionListener(this);

		this.setVisible(false);
	}

	// ── Éditeur de cellule avec validation distance >= 0 ────────────────────
	private static class DistanceEditor extends DefaultCellEditor
	{
		private static final Color COLOR_OK  = Color.WHITE;
		private static final Color COLOR_ERR = new Color(255, 200, 200);

		private final JTextField field;

		public DistanceEditor()
		{
			super(new JTextField());
			this.field = (JTextField) getComponent();
			this.field.setHorizontalAlignment(JTextField.RIGHT);
		}

		@Override
		public Component getTableCellEditorComponent(
				JTable table, Object value,
				boolean isSelected, int row, int column)
		{
			this.field.setBackground(COLOR_OK);
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		@Override
		public boolean stopCellEditing()
		{
			String texte = field.getText().trim();
			try
			{
				int val = Integer.parseInt(texte);
				if (val < 0) throw new NumberFormatException();
			}
			catch (NumberFormatException e)
			{
				field.setBackground(COLOR_ERR);
				field.selectAll();
				return false;   // refuse de quitter la cellule
			}
			field.setBackground(COLOR_OK);
			return super.stopCellEditing();
		}
	}

	// ────────────────────────────────────────────────────────────────────────

	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case "➕ Ajouter":
				this.modifierTableau("+");
				break;
			case "➖ Supprimer":
				this.modifierTableau("-");
				this.rafraichir();
				break;
			case "↩️ Annuler":
				this.appli.afficher("Annuler");
				break;
			case "🔄 Rafraîchir":
				this.rafraichir();
				break;
			case "✅ Sauvegarder":
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
				Object[] row = {sommet.getNom(), "", 0};
				this.tableModel.addRow(row);
			}
			else
			{
				for (Lien lien : sommet.getLiens())
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

		for (int i = 0; i < this.tableModel.getRowCount(); i++)
		{
			String nomSommet = (String) this.tableModel.getValueAt(i, 0);
			String nomLien   = (String) this.tableModel.getValueAt(i, 1);
			Object distObj   = this.tableModel.getValueAt(i, 2);

			int distance = parseDistance(distObj);

			// Filet de sécurité : forcer 0 si négatif (ex : import XML corrompu)
			if (distance < 0)
			{
				distance = 0;
				this.tableModel.setValueAt(0, i, 2);
			}

			Sommet sommet = null;
			for (Sommet s : this.sommets)
			{
				if (s.getNom().equals(nomSommet)) { sommet = s; break; }
			}

			if (sommet == null)
			{
				sommet = new Sommet(nomSommet);
				this.sommets.add(sommet);
			}

			if (nomLien != null && !nomLien.isEmpty())
			{
				sommet.ajouterLien(new Lien(nomLien, distance));
			}
		}

		this.graphe.actualiser();
		this.table.revalidate();
		this.table.repaint();
	}

	/** Parse la valeur d'une cellule Distance en int (retourne 0 si invalide). */
	private int parseDistance(Object distObj)
	{
		if (distObj == null)         return 0;
		if (distObj instanceof Integer) return (Integer) distObj;
		try { return Integer.parseInt(distObj.toString().trim()); }
		catch (NumberFormatException e) { return 0; }
	}

	public void sauvegarder()
	{
		this.appli.sauvegarder(this.sommets);
		this.appli.afficher("Sauvegarder");
	}

	public ArrayList<Sommet> getSommets()
	{
		return this.sommets;
	}
}