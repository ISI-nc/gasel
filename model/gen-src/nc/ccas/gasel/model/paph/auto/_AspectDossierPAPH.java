package nc.ccas.gasel.model.paph.auto;

import java.util.List;

import nc.ccas.gasel.model.paph.DossierPAPH;

/** Class _AspectDossierPAPH was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _AspectDossierPAPH extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8571166569037557724L;
	public static final String DOSSIER_PROPERTY = "dossier";
    public static final String DOSSIERS_PROPERTY = "dossiers";

    public static final String DOSSIER_ID_PK_COLUMN = "dossier_id";

    public void setDossier(nc.ccas.gasel.model.core.Dossier dossier) {
        setToOneTarget("dossier", dossier, true);
    }

    public nc.ccas.gasel.model.core.Dossier getDossier() {
        return (nc.ccas.gasel.model.core.Dossier)readProperty("dossier");
    } 
    
    
    public void addToDossiers(nc.ccas.gasel.model.paph.DossierPAPH obj) {
        addToManyTarget("dossiers", obj, true);
    }
    public void removeFromDossiers(nc.ccas.gasel.model.paph.DossierPAPH obj) {
        removeToManyTarget("dossiers", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.paph.DossierPAPH> getDossiers() {
        return (List<DossierPAPH>)readProperty("dossiers");
    }
    
    
}
