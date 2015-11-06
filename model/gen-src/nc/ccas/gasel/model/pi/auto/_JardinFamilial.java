package nc.ccas.gasel.model.pi.auto;

import java.util.List;

import nc.ccas.gasel.model.pi.DemandeJF;
import nc.ccas.gasel.model.pi.Parcelle;

/** Class _JardinFamilial was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _JardinFamilial extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4671770408570423166L;
	public static final String NOM_PROPERTY = "nom";
    public static final String ARRETE_ATTRIBUTION_PROPERTY = "arreteAttribution";
    public static final String ARRETE_RENOUVELLEMENT_PROPERTY = "arreteRenouvellement";
    public static final String COURIER_ENTRETIEN_PROPERTY = "courierEntretien";
    public static final String COURIER_PAIEMENT_PROPERTY = "courierPaiement";
    public static final String COURIER_PAIEMENT_ENTRETIEN_PROPERTY = "courierPaiementEntretien";
    public static final String DEMANDES_PROPERTY = "demandes";
    public static final String LIEU_PROPERTY = "lieu";
    public static final String PARCELLES_PROPERTY = "parcelles";

    public static final String ID_PK_COLUMN = "id";

    public void setNom(String nom) {
        writeProperty("nom", nom);
    }
    public String getNom() {
        return (String)readProperty("nom");
    }
    
    
    public void setArreteAttribution(nc.ccas.gasel.model.core.docs.ModeleDocument arreteAttribution) {
        setToOneTarget("arreteAttribution", arreteAttribution, true);
    }

    public nc.ccas.gasel.model.core.docs.ModeleDocument getArreteAttribution() {
        return (nc.ccas.gasel.model.core.docs.ModeleDocument)readProperty("arreteAttribution");
    } 
    
    
    public void setArreteRenouvellement(nc.ccas.gasel.model.core.docs.ModeleDocument arreteRenouvellement) {
        setToOneTarget("arreteRenouvellement", arreteRenouvellement, true);
    }

    public nc.ccas.gasel.model.core.docs.ModeleDocument getArreteRenouvellement() {
        return (nc.ccas.gasel.model.core.docs.ModeleDocument)readProperty("arreteRenouvellement");
    } 
    
    
    public void setCourierEntretien(nc.ccas.gasel.model.core.docs.ModeleDocument courierEntretien) {
        setToOneTarget("courierEntretien", courierEntretien, true);
    }

    public nc.ccas.gasel.model.core.docs.ModeleDocument getCourierEntretien() {
        return (nc.ccas.gasel.model.core.docs.ModeleDocument)readProperty("courierEntretien");
    } 
    
    
    public void setCourierPaiement(nc.ccas.gasel.model.core.docs.ModeleDocument courierPaiement) {
        setToOneTarget("courierPaiement", courierPaiement, true);
    }

    public nc.ccas.gasel.model.core.docs.ModeleDocument getCourierPaiement() {
        return (nc.ccas.gasel.model.core.docs.ModeleDocument)readProperty("courierPaiement");
    } 
    
    
    public void setCourierPaiementEntretien(nc.ccas.gasel.model.core.docs.ModeleDocument courierPaiementEntretien) {
        setToOneTarget("courierPaiementEntretien", courierPaiementEntretien, true);
    }

    public nc.ccas.gasel.model.core.docs.ModeleDocument getCourierPaiementEntretien() {
        return (nc.ccas.gasel.model.core.docs.ModeleDocument)readProperty("courierPaiementEntretien");
    } 
    
    
    public void addToDemandes(nc.ccas.gasel.model.pi.DemandeJF obj) {
        addToManyTarget("demandes", obj, true);
    }
    public void removeFromDemandes(nc.ccas.gasel.model.pi.DemandeJF obj) {
        removeToManyTarget("demandes", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.pi.DemandeJF> getDemandes() {
        return (List<DemandeJF>)readProperty("demandes");
    }
    
    
    public void setLieu(nc.ccas.gasel.model.pi.Lieu lieu) {
        setToOneTarget("lieu", lieu, true);
    }

    public nc.ccas.gasel.model.pi.Lieu getLieu() {
        return (nc.ccas.gasel.model.pi.Lieu)readProperty("lieu");
    } 
    
    
    public void addToParcelles(nc.ccas.gasel.model.pi.Parcelle obj) {
        addToManyTarget("parcelles", obj, true);
    }
    public void removeFromParcelles(nc.ccas.gasel.model.pi.Parcelle obj) {
        removeToManyTarget("parcelles", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.pi.Parcelle> getParcelles() {
        return (List<Parcelle>)readProperty("parcelles");
    }
    
    
}
