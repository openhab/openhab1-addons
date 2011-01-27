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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.WebAppService;
import org.openhab.ui.webapp.internal.render.PageRenderer;
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

	private static final long TIMEOUT_IN_MS = 30000L;

	/** the name of the servlet to be used in the URL */
	public static final String SERVLET_NAME = "openhab.app";
	
	private static final Logger logger = LoggerFactory.getLogger(WebAppServlet.class);
	
	private final WebAppService service; 
	
	private final PageRenderer renderer;
	
	public WebAppServlet(WebAppService webAppService) {
		service = webAppService;
		renderer = new PageRenderer(service);
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
		boolean poll = "true".equalsIgnoreCase((String) req.getParameter("poll"));
				
		// if there are no parameters, display the "default" sitemap
		if(sitemapName==null) sitemapName = "default";
		
		StringBuilder result = new StringBuilder();
		
		Sitemap sitemap = service.getSitemapProvider().getSitemap(sitemapName);
		if(sitemap!=null) {
			logger.debug("reading sitemap {}", sitemap.getName());
			if(widgetId==null || widgetId.isEmpty() || widgetId.equals("Home")) {
				String label = sitemap.getLabel()!=null ? sitemap.getLabel() : sitemapName;
				EList<Widget> children = sitemap.getChildren();
				if(poll) {
					if(waitForChanges(children)==false) {
						// we have reached the timeout, so we do not return any content as nothing has changed
						res.getWriter().append(getTimeoutResponse()).close();
						return;
					}
				}
				result.append(renderer.processPage("Home", sitemapName, label, sitemap.getChildren(), async));
			} else {
				Widget w = service.getWidget(sitemap, widgetId);
				String label = service.getLabel(w);
				if (label==null) label = "undefined";
				if(w instanceof LinkableWidget) {
					EList<Widget> children = service.getChildren((LinkableWidget) w);
					if(poll) {
						if(waitForChanges(children)==false) {
							// we have reached the timeout, so we do not return any content as nothing has changed
							res.getWriter().append(getTimeoutResponse()).close();
							return;
						}
					}
					result.append(renderer.processPage(service.getWidgetId(w), sitemapName, label, children, async));
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
		res.getWriter().append(result);
		res.getWriter().close();
	}

	private String getTimeoutResponse() {
		return "<root><part><destination mode=\"replace\" zone=\"timeout\" create=\"false\"/><data/></part></root>";
	}

	/**
	 * This method only returns when a change has occurred to any item on the page to display
	 * @param widgets the widgets of the page to observe
	 */
	private boolean waitForChanges(EList<Widget> widgets) {
		long startTime = (new Date()).getTime();
		boolean timeout = false;
		BlockingStateChangeListener listener = new BlockingStateChangeListener();
		// let's get all items for these widgets
		Set<GenericItem> items = getAllItems(widgets);
		for(GenericItem item : items) {			
			item.addStateChangeListener(listener);
		}
		while(!listener.hasChangeOccurred() && !timeout) {
			timeout = (new Date()).getTime() - startTime > TIMEOUT_IN_MS;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				timeout = true;
				break;
			}
		}
		for(GenericItem item : items) {
			item.removeStateChangeListener(listener);
		}
		return !timeout;
	}

	private Set<GenericItem> getAllItems(EList<Widget> widgets) {
		Set<GenericItem> items = new HashSet<GenericItem>();
		ItemRegistry registry = service.getItemRegistry();
		if(registry!=null) {
			for(Widget widget : widgets) {
				String itemName = service.getItem(widget);
				if(itemName!=null) {
					try {
						Item item = registry.getItem(itemName);
						if (item instanceof GenericItem) {
							final GenericItem gItem = (GenericItem) item;
							items.add(gItem);
						}
					} catch (ItemNotFoundException e) {
						// ignore
					} catch (ItemNotUniqueException e) {
						// ignore
					}
				} else {
					if(widget instanceof Frame) {
						items.addAll(getAllItems(service.getChildren((Frame) widget)));
					}
				}
			}
		}
		return items;
	}

	private class BlockingStateChangeListener implements StateChangeListener {
		private boolean changed = false;
		
		@Override
		public void stateChanged(Item item, State oldState, State newState) {
			changed = true;
		}

		public boolean hasChangeOccurred() {
			return changed;
		}

		@Override
		public void stateUpdated(Item item, State state) {
			// ignore if the state did not change
		}
	}
	
	public String getServletInfo() {
		return null;
	}

	public void destroy() {
	}

}
