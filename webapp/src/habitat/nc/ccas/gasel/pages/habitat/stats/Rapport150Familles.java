package nc.ccas.gasel.pages.habitat.stats;

import java.util.TreeSet;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.core.Ressource;
import nc.ccas.gasel.model.habitat.AideLogement;
import nc.ccas.gasel.model.habitat.AideSociale;
import nc.ccas.gasel.model.habitat.AspectDossierHabitat;
import nc.ccas.gasel.model.habitat.AspectSIE;
import nc.ccas.gasel.model.habitat.SecoursImmediatExceptionnel;

import com.asystan.common.StringUtils;

public abstract class Rapport150Familles extends BasePage {

	public abstract AspectDossierHabitat getRow();
	
	public int getCredit() {
		AspectDossierHabitat adh = getRow();
		if ( adh.getDerniereDemandeAffectation().getTypeDemande().isRehabilitation()) {
			return adh.getDerniereDemandeAffectation().getAffRehabilitation().getTraite();
		}
		else if (adh.getDerniereDemandeAffectation().getTypeDemande().isAccession()) {
			return adh.getDerniereDemandeAffectation().getAffAccession().getTraite();
		}
		
		return 0;
	}
	
	public int getRessourcesNettes() {
		AspectDossierHabitat adh = getRow();
		int total = 0;
		for (Personne p : adh.getDossier().getPersonnes()) {
			for (Ressource r : p.getRessources()) {
				total += r.getMontant();
			}
		}
		return total;
	}
	
	public int getAutresRessources() {
		AspectDossierHabitat adh = getRow();
		int total = 0;
		total += adh.getALProvince();
		for (AideLogement aides : adh.getAides()) {
			total += aides.getMontantALTotal();
		}
		for (AideSociale aides : adh.getAidesComplementaires()) {
			total += aides.getMontant();
		}
		return total;
	}
	
	public int getSolde() {
		return (getRessourcesNettes()+getAutresRessources())-getCredit();
		
	}
	
	public AspectSIE getSIE() {
		AspectDossierHabitat adh = getRow();
		return adh.getDossier().getAspect(AspectSIE.class);
	}
	
	public int getMontantSIE() {
		int total = 0;
		for(SecoursImmediatExceptionnel sie : getSIE().getSecours()) {
			total += sie.getMontant();
		}
		return total;
	}
	
	public String getMotifSIE() {
		TreeSet<String> motifs = new TreeSet<String>();
		for(SecoursImmediatExceptionnel sie : getSIE().getSecours()) {
			motifs.add(sie.getMotif().getLibelle());
		}
		return StringUtils.join(",\n", motifs);
	}

	public int getPartContributiveMensuelle() {
		AspectDossierHabitat adh = getRow();
		if ( adh.getDerniereDemandeAffectation().getTypeDemande().isRehabilitation()
				||adh.getDerniereDemandeAffectation().getTypeDemande().isAccession()) {
			return adh.getDerniereDemandeAffectation().getAffRehabilitation().getTraite()
			-getAutresRessources();
		}
		else if (adh.getDerniereDemandeAffectation().getTypeDemande().isLocatif()) {
			return adh.getDerniereDemandeAffectation().getAffLocatif().getLoyerCC()
			-getAutresRessources();
		}
		else return 0;
	}
	
	public double getTauxEffort() {
		AspectDossierHabitat adh = getRow();
		if ( adh.getDerniereDemandeAffectation().getTypeDemande().isRehabilitation()
				||adh.getDerniereDemandeAffectation().getTypeDemande().isAccession()) {
			return (double) getPartContributiveMensuelle()/adh.getDerniereDemandeAffectation().getAffRehabilitation().getTraite();
		}
		else if (adh.getDerniereDemandeAffectation().getTypeDemande().isLocatif()) {
			return (double) getPartContributiveMensuelle()/adh.getDerniereDemandeAffectation().getAffLocatif().getLoyerCC();
		}
		else return 0.00; 
	}
}
