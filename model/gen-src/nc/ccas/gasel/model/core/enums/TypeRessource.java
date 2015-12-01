package nc.ccas.gasel.model.core.enums;

import nc.ccas.gasel.model.Enumeration;
import nc.ccas.gasel.model.WithParent;
import nc.ccas.gasel.model.core.enums.auto._TypeRessource;
import nc.ccas.gasel.modelUtils.EnumerationSync;

import org.apache.cayenne.DataObjectUtils;

public class TypeRessource extends _TypeRessource implements Enumeration,
		WithParent {
	private static final long serialVersionUID = 5667139061048220412L;

	public static final int SALAIRE = 1;

	static {
		EnumerationSync sync = new EnumerationSync(TypeRessource.class);
		sync.add(SALAIRE, "Salaire", CategorieRessource.TRAVAIL);
	}

	@Override
	public String toString() {
		return getLibelle();
	}

	public Integer getId() {
		return (Integer) DataObjectUtils.pkForObject(this);
	}

	public boolean isSalaire() {
		return getId() == SALAIRE;
	}

}
