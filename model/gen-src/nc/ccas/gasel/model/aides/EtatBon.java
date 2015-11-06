package nc.ccas.gasel.model.aides;

import org.apache.cayenne.DataObjectUtils;

import nc.ccas.gasel.model.Automate;
import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.aides.auto._EtatBon;
import nc.ccas.gasel.modelUtils.EnumerationSync;

public class EtatBon extends _EtatBon implements Enumeration {
	private static final long serialVersionUID = 5590442531027735139L;

	public static final int UTILISE = 1;

	public static final int PARTIELLEMENT_UTILISE = 2;

	public static final int ANNULE = 3;

	public static final int EDITE = 4;

	public static final int CREE = 5;

	private static final Automate<Integer> AUTOMATE = new Automate<Integer>() {
		protected void initialize() {
			setInitialState(CREE);

			// Fonctionnement standard
			add(CREE, EDITE);

			add(EDITE, PARTIELLEMENT_UTILISE);
			add(EDITE, UTILISE);
			add(EDITE, ANNULE);

			// Modification de montant
			add(UTILISE, PARTIELLEMENT_UTILISE);
			add(PARTIELLEMENT_UTILISE, UTILISE);

			// Suppression d'une facture
			add(UTILISE, EDITE);
			add(PARTIELLEMENT_UTILISE, EDITE);
		}
	};

	static {
		EnumerationSync sync = new EnumerationSync(EtatBon.class);
		sync.add(UTILISE, "Utilisé");
		sync.add(PARTIELLEMENT_UTILISE, "Partiellement utilisé");
		sync.add(ANNULE, "Annulé");
		sync.add(EDITE, "Edité");
		sync.add(CREE, "Créé");
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	public boolean isUtilise() {
		return getId() == UTILISE;
	}

	public boolean isPartiellementUtilise() {
		return getId() == PARTIELLEMENT_UTILISE;
	}

	public boolean isAnnule() {
		return getId() == ANNULE;
	}

	public boolean isEdite() {
		return getId() == EDITE;
	}

	public boolean isCree() {
		return getId() == CREE;
	}

	public void checkTransition(EtatBon next) {
		Integer from = getId();
		Integer to = next.getId();

		// pas de changement -> aucune vérif
		if (from == to)
			return;

		// Vérification par automate
		AUTOMATE.checkTransition(from, to);
	}

	public String getAbbreviation() {
		switch ((int) getId()) {
		case CREE:
			return "C";
		case EDITE:
			return "E";
		case UTILISE:
			return "U";
		case PARTIELLEMENT_UTILISE:
			return "PU";
		case ANNULE:
			return "A";
		}
		return "?";
	}

}
