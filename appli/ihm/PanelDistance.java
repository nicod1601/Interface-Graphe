package appli.ihm;

import appli.metier.Sommet;
import appli.metier.Lien;
import appli.Controleur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * PanelDistance — affiche le tableau des distances itération par itération
 * selon l'algorithme choisi (Dijkstra ou Bellman-Ford).
 *
 * Colonnes : [Étape | d(S1) | d(S2) | ... | d(Sn)]
 * Lignes   : Initialisation, Itération 1, Itération 2, ..., Itération n-1
 */
public class PanelDistance extends JPanel implements ActionListener
{
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scroll;
	private JLabel lblTitre;
	private Controleur ctrl;
	private JComboBox<String> cbDepart;
	private JComboBox<String> cbArrivee;
	private JComboBox<String> cbAlgo;
	private JButton btnCalculer;

	public PanelDistance(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setLayout(new BorderLayout(0, 10));
		this.setBackground(Theme.BACKGROUND);
		this.setBorder(javax.swing.BorderFactory.createCompoundBorder(
			Theme.borderCard(),
			javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)
		));

		// Titre
		this.lblTitre = Theme.labelTitle("📊 Tableau des distances — Algorithme");
		this.lblTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
		this.add(this.lblTitre, BorderLayout.NORTH);

		// Panel de sélection
		JPanel panelSelection = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelSelection.setBackground(Theme.BACKGROUND);

		// Récupérer les noms des sommets
		List<String> noms = new ArrayList<>();
		for (Sommet s : ctrl.getSommets()) {
			if (s.getNom() != null && !s.getNom().trim().isEmpty()) {
				noms.add(s.getNom());
			}
		}

		this.cbDepart = new JComboBox<>(noms.toArray(new String[0]));
		this.cbArrivee = new JComboBox<>(noms.toArray(new String[0]));
		this.cbAlgo = new JComboBox<>(new String[]{"Dijkstra", "Bellman-Ford"});
		this.btnCalculer = new JButton("Calculer");

		panelSelection.add(new JLabel("Départ : "));
		panelSelection.add(this.cbDepart);
		panelSelection.add(new JLabel("Arrivée : "));
		panelSelection.add(this.cbArrivee);
		panelSelection.add(new JLabel("Algorithme : "));
		panelSelection.add(this.cbAlgo);
		panelSelection.add(this.btnCalculer);

		this.add(panelSelection, BorderLayout.SOUTH);

		// Table vide par défaut
		this.tableModel = new DefaultTableModel();
		this.table = new JTable(this.tableModel);
		Theme.styleTable(this.table);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		this.scroll = new JScrollPane(this.table);
		this.scroll.setBorder(Theme.borderInput());
		this.scroll.getViewport().setBackground(Theme.PANEL);
		this.add(this.scroll, BorderLayout.CENTER);

