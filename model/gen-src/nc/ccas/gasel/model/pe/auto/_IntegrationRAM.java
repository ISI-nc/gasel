package nc.ccas.gasel.model.pe.auto;

/** Class _IntegrationRAM was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _IntegrationRAM extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5436581550662745169L;
	public static final String ENTREE_PROPERTY = "entree";
    public static final String EXPIRATION_PATENTE_PROPERTY = "expirationPatente";
    public static final String MOTIF_SORTIE_PROPERTY = "motifSortie";
    public static final String NUMERO_PATENTE_PROPERTY = "numeroPatente";
    public static final String NUMERO_RUAM_PROPERTY = "numeroRUAM";
    public static final String SORTIE_PROPERTY = "sortie";
    public static final String ASSISTANTE_MATERNELLE_PROPERTY = "assistanteMaternelle";

    public static final String ID_PK_COLUMN = "id";

    public void setEntree(java.util.Date entree) {
        writeProperty("entree", entree);
    }
    public java.util.Date getEntree() {
        return (java.util.Date)readProperty("entree");
    }
    
    
    public void setExpirationPatente(java.util.Date expirationPatente) {
        writeProperty("expirationPatente", expirationPatente);
    }
    public java.util.Date getExpirationPatente() {
        return (java.util.Date)readProperty("expirationPatente");
    }
    
    
    public void setMotifSortie(String motifSortie) {
        writeProperty("motifSortie", motifSortie);
    }
    public String getMotifSortie() {
        return (String)readProperty("motifSortie");
    }
    
    
    public void setNumeroPatente(String numeroPatente) {
        writeProperty("numeroPatente", numeroPatente);
    }
    public String getNumeroPatente() {
        return (String)readProperty("numeroPatente");
    }
    
    
    public void setNumeroRUAM(String numeroRUAM) {
        writeProperty("numeroRUAM", numeroRUAM);
    }
    public String getNumeroRUAM() {
        return (String)readProperty("numeroRUAM");
    }
    
    
    public void setSortie(java.util.Date sortie) {
        writeProperty("sortie", sortie);
    }
    public java.util.Date getSortie() {
        return (java.util.Date)readProperty("sortie");
    }
    
    
    public void setAssistanteMaternelle(nc.ccas.gasel.model.pe.AssistanteMaternelle assistanteMaternelle) {
        setToOneTarget("assistanteMaternelle", assistanteMaternelle, true);
    }

    public nc.ccas.gasel.model.pe.AssistanteMaternelle getAssistanteMaternelle() {
        return (nc.ccas.gasel.model.pe.AssistanteMaternelle)readProperty("assistanteMaternelle");
    } 
    
    
}
