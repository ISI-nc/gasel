package nc.ccas.gasel.workflow;

import java.io.Serializable;

/**
 * Implementation of Synchronizer Token J2EE Patterns for Tapestry.
 * 
 * Inclusion in Tapestry page template: <code>
 * &lt;input jwcid="@(protected)"
 value="ognl:visit.flowSynchronizer.token"/&gt;
 * </code>
 * 
 * In case of a 2nd form in the same page: <code>
 * &lt;input jwcid="@Hidden"
 * value="ognl:flowSynchronizer.tokenCopy"/&gt;
 * </code>
 */
public class FlowSynchroniser implements Serializable {
	private static final long serialVersionUID = 4168734562478L;

	private long sequence;
	private String token;

	public FlowSynchroniser() {
		// initialize the sequence randomly
		sequence = (long) (Math.random() * Long.MAX_VALUE / 2l);
	}

	public String getToken() {
		// generate a new token
		token = Long.toHexString(++sequence);
		return token;
	}

	public String getTokenCopy() {
		return token;
	}

	public void setToken(String token) throws FlowSequenceException {
		// first compare the token
		if (this.token == null || !this.token.equals(token))
			throw new FlowSequenceException();
		// reset token on match -> subsequent duplicate submission will fail
		this.token = null;
	}

	public void setTokenCopy(String tokenCopy) throws FlowSequenceException {
		setToken(token);
	}

}
