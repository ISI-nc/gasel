package nc.ccas.gasel.ws;

import static java.lang.Long.parseLong;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.ccas.gasel.BasePageSql;
import nc.ccas.gasel.reports.dossiers.actualite.ActualiteReport;

import org.apache.cayenne.access.DataContext;
import org.apache.hivemind.util.Defense;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@SuppressWarnings("serial")
public class ActualiteReportServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getAuthType() == null) {
			resp.sendError(403);
			return;
		}

		Date debut = dateParam(req, "debut");
		Date fin = dateParam(req, "fin");

		BasePageSql sql = new BasePageSql(DataContext.createDataContext());
		ActualiteReport report = new ActualiteReport(sql, debut, fin);
		HSSFWorkbook wb = report.getWorkbook();

		// Ecriture
		resp.setContentType("application/vnd.ms-excel");
		wb.write(resp.getOutputStream());
	}

	private Date dateParam(HttpServletRequest req, String paramName) {
		String value = req.getParameter(paramName);
		Defense.notNull(value, paramName);
		return new Date(parseLong(value));
	}
}
