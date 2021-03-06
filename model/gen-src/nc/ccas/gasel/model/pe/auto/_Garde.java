package nc.ccas.gasel.model.pe.auto;

/** Class _Garde was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _Garde extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6797416680567354159L;
	public static final String DEBUT_PROPERTY = "debut";
    public static final String FIN_PROPERTY = "fin";
    public static final String ORDRE_PROPERTY = "ordre";
    public static final String ASSISTANTE_MATERNELLE_PROPERTY = "assistanteMaternelle";
    public static final String ENFANT_PROPERTY = "enfant";
    public static final String MODE_PROPERTY = "mode";

    public static final String ID_PK_COLUMN = "id";

    public void setDebut(java.util.Date debut) {
        writeProperty("debut", debut);
    }
    public java.util.Date getDebut() {
        return (java.util.Date)readProperty("debut");
    }
    
    
    public void setFin(java.util.Date fin) {
        writeProperty("fin", fin);
    }
    public java.util.Date getFin() {
        return (java.util.Date)readProperty("fin");
    }
    
    
    public void setOrdre(Integer ordre) {
        writeProperty("ordre", ordre);
    }
    public Integer getOrdre() {
        return (Integer)readProperty("ordre");
    }
    
    
    public void setAssistanteMaternelle(nc.ccas.gasel.model.pe.AssistanteMaternelle assistanteMaternelle) {
        setToOneTarget("assistanteMaternelle", assistanteMaternelle, true);
    }

    public nc.ccas.gasel.model.pe.AssistanteMaternelle getAssistanteMaternelle() {
        return (nc.ccas.gasel.model.pe.AssistanteMaternelle)readProperty("assistanteMaternelle");
    } 
    
    
    public void setEnfant(nc.ccas.gasel.model.pe.EnfantRAM enfant) {
        setToOneTarget("enfant", enfant, true);
    }

    public nc.ccas.gasel.model.pe.EnfantRAM getEnfant() {
        return (nc.ccas.gasel.model.pe.EnfantRAM)readProperty("enfant");
    } 
    
    
    public void setMode(nc.ccas.gasel.model.pe.enums.ModeGarde mode) {
        setToOneTarget("mode", mode, true);
    }

    public nc.ccas.gasel.model.pe.enums.ModeGarde getMode() {
        return (nc.ccas.gasel.model.pe.enums.ModeGarde)readProperty("mode");
    } 
    
    
}
