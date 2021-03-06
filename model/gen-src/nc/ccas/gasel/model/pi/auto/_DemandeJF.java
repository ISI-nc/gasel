package nc.ccas.gasel.model.pi.auto;

import java.util.List;

import nc.ccas.gasel.model.pi.AttributionJF;

/** Class _DemandeJF was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _DemandeJF extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 155381822502571881L;
	public static final String COMMISSION_PROPERTY = "commission";
    public static final String DATE_PROPERTY = "date";
    public static final String NUM_ENREGISTREMENT_PROPERTY = "numEnregistrement";
    public static final String ATTRIBUTIONS_PROPERTY = "attributions";
    public static final String AVIS_COMMISSION_PROPERTY = "avisCommission";
    public static final String COLLECTIVITE_PROPERTY = "collectivite";
    public static final String COURRIER_PROPERTY = "courrier";
    public static final String DOSSIER_PROPERTY = "dossier";
    public static final String JARDIN_PROPERTY = "jardin";
    public static final String ORIGINE_PROPERTY = "origine";

    public static final String ID_PK_COLUMN = "id";

    public void setCommission(java.util.Date commission) {
        writeProperty("commission", commission);
    }
    public java.util.Date getCommission() {
        return (java.util.Date)readProperty("commission");
    }
    
    
    public void setDate(java.util.Date date) {
        writeProperty("date", date);
    }
    public java.util.Date getDate() {
        return (java.util.Date)readProperty("date");
    }
    
    
    public void setNumEnregistrement(String numEnregistrement) {
        writeProperty("numEnregistrement", numEnregistrement);
    }
    public String getNumEnregistrement() {
        return (String)readProperty("numEnregistrement");
    }
    
    
    public void addToAttributions(nc.ccas.gasel.model.pi.AttributionJF obj) {
        addToManyTarget("attributions", obj, true);
    }
    public void removeFromAttributions(nc.ccas.gasel.model.pi.AttributionJF obj) {
        removeToManyTarget("attributions", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.pi.AttributionJF> getAttributions() {
        return (List<AttributionJF>)readProperty("attributions");
    }
    
    
    public void setAvisCommission(nc.ccas.gasel.model.pi.AvisCommissionJF avisCommission) {
        setToOneTarget("avisCommission", avisCommission, true);
    }

    public nc.ccas.gasel.model.pi.AvisCommissionJF getAvisCommission() {
        return (nc.ccas.gasel.model.pi.AvisCommissionJF)readProperty("avisCommission");
    } 
    
    
    public void setCollectivite(nc.ccas.gasel.model.pi.Collectivite collectivite) {
        setToOneTarget("collectivite", collectivite, true);
    }

    public nc.ccas.gasel.model.pi.Collectivite getCollectivite() {
        return (nc.ccas.gasel.model.pi.Collectivite)readProperty("collectivite");
    } 
    
    
    public void setCourrier(nc.ccas.gasel.model.pi.Courrier courrier) {
        setToOneTarget("courrier", courrier, true);
    }

    public nc.ccas.gasel.model.pi.Courrier getCourrier() {
        return (nc.ccas.gasel.model.pi.Courrier)readProperty("courrier");
    } 
    
    
    public void setDossier(nc.ccas.gasel.model.pi.AspectDossierPI dossier) {
        setToOneTarget("dossier", dossier, true);
    }

    public nc.ccas.gasel.model.pi.AspectDossierPI getDossier() {
        return (nc.ccas.gasel.model.pi.AspectDossierPI)readProperty("dossier");
    } 
    
    
    public void setJardin(nc.ccas.gasel.model.pi.JardinFamilial jardin) {
        setToOneTarget("jardin", jardin, true);
    }

    public nc.ccas.gasel.model.pi.JardinFamilial getJardin() {
        return (nc.ccas.gasel.model.pi.JardinFamilial)readProperty("jardin");
    } 
    
    
    public void setOrigine(nc.ccas.gasel.model.pi.enums.OrigineDemandeJF origine) {
        setToOneTarget("origine", origine, true);
    }

    public nc.ccas.gasel.model.pi.enums.OrigineDemandeJF getOrigine() {
        return (nc.ccas.gasel.model.pi.enums.OrigineDemandeJF)readProperty("origine");
    } 
    
    
}
