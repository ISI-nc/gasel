package nc.ccas.gasel.beans;

import org.apache.hivemind.util.Defense;

public class Fusion<T> {

	private T elementPrincipal;

	private T elementSecondaire;

	public T getElementPrincipal() {
		return elementPrincipal;
	}

	public void setElementPrincipal(T elementPrincipal) {
		Defense.notNull(elementPrincipal, "elementPrincipal");
		this.elementPrincipal = elementPrincipal;
	}

	public T getElementSecondaire() {
		return elementSecondaire;
	}

	public void setElementSecondaire(T elementSecondaire) {
		Defense.notNull(elementSecondaire, "elementSecondaire");
		this.elementSecondaire = elementSecondaire;
	}

}
