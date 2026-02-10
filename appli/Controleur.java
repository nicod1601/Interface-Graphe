package appli;

import appli.ihm.Appli;
import appli.metier.Lecture;

public class Controleur 
{
    private Lecture lecture;
    private Appli appli;

    public Controleur()
    {
        this.lecture = new Lecture();
        this.appli = new Appli();
    }



    public static void main(String[] args) 
    {
        new Controleur();
    }   

}
