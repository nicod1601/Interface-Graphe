package appli.ihm;

import appli.Controleur;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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
        this.ctrl = ctrl;
        this.appli = appli;
        this.setLayout(new GridLayout(1,2));

        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelMode = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        /*
        * Menu :
        */

        /*Création des composants */

        this.menuBar = new JMenuBar();
        this.menuFichier = new JMenu  ("  📁 Fichier  ");
        this.menuEdition = new JMenu  ("  ✏️ Editer  " );
        this.menuOptions = new JMenu  ("  ⚙️ Options  " );

        this.menuFichier.add(new JMenuItem("📄 Ouvrir Fichier"));
        this.menuFichier.add(new JMenuItem("💾 Enregistrer"   ));
        this.menuFichier.addSeparator();
        this.menuFichier.add(new JMenuItem("❌ Quitter"       ));

        this.menuEdition.add(new JMenuItem("✏️ Editer"));

        this.menuOptions.add(new JMenuItem("⚙️ Dijikstra"));
        this.menuOptions.add(new JMenuItem("⚙️ BellmanFordMetier"));
        this.menuOptions.add(new JMenuItem("⚙️ Désactiver"));

        /*Position des composants */
        
        this.menuBar.add(this.menuFichier);
        this.menuBar.add(this.menuEdition);
        this.menuBar.add(this.menuOptions);

        panelMenu.add(this.menuBar);


        /*
        * Mode :
        */

        /*Création des composants */

        this.btnMode = new JButton("Mode");
        this.btnMode.setBackground(Color.BLACK);
        this.btnMode.setEnabled(false);

        /*Position des composants */

        panelMode.add(this.btnMode);


        this.add(panelMenu);
        this.add(panelMode);

        /*Action */
        for (int i = 0; i < this.menuFichier.getItemCount(); i++)
        {
            if (this.menuFichier.getItem(i) != null)
            {
                this.menuFichier.getItem(i).addActionListener(this);
            }
        }

        for (int i = 0; i < this.menuEdition.getItemCount(); i++)
        {
            if (this.menuEdition.getItem(i) != null)
            {
                this.menuEdition.getItem(i).addActionListener(this);
            }
        }

        for (int i = 0; i < this.menuOptions.getItemCount(); i++)
        {
            if (this.menuOptions.getItem(i) != null)
            {
                this.menuOptions.getItem(i).addActionListener(this);
            }
        }
            
    }

    public void actionPerformed(ActionEvent e) 
    {
        switch (e.getActionCommand())
        {
            case "📄 Ouvrir Fichier":
                this.OuvrerFichier();
                break;
            case "💾 Enregistrer":
                System.out.println("Enregistrer");
                break;
            case "❌ Quitter":
                System.out.println("Quitter");
                System.exit(0);
                break;
            case "✏️ Editer":
                this.appli.afficher("Edit");
                break;
            case "⚙️ Dijikstra":
                System.out.println("Dijikstra");
                this.btnMode.setText("Dijikstra");
                this.btnMode.setBackground(Color.GREEN);
                this.ctrl.Mode("Dijikstra");
                break;
            case "⚙️ BellmanFordMetier":
                System.out.println("Bellman-Ford");
                this.btnMode.setText("Bellman-Ford");
                this.btnMode.setBackground(Color.ORANGE);
                this.ctrl.Mode("Bellman-Ford");
                break;
            case "⚙️ Désactiver":
                System.out.println("Désactiver");
                this.btnMode.setText("Mode");
                this.btnMode.setBackground(Color.BLACK);
                break;
        }
    }


    public void OuvrerFichier()
    {
        System.out.println("|-----Ouvrir Fichier-----|");

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("./appli/donnee"));
			
        int resultat = chooser.showOpenDialog(this);

        if (resultat == JFileChooser.APPROVE_OPTION) 
        {
            File fichierSelectionne = chooser.getSelectedFile();
            String nomFichier = "" + fichierSelectionne;
            System.out.println("Fichier sélectionné : " + nomFichier);
            
            this.ctrl.getSommets().clear();
            this.ctrl.setLiens(nomFichier);
            this.appli.afficher("Graphe");
        }
    }
    
}
