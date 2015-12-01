package nc.ccas.gasel.services;

import java.io.IOException;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.bindings.CayenneAllBinding;

import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.services.WebRequestServicerFilter;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

public class GaselServicerFilter implements WebRequestServicerFilter {

	public void service(WebRequest request, WebResponse response,
			WebRequestServicer servicer) throws IOException {
		// DataUpdateAgent.initialize();

		try {
			servicer.service(request, response);

		} finally {
			// Clean the context up
			CayenneAllBinding.clearCache();
			BasePage.getRequestCache().clear();
		}
	}

}
