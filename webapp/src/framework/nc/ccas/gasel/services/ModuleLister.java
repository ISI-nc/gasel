package nc.ccas.gasel.services;

import java.util.List;

import nc.ccas.gasel.configuration.ModuleDescription;
import nc.ccas.gasel.configuration.ModuleEntryPoint;

public interface ModuleLister {

	public List<ModuleDescription> getModules();

	public ModuleDescription getModule(String titre);

	public ModuleEntryPoint lookupEntryPoint(String path);

}
