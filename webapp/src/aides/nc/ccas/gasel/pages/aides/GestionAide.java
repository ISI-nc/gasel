package nc.ccas.gasel.pages.aides;

import static com.asystan.common.cayenne.QueryFactory.createAnd;
import static com.asystan.common.cayenne.QueryFactory.createEquals;
import static com.asystan.common.cayenne.QueryFactory.createTrue;
import static nc.ccas.gasel.model.aides.StatutAide.IMMEDIATE;
import static nc.ccas.gasel.modelUtils.CommonQueries.select;
import static nc.ccas.gasel.pages.aides.CalculetteAideEau.TRIMESTRE_PSM;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.Check;
import nc.ccas.gasel.EditPage;
import nc.ccas.gasel.MethodObjectCallback;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.aides.AideOM;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.aides.StatutAide;
import nc.ccas.gasel.model.aides.valid.AideValidator;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.budget.SecteurAide;
import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.model.core.docs.RefModeleDocument;
import nc.ccas.gasel.model.core.enums.TypePublic;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;
import nc.ccas.gasel.modelUtils.DateUtils;
import nc.ccas.gasel.pages.dossiers.CalculetteData;
import nc.ccas.gasel.validator.GaselValidator;

import org.apache.cayenne.DataObject;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

@SuppressWarnings("unchecked")
public abstract class GestionAide extends EditPage<Aide> {

	public GestionAide() {
		super(Aide.class);
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);