		this.btnCalculer.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnCalculer)
		{
			String depart = (String) this.cbDepart.getSelectedItem();
			String arrivee = (String) this.cbArrivee.getSelectedItem();
			String algo = (String) this.cbAlgo.getSelectedItem();

			if (depart != null && arrivee != null && algo != null)
			{
				this.ctrl.Mode(algo);
				this.ctrl.setLienSeulement(depart + " " + arrivee);
				ArrayList<Sommet> sommets = this.ctrl.getSommets();

				if (algo.equals("Dijkstra"))
				{
					// Calcul avec Dijkstra
					List<String> noms = new ArrayList<>();
					for (Sommet s : sommets)
					{
						if (s.getNom() != null && !s.getNom().trim().isEmpty())
						{
							noms.add(s.getNom());
						}
					}
					this.tableModel.setRowCount(0);
					this.tableModel.setColumnCount(0);
					String[] colonnes = new String[noms.size() + 1];
					colonnes[0] = "Étape";
					for (int i = 0; i < noms.size(); i++)
					{
						colonnes[i + 1] = "d(" + noms.get(i) + ")";
					}
					this.tableModel.setColumnIdentifiers(colonnes);

					appli.metier.Dijikstra dij = new appli.metier.Dijikstra(sommets);
					ArrayList<appli.metier.Dijikstra.EtapeDijkstra> etapes = dij.calculerDistancesEtapes(depart, arrivee);
					for (appli.metier.Dijikstra.EtapeDijkstra etape : etapes)
					{
						this.tableModel.addRow(buildRow(etape.label, noms, etape.distances));
					}
					styleColonneEtape();
					styleCellulesDistance(noms.size());
					this.table.revalidate();
					this.table.repaint();
				}
				else
				{
					// Calcul avec Bellman-Ford
					this.actualiser(sommets, depart);
				}
			}
		}
	}

	/**
	 * Calcule et affiche le tableau Bellman-Ford pour la liste de sommets donnée.
	 * Le sommet source est le sommet de départ spécifié.
	 *
	 * @param sommets liste des sommets du graphe (non nulle, non vide)
	 * @param depart  nom du sommet de départ
	 */
	public void actualiser(ArrayList<Sommet> sommets, String depart)
	{
		if (sommets == null || sommets.isEmpty())
		{
			this.tableModel.setRowCount(0);
			this.tableModel.setColumnCount(0);
			return;
		}

		// Noms des sommets
		List<String> noms = new ArrayList<>();
		for (Sommet s : sommets)
		{
			if (s.getNom() != null && !s.getNom().trim().isEmpty())
			{
				noms.add(s.getNom());
			}
		}

		if (noms.isEmpty()) return;

		int n = noms.size();

		// En-têtes de colonnes
		String[] colonnes = new String[n + 1];
		colonnes[0] = "Étape";
		for (int i = 0; i < n; i++)
		{
			colonnes[i + 1] = "d(" + noms.get(i) + ")";
		}

		this.tableModel.setColumnIdentifiers(colonnes);
		this.tableModel.setRowCount(0);

		// Initialisation
		Map<String, Integer> dist = new LinkedHashMap<>();
		Map<String, String> pred = new LinkedHashMap<>();

		for (String nom : noms)
		{
			dist.put(nom, Integer.MAX_VALUE);
			pred.put(nom, null);
		}
		dist.put(depart, 0); // source = sommet de départ, distance = 0

		this.tableModel.addRow(buildRow("Initialisation", noms, dist));

		// Itérations Bellman-Ford (n-1 fois)
		for (int iter = 1; iter < n; iter++)
		{
			boolean changed = false;

			for (Sommet s : sommets)
			{
				if (s.getNom() == null || s.getNom().trim().isEmpty()) continue;

				Integer distSource = dist.get(s.getNom());
				if (distSource == null || distSource == Integer.MAX_VALUE) continue;

				for (Lien l : s.getLiens())
				{
					String voisin = l.getNom();
					if (voisin == null || !dist.containsKey(voisin)) continue;

					int nouvelleDistance = distSource + l.getDistance();
					if (nouvelleDistance < dist.get(voisin))
					{
						dist.put(voisin, nouvelleDistance);
						pred.put(voisin, s.getNom());
						changed = true;
					}
				}
			}

			this.tableModel.addRow(buildRow("Itération " + iter, noms, dist));

			// Convergence anticipée : si aucun changement, inutile de continuer
			if (!changed) break;
		}

		// Mise en forme
		styleColonneEtape();
		styleCellulesDistance(noms.size());

		this.table.revalidate();
		this.table.repaint();
	}

	// Helpers

	private Object[] buildRow(String label, List<String> noms, Map<String, Integer> dist)
	{
		Object[] row = new Object[noms.size() + 1];
		row[0] = label;
		for (int i = 0; i < noms.size(); i++) {
			Integer d = dist.get(noms.get(i));
			row[i + 1] = (d == null || d == Integer.MAX_VALUE) ? "+∞" : String.valueOf(d);
		}
		return row;
	}

	/** Colonne "Étape" : texte muted, aligné à gauche. */
	private void styleColonneEtape()
	{
		DefaultTableCellRenderer leftMuted = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(
					JTable t, Object value, boolean sel, boolean focus, int row, int col) {
				super.getTableCellRendererComponent(t, value, sel, focus, row, col);
				setHorizontalAlignment(SwingConstants.LEFT);
				setForeground(sel ? Theme.SELECT_FG : Theme.TEXT_MUTED);
				setBackground(sel ? Theme.SELECT_BG : Theme.PANEL_ALT);
				setFont(Theme.FONT_BOLD);
				setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 6));
				return this;
			}
		};
		this.table.getColumnModel().getColumn(0).setCellRenderer(leftMuted);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(120);
		this.table.getColumnModel().getColumn(0).setMinWidth(100);
	}

	/**
	 * Colonnes de distance : "+∞" en muted, valeurs normales en texte clair,
	 * ligne 0 (Initialisation) légèrement différenciée.
	 */
	private void styleCellulesDistance(int nbSommets)
	{
		DefaultTableCellRenderer distRenderer = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(
					JTable t, Object value, boolean sel, boolean focus, int row, int col) {
				super.getTableCellRendererComponent(t, value, sel, focus, row, col);
				setHorizontalAlignment(SwingConstants.CENTER);
				setFont(Theme.FONT_NORMAL);

				boolean isInfini = "+∞".equals(value);

				if (sel)
				{
					setForeground(Theme.SELECT_FG);
					setBackground(Theme.SELECT_BG);
				}
				else if (row == 0)
				{
					// Ligne Initialisation : fond légèrement plus sombre
					setBackground(Theme.EDITOR_BG);
					setForeground(isInfini ? Theme.TEXT_MUTED : Theme.ACCENT);
				}
				else
				{
					setBackground(Theme.PANEL);
					setForeground(isInfini ? Theme.TEXT_MUTED : Theme.TEXT);
				}
				return this;
			}
		};

		for (int col = 1; col <= nbSommets; col++)
		{
			this.table.getColumnModel().getColumn(col).setCellRenderer(distRenderer);
		}
	}
}