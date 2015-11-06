package nc.ccas.gasel.jwcs.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.BaseComponent;
import nc.ccas.gasel.BasePage;

import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.Persist;

public abstract class SideBar extends BaseComponent {

	public static class Category<T extends Value> {
		public final String title;

		public final List<T> values = new LinkedList<T>();

		public Category(String name) {
			this.title = name;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	public static class Value implements Serializable {
		private static final long serialVersionUID = 6143924573423330452L;

		public final String title;

		public Value(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	@Parameter(defaultValue = "literal:false")
	public abstract boolean getAutoSelectFirst();

	public abstract List<Category<?>> getSource();

	@Persist("workflow")
	public abstract Integer getCurrentCatIdx();

	public abstract void setCurrentCatIdx(Integer value);

	@Persist("workflow")
	public abstract Integer getCurrentValueIdx();

	public abstract void setCurrentValueIdx(Integer value);

	public abstract Category<?> getCategory();

	public abstract Object getValue();

	public abstract void setValue(Object value);

	public void openCat(int idx) {
		setCurrentCatIdx(idx);
		if (getAutoSelectFirst()) {
			if (getSource().get(idx).values.isEmpty()) {
				setCurrentValueIdx(null);
			} else {
				setCurrentValueIdx(0);
			}
		} else {
			setCurrentValueIdx(null);
		}
		updateValue();
		redirect();
	}

	public void openValue(int idx) {
		setCurrentValueIdx(idx);
		updateValue();
		redirect();
	}

	private void redirect() {
		((BasePage) getPage()).redirect();
	}

	private void updateValue() {
		if (getCurrentValueIdx() != null) {
			setValue(getSource().get(getCurrentCatIdx()).values
					.get(getCurrentValueIdx()));
		} else if (getCurrentCatIdx() != null) {
			setValue(getSource().get(getCurrentCatIdx()));
		} else {
			setValue(null);
		}
	}

}
