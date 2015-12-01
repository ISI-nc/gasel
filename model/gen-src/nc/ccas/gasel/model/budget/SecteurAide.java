package nc.ccas.gasel.model.budget;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.model.budget.auto._SecteurAide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.EnumerationSync;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;

public class SecteurAide extends _SecteurAide {
	private static final long serialVersionUID = -7972860956874528354L;

	public static final int LOGEMENT = 1;

	static {
		EnumerationSync sync = new EnumerationSync(SecteurAide.class);
		sync.add(LOGEMENT, "Logement");
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public boolean isLogement() {
		return getId() == LOGEMENT;
	}

	// XXX inutilisé

	private static final SQLTemplate NOMBRE_DOSSIERS = new SQLTemplate(
			Dossier.class,
			"SELECT s.id, COUNT(a.id) AS count "
					+ "  FROM secteur_aide s"
					+ "  LEFT JOIN nature_aide n ON (n.parent_id = s.id)"
					+ "  LEFT JOIN aide a "
					+ "         ON (a.nature_id = n.id"
					+ "         AND a.debut <= $fin_mois"
					+ "         AND (a.fin IS NULL OR a.fin >= $debut_mois))" //
					+ " GROUP BY s.id");
	static {
		NOMBRE_DOSSIERS.setFetchingDataRows(true);
	}

	/**
	 * @return Le nombre de dossier aidés dans l'intervalle <code>debut</code> -
	 *         <code>fin</code> par secteur d'aide.
	 */
	@SuppressWarnings("unchecked")
	public static Map<SecteurAide, Integer> nombreDossiers(DataContext context,
			Date debut, Date fin) {
		List<DataRow> rows = context.performQuery(SqlUtils.parametrerTemplate(
				NOMBRE_DOSSIERS, //
				"debut_mois", SqlUtils.dateToSql(debut), //
				"fin_mois", SqlUtils.dateToSql(fin)));

		return SqlUtils.mapIdCount(context, SecteurAide.class, rows);
	}

	public static Map<SecteurAide, Integer> nombreAides(DataContext context,
			Date[] intervalle) {
		return nombreDossiers(context, intervalle[0], intervalle[1]);
	}

	public int getNextCode() {
		int max = 0;
		for (NatureAide nature : getNatures()) {
			if (nature.getCode() == null)
				continue;

			if (nature.getCode() > max)
				max = nature.getCode();
		}
		return max + 1;
	}

}
