package appli.ihm;

import appli.Controleur;
import appli.metier.Dijikstra;
import appli.metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panneau latéral affichant le tableau des distances calculées étape par étape.
 * <p>
 * Structure visuelle :
 * <pre>
 * ┌─────────────────────────────────────┐
 * │  📊 Titre + algorithme actif        │  ← en-tête
 * ├─────────────────────────────────────┤
 * │  Départ [▼]  Arrivée [▼]  Algo [▼] │  ← sélecteurs
 * │  [  Calculer  ]                     │
 * ├─────────────────────────────────────┤
 * │  Étape │ d(A) │ d(B) │ d(C) │ ...  │  ← tableau
 * │  Init  │  0   │  +∞  │  +∞  │      │    (scrollable)
 * │  It. 1 │  0   │  3   │  +∞  │      │
 * │  ...   │      │      │      │      │
 * └─────────────────────────────────────┘
 * </pre>
 * Supporte Dijkstra et Bellman-Ford. Le calcul est déclenché par le bouton
 * « Calculer » ou peut être invoqué programmatiquement via {@link #actualiser}.
 *
 * @author Nicolas D. & Marta AN.
 */
public class PanelDistance extends JPanel implements ActionListener
{
	// ── Référence externe ─────────────────────────────────────────────────────

	/** Accès aux données du graphe. */
	private final Controleur ctrl;

	// ── En-tête ───────────────────────────────────────────────────────────────

	/** Label de titre (mis à jour selon l'algorithme sélectionné). */
	private final JLabel lblTitre;

	// ── Sélecteurs ────────────────────────────────────────────────────────────

	/** Sélecteur du sommet de départ. */
	private final JComboBox<String> cbDepart;

	/** Sélecteur du sommet d'arrivée. */
	private final JComboBox<String> cbArrivee;

	/** Sélecteur de l'algorithme. */
	private final JComboBox<String> cbAlgo;

	/** Bouton déclenchant le calcul. */
	private final JButton btnCalculer;

	// ── Tableau de distances ──────────────────────────────────────────────────

	/** Table affichant les étapes de l'algorithme. */
	private final JTable table;

	/** Modèle de données de la table. */
	private final DefaultTableModel tableModel;

	// ── Constructeur ──────────────────────────────────────────────────────────

	/**
	 * Construit le panneau et initialise tous les sous-composants.
	 * Les sélecteurs sont remplis avec les noms des sommets actuels du graphe.
	 *
	 * @param ctrl Le contrôleur de l'application.
	 */
	public PanelDistance(Controleur ctrl)
	{
		this.ctrl = ctrl;

		// ── Layout principal ─────────────────────────────────────────────────
		this.setLayout(new BorderLayout(0, 8));
		this.setBackground(Theme.BACKGROUND);
		this.setBorder(BorderFactory.createCompoundBorder(
			Theme.borderCard(),
			BorderFactory.createEmptyBorder(4, 4, 4, 4)
		));
		this.setPreferredSize(new Dimension(600, 0));

		// ── Initialisation des composants ────────────────────────────────────
		this.lblTitre   = Theme.labelTitle("📊 Tableau des distances");
		this.tableModel = new DefaultTableModel() {
			/** Le tableau est en lecture seule : les cellules ne sont pas éditables. */
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		this.table = new JTable(this.tableModel);

		// Remplissage des ComboBox avec les noms de sommets valides
		List<String> noms = extraireNomsSommets();
		this.cbDepart  = new JComboBox<>(noms.toArray(new String[0]));
		this.cbArrivee = new JComboBox<>(noms.toArray(new String[0]));
		this.cbAlgo    = new JComboBox<>(new String[]{ "Dijkstra", "Bellman-Ford" });
		this.btnCalculer = Theme.button("▶ Calculer", Theme.ACCENT);

		// ── Assemblage ───────────────────────────────────────────────────────
		this.add(construireEntete(),     BorderLayout.NORTH);
		this.add(construireTableau(),    BorderLayout.CENTER);
		this.add(construireSelecteurs(), BorderLayout.SOUTH);

		this.btnCalculer.addActionListener(this);
	}

	// ── Construction des sous-panneaux ────────────────────────────────────────

	/**
	 * Construit l'en-tête : titre et description du panneau.
	 *
	 * @return Panneau en-tête.
	 */
	private JPanel construireEntete()
	{
		JPanel entete = Theme.panel();
		entete.setLayout(new BorderLayout());
		entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

		this.lblTitre.setFont(Theme.FONT_TITLE);
		entete.add(this.lblTitre, BorderLayout.WEST);

		JLabel lblDesc = Theme.label("Distances minimales depuis la source");
		entete.add(lblDesc, BorderLayout.EAST);

		return entete;
	}

	/**
	 * Construit la zone centrale : tableau scrollable des distances par étape.
	 *
	 * @return ScrollPane contenant le tableau.
	 */
	private JScrollPane construireTableau()
	{
		Theme.styleTable(this.table);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scroll = new JScrollPane(this.table);
		scroll.setBorder(Theme.borderInput());
		scroll.getViewport().setBackground(Theme.PANEL);
		return scroll;
	}

	/**
	 * Construit la zone sud : sélecteurs de départ/arrivée/algorithme + bouton Calculer.
	 * Organisation en deux lignes :
	 * <pre>
	 *   Départ [▼]   Arrivée [▼]   Algo [▼]
	 *                [ ▶ Calculer ]
	 * </pre>
	 *
	 * @return Panneau des sélecteurs.
	 */
	private JPanel construireSelecteurs()
	{
		// Ligne 1 : libellés + ComboBox
		JPanel ligneSelectors = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
		ligneSelectors.setBackground(Theme.PANEL);

		ligneSelectors.add(creerLabel("Départ"));
		styleCombo(this.cbDepart);
		ligneSelectors.add(this.cbDepart);

		ligneSelectors.add(creerLabel("Arrivée"));
		styleCombo(this.cbArrivee);
		ligneSelectors.add(this.cbArrivee);

		ligneSelectors.add(creerLabel("Algo"));
		styleCombo(this.cbAlgo);
		ligneSelectors.add(this.cbAlgo);

		// Ligne 2 : bouton centré
		JPanel ligneBouton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
		ligneBouton.setBackground(Theme.PANEL);
		ligneBouton.add(this.btnCalculer);

		// Conteneur global
		JPanel conteneur = Theme.panel();
		conteneur.setLayout(new BorderLayout(0, 2));
		conteneur.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
			BorderFactory.createEmptyBorder(6, 0, 2, 0)
		));
		conteneur.add(ligneSelectors, BorderLayout.NORTH);
		conteneur.add(ligneBouton,    BorderLayout.SOUTH);

		return conteneur;
	}

	// ── Gestion des actions ───────────────────────────────────────────────────

	/**
	 * Déclenche le calcul quand l'utilisateur clique sur « Calculer ».
	 * Sélectionne l'algorithme, récupère départ/arrivée et remplit le tableau.
	 *
	 * @param e L'événement d'action.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() != this.btnCalculer) return;

		String depart  = (String) this.cbDepart.getSelectedItem();
		String arrivee = (String) this.cbArrivee.getSelectedItem();
		String algo    = (String) this.cbAlgo.getSelectedItem();

		if (depart == null || arrivee == null || algo == null) return;

		// Synchroniser le contrôleur avec l'algo choisi
		this.ctrl.Mode(algo);

		// Mettre à jour le titre
		this.lblTitre.setText("📊 Tableau des distances — " + algo);

		ArrayList<Sommet> sommets = this.ctrl.getSommets();

		if (algo.equals("Dijkstra"))
			remplirDijkstra(sommets, depart, arrivee);
		///else
			//remplirBellmanFord(sommets, depart);
	}

	// ── Remplissage du tableau ────────────────────────────────────────────────

	/**
	 * Calcule et affiche le tableau Dijkstra étape par étape.
	 * Chaque ligne correspond à une extraction depuis la file de priorité.
	 *
	 * @param sommets Liste des sommets du graphe.
	 * @param depart  Nom du sommet source.
	 * @param arrivee Nom du sommet destination (utilisé par l'algo).
	 */
	private void remplirDijkstra(ArrayList<Sommet> sommets, String depart, String arrivee)
	{
		List<String> noms = extraireNomsSommets();
		reinitialiserTableau(noms);

		Dijikstra dijkstra = new Dijikstra(sommets);
		ArrayList<Dijikstra.EtapeDijkstra> etapes = dijkstra.calculerDistancesEtapes(depart, arrivee);

		for (Dijikstra.EtapeDijkstra etape : etapes)
			this.tableModel.addRow(construireLigne(etape.label, noms, etape.distances));

		appliquerStyle(noms.size());
	}

	/**
	 * Calcule et affiche le tableau Bellman-Ford itération par itération.
	 * La source est le sommet {@code depart}, toutes les distances partent de 0 pour lui.
	 *
	 * @param sommets Liste des sommets du graphe (non nulle, non vide).
	 * @param depart  Nom du sommet source.
	 */
	public void actualiser(ArrayList<Sommet> sommets, String depart)
	{
		if (sommets == null || sommets.isEmpty()) { viderTableau(); return; }

		List<String> noms = extraireNomsSommets();
		if (noms.isEmpty()) return;

		reinitialiserTableau(noms);

		// ── Initialisation Bellman-Ford ──────────────────────────────────────
		Map<String, Integer> dist = new LinkedHashMap<>();
		for (String nom : noms) dist.put(nom, Integer.MAX_VALUE);
		dist.put(depart, 0);

		this.tableModel.addRow(construireLigne("Initialisation", noms, dist));

		// ── n-1 itérations ───────────────────────────────────────────────────
		int n = noms.size();
		for (int iter = 1; iter < n; iter++)
		{
			boolean changement = false;

			for (Sommet s : sommets)
			{
				if (estNomInvalide(s.getNom())) continue;

				Integer distSource = dist.get(s.getNom());
				if (distSource == null || distSource == Integer.MAX_VALUE) continue;

				for (appli.metier.Lien lien : s.getLiens())
				{
					String voisin = lien.getNom();
					if (voisin == null || !dist.containsKey(voisin)) continue;

					int nouvDist = distSource + lien.getDistance();
					if (nouvDist < dist.get(voisin))
					{
						dist.put(voisin, nouvDist);
						changement = true;
					}
				}
			}

			this.tableModel.addRow(construireLigne("Itération " + iter, noms, dist));

			// Convergence anticipée
			if (!changement) break;
		}

		appliquerStyle(noms.size());
	}

	// ── Helpers — tableau ─────────────────────────────────────────────────────

	/**
	 * Réinitialise les colonnes du tableau selon la liste des noms de sommets.
	 * Format des colonnes : [Étape | d(S1) | d(S2) | ... | d(Sn)]
	 *
	 * @param noms Liste des noms de sommets valides.
	 */
	private void reinitialiserTableau(List<String> noms)
	{
		this.tableModel.setRowCount(0);
		this.tableModel.setColumnCount(0);

		String[] colonnes = new String[noms.size() + 1];
		colonnes[0] = "Étape";
		for (int i = 0; i < noms.size(); i++)
			colonnes[i + 1] = "d(" + noms.get(i) + ")";

		this.tableModel.setColumnIdentifiers(colonnes);
	}

	/** Vide complètement le tableau (lignes et colonnes). */
	private void viderTableau()
	{
		this.tableModel.setRowCount(0);
		this.tableModel.setColumnCount(0);
	}

	/**
	 * Construit une ligne du tableau pour une étape donnée.
	 * Les distances à {@link Integer#MAX_VALUE} sont affichées comme {@code "+∞"}.
	 *
	 * @param label  Libellé de l'étape (ex. "Initialisation", "Itération 2").
	 * @param noms   Noms des sommets (définit l'ordre des colonnes).
	 * @param dist   Map nom → distance courante.
	 * @return Tableau de {@code Object} prêt à passer à {@link DefaultTableModel#addRow}.
	 */
	private Object[] construireLigne(String label, List<String> noms, Map<String, Integer> dist)
	{
		Object[] ligne = new Object[noms.size() + 1];
		ligne[0] = label;
		for (int i = 0; i < noms.size(); i++)
		{
			Integer d = dist.get(noms.get(i));
			ligne[i + 1] = (d == null || d == Integer.MAX_VALUE) ? "+∞" : String.valueOf(d);
		}
		return ligne;
	}

	// ── Helpers — style ───────────────────────────────────────────────────────

	/**
	 * Applique les renderers personnalisés sur la colonne « Étape » et les colonnes de distance.
	 *
	 * @param nbSommets Nombre de colonnes de distance (hors colonne Étape).
	 */
	private void appliquerStyle(int nbSommets)
	{
		styleColonneEtape();
		styleColonnesDistance(nbSommets);
		this.table.revalidate();
		this.table.repaint();
	}

	/**
	 * Applique un renderer à la colonne « Étape » :
	 * texte en gras muted, aligné à gauche, fond légèrement différencié.
	 */
	private void styleColonneEtape()
	{
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(
					JTable t, Object value, boolean sel, boolean focus, int row, int col)
			{
				super.getTableCellRendererComponent(t, value, sel, focus, row, col);
				setHorizontalAlignment(SwingConstants.LEFT);
				setFont(Theme.FONT_BOLD);
				setForeground(sel ? Theme.SELECT_FG : Theme.TEXT_MUTED);
				setBackground(sel ? Theme.SELECT_BG : Theme.PANEL_ALT);
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
				return this;
			}
		};

		this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(120);
		this.table.getColumnModel().getColumn(0).setMinWidth(100);
	}

	/**
	 * Applique un renderer aux colonnes de distance :
	 * <ul>
	 *   <li>{@code "+∞"} affiché en gris muted.</li>
	 *   <li>Ligne 0 (Initialisation) : fond légèrement plus sombre, valeurs en accent.</li>
	 *   <li>Autres lignes : fond normal, valeurs en texte clair.</li>
	 * </ul>
	 *
	 * @param nbSommets Nombre de colonnes de distance.
	 */
	private void styleColonnesDistance(int nbSommets)
	{
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(
					JTable t, Object value, boolean sel, boolean focus, int row, int col)
			{
				super.getTableCellRendererComponent(t, value, sel, focus, row, col);
				setHorizontalAlignment(SwingConstants.CENTER);
				setFont(Theme.FONT_NORMAL);

				boolean estInfini = "+∞".equals(value);

				if (sel)
				{
					setForeground(Theme.SELECT_FG);
					setBackground(Theme.SELECT_BG);
				}
				else if (row == 0)
				{
					// Ligne d'initialisation : fond plus sombre pour la différencier
					setBackground(Theme.EDITOR_BG);
					setForeground(estInfini ? Theme.TEXT_MUTED : Theme.ACCENT);
				}
				else
				{
					setBackground(Theme.PANEL);
					setForeground(estInfini ? Theme.TEXT_MUTED : Theme.TEXT);
				}

				return this;
			}
		};

		for (int col = 1; col <= nbSommets; col++)
			this.table.getColumnModel().getColumn(col).setCellRenderer(renderer);
	}

	// ── Helpers — utilitaires ─────────────────────────────────────────────────

	/**
	 * Extrait les noms des sommets valides (non nuls, non vides) depuis le contrôleur.
	 *
	 * @return Liste ordonnée des noms de sommets.
	 */
	private List<String> extraireNomsSommets()
	{
		List<String> noms = new ArrayList<>();
		for (Sommet s : this.ctrl.getSommets())
			if (!estNomInvalide(s.getNom()))
				noms.add(s.getNom());
		return noms;
	}

	/**
	 * Crée un label de sélecteur stylisé (texte muted, police normale).
	 *
	 * @param texte Texte du label (ex. "Départ").
	 * @return JLabel stylisé.
	 */
	private JLabel creerLabel(String texte)
	{
		JLabel lbl = new JLabel(texte + " :");
		lbl.setFont(Theme.FONT_NORMAL);
		lbl.setForeground(Theme.TEXT_MUTED);
		return lbl;
	}

	/**
	 * Applique le style du thème à une JComboBox.
	 *
	 * @param combo La ComboBox à styler.
	 */
	private void styleCombo(JComboBox<String> combo)
	{
		combo.setFont(Theme.FONT_NORMAL);
		combo.setBackground(Theme.PANEL_ALT);
		combo.setForeground(Theme.TEXT);
		combo.setPreferredSize(new Dimension(100, 28));
	}

	/**
	 * Vérifie si un nom de sommet est invalide (null ou vide après trim).
	 *
	 * @param nom Nom à tester.
	 * @return {@code true} si le nom est invalide.
	 */
	private boolean estNomInvalide(String nom)
	{
		return nom == null || nom.trim().isEmpty();
	}
}
