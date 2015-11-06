package nc.ccas.gasel.pages.budget.mensuel;

import static java_gaps.NumberUtils.sub;

import java.util.Collection;
import java.util.Set;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.budget.BudgetImpAnnuel;
import nc.ccas.gasel.model.budget.BudgetImpMensuel;
import nc.ccas.gasel.model.budget.BudgetSecteurAide;
import nc.ccas.gasel.model.budget.BudgetTypePublic;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.modelUtils.MoisLibelle;
import nc.ccas.gasel.reports.ReportUtils;

import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.tapestry.event.PageEvent;

public abstract class EditBudgetImpMensuel extends EditPage<BudgetImpAnnuel> {

	public EditBudgetImpMensuel() {
		super(BudgetImpAnnuel.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		for (BudgetImpMensuel mensuel : getObject().getMensuels()) {
			if (mensuel.getParSecteur().isEmpty()) {
				// On doit initialiser le budget
				for (SecteurAide sa : getObject().getImputation()
						.getSecteursAide()) {
					BudgetSecteurAide bsa = createDataObject(BudgetSecteurAide.class);
					bsa.setMontant(0.0);
					bsa.setSecteurAide(sa);
					mensuel.addToParSecteur(bsa);
					for (TypePublic tp : getTypesPublic()) {
						BudgetTypePublic btp = createDataObject(BudgetTypePublic.class);
						btp.setTypePublic(tp);
						btp.setMontant(0.0);
						bsa.addToParPublic(btp);
					}
				}
			}
		}
	}

	public abstract BudgetSecteurAide getParSecteur();

	public abstract void setParSecteur(BudgetSecteurAide bsa);

	public Set<SecteurAide> getSecteursAide() {
		return getObject().getImputation().getSecteursAide();
	}

	public Collection<TypePublic> getTypesPublic() {
		return getObject().getImputation().getTypesPublic();
	}

	public MoisLibelle[] getMoiss() {
		return DateUtils.moisLongs();
	}

	public abstract MoisLibelle getMois();

	public abstract SecteurAide getSecteurAide();

	public abstract TypePublic getTypePublic();

	public BudgetSecteurAide getBudgetSecteurAide() {
		return getBudgetSecteurAide(getSecteurAide());
	}

	private BudgetSecteurAide getBudgetSecteurAide(SecteurAide secteurAide) {
		BudgetImpMensuel mensuel = getBudgetImpMensuel();
		if (mensuel == null) {
			return null;
		}
		BudgetSecteurAide retval = mensuel.budgetSecteurAide(secteurAide);
		if (retval == null) {
			retval = createDataObject(BudgetSecteurAide.class);
			retval.setParent(mensuel);
			retval.setMontant(0d);
			retval.setSecteurAide(secteurAide);
		}
		return retval;
	}

	public BudgetTypePublic getBudgetTypePublic() {
		BudgetImpMensuel mensuel = getBudgetImpMensuel();
		if (mensuel == null) {
			return null;
		}
		return getBudgetSecteurAide().budgetPublic(getTypePublic());
	}

	// Appelée dans une ligne : mois est défini
	public Double getTotalLigne() {
		if (getObject().getRepartitionParSecteur()) {
			// Par secteur d'aide
			return ReportUtils.somme(getBudgetImpMensuel().getParSecteur(),
					"montant");
		} else {
			// Par type de public
			Double total = 0.0;
			for (BudgetSecteurAide bsa : getBudgetImpMensuel().getParSecteur()) {
				total += ReportUtils.somme(bsa.getParPublic(), "montant");
			}
			return total;
		}
	}

	public BudgetImpMensuel getBudgetImpMensuel() {
		return getObject().budgetMensuel(getMois().getNumero());
	}

	public Double getResteARepartir() {
		BudgetImpMensuel bim = getBudgetImpMensuel();
		return (Double) sub(bim.getMontant(), getTotalLigne());
	}

	@Override
	protected void prepareEnregistrer() {
		super.prepareEnregistrer();
		log("PREPARE ENREGISTRER: " + getObject().getRepartitionParSecteur());
		log("Object DC: " + getObject().getObjectContext());
		log("Page DC:   " + getObjectContext());
		log("modified? 1/"
				+ getObjectContext().modifiedObjects().contains(getObject()));
		log("modified? 2/"
				+ (getObject().getPersistenceState() == PersistenceState.MODIFIED));
	}

	@Override
	protected void prepareCommit() {
		super.prepareCommit();

		// C'est fou mais c'est comme ça...
		SQLTemplate template = new SQLTemplate(BudgetImpAnnuel.class,
				"UPDATE budget_imp_annuel SET repartition_par_secteur = "
						+ (getObject().getRepartitionParSecteur() ? "true" : "false")
						+ " WHERE id=" + sql.idOf(getObject()));
		getObjectContext().performNonSelectingQuery(template);
	}

}
