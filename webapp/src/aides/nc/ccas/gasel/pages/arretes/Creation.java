package nc.ccas.gasel.pages.arretes;

import static nc.ccas.gasel.model.core.Constante.getString;
import static org.apache.cayenne.DataObjectUtils.intPKForObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.Arrete;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.aides.TypeArrete;
import nc.ccas.gasel.model.aides.UsageBon;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.services.reports.ReportService;

import org.apache.tapestry.IRequestCycle;

import com.asystan.common.AutoBox;

public abstract class Creation extends EditPage<Arrete> {

	public static void fillParameters(Map<String, Object> reportParameters,
			TypeArrete typeArrete) {
		reportParameters.put("titre", typeArrete.getTitre());
		reportParameters.put("article1", typeArrete.getArticle1());
		reportParameters
				.put("infosImputation", typeArrete.getInfosImputation());
	}

	private List<AjoutAidesLigne> _syntheseBons;

	public Creation() {
		super(Arrete.class);
	}

	@Override
	public String getTitre() {
		if (getArrete() == null)
			return "Arrêté";
		return "Arrêté"
				+ (getArrete().getNumero() == null ? "" : " n°"
						+ getArrete().getNumero()) + objectStatus();
	}

	public Arrete getArrete() {
		return getObject();
	}

	public void setArrete(Arrete arrete) {
		setObject(arrete);
	}

	@Override
	protected void prepareNewObject(Arrete arrete) {
		arrete.setCreation(new Date());
		arrete.setChamp(getString("Arretes/Champ"));
	}

	public void editer() {
		if (getArrete().getEdite()) {
			redirect();
			return;
		}

		dontRedirect();
		if (enregistrerSansFermer()) {
			getArrete().setEdite(true);
			dontCheckEnregistrementDesactive(); // sinon refusé ;)
			enregistrerSansFermer();
		}
		redirect();
	}

	public void invokeEditer(IRequestCycle cycle, String format) {
		Map<String, Object> reportParameters = new TreeMap<>();
		reportParameters.put("ARRETE_ID", intPKForObject(getObject()));

		TypeArrete typeArrete = getArrete().getType();
		fillParameters(reportParameters, typeArrete);

		ReportService.invokeWithParameters(cycle, reportView(), //
				format, reportParameters);
	}

	private String reportView() {
		switch (getArrete().getType().getId()) {
		case TypeArrete.ALIMENTATION:
			return "arrete-alimentation";
		case TypeArrete.AVANCE:
			return "arrete-avance";
		case TypeArrete.EAU:
			return "arrete-cde";
		case TypeArrete.EEC:
			return "arrete-eec";
		case TypeArrete.OM:
			return "arrete-om";
		case TypeArrete.SOLIDARITE:
			return "arrete-solidarite";
		case TypeArrete.URGENCES:
			return "arrete-urgences";
		default:
			if (AutoBox.valueOf(getArrete().getType().getAvecFactures())) {
				return "arrete-generique";
			} else {
				return "arrete-generique-sans-factures";
			}
		}
	}

	@Override
	public boolean getEnregistrementDesactive() {
		return super.getEnregistrementDesactive() || getArrete().getEdite();
	}

	public List<AjoutAidesLigne> getSyntheseBons() {
		if (_syntheseBons == null) {
			doPrefetch();

			List<UsageBon> bonsValides = getObject().getBonsValides();
			List<Bon> bons = new ArrayList<Bon>(bonsValides.size());
			for (UsageBon ub : bonsValides) {
				bons.add(ub.getBon());
			}
			_syntheseBons = AjoutAidesLigne.traite(bons);
		}
		return _syntheseBons;
	}

	private void doPrefetch() {
		Arrete arrete = getObject();

		if (isNew(arrete))
			return; // useless

		CommonQueries.prefetch(Collections.singletonList(arrete),
				"bonsValides.bon.aide.dossier.dossier.chefFamille",
				"bonsValides.bon.aide.eau", //
				"bonsValides.bon.aide.orduresMenageres", //
				"bonsValides.bon.usages" //
		);
	}

	public double getMontantTotal() {
		double sum = 0;
		for (AjoutAidesLigne aj : getSyntheseBons()) {
			for (Bon b : aj.getBons()) {
				sum += b.getUsage().getMontantUtilise();
			}
		}
		return sum;
	}

	public boolean isAideEau() {
		for (AjoutAidesLigne l : getSyntheseBons()) {
			if (l.getAide().getEau() != null)
				return true;
		}
		return false;
	}

	public boolean isAideOM() {
		for (AjoutAidesLigne l : getSyntheseBons()) {
			if (l.getAide().getOrduresMenageres() != null)
				return true;
		}
		return false;
	}

	public abstract AjoutAidesLigne getRow();

	public String getPeriode() {
		Aide aide = getRow().getAide();
		if (aide.getEau() != null) {
			return aide.getEau().getPeriodePrestation();
		} else if (aide.getOrduresMenageres() != null) {
			return aide.getOrduresMenageres().getPeriodePrestation();
		}
		return null;
	}

	public void goAjoutAides(IRequestCycle cycle) {
		AjoutAides page = (AjoutAides) cycle.getPage("arretes/AjoutAides");
		page.prepareActivation();
		page.setTypeArrete(getArrete().getType());
		page.redirect();
	}

	@Override
	protected void cleanupAfterRender(IRequestCycle cycle) {
		super.cleanupAfterRender(cycle);
		_syntheseBons = null;
	}

}
