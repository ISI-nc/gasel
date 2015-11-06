package nc.ccas.gasel.model.v1.auto;

/** Class _RessourceV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _RessourceV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2936103918849445727L;
	public static final String DATE_DEBUT_PROPERTY = "dateDebut";
    public static final String DATE_FIN_PROPERTY = "dateFin";
    public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String MONTANT_PROPERTY = "montant";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String EST_DEFINIE_PAR_PROPERTY = "estDefiniePar";
    public static final String THE_ADMINISTRE_PROPERTY = "theAdministre";
    public static final String TO_TRESSOURCELOGEMENT_PROPERTY = "toTRessourcelogement";

    public static final String T_RESSOURCE_ID_PK_COLUMN = "t_ressource_id";

    public void setDateDebut(java.util.Date dateDebut) {
        writeProperty("dateDebut", dateDebut);
    }
    public java.util.Date getDateDebut() {
        return (java.util.Date)readProperty("dateDebut");
    }
    
    
    public void setDateFin(java.util.Date dateFin) {
        writeProperty("dateFin", dateFin);
    }
    public java.util.Date getDateFin() {
        return (java.util.Date)readProperty("dateFin");
    }
    
    
    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setMontant(Double montant) {
        writeProperty("montant", montant);
    }
    public Double getMontant() {
        return (Double)readProperty("montant");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void setEstDefiniePar(nc.ccas.gasel.model.v1.TypeRessourceV1 estDefiniePar) {
        setToOneTarget("estDefiniePar", estDefiniePar, true);
    }

    public nc.ccas.gasel.model.v1.TypeRessourceV1 getEstDefiniePar() {
        return (nc.ccas.gasel.model.v1.TypeRessourceV1)readProperty("estDefiniePar");
    } 
    
    
    public void setTheAdministre(nc.ccas.gasel.model.v1.AdministreV1 theAdministre) {
        setToOneTarget("theAdministre", theAdministre, true);
    }

    public nc.ccas.gasel.model.v1.AdministreV1 getTheAdministre() {
        return (nc.ccas.gasel.model.v1.AdministreV1)readProperty("theAdministre");
    } 
    
    
    public void setToTRessourcelogement(nc.ccas.gasel.model.v1.RessourceLogementV1 toTRessourcelogement) {
        setToOneTarget("toTRessourcelogement", toTRessourcelogement, true);
    }

    public nc.ccas.gasel.model.v1.RessourceLogementV1 getToTRessourcelogement() {
        return (nc.ccas.gasel.model.v1.RessourceLogementV1)readProperty("toTRessourcelogement");
    } 
    
    
}
