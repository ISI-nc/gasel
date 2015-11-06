package nc.ccas.gasel.model.v1.auto;

import java.util.List;

import nc.ccas.gasel.model.v1.AdministreV1;

/** Class _SituationProfessionnelleV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _SituationProfessionnelleV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 668383310127821361L;
	public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String ENUMERATION_PROPERTY = "enumeration";
    public static final String T_ADMINISTRE_ARRAY_PROPERTY = "tAdministreArray";

    public static final String T_ENUM_ID_PK_COLUMN = "t_enum_id";

    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void setEnumeration(nc.ccas.gasel.model.v1.EnumerationV1 enumeration) {
        setToOneTarget("enumeration", enumeration, true);
    }

    public nc.ccas.gasel.model.v1.EnumerationV1 getEnumeration() {
        return (nc.ccas.gasel.model.v1.EnumerationV1)readProperty("enumeration");
    } 
    
    
    public void addToTAdministreArray(nc.ccas.gasel.model.v1.AdministreV1 obj) {
        addToManyTarget("tAdministreArray", obj, true);
    }
    public void removeFromTAdministreArray(nc.ccas.gasel.model.v1.AdministreV1 obj) {
        removeToManyTarget("tAdministreArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.AdministreV1> getTAdministreArray() {
        return (List<AdministreV1>)readProperty("tAdministreArray");
    }
    
    
}
