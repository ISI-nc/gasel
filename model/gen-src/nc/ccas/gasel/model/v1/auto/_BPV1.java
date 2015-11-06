package nc.ccas.gasel.model.v1.auto;

import java.util.List;

import nc.ccas.gasel.model.v1.AdresseV1;

/** Class _BPV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _BPV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7620317910132916017L;
	public static final String BP_PROPERTY = "bp";
    public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String THE_CODE_POSTAL_ID_PROPERTY = "theCodePostalId";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String T_ADRESSE_ARRAY_PROPERTY = "tAdresseArray";

    public static final String T_BP_ID_PK_COLUMN = "t_bp_id";

    public void setBp(String bp) {
        writeProperty("bp", bp);
    }
    public String getBp() {
        return (String)readProperty("bp");
    }
    
    
    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setTheCodePostalId(Integer theCodePostalId) {
        writeProperty("theCodePostalId", theCodePostalId);
    }
    public Integer getTheCodePostalId() {
        return (Integer)readProperty("theCodePostalId");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void addToTAdresseArray(nc.ccas.gasel.model.v1.AdresseV1 obj) {
        addToManyTarget("tAdresseArray", obj, true);
    }
    public void removeFromTAdresseArray(nc.ccas.gasel.model.v1.AdresseV1 obj) {
        removeToManyTarget("tAdresseArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.AdresseV1> getTAdresseArray() {
        return (List<AdresseV1>)readProperty("tAdresseArray");
    }
    
    
}
