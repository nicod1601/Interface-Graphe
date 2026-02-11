package appli.ihm;

import appli.Controleur;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import appli.metier.Sommet;
import appli.metier.Lien;
import javax.swing.JButton;
import appli.Controleur;

public class Edit extends JPanel implements ActionListener
{
    private Controleur ctrl;

    /*Panel Document */
    private JEditorPane txtDocument;

    /*Panel Tableau */
    private JTable table;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnAnnuler;
    private DefaultTableModel tableModel;
    private Appli appli;

    /*panel graphe */
    private GrapheCopie graphe;
    private ArrayList<Sommet> sommets;

    public Edit(Controleur ctrl, Appli appli)
    {
        this.ctrl = ctrl;
        this.appli = appli;
        this.setLayout(new BorderLayout());

        /*Panel Document */
        JPanel panelDocument = new JPanel();
        panelDocument.setLayout(new BorderLayout());

        JLabel lblDocument = new JLabel("Document");
        this.txtDocument = new JEditorPane();

        panelDocument.add(lblDocument, BorderLayout.NORTH);
        panelDocument.add(new JScrollPane(this.txtDocument), BorderLayout.CENTER);

        /*Panel Graphe */
        this.sommets = new ArrayList<>();
        this.graphe = new GrapheCopie(this);


        /*Panel Tableau */
        JPanel panelTableau = new JPanel(new BorderLayout());
        JPanel panelTableauNorth = new JPanel(new GridLayout(1,2));
        JPanel panelBtn = new JPanel();
        

        JLabel lblTableau = new JLabel("Tableau");
        this.btnAjouter = new JButton("+");
        this.btnSupprimer = new JButton("-");
        this.btnAnnuler = new JButton("Annuler");
        
        panelBtn.add(this.btnAnnuler);
        panelBtn.add(this.btnSupprimer);
        panelBtn.add(this.btnAjouter);

        
        panelTableauNorth.add(lblTableau);
        panelTableauNorth.add(panelBtn);

        String[] colonne = {"Sommet", "Liens", "Distance"};
        this.tableModel = new DefaultTableModel(colonne, 0);
        this.table = new JTable(this.tableModel);


        panelTableau.add(panelTableauNorth, BorderLayout.NORTH);
        panelTableau.add(new JScrollPane(this.table), BorderLayout.CENTER);


        /*Panel TabGraphe */
        JPanel panelTabGraph = new JPanel(new GridLayout(2,1));
        panelTabGraph.add(panelTableau);
        panelTabGraph.add(this.graphe);

        /*Position des composants */    
        this.add(panelDocument, BorderLayout.WEST);
        this.add(panelTabGraph, BorderLayout.CENTER);


        /*Activation */
        this.btnAjouter.addActionListener(this);
        this.btnSupprimer.addActionListener(this);
        this.btnAnnuler.addActionListener(this);



        this.setVisible(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "+":
                System.out.println("Ajouter");
                this.modifierTableau("+");
                break;
            case "-":
                System.out.println("Supprimer");
                this.modifierTableau("-");
                break;
            case"Annuler":
                System.out.println("Annuler");
                this.appli.afficher("Annuler");
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
            for(Lien lien : sommet.getLiens())
            {
                Object[] row = {sommet.getNom(), lien.getNom(), lien.getDistance()};
                this.tableModel.addRow(row);
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

    public ArrayList<Sommet> getSommets()
    {
        return this.sommets;
    }

}
