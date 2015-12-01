package nc.ccas.gasel.pages.paph;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.checks.DateInPeriode;
import nc.ccas.gasel.model.core.enums.Problematique;
import nc.ccas.gasel.model.core.enums.TypeProblematique;
import nc.ccas.gasel.model.paph.AccompagnementPAPH;
import nc.ccas.gasel.model.paph.DemandeTaxi;
import nc.ccas.gasel.model.paph.DeplacementTaxi;
import nc.ccas.gasel.model.paph.DossierPAPH;
import nc.ccas.gasel.model.paph.HandicapPAPH;
import nc.ccas.gasel.model.paph.ProcurationPAPH;
import nc.ccas.gasel.model.paph.SpecificiteCartePAPH;
import nc.ccas.gasel.model.paph.enums.Handicap;
import nc.ccas.gasel.model.paph.enums.SpecificiteCarteHand;
import nc.ccas.gasel.model.paph.enums.TauxHandicap;
import nc.ccas.gasel.reports.PeriodeProps;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class EditerDossierPaPh extends EditPage<DossierPAPH> implements
		PeriodeProps {

	public EditerDossierPaPh() {
		super(DossierPAPH.class);
	}

	public Date getDefaultPeriodeDebut() {
		return dates.debutAnnee();
	}

	public Date getDefaultPeriodeFin() {
		return dates.finAnnee();
	}

	public DossierPAPH getPaph() {
		return getObject();
	}

	public void setPaph(DossierPAPH paph) {
		setObject(paph);
	}

	@Override
	protected void prepareNewObject(DossierPAPH paph) {
		if (paph == null) {
			throw new RuntimeException("Impossible de créer un dossier PA/PH");
		}
		paph.setDateCreation(dates.today());
		paph.setSousTutelle(false);
		paph.setCorh(false);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);

		DossierPAPH paph = getPaph();

		if (paph.getEstHandicape() && paph.getPassageHandicapee() == null) {
			paph.setPassageHandicapee(new Date());
		}
	}

	public abstract ProcurationPAPH getProcuration();

	@Persist("workflow")
	public abstract SpecificiteCarteHand getSpecificiteHand();

	@Persist("workflow")
	public abstract SpecificiteCarteHand getSpecificite();

	public void ajouterSpecificite() {
		if (getSpecificite() == null)
			return;

		SpecificiteCartePAPH specPAPH = createDataObject(SpecificiteCartePAPH.class);
		specPAPH.setSpecificite(getSpecificite());
		getPaph().addToSpecificitesCarte(specPAPH);
		redirect();
	}

	@Persist("workflow")
	public abstract TauxHandicap getTauxHandicap();

	@Persist("workflow")
	public abstract Handicap getHandicap();

	public abstract HandicapPAPH getHandicapPAPH();

	public void ajouterHandicap() {
		if ((getHandicap() == null) || (getTauxHandicap() == null))
			return;

		HandicapPAPH handicap = createDataObject(HandicapPAPH.class);
		handicap.setHandicap(getHandicap());
		handicap.setTaux(getTauxHandicap());
		getPaph().addToHandicaps(handicap);
		redirect();
	}

	@Persist("workflow")
	public abstract TypeProblematique getType();

	@Persist("workflow")
	public abstract Problematique getProblematique();

	@Persist("workflow")
	public abstract AccompagnementPAPH getAccompagnement();

	public void ajouterAccompagnement() {
		((EditerAccompagnement) getRequestCycle().getPage(
				"paph/EditerAccompagnement")).activateForParent(getObject(),
				"dossier");
	}

	public void ajouterDeplacement() {
		((EditerDeplacement) getRequestCycle()
				.getPage("paph/EditerDeplacement")).activateForParent(
				getObject(), "dossier");
	}

	public void ajouterDemande() {
		((DemanderTaxi) getRequestCycle().getPage("paph/DemanderTaxi"))
				.activateForParent(getObject(), "dossier");
	}

	public List<DemandeTaxi> getDemandesTaxi() {
		return lists.filter(getPaph().getDemandesTaxi(),
				new DateInPeriode<DemandeTaxi>(getPeriodeDebut(),
						getPeriodeFin()) {
					@Override
					protected Date getDate(DemandeTaxi value) {
						return value.getDateDemande();
					}
				});
	}

	public List<DeplacementTaxi> getDeplacementsTaxi() {
		return lists.filter(getPaph().getDeplacementsTaxi(),
				new DateInPeriode<DeplacementTaxi>(getPeriodeDebut(),
						getPeriodeFin()) {
					@Override
					protected Date getDate(DeplacementTaxi value) {
						return value.getDate();
					}
				});
	}

}