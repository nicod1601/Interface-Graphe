package appli.metier;

import java.util.ArrayList;
import java.util.HashMap;

public class BellmanFord
{
	private HashMap<String, Integer> distance;
	private HashMap<String, String> precedent;
	private ArrayList<Sommet> lstTest;

	public BellmanFord(ArrayList<Sommet> lst)
	{
		this.distance = new HashMap<>();
		this.precedent = new HashMap<>();

		this.lstTest = lst;

		this.calculCheminCourt();
	}


	public void calculCheminCourt()
	{
		ArrayList<Sommet> sommets = this.lstTest;

		for (Sommet s : sommets)
			this.distance.put(s.getNom(), Integer.MAX_VALUE);

		this.distance.put(sommets.get(0).getNom(), 0);

		for (int i = 0; i < sommets.size() - 1; i++)
		{
			for (Sommet s : sommets)
			{
				for (Lien l : s.getLiens())
				{
					int distSource = this.distance.get(s.getNom());

					if (distSource != Integer.MAX_VALUE && 
						distSource + l.getDistance() < this.distance.get(l.getNom()))
					{
						this.distance.put(l.getNom(), distSource + l.getDistance());
						this.precedent.put(l.getNom(), s.getNom());
					}
				}
			}
		}
	}



	public ArrayList<String> getChemin(String destination)
	{
		ArrayList<String> chemin = new ArrayList<>();
		String courant = destination;

		while (courant != null)
		{
			chemin.add(0, courant);
			courant = this.precedent.get(courant);
		}

		return chemin;
	}

	public HashMap<String, Integer> getDistance()
	{
		return distance;
	}

	public HashMap<String, String> getPrecedent()
	{
		return precedent;
	}
}