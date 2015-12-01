package nc.ccas.gasel.starjet.aides;

import java.util.Date;

import nc.ccas.gasel.Formats;
import nc.ccas.gasel.cayenne.Error;
import nc.ccas.gasel.fop.PageFo;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.services.fop.FoBlock;
import nc.ccas.gasel.services.fop.FoBlockContainer;
import nc.ccas.gasel.services.fop.FoFlow;
import nc.ccas.gasel.services.fop.FoPage;
import nc.ccas.gasel.services.fop.FoPageSequence;
import nc.ccas.gasel.services.fop.FoTable;
import nc.ccas.gasel.services.fop.FoTableBody;
import nc.ccas.gasel.services.fop.FoTableRow;
import nc.ccas.gasel.services.fop.XslFoOutput;

import org.apache.tapestry.IMarkupWriter;

public class CourrierAideGaz extends XslFoOutput<Aide> {

	private static final Object[] STYLE_PARAGRAPHE = new Object[] {
			"margin-top", "3mm", "margin-bottom", "2mm", "font-weight", "bold",
			"text-align", "justify" };

	private static final Object[] BOLD = new Object[] { "font-weight", "bold" };

	public CourrierAideGaz() {
		super(Aide.class);
	}

	@Override
	protected void writeXslFoImpl(Aide aide, IMarkupWriter writer) {
		if (aide.getBons().isEmpty())
			throw new Error("Pas de bons !");

		// Premier bon (plus petit numéro)
		Bon bon = null;
		for (Bon bon2 : aide.getBons()) {
			if (bon != null && bon2.getNumero().compareTo(bon.getNumero()) <= 0)
				continue;

			bon = bon2;
		}

		// Rendu
		FoPage document = new PageFo(writer).begin();
		FoPageSequence sequence;
		FoFlow flow;
		FoBlock block;

		sequence = document.sequence();
		flow = sequence.body();

		// Tableau en-tête
		FoTable table = new FoTable(writer);
		table.begin();
		// 4 colonnes
		table.column().column().column().column();

		FoTableBody tableBody = table.body();
		// tableBody.rowAttributes(??)
		// tableBody.cellAttributes(??)
		FoTableRow row;

		// Ligne des exp/destinataire
		row = tableBody.row();
		row.cell(BOLD).print("Expéditeur :").end();
		row.cell().print("Centre Communal d'Action Sociale").end();
		row.cell(BOLD).print("Destinataire :").end();
		row.cell().print("Société SODIGAZ").end();

		row = tableBody.row();
		row.cell().end();
		row.cell().end();
		row.cell().end();
		row.cell(BOLD).print("A l'attention de Rose");

		// Ligne des faxes
		row.cell(BOLD).print("FAX :").end();
		row.cell().print("27.07.72").end();
		row.cell(BOLD).print("FAX :").end();
		row.cell().print("27.62.97").end();

		table.end();

		flow.block(BOLD).print("Nombre de pages : 1").end();
		flow.block().print("Date :").print(
				Formats.DATE_FORMAT.format(new Date())).end();
		flow.block().print("Objet : Livraison de gaz.").end();

		FoBlockContainer container = flow.blockContainer("margin-top", "1em");
		container.block().print("Madame,").end();
		container.block().print(
				"Je vous prie de bien vouloir livrer une bouteille de gaz à :")
				.end();

		Personne chefFamille = aide.getDossier().getDossier().getChefFamille();
		container.block("font-weight", "bold", "text-align", "center").print(
				chefFamille.getDesignationLongue() + " "
						+ chefFamille.getPrenom() + " "
						+ chefFamille.getNomCommun()).end();

		container.block().print("à l'adresse suivante :").end();

		block = flow.block(STYLE_PARAGRAPHE);
		block.begin("font-weight", "bold").print("Expéditeur :").end();
		block.begin("font-weight", "bold").print("Destinataire :").end();
		block.print("Société SODIGAZ");
		block.end();

	}

}
