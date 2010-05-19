package org.openhab.ui.webapp.internal.http;

import java.util.Hashtable;

import javax.servlet.ServletException;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebAppServlet {
	private static final String WEBAPP_ALIAS = "/";
	private ExtendedHttpService httpService;

	private final static Logger logger = LoggerFactory.getLogger(WebAppServlet.class);
	
	public void setHttpService(ExtendedHttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(ExtendedHttpService httpService) {
		this.httpService = null;
	}

	protected void startup() {
		try {
			logger.debug("Starting up webapp at " + WEBAPP_ALIAS);
			httpService.registerResources(WEBAPP_ALIAS, "src/main/resources", null);
			
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put("applicationClassName", MainApplication.class.getName());
			props.put("pattern", "xxx/.*");
//			httpService.registerFilter("xxx/.*", null, props, null);
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
//		} catch (ServletException e) {
//			logger.error("Error during servlet startup", e);
		}
	}

	protected void shutdown() {
		httpService.unregister(WEBAPP_ALIAS);
	}
}
