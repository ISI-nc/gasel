package nc.ccas.gasel.starjet.aides;

import static nc.ccas.gasel.French.nombreToutesLettres;
import static nc.ccas.gasel.modelUtils.CommonQueries.unique;
import static nc.ccas.gasel.modelUtils.DateUtils.debutMois;
import static nc.ccas.gasel.pages.arretes.AjoutAidesLigne.traiteUsages;
import static org.apache.cayenne.exp.Expression.fromString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.Formats;
import nc.ccas.gasel.fop.FoTableau;
import nc.ccas.gasel.fop.PageFo;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.TypeArrete;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.pages.arretes.AjoutAidesLigne;
import nc.ccas.gasel.services.fop.FoBlock;
import nc.ccas.gasel.services.fop.FoBlockContainer;
import nc.ccas.gasel.services.fop.FoFlow;
import nc.ccas.gasel.services.fop.FoPage;
import nc.ccas.gasel.services.fop.FoPageSequence;
import nc.ccas.gasel.services.fop.FoStaticContent;
import nc.ccas.gasel.services.fop.FoTableRow;
import nc.ccas.gasel.services.fop.XslFoOutput;
import nc.ccas.gasel.utils.QuickHashMap;

import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.IMarkupWriter;

public class ArreteFo extends XslFoOutput<Arrete> {

	private static final int LIGNES_ENTRE_SOUS_TOTAUX = 46;

	private static final Object[] STYLE_PARAGRAPHE = new Object[] {
			"margin-top", "3mm", "margin-bottom", "2mm", "text-align",
			"justify" };

	private static final Object[] BOLD = new Object[] { "font-weight", "bold" };

	private static final Map<Integer, String[]> TITRES = new QuickHashMap<Integer, String[]>() //
			.put(
					TypeArrete.AVANCE,
					new String[] {
							"Relatif à l'octroi d'aides sociales",
							"par le Centre Communal d'Action Sociale de la Ville de Nouméa",
							"à ses bénéficiaires" //
					}) //

			.ref(TypeArrete.SOLIDARITE, TypeArrete.AVANCE) //

			.put(
					TypeArrete.EAU,
					new String[] {
							"Relatif à la prise en charge par le budget du",
							"Centre Communal d'Action Sociale de la Ville de Nouméa",
							"des factures d'eau et d'assainissement",
							"en faveur des bénéficiaires du C.C.A.S." //
					}) //

			.put(
					TypeArrete.OM,
					new String[] {
							"Relatif à la prise en charge par le budget du",
							"Centre Communal d'Action Sociale de la Ville de Nouméa",
							"des factures d'ordures ménagères",
							"en faveur des bénéficiaires du C.C.A.S." //
					}) //

			.map();

	private static final String[] PREMIERE_PAGE = new String[] { //
			"La Présidente du Centre Communal d'Action Sociale,",

			"VU la loi organique modifiée n°99-209 du 19 mars 1999 relative à la"
					+ " Nouvelle-Calédonie, publiée au journal officiel de la"
					+ " Nouvelle-Calédonie le 24 mars 1999,",

			"VU la loi modifiée n°99-210 du 19 mars 1999 relative à la"
					+ " Nouvelle-Calédonie, publiée au journal officiel de la"
					+ " Nouvelle-Calédonie le 24 mars 1999,",

			"VU la délibération du Conseil Municipal de la Ville de Nouméa n°91/160"
					+ " du 9 octobre 1991 portant création du Centre"
					+ " Communal d'Action Sociale de la Ville de Nouméa,",

			"VU la délibération du Conseil Municipal de la Ville de Nouméa n°2011/696"
					+ " du 22 juin 2011 modifiant la délibération du Conseil"
					+ " Municipal de la Ville de Nouméa n°91/160 du 9 octobre 1991"
					+ " portant création du Centre Communal d'Action Sociale de la"
					+ " Ville de Nouméa",

			"VU la délibération du Conseil d'Administration du Centre Communal "
					+ "d'Action Sociale de la Ville de Nouméa n°2008/35 relative "
					+ "à la révision des conditions d'octroi des aides sociales du "
					+ "Centre Communal d'Action Sociale de la Ville de Nouméa," //
	};

