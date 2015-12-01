package test;

import nc.ccas.gasel.configuration.ModuleDescription;
import nc.ccas.gasel.configuration.ModuleEntryPoint;
import nc.ccas.gasel.configuration.ModulePart;
import nc.ccas.gasel.services.ModuleLister;

import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;

public class TestModuleLister {

	public static void main(String[] args) {
		Registry registry = RegistryBuilder.constructDefaultRegistry();

		ModuleLister ml = (ModuleLister) registry
				.getService(ModuleLister.class);
		for (ModuleDescription mod : ml.getModules()) {
			System.out.println("- " + mod.getPath() + " - " + mod.getTitre());
			if (true)
				continue;
			for (ModulePart part : mod.getParts()) {
				System.out.println("  - " + part.getPath() + " - "
						+ part.getTitre());
				for (ModuleEntryPoint ep : part.getEntryPoints()) {
					System.out.println("    - " + ep.getPageName() + " - "
							+ ep.getTitre());
				}
			}
		}
	}

}
