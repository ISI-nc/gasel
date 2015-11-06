package nc.ccas.gasel.xls;

import static org.apache.tapestry.services.ServiceConstants.COMPONENT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.Format;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import nc.ccas.gasel.jwcs.core.Tableau;
import nc.ccas.gasel.jwcs.core.tableau.Cellule;
import nc.ccas.gasel.jwcs.core.tableau.GroupeDesc;
import nc.ccas.gasel.workflow.IWorkflowScopeManager;
import nc.ccas.gasel.workflow.WorkflowEntry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.NullWriter;
import org.apache.tapestry.json.IJSONWriter;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;

public class XlsService implements IEngineService {

	public static final ContentType CONTENT_TYPE = new ContentType(
			"application/msexcel");

	public static final String NAME = "xls";

	private static final String CLIENT_ID = "id";

	private LinkFactory _linkFactory;

	private IWorkflowScopeManager _workflowScopeManager;

	public ILink getLink(boolean post, Object parameter) {
		Object[] params = (Object[]) parameter;
		String path = (String) params[0];
		String clientId = (String) params[1];

		Map<String, String> map = new TreeMap<String, String>();
		map.put(COMPONENT, path);
		map.put(CLIENT_ID, clientId);
		map.put("entry", String.valueOf(_workflowScopeManager.getWorkflow()
				.getCurrentEntry().getId()));
		return _linkFactory.constructLink(this, post, map, true);
	}

	public String getName() {
		return NAME;
	}

	public void service(IRequestCycle cycle) throws IOException {
		String idPath = cycle.getParameter(COMPONENT);
		String clientId = cycle.getParameter(CLIENT_ID);

		// Check présence de la session
		WebSession session = cycle.getInfrastructure().getRequest().getSession(
				false);

		if (session == null || session.isNew())
			throw new StaleSessionException();

		// Récupération de la page
		WorkflowEntry entry = _workflowScopeManager.getWorkflow()
				.getCurrentEntry();
		if (entry == null) { // Timeout
			return;
		}

		// Récupération du tableau
		Tableau tableau = findTableau(cycle, entry, idPath);

		if (tableau == null) {
			throw new NullPointerException("tableau non défini (" + idPath
					+ ")");
		}

		tableau.useCache(clientId);
		List<?> source = tableau.getSource();

		if (source == null) {
			throw new NullPointerException("source non définie (" + idPath
					+ ")");
		}

		// ----------------
		// Rendu du tableau
		//

		String titre = tableau.getTitre();
		if (titre == null) {
			titre = entry.getTitle();
		}
		if (titre == null) {
			titre = "Tableau";
		}
		titre = titre.replace(':', '-');
		titre = titre.replace('/', '-');
		titre = titre.replace('\\', '-');

		HSSFWorkbook wb = render(tableau, source, titre);

		// Sortie
		WebResponse response = cycle.getInfrastructure().getResponse();
		OutputStream out = response.getOutputStream(CONTENT_TYPE);
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ titre + ".xls\"");
		wb.write(out);
	}

	private HSSFWorkbook render(Tableau tableau, List<?> source, String titre) {
		titre = validSheetName(titre);

		HSSFWorkbook wb = new HSSFWorkbook();
		// Titre de la page = "Tableau" parce que trop de contraintes (et donc
		// de problèmes) sur les titres libres...
		HSSFSheet sheet = wb.createSheet("Tableau");
		CellStyles styles = new CellStyles(wb);

		// -----

		short rowNum = 0;
		short col = 0;
		HSSFRow row;

		// Groupes
		if (!tableau.getGroupes().isEmpty()) {
			row = sheet.createRow(rowNum++);
			for (GroupeDesc groupe : tableau.getGroupes()) {
				HSSFCell cell = row.createCell(col);
				cell.setCellValue(new HSSFRichTextString(groupe.getTitre()));
				cell.setCellStyle(styles.title);

				short end = (short) (col + groupe.getLargeur() - 1);
				sheet.addMergedRegion(new Region(rowNum - 1, col, rowNum - 1,
						end));
				col = (short) (end + 1);
			}
		}

		// Titre
		row = sheet.createRow(rowNum++);
		col = 0;
		for (String colTitle : tableau.getTitres()) {
			HSSFCell cell = row.createCell(col++);
			cell.setCellValue(new HSSFRichTextString(colTitle));
			cell.setCellStyle(styles.title);
		}

		// Valeurs
		for (int r = 0; r < source.size(); r++) {
			Object o = source.get(r);
			boolean last = (r == (source.size() - 1));
			tableau.setValue(o);
			row = sheet.createRow(rowNum++);
			col = 0;
			for (Cellule cellule : tableau.getValeurs()) {
				Object value = translateCellValue(cellule);
				HSSFCell cell = row.createCell(col++);

				cell.setCellStyle(styles.cell(cellule.isHighlight(), cellule
						.isDottedTop(), value instanceof Date, last));

				if (value == null) {
					continue;
				} else if (value instanceof String) {
					cell.setCellValue(new HSSFRichTextString((String) value));
				} else if (value instanceof Date) {
					cell.setCellValue((Date) value);
				} else if (value instanceof Number) {
					cell.setCellValue(((Number) value).doubleValue());
				} else {
					throw new IllegalArgumentException( //
							value.getClass().toString() + ": "
									+ value.toString());
				}
			}
		}
		return wb;
	}

	private String validSheetName(String titre) {
		titre = titre //
				.replace(':', '-') //
				.replace('/', '-') //
				.replace('\\', '-') //
				.replace('*', 'x') //
				.replace('?', '.') //
				.replace('[', '(') //
				.replace(']', ')');

		return titre.length() > 30 ? titre.substring(0, 30) : titre;
	}

	private Tableau findTableau(IRequestCycle cycle, WorkflowEntry entry,
			String componentId) {
		IPage page = cycle.getPage(entry.getPageName());
		cycle.activate(page);
		Tableau tableau = (Tableau) page.getNestedComponent(componentId);
		return tableau;
	}

	private Object translateCellValue(Cellule cellule) {
		Object cellValue = cellule.getValue();
		if (cellValue instanceof Number || cellValue instanceof Date) {
			return cellValue;
		}
		Format format = cellule.getFormat();
		String value;
		if (cellValue == null) {
			value = null;
		} else if (format == null) {
			value = String.valueOf(cellValue);
		} else {
			value = format.format(cellValue);
		}
		return value;
	}

	// Injected

	public void setLinkFactory(LinkFactory linkFactory) {
		_linkFactory = linkFactory;
	}

	public void setWorkflowScopeManager(
			IWorkflowScopeManager workflowScopeManager) {
		_workflowScopeManager = workflowScopeManager;
	}

}

class NullMarkupWriterSource implements MarkupWriterSource {

	public IJSONWriter newJSONWriter(PrintWriter writer, ContentType contentType) {
		return null;
	}

	public IMarkupWriter newMarkupWriter(PrintWriter writer,
			ContentType contentType) {
		return new NullWriter();
	}

}

class NullRequestLocaleManager implements RequestLocaleManager {

	public Locale extractLocaleForCurrentRequest() {
		return Locale.getDefault();
	}

	public void persistLocale() {
		// skip
	}

}