package nc.ccas.gasel.stats;

import nc.ccas.gasel.stats.repr.StringRepr;
import nc.ccas.gasel.stats.tr.SqlTr;

public class ColumnDefinition {

	public static final boolean DEFAULT_GROUPING = false;
	public static final boolean DEFAULT_COUNTING = true;

	private final String titre;

	private final Transformation tr;

	private final Representation<?> repr;

	private final boolean grouping;

	private final boolean counting;

	public ColumnDefinition(String titre, Transformation tr) {
		this(titre, tr, StringRepr.INSTANCE);
	}

	public ColumnDefinition(String titre, Transformation tr,
			Representation<?> repr) {
		this(titre, tr, repr, DEFAULT_GROUPING);
	}

	public ColumnDefinition(String titre, Transformation tr,
			Representation<?> repr, boolean grouping) {
		this(titre, tr, repr, grouping, DEFAULT_COUNTING);
	}

	public ColumnDefinition(String titre, Transformation tr,
			Representation<?> repr, boolean grouping, boolean counting) {
		this.titre = titre;
		this.tr = tr;
		this.repr = repr;
		this.grouping = grouping;
		this.counting = counting;
	}

	public ColumnDefinition(String titre, String transformation) {
		this(titre, transformation, StringRepr.INSTANCE);
	}

	public ColumnDefinition(String titre, String transformation,
			Representation<?> repr) {
		this(titre, transformation, repr, false);
	}

	public ColumnDefinition(String titre, String transformation,
			Representation<?> repr, boolean grouping) {
		this(titre, transformation, repr, grouping, true);
	}

	public ColumnDefinition(String titre, String transformation,
			Representation<?> repr, boolean grouping, boolean counting) {
		this(titre, new SqlTr(new TransformationQuery()
				.definition(transformation)), repr, grouping, counting);
	}

	public String getTitre() {
		return titre;
	}

	Representation<?> getRepr() {
		return repr;
	}

	Transformation getTr() {
		return tr;
	}

	public boolean isCounting() {
		return counting;
	}

	public boolean isGrouping() {
		return grouping;
	}

}