		if (getParent() == null) {
			setParent(getAide().getDossier());
			setParentProperty("dossier");
		}
		if (isNew(getAide())) {
			if (getSecteurAide() == null) {
				setNatureAide(null);
			}
		}
		if (getAide().getNature() != null && getAide().getStatut() == null) {
			int defaultFreq = StatutAide.IMMEDIATE;
			if (getAide().getNature().getImputation().isAlimentation()) {
				defaultFreq = StatutAide.PLURIMENSUELLE;
			}
			getAide().setStatut(objectForPk(StatutAide.class, defaultFreq));
		}
		if (getAide().getNature() != null) {
			if (getAide().getNature().isEau() && getAide().getEau() == null) {
				getAide().setEau(createDataObject(AideEau.class));
			}
			if (getAide().getNature().isOrduresMenageres()
					&& getAide().getOrduresMenageres() == null) {
				getAide().setOrduresMenageres(createDataObject(AideOM.class));
			}
		}
	}

	@Override
	protected void prepareNewObject(Aide aide) {
		AspectAides dossier = (AspectAides) getParent();
		if (dossier == null) {
			throw new RuntimeException(
					"Impossible de créer une aide sans dossier");
		}
		aide.setStatut(sql.query().byId(StatutAide.class, IMMEDIATE));
		aide.setDebut(dates.today());
		aide.setFin(dates.finMois());
	}

	@Override
	protected void prepareEnregistrer() {
		Aide aide = getAide();
		NatureAide nature = getAide().getNature();
		if (nature != null) {
			if (!nature.isEau() && aide.getEau() != null) {
				AideEau eau = aide.getEau();
				aide.setEau(null);
				getObjectContext().deleteObject(eau);
			}
			if (!nature.isOrduresMenageres()
					&& aide.getOrduresMenageres() != null) {
				AideOM om = aide.getOrduresMenageres();
				aide.setOrduresMenageres(null);
				getObjectContext().deleteObject(om);
			}
		}
	}

	public Aide getAide() {
		return getObject();
	}

	public void setAide(Aide aide) {
		setObject(aide);
	}

	public boolean getModifAideDisabled() {
		boolean aideModifiable = isNew(getObject())
				|| getObject().getDebut() == null
				|| getObject().getDebut().before(dates.finMois());
		return !aideModifiable;
	}

	public boolean getModifAideAlwaysDisabled() {
		return getAide().getPersistenceState() != PersistenceState.NEW;
	}

	public IPropertySelectionModel getPeriodePSM() {
		LinkedList<String> options = new LinkedList<String>();
		for (int annee = DateUtils.anneeEnCours() - 1; annee <= DateUtils
				.anneeEnCours() + 1; annee++) {
			for (String periode : Arrays.asList("1er trimestre",
					"2è trimestre", "3è trimestre", "4è trimestre")) {
				options.add(periode + " " + annee);
			}
		}
		if (getPeriode() != null && !options.contains(getPeriode())) {
			options.addFirst(getPeriode());
		}
		return new StringPropertySelectionModel(
				options.toArray(new String[options.size()]));
	}

	public String getPeriode() {
		Aide aide = getAide();
		if (aide == null) {
			return null;
		}
		if (aide.getEau() != null) {
			return aide.getEau().getPeriodePrestation();
		}
		if (aide.getOrduresMenageres() != null) {
			return aide.getOrduresMenageres().getPeriodePrestation();
		}
		return null;
	}

	public void setPeriode(String periode) {
		Aide aide = getAide();
		if (aide == null) {
			return;
		}
		if (aide.getEau() != null) {
			aide.getEau().setPeriodePrestation(periode);
		} else if (aide.getOrduresMenageres() != null) {
			aide.getOrduresMenageres().setPeriodePrestation(periode);
		}
	}

	// ------------------------------------------------------------------------
	// Gestion du type d'aide
	//

	@Persist("workflow")
	public abstract SecteurAide getSecteurAide();

	public abstract void setSecteurAide(SecteurAide secteur);

	public abstract void setNatureAide(NatureAide value);

	public List<TypePublic> getTypesPublic() {
		List<TypePublic> publics = sql.query().enumeration(TypePublic.class);
		if (!isNew(getAide())) {
			// On a que le combo pour changer le type de public
			// => filtrage pas nature d'aide
			final NatureAide nature = getAide().getNature();
			publics = sql.filtrer(publics, new Check<TypePublic>() {

				public boolean check(TypePublic value) {
					return nature.isAutorise(value);
				}
			});
		}
		return publics;
	}

	public TypePublic getTypePublic() {
		return getAide().getPublic();
	}

	public void setTypePublic(TypePublic typePublic) {
		getAide().setPublic(typePublic);
		setSecteurAide(null);
		getAide().setNature(null);
	}

	public List<SecteurAide> getSecteursAide() {
		SelectQuery query = new SelectQuery(SecteurAide.class, //
				createEquals("natures.publicsConcernes.typePublic", getAide()
						.getPublic()));
		return getObjectContext().performQuery(query);
	}

	public List<NatureAide> getNaturesAide() {
		return select(
				NatureAide.class,
				createAnd(
						//
						createTrue("actif"), //
						createEquals("parent", getSecteurAide()), //
						createEquals("publicsConcernes.typePublic", getAide()
								.getPublic())));
	}

	@Override
	protected GaselValidator<? super Aide> buildValidator() {
		return new AideValidator() {
			@Override
			public void validateImpl(Aide aide) {
				super.validateImpl(aide);
				if (isNew(aide)) {
					require(true, getSecteurAide(), "secteur d'aide");
				}
			}
		};
	}

	// ------------------------------------------------------------------------
	// Ajouter une Aide directement
	//

	public void ajouterAide() {
		dontRedirect();
		if (!enregistrerSansFermer()) {
			return;
		}
		setObject(null);
		dontOpenNewEntry = true;
		setMessageEnregistrement(getMessageEnregistrement()
				+ " Nouvelle aide ouverte.");

		// Ouverture d'un nouveau contexte
		setDataContext(CayenneUtils.createDataContext());
		activateForParent(getParent(), "dossier");
	}

	// ------------------------------------------------------------------------
	// Édition des bons
	//

	public void editerBons() {
		((EditionBons) getRequestCycle().getPage("aides/EditionBons"))
				.open(getAide());
	}

	// ------------------------------------------------------------------------
	// Calculatrice
	//

	@InjectPage("aides/CalculetteAideEau")
	public abstract CalculetteAideEau getPageCalculette();

	public void ouvrirCalculette() {
		getPageCalculette().activate((AspectAides) getParent(),
				new MethodObjectCallback(this, "retourCalculetteAideEau"));
	}

	public void retourCalculetteAideEau(CalculetteData data) {
		Aide aide = getAide();
		AideEau eau = aide.getEau();
		eau.setDepassementM3(data.getDepassementM3());
		eau.setMontantDejaPaye((int) Math.round(data.getMontantDejaPaye()));
		eau.setPeriodePrestation(data.getPeriodePrestation());
		eau.setPriseEnChargeConso((int) Math.round(data
				.getPriseEnChargeTotale()));
		eau.setRestantDu((int) Math.round(data.getRestantDu()));
		aide.setMontant((int) Math.round(data.getPriseEnChargeTotale()));
	}

	// ------------------------------------------------------------------------
	// Courriers
	//

	public List<Courrier> getCourriers() {
		NatureAide na = getAide().getNature();
		if (na == null) {
			return Collections.emptyList();
		} else if (na.isOrduresMenageres()) {
			return Arrays.asList( //
					// Arrays.asList("aide.om.admin", "Courrier administré"), //
					new Courrier("aides.courrier_frn_om",
							"Courrier fournisseur", getAide()
									.getOrduresMenageres()) //
					);
		} else if (na.isEau()) {
			return Arrays.asList(new Courrier("aides.courrier_frn_eau",
					"Courrier fournisseur", getAide().getEau()));
		} else {
			return Arrays.asList( //
					new Courrier("aides.courrier_frn_gaz", "Courrier gaz",
							getAide()) //
					);
		}
	}

	@SuppressWarnings("serial")
	public class Courrier implements Serializable {
		private String ref;
		private String label;
		private DataObject subject;

		public Courrier(String ref, String label, DataObject subject) {
			super();
			this.ref = ref;
			this.label = label;
			this.subject = subject;
		}

		public String getRef() {
			return ref;
		}

		public String getLabel() {
			return label;
		}

		public DataObject getSubject() {
			return subject;
		}
	}

	/*
	 * Courriers
	 */

	public ModeleDocument getDocCourierEau() {
		return getDoc("aides.courrier_eau");
	}

	public ModeleDocument getDocCourierOM() {
		return getDoc("aides.courrier_om");
	}

	private ModeleDocument getDoc(String key) {
		RefModeleDocument ref = CommonQueries.unique(getObjectContext(),
				RefModeleDocument.class, RefModeleDocument.KEY_PROPERTY, key);
		if (ref == null)
			return null;
		return ref.getModele();
	}

	// --

	public IPropertySelectionModel getPeriodePrestationPSM() {
		return TRIMESTRE_PSM;
	}

}