	private static final Map<Integer, String> TEXTES_ARTICLE_1 = new QuickHashMap<Integer, String>()
			.put(
					TypeArrete.AVANCE,
					"Sont accordées aux personnes dont les noms suivent,"
							+ " les aides sociales suivantes :") //

			.ref(TypeArrete.SOLIDARITE, TypeArrete.AVANCE) //

			.put(
					TypeArrete.EAU,
					"Est accordée aux personnes dont les noms suivent une aide"
							+ " relative à la prise en charge de leur facture"
							+ " d'eau et d'assainissement :") //

			.put(
					TypeArrete.OM,
					"Est accordée aux personnes dont les noms suivent une aide"
							+ " relative à la prise en charge de leur facture"
							+ " d'enlèvement des ordures ménagères :") //

			.map();

	public ArreteFo() {
		super(Arrete.class);
	}

	@Override
	protected void writeXslFoImpl(Arrete source, IMarkupWriter writer) {
		source = doPrefetch(source);

		FoPage document = new PageFo(writer).begin();
		FoPageSequence sequence;
		FoFlow flow;
		FoBlock block;

		// --------------------------------------------------------------
		// Première page
		//
		sequence = document.sequence();
		header(source, sequence);
		footer(sequence);

		flow = sequence.body();

		// LL (?)
		FoBlockContainer container = flow.blockContainer("margin-top", "4.5cm",
				"margin-bottom", "9mm", "height", "5cm", "font-weight", "bold");
		//

		container.blockAttributes(STYLE_PARAGRAPHE);
		container.textBlock("LL");

		// Titre
		container.blockAttributes(STYLE_PARAGRAPHE);

		container.block("text-align", "center", //
				"margin-top", "0.5cm", //
				"margin-bottom", "0.2cm") //
				.print("Arrêté n°".toUpperCase()) //
				.print(source.getNumero()) //
				.end();

		container.blockAttributes( //
				"font-weight", "bold", "text-align", "center");
		for (String ligne : TITRES.get(source.getType().getId())) {
			container.textBlock(ligne.toUpperCase());
		}

		container.end();

		// Paragraphes de l'arrêté
		flow.blockAttributes(STYLE_PARAGRAPHE);

		for (String paragraphe : PREMIERE_PAGE)
			flow.block().print(paragraphe).end();

		flow.block().print(source.getChamp()).end();

		flow.block("text-align", "center", //
				"margin-top", "1cm", //
				"margin-bottom", "0.5cm", //
				"font-weight", "bold") //
				.print("Arrêté".toUpperCase()) //
				.end();

		flow.block(BOLD).print("ARTICLE 1ER".toUpperCase()).end();
		flow.block().print(TEXTES_ARTICLE_1.get(source.getType().getId()))
				.end();

		flow.end();
		sequence.end();

		// --------------------------------------------------------------
		// Tableau des aides
		//

		sequence = document.sequence();
		header(source, sequence);
		footer(sequence);

		flow = sequence.body("font-size", "10pt"); // , "font-weight", "bold");

		FoTableau tableau = new FoTableau(writer);
		tableau.setAllowMonospace(false);

		TableauHelper<Arrete> helper = helper(source);
		helper.header(tableau);

		Object[] emptyRow = new Object[tableau.columnCount()];
		for (int i = 0; i < emptyRow.length; i++)
			emptyRow[i] = "";

		int ligne = 0;
		double total = 0d;
		double sousTotal = 0d;
		for (RowMontant row : helper.generateRows(source)) {
			total += row.getMontant();
			sousTotal += row.getMontant();
			tableau.row(row.getValues());

			ligne++;
			if (ligne % LIGNES_ENTRE_SOUS_TOTAUX == 0) {
				rowTotal(tableau, "Sous-total", sousTotal);
				sousTotal = 0d;
			}
		}
		// Remplissage de la page, sous-total final
		while (ligne % LIGNES_ENTRE_SOUS_TOTAUX != 0) {
			ligne++;
			if (ligne % LIGNES_ENTRE_SOUS_TOTAUX == 0) {
				rowTotal(tableau, "Sous-total", sousTotal);
			} else {
				tableau.row(emptyRow);
			}
		}

		// --------------------------------------------------------------
		// Ligne du total
		rowTotal(tableau, "Total", total);

		tableau.end();

		flow.end();
		sequence.end();

		// --------------------------------------------------------------
		// Dernière page
		//
		sequence = document.sequence();
		header(source, sequence);
		footer(sequence);

		flow = sequence.body();
		flow.blockAttributes(STYLE_PARAGRAPHE);

		// --------------------------------------------------------------
		flow.block(BOLD).print("ARTICLE 2").end();

		block = flow.block()//
				.print("La dépense représentant un montant global de ");
		;

		block.inline(BOLD).print(
				nombreToutesLettres((long) total).toUpperCase())//
				.print(" FRANCS CFP (") //
				.print(Formats.MONTANT.format(total)) //
				.print(" F/CFP)")//
				.end();

		block.print(
				" est imputable au Budget du Centre Communal "
						+ "d\'Action Sociale de la Ville de Nouméa :")//
				.end();

		block = flow.block("margin-left", "4cm", "margin-top", "5mm");

		block.block().print(
				"- Fonction 5 : Interventions sociales et de santé ;").end();
		block.block().print(
				"- Sous fonction 523 : actions en faveur des"
						+ " personnes en difficulté ;").end();
		block.block().print(
				"- Chapitre 65 : Autres charges de gestion courante.").end();

		block.end();

		// --------------------------------------------------------------
		flow.block("margin-top", "5mm", "font-weight", "bold") //
				.print("ARTICLE 3").end();

		flow.block("margin-top", "5mm").print(
				"Le présent arrêté sera enregistré et notifié aux intéressés.")
				.end();

		// --------------------------------------------------------------
		block = flow.block("margin-top", "3cm", "margin-left", "12cm");
		block.blockAttributes(STYLE_PARAGRAPHE);
		block //
				.textBlock("Nouméa, le") //
				.textBlock("LA PRÉSIDENTE", "margin-top", "1cm");
		block.end();

		flow.end();
		sequence.end();

		// End
		document.end();
	}

