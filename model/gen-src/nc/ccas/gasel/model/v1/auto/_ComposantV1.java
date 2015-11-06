package nc.ccas.gasel.model.v1.auto;

import java.util.List;

import nc.ccas.gasel.model.v1.BoutonV1;
import nc.ccas.gasel.model.v1.HabilitationComposantV1;

/** Class _ComposantV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _ComposantV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3744456138283029682L;
	public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String DESCRIPTION_PROPERTY = "description";
    public static final String LABEL_PROPERTY = "label";
    public static final String NOM_PROPERTY = "nom";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String T_BOUTON_ARRAY_PROPERTY = "tBoutonArray";
    public static final String T_HABILITATIONCOMPOSANT_ARRAY_PROPERTY = "tHabilitationcomposantArray";

    public static final String T_COMPOSANT_ID_PK_COLUMN = "t_composant_id";

    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setDescription(String description) {
        writeProperty("description", description);
    }
    public String getDescription() {
        return (String)readProperty("description");
    }
    
    
    public void setLabel(String label) {
        writeProperty("label", label);
    }
    public String getLabel() {
        return (String)readProperty("label");
    }
    
    
    public void setNom(String nom) {
        writeProperty("nom", nom);
    }
    public String getNom() {
        return (String)readProperty("nom");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void addToTBoutonArray(nc.ccas.gasel.model.v1.BoutonV1 obj) {
        addToManyTarget("tBoutonArray", obj, true);
    }
    public void removeFromTBoutonArray(nc.ccas.gasel.model.v1.BoutonV1 obj) {
        removeToManyTarget("tBoutonArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.BoutonV1> getTBoutonArray() {
        return (List<BoutonV1>)readProperty("tBoutonArray");
    }
    
    
    public void addToTHabilitationcomposantArray(nc.ccas.gasel.model.v1.HabilitationComposantV1 obj) {
        addToManyTarget("tHabilitationcomposantArray", obj, true);
    }
    public void removeFromTHabilitationcomposantArray(nc.ccas.gasel.model.v1.HabilitationComposantV1 obj) {
        removeToManyTarget("tHabilitationcomposantArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.HabilitationComposantV1> getTHabilitationcomposantArray() {
        return (List<HabilitationComposantV1>)readProperty("tHabilitationcomposantArray");
    }
    
    
}
