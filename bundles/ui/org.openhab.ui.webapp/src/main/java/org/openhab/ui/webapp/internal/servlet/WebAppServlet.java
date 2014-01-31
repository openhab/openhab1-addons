/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.internal.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapProvider;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.render.PageRenderer;
import org.openhab.ui.webapp.render.RenderException;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main servlet for the WebApp UI. 
 * It serves the Html code based on the sitemap model.
 * 
 * @author Kai Kreuzer
 *
 */
public class WebAppServlet extends BaseServlet {

	private static final Logger logger = LoggerFactory.getLogger(WebAppServlet.class);

	/** timeout for polling requests in milliseconds; if no state changes during this time, 
	 *  an empty response is returned.
	 */
	private static final long TIMEOUT_IN_MS = 10000L;

	/** the name of the servlet to be used in the URL */
	public static final String SERVLET_NAME = "openhab.app";
		
	private PageRenderer renderer;
	protected SitemapProvider sitemapProvider;
	
	
	public void setSitemapProvider(SitemapProvider sitemapProvider) {
		this.sitemapProvider = sitemapProvider;
	}

	public void unsetSitemapProvider(SitemapProvider sitemapProvider) {
		this.sitemapProvider = null;
	}
	
	public void setPageRenderer(PageRenderer renderer) {
		this.renderer = renderer;
	}
	
	
	protected void activate() {
		try {			
			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(WEBAPP_ALIAS + SERVLET_NAME, this, props, createHttpContext());
			httpService.registerResources(WEBAPP_ALIAS, "web", null);
			logger.info("Started Classic UI at " + WEBAPP_ALIAS + SERVLET_NAME);
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during servlet startup", e);
		}
	}
	
	protected void deactivate() {
		httpService.unregister(WEBAPP_ALIAS + SERVLET_NAME);
		httpService.unregister(WEBAPP_ALIAS);
		logger.info("Stopped Classic UI");
	}
	
	/**
	 * {@inheritDoc}
	 */
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
		
		Sitemap sitemap = sitemapProvider.getSitemap(sitemapName);
		try {
			if(sitemap==null) {
				throw new RenderException("Sitemap '" + sitemapName + "' could not be found");
			}
			logger.debug("reading sitemap {}", sitemap.getName());
			if(widgetId==null || widgetId.isEmpty() || widgetId.equals("Home")) {
				// we are at the homepage, so we render the children of the sitemap root node
				String label = sitemap.getLabel()!=null ? sitemap.getLabel() : sitemapName;
				EList<Widget> children = sitemap.getChildren();
				if(poll && waitForChanges(children)==false) {
					// we have reached the timeout, so we do not return any content as nothing has changed
					res.getWriter().append(getTimeoutResponse()).close();
					return;
				}
				result.append(renderer.processPage("Home", sitemapName, label, sitemap.getChildren(), async));
			} else if(!widgetId.equals("Colorpicker")) {
				// we are on some subpage, so we have to render the children of the widget that has been selected
				Widget w = renderer.getItemUIRegistry().getWidget(sitemap, widgetId);
				if(w!=null) {
					String label = renderer.getItemUIRegistry().getLabel(w);
					if (label==null) label = "undefined";
					if(!(w instanceof LinkableWidget)) {
						throw new RenderException("Widget '" + w + "' can not have any content");
					}
					EList<Widget> children = renderer.getItemUIRegistry().getChildren((LinkableWidget) w);
					if(poll && waitForChanges(children)==false) {
						// we have reached the timeout, so we do not return any content as nothing has changed
						res.getWriter().append(getTimeoutResponse()).close();
						return;
					}
					result.append(renderer.processPage(renderer.getItemUIRegistry().getWidgetId(w), sitemapName, label, children, async));
				}
			}
		} catch(RenderException e) {
			throw new ServletException(e.getMessage(), e);
		}
		if(async) {
			res.setContentType("application/xml;charset=UTF-8");
		} else {
			res.setContentType("text/html;charset=UTF-8");
		}
		res.getWriter().append(result);
		res.getWriter().close();
	}

	/**
	 * Defines the response to return on a polling timeout.
	 * 
	 * @return the response of the servlet on a polling timeout
	 */
	private String getTimeoutResponse() {
		return "<root><part><destination mode=\"replace\" zone=\"timeout\" create=\"false\"/><data/></part></root>";
	}

	/**
	 * This method only returns when a change has occurred to any item on the page to display
	 * 
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
		do {
			timeout = (new Date()).getTime() - startTime > TIMEOUT_IN_MS;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				timeout = true;
				break;
			}
		} while(!listener.hasChangeOccurred() && !timeout);
		for(GenericItem item : items) {
			item.removeStateChangeListener(listener);
		}
		return !timeout;
	}

	/**
	 * Collects all items that are represented by a given list of widgets
	 * 
	 * @param widgets the widget list to get the items for
	 * @return all items that are represented by the list of widgets
	 */
	private Set<GenericItem> getAllItems(EList<Widget> widgets) {
		Set<GenericItem> items = new HashSet<GenericItem>();
		if(itemRegistry!=null) {
			for(Widget widget : widgets) {
				String itemName = widget.getItem();
				if(itemName!=null) {
					try {
						Item item = itemRegistry.getItem(itemName);
						if (item instanceof GenericItem) {
							final GenericItem gItem = (GenericItem) item;
							items.add(gItem);
						}
					} catch (ItemNotFoundException e) {
						// ignore
					}
				} else {
					if(widget instanceof Frame) {
						items.addAll(getAllItems(((Frame) widget).getChildren()));
					}
				}
			}
		}
		return items;
	}

	/**
	 * This is a state change listener, which is merely used to determine, if a state
	 * change has occurred on one of a list of items.
	 * 
	 * @author Kai Kreuzer
	 *
	 */
	private static class BlockingStateChangeListener implements StateChangeListener {
		
		private boolean changed = false;
		
		/**
		 * {@inheritDoc}
		 */
		public void stateChanged(Item item, State oldState, State newState) {
			changed = true;
		}

		/**
		 * determines, whether a state change has occurred since its creation
		 * 
		 * @return true, if a state has changed
		 */
		public boolean hasChangeOccurred() {
			return changed;
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateUpdated(Item item, State state) {
			// ignore if the state did not change
		}
	}
	
	
}
