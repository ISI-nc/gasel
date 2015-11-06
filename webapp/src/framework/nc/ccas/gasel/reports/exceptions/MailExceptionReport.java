package nc.ccas.gasel.reports.exceptions;

import static org.apache.log4j.Logger.getLogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.AppContext;
import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.LoginData;
import nc.ccas.gasel.ObjectPage;
import nc.ccas.gasel.workflow.Workflow;
import nc.ccas.gasel.workflow.WorkflowDisplayHelper;
import nc.ccas.gasel.workflow.WorkflowDisplayHelperImpl;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.cayenne.DataObject;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.log4j.Logger;
import org.apache.tapestry.IPage;

public class MailExceptionReport implements ExceptionReport {

	private static final Logger log = getLogger(MailExceptionReport.class);

	public void reportException(Throwable exception, IPage page, LoginData login) {
		String message = messageBody(exception, page);

		// Envoi du message
		String to = login.getEmail();
		if (to == null) {
			to = AppContext.mail("default-recipient");
		}
		try {
			MailEnv.sendMail( //
					"GASEL <noreply@ville-noumea.nc>", // From
					to, // To
					"[GASEL] Rapport d'erreur", // Subject
					message // Body
			);
		} catch (Exception ex) {
			log.error("Unable to send mail to \"" + to + "\" through \""
					+ AppContext.mail("smtp-server") + "\": "
					+ ex.getClass().getName());
			log.error(ex);
		}
	}

	public String messageBody(Throwable exception, IPage page) {
		// Préparation des données
		Workflow workflow = null;
		if (page instanceof BasePage) {
			workflow = ((BasePage) page).getWorkflow();
		}
		DataObject object = null;
		if (page instanceof ObjectPage) {
			object = ((ObjectPage<?>) page).getObject();
		}

		// Construction du message
		StringWriter message = new StringWriter();
		PrintWriter out = new PrintWriter(message);

		out.println("Bonjour,");
		out.println();
		out.println("Ceci est un rapport d'erreur GASEL à transmettre au");
		out.println("responsable technique de l'application.");
		out.println();
		out.println("Si possible, indiquez ce que vous faisiez quand vous");
		out.println("avez eu l'erreur.");
		out.println();
		out.println("Merci.");
		out.println();
		out.println("-------------------------------------------------------");
		out.println("-- INFORMATIONS TECHNIQUES");
		out.println("-------------------------------------------------------");
		out.println();
		out.println("Exception :");
		out.println("  - classe :  " + exception.getClass().getName());
		out.println("  - message : " + exception.getMessage());
		out.println();
		out.println("Page:");
		out.println("  - nom :    " + page.getPageName());
		out.println("  - classe : " + page.getClass());
		out.println("           < " + page.getClass().getSuperclass());
		if (object != null) {
			out.println("     - " + object.getObjectId());
			out.println("       " + object);
		}
		out.println();

		out.println("----------------------------------------");
		out.println("-- Résumé de l'exception :");
		out.println("--");
		out.println();
		for (Throwable t = exception; t != null; t = t.getCause()) {
			out.println("-> " + t.getClass());
			out.println(" # " + t.getMessage());
			if (t instanceof ApplicationRuntimeException) {
				ApplicationRuntimeException are = ((ApplicationRuntimeException) t);
				out.println(" @ " + are.getLocation());
			}
			out.println();
		}

		out.println("----------------------------------------");
		out.println("-- Détail de l'exception :");
		out.println("--");
		out.println();
		exception.printStackTrace(out);
		out.println();

		if (workflow != null) {
			out.println("----------------------------------------");
			out.println("-- Résumé du workflow :");
			out.println("--");
			out.println();
			printWorkflow(out, workflow);

			// out.println("----------------------------------------");
			// out.println("-- Détail du workflow :");
			// out.println("--");
			// out.println();
			// printWorkflowDetails(out, workflow);
		}

		out.close();
		return message.toString();
	}

	private void printWorkflow(PrintWriter out, Workflow workflow) {
		WorkflowDisplayHelper displayHelper = WorkflowDisplayHelper.INSTANCE;
		Map<Integer, WorkflowEntry> entriesById = new TreeMap<Integer, WorkflowEntry>();

		WorkflowEntry currentEntry = workflow.getCurrentEntry();

		WorkflowEntry[][] entries = displayHelper.toArray(workflow);
		if (entries.length == 0) {
			out.println("(empty)");
			return;
		}

		// Construction du tableau des représentations
		int width = entries[0].length;
		int height = entries.length;

		String[][] values = new String[height][width];
		int[] widths = new int[width];

		for (int x = 0; x < width; x++) {
			widths[x] = 0;
			for (int y = 0; y < height; y++) {
				WorkflowEntry entry = entries[y][x];
				if (entry == null) {
					values[y][x] = "";
					continue;
				}
				entriesById.put(entry.getId(), entry);

				values[y][x] = entry.getTitle() + " (" + entry.getId() + ")"
						+ (entry == currentEntry ? "*" : "");
				if (widths[x] < values[y][x].length())
					widths[x] = values[y][x].length();
			}
		}

		// Modèle de ligne
		StringBuilder buf = new StringBuilder();
		for (int w : widths) {
			if (buf.length() > 0)
				buf.append(" %2s ");
			buf.append("%-").append(w).append('s');
		}
		buf.append('\n');
		String pattern = buf.toString();

		// Affichage
		for (int y = 0; y < height; y++) {
			Object[] params = new Object[width * 2 - 1];
			for (int x = 0; x < width; x++) {
				if (x > 0)
					params[x * 2 - 1] = arrow(x, y, entries);
				params[x * 2] = values[y][x];
			}
			out.printf(pattern, params);
		}
		out.println();

		// Plus trop d'idées mais des détails :)
		// genre -->
		// for (Map.Entry<Integer, WorkflowEntry> entry :
		// entriesById.entrySet()) { ... }
	}

	private static Map<String, String> ARROWS = new TreeMap<String, String>();
	static {
		// 0 -- 1 -- 2
		// | |- 3
		// | `- 4
		// `- 5
		ARROWS.put("", "");
		ARROWS.put("o", "");
		ARROWS.put("oe", "--");
		ARROWS.put("oes", "--");
		ARROWS.put("nes", "|-");
		ARROWS.put("ne", "`-");
		ARROWS.put("ns", "| ");
	}

	private String arrow(int x, int y, WorkflowEntry[][] entries) {
		String arrow = ARROWS.get(WorkflowDisplayHelperImpl.INSTANCE
				.cardinalities(x, y, entries));
		if (arrow == null)
			return "..";
		return arrow;
	}
}
