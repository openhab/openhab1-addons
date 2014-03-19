/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.rrd4j.internal.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.items.NumberItem;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.persistence.rrd4j.internal.RRD4jService;
import org.openhab.ui.chart.ChartProvider;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
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
 * @author Chris Jackson
 * @since 1.0.0
 *
 */
public class RRD4jChartServlet implements Servlet, ChartProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(RRD4jChartServlet.class);

	/** the URI of this servlet */
	public static final String SERVLET_NAME = "/rrdchart.png";

	protected static final Color[] LINECOLORS = new Color[] { 
		Color.RED, Color.GREEN, Color.BLUE, 
		Color.MAGENTA, Color.ORANGE, Color.CYAN, 
		Color.PINK, Color.DARK_GRAY, Color.YELLOW };
	protected static final Color[] AREACOLORS = new Color[] { 
		new Color(255, 0, 0, 30), new Color(0, 255, 0, 30), new Color(0, 0, 255, 30), 
		new Color(255, 0, 255, 30), new Color(255, 128, 0, 30), new Color(0, 255, 255, 30), 
		new Color(255, 0, 128, 30), new Color(255, 128, 128, 30), new Color(255, 255, 0, 30)};
	
	protected static final Map<String, Long> PERIODS = new HashMap<String, Long>();
		
	static {
		PERIODS.put("h", -3600000L);
		PERIODS.put("4h", -14400000L);
		PERIODS.put("8h", -28800000L);
		PERIODS.put("12h", -43200000L);
		PERIODS.put("D", -86400000L);
		PERIODS.put("3D", -259200000L);
		PERIODS.put("W", -604800000L);
		PERIODS.put("2W", -1209600000L);
		PERIODS.put("M", -2592000000L);
		PERIODS.put("2M", -5184000000L);
		PERIODS.put("4M", -10368000000L);
		PERIODS.put("Y", -31536000000L);
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
		logger.debug("RRD4J Received incoming chart request: ", req);

		int width = 480;
		try {
			width = Integer.parseInt(req.getParameter("w"));
		} catch (Exception e) {
		}
		int height = 240;
		try {
			height = Integer.parseInt(req.getParameter("h"));
		} catch (Exception e) {
		}
		Long period = PERIODS.get(req.getParameter("period"));
		if (period == null) {
			// use a day as the default period
			period = PERIODS.get("D");
		}
		// Create the start and stop time
		Date timeEnd = new Date();
		Date timeBegin = new Date(timeEnd.getTime() + period);

		// Set the content type to that provided by the chart provider
		res.setContentType("image/"+getChartType());
		try {
			BufferedImage chart = createChart(null, null, timeBegin, timeEnd, height, width, req.getParameter("items"), req.getParameter("groups"));
			ImageIO.write(chart, getChartType().toString(), res.getOutputStream());
		} catch (ItemNotFoundException e) {
			logger.debug("Item not found error while generating chart.");
		} catch (IllegalArgumentException e) {
			logger.debug("Illegal argument in chart: {}", e);
		}
	}

	/**
	 * Adds a line for the item to the graph definition.
	 * The color of the line is determined by the counter, it simply picks the according index from LINECOLORS (and rolls over if necessary).
	 * 
	 * @param graphDef the graph definition to fill
	 * @param item the item to add a line for
	 * @param counter defines the number of the datasource and is used to determine the line color
	 */
	protected void addLine(RrdGraphDef graphDef, Item item, int counter) {
		Color color = LINECOLORS[counter%LINECOLORS.length];
		String label = itemUIRegistry.getLabel(item.getName());
		if(label!=null && label.contains("[") && label.contains("]")) {
			label = label.substring(0, label.indexOf('['));
		}
		if(item instanceof NumberItem) {
			// we only draw a line
			graphDef.datasource(Integer.toString(counter), "./etc/rrd4j/" + item.getName() + ".rrd", "state", RRD4jService.getConsolidationFunction(item));
			graphDef.line(Integer.toString(counter), color, label, 2);
		} else {
			// we draw a line and fill the area beneath it with a transparent color
			graphDef.datasource(Integer.toString(counter), "./etc/rrd4j/" + item.getName() + ".rrd", "state", RRD4jService.getConsolidationFunction(item));
			Color areaColor = AREACOLORS[counter%LINECOLORS.length];
			
			graphDef.area(Integer.toString(counter), areaColor);
			graphDef.line(Integer.toString(counter), color, label, 2);
		}
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

	// ----------------------------------------------------------
	// The following methods implement the ChartServlet interface
	
	@Override
	public String getName() {
		return "rrd4j";
	}

	@Override
	public BufferedImage createChart(String service, String theme, Date startTime, Date endTime, int height, int width,
			String items, String groups) throws ItemNotFoundException {
		RrdGraphDef graphDef = new RrdGraphDef();

		long period = (startTime.getTime() - endTime.getTime()) / 1000;
		
		graphDef.setWidth(width);
		graphDef.setHeight(height);
		graphDef.setAntiAliasing(true);
		graphDef.setImageFormat("PNG");
		graphDef.setStartTime(period);
		graphDef.setTextAntiAliasing(true);
		graphDef.setLargeFont(new Font("SansSerif", Font.PLAIN, 15));
		graphDef.setSmallFont(new Font("SansSerif", Font.PLAIN, 11));
		
		int seriesCounter = 0;

		// Loop through all the items
		if (items != null) {
			String[] itemNames = items.split(",");
			for (String itemName : itemNames) {
				Item item = itemUIRegistry.getItem(itemName);
				addLine(graphDef, item, seriesCounter++);
			}
		}

		// Loop through all the groups and add each item from each group
		if (groups != null) {
			String[] groupNames = groups.split(",");
			for (String groupName : groupNames) {
				Item item = itemUIRegistry.getItem(groupName);
				if (item instanceof GroupItem) {
					GroupItem groupItem = (GroupItem) item;
					for (Item member : groupItem.getMembers()) {
						addLine(graphDef, member, seriesCounter++);
					}
				} else {
					throw new ItemNotFoundException("Item '" + item.getName() + "' defined in groups is not a group.");
				}
			}
		}

		// Write the chart as a PNG image
		RrdGraph graph;
		try {
			graph = new RrdGraph(graphDef);
			BufferedImage bi = new BufferedImage(graph.getRrdGraphInfo().getWidth(), graph.getRrdGraphInfo().getHeight(), BufferedImage.TYPE_INT_RGB);
			graph.render(bi.getGraphics());
			
			return bi;
		} catch (IOException e) {
			logger.error("Error generating graph: {}", e);
		}
		
		return null;
	}

	@Override
	public ImageType getChartType() {
		return ImageType.png;
	}
}