	private void rowTotal(FoTableau tableau, String label, double sousTotal) {
		FoTableRow r = tableau.manualRow("border", "1pt solid black",
				"font-weight", "bold");
		r.textCell(label + " :", "number-columns-spanned", tableau
				.columnCount() - 2, "text-align", "right");
		tableau.autoCell(1, sousTotal, "number-columns-spanned", 2);
		r.end();
	}

	private void header(Arrete source, FoPageSequence sequence) {
		FoStaticContent header = sequence.header();
		header.child("block") //
				// .print("Arrêté n°") //
				// .print(source.getNumero()) //
				.end();
		header.end();
	}

	private void footer(FoPageSequence sequence) {
		FoStaticContent footer = sequence.footer();
		footer.block()
		// .print("Page ").emptyChild("page-number")
				.end();
		footer.end();
	}

	private TableauHelper<Arrete> helper(Arrete source) {
		TypeArrete type = source.getType();

		if (type.isAvance()) {
			return AVANCE_HELPER;
		} else if (type.isEau()) {
			return EAU_HELPER;
		} else if (type.isOM()) {
			return OM_HELPER;
		} else if (type.isSolidarite()) {
			return SOLIDARITE_HELPER;
		}

		throw new IllegalArgumentException("Type d'arrêté non géré : " + type);
	}

	// ------------------------------------------------

