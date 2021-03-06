package org.owasp.csrfguard;

import java.io.InputStream;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.owasp.csrfguard.util.Resources;

import org.owasp.csrfguard.util.Streams;

public class CsrfGuardServletContextListener implements ServletContextListener {

	private final static String CONFIG_PARAM = "Owasp.CsrfGuard.Config";

	private final static String CONFIG_PRINT_PARAM = "Owasp.CsrfGuard.Config.Print";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String config = context.getInitParameter(CONFIG_PARAM);


		if (config == null) {
			throw new RuntimeException(String.format("failure to specify context init-param - %s", CONFIG_PARAM));
		}

		InputStream is = null;
		Properties properties = new Properties();

		try {
			is = Resources.getResourceStream(config, context, CsrfGuardServletContextListener.class);
			properties.load(is);
			CsrfGuard.load(properties);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Streams.close(is);
		}


		String printConfig = context.getInitParameter(CONFIG_PRINT_PARAM);

		if (printConfig != null && Boolean.parseBoolean(printConfig)) {
			context.log(CsrfGuard.getInstance().toString());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		/** nothing to do **/
	}

}
