package nc.ccas.gasel.model.habitat.auto;

import java.util.List;

import nc.ccas.gasel.model.core.enums.ReponseProblematique;

/** Class _ObjectifProbHabitat was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public abstract class _ObjectifProbHabitat extends org.apache.cayenne.CayenneDataObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1703437779156552895L;
	public static final String OBJECTIF_PROPERTY = "objectif";
    public static final String PROBLEMATIQUE_PROPERTY = "problematique";
    public static final String REPONSES_PROPERTY = "reponses";

    public static final String ID_PK_COLUMN = "id";

    public void setObjectif(nc.ccas.gasel.model.habitat.ObjectifHabitat objectif) {
        setToOneTarget("objectif", objectif, true);
    }

    public nc.ccas.gasel.model.habitat.ObjectifHabitat getObjectif() {
        return (nc.ccas.gasel.model.habitat.ObjectifHabitat)readProperty("objectif");
    } 
    
    
    public void setProblematique(nc.ccas.gasel.model.core.enums.Problematique problematique) {
        setToOneTarget("problematique", problematique, true);
    }

    public nc.ccas.gasel.model.core.enums.Problematique getProblematique() {
        return (nc.ccas.gasel.model.core.enums.Problematique)readProperty("problematique");
    } 
    
    
    public void addToReponses(nc.ccas.gasel.model.core.enums.ReponseProblematique obj) {
        addToManyTarget("reponses", obj, true);
    }
    public void removeFromReponses(nc.ccas.gasel.model.core.enums.ReponseProblematique obj) {
        removeToManyTarget("reponses", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<nc.ccas.gasel.model.core.enums.ReponseProblematique> getReponses() {
        return (List<ReponseProblematique>)readProperty("reponses");
    }
    
    
}
