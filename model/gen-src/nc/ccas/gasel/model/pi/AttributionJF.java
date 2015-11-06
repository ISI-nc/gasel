package nc.ccas.gasel.model.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._AttributionJF;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;

@Feminin
public class AttributionJF extends _AttributionJF implements ComplexDeletion,
		ModifListener {
	private static final long serialVersionUID = 8321364208540583782L;

	public void prepareForDeletion() {
		ObjectContext context = getObjectContext();
		for (ArreteJF arrete : getArretes()) {
			context.deleteObject(arrete);
		}
		for (Paiement paiement : getPaiements()) {
			context.deleteObject(paiement);
		}
		for (ControleEntretien controle : getControles()) {
			context.deleteObject(controle);
		}
		setDemande(null);
		setParcelle(null);
	}

	// public Date getDebut() {
	// ArreteJF arrete = getPremierArrete();
	// if (arrete == null)
	// return null;
	// return arrete.getDebut();
	// }

	public Date getFin() {
		ArreteJF arrete = getDernierArrete();
		if (arrete == null)
			return null;
		return arrete.getFin();
	}

	public ArreteJF getPremierArrete() {
		return premier(getArretes());
	}

	public ArreteJF getDernierArrete() {
		return dernier(getArretes());
	}

	public Paiement getDernierPaiement() {
		return dernier(getPaiements());
	}

	public ControleEntretien getDernierControle() {
		// return dernier(getControles());
		if (isTerminee())
			return null;
		// Contrôles par ordre chrono décroissant (plus récent avant)
		ArrayList<ControleEntretien> controles = new ArrayList<ControleEntretien>();
		controles.addAll(getControles());
		Collections.sort(controles, new Comparator<ControleEntretien>() {
			public int compare(ControleEntretien o1, ControleEntretien o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		if (controles.size() > 0)
			return controles.get(0);
		else
			return null;
	}

	private <T extends DataObject> T premier(List<T> list) {
		T retval = null;
		for (T t : list) {
			if (retval == null || debut(t).before(debut(retval)))
				retval = t;
		}
		return retval;
	}

	private <T extends DataObject> T dernier(List<T> list) {
		T retval = null;
		for (T t : list) {
			if (retval == null || debut(t).after(debut(retval)))
				retval = t;
		}
		return retval;
	}

	private Date debut(DataObject o) {
		return (Date) o.readProperty("debut");
	}

	public boolean isTerminee() {
		ArreteJF a = getDernierArrete();
		return a == null || a.getFin().before(new Date());
	}

	public boolean hasPaiementEnCours() {
		Paiement p = getDernierPaiement();
		return p != null && p.getFin().after(new Date());
	}

	public boolean isRetardPaiement() {
		for (ArreteJF arrete : getArretes()) {
			if (arrete.isRetardPaiement())
				return true;
		}
		return false;
	}

	public boolean isRetardEntretien() {
		if (isTerminee())
			return false;

		// Contrôles par ordre chrono décroissant (plus récent avant)
		ArrayList<ControleEntretien> controles = new ArrayList<ControleEntretien>();
		controles.addAll(getControles());
		Collections.sort(controles, new Comparator<ControleEntretien>() {
			public int compare(ControleEntretien o1, ControleEntretien o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});

		// Parcours des contrôles :
		// - renvoie true si tous les contrôles sont à corriger depuis plus de 2
		// | mois.
		// - renvoie false si un contrôle est OK dans les 2 mois.
		// - renvoie false sinon.
		//
		// Donc: renvoie true ssi tous les contrôles sont à corriger depuis plus
		// de 2 mois.

		Date dateMin = DateUtils.nMoisAvant(2);
		for (ControleEntretien controle : controles) {
			if (!controle.getEtatParcelle().getACorriger())
				return false;
			// Contrôle à corriger
			if (controle.getDate().before(dateMin)) {
				// Et datant de plus de 2 mois
				return true;
			}
		}
		return false;
	}

	public boolean isEnFinDeLocation() {
		if (isTerminee())
			return false;
		return getDernierArrete().getFin().compareTo(DateUtils.nMoisApres(2)) <= 0;
	}

	public Date getPayeJusquA() {
		Date retval = null;
		for (Paiement paiement : getPaiements()) {
			if (retval == null || retval.before(paiement.getFin())) {
				retval = paiement.getFin();
			}
		}
		return retval;
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getDemande());
	}

}
