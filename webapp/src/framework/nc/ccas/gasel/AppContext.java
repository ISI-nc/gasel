package nc.ccas.gasel;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bridge to application's configuration.
 * 
 */
public class AppContext implements ServletContextListener {

	private static ServletContext servletContext = null;

	public static String getInitParameter(String name) {
		return servletContext.getInitParameter(name);
	}

	public static String ldap(String key) {
		return getInitParameter("LDAP." + key);
	}

	public static String mail(String key) {
		return getInitParameter("Mail." + key);
	}

	public static String starjet(String key) {
		return getInitParameter("Starjet." + key);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		servletContext = null;
	}

}
