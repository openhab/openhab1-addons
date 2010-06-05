package org.openhab.ui.webapp.internal.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.emf.common.util.EList;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.WebAppService;
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

	/** the name of the servlet to be used in the URL */
	public static final String SERVLET_NAME = "openhab.app";
	
	private static final Logger logger = LoggerFactory.getLogger(WebAppServlet.class);
	
	private final WebAppService service; 
	
	public WebAppServlet(WebAppService webAppService) {
		service = webAppService;
	}

	public void init(ServletConfig config) throws ServletException {
	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		logger.debug("Servlet request received!");

		// read request parameters
		String sitemapName = (String) req.getParameter("sitemap");
		String widgetId = (String) req.getParameter("w");
		boolean async = "true".equalsIgnoreCase((String) req.getParameter("__async"));
		
		// if there are no parameters, display the "default" sitemap
		if(sitemapName==null) sitemapName = "default";
		
		StringBuilder sb = new StringBuilder();

		Sitemap sitemap = service.getSitemapProvider().getSitemap(sitemapName);
		if(sitemap!=null) {
			logger.debug("reading sitemap {}", sitemap.getName());
			if(widgetId==null) {
				String label = sitemap.getLabel()!=null ? sitemap.getLabel() : sitemapName;
				processPage("Home", label, sitemap.getChildren(), async, sb);
			} else {
				Widget w = service.getWidget(sitemap, widgetId);
				String label = service.getLabel(w);
				if (label==null) label = "undefined";
				EList<Widget> children = service.getChildren((Group) w);
				processPage(service.getWidgetId(w), label, children, async, sb);
			}
		} else {
			throw new ServletException("Sitemap '" + sitemapName + "' could not be found");
		}
		res.getWriter().append(sb);
		res.getWriter().close();
	}

	private void processPage(String id, String label, EList<Widget> children, boolean async, StringBuilder sb) throws IOException, ServletException {
		String snippet = service.getSnippet(async ? "layer" : "main");
		snippet = snippet.replaceAll("%id%", id);
		snippet = snippet.replaceAll("%label%", label);
		snippet = snippet.replaceAll("%servletname%", SERVLET_NAME);
		processChildren(snippet, sb, children);
	}

	private void processChildren(String snippet, StringBuilder sb,
			EList<Widget> children) throws IOException, ServletException {

		// put a single frame around all children widgets, if there are no explicit frames 
		if(!children.isEmpty() && 
				!(children.get(0).eContainer() instanceof Frame || children.get(0).eContainer() instanceof Sitemap)) {
			String frameSnippet = service.getSnippet("frame");
			frameSnippet = frameSnippet.replace("%label%", "");
			snippet = snippet.replace("%children%", frameSnippet);
		}

		String[] parts = snippet.split("%children%");
		sb.append(parts[0]);

		if(parts.length==2) {
			for(Widget w : children) {
				processWidget(w, sb);
			}
			sb.append(parts[1]);
		} else if(parts.length > 2){
			logger.error("Snippet contains multiple %children% sections, but only one is allowed!");
		}
	}

	private void processWidget(Widget w, StringBuilder sb) throws IOException, ServletException {
		String snippet = service.getSnippet(w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1));
		snippet = snippet.replaceAll("%id%", service.getWidgetId(w));
		snippet = snippet.replaceAll("%icon%", service.getIcon(w));
		snippet = snippet.replaceAll("%item%", service.getItem(w));
		snippet = snippet.replaceAll("%label%", service.getLabel(w));
		snippet = snippet.replaceAll("%servletname%", SERVLET_NAME);
		
		if(w instanceof Image) {
			snippet = snippet.replaceAll("%url%", ((Image) w).getUrl());
		}
		
		if(w instanceof Frame) {
			processChildren(snippet, sb, service.getChildren(w));
		} else {
			sb.append(snippet);
		}
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {
	}

}
