package nc.ccas.gasel.model.budget.auto;

import java.util.List;

import nc.ccas.gasel.model.budget.BudgetImpMensuel;

/** Class _BudgetImpAnnuel was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _BudgetImpAnnuel extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3333289471734855421L;
	public static final String MONTANT_PROPERTY = "montant";
    public static final String REPARTITION_PAR_SECTEUR_PROPERTY = "repartitionParSecteur";
    public static final String RESTANT_PROPERTY = "restant";
    public static final String IMPUTATION_PROPERTY = "imputation";
    public static final String MENSUELS_PROPERTY = "mensuels";
    public static final String PARENT_PROPERTY = "parent";

    public static final String ID_PK_COLUMN = "id";

    public void setMontant(Double montant) {
        writeProperty("montant", montant);
    }
    public Double getMontant() {
        return (Double)readProperty("montant");
    }
    
    
    public void setRepartitionParSecteur(Boolean repartitionParSecteur) {
        writeProperty("repartitionParSecteur", repartitionParSecteur);
    }
    public Boolean getRepartitionParSecteur() {
        return (Boolean)readProperty("repartitionParSecteur");
    }
    
    
    public void setRestant(Double restant) {
        writeProperty("restant", restant);
    }
    public Double getRestant() {
        return (Double)readProperty("restant");
    }
    
    
    public void setImputation(nc.ccas.gasel.model.budget.Imputation imputation) {
        setToOneTarget("imputation", imputation, true);
    }

    public nc.ccas.gasel.model.budget.Imputation getImputation() {
        return (nc.ccas.gasel.model.budget.Imputation)readProperty("imputation");
    } 
    
    
    public void addToMensuels(nc.ccas.gasel.model.budget.BudgetImpMensuel obj) {
        addToManyTarget("mensuels", obj, true);
    }
    public void removeFromMensuels(nc.ccas.gasel.model.budget.BudgetImpMensuel obj) {
        removeToManyTarget("mensuels", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.budget.BudgetImpMensuel> getMensuels() {
        return (List<BudgetImpMensuel>)readProperty("mensuels");
    }
    
    
    public void setParent(nc.ccas.gasel.model.budget.BudgetAnnuel parent) {
        setToOneTarget("parent", parent, true);
    }

    public nc.ccas.gasel.model.budget.BudgetAnnuel getParent() {
        return (nc.ccas.gasel.model.budget.BudgetAnnuel)readProperty("parent");
    } 
    
    
}
