package nc.ccas.gasel.model.v1.auto;

import java.util.List;

import nc.ccas.gasel.model.v1.AideV1;

/** Class _TypeAideV1 was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _TypeAideV1 extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4581627370153670973L;
	public static final String CODE_PROPERTY = "code";
    public static final String DATE_MODIFICATION_PROPERTY = "dateModification";
    public static final String USER_ID_MODIF_PROPERTY = "userIdModif";
    public static final String ARTICLE_PROPERTY = "article";
    public static final String DEPEND_DE_IMPUTATION_PROPERTY = "dependDeImputation";
    public static final String SECTEUR_AIDE_PROPERTY = "secteurAide";
    public static final String T_AIDE_ARRAY_PROPERTY = "tAideArray";
    public static final String TYPE_PUBLIC_PROPERTY = "typePublic";

    public static final String T_TYPEAIDE_ID_PK_COLUMN = "t_typeaide_id";

    public void setCode(String code) {
        writeProperty("code", code);
    }
    public String getCode() {
        return (String)readProperty("code");
    }
    
    
    public void setDateModification(java.util.Date dateModification) {
        writeProperty("dateModification", dateModification);
    }
    public java.util.Date getDateModification() {
        return (java.util.Date)readProperty("dateModification");
    }
    
    
    public void setUserIdModif(String userIdModif) {
        writeProperty("userIdModif", userIdModif);
    }
    public String getUserIdModif() {
        return (String)readProperty("userIdModif");
    }
    
    
    public void setArticle(nc.ccas.gasel.model.v1.ArticlePrestationV1 article) {
        setToOneTarget("article", article, true);
    }

    public nc.ccas.gasel.model.v1.ArticlePrestationV1 getArticle() {
        return (nc.ccas.gasel.model.v1.ArticlePrestationV1)readProperty("article");
    } 
    
    
    public void setDependDeImputation(nc.ccas.gasel.model.v1.ImputationV1 dependDeImputation) {
        setToOneTarget("dependDeImputation", dependDeImputation, true);
    }

    public nc.ccas.gasel.model.v1.ImputationV1 getDependDeImputation() {
        return (nc.ccas.gasel.model.v1.ImputationV1)readProperty("dependDeImputation");
    } 
    
    
    public void setSecteurAide(nc.ccas.gasel.model.v1.SecteurAideV1 secteurAide) {
        setToOneTarget("secteurAide", secteurAide, true);
    }

    public nc.ccas.gasel.model.v1.SecteurAideV1 getSecteurAide() {
        return (nc.ccas.gasel.model.v1.SecteurAideV1)readProperty("secteurAide");
    } 
    
    
    public void addToTAideArray(nc.ccas.gasel.model.v1.AideV1 obj) {
        addToManyTarget("tAideArray", obj, true);
    }
    public void removeFromTAideArray(nc.ccas.gasel.model.v1.AideV1 obj) {
        removeToManyTarget("tAideArray", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.v1.AideV1> getTAideArray() {
        return (List<AideV1>)readProperty("tAideArray");
    }
    
    
    public void setTypePublic(nc.ccas.gasel.model.v1.TypePublicV1 typePublic) {
        setToOneTarget("typePublic", typePublic, true);
    }

    public nc.ccas.gasel.model.v1.TypePublicV1 getTypePublic() {
        return (nc.ccas.gasel.model.v1.TypePublicV1)readProperty("typePublic");
    } 
    
    
}
