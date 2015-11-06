package nc.ccas.gasel.docs.aides;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideOM;
import nc.ccas.gasel.services.doc.ParamsProvider;
import nc.ccas.gasel.utils.QuickMap;
import nc.ccas.gasel.utils.QuickTreeMap;

public class AideOMParams extends ParamsProvider<AideOM> {

	public static final Set<String> PROVIDED = new TreeSet<String>();
	static {
		PROVIDED.addAll(AideParams.PROVIDED);
		PROVIDED.add("periode");
		PROVIDED.add("n° redevable");
		PROVIDED.add("n° facture");
	}

	private final AideParams helper = new AideParams();

	public AideOMParams() {
		super(AideOM.class);
	}

	@Override
	protected Set<String> getProvidedParams() {
		return PROVIDED;
	}

	@Override
	protected Map<String, String> toParamsImpl(AideOM om) {
		Aide aide = om.getAide();
		
		QuickMap<String, String> map = new QuickTreeMap<String, String>();
		map.merge(helper.toParams(aide));

		map.put("periode", om.getPeriodePrestation());
		map.put("n° redevable", om.getNumeroRedevable());
		map.put("n° facture", om.getNumeroFacture());

		return map.map();
	}

}
