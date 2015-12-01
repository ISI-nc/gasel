package nc.ccas.gasel.agents.budget;

import static java_gaps.ResourceUtils.readResource;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import nc.ccas.gasel.AppContext;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.reports.exceptions.MailEnv;

import org.apache.cayenne.access.DataNode;
import org.apache.log4j.Logger;

public class DataUpdateWork implements Runnable {

	private static final Logger LOG = Logger.getLogger(DataUpdateWork.class);

	public static Date lastUpdate = null;

	public void run() {
		// Verif de l'heure
		GregorianCalendar cal = new GregorianCalendar();
		if (cal.get(Calendar.HOUR_OF_DAY) < 6
				|| cal.get(Calendar.HOUR_OF_DAY) > 20)
			return;

		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SATURDAY:
		case Calendar.SUNDAY:
			return;
		}

		// Go
		try {
			synchronized (DataUpdateWork.class) {
				LOG.info("Starting update...");
				System.out.println("Starting update...");
				realRun();
			}
			lastUpdate = new Date();
			LOG.info("Update finished.");
			System.out.println("Update finished.");
		} catch (Exception ex) {
			LOG.info("Update error.");
			LOG.error(ex);
			System.out.println("Update error.");
			ex.printStackTrace(System.out);

			StringWriter s = new StringWriter();
			ex.printStackTrace(new PrintWriter(s));
			try {
				MailEnv.sendMail("GASELv2 <null@ville-noumea.nc>",
						AppContext.mail("default-recipient"),
						"[GASELv2] DataUpdateAgent exception", //
						s.toString());
			} catch (IOException e) {
				LOG.warn(e);
				System.out.println("(!!) " + e);
			}
		}
	}

	private DataSource ds() {
		// try {
		// return (DataSource) new InitialContext()
		// .lookup(MairieDSF.JNDI_LOCATION);
		// } catch (NamingException e1) {
		// throw new RuntimeException(e1);
		// }
		DataNode dataNode = (DataNode) CayenneUtils.getDomain()
				.getDataNodes().iterator().next();
		return dataNode.getDataSource();
	}

	private void realRun() throws SQLException {
		String fileContents;
		try {
			fileContents = readResource(DataUpdateWork.class, "update_data.sql");
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

		Connection cx;
		try {
			cx = ds().getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			cx.setAutoCommit(true);
			StringTokenizer tok = new StringTokenizer(fileContents, ";");
			while (tok.hasMoreElements()) {
				String query = tok.nextToken().replaceAll("\\s*--.*", "")
						.replaceAll("\n+", "\n").trim();
				if (query.length() == 0)
					continue;
				runQuery(cx, query);
			}
		} finally {
			try {
				cx.rollback(); // si pas de commit -> rollback
				cx.setAutoCommit(false); // just in case
			} catch (SQLException e) {
				// ignore, we tried, at least...
			} finally {
				cx.close();
			}
		}
	}

	private void runQuery(Connection cx, String query) throws SQLException {
		// Gestion du mode transactionnel
		if (query.equalsIgnoreCase("begin")) {
			cx.setAutoCommit(false);
			return;
		}
		if (query.equalsIgnoreCase("commit")) {
			cx.commit();
			cx.setAutoCommit(true);
			return;
		}
		if (query.equalsIgnoreCase("rollback")) {
			cx.rollback();
			cx.setAutoCommit(true);
			return;
		}

		// Exécution de la requête
		PreparedStatement stmt = cx.prepareStatement(query);
		try {
			LOG.debug("Performing query:\n" + query);
			stmt.execute();
		} finally {
			stmt.close();
		}
	}

}
