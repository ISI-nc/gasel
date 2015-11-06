/**
 * 
 */
package nc.ccas.gasel.starjet.aides;

import java.util.List;

import nc.ccas.gasel.fop.FoTableau;

public interface TableauHelper<T> {

	public void header(FoTableau tableau);

	public List<RowMontant> generateRows(T source);

}