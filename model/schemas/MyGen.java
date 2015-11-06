import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import nc.ccas.gasel.model.aides.AideAlterator;
import nc.ccas.gasel.model.budget.LiensFournisseursAlterator;
import nc.ccas.gasel.model.core.AdresseAlterator;
import nc.ccas.gasel.model.core.PersonneAlterator;
import nc.ccas.gasel.model.habitat.AspectDossierHabitatAlterator;
import nc.ccas.gasel.model.habitat.CodeQuartierHabitatAlterator;
import nc.ccas.gasel.model.pe.EnfantRAMAlterator;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.conf.ConfigLoaderDelegate;
import org.apache.cayenne.conf.ConfigStatus;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.RuntimeLoadDelegate;
import org.apache.cayenne.gen.DefaultClassGenerator;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.project.ApplicationProject;
import org.apache.cayenne.project.ProjectConfiguration;
import org.apache.cayenne.project.validator.ValidationInfo;
import org.apache.cayenne.project.validator.Validator;

import cayennetools.dia_modeller.NameTranslator;
import cayennetools.dia_modeller.cayenne.Generator;
import cayennetools.dia_modeller.impl.NameTranslatorImpl;
import dia_modeller.ext.Alterator;

public class MyGen {

	static final String DEST_DIR = "gen-src";

	// ------------------------------------------------------------------
	// Altérateurs à passer sur le modèle.
	//

	private static final List<Alterator> ALTERATORS = Arrays.asList( //
			new AdresseAlterator(), //
			new LiensFournisseursAlterator(), //
			new CodeQuartierHabitatAlterator(), //
			new AspectDossierHabitatAlterator(), //
			new EnfantRAMAlterator(), //
			new PersonneAlterator(), //
			new AideAlterator() //
			);

	//
	// ------------------------------------------------------------------

	public static void main(String[] args) throws Exception {
		System.setProperty("force-schema", "gasel_v2");
		Generator.generate(DEST_DIR, true, false);

		ApplicationProject project = loadProject();
		DataDomain domain = project.getConfiguration().getDomain();

		// Application des altérations
		for (Alterator alterator : ALTERATORS) {
			alterator.alterDomain(domain);
		}

		// Sauvegarde
		project.save();

		// Validation du projet
		Validator validator = project.getValidator();
		if (validator.validate() != ValidationInfo.VALID) {
			StringBuilder buf = new StringBuilder();
			buf.append("Resulting project is not valid:\n");
			for (Object o : validator.validationResults()) {
				buf.append("* ").append(o.toString()).append("\n");
			}
			throw new RuntimeException(buf.toString());
		}

		// Régénération des classes
		File subclassTemplate = getReadableFile("subclass-template");
		File superclassTemplate = getReadableFile("superclass-template");
		generateClasses(domain, DEST_DIR, subclassTemplate, superclassTemplate,
				new NameTranslatorImpl());

		// Synchronisation
		// Generator.generate(DEST_DIR, false, true);
	}

	// FIXME Copie depuis Generator
	private static ApplicationProject loadProject() {
		File projectFile = new File(DEST_DIR + "/cayenne.xml");
		ProjectConfiguration configuration = new ProjectConfiguration(
				projectFile);
		ConfigLoaderDelegate loaderDelegate = new ProjectLoadDelegate(
				configuration, configuration.getLoadStatus());
		configuration.setLoaderDelegate(loaderDelegate);
		ApplicationProject project = new ApplicationProject(projectFile,
				configuration);
		return project;
	}

	// FIXME Copie depuis Generator
	@SuppressWarnings( { "deprecation", "unchecked" })
	private static void generateClasses(DataDomain domain, String outputFolder,
			File subclassTemplate, File superclassTemplate,
			NameTranslator translator) throws Exception {
		// Configure the generator
		DefaultClassGenerator generator = new DefaultClassGenerator();
		generator.setTemplate(subclassTemplate);
		generator.setSuperTemplate(superclassTemplate);
		generator.setDestDir(new File(outputFolder));
		generator.setMakePairs(true);

		for (Object o : domain.getDataMaps()) {
			DataMap dataMap = (DataMap) o;
			String superPkg;
			if (dataMap.getDefaultPackage() != null) {
				superPkg = dataMap.getDefaultPackage() + ".auto";
			} else {
				superPkg = "auto";
			}

			generator.setSuperPkg(superPkg);
			generator.setDataMap(dataMap);
			generator.setObjEntities(new ArrayList<Object>(dataMap
					.getObjEntities()));
			generator.execute();
		}
	}

	// FIXME Copie depuis Generator
	private static File getReadableFile(String property) throws IOException {
		File retval = null;
		if (System.getProperties().containsKey(property)) {
			retval = new File(System.getProperty(property));
			if (!retval.canRead()) {
				throw new IOException("Cannot read " + property + " " + retval);
			}
		}
		return retval;
	}

}

class ProjectLoadDelegate extends RuntimeLoadDelegate {

	public ProjectLoadDelegate(Configuration config, ConfigStatus status) {
		super(config, status);
	}

	@Override
	protected DataNode createDataNode(String nodeName) {
		return new DataNode(nodeName) {

			@Override
			public DataSource getDataSource() {
				return dataSource;
			}
		};
	}

}