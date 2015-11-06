package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import nc.ccas.gasel.configuration.ModuleDescription;
import nc.ccas.gasel.configuration.ModuleEntryPoint;
import nc.ccas.gasel.configuration.ModulePart;
import nc.ccas.gasel.services.ModuleLister;

import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;

public class TemplateCreator {

	private static final boolean OVERWRITE = false;

	private static final String ROOT = "context/WEB-INF";

	private static final String TEMPLATES = "tests/templates";

	private static final String ENCODING = "UTF-8";

	public static void main(String[] args) throws IOException {
		// Specification
		String spec = readFile(TEMPLATES + "/X.page");
		// Template
		String template = readFile(TEMPLATES + "/X.html");

		Registry registry = RegistryBuilder.constructDefaultRegistry();

		ModuleLister ml = (ModuleLister) registry
				.getService(ModuleLister.class);
		for (ModuleDescription mod : ml.getModules()) {
			handleModule(mod, spec, template);
		}
	}

	public static void handleModule(ModuleDescription module, String spec,
			String template) throws IOException {
		String moduleDir = ROOT + "/" + module.getDossier();
		new File(moduleDir).mkdirs();
		for (ModulePart part : module.getParts()) {
			String partDir = moduleDir;
			if (part.getDossier() != null)
				partDir += "/" + part.getDossier();

			for (ModuleEntryPoint ep : part.getEntryPoints()) {
				String modifiedSpec = spec.replace("[TITRE]", ep.getTitre()
						+ " - " + module.getTitre());
				writeFile(partDir + "/" + ep.getPage() + ".page", modifiedSpec);
				writeFile(partDir + "/" + ep.getPage() + ".html", template);
			}
		}
	}

	private static String readFile(String fileName) throws IOException {
		Reader file = new InputStreamReader(new FileInputStream(fileName),
				ENCODING);
		int readCount;
		char[] bytes = new char[4096];
		StringBuilder buf = new StringBuilder();

		do {
			readCount = file.read(bytes);
			buf.append(new String(bytes));
		} while (readCount == bytes.length);

		file.close();
		return new String(buf).trim();
	}

	private static void writeFile(String fileName, String contents)
			throws IOException {
		if (!OVERWRITE && new File(fileName).exists()) {
			System.out.println("skip " + fileName);
			return;
		}
		System.out.println("create " + fileName);
		Writer file = new OutputStreamWriter(new FileOutputStream(fileName),
				ENCODING);
		file.write(contents);
		file.close();
	}

}
