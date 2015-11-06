package nc.ccas.gasel.model;

import java.util.Arrays;
import java.util.Date;

import nc.ccas.gasel.model.core.Utilisateur;

public class ModifUtils {

	/**
	 * @see ModifUtils#triggerModified(Utilisateur, Date, Iterable)
	 */
	public static void triggerModified(Utilisateur user, Date date,
			Object... targets) {
		triggerModified(user, date, Arrays.asList(targets));
	}

	/**
	 * Répercute le signal de modification aux cibles.
	 * 
	 * <p>
	 * Les cibles peuvent êtres des {@link ModifListener} ou des
	 * <code>{@link Iterable}&lt;? extends {@link ModifListener}&gt;</code>.
	 */
	public static void triggerModified(Utilisateur user, Date date,
			Iterable<?> targets) {
		for (Object target : targets) {
			if (target == null)
				continue;

			if (target instanceof Iterable<?>) {
				// Cas Iterable<?>
				triggerModified(user, date, (Iterable<?>) target);

			} else if (target instanceof ModifListener) {
				// Cas ModifListener
				ModifListener toTrigger = (ModifListener) target;
				toTrigger.modified(user, date);
			}
		}
	}

	public static void updateTraqueModifs(TraqueModifs target,
			Utilisateur user, Date date) {
		target.setModifDate(date);
		target.setModifUtilisateur(user);
	}

}
