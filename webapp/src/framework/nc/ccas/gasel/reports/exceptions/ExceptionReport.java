package nc.ccas.gasel.reports.exceptions;

import nc.ccas.gasel.LoginData;

import org.apache.tapestry.IPage;

public interface ExceptionReport {

	/**
	 * Signals an exception to report.
	 * 
	 * @param exception
	 *            The exception to report.
	 * @param page
	 *            The current page.
	 * @param login
	 *            The data about the logged user.
	 */
	public void reportException(Throwable exception, IPage page, LoginData login);

}
