package nc.ccas.gasel.model.mairie.auto;

/** Class _CodePostal was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _CodePostal extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -37044562360293152L;
	public static final String CODE_PROPERTY = "code";
    public static final String LIBELLE_PROPERTY = "libelle";
    public static final String COMMUNE_PROPERTY = "commune";

    public static final String CDCPOS_PK_COLUMN = "cdcpos";
    public static final String CODCOM_PK_COLUMN = "codcom";

    public void setCode(Integer code) {
        writeProperty("code", code);
    }
    public Integer getCode() {
        return (Integer)readProperty("code");
    }
    
    
    public void setLibelle(String libelle) {
        writeProperty("libelle", libelle);
    }
    public String getLibelle() {
        return (String)readProperty("libelle");
    }
    
    
    public void setCommune(nc.ccas.gasel.model.mairie.Commune commune) {
        setToOneTarget("commune", commune, true);
    }

    public nc.ccas.gasel.model.mairie.Commune getCommune() {
        return (nc.ccas.gasel.model.mairie.Commune)readProperty("commune");
    } 
    
    
}
