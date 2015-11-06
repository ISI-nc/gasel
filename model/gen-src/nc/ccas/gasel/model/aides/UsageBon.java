package nc.ccas.gasel.model.aides;

import static nc.ccas.gasel.modelUtils.CommonQueries.findById;

import java.util.Date;

import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.aides.auto._UsageBon;
import nc.ccas.gasel.model.core.Utilisateur;

import com.asystan.common.beans.BeanUtils;

public class UsageBon extends _UsageBon implements ComplexDeletion,
		ModifListener {
	private static final long serialVersionUID = -3108630036286887720L;

	public void prepareForDeletion() {
		getBon().setEtat(
				findById(EtatBon.class, EtatBon.EDITE, getObjectContext()));
		getBon().setUsage(null);
		setBon(null);
	}

	@Override
	public void setMontantUtilise(Integer montantUtilise) {
		super.setMontantUtilise(montantUtilise);
		if (BeanUtils.nullSafeEquals(montantUtilise, getBon().getMontant())) {
			getBon().setEtat(EtatBon.UTILISE);
		} else {
			getBon().setEtat(EtatBon.PARTIELLEMENT_UTILISE);
		}
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getBon());
		ModifUtils.triggerModified(user, date, getFacture());
		ModifUtils.triggerModified(user, date, getArrete());
	}

}
