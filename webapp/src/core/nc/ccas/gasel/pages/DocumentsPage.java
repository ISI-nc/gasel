package nc.ccas.gasel.pages;

import static nc.ccas.gasel.modelUtils.CommonQueries.unique;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.model.core.docs.RefModeleDocument;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.pages.pi.RefDescVarsDocument;
import nc.ccas.gasel.services.doc.DocumentCheckResult;
import nc.ccas.gasel.services.doc.XmlDocChecker;

import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;

public abstract class DocumentsPage extends BasePage {

	public static void printTableCode(Iterable<RefDescVarsDocument> descs) {
		// XXX Genère le code Tapestry car Document ne marche pas dans un For.
		for (RefDescVarsDocument desc : descs) {
			String key = desc.getKey();
			System.out.println("		<tr>");
			System.out.println("			<td class=\"label\"><span"
					+ " jwcid=\"@Insert\" value=\"ognl:label(\'" + key
					+ "\')\"/>&nbsp;:</td>");
			System.out.println("			<td><input"
					+ " jwcid=\"@core/Document\" document=\"ognl:ref(\'" + key
					+ "\').modele\" /></td>");
			System.out.println("		</tr>");
		}
	}

	public abstract RefDescVarsDocument getDesc();

	public abstract void setDesc(RefDescVarsDocument desc);

	@Persist("workflow")
	@InitialValue("new java.util.TreeMap()")
	public abstract Map<String, RefModeleDocument> getRefs();

	public abstract List<RefDescVarsDocument> getDescs();

	public void enregistrerEtFermer() {
		enregistrer();
		workflowClose();
	}

	public void enregistrer() {
		if (getCheckResult().errors().size() > 0)
			redirect();

		getObjectContext().commitChanges();
	}

	public DocumentCheckResult getCheckResult() {
		DocumentCheckResult res = new DocumentCheckResult();

		for (RefDescVarsDocument desc : getDescs()) {

			RefModeleDocument ref = getRefDocument(desc.getKey());
			if (ref.getModele() == null) {
				res.error(desc.getDescription() + " : indéfini.");
				continue;
			}

			ModeleDocument modele = ref.getModele();

			DocumentCheckResult checkResult = XmlDocChecker.INSTANCE
					.checkDocument(modele.getData(), desc.getVars());

			for (String error : checkResult.errors())
				res.error(desc.getDescription() + " : " + error);

			// for (String warning : checkResult.warnings())
			// res.warning(desc.getDescription() + " : " + warning);
		}
		return res;
	}

	public String label(String key) {
		for (RefDescVarsDocument desc : getDescs()) {
			if (desc.getKey().equals(key))
				return desc.getDescription();
		}
		return null;
	}

	public RefModeleDocument ref(String key) {
		return getRefDocument(key);
	}

	public Collection<String> variables(String key) {
		for (RefDescVarsDocument desc : getDescs()) {
			if (desc.getKey().equals(key))
				return desc.getVars();
		}
		return null;
	}

	public RefModeleDocument getRefDocument(String key) {
		if (!getRefs().containsKey(key)) {
			DataContext context = getObjectContext();
			RefModeleDocument ref = findRef(context, key);
			if (ref == null) {
				createRef(context, key);
				ref = findRef(context, key);
			}
			getRefs().put(key, ref);
		}
		return getRefs().get(key);
	}

	private void createRef(DataContext context, String key) {
		synchronized (RefModeleDocument.class) {
			RefModeleDocument ref = findRef(context, key);
			if (ref != null)
				return;

			DataContext dc = CayenneUtils.createDataContext();

			ModeleDocument doc = (ModeleDocument) dc
					.newObject(ModeleDocument.class);
			doc.setEmpty();

			ref = (RefModeleDocument) dc.newObject(RefModeleDocument.class);
			ref.setKey(key);
			ref.setModele(doc);

			dc.commitChanges();
		}
	}

	private RefModeleDocument findRef(DataContext context, String key) {
		return unique(context, RefModeleDocument.class, "key", key);
	}

}
