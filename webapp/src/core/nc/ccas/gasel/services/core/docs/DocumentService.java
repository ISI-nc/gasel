package nc.ccas.gasel.services.core.docs;

import static nc.ccas.gasel.services.core.docs.ModeleDocumentService.CONTENT_TYPE;
import static org.apache.cayenne.DataObjectUtils.objectForPK;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.docs.aides.AideEauParams;
import nc.ccas.gasel.docs.aides.AideOMParams;
import nc.ccas.gasel.docs.aides.AideParams;
import nc.ccas.gasel.docs.pi.ArreteJFParams;
import nc.ccas.gasel.jwcs.core.Document;
import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AideEau;
import nc.ccas.gasel.model.aides.AideOM;
import nc.ccas.gasel.model.core.docs.ModeleDocument;
import nc.ccas.gasel.model.pi.ArreteJF;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.services.doc.ParamsProvider;
import nc.ccas.gasel.services.doc.XmlDocChanger;

import org.apache.cayenne.ObjectId;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.access.DataContext;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebResponse;

public class DocumentService implements IEngineService {

	// FIXME Mieux en service HiveMind avec registre...

	private static final Map<Class<?>, ParamsProvider<?>> providers = new HashMap<Class<?>, ParamsProvider<?>>();

	public static <T extends Persistent> void register(Class<T> clazz,
			ParamsProvider<T> provider) {
		providers.put(clazz, provider);
	}

	static {
		register(ArreteJF.class, new ArreteJFParams());
		register(Aide.class, new AideParams());
		register(AideEau.class, new AideEauParams());
		register(AideOM.class, new AideOMParams());
	}

	// ------

	public static final String NAME = "document";

	private LinkFactory _linkFactory;

	private DataSqueezer _squeezer;

	public ILink getLink(boolean post, Object parameter) {
		Object[] params = (Object[]) parameter;
		ModeleDocument doc = (ModeleDocument) params[0];
		Persistent source = (Persistent) params[1];

		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(ServiceConstants.PARAMETER, //
				new Object[] { doc.getObjectId(), source.getObjectId() });
		return _linkFactory.constructLink(this, post, map, true);
	}

	public String getName() {
		return NAME;
	}

	public void service(IRequestCycle cycle) throws IOException {
		String[] params = cycle.getParameters(ServiceConstants.PARAMETER);
		ObjectId docId = (ObjectId) _squeezer.unsqueeze(params[0]);
		ObjectId sourceId = (ObjectId) _squeezer.unsqueeze(params[1]);

		DataContext context = CayenneUtils.createDataContext();
		ModeleDocument modele = (ModeleDocument) objectForPK(context, docId);
		Persistent source = (Persistent) objectForPK(context, sourceId);

		ParamsProvider<?> provider = providers.get(source.getClass());
		if (provider == null)
			throw new RuntimeException("No provider for " + source.getClass());

		// Finalement, on transforme le document avant d'altérer la réponse...
		String result = new XmlDocChanger(modele.getData()) //
				.subst(provider.toParams(source)) //
				.result();

		// Sortie
		WebResponse response = cycle.getInfrastructure().getResponse();
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"document.doc.xml\"");
		OutputStream out = response.getOutputStream(CONTENT_TYPE);
		out.write(result.getBytes(Document.ENCODING));
	}

	// Injected

	public void setLinkFactory(LinkFactory linkFactory) {
		_linkFactory = linkFactory;
	}

	public void setSqueezer(DataSqueezer squeezer) {
		this._squeezer = squeezer;
	}

}
