package nc.ccas.gasel.bindings;

import ognl.OgnlException;

import org.apache.hivemind.Location;
import org.apache.log4j.Logger;
import org.apache.tapestry.IBinding;

public class ProtectedBinding implements IBinding {

	private static final Logger LOG = Logger.getLogger(ProtectedBinding.class);

	private final IBinding binding;

	public ProtectedBinding(IBinding binding) {
		this.binding = binding;
	}

	private void handle(Exception ex) {
		Throwable root = ex;
		while (root.getCause() != null)
			root = root.getCause();

		// if (root instanceof NullPointerException
		// || root instanceof ArrayIndexOutOfBoundsException) {
		if (root instanceof OgnlException
				&& root.getMessage().startsWith("source is null")) {
			LOG.debug(ex);
			return;
		}
		LOG.warn(ex); // throw new RuntimeException(ex);
	}

	public Object getObject() {
		try {
			return binding.getObject();
		} catch (Exception ex) {
			handle(ex);
			return null;
		}
	}

	public Object getObject(@SuppressWarnings("rawtypes") Class type) {
		try {
			return binding.getObject(type);
		} catch (Exception ex) {
			handle(ex);
			return null;
		}
	}

	// ------------------------------------------------------------------------

	public void setObject(Object value) {
		binding.setObject(value);
	}

	public String getDescription() {
		return binding.getDescription();
	}

	public Location getLocation() {
		return binding.getLocation();
	}

	public boolean isInvariant() {
		return binding.isInvariant();
	}

}
