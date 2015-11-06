package nc.ccas.gasel.model.budget;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.budget.auto._NatureAide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.EnumerationSync;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;

@Feminin
public class NatureAide extends _NatureAide {
	private static final long serialVersionUID = -3959924389528293327L;

	public static final int EAU = 1;

	public static final int ORDURES_MENAGERES = 2;

	public static final int GAZ = 3;

	static {
		EnumerationSync sync = new EnumerationSync(NatureAide.class);
		sync.add(EAU, "Eau", SecteurAide.LOGEMENT);
		sync.add(ORDURES_MENAGERES, "Ordures ménagères", SecteurAide.LOGEMENT);
		sync.add(GAZ, "Gaz", SecteurAide.LOGEMENT);
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public boolean isEau() {
		String lowerCased = getLibelle().toLowerCase();

		return (getObjectId() != null && !getObjectId().isTemporary() && getId() == EAU) //
				|| lowerCased.matches("eau|eau\\b.*|.*\\beau$");
	}

	public boolean isOrduresMenageres() {
		return getId() == ORDURES_MENAGERES;
	}

	public boolean isGaz() {
		return getId() == GAZ;
	}

	public boolean isAutorise(TypePublic typePublic) {
		for (TypePublicNature tpn : getPublicsConcernes()) {
			if (tpn.getTypePublic().equals(typePublic)) {
				return true;
			}
		}
		return false;
	}

	// XXX inutilisé

	private static final SQLTemplate NOMBRE_DOSSIERS = new SQLTemplate(
			Dossier.class, "SELECT n.id, COUNT(a.id) AS count "
					+ "  FROM nature_aide n"
					+ "  LEFT JOIN aide a "
					+ "         ON (a.nature_id = n.id"
					+ "         AND a.debut <= $fin_mois"
					+ "         AND (a.fin IS NULL OR a.fin >= $debut_mois))" //
					+ " GROUP BY n.id");
	static {
		NOMBRE_DOSSIERS.setFetchingDataRows(true);
	}

	/**
	 * @return Le nombre de dossier aidés dans l'intervalle <code>debut</code> -
	 *         <code>fin</code> par secteur d'aide.
	 */
	@SuppressWarnings("unchecked")
	public static Map<NatureAide, Integer> nombreDossiers(DataContext context,
			Date debut, Date fin) {
		List<DataRow> rows = context.performQuery(SqlUtils.parametrerTemplate(
				NOMBRE_DOSSIERS, //
				"debut_mois", SqlUtils.dateToSql(debut), //
				"fin_mois", SqlUtils.dateToSql(fin)));

		return SqlUtils.mapIdCount(context, NatureAide.class, rows);
	}

	public static Map<NatureAide, Integer> nombreAides(DataContext context,
			Date[] intervalle) {
		return nombreDossiers(context, intervalle[0], intervalle[1]);
	}

}
