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
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Edit extends JPanel implements ActionListener
{
	private Controleur ctrl;

	/* Panel Document */
	private JPanel      panelListe;
	private JScrollPane scrollDocument;

	/* Panel Tableau */
	private JTable            table;
	private JButton           btnAjouter;
	private JButton           btnSupprimer;
	private JButton           btnAnnuler;
	private JButton           btnRafraichir;
	private JButton           btnAppliquer;
	private DefaultTableModel tableModel;
	private Appli             appli;

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

		this.panelListe = new JPanel();
		this.panelListe.setLayout(new BoxLayout(this.panelListe, BoxLayout.Y_AXIS));
		this.panelListe.setBackground(Theme.EDITOR_BG);
		this.panelListe.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));

		this.scrollDocument = new JScrollPane(this.panelListe);
		this.scrollDocument.setBorder(Theme.borderInput());
		this.scrollDocument.getViewport().setBackground(Theme.EDITOR_BG);
		this.scrollDocument.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelDocument.add(Theme.labelTitle("📄 Document"), BorderLayout.NORTH);
		panelDocument.add(this.scrollDocument, BorderLayout.CENTER);
		panelDocument.setPreferredSize(new Dimension(260, 0));

		/* ── Panel Graphe ── */
		this.sommets = new ArrayList<>();
		this.graphe  = new GrapheCopie(this);
		this.graphe.setBackground(Theme.PANEL);
		this.graphe.setBorder(Theme.borderCard());

		/* ── Panel Tableau ── */
		JPanel panelTableau = Theme.panel();
		panelTableau.setLayout(new BorderLayout(0, 10));
		panelTableau.setBorder(Theme.borderCard());

		JPanel panelTableauNorth = Theme.panel();
		panelTableauNorth.setLayout(new BorderLayout(10, 0));

		JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panelBtn.setBackground(Theme.PANEL);

		this.btnRafraichir = Theme.button("🔄 Rafraîchir", Theme.ACCENT);
		this.btnAnnuler    = Theme.button("↩️ Annuler",    Theme.TEXT_MUTED);
		this.btnSupprimer  = Theme.button("➖ Supprimer",  Theme.DANGER);
		this.btnAjouter    = Theme.button("➕ Ajouter",    Theme.SUCCESS);
		this.btnAppliquer = Theme.button("✅ Appliquer",Theme.SUCCESS);

		panelBtn.add(this.btnRafraichir);
		panelBtn.add(this.btnAnnuler);
		panelBtn.add(this.btnAppliquer);
		panelBtn.add(this.btnSupprimer);
		panelBtn.add(this.btnAjouter);

		panelTableauNorth.add(Theme.labelTitle("📊 Gestion des Sommets"), BorderLayout.WEST);
		panelTableauNorth.add(panelBtn, BorderLayout.EAST);

		String[] colonne = {"Sommet", "Liens", "Distance"};
		this.tableModel = new DefaultTableModel(colonne, 0);
		this.table      = new JTable(this.tableModel);
		Theme.styleTable(this.table);
		this.table.getColumnModel().getColumn(2).setCellEditor(new DistanceEditor());

		// Rafraîchir automatiquement après chaque modification de cellule
		this.tableModel.addTableModelListener(new TableModelListener()
		{
			private boolean enCours = false;

			public void tableChanged(TableModelEvent e)
			{
				if (e.getType() != TableModelEvent.UPDATE) return;
				if (enCours) return;
				enCours = true;
				javax.swing.SwingUtilities.invokeLater(() -> {
					rafraichir();
					enCours = false;
				});
			}
		});

		JScrollPane scrollTable = new JScrollPane(this.table);
		scrollTable.setBorder(Theme.borderInput());
		scrollTable.getViewport().setBackground(Theme.PANEL);

		panelTableau.add(panelTableauNorth, BorderLayout.NORTH);
		panelTableau.add(scrollTable,       BorderLayout.CENTER);

		/* ── Panel TabGraphe ── */
		JPanel panelTabGraph = Theme.panelBackground();
		panelTabGraph.setLayout(new GridLayout(2, 1, 0, 10));
		this.graphe.setPreferredSize(new Dimension(0, 220));
		panelTabGraph.add(panelTableau,  BorderLayout.CENTER);
		panelTabGraph.add(this.graphe,   BorderLayout.SOUTH);

		this.add(panelDocument, BorderLayout.WEST);
		this.add(panelTabGraph, BorderLayout.CENTER);

		this.btnAjouter.addActionListener(this);
		this.btnSupprimer.addActionListener(this);
		this.btnAnnuler.addActionListener(this);
		this.btnRafraichir.addActionListener(this);
		this.btnAppliquer.addActionListener(this);

		this.setVisible(false);
	}

	// ── Liste de cartes : une carte par lien ────────────────────────────────
	private void actualiserListe()
	{
		this.panelListe.removeAll();

		for (Sommet sommet : this.sommets)
		{
			if (sommet.getLiens().isEmpty())
			{
				this.panelListe.add(creerCarte(sommet.getNom(), "∅", -1));
			}
			else
			{
				for (Lien lien : sommet.getLiens())
					this.panelListe.add(creerCarte(sommet.getNom(), lien.getNom(), lien.getDistance()));
			}
			// Espace entre les cartes
			this.panelListe.add(javax.swing.Box.createRigidArea(new Dimension(0, 6)));
		}

		this.panelListe.add(javax.swing.Box.createVerticalGlue());
		this.panelListe.revalidate();
		this.panelListe.repaint();
	}

	/**
	 * Carte encadrée :
	 *   ┌──────────────────┐
	 *   │  A  ──▶  B  (5)  │
	 *   └──────────────────┘
	 */
	private JPanel creerCarte(String source, String dest, int distance)
	{
		JPanel carte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		carte.setBackground(Theme.PANEL);
		carte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
		carte.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));

		JLabel lblSrc = new JLabel(source);
		lblSrc.setFont(Theme.FONT_BOLD.deriveFont(16f));
		lblSrc.setForeground(Theme.ACCENT);

		JLabel lblFleche = new JLabel("--▶");
		lblFleche.setFont(Theme.FONT_NORMAL.deriveFont(15f));
		lblFleche.setForeground(Theme.TEXT_MUTED);

		JLabel lblDest = new JLabel(dest);
		lblDest.setFont(Theme.FONT_BOLD.deriveFont(16f));
		lblDest.setForeground(distance < 0 ? Theme.TEXT_MUTED : Theme.TEXT);

		carte.add(lblSrc);
		carte.add(lblFleche);
		carte.add(lblDest);

		if (distance >= 0)
		{
			JLabel lblDist = new JLabel("(" + distance + ")");
			lblDist.setFont(Theme.FONT_NORMAL.deriveFont(15f));
			lblDist.setForeground(Theme.WARNING);
			carte.add(lblDist);
		}

		return carte;
	}

	// ── Éditeur cellule Distance >= 0 ────────────────────────────────────────
	private static class DistanceEditor extends DefaultCellEditor
	{
		private static final Color COLOR_OK  = Theme.PANEL_ALT;
		private static final Color COLOR_ERR = new Color(80, 30, 30);
		private final JTextField field;

		public DistanceEditor()
		{
			super(new JTextField());
			this.field = (JTextField) getComponent();
			this.field.setHorizontalAlignment(JTextField.RIGHT);
			this.field.setBackground(COLOR_OK);
			this.field.setForeground(Theme.TEXT);
			this.field.setCaretColor(Theme.ACCENT);
			this.field.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		}

		public Component getTableCellEditorComponent(
				JTable table, Object value, boolean isSelected, int row, int column)
		{
			this.field.setBackground(COLOR_OK);
			super.getTableCellEditorComponent(table, value, isSelected, row, column);
			javax.swing.SwingUtilities.invokeLater(() -> {
				this.field.selectAll();
			});
			return this.field;
		}

		public boolean stopCellEditing()
		{
			try
			{
				int val = Integer.parseInt(field.getText().trim());
				if (val < 0) throw new NumberFormatException();
			}
			catch (NumberFormatException e)
			{
				field.setBackground(COLOR_ERR);
				field.selectAll();
				return false;
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
			case "➕ Ajouter":    this.modifierTableau("+");        break;
			case "➖ Supprimer":  this.modifierTableau("-");
			                      this.rafraichir();                 break;
			case "↩️ Annuler":   this.appli.afficher("Annuler");    break;
			case "🔄 Rafraîchir": this.rafraichir();                 break;
			case "✅ Appliquer": this.appliquer();               break;
		}
	}

	public void setDocument(String document)
	{
		this.creationTableau();
	}

	public void creationTableau()
	{
		this.sommets = new ArrayList<>();
		this.sommets = this.ctrl.getSommets();
		this.graphe.actualiser();
		this.actualiserListe();

		this.tableModel.setRowCount(0);
		for (Sommet sommet : this.sommets)
		{
			if (sommet.getLiens().isEmpty())
			{
				this.tableModel.addRow(new Object[]{sommet.getNom(), "", 0});
			}
			else
			{
				for (Lien lien : sommet.getLiens())
					this.tableModel.addRow(new Object[]{sommet.getNom(), lien.getNom(), lien.getDistance()});
			}
		}
	}

	public void modifierTableau(String action)
	{
		if (action.equals("+"))
			this.tableModel.addRow(new Object[]{"", "", 0});

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
			int    distance  = parseDistance(this.tableModel.getValueAt(i, 2));

			if (nomSommet == null || nomSommet.trim().isEmpty()) continue;

			if (distance < 0) { distance = 0; this.tableModel.setValueAt(0, i, 2); }

			Sommet sommet = null;
			for (Sommet s : this.sommets)
				if (s.getNom().equals(nomSommet)) { sommet = s; break; }

			if (sommet == null) { sommet = new Sommet(nomSommet); this.sommets.add(sommet); }

			if (nomLien != null && !nomLien.isEmpty())
				sommet.ajouterLien(new Lien(nomLien, distance));
		}

		this.actualiserListe();
		this.graphe.actualiser();
		this.table.revalidate();
		this.table.repaint();
	}


	private int parseDistance(Object o)
	{
		if (o == null)            return 0;
		if (o instanceof Integer) return (Integer) o;
		try { return Integer.parseInt(o.toString().trim()); }
		catch (NumberFormatException e) { return 0; }
	}

	public void appliquer()
	{
		this.appli.appliquer(this.sommets);
		this.appli.afficher("Appliquer");
	}

	public ArrayList<Sommet> getSommets() { return this.sommets; }
}