package nc.ccas.gasel.model.budget;

import static com.asystan.common.cayenne.QueryFactory.createOr;
import static nc.ccas.gasel.model.budget.auto._LigneVirement.DESTINATION_ID_PROPERTY;
import static nc.ccas.gasel.model.budget.auto._LigneVirement.DESTINATION_TYPE_PROPERTY;
import static nc.ccas.gasel.model.budget.auto._LigneVirement.SOURCE_ID_PROPERTY;
import static nc.ccas.gasel.model.budget.auto._LigneVirement.SOURCE_TYPE_PROPERTY;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;

import java.util.List;

import nc.ccas.gasel.model.vues.HasAideMontants;
import nc.ccas.gasel.sql.QuickAnd;

import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.Persistent;

public interface Budget extends Persistent, HasAideMontants {

	public int getType();

	public Double getMontant();

	public Budget getParent();

	public List<? extends Budget> getChildren();

	public String getLibelle();

	public double getVirements();

	/**
	 * Helper for Budget implementation
	 */
	static class Helper {
		private final Budget budget;

		private List<LigneVirement> lignes;

		public Helper(Budget budget) {
			this.budget = budget;
		}

		public double getMontantVirements() {
			double montant = 0;
			for (LigneVirement ligne : getLignes()) {
				if (ligne.isFrom(budget)) {
					montant -= ligne.getMontant();
				} else if (ligne.isTo(budget)) {
					montant += ligne.getMontant();
				} else {
					throw new RuntimeException("Ligne invalide: " + ligne
							+ " (pour " + budget + ")");
				}
			}
			for (Budget child : budget.getChildren()) {
				montant += child.getVirements();
			}
			return montant;
		}

		private List<LigneVirement> getLignes() {
			if (lignes == null) {
				int type = budget.getType();
				int id = DataObjectUtils.intPKForObject(budget);
				lignes = select(LigneVirement.class, createOr( //
						new QuickAnd() //
								.equals(SOURCE_TYPE_PROPERTY, type) //
								.equals(SOURCE_ID_PROPERTY, id) //
								.expr(), //
						new QuickAnd() //
								.equals(DESTINATION_TYPE_PROPERTY, type) //
								.equals(DESTINATION_ID_PROPERTY, id) //
								.expr()));
			}
			return lignes;
		}

		public void clear() {
			lignes = null;
		}

	}

}
