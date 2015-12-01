package nc.ccas.gasel.configuration;

import java.io.Serializable;

public class ModuleEntryPoint implements Serializable {
	private static final long serialVersionUID = -6686373911086372667L;

	private String page;

	private String titre;

	private String description;

	ModulePart part;

	public String getPageName() {
		return ModuleDescription.composePath(part.module.getDossier(), part
				.getDossier(), page);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String nomPage) {
		this.page = nomPage;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public String toString() {
		return "<ModuleEntrypPoint " + titre + ">";
	}

}
