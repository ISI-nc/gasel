package nc.ccas.gasel.pages.pi;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.model.pi.JardinFamilial;

public abstract class ListeJardinsFamiliaux extends BasePage {
	
	public abstract JardinFamilial getValue();
	
	public boolean isJardinSupprimable() {
		JardinFamilial jardin = getValue();
		return jardin.getParcelles().isEmpty();
	}

}
