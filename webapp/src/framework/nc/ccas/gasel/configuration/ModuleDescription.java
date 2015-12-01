package nc.ccas.gasel.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModuleDescription implements Serializable {
	private static final long serialVersionUID = -5977565379693026962L;

	static String composePath(String... parts) {
		StringBuilder buf = new StringBuilder();
		for (String part : parts) {
			if (part == null)
				continue;
			if (buf.length() > 0)
				buf.append('/');
			buf.append(part);
		}
		return buf.toString();
	}

	private int position = 100000;

	private String titre;

	private String dossier;

	private List<ModulePart> parts = new ArrayList<ModulePart>();

	public String getPath() {
		return composePath(getDossier());
	}

	public void addPart(ModulePart part) {
		parts.add(part);
		part.module = this;
	}

	public String getDossier() {
		return dossier;
	}

	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	public List<ModulePart> getParts() {
		return parts;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
