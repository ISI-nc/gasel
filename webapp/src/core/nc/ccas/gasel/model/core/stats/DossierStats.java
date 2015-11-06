package nc.ccas.gasel.model.core.stats;

import static nc.ccas.gasel.modelUtils.SqlUtils.dateToSql;
import static nc.ccas.gasel.stats.ColumnDefinition.DEFAULT_COUNTING;
import static nc.ccas.gasel.stats.ColumnDefinition.DEFAULT_GROUPING;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.enums.LienParente;
import nc.ccas.gasel.reports.PeriodeProps;
import nc.ccas.gasel.stats.ColumnDefinition;
import nc.ccas.gasel.stats.repr.BoolIntRepr;
import nc.ccas.gasel.stats.repr.RangeRepr;
import nc.ccas.gasel.stats.repr.StringRepr;
import nc.ccas.gasel.stats.tr.ObjPathTr;
import nc.ccas.gasel.stats.tr.SqlTr;

public class DossierStats {

	public static String criterePeriode(PeriodeProps periode) {
		return criterePeriode(periode.getPeriodeDebut(), periode
				.getPeriodeFin());
	}

	public static String criterePeriode(Date debut, Date fin) {
		String col = "$t.modif_date";
		return col + " BETWEEN " + dateToSql(debut) + " AND " + dateToSql(fin);
	}

	public static String critereResteDuMonde() {
		List<Class<?>> aspects = new LinkedList<Class<?>>(Arrays
				.asList(Dossier.ASPECTS));
		aspects.remove(AspectAides.class);
		return "NOT EXISTS " + Dossier.aspectsActifsSql("$t.id", aspects);
	}

	// --------------------------------------------------------------- Quartier

