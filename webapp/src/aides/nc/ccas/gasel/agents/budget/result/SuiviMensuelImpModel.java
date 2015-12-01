package nc.ccas.gasel.agents.budget.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.collections.ToStringComparator;

import org.apache.cayenne.DataObject;

import com.asystan.common.AutoBox;

public class SuiviMensuelImpModel {

	public static class Element {

		private Integer budgetPrevu;

		private Integer reportMoisPrec;

		private Integer virements;

		private Integer bonsUOP;

		private Integer recupBons;

		public Integer getBonsUOP() {
			return bonsUOP;
		}

		public void setBonsUOP(Integer bonsUOP) {
			this.bonsUOP = bonsUOP;
		}

		public Integer getBudgetPrevu() {
			return budgetPrevu;
		}

		public void setBudgetPrevu(Integer budgetPrevu) {
			this.budgetPrevu = budgetPrevu;
		}

		public Integer getRecupBons() {
			return recupBons;
		}

		public void setRecupBons(Integer recupBons) {
			this.recupBons = recupBons;
		}

		public Integer getReportMoisPrec() {
			return reportMoisPrec;
		}

		public void setReportMoisPrec(Integer reportMoisPrec) {
			this.reportMoisPrec = reportMoisPrec;
		}

		public Integer getReste() {
			return AutoBox.valueOf(getTotal())
					+ AutoBox.valueOf(getRecupBons())
					- AutoBox.valueOf(getBonsUOP());
		}

		public Integer getTotal() {
			return AutoBox.valueOf(getBudgetPrevu())
					+ AutoBox.valueOf(getReportMoisPrec())
					+ AutoBox.valueOf(getVirements());
		}

		public Integer getVirements() {
			return virements;
		}

		public void setVirements(Integer virements) {
			this.virements = virements;
		}

		public String toTable() {
			return getBudgetPrevu() + "\t" //
					+ getReportMoisPrec() + "\t" //
					+ getVirements() + "\t" //
					+ getTotal() + "\t" //
					+ getBonsUOP() + "\t" //
					+ getRecupBons() + "\t" //
					+ getReste();
		}

	}

	private Map<DataObject, Element> elements = new HashMap<DataObject, Element>();

	public List<DataObject> getKeys() {
		ArrayList<DataObject> keys = new ArrayList<DataObject>(elements.size());
		keys.addAll(elements.keySet());
		Collections.sort(keys, ToStringComparator.INSTANCE);
		return keys;
	}

	public Map<DataObject, Element> getElements() {
		return elements;
	}

}
