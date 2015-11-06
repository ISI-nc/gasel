package nc.ccas.gasel.pages.aides;

import java.util.Date;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.ObjectCallback;
import nc.ccas.gasel.ObjectPage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.EtatBon;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.services.reports.ReportService;

import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.annotations.Persist;

public abstract class EditionBons extends ObjectPage<Aide> {

	public abstract Date getMois();

	@Persist("workflow")
	public abstract Personne getPersonne();

	public abstract void setPersonne(Personne personne);

	@Override
	public void open(Aide object, ObjectCallback<?, ? super Aide> callback) {
		prepareOpen(object, callback);
		setPersonne(object.getDossier().getDossier().getChefFamille());
		if (isNew(object)) {
			throw new PageRedirectException("NoParent");
		}
		redirect();
	}

	public void creerBons() {
		getAide().creerBons(getMois(), getLogin().getUser(), getPersonne());
	}

	public void editer(IRequestCycle cycle) {
		DataContext.bindThreadDataContext(getObjectContext());
		
		// Safety commit
		try {
			getObjectContext().commitChanges();
		} catch (Exception ex) {
			getObjectContext().rollbackChanges();
			throw new RuntimeException(ex);
		}

		// Création des bons manquants
		getAide().creerBons(getMois(), getLogin().getUser(), getPersonne());

		// Liste des bons à éditer
		List<Bon> bons = bonsAEditer();
		marquerEdite(bons);

		ReportService.invoke(cycle, "edition-bons", "BONS_ID", CayenneUtils.collectIds(bons));
	}

	public void reediter(IRequestCycle cycle) {
		List<Bon> bonsEdites = getBonsEdites();

		// Mise à jour des montants
		int montantBon = getAide().getMontantUnitaire();
		for (Bon bon : bonsEdites) {
			bon.setMontant(montantBon);
		}
		getObjectContext().commitChanges();

		ReportService.invoke(cycle, "edition-bons", "BONS_ID", CayenneUtils.collectIds(bonsEdites));
	}

	public List<Bon> getBonsEdites() {
		List<Bon> bonsEdites = sql.filtrer(getBonsMois(), new Check<Bon>() {
			public boolean check(Bon value) {
				return value.getEtat().isEdite();
			}
		});
		return bonsEdites;
	}

	private Date _bonsMois_date;

	private List<Bon> _bonsMois;

	public List<Bon> getBonsMois() {
		if (_bonsMois == null || _bonsMois_date != getMois()) {
			_bonsMois = getAide().bonsMois(getMois());
		}
		return _bonsMois;
	}

	public int getBonsAEditer() {
		return getAide().getQuantiteMensuelle() - getBonsMois().size()
				+ bonsAEditer().size();
	}

	private List<Bon> bonsAEditer() {
		return sql.filtrer(getBonsMois(), new Check<Bon>() {
			public boolean check(Bon value) {
				return value.getEtat().isCree();
			}
		});
	}

	private void marquerEdite(List<Bon> bons) {
		for (Bon bon : bons) {
			bon.setEtat(EtatBon.EDITE);
		}
		try {
			getObjectContext().commitChanges();
		} catch (Exception ex) {
			getObjectContext().rollbackChanges();
			throw new RuntimeException(ex);
		}
	}

	public Aide getAide() {
		return getObject();
	}

	public void setAide(Aide aide) {
		setObject(aide);
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_bonsMois = null;
		_bonsMois_date = null;
	}

}
