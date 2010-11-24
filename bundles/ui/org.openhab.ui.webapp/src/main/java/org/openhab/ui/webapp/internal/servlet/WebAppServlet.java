/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.ui.webapp.internal.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Switch;
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
				processPage("Home", sitemapName, label, sitemap.getChildren(), async, sb);
			} else {
				Widget w = service.getWidget(sitemap, widgetId);
				String label = service.getLabel(w);
				if (label==null) label = "undefined";
				if(w instanceof LinkableWidget) {
					EList<Widget> children = service.getChildren((LinkableWidget) w);
					processPage(service.getWidgetId(w), sitemapName, label, children, async, sb);
				} else {
					throw new ServletException("Widget '" + w + "' can not have any content");
				}
			}
		} else {
			throw new ServletException("Sitemap '" + sitemapName + "' could not be found");
		}
		
		if(async) {
			res.setContentType("application/xml;charset=UTF-8");
		} else {
			res.setContentType("text/html;charset=UTF-8");
		}
		res.getWriter().append(sb);
		res.getWriter().close();
	}

	private void processPage(String id, String sitemap, String label, EList<Widget> children, boolean async, StringBuilder sb) throws IOException, ServletException {
		String snippet = service.getSnippet(async ? "layer" : "main");
		snippet = snippet.replaceAll("%id%", id);
		// if the label contains a value span, we remove this span as
		// the title of a page/layer cannot deal with this
		if(label.contains("<span>")) {
			label = label.substring(0, label.indexOf("<span>"));
		}
		snippet = snippet.replaceAll("%label%", label);
		snippet = snippet.replaceAll("%servletname%", SERVLET_NAME);
		snippet = snippet.replaceAll("%sitemap%", sitemap);
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
		String snippetName;
		if(w instanceof Switch) {
			Item item;
			try {
				item = service.getItemRegistry().getItem(service.getItem(w));
				if(item instanceof RollershutterItem) {
					snippetName = "rollerblind";
				} else {
					snippetName = "switch";
				}
			} catch (ItemNotFoundException e) {
				logger.warn("Cannot determine item type of '{}'", service.getItem(w), e);
				snippetName = "switch";
			} catch (ItemNotUniqueException e) {
				logger.warn("Cannot determine item type of '{}'", service.getItem(w), e);
				snippetName = "switch";
			}
		} else {
			// for all others, we choose the snippet with the name of the instance
			snippetName = w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1);
		}
		
		if(!(w instanceof Frame || w instanceof Group) && 
			(w instanceof LinkableWidget) && ((LinkableWidget)w).getChildren().size() > 0) {
			snippetName += "_link";
		}
		String snippet = service.getSnippet(snippetName);

		snippet = snippet.replaceAll("%id%", service.getWidgetId(w));
		snippet = snippet.replaceAll("%icon%", service.getIcon(w));
		snippet = snippet.replaceAll("%item%", service.getItem(w));
		snippet = snippet.replaceAll("%label%", service.getLabel(w));
		snippet = snippet.replaceAll("%servletname%", SERVLET_NAME);
		
		if(w instanceof Switch) {
			State state = service.getState(w);
			if(state.equals(OnOffType.ON)) {
				snippet = snippet.replaceAll("%checked%", "checked=true");
			} else {
				snippet = snippet.replaceAll("%checked%", "");
			}
		}
		
		if(w instanceof Image) {
			snippet = snippet.replaceAll("%url%", ((Image) w).getUrl());
		}
		
		if(w instanceof Frame) {
			processChildren(snippet, sb, service.getChildren((Frame)w));
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
