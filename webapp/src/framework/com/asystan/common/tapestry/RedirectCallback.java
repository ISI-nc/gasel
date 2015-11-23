/*
 * Créé le 26 déc. 2004
 */
package com.asystan.common.tapestry;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * @author Mikaël Cluseau
 */
public class RedirectCallback implements ICallback {

	private static final long serialVersionUID = 3616727188385575224L;

	public static RedirectCallback createPage(IPage page, IRequestCycle cycle) {
		return createPage(page.getPageName(), cycle);
	}

	public static RedirectCallback createPage(String pageName,
			IRequestCycle cycle) {
		return new RedirectCallback(cycle, Tapestry.PAGE_SERVICE, pageName);
	}

	public static RedirectCallback createExternal(IExternalPage page,
			Object[] parameters, IRequestCycle cycle) {
		ExternalServiceParameter parameter = new ExternalServiceParameter(page
				.getPageName(), parameters);
		return new RedirectCallback(cycle, Tapestry.EXTERNAL_SERVICE, parameter);
	}

	public static RedirectCallback createExternal(IExternalPage page,
			IRequestCycle cycle, Object... parameters) {
		return createExternal(page, parameters, cycle);
	}

	public static RedirectCallback createExternal(String pageName,
			IRequestCycle cycle, Object... parameters) {
		return createExternal((IExternalPage) cycle.getPage(pageName),
				parameters, cycle);
	}

	public static RedirectCallback createService(String serviceName,
			IRequestCycle cycle, Object... parameters) {
		return new RedirectCallback(cycle, serviceName, parameters);
	}

	public static RedirectCallback createService(IEngineService service,
			IRequestCycle cycle, Object... parameters) {
		return new RedirectCallback(cycle, service, parameters);
	}

	public static RedirectCallback createService(IRequestCycle cycle) {
		return new RedirectCallback(cycle, cycle.getService(), cycle
				.getListenerParameters());
	}

	public static RedirectCallback createHome(IRequestCycle cycle) {
		return createService(Tapestry.HOME_SERVICE, cycle, (Object[]) null);
	}

	/*
	 * 
	 */

	private String url;

	private RedirectCallback() {
		url = null;
	}

	public RedirectCallback(String url) {
		this.url = url;
	}

	public RedirectCallback(ILink link) {
		this(link, false);
	}

	public RedirectCallback(ILink link, boolean absolute) {
		url = absolute ? link.getAbsoluteURL() : link.getURL();
	}

	public RedirectCallback(IRequestCycle cycle, IEngineService service,
			Object parameters) {
		url = service.getLink(false, parameters).getURL();
	}

	public RedirectCallback(IRequestCycle cycle, String service,
			Object parameters) {
		this(cycle, cycle.getInfrastructure().getServiceMap().getService(
				service), parameters);
	}

	public void performCallback(IRequestCycle cycle) {
		throw new RedirectException(url);
	}

	public String toString() {
		return super.toString() + ": " + url;
	}

	public void readObject(ObjectInput arg0) throws IOException,
			ClassNotFoundException {
		url = (String) arg0.readObject();
	}

	public void writeObject(ObjectOutput arg0) throws IOException {
		arg0.writeObject(url);
	}

	public String getUrl() {
		return url;
	}
}
