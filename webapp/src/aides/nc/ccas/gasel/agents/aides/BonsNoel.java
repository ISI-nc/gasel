package nc.ccas.gasel.agents.aides;

import java.util.List;

import nc.ccas.gasel.agents.aides.impl.BonsNoelImpl;
import nc.ccas.gasel.model.aides.Aide;

public interface BonsNoel {

	// TODO Hivemind
	public static BonsNoel INSTANCE = new BonsNoelImpl();

	/**
	 * Crée les aides de Noël pour l'année en cours.
	 */
	public List<Aide> createAidesNoel();

	/**
	 * Crée les aides de Noël pour l'année en <code>year</code>.
	 */
	public List<Aide> createAidesNoel(int year);

}