	private static final TableauHelper<Arrete> EAU_HELPER = new TableauHelper<Arrete>() {
		public void header(FoTableau tableau) {
			tableau.column("Nom, prénom", "column-width", "30em");
			tableau.column("Police");
			tableau.column("Période");
			tableau.column("Montant", "column-width", "5em");
		}

		public List<RowMontant> generateRows(Arrete source) {
			List<AjoutAidesLigne> lignes = traiteUsages(source.getBonsValides());
			List<RowMontant> rows = new ArrayList<RowMontant>(lignes.size());
			for (AjoutAidesLigne ligne : lignes) {
				Aide aide = ligne.getAide();
				AideEau eau = aide.getEau();
				Personne cf = aide.getDossier().getDossier().getChefFamille();

				// Montant = prise en charge conso
				int montant = eau.getPriseEnChargeConso();

				rows.add(new RowMontant(montant, //
						cf.getNom() + " " + cf.getPrenom(), //
						eau.getPolice(), //
						eau.getPeriodePrestation(), //
						montant));

				// Le reste correspond à la remise compteur
				if (eau.getPriseEnChargeConso() < ligne.getMontant()) {
					montant = ligne.getMontant() - eau.getPriseEnChargeConso();
					rows.add(new RowMontant(montant, //
							cf.getNom() + " " + cf.getPrenom(), //
							eau.getPolice(), //
							"Remise compteur", //
							montant));
				}
			}
			return rows;
		}
	};

	private static final TableauHelper<Arrete> OM_HELPER = new TableauHelper<Arrete>() {
		public void header(FoTableau tableau) {
			tableau.column("Nom, prénom", "column-width", "24em");
			tableau.column("Redevable", "column-width", "6em");
			tableau.column("Article");
			tableau.column("Période");
			tableau.column("Montant", "column-width", "5em");
		}

		public List<RowMontant> generateRows(Arrete source) {
			List<AjoutAidesLigne> lignes = traiteUsages(source.getBonsValides());
			List<RowMontant> rows = new ArrayList<RowMontant>(lignes.size());
			for (AjoutAidesLigne ligne : lignes) {
				Aide aide = ligne.getAide();
				Personne cf = aide.getDossier().getDossier().getChefFamille();
				int montant = ligne.getMontant();
				rows.add(new RowMontant(montant, //
						cf.getNom() + " " + cf.getPrenom(), //
						aide.getOrduresMenageres().getNumeroRedevable(), //
						aide.getOrduresMenageres().getNumeroFacture(), //
						aide.getOrduresMenageres().getPeriodePrestation(), //
						montant));
			}
			return rows;
		}
	};

	private static final TableauHelper<Arrete> SOLIDARITE_HELPER = new TableauHelper<Arrete>() {
		public void header(FoTableau tableau) {
			tableau.column("Nom, prénom");
			tableau.column("Type d'aide");
			tableau.column("Période", Formats.MOIS_ANNEE, //
					"column-width", "5em");
			tableau.column("Montant", "column-width", "5em");
		}

		public List<RowMontant> generateRows(Arrete source) {
			List<AjoutAidesLigne> lignes = traiteUsages(source.getBonsValides());
			List<RowMontant> rows = new ArrayList<RowMontant>(lignes.size());
			for (AjoutAidesLigne ligne : lignes) {
				Aide aide = ligne.getAide();
				Personne cf = aide.getDossier().getDossier().getChefFamille();

				String nom = cf.getNom() + " " + cf.getPrenom();
				String typeAide = aide.getNature().getLibelle();
				Date periode = debutMois(ligne.getDebut());
				int montant = ligne.getMontant();

				rows.add(new RowMontant(montant, nom, typeAide, periode,
						montant));
			}
			return rows;
		}
	};

	private static final TableauHelper<Arrete> AVANCE_HELPER = SOLIDARITE_HELPER;

	private Arrete doPrefetch(Arrete arrete) {
		SelectQuery query = new SelectQuery(Arrete.class);

		query.setQualifier(fromString("db:id = "
				+ arrete.getObjectId().getIdSnapshot().get("id")));
		query.addPrefetch("bonsValides");
		query.addPrefetch("bonsValides.bon");
		query.addPrefetch("bonsValides.bon.aide");
		query.addPrefetch("bonsValides.bon.aide.nature");
		query.addPrefetch("bonsValides.bon.aide.dossier");
		query.addPrefetch("bonsValides.bon.aide.dossier.dossier");
		query.addPrefetch("bonsValides.bon.aide.dossier.dossier.chefFamille");

		if (arrete.getType().isEau())
			query.addPrefetch("bonsValides.bon.aide.eau");
		if (arrete.getType().isOM())
			query.addPrefetch("bonsValides.bon.aide.orduresMenageres");

		return (Arrete) unique(arrete.getObjectContext(), query);
	}

}
