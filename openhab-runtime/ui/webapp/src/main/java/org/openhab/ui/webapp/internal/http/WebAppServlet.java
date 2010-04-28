package org.openhab.ui.webapp.internal.http;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebAppServlet {
	private static final String WEBAPP_ALIAS = "/webapp";
	private HttpService httpService;

	private final static Logger logger = LoggerFactory.getLogger(WebAppServlet.class);
	
	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	protected void startup() {
		try {
			logger.debug("Starting up webapp at " + WEBAPP_ALIAS);
			httpService.registerResources(WEBAPP_ALIAS, "src/main/resources", null);
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		}
	}

	protected void shutdown() {
		httpService.unregister(WEBAPP_ALIAS);
	}
}
