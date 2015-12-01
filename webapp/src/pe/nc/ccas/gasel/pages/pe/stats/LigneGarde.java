package nc.ccas.gasel.pages.pe.stats;

import java.io.Serializable;
import java.util.Comparator;

import nc.ccas.gasel.model.pe.AssistanteMaternelle;
import nc.ccas.gasel.model.pe.EnfantRAM;

import org.apache.cayenne.query.Ordering;
import com.asystan.common.comparators.CompositeComparator;

@SuppressWarnings("unchecked")
public class LigneGarde implements Serializable, Comparable<LigneGarde> {
	private static final long serialVersionUID = -5421956566347696144L;

	private static final Comparator<Object> COMPARATOR = new CompositeComparator<Object>(
			(Comparator<Object>) new Ordering("personne.nom", true), //
			(Comparator<Object>) new Ordering("personne.prenom", true));

	private final AssistanteMaternelle assistante;

	private final EnfantRAM enfant;

	public LigneGarde(AssistanteMaternelle assistante, EnfantRAM enfant) {
		this.assistante = assistante;
		this.enfant = enfant;
	}

	public int compareTo(LigneGarde o) {
		int cmp;
		cmp = COMPARATOR.compare(assistante, o.getAssistante());
		if (cmp != 0)
			return cmp;
		cmp = COMPARATOR.compare(enfant, o.getEnfant());
		return cmp;
	}

	public AssistanteMaternelle getAssistante() {
		return assistante;
	}

	public EnfantRAM getEnfant() {
		return enfant;
	}

}
