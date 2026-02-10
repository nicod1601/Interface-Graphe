package appli.ihm;

import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Menu extends JPanel
{
    private JMenuBar menuBar;
    private JMenu menuFichier;
    private JMenu menuEdition;
    
    public Menu()
    {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));


        /*Création des composants */

        this.menuBar = new JMenuBar();
        this.menuFichier = new JMenu  ("  📁 Fichier  ");
        this.menuEdition = new JMenu  ("  ✏️ Editer  " );

        this.menuFichier.add(new JMenuItem("📄 Ouvrir Fichier"));
        this.menuFichier.add(new JMenuItem("💾 Enregistrer"   ));
        this.menuFichier.addSeparator();
        this.menuFichier.add(new JMenuItem("❌ Quitter"       ));


        /*Position des composants */
        
        this.menuBar.add(this.menuFichier);
        this.menuBar.add(this.menuEdition);

        this.add(this.menuBar);
            
    }
    
}
