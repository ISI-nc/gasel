package nc.ccas.gasel.jwcs.core.edit;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.AbstractFormComponent;

@ComponentClass(allowBody = false)
public abstract class Periode extends AbstractFormComponent {

	@Parameter(required = true)
	public abstract java.util.Date getDebut();

	public abstract void setDebut(java.util.Date date);

	@Parameter(required = true)
	public abstract java.util.Date getFin();

	public abstract void setFin(java.util.Date date);

	@Parameter(defaultValue = "literal:10")
	public abstract Integer getSize();

	@Component(id = "debut", type = "core/edit/Date", bindings = { "value=ognl:debut" })
	public abstract Date getSelectDebut();

	@Component(id = "fin", type = "core/edit/Date", bindings = { "value=ognl:fin" })
	public abstract Date getSelectFin();

	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getSelectDebut().render(writer, cycle);
		writer.print(" au ");
		getSelectFin().render(writer, cycle);
	}

	@Override
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		getSelectDebut().render(writer, cycle);
		getSelectFin().render(writer, cycle);
	}

}
