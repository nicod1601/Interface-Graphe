package appli;

import appli.metier.Lecture;

public class Controleur 
{
    private Lecture lecture;

    public Controleur()
    {
        this.lecture = new Lecture();
    }



    public static void main(String[] args) 
    {
        new Controleur();
    }   

}
