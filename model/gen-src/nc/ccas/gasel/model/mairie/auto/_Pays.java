package nc.ccas.gasel.model.mairie.auto;

import java.util.List;

import nc.ccas.gasel.model.mairie.CodePostalEtranger;
import nc.ccas.gasel.model.mairie.VilleEtrangere;

/** Class _Pays was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _Pays extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5356338347178192583L;
	public static final String LIBNAT_PROPERTY = "libnat";
    public static final String LIBPAY_PROPERTY = "libpay";
    public static final String CODES_POSTAUX_PROPERTY = "codesPostaux";
    public static final String VILLES_PROPERTY = "villes";

    public static final String CODPAY_PK_COLUMN = "codpay";

    public void setLibnat(String libnat) {
        writeProperty("libnat", libnat);
    }
    public String getLibnat() {
        return (String)readProperty("libnat");
    }
    
    
    public void setLibpay(String libpay) {
        writeProperty("libpay", libpay);
    }
    public String getLibpay() {
        return (String)readProperty("libpay");
    }
    
    
    public void addToCodesPostaux(nc.ccas.gasel.model.mairie.CodePostalEtranger obj) {
        addToManyTarget("codesPostaux", obj, true);
    }
    public void removeFromCodesPostaux(nc.ccas.gasel.model.mairie.CodePostalEtranger obj) {
        removeToManyTarget("codesPostaux", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.mairie.CodePostalEtranger> getCodesPostaux() {
        return (List<CodePostalEtranger>)readProperty("codesPostaux");
    }
    
    
    public void addToVilles(nc.ccas.gasel.model.mairie.VilleEtrangere obj) {
        addToManyTarget("villes", obj, true);
    }
    public void removeFromVilles(nc.ccas.gasel.model.mairie.VilleEtrangere obj) {
        removeToManyTarget("villes", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.mairie.VilleEtrangere> getVilles() {
        return (List<VilleEtrangere>)readProperty("villes");
    }
    
    
}
