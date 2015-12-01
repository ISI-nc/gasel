package nc.ccas.gasel.docs.aides;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.core.Adresse;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.services.doc.ParamsProvider;
import nc.ccas.gasel.starjet.aides.CourrierUtils;
import nc.ccas.gasel.utils.QuickMap;
import nc.ccas.gasel.utils.QuickTreeMap;

public class AideParams extends ParamsProvider<Aide> {

	public static final Set<String> PROVIDED = new TreeSet<String>();
	static {
		PROVIDED.add("designation");
		PROVIDED.add("nom");
		PROVIDED.add("adresse 1");
		PROVIDED.add("adresse 2");
		PROVIDED.add("cp");
		PROVIDED.add("ville");
		PROVIDED.add("date courrier");
		PROVIDED.add("montant");
	}

	public AideParams() {
		super(Aide.class);
	}

	@Override
	protected Set<String> getProvidedParams() {
		return PROVIDED;
	}

	@Override
	protected Map<String, String> toParamsImpl(Aide aide) {
		QuickMap<String, String> map = new QuickTreeMap<String, String>();

		Dossier dossier = aide.getDossier().getDossier();
		Personne chefFamille = dossier.getChefFamille();
		Adresse adresse = adresseAUtiliser(dossier);

		map.put("designation", chefFamille.getDesignationLongue());
		map.put("nom", chefFamille.getPrenom() + " " + chefFamille.getNom());
		map.put("adresse 1", adresse1(adresse));
		map.put("adresse 2", adresse2(adresse));
		map.put("cp", codePostal(adresse));
		map.put("ville", ville(adresse));
		map.put("date courrier", CourrierUtils.dateCourrier());
		map.put("montant", CourrierUtils.montant(aide.getMontant()));

		return map.map();
	}

	private Adresse adresseAUtiliser(Dossier dossier) {
		Adresse adresse;
		if (exploitable(dossier.getAdressePostale())) {
			adresse = dossier.getAdressePostale();
		} else {
			adresse = dossier.getAdresseHabitation();
		}
		return adresse;
	}

	private String adresse1(Adresse adresse) {
		String retval;
		if (adresse.getBoitePostale() != null) {
			retval = "BP" + adresse.getBoitePostale();
		} else {
			retval = ifnull(adresse.getNumero(), "") + " "
					+ ifnull(adresse.getRue(), "").trim();
		}
		return retval.toUpperCase();
	}

	private String adresse2(Adresse adresse) {
		return ifnull(adresse.getAutres(), "");
	}

	private String codePostal(Adresse adresse) {
		return String.valueOf(adresse.getCodePostal());
	}

	private String ville(Adresse adresse) {
		return ifnull(adresse.getVille(), "").toUpperCase();
	}

	private String ifnull(Object o, String valueIfNull) {
		return o == null ? valueIfNull : String.valueOf(o);
	}

	private static boolean exploitable(Adresse adressePostale) {
		return adressePostale != null
				&& adressePostale.getBoitePostale() != null;
	}

}
