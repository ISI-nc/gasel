package nc.ccas.gasel.model.v1.auto;

import java.util.List;

import nc.ccas.gasel.model.v1.BudgetTypePublicV1;

/** Class _BudgetSecteurAideV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _BudgetSecteurAideV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6767994832362873936L;
	public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String MONTANT_PREVU_PROPERTY = "montantPrevu";
    public static final String MONTANT_UTILISE_PROPERTY = "montantUtilise";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String BUDGET_PROPERTY = "Budget";
    public static final String DANS_SECTEUR_AIDE_PROPERTY = "dansSecteurAide";
    public static final String EST_DANS_BUDGET_IMPUTATION_PROPERTY = "estDansBudgetImputation";
    public static final String T_BUDGETTYPEPUBLIC_ARRAY_PROPERTY = "tBudgettypepublicArray";

    public static final String T_BUDGET_ID_PK_COLUMN = "t_budget_id";

    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setMontantPrevu(Double montantPrevu) {
        writeProperty("montantPrevu", montantPrevu);
    }
    public Double getMontantPrevu() {
        return (Double)readProperty("montantPrevu");
    }
    
    
    public void setMontantUtilise(Double montantUtilise) {
        writeProperty("montantUtilise", montantUtilise);
    }
    public Double getMontantUtilise() {
        return (Double)readProperty("montantUtilise");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void setBudget(nc.ccas.gasel.model.v1.BudgetV1 Budget) {
        setToOneTarget("Budget", Budget, true);
    }

    public nc.ccas.gasel.model.v1.BudgetV1 getBudget() {
        return (nc.ccas.gasel.model.v1.BudgetV1)readProperty("Budget");
    } 
    
    
    public void setDansSecteurAide(nc.ccas.gasel.model.v1.SecteurAideV1 dansSecteurAide) {
        setToOneTarget("dansSecteurAide", dansSecteurAide, true);
    }

    public nc.ccas.gasel.model.v1.SecteurAideV1 getDansSecteurAide() {
        return (nc.ccas.gasel.model.v1.SecteurAideV1)readProperty("dansSecteurAide");
    } 
    
    
    public void setEstDansBudgetImputation(nc.ccas.gasel.model.v1.BudgetImputationV1 estDansBudgetImputation) {
        setToOneTarget("estDansBudgetImputation", estDansBudgetImputation, true);
    }

    public nc.ccas.gasel.model.v1.BudgetImputationV1 getEstDansBudgetImputation() {
        return (nc.ccas.gasel.model.v1.BudgetImputationV1)readProperty("estDansBudgetImputation");
    } 
    
    
    public void addToTBudgettypepublicArray(nc.ccas.gasel.model.v1.BudgetTypePublicV1 obj) {
        addToManyTarget("tBudgettypepublicArray", obj, true);
    }
    public void removeFromTBudgettypepublicArray(nc.ccas.gasel.model.v1.BudgetTypePublicV1 obj) {
        removeToManyTarget("tBudgettypepublicArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.BudgetTypePublicV1> getTBudgettypepublicArray() {
        return (List<BudgetTypePublicV1>)readProperty("tBudgettypepublicArray");
    }
    
    
}
