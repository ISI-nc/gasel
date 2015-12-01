package nc.ccas.gasel.model.core.valid;

import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.Stack;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.SqlParams;
import nc.ccas.gasel.Utils;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.DataRow;
import org.apache.cayenne.query.SQLTemplate;

public class DossierValidator extends GaselValidator<Dossier> {
	private static final SQLTemplate CONFLICT_CHECK = new SQLTemplate(
			Personne.class, "SELECT dossier_id, personne_id"
					+ "  FROM dossier_personnes"
					+ " WHERE personne_id IN $ids$conds");

	public DossierValidator() {
		super(Dossier.class);
	}

	@Override
	public void validateImpl(Dossier object) {
		BasePageSql sql = new BasePageSql(object.getObjectContext());
		// --
		validateDataObject(object);
		requireProperty(false, object, "typeHabitat");

		requireProperty(true, object, "adresseHabitation");

		requireProperty(false, object, "adressePostale");

		if (requireProperty(true, object, "dateOuverture")) {
			if (object.getDateOuverture().after(Utils.today())) {
				error("Date d'ouverture dans le futur");
			}
		}

		// Pas de personnes en double
		checkPersonnesEnDouble(object);
		// Une personne ne peut être dans deux dossiers différents
		checkConflitPersonnes(object, sql);
	}

	private void checkPersonnesEnDouble(Dossier object) {
		Stack<Personne> personnes = new Stack<Personne>();
		personnes.addAll(object.getPersonnes());
		while (!personnes.isEmpty()) {
			Personne ref = personnes.pop();
			if (personnes.contains(ref)) {
				error(ref.getRepr() + " est en double.");
				while (personnes.contains(ref))
					personnes.remove(ref);
			}
		}
	}

	private void checkConflitPersonnes(Dossier object, BasePageSql sql) {
		if (object.getPersonnes().isEmpty())
			return;
		SqlParams params = sql.params() //
				.set("ids", object.getPersonnes()) //
				.setRaw("conds", condNotDossier(object));

		for (DataRow row : sql.query().rows(CONFLICT_CHECK, params)) {
			int personneId = (Integer) row.get("personne_id");
			int dossierId = (Integer) row.get("dossier_id");
			Personne p = sql.query().byId(Personne.class, personneId);
			Dossier d = sql.query().byId(Dossier.class, dossierId);
			error(p.getRepr() + " est déjà dans le dossier n°" + dossierId
					+ " (" + d.getChefFamille() + ")");
		}
	}

	private String condNotDossier(Dossier object) {
		return isInDb(object) ? " AND dossier_id <> " + intPKForObject(object)
				: "";
	}

}
