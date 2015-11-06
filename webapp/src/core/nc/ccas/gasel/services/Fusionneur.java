package nc.ccas.gasel.services;

import nc.ccas.gasel.beans.Fusion;

public interface Fusionneur<T> {
	
	public void fusionner(Fusion<T> fusion);

}
