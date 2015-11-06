package nc.ccas.gasel.pages.arretes;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static nc.ccas.gasel.utils.SerializationUtils.restoreId;
import static nc.ccas.gasel.utils.SerializationUtils.restoreList;
import static nc.ccas.gasel.utils.SerializationUtils.storeId;
import static nc.ccas.gasel.utils.SerializationUtils.storeList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import java_gaps.ComparisonChain;
import java_gaps.MinMax;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AjoutAidesLigne implements Serializable,
		Comparable<AjoutAidesLigne> {
	private static final long serialVersionUID = 126418608060830962L;

	public static final Comparator<AjoutAidesLigne> COMP_MONTANT = new Comparator<AjoutAidesLigne>() {
		public int compare(AjoutAidesLigne o1, AjoutAidesLigne o2) {
			return new ComparisonChain() //
					.append(o1.getMontant(), o2.getMontant()) //
					.append(o1, o2) //
					.result();
		}
	};

	public static List<AjoutAidesLigne> traiteUsages(List<UsageBon> usages) {
		List<Bon> bons = new ArrayList<Bon>(usages.size());
		for (UsageBon usage : usages) {
			bons.add(usage.getBon());
		}
		return traite(bons);
	}

	/**
	 * Prefetches conseillés : aide.dossier.chefFamille, usage.
	 */
	public static List<AjoutAidesLigne> traite(List<Bon> bons) {
		Map<Date, Map<Aide, AjoutAidesLigne>> map = new TreeMap<Date, Map<Aide, AjoutAidesLigne>>();
		int count = 0;

		for (Bon bon : bons) {
			// Récupération de la map pour le mois du bon
			Date mois = DateUtils.debutMois(bon.getDebut());

			Map<Aide, AjoutAidesLigne> mapMois = map.get(mois);
			if (mapMois == null) {
				mapMois = new HashMap<Aide, AjoutAidesLigne>();
				map.put(mois, mapMois);
			}

			// Aggrégation par aide
			Aide aide = bon.getAide();
			if (!mapMois.containsKey(aide)) {
				mapMois.put(aide, new AjoutAidesLigne(aide));
				count++;
			}
			AjoutAidesLigne l = mapMois.get(aide);
			l.addBon(bon);
		}

		List<AjoutAidesLigne> retval = new ArrayList<AjoutAidesLigne>(count);
		for (Map<Aide, AjoutAidesLigne> mapMois : map.values()) {
			retval.addAll(mapMois.values());
		}
		Collections.sort(retval);
		return retval;
	}

	private Aide aide;

	private Set<Bon> bons = new HashSet<Bon>();

	public AjoutAidesLigne(Aide aide) {
		this.aide = aide;
	}

	public void addBon(Bon bon) {
		bons.add(bon);
	}

	public int getNombre() {
		return bons.size();
	}

	public int getMontant() {
		int sum = 0;
		for (Bon bon : bons) {
			if (bon.getUsage() == null)
				continue;
			sum = sum + bon.getUsage().getMontantUtilise();
		}
		return sum;
	}

	public Aide getAide() {
		return aide;
	}

	public Set<Bon> getBons() {
		return bons;
	}

	public Date getDebut() {
		MinMax<Date> min = new MinMax<Date>();
		for (Bon bon : bons) {
			min.inject(bon.getDebut());
		}
		return min.min();
	}

	public Date getFin() {
		MinMax<Date> max = new MinMax<Date>();
		for (Bon bon : bons) {
			max.inject(bon.getFin());
		}
		return max.max();
	}

	public String getPeriode() {
		GregorianCalendar debut = gc(getDebut());
		GregorianCalendar fin = gc(getFin());

		if (debut == null || fin == null)
			return "?";

		int moisDebut = debut.get(MONTH) + 1;
		int moisFin = fin.get(MONTH) + 1;

		if (debut.get(YEAR) == fin.get(YEAR)) {
			if (moisDebut == moisFin) {
				return moisDebut + "/" + debut.get(YEAR);
			} else {
				return moisDebut + "-" + moisFin + "/" + debut.get(YEAR);
			}
		} else {
			return moisDebut + "/" + debut.get(YEAR) + "-" + //
					moisFin + "/" + fin.get(YEAR);
		}
	}

	private GregorianCalendar gc(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc;
	}

	public int compareTo(AjoutAidesLigne o) {
		Personne chefFamille = aide.getDossier().getDossier().getChefFamille();
		Personne chefFamille2 = o.aide.getDossier().getDossier()
				.getChefFamille();

		return new ComparisonChain() //
				.append(chefFamille, chefFamille2) //
				.append(getDebut(), o.getDebut()) //
				.result();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AjoutAidesLigne))
			return false;
		AjoutAidesLigne o = (AjoutAidesLigne) obj;
		return new EqualsBuilder() //
				.append(idAide(), o.idAide()) //
				.append(getDebut(), o.getDebut()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder() //
				.append(idAide()) //
				.append(getDebut()) //
				.toHashCode();
	}

	private int idAide() {
		return aide.getId();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		storeId(out, aide);
		storeList(out, bons);
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		aide = restoreId(in, Aide.class);
		bons = new HashSet<Bon>(restoreList(in, Bon.class));
	}

}
