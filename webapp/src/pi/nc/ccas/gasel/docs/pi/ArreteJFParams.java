package nc.ccas.gasel.docs.pi;

import static nc.ccas.gasel.Formats.DATE_FORMAT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import java_gaps.DateIncrement;
import java_gaps.Interval;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.French;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.model.pi.AttributionJF;
import nc.ccas.gasel.model.pi.Collectivite;
import nc.ccas.gasel.model.pi.ControleEntretien;
import nc.ccas.gasel.model.pi.DemandeJF;
import nc.ccas.gasel.services.doc.ParamsProvider;
import nc.ccas.gasel.utils.QuickTreeMap;

public class ArreteJFParams extends ParamsProvider<ArreteJF> {

	private static final DateFormat FORMAT_MOIS = new SimpleDateFormat("MMMMM",
			Locale.FRENCH);

	private static final DateFormat FORMAT_MOIS_ANNEE = new SimpleDateFormat(
			"MMMMM yyyy", Locale.FRENCH);

	private static final DateFormat FORMAT_JOUR_MOIS_ANNEE = new SimpleDateFormat(
			"d MMMMM yyyy", Locale.FRENCH) {
		private static final long serialVersionUID = 5293123004641467092L;

		public StringBuffer format(Date date, StringBuffer toAppendTo,
				java.text.FieldPosition pos) {
			StringBuffer buf = new StringBuffer();
			super.format(date, buf, pos);
			return new StringBuffer(buf.toString().replaceFirst("^1 ", "1er ")
					.toLowerCase(Locale.FRENCH));
		}
	};

	public static final Set<String> PROVIDED = new TreeSet<String>();
	static {
		PROVIDED.add("arrete");
		PROVIDED.add("designation");
		PROVIDED.add("nom");
		PROVIDED.add("jardin");
		PROVIDED.add("parcelle");
		PROVIDED.add("date courrier");
		PROVIDED.add("date dernier controle");
		PROVIDED.add("mois dus");
		PROVIDED.add("somme due");
		PROVIDED.add("numero demande");
		PROVIDED.add("date commission");
		PROVIDED.add("date enregistrement");
		PROVIDED.add("date debut");
		PROVIDED.add("date fin");
		PROVIDED.add("adresse");
	}

	public ArreteJFParams() {
		super(ArreteJF.class);
	}

	@Override
	protected Set<String> getProvidedParams() {
		return PROVIDED;
	}

	@Override
	protected Map<String, String> toParamsImpl(ArreteJF source) {
		QuickTreeMap<String, String> map = new QuickTreeMap<String, String>();

		AttributionJF attribution = source.getAttribution();
		DemandeJF demande = attribution.getDemande();

		if (demande.getCollectivite() != null) {
			Collectivite collectivite = demande.getCollectivite();
			map.put("nom", collectivite.getDesignation());
			map.put("adresse", collectivite.getAdresse().toString());

			map.put("designation", "Collectivité");
		} else {
			Dossier dossier = demande.getDossier().getDossier();
			Personne chefFamille = dossier.getChefFamille();
			map.put("nom", chefFamille.getNom() + " " + chefFamille.getPrenom());
			map.put("adresse", dossier.getAdresseHabitation().toString()
					.replace(" -- ", ", "));

			map.put("designation", chefFamille.getDesignationLongue());
		}

		Date dateCourrier;
		if (demande.getCourrier() == null) {
			dateCourrier = demande.getDate();
		} else {
			dateCourrier = demande.getCourrier().getArrivee();
		}
		map.put("date courrier", DATE_FORMAT.format(dateCourrier));

		String dateDernierControle;
		ControleEntretien dernierControle = attribution.getDernierControle();
		if (dernierControle == null) {
			dateDernierControle = "?";
		} else {
			dateDernierControle = DATE_FORMAT.format(dernierControle.getDate());
		}
		map.put("date dernier controle", dateDernierControle);

		Interval<Date> inter = source.getPeriodeNonPayee();

		map.put("mois dus", moisDus(inter).toLowerCase(Locale.FRENCH));
		map.put("somme due", sommeDue(inter));

		map.put("arrete", valueOf(source.getNumero()));
		map.put("jardin", valueOf( //
				attribution.getParcelle().getJardin().getNom()));
		map.put("parcelle", valueOf(attribution.getParcelle().getNumero()));
		map.put("numero demande", valueOf(demande.getNumEnregistrement()));
		map.put("date commission", dateToString(demande.getCommission()));
		map.put("date enregistrement", dateToString(demande.getDate()));
		map.put("date debut", dateToString(source.getDebut()));
		map.put("date fin", dateToString(source.getFin()));

		return map.map();
	}

	private String sommeDue(Interval<Date> inter) {
		int sommeDue = 0;
		if (inter != null) {
			for (@SuppressWarnings("unused")
			Date date : inter.iterate(new DateIncrement(0, 1, 0))) {
				sommeDue += 1000;
			}
		}
		return Formats.MONTANT.format(sommeDue);
	}

	private static String moisDus(Interval<Date> inter) {
		if (inter == null)
			return "";

		int startMois;
		int startAnnee;
		int endMois;
		int endAnnee;

		{
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(inter.start);
			startAnnee = cal.get(Calendar.YEAR);
			startMois = cal.get(Calendar.MONTH);
			cal.setTime(inter.end);
			endAnnee = cal.get(Calendar.YEAR);
			endMois = cal.get(Calendar.MONTH);
		}

		if (startAnnee == endAnnee && startMois == endMois) {
			String mois = FORMAT_MOIS_ANNEE.format(inter.start);
			return "du mois " + French.particuleDe(mois) + mois;
		} else {
			String debut;
			if (startAnnee == endAnnee) {
				debut = FORMAT_MOIS.format(inter.start);
			} else {
				debut = FORMAT_MOIS_ANNEE.format(inter.start);
			}
			return "des mois " + French.particuleDe(debut) + debut + " à "
					+ FORMAT_MOIS_ANNEE.format(inter.end);
		}
	}

	private String dateToString(Date date) {
		if (date == null) {
			return "?";
		}
		return FORMAT_JOUR_MOIS_ANNEE.format(date);
	}

	private String valueOf(Object object) {
		if (object == null)
			return "?";
		return object.toString();
	}

}
