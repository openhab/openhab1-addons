package org.openhab.ui.webapp.internal.http;

import java.util.Hashtable;

import javax.servlet.ServletException;

import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service registers all relevant resource folders and servlets for the WebApp UI.
 * 
 * @author Kai Kreuzer
 *
 */
public class WebAppService {
	
	private final static Logger logger = LoggerFactory.getLogger(WebAppService.class);

	private static final String WEBAPP_ALIAS = "/";
	private HttpService httpService;
	
	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	protected void startup() {
		try {
			logger.debug("Starting up webapp at " + WEBAPP_ALIAS);
			httpService.registerResources(WEBAPP_ALIAS, "web", null);
			
			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(WEBAPP_ALIAS + "openhab", new WebAppServlet(), props, null);
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during servlet startup", e);
		}
	}

	protected void shutdown() {
		httpService.unregister(WEBAPP_ALIAS);
	}
}
