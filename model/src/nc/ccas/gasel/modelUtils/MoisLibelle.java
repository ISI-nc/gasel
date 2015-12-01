package nc.ccas.gasel.modelUtils;

import java.io.Serializable;

public class MoisLibelle implements Serializable {
	private static final long serialVersionUID = 2437122698578775503L;

	private final int numero;

	private final String libelle;

	public MoisLibelle(int numero, String libelle) {
		this.numero = numero;
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	public int getNumero() {
		return numero;
	}

}
