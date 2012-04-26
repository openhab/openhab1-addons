/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.persistence.rrd4j.internal.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.rrd4j.ConsolFun;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet generates time-series charts for a given set of items.
 * It accepts the following HTTP parameters:
 * <ul>
 * 	<li>w: width in pixels of image to generate</li>
 * 	<li>h: height in pixels of image to generate</li>
 * 	<li>period: the time span for the x-axis. Value can be h,4h,8h,12h,D,3D,W,2W,M,2M,4M,Y</li>
 * 	<li>items: A comma separated list of item names to display
 * 	<li>groups: A comma separated list of group names, whose members should be displayed 
 * </ul>
 *  
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class RRD4jChartServlet implements Servlet {
	
	private static final Logger logger = LoggerFactory.getLogger(RRD4jChartServlet.class);

	/** the URI of this servlet */
	public static final String SERVLET_NAME = "/rrdchart.png";

	protected static final Color[] LINECOLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK, Color.DARK_GRAY, Color.YELLOW };
	
	protected static final Map<String, Long> PERIODS = new HashMap<String, Long>();
		
	static {
		PERIODS.put("h", -3600L);
		PERIODS.put("4h", -14400L);
		PERIODS.put("8h", -28800L);
		PERIODS.put("12h", -43200L);
		PERIODS.put("D", -86400L);
		PERIODS.put("3D", -259200L);
		PERIODS.put("W", -604800L);
		PERIODS.put("2W", -1209600L);
		PERIODS.put("M", -2592000L);
		PERIODS.put("2M", -5184000L);
		PERIODS.put("4M", -10368000L);
		PERIODS.put("Y", -31536000L);
	}
	
	protected HttpService httpService;
	protected ItemUIRegistry itemUIRegistry;

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}

	public void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = null;
	}

	protected void activate() {
		try {
			logger.debug("Starting up rrd chart servlet at " + SERVLET_NAME);

			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(SERVLET_NAME, this, props, createHttpContext());

		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during servlet startup", e);
		}
	}

	protected void deactivate() {
		httpService.unregister(SERVLET_NAME);
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		RrdGraphDef graphDef = new RrdGraphDef();

		configureImageParameters(graphDef, req);
		configureContents(graphDef, req);

		try {
			RrdGraph graph = new RrdGraph(graphDef);
			BufferedImage bi = new BufferedImage(graph.getRrdGraphInfo().getWidth(), graph.getRrdGraphInfo().getHeight(), BufferedImage.TYPE_INT_RGB);
			graph.render(bi.getGraphics());			
			javax.imageio.ImageIO.write(bi, "png", res.getOutputStream());
		} catch(FileNotFoundException e) {
			throw new ServletException("Could not read database files for all requested items.", e);
		}

	}

	/**
	 * Sets the overall layout and rendering parameters for the chart
	 * 
	 * @param graphDef the graph definition to fill
	 * @param req the HTTP request to read the parameters from
	 */
	protected void configureImageParameters(RrdGraphDef graphDef, ServletRequest req) {
		int width = 480;
		try {
			width = Integer.parseInt(req.getParameter("w"));
		} catch(Exception e) {}
		int height = 240;
		try {
			height = Integer.parseInt(req.getParameter("h"));
		} catch(Exception e) {}
		Long period = PERIODS.get(req.getParameter("period"));
		if(period==null) {
			// use a day as the default period
			period = PERIODS.get("D");
		}
		
		graphDef.setWidth(width);
		graphDef.setHeight(height);
		graphDef.setAntiAliasing(true);
		graphDef.setStartTime(period);
		graphDef.setTextAntiAliasing(true);
		graphDef.setLargeFont(new Font("SansSerif", Font.PLAIN, 15));
		graphDef.setSmallFont(new Font("SansSerif", Font.PLAIN, 11));
	}

	/**
	 * Adds the content for the chart
	 * 
	 * @param graphDef the graph definition to fill
	 * @param req the HTTP request to read the parameters from
	 */
	protected void configureContents(RrdGraphDef graphDef, ServletRequest req) throws ServletException {
		int counter = 0;
		String itemList = req.getParameter("items");
		if(itemList!=null) {
			String[] itemNames = itemList.split(",");
			for(String itemName : itemNames) {
				addLine(graphDef, itemName, counter++);
			}
		}
		
		String groupList = req.getParameter("groups");
		if(groupList!=null) {
			String[] groupNames = groupList.split(",");
			for(String groupName : groupNames) {
				try {
					Item item = itemUIRegistry.getItem(groupName);
					if(item instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) item;
						for(Item member : groupItem.getMembers()) {
							addLine(graphDef, member.getName(), counter++);
						}
					} else {
						throw new ServletException("Item '" + groupName + "' is no group item!");
					}
				} catch (ItemNotFoundException e) {
					throw new ServletException("Group item '" + groupName + "' does not exist!");
				}
			}
		}
		
		if(counter==0) {
			throw new ServletException("At least one item must be specified using either the 'items' or 'groups' parameter in the request!");
		}
	}

	protected void addLine(RrdGraphDef graphDef, String itemName, int counter) {
		Color color = LINECOLORS[counter%LINECOLORS.length];
		String label = itemUIRegistry.getLabel(itemName);
		if(label.contains("[") && label.contains("]")) {
			label = label.substring(0, label.indexOf('['));
		}
		graphDef.datasource(Integer.toString(counter), "./etc/rrd4j/" + itemName + ".rrd", "state", ConsolFun.AVERAGE);
		graphDef.line(Integer.toString(counter), color, label, 2);
	}

	/**
	 * Creates a {@link SecureHttpContext} which handles the security for this
	 * servlet
	 * 
	 * @return a {@link SecureHttpContext}
	 */
	protected HttpContext createHttpContext() {
		HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
		return new SecureHttpContext(defaultHttpContext, "openHAB.org");
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * {@inheritDoc}
	 */
	public ServletConfig getServletConfig() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getServletInfo() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
	}

}
