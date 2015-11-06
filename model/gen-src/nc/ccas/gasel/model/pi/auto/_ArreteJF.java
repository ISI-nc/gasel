package nc.ccas.gasel.model.pi.auto;

/** Class _ArreteJF was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _ArreteJF extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4410801796058498273L;
	public static final String DEBUT_PROPERTY = "debut";
    public static final String FIN_PROPERTY = "fin";
    public static final String NUMERO_PROPERTY = "numero";
    public static final String ATTRIBUTION_PROPERTY = "attribution";
    public static final String TYPE_PROPERTY = "type";

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
    
    
    public void setNumero(String numero) {
        writeProperty("numero", numero);
    }
    public String getNumero() {
        return (String)readProperty("numero");
    }
    
    
    public void setAttribution(nc.ccas.gasel.model.pi.AttributionJF attribution) {
        setToOneTarget("attribution", attribution, true);
    }

    public nc.ccas.gasel.model.pi.AttributionJF getAttribution() {
        return (nc.ccas.gasel.model.pi.AttributionJF)readProperty("attribution");
    } 
    
    
    public void setType(nc.ccas.gasel.model.pi.enums.TypeArreteJF type) {
        setToOneTarget("type", type, true);
    }

    public nc.ccas.gasel.model.pi.enums.TypeArreteJF getType() {
        return (nc.ccas.gasel.model.pi.enums.TypeArreteJF)readProperty("type");
    } 
    
    
}
