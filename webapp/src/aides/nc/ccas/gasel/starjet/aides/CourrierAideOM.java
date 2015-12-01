package nc.ccas.gasel.starjet.aides;

import java.io.IOException;
import java.io.PrintWriter;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideOM;
import nc.ccas.gasel.starjet.StarjetWriter;

public class CourrierAideOM extends CourrierAide {

	private final TypeCourrierAide type;

	public CourrierAideOM(TypeCourrierAide type) {
		this.type = type;
	}

	@Override
	public String getStarpageFile() {
		if (TypeCourrierAide.COURRIER_ADMINISTRE == type)
			return "courrieradmin_om";
		if (TypeCourrierAide.FAX_FOURNISSEUR == type)
			return "fax_CaledonienneDesEaux";
		throw new IllegalArgumentException(type.toString());
	}

	@Override
	protected void writeImpl(PrintWriter out, Aide aide) throws IOException {
		StarjetWriter w = writer(out);

		// Infos courrier
		dateCourrier(w);

		// Infos aide
		AideOM om = aide.getOrduresMenageres();
		nom(aide, w);
		w.printf("Periode: %s", om.getPeriodePrestation()).ln();
		montant(aide, w);
		w.printf("N° redevable: %s", om.getNumeroRedevable()).ln();
		w.printf("N° facture: %s", om.getNumeroFacture()).ln();
		adresse(aide.getDossier().getDossier(), w);
	}

	@Override
	protected String translateDesignation(String designation) {
		return CourrierUtils.designationLongue(designation);
	}

}
