package nc.ccas.gasel.pages.budget.mensuel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java_gaps.NumberUtils;
import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.agents.budget.BudgetInformator;
import nc.ccas.gasel.agents.budget.SuiviMensuelImp;
import nc.ccas.gasel.agents.budget.result.SuiviMensuelImpModel;
import nc.ccas.gasel.model.budget.BudgetImpMensuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.cayenne.DataObject;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class Alimentation extends BasePage {

	private BudgetImpMensuel _budget;

	@Persist("workflow")
	public abstract Imputation getImputation();

	public abstract void setImputation(Imputation imputation);

	@Persist("workflow")
	@InitialValue("dates.debutMois")
	public abstract Date getMois();

	public void setImputationById(int id) {
		setImputation(sql.query().byId(Imputation.class, id));
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getImputation() == null) {
			setImputationById(Imputation.ALIMENTATION);
		}
	}

	public Number getAnneeMin() {
		return BudgetInformator.INSTANCE.getAnneeMin();
	}

	public Number getAnneeMax() {
		return BudgetInformator.INSTANCE.getAnneeMax();
	}

	public abstract Map<String, String> getQuerySets();

	private static final String[] TITRES = new String[] { //
	"Budget prévu", //
			"Report du mois précédent", //
			"Virements de crédit ou débits", // 
			"Total", //
			"Total des bons", //
			"Bons annulés ou partiellement utilisés", //
			"Reste disponible" };

	private List<Map<Object, Object>> _tableau;

	public List<Map<Object, Object>> getTableau() {
		if (_tableau != null)
			return _tableau;

		if (getBudget() == null) {
			return Collections.emptyList();
		}

		// Création du tableau
		_tableau = new ArrayList<Map<Object, Object>>(7);
		for (int i = 0; i < 7; i++) {
			Map<Object, Object> row = new HashMap<Object, Object>();
			// Titre
			row.put("titre", TITRES[i]);
			// Mise en évidence des cumuls (total et reste dispo)
			row.put("highlight", i == 3 || i == 6);
			// ok
			_tableau.add(row);
		}

		// Requète
		SuiviMensuelImpModel result = getResultModel();

		// Remplissage des cases
		for (DataObject col : result.getKeys()) {
			SuiviMensuelImpModel.Element element;
			element = result.getElements().get(col);

			int i = 0;
			_tableau.get(i++).put(col, element.getBudgetPrevu());
			_tableau.get(i++).put(col, element.getReportMoisPrec());
			_tableau.get(i++).put(col, element.getVirements());
			_tableau.get(i++).put(col, element.getTotal());
			_tableau.get(i++).put(col, element.getBonsUOP());
			_tableau.get(i++).put(col, element.getRecupBons());
			_tableau.get(i++).put(col, element.getReste());
		}

		return _tableau;
	}

	private SuiviMensuelImpModel _resultModel;

	private SuiviMensuelImpModel getResultModel() {
		if (_resultModel == null) {
			_resultModel = SuiviMensuelImp.INSTANCE //
					.getSuiviMensuelImp(getImputation(), getMois(), sql);
		}
		return _resultModel;
	}

	// ------------------------------------------------------------------------

	public BudgetImpMensuel getBudget() {
		if (_budget == null) {
			_budget = sql.query().unique(BudgetImpMensuel.class, new QuickAnd() //
					.equals("mois", mois(Calendar.MONTH)) //
					.equals("parent.imputation", getImputation()) //
					.equals("parent.parent.annee", mois(Calendar.YEAR)) //
					.expr());
		}
		return _budget;
	}

	public Number total(Map<Object, Object> row) {
		Number n = null;
		for (DataObject col : getColonnes()) {
			n = NumberUtils.add(n, (Number) row.get(col));
		}
		return n;
	}

	public List<? extends DataObject> getColonnes() {
		return getResultModel().getKeys();
	}

	private int mois(int field) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(getMois());
		return gc.get(field);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_budget = null;
		_tableau = null;
		_resultModel = null;
	}

	@Override
	public String getTitre() {
		return "Suivi de budget mensuel : " + getImputation();
	}

}
