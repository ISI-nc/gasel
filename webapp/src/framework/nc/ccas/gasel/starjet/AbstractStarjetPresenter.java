package nc.ccas.gasel.starjet;

import java.io.Writer;

import nc.ccas.gasel.editique.AbstractView;
import nc.ccas.gasel.utils.StarjetCoder;

public abstract class AbstractStarjetPresenter<T> extends AbstractView<T>
		implements StarjetPresenter {

	public AbstractStarjetPresenter(Class<T> clazz) {
		super(clazz);
	}

	protected StarjetWriter writer(Writer out) {
		return new StarjetWriter(out);
	}

	protected String coder(String numero) {
		return StarjetCoder.coder(numero);
	}

}