package nc.ccas.gasel.pages.budget.annuel;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.BudgetAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpMensuel;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.pages.budget.mensuel.EditBudgetImpMensuel;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.PageEvent;

public abstract class EditBudgetImpAnnuel extends EditPage<BudgetImpAnnuel> {

	public abstract Imputation getImputation();

	public abstract void setImputation(Imputation imputation);

	public EditBudgetImpAnnuel() {
		super(BudgetImpAnnuel.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		setImputation(getObject().getImputation());
		if (getObject().getMensuels().isEmpty()) {
			for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
				BudgetImpMensuel budImpMensuel = createDataObject(BudgetImpMensuel.class);
				budImpMensuel.setMois((short) i);
				// la valeur par défaut car reparti selon les secteurAide
				budImpMensuel.setRestant(0.0);
				getObject().addToMensuels(budImpMensuel);
			}
		}
	}

	@Override
	protected void prepareNewObject(BudgetImpAnnuel object) {
		getObject().setRepartitionParSecteur(true);
	}

	public void editerBudgetImpMensuel() {
		EditBudgetImpMensuel page = (EditBudgetImpMensuel) getRequestCycle()
				.getPage("budget/mensuel/EditBudgetImpMensuel");
		page.open(getObject());
	}

	public void repartirBudgetImpMensuel() {
		double montant = getObject().getMontant();
		int montantMois = (int) montant / 12;
		int reste = (int) montant - montantMois * 12;
		for (BudgetImpMensuel budMensuel : getObject().getMensuels()) {
			budMensuel.setMontant((double) (montantMois + (reste > 0 ? 1 : 0)));
			if (reste > 0)
				reste--;
		}
	}

	public void changerBudgetEncoursEdition() {
		Imputation imputation = getImputation();
		BudgetAnnuel ba = getObject().getParent();
		for (BudgetImpAnnuel bia : ba.getBudgetsImputation()) {
			if (bia.getImputation().equals(imputation)) {
				setObject(bia);
				redirect();
			}
		}
		throw new ApplicationRuntimeException(
				"Pas de budget pour l'imputation " + imputation);
	}

	@SuppressWarnings("unchecked")
	public double getBudgetImpMensuel(Imputation imputation) {
		Expression qualifier3 = Expression.fromString("parent.imputation ="
				+ " $imputation");
		qualifier3 = qualifier3.expWithParameters(Collections.singletonMap(
				"imputation", imputation));
		SelectQuery select3 = new SelectQuery(BudgetImpMensuel.class,
				qualifier3);
		List<BudgetImpMensuel> nbPH = getObjectContext().performQuery(select3);
		if (nbPH.size() > 0) {
			return nbPH.get(0).getMontant();
		} else {
			return 0.0;
		}
	}

	public double getRestantBudgetImpMensuel(short mois) {
		for (BudgetImpMensuel bud : getObject().getMensuels()) {
			if (bud.getMois().equals(mois)) {
				return bud.getRestant();
			}
		}
		return 0.0;
	}

}
