package nc.ccas.gasel.bindings;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.AbstractBindingFactory;

public class EnumerationBindingFactory extends AbstractBindingFactory {

	public IBinding createBinding(IComponent root, String bindingDescription,
			String expression, Location location) {
		return new EnumerationBinding(expression, bindingDescription,
				getValueConverter(), location);
	}

}
