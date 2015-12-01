package gasel.maintenance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import tests.model.AllModelTests;

public class AnalyseEditionBons {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// ------------------------------------------------------------
		String fichier = "c:/isinc/data/20120327_133016_172.16.19.89_bon.dat";
		AllModelTests.setupDatabase("noumea");
		// ------------------------------------------------------------

		String numeroBon = null;
		Map<String, String> aideVersBon = new TreeMap<String, String>();
		Set<String> bonsVus = new TreeSet<String>();

		BufferedReader reader = new BufferedReader(new FileReader(fichier));
		String line, action = null, numeroDossier = null, nom = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("Bon n°")) {
				numeroBon = line.split(": ", 2)[1].substring(0, 11) + "_";
			} else if (line.startsWith("Action")) {
				action = line.split(": ", 2)[1].split(" ", 2)[0];
			} else if (line.startsWith("Nom")) {
				nom = line.split(": ", 2)[1];
			} else if (line.startsWith("Dossier n°")) {
				numeroDossier = line.split(": ", 2)[1];
			} else if (line.startsWith("*****")) {
				numeroBon = null;
				action = null;
				nom = null;
				numeroDossier = null;
			}
			if (line.startsWith("Date fin")) {
				if (bonsVus.contains(numeroBon))
					continue;
				bonsVus.add(numeroBon);

				String key = numeroDossier + ", aide type " + action;
				String oldValue = aideVersBon.get(key);
				if (oldValue != null) {
					System.out.println("!!! " + key + "\tbon " + numeroBon
							+ " <!> " + oldValue + " / " + nom);
				} else {
					aideVersBon.put(key, numeroBon);
				}
			}
		}
		reader.close();
	}

}
