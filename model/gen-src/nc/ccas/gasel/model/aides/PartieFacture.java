package nc.ccas.gasel.model.aides;

import java.util.Date;

import nc.ccas.gasel.Feminin;
import nc.ccas.gasel.model.ComplexDeletion;
import nc.ccas.gasel.model.aides.auto._PartieFacture;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;

@Feminin
public class PartieFacture extends _PartieFacture implements ComplexDeletion {
	private static final long serialVersionUID = -1277078256379350440L;

	public int getTotal() {
		int total = 0;
		for (UsageBon usageBon : getBons()) {
			total += usageBon.getMontantUtilise();
		}
		return total;
	}

	public int getTotalAlloue() {
		int total = 0;
		for (UsageBon usageBon : getBons()) {
			total += usageBon.getBon().getMontant();
		}
		return total;
	}

	public void prepareForDeletion() {
		ObjectContext context = getObjectContext();
		for (UsageBon usage : getBons()) {
			usage.prepareForDeletion();
			context.deleteObject(usage);
		}
	}

	public void ajouterBon(Bon bon) {
		UsageBon usage = (UsageBon) getObjectContext().newObject(UsageBon.class);
		usage.setBon(bon);
		usage.setDate(new Date());
		usage.setMontantUtilise(bon.getMontant());
		bon.setEtat((EtatBon) DataObjectUtils.objectForPK(getObjectContext(),
				EtatBon.class, EtatBon.UTILISE));
		bon.setUsage(usage);
		addToBons(usage);
	}

}
