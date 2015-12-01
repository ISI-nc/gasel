package nc.ccas.gasel.jwcs.core;

import java.util.List;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.psm.IntRangePSM;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Component;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.PropertySelection;
import org.apache.tapestry.form.Submit;

/**
 * 
 * La particularité ici c'est qu'on appelle getPageNumber/getPageCount
 * uniquement lorsque c'est strictement nécessaire.
 * 
 * @author Mikaël Cluseau - ISI.NC
 * 
 */
public abstract class Pager extends AbstractComponent {

	@Parameter(name = "page", required = true)
	public abstract int getPageNumber();

	public abstract void setPageNumber(int pageNumber);

	@Parameter(required = true)
	public abstract int getPageCount();

	@Parameter(name = "onchange")
	public abstract IActionListener getOnChange();

	@Parameter(defaultValue = "false")
	public abstract boolean getAsync();

	@Parameter(defaultValue = "null")
	public abstract List<String> getUpdateComponents();

	@Component(bindings = { "class=literal:button", "action=listener:goToPage",
			"parameters=pageNumber-1", "disabled=pagePrecDisabled",
			"value=literal:«", "async=async",
			"updateComponents=ognl:async?updateComponents:null" })
	public abstract Submit getPagePrec();

	@Component(bindings = { "class=literal:button", "action=listener:goToPage",
			"parameters=pageNumber+1", "disabled=pageSuivDisabled",
			"value=literal:»", "async=async",
			"updateComponents=ognl:async?updateComponents:null" })
	public abstract Submit getPageSuiv();

	@Component(bindings = { "value=pageNumber", "model=selectPageModel",
			"onchange=literal:refreshPage()" })
	public abstract PropertySelection getSelectPage();

	private boolean _rewinding;

	public IPropertySelectionModel getSelectPageModel() {
		return new IntRangePSM(1, getPageCount());
	}

	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		_rewinding = cycle.isRewinding();
		getPagePrec().render(writer, cycle);
		if (!_rewinding) {
			writer.begin("span");
			writer.attribute("class", "pager-text");
			writer.printRaw("&nbsp;Page&nbsp;");
		}
		getSelectPage().render(writer, cycle);
		if (!_rewinding) {
			writer.printRaw("/" + getPageCount() + "&nbsp;");
			writer.end(); // span
		}
		getPageSuiv().render(writer, cycle);
	}

	public void goToPage(IRequestCycle cycle, int page) {
		if (page < 1) {
			page = 1;
		} else {
			int pageCount = getPageCount();
			if (page > pageCount) {
				page = pageCount;
			}
		}
		setPageNumber(page);
		IActionListener onchange = getOnChange();
		if (onchange != null) {
			onchange.actionTriggered(this, cycle);
		}

		if (!getAsync())
			((BasePage) getPage()).redirect(cycle);
	}

	public boolean isPagePrecDisabled() {
		return !_rewinding && getPageNumber() <= 1;
	}

	public boolean isPageSuivDisabled() {
		return !_rewinding && getPageNumber() >= getPageCount();
	}

}
