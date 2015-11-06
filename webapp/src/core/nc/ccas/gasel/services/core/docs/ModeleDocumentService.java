package nc.ccas.gasel.services.core.docs;

import static org.apache.cayenne.DataObjectUtils.intPKForObject;
import static org.apache.cayenne.DataObjectUtils.objectForPK;
import static nc.ccas.gasel.modelUtils.CayenneUtils.createDataContext;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.jwcs.core.Document;
import nc.ccas.gasel.model.core.docs.ModeleDocument;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

public class ModeleDocumentService implements IEngineService {

	public static final ContentType CONTENT_TYPE = new ContentType(
			"application/msword");

	public static final String NAME = "modele_document";

	private LinkFactory _linkFactory;

	private DataSqueezer _squeezer;

	public ILink getLink(boolean post, Object parameter) {
		Object param;
		if (parameter instanceof Object[]) {
			param = ((Object[]) parameter)[0];
		} else {
			param = parameter;
		}

		int docId;
		if (param instanceof Integer) {
			docId = (Integer) param;
		} else {
			docId = intPKForObject((ModeleDocument) param);
		}

		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(ServiceConstants.PARAMETER, new Object[] { docId });
		return _linkFactory.constructLink(this, post, map, true);
	}

	public String getName() {
		return NAME;
	}

	public void service(IRequestCycle cycle) throws IOException {
		int docId = (Integer) _squeezer.unsqueeze(cycle
				.getParameter(ServiceConstants.PARAMETER));
		ModeleDocument document = (ModeleDocument) objectForPK(
				createDataContext(), ModeleDocument.class, docId);

		// Sortie
		WebResponse response = cycle.getInfrastructure().getResponse();
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"document-" + docId + ".doc.xml\"");
		response.getOutputStream(CONTENT_TYPE) //
				.write(document.getData().getBytes(Document.ENCODING));
	}

	// Injected

	public void setLinkFactory(LinkFactory linkFactory) {
		_linkFactory = linkFactory;
	}

	public void setSqueezer(DataSqueezer squeezer) {
		this._squeezer = squeezer;
	}

}
