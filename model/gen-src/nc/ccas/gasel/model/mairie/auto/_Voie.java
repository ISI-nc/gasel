package nc.ccas.gasel.model.mairie.auto;

import java.util.List;

import nc.ccas.gasel.model.mairie.Quartier;

/** Class _Voie was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _Voie extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8211368059320707262L;
	public static final String ACTE_CLASSEMENT_PROPERTY = "acteClassement";
    public static final String ACTE_PROPRIETE_PROPERTY = "actePropriete";
    public static final String CODE_PROPERTY = "code";
    public static final String CODE_ARTICLE_PROPERTY = "codeArticle";
    public static final String CODE_DOMAINE_PUBLIC_PROPERTY = "codeDomainePublic";
    public static final String CODE_RIVOLI_PROPERTY = "codeRivoli";
    public static final String CODE_TITRE_PROPERTY = "codeTitre";
    public static final String CODE_VOIE_DEBUT_PROPERTY = "codeVoieDebut";
    public static final String CODE_VOIE_FIN_PROPERTY = "codeVoieFin";
    public static final String CODE_VOIE_ORIG_PROPERTY = "codeVoieOrig";
    public static final String COMPLEMENT_PROPERTY = "complement";
    public static final String DATE_DEBUT_PROPERTY = "dateDebut";
    public static final String DATE_DELIBERATION_CLASSEMENT_PROPERTY = "dateDeliberationClassement";
    public static final String DATE_FIN_PROPERTY = "dateFin";
    public static final String DEBUT_IMPAIR_PROPERTY = "debutImpair";
    public static final String DEBUT_IMPAIR_ORIG_PROPERTY = "debutImpairOrig";
    public static final String DEBUT_PAIR_PROPERTY = "debutPair";
    public static final String DEBUT_PAIR_ORIG_PROPERTY = "debutPairOrig";
    public static final String DEBUT_PROPRIETE_PROPERTY = "debutPropriete";
    public static final String DELIBERATION_CLASSEMENT_PROPERTY = "deliberationClassement";
    public static final String DELIBERATION_DECLASSEMENT_PROPERTY = "deliberationDeclassement";
    public static final String DELIBERATION_DENOMINATION_PROPERTY = "deliberationDenomination";
    public static final String DELIBERATION_FIN_PROPERTY = "deliberationFin";
    public static final String DELIBERATION_SESSION_PROPERTY = "deliberationSession";
    public static final String FIN_IMPAIRE_PROPERTY = "finImpaire";
    public static final String FIN_IMPAIRE_ORIG_PROPERTY = "finImpaireOrig";
    public static final String FIN_PAIRE_PROPERTY = "finPaire";
    public static final String FIN_PAIRE_ORIG_PROPERTY = "finPaireOrig";
    public static final String FIN_PROPRIETE_PROPERTY = "finPropriete";
    public static final String GESTIONNAIRE_COMPETENCE_PROPERTY = "gestionnaireCompetence";
    public static final String LIBELLE_PROPERTY = "libelle";
    public static final String LIBELLE_ARTICLE_PROPERTY = "libelleArticle";
    public static final String LIBELLE_DOMAINE_PROPERTY = "libelleDomaine";
    public static final String LIBELLE_TITRE_PROPERTY = "libelleTitre";
    public static final String NOM_PROPERTY = "nom";
    public static final String NUMERO_PLAN_PROPERTY = "numeroPlan";
    public static final String PREMIERE_LETTRE_PROPERTY = "premiereLettre";
    public static final String PRENOM_PROPERTY = "prenom";
    public static final String REPERE_PLAN_PROPERTY = "reperePlan";
    public static final String COMMUNE_PROPERTY = "commune";
    public static final String QUARTIERS_PROPERTY = "quartiers";

    public static final String CDVOIE_PK_COLUMN = "cdvoie";

    public void setActeClassement(String acteClassement) {
        writeProperty("acteClassement", acteClassement);
    }
    public String getActeClassement() {
        return (String)readProperty("acteClassement");
    }
    
    
    public void setActePropriete(String actePropriete) {
        writeProperty("actePropriete", actePropriete);
    }
    public String getActePropriete() {
        return (String)readProperty("actePropriete");
    }
    
    
    public void setCode(Integer code) {
        writeProperty("code", code);
    }
    public Integer getCode() {
        return (Integer)readProperty("code");
    }
    
    
    public void setCodeArticle(Integer codeArticle) {
        writeProperty("codeArticle", codeArticle);
    }
    public Integer getCodeArticle() {
        return (Integer)readProperty("codeArticle");
    }
    
    
    public void setCodeDomainePublic(Integer codeDomainePublic) {
        writeProperty("codeDomainePublic", codeDomainePublic);
    }
    public Integer getCodeDomainePublic() {
        return (Integer)readProperty("codeDomainePublic");
    }
    
    
    public void setCodeRivoli(Integer codeRivoli) {
        writeProperty("codeRivoli", codeRivoli);
    }
    public Integer getCodeRivoli() {
        return (Integer)readProperty("codeRivoli");
    }
    
    
    public void setCodeTitre(Integer codeTitre) {
        writeProperty("codeTitre", codeTitre);
    }
    public Integer getCodeTitre() {
        return (Integer)readProperty("codeTitre");
    }
    
    
    public void setCodeVoieDebut(Integer codeVoieDebut) {
        writeProperty("codeVoieDebut", codeVoieDebut);
    }
    public Integer getCodeVoieDebut() {
        return (Integer)readProperty("codeVoieDebut");
    }
    
    
    public void setCodeVoieFin(Integer codeVoieFin) {
        writeProperty("codeVoieFin", codeVoieFin);
    }
    public Integer getCodeVoieFin() {
        return (Integer)readProperty("codeVoieFin");
    }
    
    
    public void setCodeVoieOrig(Integer codeVoieOrig) {
        writeProperty("codeVoieOrig", codeVoieOrig);
    }
    public Integer getCodeVoieOrig() {
        return (Integer)readProperty("codeVoieOrig");
    }
    
    
    public void setComplement(String complement) {
        writeProperty("complement", complement);
    }
    public String getComplement() {
        return (String)readProperty("complement");
    }
    
    
    public void setDateDebut(Integer dateDebut) {
        writeProperty("dateDebut", dateDebut);
    }
    public Integer getDateDebut() {
        return (Integer)readProperty("dateDebut");
    }
    
    
    public void setDateDeliberationClassement(Integer dateDeliberationClassement) {
        writeProperty("dateDeliberationClassement", dateDeliberationClassement);
    }
    public Integer getDateDeliberationClassement() {
        return (Integer)readProperty("dateDeliberationClassement");
    }
    
    
    public void setDateFin(Integer dateFin) {
        writeProperty("dateFin", dateFin);
    }
    public Integer getDateFin() {
        return (Integer)readProperty("dateFin");
    }
    
    
    public void setDebutImpair(Integer debutImpair) {
        writeProperty("debutImpair", debutImpair);
    }
    public Integer getDebutImpair() {
        return (Integer)readProperty("debutImpair");
    }
    
    
    public void setDebutImpairOrig(Integer debutImpairOrig) {
        writeProperty("debutImpairOrig", debutImpairOrig);
    }
    public Integer getDebutImpairOrig() {
        return (Integer)readProperty("debutImpairOrig");
    }
    
    
    public void setDebutPair(Integer debutPair) {
        writeProperty("debutPair", debutPair);
    }
    public Integer getDebutPair() {
        return (Integer)readProperty("debutPair");
    }
    
    
    public void setDebutPairOrig(Integer debutPairOrig) {
        writeProperty("debutPairOrig", debutPairOrig);
    }
    public Integer getDebutPairOrig() {
        return (Integer)readProperty("debutPairOrig");
    }
    
    
    public void setDebutPropriete(Integer debutPropriete) {
        writeProperty("debutPropriete", debutPropriete);
    }
    public Integer getDebutPropriete() {
        return (Integer)readProperty("debutPropriete");
    }
    
    
    public void setDeliberationClassement(String deliberationClassement) {
        writeProperty("deliberationClassement", deliberationClassement);
    }
    public String getDeliberationClassement() {
        return (String)readProperty("deliberationClassement");
    }
    
    
    public void setDeliberationDeclassement(String deliberationDeclassement) {
        writeProperty("deliberationDeclassement", deliberationDeclassement);
    }
    public String getDeliberationDeclassement() {
        return (String)readProperty("deliberationDeclassement");
    }
    
    
    public void setDeliberationDenomination(String deliberationDenomination) {
        writeProperty("deliberationDenomination", deliberationDenomination);
    }
    public String getDeliberationDenomination() {
        return (String)readProperty("deliberationDenomination");
    }
    
    
    public void setDeliberationFin(String deliberationFin) {
        writeProperty("deliberationFin", deliberationFin);
    }
    public String getDeliberationFin() {
        return (String)readProperty("deliberationFin");
    }
    
    
    public void setDeliberationSession(String deliberationSession) {
        writeProperty("deliberationSession", deliberationSession);
    }
    public String getDeliberationSession() {
        return (String)readProperty("deliberationSession");
    }
    
    
    public void setFinImpaire(Integer finImpaire) {
        writeProperty("finImpaire", finImpaire);
    }
    public Integer getFinImpaire() {
        return (Integer)readProperty("finImpaire");
    }
    
    
    public void setFinImpaireOrig(Integer finImpaireOrig) {
        writeProperty("finImpaireOrig", finImpaireOrig);
    }
    public Integer getFinImpaireOrig() {
        return (Integer)readProperty("finImpaireOrig");
    }
    
    
    public void setFinPaire(Integer finPaire) {
        writeProperty("finPaire", finPaire);
    }
    public Integer getFinPaire() {
        return (Integer)readProperty("finPaire");
    }
    
    
    public void setFinPaireOrig(Integer finPaireOrig) {
        writeProperty("finPaireOrig", finPaireOrig);
    }
    public Integer getFinPaireOrig() {
        return (Integer)readProperty("finPaireOrig");
    }
    
    
    public void setFinPropriete(Integer finPropriete) {
        writeProperty("finPropriete", finPropriete);
    }
    public Integer getFinPropriete() {
        return (Integer)readProperty("finPropriete");
    }
    
    
    public void setGestionnaireCompetence(String gestionnaireCompetence) {
        writeProperty("gestionnaireCompetence", gestionnaireCompetence);
    }
    public String getGestionnaireCompetence() {
        return (String)readProperty("gestionnaireCompetence");
    }
    
    
    public void setLibelle(String libelle) {
        writeProperty("libelle", libelle);
    }
    public String getLibelle() {
        return (String)readProperty("libelle");
    }
    
    
    public void setLibelleArticle(String libelleArticle) {
        writeProperty("libelleArticle", libelleArticle);
    }
    public String getLibelleArticle() {
        return (String)readProperty("libelleArticle");
    }
    
    
    public void setLibelleDomaine(String libelleDomaine) {
        writeProperty("libelleDomaine", libelleDomaine);
    }
    public String getLibelleDomaine() {
        return (String)readProperty("libelleDomaine");
    }
    
    
    public void setLibelleTitre(String libelleTitre) {
        writeProperty("libelleTitre", libelleTitre);
    }
    public String getLibelleTitre() {
        return (String)readProperty("libelleTitre");
    }
    
    
    public void setNom(String nom) {
        writeProperty("nom", nom);
    }
    public String getNom() {
        return (String)readProperty("nom");
    }
    
    
    public void setNumeroPlan(Integer numeroPlan) {
        writeProperty("numeroPlan", numeroPlan);
    }
    public Integer getNumeroPlan() {
        return (Integer)readProperty("numeroPlan");
    }
    
    
    public void setPremiereLettre(String premiereLettre) {
        writeProperty("premiereLettre", premiereLettre);
    }
    public String getPremiereLettre() {
        return (String)readProperty("premiereLettre");
    }
    
    
    public void setPrenom(String prenom) {
        writeProperty("prenom", prenom);
    }
    public String getPrenom() {
        return (String)readProperty("prenom");
    }
    
    
    public void setReperePlan(String reperePlan) {
        writeProperty("reperePlan", reperePlan);
    }
    public String getReperePlan() {
        return (String)readProperty("reperePlan");
    }
    
    
    public void setCommune(nc.ccas.gasel.model.mairie.Commune commune) {
        setToOneTarget("commune", commune, true);
    }

    public nc.ccas.gasel.model.mairie.Commune getCommune() {
        return (nc.ccas.gasel.model.mairie.Commune)readProperty("commune");
    } 
    
    
    public void addToQuartiers(nc.ccas.gasel.model.mairie.Quartier obj) {
        addToManyTarget("quartiers", obj, true);
    }
    public void removeFromQuartiers(nc.ccas.gasel.model.mairie.Quartier obj) {
        removeToManyTarget("quartiers", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.mairie.Quartier> getQuartiers() {
        return (List<Quartier>)readProperty("quartiers");
    }
    
    
}
