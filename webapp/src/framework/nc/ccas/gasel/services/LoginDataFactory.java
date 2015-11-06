package nc.ccas.gasel.services;

import javax.servlet.http.HttpServletRequest;

import nc.ccas.gasel.LoginData;

import org.apache.tapestry.engine.state.StateObjectFactory;

public class LoginDataFactory implements StateObjectFactory {

	private HttpServletRequest request;

	@Override
	public Object createStateObject() {
		LoginData data = new LoginData();
		data.setRequest(request);
		return data;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
