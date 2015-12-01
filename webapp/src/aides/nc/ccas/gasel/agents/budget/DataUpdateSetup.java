package nc.ccas.gasel.agents.budget;

public class DataUpdateSetup implements Runnable {

	public static final int DELAY = 1; // mise à jour chaque heure

	private static boolean run = false;

	public void run() {
		// Garantie d'une seule exécution (au moins par VM)
		if (run)
			return;
		
		synchronized (DataUpdateSetup.class) {
			if (run)
				return;
			run = true;
		}

		// Création du thread de mise à jour
		new Thread(new Runnable() {
			public void run() {
				DataUpdateWork work = new DataUpdateWork();

				for (;;) {
					try {
						work.run();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(DELAY * 60 * 60 * 1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}).start();
	}
}
