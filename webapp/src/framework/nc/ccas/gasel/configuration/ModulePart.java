package nc.ccas.gasel.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModulePart implements Serializable {
	private static final long serialVersionUID = -8469160489456819207L;

	private String titre;

	private String dossier;

	private List<ModuleEntryPoint> entryPoints = new ArrayList<ModuleEntryPoint>();

	ModuleDescription module;

	public String getPath() {
		return ModuleDescription.composePath(module.getDossier(), getDossier());
	}

	public void addEntryPoint(ModuleEntryPoint pointEntree) {
		entryPoints.add(pointEntree);
		pointEntree.part = this;
	}

	public String getDossier() {
		return dossier;
	}

	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	public List<ModuleEntryPoint> getEntryPoints() {
		return entryPoints;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public String toString() {
		return "<ModulePart " + titre + ">";
	}

}
