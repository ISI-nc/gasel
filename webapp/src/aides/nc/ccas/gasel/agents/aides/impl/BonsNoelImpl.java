package nc.ccas.gasel.agents.aides.impl;

import static nc.ccas.gasel.model.aides.StatutAide.OCCASIONNELLE;
import static nc.ccas.gasel.model.aides.StatutAide.PLURIMENSUELLE;
import static nc.ccas.gasel.modelUtils.CommonQueries.findById;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import static nc.ccas.gasel.reports.PeriodePropsBean.mois;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.agents.aides.BonsNoel;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.reports.PeriodePropsBean;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SQLTemplate;

public class BonsNoelImpl implements BonsNoel {

	private static final SQLTemplate SQL = new SQLTemplate(Aide.class,
			"SELECT t0.*" //
					+ "  FROM aide t0,"
					+ "       nature_aide t1,"
					+ "       secteur_aide t2,"
					+ "       type_public t3"
					+ " WHERE t0.nature_id = t1.id"
					+ "   AND t1.parent_id = t2.id"
					+ "   AND t0.public_id = t3.id"
					+ "   AND t0.statut_id = "
					+ PLURIMENSUELLE
					+ "   AND t1.libelle = 'Alimentation'"
					+ "   AND t2.libelle = 'Alimentation'"
//					+ "   AND t0.montant >= 10000"
					+ "   AND (t3.libelle = 'Personne Agée'"
					+ "     OR t3.libelle = 'Personne Handicapée')"
					+ "   AND t0.debut <= $debut" //
					+ "   AND t0.fin >= $fin");

	public List<Aide> createAidesNoel() {
		return createAidesNoel(DateUtils.anneeEnCours());
	}

	public List<Aide> createAidesNoel(int year) {
		ObjectContext dc = DataContext.getThreadDataContext();
		PeriodeProps periode = periodeAide(year);

		// Aides sources
		List<Aide> aidesSource = getAidesSource(dc, periode);

		// Création des aides de Noël
		StatutAide freq = findById(StatutAide.class, OCCASIONNELLE);

		// Nature
		NatureAide natureNoel = unique(dc, NatureAide.class, //
				"libelle", "Aide exceptionnelle Noël");

		List<Aide> aides = new LinkedList<Aide>();
		for (Aide aideSource : aidesSource) {
			Aide aideNoel = (Aide) dc.newObject(Aide.class);

			// Trucs "constants"
			aideNoel.setStatut(freq);
			aideNoel.setDebut(periode.getPeriodeDebut());
			aideNoel.setFin(periode.getPeriodeFin());
			aideNoel.setNature(natureNoel);

			// Trucs variables
			aideNoel.setDossier(aideSource.getDossier());
			aideNoel.setQuantiteMensuelle(aideSource.getQuantiteMensuelle());
			aideNoel.setMontant(aideSource.getMontant());
			aideNoel.setPublic(aideSource.getPublic());

			// Ajout aux résultats
			aides.add(aideNoel);
		}

		return aides;
	}

	@SuppressWarnings("unchecked")
	private List<Aide> getAidesSource(ObjectContext dc, PeriodeProps periode) {
		return dc.performQuery(SQL.queryWithParameters(new SqlParams()
				.setPeriode(periode)));
	}

	private PeriodePropsBean periodeAide(int year) {
		return mois(year, Calendar.DECEMBER);
	}

}
