package nc.ccas.gasel.model.budget.auto;

/** Class _BudgetTypePublic was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _BudgetTypePublic extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8732602550928495562L;
	public static final String MONTANT_PROPERTY = "montant";
    public static final String RESTANT_PROPERTY = "restant";
    public static final String PARENT_PROPERTY = "parent";
    public static final String TYPE_PUBLIC_PROPERTY = "typePublic";

    public static final String ID_PK_COLUMN = "id";

    public void setMontant(Double montant) {
        writeProperty("montant", montant);
    }
    public Double getMontant() {
        return (Double)readProperty("montant");
    }
    
    
    public void setRestant(Double restant) {
        writeProperty("restant", restant);
    }
    public Double getRestant() {
        return (Double)readProperty("restant");
    }
    
    
    public void setParent(nc.ccas.gasel.model.budget.BudgetSecteurAide parent) {
        setToOneTarget("parent", parent, true);
    }

    public nc.ccas.gasel.model.budget.BudgetSecteurAide getParent() {
        return (nc.ccas.gasel.model.budget.BudgetSecteurAide)readProperty("parent");
    } 
    
    
    public void setTypePublic(nc.ccas.gasel.model.core.enums.TypePublic typePublic) {
        setToOneTarget("typePublic", typePublic, true);
    }

    public nc.ccas.gasel.model.core.enums.TypePublic getTypePublic() {
        return (nc.ccas.gasel.model.core.enums.TypePublic)readProperty("typePublic");
    } 
    
    
}
