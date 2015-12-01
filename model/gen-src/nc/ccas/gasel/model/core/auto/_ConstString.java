package nc.ccas.gasel.model.core.auto;

/** Class _ConstString was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _ConstString extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 168684724110217367L;
	public static final String DESCRIPTION_PROPERTY = "description";
    public static final String LIBELLE_PROPERTY = "libelle";
    public static final String NOM_PROPERTY = "nom";
    public static final String VALEUR_PROPERTY = "valeur";

    public static final String ID_PK_COLUMN = "id";

    public void setDescription(String description) {
        writeProperty("description", description);
    }
    public String getDescription() {
        return (String)readProperty("description");
    }
    
    
    public void setLibelle(String libelle) {
        writeProperty("libelle", libelle);
    }
    public String getLibelle() {
        return (String)readProperty("libelle");
    }
    
    
    public void setNom(String nom) {
        writeProperty("nom", nom);
    }
    public String getNom() {
        return (String)readProperty("nom");
    }
    
    
    public void setValeur(String valeur) {
        writeProperty("valeur", valeur);
    }
    public String getValeur() {
        return (String)readProperty("valeur");
    }
    
    
}
