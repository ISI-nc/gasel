package nc.ccas.gasel.model.pi.auto;

/** Class _ControleEntretien was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _ControleEntretien extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6795747962548648351L;
	public static final String DATE_PROPERTY = "date";
    public static final String ATTRIBUTION_PROPERTY = "attribution";
    public static final String ETAT_PARCELLE_PROPERTY = "etatParcelle";

    public static final String ID_PK_COLUMN = "id";

    public void setDate(java.util.Date date) {
        writeProperty("date", date);
    }
    public java.util.Date getDate() {
        return (java.util.Date)readProperty("date");
    }
    
    
    public void setAttribution(nc.ccas.gasel.model.pi.AttributionJF attribution) {
        setToOneTarget("attribution", attribution, true);
    }

    public nc.ccas.gasel.model.pi.AttributionJF getAttribution() {
        return (nc.ccas.gasel.model.pi.AttributionJF)readProperty("attribution");
    } 
    
    
    public void setEtatParcelle(nc.ccas.gasel.model.pi.enums.EtatParcelle etatParcelle) {
        setToOneTarget("etatParcelle", etatParcelle, true);
    }

    public nc.ccas.gasel.model.pi.enums.EtatParcelle getEtatParcelle() {
        return (nc.ccas.gasel.model.pi.enums.EtatParcelle)readProperty("etatParcelle");
    } 
    
    
}
