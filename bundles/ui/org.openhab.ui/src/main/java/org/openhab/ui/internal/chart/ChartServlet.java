/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.internal.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager.ChartTheme;

/**
 * This servlet generates time-series charts for a given set of items. It
 * accepts the following HTTP parameters:
 * <ul>
 * <li>w: width in pixels of image to generate</li>
 * <li>h: height in pixels of image to generate</li>
 * <li>period: the time span for the x-axis. Value can be
 * h,4h,8h,12h,D,3D,W,2W,M,2M,4M,Y</li>
 * <li>items: A comma separated list of item names to display
 * <li>groups: A comma separated list of group names, whose members should be
 * displayed
 * </ul>
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */

public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 7700873790924746422L;

	private static final Logger logger = LoggerFactory.getLogger(ChartServlet.class);

	// the URI of this servlet
	public static final String SERVLET_NAME = "/chart.png";

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
	static protected Map<String, QueryablePersistenceService> persistenceServices = new HashMap<String, QueryablePersistenceService>();

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

	public void addPersistenceService(PersistenceService service) {
		if (service instanceof QueryablePersistenceService)
			persistenceServices.put(service.getName(), (QueryablePersistenceService) service);
	}

	public void removePersistenceService(PersistenceService service) {
		persistenceServices.remove(service.getName());
	}

	static public Map<String, QueryablePersistenceService> getPersistenceServices() {
		return persistenceServices;
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

		// Create Chart
		Chart chart = new ChartBuilder().width(width).height(height).theme(ChartTheme.Matlab).build();
		// chart.getStyleManager().setPlotGridLinesVisible(false);

		String serviceName = "mysql";
		QueryablePersistenceService service = getPersistenceServices().get(serviceName);
		if (service == null) {
			logger.debug("Persistence service not found '{}'.", serviceName);
			throw new ServletException("Persistence service not found.");
		}

		if (!(service instanceof QueryablePersistenceService)) {
			logger.debug("Persistence service not queryable '{}'.", serviceName);
			throw new ServletException("Persistence service not queryable.");
		}

		Date dateTimeEnd = new Date();
		Date dateTimeBegin = new Date(dateTimeEnd.getTime() - period);

		configureContents(service, dateTimeBegin, dateTimeEnd, chart, req);

		BufferedImage bi = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
		res.setContentType("image/png");

		BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D lGraphics2D = lBufferedImage.createGraphics();
		chart.paint(lGraphics2D);

		ImageIO.write(lBufferedImage, "png", res.getOutputStream());

		javax.imageio.ImageIO.write(bi, "png", res.getOutputStream());
	}

	/**
	 * Adds the content for the chart
	 * 
	 * @param graphDef
	 *            the graph definition to fill
	 * @param req
	 *            the HTTP request to read the parameters from
	 */
	protected void configureContents(QueryablePersistenceService service, Date dateTimeBegin, Date dateTimeEnd,
			Chart graphDef, HttpServletRequest req) throws ServletException {
		int counter = 0;
		String itemList = req.getParameter("items");
		if (itemList != null) {
			String[] itemNames = itemList.split(",");
			for (String itemName : itemNames) {
				try {
					Item item = itemUIRegistry.getItem(itemName);
					addItem(service, dateTimeBegin, dateTimeEnd, graphDef, item, counter++);
				} catch (ItemNotFoundException e) {
					throw new ServletException("Item '" + itemName + "' does not exist!");
				}
			}
		}

		String groupList = req.getParameter("groups");
		if (groupList != null) {
			String[] groupNames = groupList.split(",");
			for (String groupName : groupNames) {
				try {
					Item item = itemUIRegistry.getItem(groupName);
					if (item instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) item;
						for (Item member : groupItem.getMembers()) {
							addItem(service, dateTimeBegin, dateTimeEnd, graphDef, member, counter++);
						}
					} else {
						throw new ServletException("Item '" + groupName + "' is no group item!");
					}
				} catch (ItemNotFoundException e) {
					throw new ServletException("Group item '" + groupName + "' does not exist!");
				}
			}
		}

		if (counter == 0) {
			throw new ServletException(
					"At least one item must be specified using either the 'items' or 'groups' parameter in the request!");
		}
	}

	/**
	 * Adds a line for the item to the graph definition. The color of the line
	 * is determined by the counter, it simply picks the according index from
	 * LINECOLORS (and rolls over if necessary).
	 * 
	 * @param graphDef
	 *            the graph definition to fill
	 * @param item
	 *            the item to add a line for
	 * @param counter
	 *            defines the number of the datasource and is used to determine
	 *            the line color
	 */
	protected void addItem(QueryablePersistenceService service, Date dateTimeBegin, Date dateTimeEnd, Chart chart,
			Item item, int counter) {
		Color color = LINECOLORS[counter % LINECOLORS.length];
		String label = itemUIRegistry.getLabel(item.getName());
		if (label != null && label.contains("[") && label.contains("]")) {
			label = label.substring(0, label.indexOf('['));
		}
		if (label == null)
			label = item.getName();

		FilterCriteria filter = new FilterCriteria();
		filter.setBeginDate(dateTimeBegin);
		filter.setEndDate(dateTimeEnd);
		filter.setItemName(item.getName());
		filter.setOrdering(Ordering.ASCENDING);

		Iterable<HistoricItem> result = service.query(filter);
		Iterator<HistoricItem> it = result.iterator();

		// generate data
		Collection<Date> xData = new ArrayList<Date>();
		Collection<Number> yData = new ArrayList<Number>();

		// Iterate through the data
		while (it.hasNext()) {
			HistoricItem historicItem = it.next();
			org.openhab.core.types.State state = historicItem.getState();
			if (state instanceof DecimalType) {
				DecimalType value = (DecimalType) state;

				xData.add(historicItem.getTimestamp());
				yData.add(value);
			}
		}

		Series series = chart.addDateSeries(label, xData, yData);
		series.setMarker(SeriesMarker.NONE);
		series.setLineColor(color);
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
