package nc.ccas.gasel.services.impl;

import nc.ccas.gasel.beans.Fusion;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.services.Fusionneur;

public class FusionneurAspectAides implements Fusionneur<AspectAides> {

	@Override
	public void fusionner(Fusion<AspectAides> fusion) {
		AspectAides main = fusion.getElementPrincipal();
		AspectAides sec = fusion.getElementSecondaire();

		main.basculerAidesDepuis(sec);
	}

}
