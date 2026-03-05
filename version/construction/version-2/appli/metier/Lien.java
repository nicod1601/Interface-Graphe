package appli.metier;

public class Lien
{
	private String nom;
	private int distance;

	public Lien(String nom, int distance)
	{
		this.nom = nom;
		this.distance = distance;
	}

	public String getNom()
	{
		return nom;
	}

	public int getDistance()
	{
		return distance;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}

	public String toString()
	{
		return nom + " (" + distance + ")";
	}
}
