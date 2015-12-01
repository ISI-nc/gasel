package org.apache.cayenne.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.access.QueryLogger;
import org.apache.cayenne.util.Util;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;

public class GaselDataSourceFactory implements DataSourceFactory {

	private static final String FALLBACK_DB_PROPERTIES_PATH = "fallback-db.properties";

	private static String dsfOverrideName;

	/**
	 * Allows to override the factory from another one in the configuration.
	 * 
	 * @param dsfName
	 *            The name of the alternate {@link DataSourceFactory} to use.
	 */
	public static void setOverride(String dsfName) {
		GaselDataSourceFactory.dsfOverrideName = dsfName;
	}

	private static final Log logger = LogFactory
			.getLog(GaselDataSourceFactory.class);

    protected Configuration parentConfig;

    public void initializeWithParentConfiguration(Configuration conf) {
        this.parentConfig = conf;
    }

	@Override
	public DataSource getDataSource(String location, Level logLevel)
			throws Exception {
        return getDataSource(location);
	}

	@Override
	public DataSource getDataSource(String location) throws Exception {
		if (location == null) {
			throw new CayenneRuntimeException(
					"Null 'location'");
		}

		if (dsfOverrideName != null) {
			logger.warn("Using override: " + dsfOverrideName);
			return loadFallbackDb(dsfOverrideName);
		}

		try {
			return lookupViaJNDI(location);

		} catch (Exception ex) {
			logger.info("failed JNDI lookup, attempt to load "
					+ "from local preferences. Location key:" + location);

			// failover to preferences loader to allow local development
			try {
				return loadFallbackDb(FALLBACK_DB_PROPERTIES_PATH);
			} catch (Exception fallbackException) {

				logger.info("failed loading from fallback",
						Util.unwindException(fallbackException));

				logger.fatal(fallbackException.getMessage());
				fallbackException.printStackTrace();

				// giving up ... rethrow original exception...
				QueryLogger.logConnectFailure(ex);
				throw ex;
			}
		}
	}

	DataSource lookupViaJNDI(String location) throws NamingException {
        QueryLogger.logConnect(location);

		Context context = new InitialContext();
		DataSource dataSource;
		try {
			Context envContext = (Context) context.lookup("java:comp/env");
			dataSource = (DataSource) envContext.lookup(location);
		} catch (NamingException namingEx) {
			// try looking up the location directly...
			dataSource = (DataSource) context.lookup(location);
		}

		QueryLogger.logConnectSuccess();
		return dataSource;
	}

	DataSource loadFallbackDb(String path) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		URL url = loader.getResource(path);

		Properties properties = new Properties();
		InputStream in = url.openStream();
		try {
			properties.load(in);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return BasicDataSourceFactory.createDataSource(properties);
	}

}
