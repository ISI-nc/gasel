package nc.ccas.gasel.model.aides;

import static nc.ccas.gasel.modelUtils.SqlUtils.templateDateInRange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.modelUtils.SqlUtils;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DataRow;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SQLTemplate;

public class UsageAide {

	private static final SQLTemplate TEMPLATE = new SQLTemplate(
			Aide.class,
			"SELECT a.nature_id AS nature, a.id AS aide, a.montant AS montant_aide,\n"
					+ "       (SELECT SUM(montant) FROM bon b\n"
					+ "         WHERE a.id = aide_id AND "
					+ templateDateInRange("b")
					+ ")\n"
					+ "         AS montant_bons,\n"
					+ "       (SELECT SUM(montant) FROM bon b\n"
					+ "         WHERE a.id = aide_id AND etat_id=3 AND "
					+ templateDateInRange("b")
					+ ")\n"
					+ "         AS montant_annule,\n"
					+ "       (SELECT SUM(ub.montant_utilise) FROM bon b\n"
					+ "          JOIN usage_bon ub ON (ub.bon_id = b.id)\n"
					+ "         WHERE a.id = aide_id AND "
					+ templateDateInRange("b") + "\n"
					+ "       ) AS montant_utilise\n"
					+ "  FROM aide a\n" + " WHERE "
					+ templateDateInRange("a"));
	static {
		TEMPLATE.setFetchingDataRows(true);
	}

	public static List<UsageAide> usages(DataContext context, Date[] intervalle) {
		return usages(context, intervalle[0], intervalle[1]);
	}

	@SuppressWarnings("unchecked")
	public static List<UsageAide> usages(DataContext context, Date debut,
			Date fin) {
		List<DataRow> rows = context.performQuery(SqlUtils.parametrerTemplate(
				TEMPLATE, //
				"debut_mois", SqlUtils.dateToSql(debut), // 
				"fin_mois", SqlUtils.dateToSql(fin)));

		List<UsageAide> usages = new ArrayList<UsageAide>(rows.size());
		for (DataRow row : rows) {
			usages.add(new UsageAide(context, row, debut, fin));
		}
		return usages;
	}

	private final int natureId;

	private final int aideId;

	private final int montantAide;

	private final int montantBons;

	private final int montantAnnule;

	private final int montantUtilise;

	private final DataContext context;

	private final Date debutPeriode;

	private final Date finPeriode;

	private UsageAide(DataContext context, DataRow row, Date debutPeriode,
			Date finPeriode) {
		this.context = context;
		aideId = (Integer) row.get("aide");
		natureId = (Integer) row.get("nature");
		montantAide = SqlUtils.toInt(row.get("montant_aide"));
		montantBons = SqlUtils.toInt(row.get("montant_bons"));
		montantAnnule = SqlUtils.toInt(row.get("montant_annule"));
		montantUtilise = SqlUtils.toInt(row.get("montant_utilise"));
		this.debutPeriode = debutPeriode;
		this.finPeriode = finPeriode;
	}

	public Aide getAide() {
		return (Aide) DataObjectUtils.objectForPK(context, Aide.class, aideId);
	}

	public NatureAide getNature() {
		return (NatureAide) DataObjectUtils.objectForPK(context,
				NatureAide.class, natureId);
	}

	public int getAideId() {
		return aideId;
	}

	public DataContext getContext() {
		return context;
	}

	public int getMontantAide() {
		return montantAide;
	}

	public int getMontantAnnule() {
		return montantAnnule;
	}

	public int getMontantBons() {
		return montantBons;
	}

	public int getMontantUtilise() {
		return montantUtilise;
	}

	public Date getDebutPeriode() {
		return debutPeriode;
	}

	public Date getFinPeriode() {
		return finPeriode;
	}

}
