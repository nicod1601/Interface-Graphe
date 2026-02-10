package appli.ihm;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Appli extends JFrame
{
    private Menu menu;

    public Appli()
    {
        this.setTitle("Interface Graphe");
        Image icon = Toolkit.getDefaultToolkit().getImage("./appli/logo.png");
        this.setIconImage(icon);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        this.menu = new Menu();

        this.add(this.menu, BorderLayout.NORTH);

    }
}
