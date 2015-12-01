package nc.ccas.gasel.model.pi;

import static nc.ccas.gasel.modelUtils.DateUtils.addDays;
import static nc.ccas.gasel.modelUtils.DateUtils.addMonths;
import static nc.ccas.gasel.modelUtils.DateUtils.debutMois;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import java_gaps.Interval;
import nc.ccas.gasel.model.ModifListener;
import nc.ccas.gasel.model.ModifUtils;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.model.pi.auto._ArreteJF;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.query.SelectQuery;

import com.asystan.common.cayenne.QueryFactory;

public class ArreteJF extends _ArreteJF implements ModifListener {

	private static final long serialVersionUID = 6242101212800443663L;

	@SuppressWarnings("unchecked")
	public List<ArreteJF> getArretesDeLaMemeParcelle() {
		return getObjectContext().performQuery(
				new SelectQuery(ArreteJF.class, //
						QueryFactory.createEquals("attribution.parcelle",
								getAttribution().getParcelle())));
	}

	public boolean isRetardPaiement() {
		Interval<Date> periodeNonPayee = getPeriodeNonPayee();
		if (periodeNonPayee == null)
			return false;

		Date limite = DateUtils.addMonths(DateUtils.today(), -2);
		return periodeNonPayee.start.compareTo(limite) <= 0;
	}

	public Interval<Date> getPeriodeNonPayee() {
		Date payeJusquA = getAttribution().getPayeJusquA();
		Date debutPeriodeDue = payeJusquA == null ? null : addDays(payeJusquA,
				1);
		Date finPeriodeDue;
		if (new GregorianCalendar().get(Calendar.DAY_OF_MONTH) <= 10) {
			finPeriodeDue = addDays(debutMois(), -1);
		} else { // Le mois en cours est dû après le 10
			finPeriodeDue = addDays(addMonths(debutMois(), 1), -1);
		}
		Interval<Date> inter = DateUtils.inter( //
				debutPeriodeDue, finPeriodeDue, //
				getDebut(), getFin());
		if (inter == null)
			return null; // pas d'impayé, c'est bon
		if (inter.start.equals(getFin()))
			return null; // payé jusqu'à la fin, c'est bon

		return inter;
	}

	public void modified(Utilisateur user, Date date) {
		ModifUtils.triggerModified(user, date, getAttribution());
	}

}
