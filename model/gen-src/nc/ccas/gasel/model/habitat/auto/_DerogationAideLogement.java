package nc.ccas.gasel.model.habitat.auto;

/** Class _DerogationAideLogement was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _DerogationAideLogement extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4506890595983609992L;
	public static final String DEBUT_PROPERTY = "debut";
    public static final String FIN_PROPERTY = "fin";
    public static final String MONTANT_PROPERTY = "montant";
    public static final String MOTIF_PROPERTY = "motif";

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
    
    
    public void setMontant(Integer montant) {
        writeProperty("montant", montant);
    }
    public Integer getMontant() {
        return (Integer)readProperty("montant");
    }
    
    
    public void setMotif(nc.ccas.gasel.model.habitat.enums.MotifDerogationHabitat motif) {
        setToOneTarget("motif", motif, true);
    }

    public nc.ccas.gasel.model.habitat.enums.MotifDerogationHabitat getMotif() {
        return (nc.ccas.gasel.model.habitat.enums.MotifDerogationHabitat)readProperty("motif");
    } 
    
    
}
