package nc.ccas.gasel.pages.pi;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;
import static org.apache.cayenne.exp.Expression.fromString;

import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Check;
import nc.ccas.gasel.checks.NegateCheck;
import nc.ccas.gasel.checks.PathCheck;
import nc.ccas.gasel.checks.PeriodeActive;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.model.pi.AspectDossierPI;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.JardinFamilial;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.exp.Expression;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;

public abstract class GestionDispositif extends BasePage {

	private List<AttributionJF> _attributions;

	public abstract JardinFamilial getJardin();

	@InitialValue("true")
	@Persist("workflow")
	public abstract boolean getAttributionActive();

	public String getTitreTableau() {
		if (getJardin() == null) {
			return "Jardins familiaux";
		}
		return "Jardin familial : " + getJardin().getNom();
	}

	public List<AttributionJF> getAttributionsEnCours() {
		if (_attributions == null) {
			List<Expression> exprs = new LinkedList<Expression>();
			if (getJardin() != null) {
				exprs.add(createEquals("parcelle.jardin", getJardin()));
			}

			// Pré-filtre pour "active" (pas encore fiable cause plusieurs
			// arrêtes possibles, donc trop large)
			// --> on complète avec un filtre java
			Expression activeExpr;
			Check<DataObject> arreteCheck = new PathCheck<DataObject, ArreteJF>( //
					"arretes", ArreteJF.class, //
					PeriodeActive.INSTANCE);
			if (getAttributionActive()) {
				activeExpr = fromString("arretes.debut <= $date and $date <= arretes.fin");
			} else {
				activeExpr = fromString("$date < arretes.debut or arretes.fin < $date");
				arreteCheck = new NegateCheck<DataObject>(arreteCheck);
			}
			exprs.add(activeExpr.expWithParameters(sql.params() //
					.timestamp("date", dates.today())));

			_attributions = sort(
					sql.filtrer(sql.query(AttributionJF.class,
							createAnd(exprs), "parcelle", "arretes"),
							arreteCheck)) //
					.by("parcelle.jardin.nom") //
					.by("parcelle.numero").results();
		}
		return _attributions;
	}

	public abstract AttributionJF getAttr();

	public String getAdresseAttr() {
		if (getAttr().getDemande().getDossier() != null) {
			return getAttr().getDemande().getDossier().getDossier()
					.getAdresseHabitation().toString();
		}
		if (getAttr().getDemande().getCollectivite() != null) {
			return getAttr().getDemande().getCollectivite().getAdresse()
					.toString();
		}
		return null;
	}

	public String getTelephone() {
		AspectDossierPI dossier = getAttr().getDemande().getDossier();
		if (dossier == null)
			return null;
		
		Personne cf = dossier.getDossier().getChefFamille();

		String retval = (cf.getTelephoneFixe() == null ? "--" : //
				cf.getTelephoneFixe());
		retval += " / ";
		retval += (cf.getTelephonePortable() == null ? "--" : //
				cf.getTelephonePortable());
		retval = retval.replace("null", "--"); // nulls résiduels...
		return retval;
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_attributions = null;
	}

}
