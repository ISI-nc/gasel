package nc.ccas.gasel.pages.compta;

import java.util.ArrayList;
import java.util.List;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.Facture;
import nc.ccas.gasel.model.aides.PartieFacture;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.form.IPropertySelectionModel;

public abstract class RetoursBons extends EditPage<Facture> {

	private List<UsageBon> _bonsTableau;

	public RetoursBons() {
		super(Facture.class);
	}
	
	public abstract Bon getBon();
	public abstract void setBon(Bon bon);

	@Override
	protected void prepareNewObject(Facture object) {
		object.setValide(false);
	}

	public abstract String getLibelleNouvellePartie();

	public abstract void setLibelleNouvellePartie(String value);

	public void ajouterPartie() {
		String libelle = getLibelleNouvellePartie();
		if (libelle == null) {
			return;
		}

		PartieFacture partie = createDataObject(PartieFacture.class);
		partie.setLibelle(libelle);
		getFacture().addToParties(partie);
		setLibelleNouvellePartie(null);
		setPartieAjout(partie);
		setPartieActive(partie);

		enregistrerSansFermer();
		redirect();
	}

	@Persist("workflow")
	public abstract PartieFacture getPartieAjout();

	public abstract void setPartieAjout(PartieFacture partie);

	@Persist("workflow")
	public abstract PartieFacture getPartieActive();

	public abstract void setPartieActive(PartieFacture partie);

	public IPropertySelectionModel getPartiesModel() {
		return psm.dataObject(getFacture().getParties(), "libelle");
	}

	public abstract String getBonAjout();

	public abstract void setBonAjout(String numero);

	public Facture getFacture() {
		return getObject();
	}

	public void setFacture(Facture facture) {
		setObject(facture);
	}

	public int getMontantTotal() {
		int total = 0;
		for (PartieFacture partie : getFacture().getParties()) {
			for (UsageBon usageBon : partie.getBons()) {
				if (usageBon.getMontantUtilise() == null) {
					continue;
				}
				total += usageBon.getMontantUtilise();
			}
		}
		return total;
	}

	public void ajouterBon() {
		Bon bon = findBon();

		if (bon == null ) {
			if (getBonsCorrespondants().isEmpty()) {
				setErreurAjout("Pas de bon n°" + getBonAjout());
			} else {
				setErreurAjout("Plusieurs bons n°" + getBonAjout());
			}
			return;
		}

		ajouterBon(bon);
	}

	public void ajouterBon(Bon bon) {
		if (bon.getUsage() != null) {
			setErreurAjout("Bon déjà utilisé ! ("
					+ bon.getUsage().getFacture().getFacture().getNumero()
					+ ")");
			return;
		}
		if (!bon.getEtat().isEdite()) {
			setErreurAjout("Bon à l'état "
					+ bon.getEtat().getLibelle().toLowerCase() + " !");
			return;
		}
		
		setBonAjout(null);
		setBonsCorrespondants(null);
		
		getPartieAjout().ajouterBon(bon);
		if (getPartieActive() != null)
			setPartieActive(getPartieAjout());

		if (!enregistrerSansFermer()) {
			setErreurAjout("Attention : problème à l'enregistrement");
		}
		redirect();
	}

	private Bon findBon() {
		List<Bon> bons = sql.query(Bon.class, new QuickAnd().equals("numero",
				getBonAjout()).expr());
		setBonsCorrespondants(bons);
		if (bons.size() != 1)
			return null;
		return bons.get(0);
	}

	@Persist("workflow")
	public abstract List<Bon> getBonsCorrespondants() ;

	public abstract void setBonsCorrespondants(List<Bon> bons) ;

	public abstract void setErreurAjout(String value);

	public abstract PartieFacture getPartieASupprimer();

	public abstract void setPartieASupprimer(PartieFacture partie);

	public void setPartieASupprimer() {
		setPartieASupprimer(getPartieActive());
	}

	public void supprimerPartie() {
		PartieFacture partieASupprimer = getPartieASupprimer();
		partieASupprimer.prepareForDeletion();
		getFacture().removeFromParties(getPartieASupprimer());
		getObjectContext().deleteObject(partieASupprimer);
		enregistrerSansFermer();
		redirect();
	}

	public List<UsageBon> getBonsTableau() {
		if (_bonsTableau == null) {
			List<UsageBon> bons;

			if (getPartieActive() == null) {
				bons = new ArrayList<UsageBon>();
				for (PartieFacture partie : getFacture().getParties()) {
					bons.addAll(partie.getBons());
				}
			} else {
				bons = getPartieActive().getBons();
			}

			_bonsTableau = sort(bons) //
					.by("facture.libelle") //
					.by("bon.montant") //
					.by("bon.numero") //
					.results();
		}
		return _bonsTableau;
	}

	@InitialValue("false")
	public abstract boolean getCreationDansLaFoulee();

	public abstract void setCreationDansLaFoulee(boolean value);

	public void nouvelleFacture() {
		setCreationDansLaFoulee(true);
		enregistrer();
		redirect();
	}

	@Override
	public void success() {
		if (getCreationDansLaFoulee()) {
			setObject(null);
			redirect();
			return;
		}
		super.success();
	}

	@Override
	public String getTitre() {
		Facture f = getFacture();
		if (f == null || f.getNumero() == null)
			return "Facture n°?";
		return "Facture n°" + f.getNumero();
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);

		_bonsTableau = null;
	}

}
