package nc.ccas.gasel.model.budget;

import java.util.HashSet;
import java.util.Set;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.budget.auto._Imputation;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.commons.collections.map.SingletonMap;

@Feminin
public class Imputation extends _Imputation {
	private static final long serialVersionUID = 3185381863381040489L;

	public static final int ALIMENTATION = 1;

	public static final int AVANCE = 2;

	public static final int URGENCES = 3;

	public static final int SOLIDARITE = 4;

	static {
		EnumerationSync sync = new EnumerationSync(Imputation.class);
		sync.add(ALIMENTATION, "Alimentation");
		sync.add(AVANCE, "Avance");
		sync.add(URGENCES, "Urgences");
		sync.add(SOLIDARITE, "Solidarité");
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public boolean isAlimentation() {
		return getId() == ALIMENTATION;
	}

	private static final SQLTemplate QUERY_TYPE_PUBLIC = new SQLTemplate(
			TypePublic.class,
			"SELECT DISTINCT tp.*\n"
					+ "  FROM nature_aide na\n"
					+ "  JOIN type_public_nature tpn ON (tpn.nature_aide_id = na.id)\n"
					+ "  JOIN type_public tp ON (tp.id = tpn.type_public_id)\n"
					+ " WHERE na.imputation_id = $impId");

	public Set<TypePublic> getTypesPublic() {
		return doQuery(QUERY_TYPE_PUBLIC);
	}

	private static final SQLTemplate QUERY_SECTEUR_AIDE = new SQLTemplate(
			SecteurAide.class, "SELECT DISTINCT sa.*\n"
					+ "  FROM secteur_aide sa\n"
					+ "  JOIN nature_aide na ON (sa.id = na.parent_id)\n"
					+ " WHERE na.imputation_id = $impId");

	private Set<SecteurAide> secteursAide;

	public Set<SecteurAide> getSecteursAide() {
		if (secteursAide == null) {
			secteursAide = doQuery(QUERY_SECTEUR_AIDE);
		}
		return secteursAide;
	}

	private static final SQLTemplate QUERY_NATURE_AIDE = new SQLTemplate(
			NatureAide.class, "SELECT DISTINCT *\n" + "  FROM nature_aide\n"
					+ " WHERE imputation_id = $impId");

	private Set<NatureAide> naturesAide;

	public Set<NatureAide> getNaturesAide() {
		if (naturesAide == null) {
			naturesAide = doQuery(QUERY_NATURE_AIDE);
		}
		return naturesAide;
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> doQuery(SQLTemplate template) {
		ObjectContext dc = getObjectContext();
		SQLTemplate query = template.queryWithParameters(new SingletonMap(
				"impId", getId()));
		return new HashSet<T>(dc.performQuery(query));
	}

}
