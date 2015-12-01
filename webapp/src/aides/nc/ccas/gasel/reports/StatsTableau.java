package nc.ccas.gasel.reports;

import java.util.LinkedList;
import java.util.List;

public class StatsTableau<T> {

	private final List<StatsColonne<T>> colonnes = new LinkedList<StatsColonne<T>>();

	public StatsTableau<T> add(String titre, StatsTransform<T> transformation) {
		colonnes.add(new StatsColonne<T>(titre, transformation));
		return this;
	}

	public StatsColonne<T> colonne(int col) {
		return colonnes.get(col);
	}

	public List<StatsColonne<T>> getColonnes() {
		return colonnes;
	}

}
