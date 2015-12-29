/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.internal.chart;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.ui.chart.ChartProvider;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This servlet generates time-series charts for a given set of items. It
 * accepts the following HTTP parameters:
 * <ul>
 * <li>w: width in pixels of image to generate</li>
 * <li>h: height in pixels of image to generate</li>
 * <li>period: the time span for the x-axis. Value can be
 * h,4h,8h,12h,D,3D,W,2W,M,2M,4M,Y</li>
 * <li>items: A comma separated list of item names to display</li>
 * <li>groups: A comma separated list of group names, whose members should be
 * displayed</li>
 * <li>service: The persistence service name. If not supplied the first service found will be used.</li>
 * </ul>
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */

public class ChartServlet extends HttpServlet implements ManagedService {

	private static final long serialVersionUID = 7700873790924746422L;
	private static final Integer CHART_HEIGHT = 240;
	private static final Integer CHART_WIDTH = 480;
	private static final String dateFormat = "yyyyMMddHHmm";

	private static final DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
	
	private static final Logger logger = LoggerFactory.getLogger(ChartServlet.class);	

	protected String providerName = "default";
	protected Integer defaultHeight = CHART_HEIGHT;
	protected Integer defaultWidth = CHART_WIDTH;
	protected Double scale = 1.0;
	
	// The URI of this servlet
	public static final String SERVLET_NAME = "/chart";

	protected static final Color[] LINECOLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA,
			Color.ORANGE, Color.CYAN, Color.PINK, Color.DARK_GRAY, Color.YELLOW };
	protected static final Color[] AREACOLORS = new Color[] { new Color(255, 0, 0, 30), new Color(0, 255, 0, 30),
			new Color(0, 0, 255, 30), new Color(255, 0, 255, 30), new Color(255, 128, 0, 30),
			new Color(0, 255, 255, 30), new Color(255, 0, 128, 30), new Color(255, 128, 128, 30),
			new Color(255, 255, 0, 30) };

	protected static final Map<String, Long> PERIODS = new HashMap<String, Long>();

	static {
		PERIODS.put("h", 3600000L);
		PERIODS.put("4h", 14400000L);
		PERIODS.put("8h", 28800000L);
		PERIODS.put("12h", 43200000L);
		PERIODS.put("D", 86400000L);
		PERIODS.put("3D", 259200000L);
		PERIODS.put("W", 604800000L);
		PERIODS.put("2W", 1209600000L);
		PERIODS.put("M", 2592000000L);
		PERIODS.put("2M", 5184000000L);
		PERIODS.put("4M", 10368000000L);
		PERIODS.put("Y", 31536000000L);
	}

	protected HttpService httpService;
	protected ItemUIRegistry itemUIRegistry;
	static protected Map<String, ChartProvider> chartProviders = new HashMap<String, ChartProvider>();

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

	public void addChartProvider(ChartProvider provider) {
		chartProviders.put(provider.getName(), provider);
	}

	public void removeChartProvider(ChartProvider provider) {
		chartProviders.remove(provider.getName());
	}

	static public Map<String, ChartProvider> getChartProviders() {
		return chartProviders;
	}

	protected void activate() {
		try {
			logger.debug("Starting up chart servlet at " + SERVLET_NAME);

			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(SERVLET_NAME, this, props, createHttpContext());

		} catch (NamespaceException e) {
			logger.error("Error during chart servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during chart servlet startup", e);
		}
	}

	protected void deactivate() {
		httpService.unregister(SERVLET_NAME);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.debug("Received incoming chart request: ", req);

		int width = defaultWidth;

		try {
			String w = req.getParameter("w");
			if(w != null) {
				Double d = Double.parseDouble(w) * scale;
				width = d.intValue();
			}
		} catch (Exception e) {
		}
		int height = defaultHeight;
		try {
			String h = req.getParameter("h");
			if(h != null) {
				Double d = Double.parseDouble(h) * scale;
				height = d.intValue();
			}
		} catch (Exception e) {
		}
		

		//To avoid ambiguity you are not allowed to specify period, begin and end time at the same time.
		if (req.getParameter("period") != null
			&& req.getParameter("begin") != null && req.getParameter("end") != null) {
			throw new ServletException("Do not specify the three parameter period, begin and" +
				"end at the same time.");
		}
		

		//Read out the parameter period, begin and end and save them.
		Date timeBegin = null;
		Date timeEnd = null;
		
		Long period = PERIODS.get(req.getParameter("period"));			
		if (period == null && (req.getParameter("begin") == null || req.getParameter("end") == null)) {			
			// use a day as the default period
			period = PERIODS.get("D");
			logger.debug("Use a day as the period (default period).");			
		}
					
		if (req.getParameter("begin") != null) {
			try {
				timeBegin = dateFormatter.parse(req.getParameter("begin"));
			} catch (ParseException e) {
				throw new ServletException("Begin and end must have this format: " + dateFormat + ".");
			}
		}

		if (req.getParameter("end") != null) {
			try {				
				timeEnd = dateFormatter.parse(req.getParameter("end"));
			} catch (ParseException e) {
				throw new ServletException("Begin and end must have this format: " + dateFormat + ".");
			}
		}


		//Set begin and end time and check legality.		
		if (timeBegin == null && timeEnd == null) {
			timeEnd = new Date();
			timeBegin = new Date(timeEnd.getTime() - period);
			logger.debug("No begin and end are specified, use now as end and now - period as begin.");
		}
		else if (timeEnd == null) {
			timeEnd = new Date(timeBegin.getTime() + period);
			logger.debug("No end is specified, use begin + period as end.");
		}
		else if (timeBegin == null) {
			timeBegin = new Date(timeEnd.getTime() - period);
			logger.debug("No begin is specified, use end - period as begin");
		}
		else if (timeEnd.before(timeBegin)) {			
			throw new ServletException("The end is before the begin.");
		}


		// If a persistence service is specified, find the provider
		String serviceName = req.getParameter("service");

		ChartProvider provider = getChartProviders().get(providerName);
		if (provider == null)
			throw new ServletException("Could not get chart provider.");

		// Set the content type to that provided by the chart provider
		res.setContentType("image/" + provider.getChartType());
		try {
			BufferedImage chart = provider.createChart(serviceName, null, timeBegin, timeEnd, height, width,
					req.getParameter("items"), req.getParameter("groups"));
			ImageIO.write(chart, provider.getChartType().toString(), res.getOutputStream());
		} catch (ItemNotFoundException e) {
			logger.info("Item not found error while generating chart: {}", e);
		} catch (IllegalArgumentException e) {
			logger.info("Illegal argument in chart: {}", e);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		if(properties == null)
			return;

		if(properties.get("provider") != null) {
			providerName = (String) properties.get("provider");
		}
		if(properties.get("defaultHeight") != null) {
			defaultHeight = Integer.parseInt((String)properties.get("defaultHeight"));
		}
		if(properties.get("defaultWidth") != null) {
			defaultWidth = Integer.parseInt((String)properties.get("defaultWidth"));
		}
		if(properties.get("scale") != null) {
			scale = Double.parseDouble((String)properties.get("scale"));
			// Set scale to normal if the custom value is unrealisticly low
			if(scale < 0.1) {
				scale = 1.0;
			}
		}
	}

}
