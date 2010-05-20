package org.openhab.ui.webapp.internal.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main servlet for the WebApp UI. 
 * It serves the Html code based on the sitemap model.
 * 
 * @author Kai Kreuzer
 *
 */
public class WebAppServlet implements javax.servlet.Servlet {

	private static final Logger logger = LoggerFactory.getLogger(WebAppServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		logger.debug("Servlet request received!");

	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
