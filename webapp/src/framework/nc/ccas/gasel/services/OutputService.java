package nc.ccas.gasel.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import nc.ccas.gasel.utils.QuickHashMap;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;

import com.asystan.common.tapestry.RedirectCallback;

public abstract class OutputService implements IEngineService {

	public static final String SESSION_DATA = "service-data";

	public static final String SESSION_VIEW = "service-view";

	public static final ContentType PDF = new ContentType("text/pdf");

	public static final ContentType PLAIN_TEXT = new ContentType("text/plain");

	/**
	 * @param cycle
	 * @return The extension of the file name.
	 */
	public abstract String getExtension(IRequestCycle cycle, Map<String, ?> parameters);

	/**
	 * @param cycle
	 * @param parameters 
	 * @return The content type provided by the service.
	 */
	public abstract ContentType getContentType(IRequestCycle cycle, Map<String, ?> parameters);

	/**
	 * @param viewName
	 *            The name of the requested view.
	 * @return <code>true</code> if the view exists.
	 */
	public abstract boolean doesViewExists(String viewName);

	/**
	 * Performs the rendering of the <code>data</code> through the
	 * <code>view</code>.
	 * 
	 * @param response
	 */
	protected void render(OutputStream output, IRequestCycle cycle,
			String view, Map<String, ?> parameters) throws IOException {
		// skip
	}

	/**
	 * Invoque le service.
	 */
	public static void invoke(IRequestCycle cycle, String name, String view,
			Map<String, ?> parameters) {
		String key = Integer.toString(
				(int) (Math.random() * Integer.MAX_VALUE), Character.MAX_RADIX);

		if (parameters == null) {
			parameters = Collections.emptyMap();
		}

		WebSession session = cycle.getInfrastructure().getRequest() //
				.getSession(true);
		session.setAttribute(keyValue(SESSION_VIEW, key), view);
		session.setAttribute(keyValue(SESSION_DATA, key), parameters);

		RedirectCallback.createService(name, cycle, key).performCallback(cycle);
	}

	private LinkFactory _linkFactory;

	public ILink getLink(boolean post, Object parameter) {
		Object[] params = (Object[]) parameter;
		String key = (String) params[0];
		return _linkFactory
				.constructLink(this, post, new QuickHashMap<String, Object>()
						.put(ServiceConstants.PARAMETER, new Object[] { key })
						.map(), true);
	}

	private static String keyValue(String value, String key) {
		return value + "-" + key;
	}

	public void service(IRequestCycle cycle) throws IOException {
		// Check présence de la session
		WebSession session = cycle.getInfrastructure().getRequest()
				.getSession(false);

		if (session == null || session.isNew())
			throw new StaleSessionException();

		// Récupération de la clé (paramètre du service)
		DataSqueezer squeezer = cycle.getInfrastructure().getDataSqueezer();
		String key = (String) squeezer.unsqueeze(cycle
				.getParameter(ServiceConstants.PARAMETER));
		Defense.notNull(key, "key");

		// Récupération des données
		String view = (String) flashAttr(session, keyValue(SESSION_VIEW, key));
		Defense.notNull(view, SESSION_VIEW);

		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) flashAttr(session,
				keyValue(SESSION_DATA, key));
		Defense.notNull(parameters, SESSION_DATA);

		serviceImpl(cycle, view, parameters);
	}

	protected void serviceImpl(IRequestCycle cycle, String view,
			Map<String, Object> parameters) throws IOException {
		WebResponse response = cycle.getInfrastructure().getResponse();

		if (!doesViewExists(view)) {
			response.setStatus(404);
			OutputStream output = response.getOutputStream(PLAIN_TEXT);
			output.write(("La vue «" + view + "» n'existe pas.").getBytes());
			output.close();
			return;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		render(out, cycle, view, parameters);
		out.close();

		setupResponse(response, view + "." + getExtension(cycle, parameters));
		OutputStream output = response.getOutputStream(getContentType(cycle, parameters));
		try {
			output.write(out.toByteArray());
		} finally {
			output.close();
		}
	}

	protected void setupResponse(WebResponse response, String fileName) {
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
	}

	protected Object flashAttr(WebSession session, String attribute) {
		Object data = session.getAttribute(attribute);
		session.setAttribute(attribute, null);
		return data;
	}

	// Injected

	public void setLinkFactory(LinkFactory linkFactory) {
		_linkFactory = linkFactory;
	}

}
