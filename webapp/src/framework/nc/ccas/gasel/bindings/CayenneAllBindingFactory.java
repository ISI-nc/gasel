package nc.ccas.gasel.bindings;

import org.apache.cayenne.query.SelectQuery;
import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.AbstractBindingFactory;

/**
 * Bindings de type "tous les objets d'une table".
 * 
 * @author nwrk
 * 
 */
public class CayenneAllBindingFactory extends AbstractBindingFactory {

	public IBinding createBinding(IComponent root, String bindingDescription,
			String expression, Location location) {
		return new CayenneSelectBinding(new SelectQuery(expression),
				bindingDescription, getValueConverter(), location);
	}

}
