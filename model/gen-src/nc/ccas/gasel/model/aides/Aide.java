package nc.ccas.gasel.model.aides;

import static com.asystan.common.beans.BeanUtils.nullSafeEquals;
import static com.asystan.common.cayenne_new.QueryFactory.createAnd;
import static com.asystan.common.cayenne_new.QueryFactory.createBetween;
import static com.asystan.common.cayenne_new.QueryFactory.createEquals;
import static nc.ccas.gasel.model.DeletionUtils.delete;
import static nc.ccas.gasel.modelUtils.DateUtils.debutMois;
import static nc.ccas.gasel.modelUtils.DateUtils.finMois;
import static nc.ccas.gasel.modelUtils.DateUtils.intersection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.TraqueModifs;
import nc.ccas.gasel.model.aides.auto._Aide;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;

@Feminin
public class Aide extends _Aide implements ComplexDeletion, ModifListener,
		TraqueModifs {
	private static final long serialVersionUID = 2974428177120705521L;

	private static final DateFormat SESSION_FORMAT = new SimpleDateFormat(
			"yyyyMMdd.kkmmss");

	public void creerBons(Date mois, Utilisateur utilisateur, Personne personne) {
		// if (!getObjectContext().modifiedObjects().isEmpty()
		// || !getObjectContext().deletedObjects().isEmpty()
		// || !getObjectContext().newObjects().isEmpty()) {
		// throw new IllegalStateException("Il y a des modifications");
		// }

		if (getObjectId().isTemporary())
			throw new IllegalStateException("Aide non enregistrée");

		// Si l'aide ne vaut pas pour ce mois, aucun bons à créer
		Date debutMoisBons = debutMois(mois);
		Date finMoisBons = finMois(mois);
		if (!intersection(debutMoisBons, finMoisBons, getDebut(), getFin()))
			return;

		String session = "EM_" + SESSION_FORMAT.format(new Date())
				+ utilisateur.getLogin();

		personne = (Personne) getObjectContext().localObject(
				personne.getObjectId(), personne);

		synchronized (Bon.class) {
			List<Bon> bons = bonsMois(mois);
			int bonsACreer = getQuantiteMensuelle() - bons.size();
			if (bonsACreer <= 0) {
				return;
			}

			// Méthode de génération V1...
			String prefixeSequence = prefixeSequence(mois);
			int sequence = prochaineSequenceDispo(prefixeSequence, bons);
			for (int i = 0; i < bonsACreer; i++) {
				Bon bon = (Bon) getObjectContext().newObject(Bon.class);
				bon.setMontant(getMontantUnitaire());
				bon.setDebut(debutMoisBons);
				bon.setFin(finMoisBons);
				bon.setSessionEdition(session);
				bon.setEtat(EtatBon.CREE);
				bon.setPersonne(personne);

				String numero = String.format("%6s%04d%02d", prefixeSequence,
						sequence, i + bons.size() + 1);

				bon.setNumero(numero);
				bon.setAide(this);
				// addToBons(bon); // implicitement fait par bon.setAide
			}
			getObjectContext().commitChanges();
		}
	}

	private String prefixeSequence(Date mois) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(mois);
		String prefixeDuNumero = String.format("%04d%02d", //
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
		return prefixeDuNumero;
	}

	private int prochaineSequenceDispo(String prefixeDuNumero, List<Bon> bons) {
		if (bons.size() > 0) {
			// Il y a déjà des bons générés ce mois sur cette aide
			// => on réutilise la séquence
			return Bon.extractSeqNum(bons.get(0).getNumero());
		}
		// Sinon on initialise une nouvelle séquence
		String ref = CommonQueries.maxString(Bon.class, "numero",
				"numero LIKE '" + prefixeDuNumero + "%'");
		if (ref == null) {
			// Aucun bons générés pour ce mois
			return 1;
		}
		// Incrément de la session précédente
		return Bon.extractSeqNum(ref) + 1;
	}

	@SuppressWarnings("unchecked")
	public List<Bon> bonsMois(Date mois) {
		// Sinon on cherche les bons déjà créés pour ce mois
		Expression expr = createAnd(createEquals("aide", this), //
				createBetween("debut", debutMois(mois), finMois(mois)));
		SelectQuery query = new SelectQuery(Bon.class, expr);
		query.addOrdering("numero", true);
		return getObjectContext().performQuery(query);
	}

	public List<Bon> bonsAEditer(Date mois) {
		List<Bon> retval = new ArrayList<Bon>();
		for (Bon bon : bonsMois(mois)) {
			if (!bon.getEtat().isCree())
				continue;
			retval.add(bon);
		}
		return retval;
	}

	public Integer getMontantUnitaire() {
		if (getMontant() == null || getQuantiteMensuelle() == null) {
			return null;
		}
		return getMontant() / getQuantiteMensuelle();
	}

	public void setMontantUnitaire(Integer value) {
		if (value == null) {
			setMontant(null);
			return;
		}
		if (getQuantiteMensuelle() == null) {
			setQuantiteMensuelle(1);
		}
		setMontant(getQuantiteMensuelle() * value);
	}

	@Override
	public void setQuantiteMensuelle(Integer quantiteMensuelle) {
		Integer montantUnitaire = getMontantUnitaire();
		super.setQuantiteMensuelle(quantiteMensuelle);
		if (montantUnitaire != null) {
			// On préserve le montant unitaire
			setMontantUnitaire(montantUnitaire);
		}
	}

	public boolean enConflit(Aide aide2) {
		// Nature différente : pas de conflit.
		if (!nullSafeEquals(getNature(), aide2.getNature()))
			return false;
		// Pas de mois en commun : pas de conflit.
		if (!DateUtils.moisEnCommun(getDebut(), getFin(), aide2.getDebut(),
				aide2.getFin()))
			return false;

		// Aucun bon : conflit.
		if (aide2.getBons().isEmpty())
			return true;
		// L'autre aide n'est pas immédiate : conflit.
		if (!aide2.getStatut().isImmediate())
			return true;

		// L'aide conflictuelle est immédiate, si elle est totalement annulée
		// elle
		// peut être ignorée.

		// Un bon non annulé : conflit.
		for (Bon bon : aide2.getBons()) {
			if (!bon.getEtat().isAnnule())
				return true;
		}

		// Tous les tests sont ok.
		return false;
	}

	public Integer getId() {
		if (getObjectId().isTemporary())
			return null;
		return DataObjectUtils.intPKForObject(this);
	}

	public void prepareForDeletion() {
		delete(getEau());
		delete(getOrduresMenageres());
		delete(getBons());
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.updateTraqueModifs(this, user, date);
		ModifUtils.triggerModified(user, date, getDossier());
	}

	public String getCode() {
		TypePublic p = getPublic();
		NatureAide n = getNature();
		SecteurAide s = n.getParent();

		return code(p) + "." + code(s) + "." + code(n);
	}

	private String code(DataObject o) {
		if (o == null)
			return "?";
		Object code = o.readProperty("code");
		if (code == null)
			return "?";
		return String.valueOf(code);
	}

	public int getDureeMois() {
		if (getDebut() == null || getFin() == null) {
			return 0; // not yet defined
		}
		return 1 + DateUtils.anneeMois(getFin())
				- DateUtils.anneeMois(getDebut());
	}

	public List<Bon> getBonsAnnules() {
		List<Bon> bonsAnnules = new ArrayList<>();
		for (Bon bon : getBons()) {
			if (bon.getEtat().isAnnule()) {
				bonsAnnules.add(bon);
			}
		}
		return bonsAnnules;
	}
	
	public boolean getHasBonsEdites() {
		for (Bon bon: getBons()) {
			if (bon.getEtat().isEdite() || bon.getEtat().isPartiellementUtilise() || bon.getEtat().isUtilise()) {
				return true;
			}
		}
		return false;
	}

}
