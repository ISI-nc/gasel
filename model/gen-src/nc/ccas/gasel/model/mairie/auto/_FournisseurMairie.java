package nc.ccas.gasel.model.mairie.auto;

/** Class _FournisseurMairie was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _FournisseurMairie extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1714735065994440070L;
	public static final String EST_ACTIF_PROPERTY = "estActif";
    public static final String LIBELLE_PROPERTY = "libelle";

    public static final String IDETBS_PK_COLUMN = "idetbs";

    public void setEstActif(String estActif) {
        writeProperty("estActif", estActif);
    }
    public String getEstActif() {
        return (String)readProperty("estActif");
    }
    
    
    public void setLibelle(String libelle) {
        writeProperty("libelle", libelle);
    }
    public String getLibelle() {
        return (String)readProperty("libelle");
    }
    
    
}
