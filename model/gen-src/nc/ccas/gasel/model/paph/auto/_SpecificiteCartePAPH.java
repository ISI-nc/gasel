package nc.ccas.gasel.model.paph.auto;

/** Class _SpecificiteCartePAPH was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _SpecificiteCartePAPH extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -359221051076731897L;
	public static final String DOSSIER_PROPERTY = "dossier";
    public static final String SPECIFICITE_PROPERTY = "specificite";

    public static final String DOSSIER_ID_PK_COLUMN = "dossier_id";
    public static final String SPECIFICITE_ID_PK_COLUMN = "specificite_id";

    public void setDossier(nc.ccas.gasel.model.paph.DossierPAPH dossier) {
        setToOneTarget("dossier", dossier, true);
    }

    public nc.ccas.gasel.model.paph.DossierPAPH getDossier() {
        return (nc.ccas.gasel.model.paph.DossierPAPH)readProperty("dossier");
    } 
    
    
    public void setSpecificite(nc.ccas.gasel.model.paph.enums.SpecificiteCarteHand specificite) {
        setToOneTarget("specificite", specificite, true);
    }

    public nc.ccas.gasel.model.paph.enums.SpecificiteCarteHand getSpecificite() {
        return (nc.ccas.gasel.model.paph.enums.SpecificiteCarteHand)readProperty("specificite");
    } 
    
    
}
