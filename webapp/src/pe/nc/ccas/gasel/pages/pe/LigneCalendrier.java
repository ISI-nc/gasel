package nc.ccas.gasel.pages.pe;

import java.io.Serializable;

public class LigneCalendrier implements Serializable {
	private static final long serialVersionUID = 7681030013838806019L;

	private final String titre;

	private final boolean highlight;

	private Integer lundi;

	private Integer mardi;

	private Integer mercredi;

	private Integer jeudi;

	private Integer vendredi;

	private Integer samedi;

	private Integer dimanche;

	public LigneCalendrier(boolean highlight, String titre) {
		this.highlight = highlight;
		this.titre = titre;
	}

	public Integer getDimanche() {
		return dimanche;
	}

	public void setDimanche(Integer dimanche) {
		this.dimanche = dimanche;
	}

	public Integer getJeudi() {
		return jeudi;
	}

	public void setJeudi(Integer jeudi) {
		this.jeudi = jeudi;
	}

	public Integer getLundi() {
		return lundi;
	}

	public void setLundi(Integer lundi) {
		this.lundi = lundi;
	}

	public Integer getMardi() {
		return mardi;
	}

	public void setMardi(Integer mardi) {
		this.mardi = mardi;
	}

	public Integer getMercredi() {
		return mercredi;
	}

	public void setMercredi(Integer mercredi) {
		this.mercredi = mercredi;
	}

	public Integer getSamedi() {
		return samedi;
	}

	public void setSamedi(Integer samedi) {
		this.samedi = samedi;
	}

	public String getTitre() {
		return titre;
	}

	public Integer getVendredi() {
		return vendredi;
	}

	public void setVendredi(Integer vendredi) {
		this.vendredi = vendredi;
	}

	public boolean isHighlight() {
		return highlight;
	}

}
