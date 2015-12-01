/**
 * 
 */
package nc.ccas.gasel.starjet.aides;

public class RowMontant {

	private final double montant;

	private final Object[] values;

	public RowMontant(double montant, Object... values) {
		this.montant = montant;
		this.values = values;
	}

	public double getMontant() {
		return montant;
	}

	public Object[] getValues() {
		return values;
	}

}