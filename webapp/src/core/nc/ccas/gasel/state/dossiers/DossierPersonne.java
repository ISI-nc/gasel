package nc.ccas.gasel.state.dossiers;

import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.io.IOException;
import java.io.Serializable;

import java_gaps.ComparisonChain;

import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DossierPersonne implements Comparable<DossierPersonne>,
		Serializable {
	private static final long serialVersionUID = -3322024135652653543L;

	private Dossier dossier;
	private Personne personne;

	public DossierPersonne(Dossier dossier, Personne personne) {
		this.dossier = dossier;
		this.personne = personne;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public Personne getPersonne() {
		return personne;
	}

	public int compareTo(DossierPersonne o) {
		return new ComparisonChain() //
				.append(personne.getNom(), o.personne.getNom()) //
				.append(personne.getPrenom(), o.personne.getPrenom()) //
				.append(intPKForObject(dossier), intPKForObject(o.dossier)) //
				.append(intPKForObject(personne), intPKForObject(o.personne)) //
				.result();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DossierPersonne))
			return false;
		DossierPersonne dp = (DossierPersonne) obj;
		return new EqualsBuilder().append(dossier, dp.dossier).append(personne,
				dp.personne).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(dossier).append(personne)
				.toHashCode();
	}

	@Override
	public String toString() {
		return dossier + "/" + personne;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(intPKForObject(getDossier()));
		out.writeInt(intPKForObject(getPersonne()));
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		dossier = CommonQueries.findById(Dossier.class, in.readInt());
		personne = CommonQueries.findById(Personne.class, in.readInt());
	}

}
