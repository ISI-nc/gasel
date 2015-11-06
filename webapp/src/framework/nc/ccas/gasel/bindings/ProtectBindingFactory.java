package nc.ccas.gasel.bindings;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.AbstractBindingFactory;
import org.apache.tapestry.binding.BindingFactory;

/**
 * 
 * 
 * @author Mikaël Cluseau
 */
public class ProtectBindingFactory extends AbstractBindingFactory {

	private BindingFactory factory;

	public IBinding createBinding(IComponent root, String bindingDescription,
			String expression, Location location) {
		return new ProtectedBinding(factory.createBinding(root,
				bindingDescription, expression, location));
	}

	public void setDelegate(BindingFactory factory) {
		this.factory = factory;
	}

}
