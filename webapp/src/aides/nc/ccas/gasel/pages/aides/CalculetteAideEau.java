package nc.ccas.gasel.pages.aides;

import java.util.Calendar;
import java.util.GregorianCalendar;

import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectCallbackPage;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.core.Constante;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.pages.dossiers.CalculetteData;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

public abstract class CalculetteAideEau extends
		ObjectCallbackPage<CalculetteData> {

	public void success() {
		CalculetteData data = new CalculetteData();

		data.setPeriodePrestation(getPeriodePrestation() + " "
				+ getAnneePrestation());
		data.setPriseEnChargeTotale(getPriseEnChargeTotale());
		data.setMontantDejaPaye(getDejaPaye());
		data.setRestantDu(getRestantDu());
		data.setDepassementM3(getDepassementM3());

		success(data);
	}

	@Persist("workflow")
	public abstract Dossier getDossier();

	public abstract void setDossier(Dossier dossier);

	public void activate(AspectAides aspect,
			ObjectCallback<?, ? super CalculetteData> callback) {
		activate(aspect.getDossier(), callback);
	}

	public void activate(Dossier dossier,
			ObjectCallback<?, ? super CalculetteData> callback) {
		prepareActivation(callback);
		setDossier(dossier);

		setNbPersonnes(dossier.getNbPersonnes());
		setNbHandicapes(dossier.getNbPersonnesHandicapees());
		redirect();
	}

	public abstract int getNbPersonnes();

	public abstract void setNbPersonnes(int nb);

	public abstract int getNbHandicapes();

	public abstract void setNbHandicapes(int nb);

	public abstract String getPeriodePrestation();

	public abstract int getAnneePrestation();

	public abstract double getMontantFacture();

	public abstract double getPecRemise();

	public abstract double getConsoEffective();

	public abstract double getDejaPaye();

	public abstract double getPrixUnitaireM3();

	public abstract double getPrixAbonnement();

	public abstract double getPrixEauxUsees();

	public abstract double getPrixServiceEau();

	public abstract double getPrixAssainissement();

	public abstract double getPrixGdTuyau();

	public double getDouble(String nom) {
		return Constante.getDouble(nom);
	}

	public int getInt(String nom) {
		return Constante.getInteger(nom);
	}

	public void calculer() {
		// nb de m3 par personnes du foyer
		Double nbM3PremierePersonne = getDouble("aides/eau/conso_1ere_pers");
		Double nbM3ParPersonneDuFoyer = getDouble("aides/eau/conso_pers");

		Double consoMaxPriseEnCharge = getDouble("aides/eau/conso_max");

		// conso Max accordée:
		// = (nbPersonnes * nbM3parFoyer) + (nbHandicapés *
		// nbM3SupplémentairesParHandicapé)
		Double consoMaxAccordee = nbM3PremierePersonne //
				+ nbM3ParPersonneDuFoyer * (getNbPersonnes() - 1);
		if (consoMaxPriseEnCharge > 0
				&& consoMaxAccordee > consoMaxPriseEnCharge) {
			consoMaxAccordee = consoMaxPriseEnCharge;
		}
		// conso accordée = min(consoMax,consoEffective)
		_consoAccordee = Math.min(consoMaxAccordee, getConsoEffective());
		_depassementM3 = Math.max(0, getConsoEffective() - consoMaxAccordee);

		// totaux en fonction de la consommation et du prix au m3 Double
		Double totalConsoPriseEnCharge = _consoAccordee * getPrixUnitaireM3();
		Double totalEauxUsees = _consoAccordee * getPrixEauxUsees();
		Double totalServiceEau = _consoAccordee * getPrixServiceEau();
		Double totalAssainissement = _consoAccordee * getPrixAssainissement();
		Double totalGdTuyau = _consoAccordee * getPrixGdTuyau();
		Double totalAbonnement = getPrixAbonnement();

		// prise en charge totale: somme des totaux
		Double priseEnCharge = totalConsoPriseEnCharge + totalEauxUsees
				+ totalServiceEau + totalAssainissement + totalGdTuyau
				+ totalAbonnement;
		// vérification de non dépassement de la facture avec le cumule la somme
		// déjà payée:
		// reste à payer: montantFacture - montantPayé
		Double resteAPayer = getMontantFacture() - getDejaPaye();
		// prise en charge = mini des deux
		priseEnCharge = Math.min(priseEnCharge, resteAPayer);

		// restantDu = montantFacture - priseEnCharge - montantPayé
		Double restantDu = getMontantFacture() - priseEnCharge - getDejaPaye();
		// Modif pour Lolo
		restantDu = (double) Math.round(restantDu);

		// récupération du montant mini de restant du (sinon prise en charge par
		// le ccas)
		Double borneInfRestantDu = (double) getInt("aides/eau/restant_du_min");

		// comparaison de la borne inf avec le restantDu
		if (restantDu <= borneInfRestantDu) {
			// ajout du restantDu à la prise en charge
			// -> restantDu = 0
			priseEnCharge += restantDu;
			restantDu = 0D;
		}

		Double priseEnChargeTotale = priseEnCharge + getPecRemise();

		// maj du model de vue:
		_priseEnCharge = priseEnCharge;
		_priseEnChargeTotale = priseEnChargeTotale;
		_restantDu = restantDu;

		_calculFait = true;
	}

	private boolean _calculFait = false;

	private double _consoAccordee;

	private double _priseEnCharge;

	private double _priseEnChargeTotale;

	private double _restantDu;

	private double _depassementM3;

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_calculFait = false;
	}

	public double getConsoAccordee() {
		if (!_calculFait)
			calculer();
		return _consoAccordee;
	}

	public double getPriseEnCharge() {
		if (!_calculFait)
			calculer();
		return _priseEnCharge;
	}

	public double getPriseEnChargeTotale() {
		if (!_calculFait)
			calculer();
		return _priseEnChargeTotale;
	}

	public double getRestantDu() {
		if (!_calculFait)
			calculer();
		return _restantDu;
	}

	public double getDepassementM3() {
		if (!_calculFait)
			calculer();
		return _depassementM3;
	}

	public static final IPropertySelectionModel TRIMESTRE_PSM = new StringPropertySelectionModel(
			new String[] { "1er trimestre", "2è trimestre", "3è trimestre",
					"4è trimestre" });

	public IPropertySelectionModel getPeriodePrestationPSM() {
		return TRIMESTRE_PSM;
	}

	public int getCurrentYear() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}

}
