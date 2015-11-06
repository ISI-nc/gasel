package nc.ccas.gasel.pages;

import nc.ccas.gasel.LoginData;
import nc.ccas.gasel.reports.exceptions.MailExceptionReport;
import nc.ccas.gasel.workflow.FlowSequenceException;
import nc.ccas.gasel.workflow.Workflow;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.util.exception.ExceptionDescription;

public abstract class Exception extends org.apache.tapestry.pages.Exception {

	private boolean flowSequenceError = false;

	private boolean pageNotFound;

	private boolean error;

	@InjectState("workflow")
	public abstract Workflow getWorkflow();

	@InjectState("login")
	public abstract LoginData getLogin();

	public abstract String getMessageBody();

	public abstract void setMessageBody(String messageBody);

	public void workflowClose() {
		getWorkflow().close(getRequestCycle());
	}

	public abstract ExceptionDescription[] getExceptions();

	public abstract Throwable getRootCause();

	public abstract void setRootCause(Throwable ex);

	@Override
	public void setException(Throwable value) {
		super.setException(value);
		Throwable cause = value;
		while (cause.getCause() != null)
			cause = cause.getCause();
		setRootCause(cause);
		// --
		pageNotFound = cause instanceof PageNotFoundException;
		flowSequenceError = cause instanceof FlowSequenceException;
		error = cause instanceof nc.ccas.gasel.cayenne.Error;
		// --
		if (pageNotFound || error || flowSequenceError)
			return;

		IRequestCycle cycle = getRequestCycle();
		String pageName = getWorkflow().getCurrentEntry().getPageName();
		IPage page = cycle.getPage(pageName);
		MailExceptionReport reporter = new MailExceptionReport();
		try {
			reporter.reportException(value, page, getLogin());
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			setMessageBody(reporter.messageBody(value, page));
		}
	}

	public boolean getIsPageNotFound() {
		return pageNotFound;
	}

	public boolean isFlowSequenceError() {
		return flowSequenceError;
	}

	public boolean getIsError() {
		return error;
	}

}
