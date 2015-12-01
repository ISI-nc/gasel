package nc.ccas.gasel.reports;

import java.io.Serializable;

public class NombreValeur implements Cumulable<NombreValeur>, Serializable {
	private static final long serialVersionUID = -5751838397563679828L;

	private int nombre = 0;

	private int valeur = 0;

	public void cumule(NombreValeur other) {
		nombre += other.nombre;
		valeur += other.valeur;
	}

	public int getNombre() {
		return nombre;
	}

	public void setNombre(int nombre) {
		this.nombre = nombre;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

}