	public static final ColumnDefinition quartier() {
		return quartier(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition quartier(boolean grouping) {
		return quartier(grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition quartier(boolean grouping,
			boolean counting) {
		return quartier("Quartier", grouping, counting);
	}

	public static final ColumnDefinition quartier(String titre,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(
				titre, //
				new ObjPathTr("adresseHabitation",
						"adresse_quartier($t.id)"),
				StringRepr.INSTANCE, grouping, counting);
	}

	// ---------------------------------------------------- Situation familiale

	public static final ColumnDefinition situationFamiliale(boolean grouping) {
		return situationFamiliale(grouping, true);
	}

	public static final ColumnDefinition situationFamiliale(boolean grouping,
			boolean counting) {
		return situationFamiliale("Situation familiale", grouping, counting);
	}

	public static final ColumnDefinition situationFamiliale(String titre,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(titre, //
				new SqlTr("CASE WHEN (" //
						+ "$t.conjoint_id IS NULL OR "
						+ "(SELECT 1"
						+ "   FROM personne p"
						+ "  WHERE p.id=$t.conjoint_id"
						+ "    AND p.date_deces IS NOT NULL)>0"
						+ ") THEN 0 "
						+ "ELSE 1 END"), //
				new BoolIntRepr("Célib.", "Couple"), grouping, counting);
	}

	// ------------------------------------------------------- Enfants à charge

	public static final RangeRepr REPR_ENFANTS_A_CHARGE = new RangeRepr(0, 1,
			3, 5);

	public static final ColumnDefinition enfantsACharge() {
		return enfantsACharge(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition enfantsACharge(boolean grouping) {
		return enfantsACharge(grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition enfantsACharge(boolean grouping,
			boolean counting) {
		return enfantsACharge("Enfants à charge", grouping, counting);
	}

	public static final ColumnDefinition enfantsACharge(String titre,
			boolean grouping, boolean counting) {
		ObjPathTr tr = ObjPathTr.count("personnesACharge");
		tr.addQualifier("${personnesACharge}.lien_chef_famille_id IN ("
				+ LienParente.EACF_COUPLE + "," + LienParente.EACF_CF + ","
				+ LienParente.EACF_CONJOINT + "," + LienParente.EACF_ADOPTE
				+ "," + LienParente.EACF_CONFIE + ")");
		return new ColumnDefinition(titre, tr, REPR_ENFANTS_A_CHARGE, grouping,
				counting);
	}

	// --------------------------------------------------------- Problématiques

	public static final ColumnDefinition problematiques() {
		return problematiques(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition problematiques(boolean grouping) {
		return problematiques(grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition problematiques(boolean grouping,
			boolean counting) {
		return problematiques("Problématiques", grouping, counting);
	}

	public static final ColumnDefinition problematiques(String title,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(
				title, //
				new ObjPathTr("problematiques.parent.libelle"),
				StringRepr.INSTANCE, grouping, counting);
	}

	// ---------------------------------------------------------- Type de suivi

	public static final ColumnDefinition typeSuivi() {
		return typeSuivi(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition typeSuivi(boolean grouping) {
		return typeSuivi(grouping, grouping);
	}

	public static final ColumnDefinition typeSuivi(boolean grouping,
			boolean counting) {
		return typeSuivi("Type de suivi", grouping, counting);
	}

	public static final ColumnDefinition typeSuivi(String title,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(
				title, //
				TypeSuiviTr.INSTANCE, TypeSuiviAbbregeRepr.INSTANCE, grouping,
				counting);
	}

	// ----------------------------------------------- Situation professionelle

	public static final ColumnDefinition situationProf() {
		return situationProf(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition situationProf(boolean grouping) {
		return situationProf(grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition situationProf(boolean grouping,
			boolean counting) {
		return situationProf("Situation professionnelle", grouping, counting);
	}

	public static final ColumnDefinition situationProf(String title,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(
				title, //
				new ObjPathTr("chefFamille.situationProfessionelle.libelle"),
				StringRepr.INSTANCE, grouping, counting);
	}

	// ---------------------------------------------------------------- Revenus

	public static final RangeRepr REPR_REVENUS = new RangeRepr(0, 1, 50000,
			100000, 150000);

	public static final ColumnDefinition revenus(Date periodeDebut,
			Date periodeFin) {
		return revenus(periodeDebut, periodeFin, DEFAULT_GROUPING);
	}

	public static final ColumnDefinition revenus(Date periodeDebut,
			Date periodeFin, boolean grouping) {
		return revenus(periodeDebut, periodeFin, grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition revenus(Date periodeDebut,
			Date periodeFin, boolean grouping, boolean counting) {
		return revenus(periodeDebut, periodeFin, "Revenus", grouping, counting);
	}

	public static final ColumnDefinition revenus(Date periodeDebut,
			Date periodeFin, String title, boolean grouping, boolean counting) {
		String sql = String.format("dossier_revenus($t.id,%s,%s)",
				dateToSql(periodeDebut), dateToSql(periodeFin));
		return new ColumnDefinition(title, //
				new SqlTr(sql), REPR_REVENUS, grouping, counting);
	}

	// Via PeriodeProps

	public static final ColumnDefinition revenus(PeriodeProps pp) {
		return revenus(pp.getPeriodeDebut(), pp.getPeriodeFin());
	}

	public static final ColumnDefinition revenus(PeriodeProps pp,
			boolean grouping) {
		return revenus(pp.getPeriodeDebut(), pp.getPeriodeFin(), grouping);
	}

	public static final ColumnDefinition revenus(PeriodeProps pp,
			boolean grouping, boolean counting) {
		return revenus(pp.getPeriodeDebut(), pp.getPeriodeFin(), grouping,
				counting);
	}

	public static final ColumnDefinition revenus(PeriodeProps pp, String title,
			boolean grouping, boolean counting) {
		return revenus(pp.getPeriodeDebut(), pp.getPeriodeFin(), title,
				grouping, counting);
	}

	// ------------------------------------------------- Origine de signalement

	public static final ColumnDefinition origineSignalement() {
		return origineSignalement(DEFAULT_GROUPING);
	}

	public static final ColumnDefinition origineSignalement(boolean grouping) {
		return origineSignalement(grouping, DEFAULT_COUNTING);
	}

	public static final ColumnDefinition origineSignalement(boolean grouping,
			boolean counting) {
		return origineSignalement("Origine du signalement", grouping, counting);
	}

	public static final ColumnDefinition origineSignalement(String title,
			boolean grouping, boolean counting) {
		return new ColumnDefinition(
				title, //
				new ObjPathTr("signalement.libelle"), StringRepr.INSTANCE,
				grouping, counting);
	}

}
