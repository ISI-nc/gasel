package nc.ccas.gasel.model.aides.auto;

/** Class _StatutAide was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _StatutAide extends org.apache.cayenne.CayenneDataObject {

    public static final String ACTIF_PROPERTY = "actif";
    public static final String LIBELLE_PROPERTY = "libelle";
    public static final String LOCKED_PROPERTY = "locked";

    public static final String ID_PK_COLUMN = "id";

    public void setActif(Boolean actif) {
        writeProperty("actif", actif);
    }
    public Boolean getActif() {
        return (Boolean)readProperty("actif");
    }
    
    
    public void setLibelle(String libelle) {
        writeProperty("libelle", libelle);
    }
    public String getLibelle() {
        return (String)readProperty("libelle");
    }
    
    
    public void setLocked(Boolean locked) {
        writeProperty("locked", locked);
    }
    public Boolean getLocked() {
        return (Boolean)readProperty("locked");
    }
    
    
}
