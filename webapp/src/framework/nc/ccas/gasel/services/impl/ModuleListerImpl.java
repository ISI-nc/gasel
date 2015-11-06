package nc.ccas.gasel.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import nc.ccas.gasel.configuration.ModuleDescription;
import nc.ccas.gasel.configuration.ModuleEntryPoint;
import nc.ccas.gasel.configuration.ModulePart;
import nc.ccas.gasel.services.ModuleLister;

import com.asystan.common.beans.BeanUtils;

public class ModuleListerImpl implements ModuleLister {

	private List<ModuleDescription> modules;

	public List<ModuleDescription> getModules() {
		return modules;
	}

	public ModuleDescription getModule(String titre) {
		for (ModuleDescription module : modules) {
			if (titre.equals(module.getTitre())) {
				return module;
			}
		}
		return null;
	}

	// Injections

	public void setModules(List<ModuleDescription> modules) {
		this.modules = new ArrayList<ModuleDescription>(modules);
		// Ordre : position ASC, nom ASC

		// Tri par nom
		Collections.sort(this.modules, new Comparator<ModuleDescription>() {
			public int compare(ModuleDescription o1, ModuleDescription o2) {
				return o1.getTitre().compareToIgnoreCase(o2.getTitre());
			}
		});
		// Tri par position
		Collections.sort(this.modules, new Comparator<ModuleDescription>() {
			public int compare(ModuleDescription o1, ModuleDescription o2) {
				return new Integer(o1.getPosition())
						.compareTo(o2.getPosition());
			}
		});
	}

	public ModuleEntryPoint lookupEntryPoint(String path) {
		String[] pathElements = path.split("/");
		List<ModulePart> parts;
		switch (pathElements.length) {
		case 1:
			parts = lookupPart(null, null);
			break;
		case 2:
			parts = lookupPart(pathElements[0]);
			break;
		case 3:
			parts = lookupPart(pathElements[0], pathElements[1]);
			break;
		default:
			throw new IllegalArgumentException("Chemin invalide : " + path);
		}
		for (ModulePart part : parts) {
			for (ModuleEntryPoint ep : part.getEntryPoints()) {
				if (ep.getPage().equals(pathElements[pathElements.length - 1])) {
					return ep;
				}
			}
		}
		return null;
	}

	private List<ModuleDescription> lookupModule(String path) {
		List<ModuleDescription> modules = new LinkedList<ModuleDescription>();
		for (ModuleDescription desc : getModules()) {
			if (BeanUtils.nullSafeEquals(desc.getDossier(), path))
				modules.add(desc);
		}
		return modules;
	}

	private List<ModulePart> lookupPart(String path) {
		List<ModulePart> parts = new ArrayList<ModulePart>();
		parts.addAll(lookupPart(null, path));
		parts.addAll(lookupPart(path, null));
		return parts;
	}

	private List<ModulePart> lookupPart(String modulePath, String partPath) {
		List<ModulePart> parts = new LinkedList<ModulePart>();
		for (ModuleDescription desc : lookupModule(modulePath)) {
			for (ModulePart part : desc.getParts()) {
				if (BeanUtils.nullSafeEquals(part.getDossier(), partPath))
					parts.add(part);
			}
		}
		return parts;
	}

}
