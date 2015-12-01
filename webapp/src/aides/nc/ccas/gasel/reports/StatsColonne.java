package nc.ccas.gasel.reports;

public class StatsColonne<T> {

	private final String titre;

	private final StatsTransform<T> transformation;

	public StatsColonne(String titre, StatsTransform<T> transformation) {
		this.titre = titre;
		this.transformation = transformation;
	}

	public String getTitre() {
		return titre;
	}

	public StatsTransform<T> getTransformation() {
		return transformation;
	}
	
	public Object valueFor(T input) {
		return transformation.transform(input);
	}

}
