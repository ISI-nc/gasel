package nc.ccas.gasel.pages.dossiers;

import java.io.Serializable;

public class CalculetteData implements Serializable {
	private static final long serialVersionUID = 67546530247485233L;

	private double depassementM3;

	private double montantDejaPaye;

	private String periodePrestation;

	private double priseEnChargeTotale;

	private double restantDu;

	public String getPeriodePrestation() {
		return periodePrestation;
	}

	public void setPeriodePrestation(String periodePrestation) {
		this.periodePrestation = periodePrestation;
	}

	public double getPriseEnChargeTotale() {
		return priseEnChargeTotale;
	}

	public void setPriseEnChargeTotale(double priseEnChargeTotale) {
		this.priseEnChargeTotale = priseEnChargeTotale;
	}

	public double getRestantDu() {
		return restantDu;
	}

	public void setRestantDu(double restantDu) {
		this.restantDu = restantDu;
	}

	public double getDepassementM3() {
		return depassementM3;
	}

	public void setDepassementM3(double depassementM3) {
		this.depassementM3 = depassementM3;
	}

	public double getMontantDejaPaye() {
		return montantDejaPaye;
	}

	public void setMontantDejaPaye(double montantDejaPaye) {
		this.montantDejaPaye = montantDejaPaye;
	}

}
