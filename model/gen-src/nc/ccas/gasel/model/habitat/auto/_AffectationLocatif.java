package nc.ccas.gasel.model.habitat.auto;

import java.util.List;

import nc.ccas.gasel.model.habitat.enums.ProblemeLogement;

/** Class _AffectationLocatif was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _AffectationLocatif extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6111087582173210850L;
	public static final String BAIL_POUR_LE_COMPTE_DE_PROPERTY = "bailPourLeCompteDe";
    public static final String CAUTION_SOLIDAIRE_PROPERTY = "cautionSolidaire";
    public static final String CHARGES_PROPERTY = "charges";
    public static final String COMMISSION_ALT_PROPERTY = "commissionALT";
    public static final String COMPLEMENT_FSH_PROPERTY = "complementFSH";
    public static final String COMPTEUR_EAU_PROPERTY = "compteurEau";
    public static final String COMPTEUR_ELECTRICITE_PROPERTY = "compteurElectricite";
    public static final String DEPOT_GARANTIE_PROPERTY = "depotGarantie";
    public static final String ENTREE_PROPERTY = "entree";
    public static final String FRAIS_COMPLEMENTAIRES_PROPERTY = "fraisComplementaires";
    public static final String LOYER_HC_PROPERTY = "loyerHC";
    public static final String MONTANT_ASSURANCE_HABITATION_PROPERTY = "montantAssuranceHabitation";
    public static final String NUMERO_BAIL_PROPERTY = "numeroBail";
    public static final String ASSURANCE_HABITATION_PROPERTY = "assuranceHabitation";
    public static final String BAILLEUR_PROPERTY = "bailleur";
    public static final String CATEGORIE_PROPERTY = "categorie";
    public static final String PROBLEMES_PROPERTY = "problemes";
    public static final String QUARTIER_PROPERTY = "quartier";
    public static final String TYPE_PROPERTY = "type";

    public static final String ID_PK_COLUMN = "id";

    public void setBailPourLeCompteDe(Boolean bailPourLeCompteDe) {
        writeProperty("bailPourLeCompteDe", bailPourLeCompteDe);
    }
    public Boolean getBailPourLeCompteDe() {
        return (Boolean)readProperty("bailPourLeCompteDe");
    }
    
    
    public void setCautionSolidaire(Boolean cautionSolidaire) {
        writeProperty("cautionSolidaire", cautionSolidaire);
    }
    public Boolean getCautionSolidaire() {
        return (Boolean)readProperty("cautionSolidaire");
    }
    
    
    public void setCharges(Integer charges) {
        writeProperty("charges", charges);
    }
    public Integer getCharges() {
        return (Integer)readProperty("charges");
    }
    
    
    public void setCommissionALT(java.util.Date commissionALT) {
        writeProperty("commissionALT", commissionALT);
    }
    public java.util.Date getCommissionALT() {
        return (java.util.Date)readProperty("commissionALT");
    }
    
    
    public void setComplementFSH(Boolean complementFSH) {
        writeProperty("complementFSH", complementFSH);
    }
    public Boolean getComplementFSH() {
        return (Boolean)readProperty("complementFSH");
    }
    
    
    public void setCompteurEau(Integer compteurEau) {
        writeProperty("compteurEau", compteurEau);
    }
    public Integer getCompteurEau() {
        return (Integer)readProperty("compteurEau");
    }
    
    
    public void setCompteurElectricite(Integer compteurElectricite) {
        writeProperty("compteurElectricite", compteurElectricite);
    }
    public Integer getCompteurElectricite() {
        return (Integer)readProperty("compteurElectricite");
    }
    
    
    public void setDepotGarantie(Integer depotGarantie) {
        writeProperty("depotGarantie", depotGarantie);
    }
    public Integer getDepotGarantie() {
        return (Integer)readProperty("depotGarantie");
    }
    
    
    public void setEntree(java.util.Date entree) {
        writeProperty("entree", entree);
    }
    public java.util.Date getEntree() {
        return (java.util.Date)readProperty("entree");
    }
    
    
    public void setFraisComplementaires(Integer fraisComplementaires) {
        writeProperty("fraisComplementaires", fraisComplementaires);
    }
    public Integer getFraisComplementaires() {
        return (Integer)readProperty("fraisComplementaires");
    }
    
    
    public void setLoyerHC(Integer loyerHC) {
        writeProperty("loyerHC", loyerHC);
    }
    public Integer getLoyerHC() {
        return (Integer)readProperty("loyerHC");
    }
    
    
    public void setMontantAssuranceHabitation(Integer montantAssuranceHabitation) {
        writeProperty("montantAssuranceHabitation", montantAssuranceHabitation);
    }
    public Integer getMontantAssuranceHabitation() {
        return (Integer)readProperty("montantAssuranceHabitation");
    }
    
    
    public void setNumeroBail(Integer numeroBail) {
        writeProperty("numeroBail", numeroBail);
    }
    public Integer getNumeroBail() {
        return (Integer)readProperty("numeroBail");
    }
    
    
    public void setAssuranceHabitation(nc.ccas.gasel.model.habitat.enums.AssuranceHabitation assuranceHabitation) {
        setToOneTarget("assuranceHabitation", assuranceHabitation, true);
    }

    public nc.ccas.gasel.model.habitat.enums.AssuranceHabitation getAssuranceHabitation() {
        return (nc.ccas.gasel.model.habitat.enums.AssuranceHabitation)readProperty("assuranceHabitation");
    } 
    
    
    public void setBailleur(nc.ccas.gasel.model.habitat.enums.Bailleur bailleur) {
        setToOneTarget("bailleur", bailleur, true);
    }

    public nc.ccas.gasel.model.habitat.enums.Bailleur getBailleur() {
        return (nc.ccas.gasel.model.habitat.enums.Bailleur)readProperty("bailleur");
    } 
    
    
    public void setCategorie(nc.ccas.gasel.model.habitat.enums.CategorieSocialeLogement categorie) {
        setToOneTarget("categorie", categorie, true);
    }

    public nc.ccas.gasel.model.habitat.enums.CategorieSocialeLogement getCategorie() {
        return (nc.ccas.gasel.model.habitat.enums.CategorieSocialeLogement)readProperty("categorie");
    } 
    
    
    public void addToProblemes(nc.ccas.gasel.model.habitat.enums.ProblemeLogement obj) {
        addToManyTarget("problemes", obj, true);
    }
    public void removeFromProblemes(nc.ccas.gasel.model.habitat.enums.ProblemeLogement obj) {
        removeToManyTarget("problemes", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.habitat.enums.ProblemeLogement> getProblemes() {
        return (List<ProblemeLogement>)readProperty("problemes");
    }
    
    
    public void setQuartier(nc.ccas.gasel.model.mairie.Quartier quartier) {
        setToOneTarget("quartier", quartier, true);
    }

    public nc.ccas.gasel.model.mairie.Quartier getQuartier() {
        return (nc.ccas.gasel.model.mairie.Quartier)readProperty("quartier");
    } 
    
    
    public void setType(nc.ccas.gasel.model.habitat.enums.TypeLogement type) {
        setToOneTarget("type", type, true);
    }

    public nc.ccas.gasel.model.habitat.enums.TypeLogement getType() {
        return (nc.ccas.gasel.model.habitat.enums.TypeLogement)readProperty("type");
    } 
    
    
}
