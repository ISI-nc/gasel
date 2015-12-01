package nc.ccas.gasel.docs.aides;

import static java_gaps.NumberUtils.add;
import static java_gaps.NumberUtils.sub;
import static nc.ccas.gasel.starjet.aides.CourrierUtils.montant;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.core.Utilisateur;
import nc.ccas.gasel.services.doc.ParamsProvider;
import nc.ccas.gasel.utils.QuickMap;
import nc.ccas.gasel.utils.QuickTreeMap;

public class AideEauParams extends ParamsProvider<AideEau> {

	public static final Set<String> PROVIDED = new TreeSet<String>();
	static {
		PROVIDED.addAll(AideParams.PROVIDED);
		PROVIDED.add("montant facture");
		PROVIDED.add("remise compteur");
		PROVIDED.add("periode");
		PROVIDED.add("pec conso");
		PROVIDED.add("depassement");
		PROVIDED.add("restant du");
		PROVIDED.add("police");
		PROVIDED.add("deja paye");
	}

	private final AideParams helper = new AideParams();

	public AideEauParams() {
		super(AideEau.class);
	}

	@Override
	protected Set<String> getProvidedParams() {
		return PROVIDED;
	}

	@Override
	protected Map<String, String> toParamsImpl(AideEau eau) {
		Aide aide = eau.getAide();
		
		QuickMap<String, String> map = new QuickTreeMap<String, String>();
		map.merge(helper.toParams(aide));

		map.put("montant facture", montant(montantFacture(eau)));
		map.put("remise compteur", montant(remiseCompteur(aide)));
		map.put("periode", eau.getPeriodePrestation());
		map.put("pec conso", montant(eau.getPriseEnChargeConso()));
		map.put("depassement", String.valueOf(eau.getDepassementM3()));
		map.put("restant du", montant(eau.getRestantDu()));
		map.put("police", eau.getPolice());
		map.put("deja paye", montant(eau.getMontantDejaPaye()));

		// Créateur de l'aide (spécifié ainsi, la v1 renvoi l'utilisateur logué)
		Utilisateur u = aide.getModifUtilisateur();
		map.put("responsable", u.getPrenom() + " " + u.getNom());

		return map.map();
	}

	private Number montantFacture(AideEau aide) {
		return add(aide.getPriseEnChargeConso(), aide.getRestantDu());
	}

	private Number remiseCompteur(Aide aide) {
		return sub(aide.getMontant(), aide.getEau().getPriseEnChargeConso());
	}

}
