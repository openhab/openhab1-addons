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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.ui.chart.ChartProvider;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;

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
 * <li>service: The persistence service name. If not supplied the first service
 * found will be used.</li>
 * </ul>
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */

public class DefaultChartProvider implements ChartProvider {

	private static final Logger logger = LoggerFactory.getLogger(DefaultChartProvider.class);

	protected static final Color[] LINECOLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA,
			Color.ORANGE, Color.CYAN, Color.PINK, Color.DARK_GRAY, Color.YELLOW };
	protected static final Color[] AREACOLORS = new Color[] { new Color(255, 0, 0, 30), new Color(0, 255, 0, 30),
			new Color(0, 0, 255, 30), new Color(255, 0, 255, 30), new Color(255, 128, 0, 30),
			new Color(0, 255, 255, 30), new Color(255, 0, 128, 30), new Color(255, 128, 128, 30),
			new Color(255, 255, 0, 30) };

	protected ItemUIRegistry itemUIRegistry;
	static protected Map<String, QueryablePersistenceService> persistenceServices = new HashMap<String, QueryablePersistenceService>();

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
		logger.debug("Starting up default chart provider.");
	}

	protected void deactivate() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
	}

	public String getName() {
		return "default";
	}

	@Override
	public BufferedImage createChart(String service, String theme, Date startTime, Date endTime,
			int height, int width, String items, String groups) throws ItemNotFoundException {

		QueryablePersistenceService persistenceService;

		int seriesCounter = 0;

		// Create Chart
		Chart chart = new ChartBuilder().width(width).height(height).build();
		// chart.getStyleManager().setPlotGridLinesVisible(false);

		// Set the background color opaque
		// Color bkgndColor = new Color(Color.TRANSLUCENT);
		// chart.getStyleManager().setChartBackgroundColor(bkgndColor);
		// chart.getStyleManager().setPlotBackgroundColor(ChartColor.)

		// Set the legend below the chart - makes more room!
		// chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);

		persistenceService = null;

		// If a persistence service is specified, find the provider
		if (service != null) {
			persistenceService = getPersistenceServices().get(service);
		} else {
			// Otherwise, just get the first service
			persistenceService = getPersistenceServices().entrySet().iterator().next().getValue();
		}

		// Did we find a service?
		if (persistenceService == null) {
			logger.debug("Persistence service not found '{}'.", service);
			return null;
		}

		// Loop through all the items
		if (items != null) {
			String[] itemNames = items.split(",");
			for (String itemName : itemNames) {
				Item item = itemUIRegistry.getItem(itemName);
				addItem(chart, persistenceService, startTime, endTime, item, seriesCounter);
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
						addItem(chart, persistenceService, startTime, endTime, member, seriesCounter);
					}
				} else {
					throw new ItemNotFoundException("Item '" + item.getName() + "' defined in groups is not a group.");
				}
			}
		}

		// Write the chart as a PNG image
		BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D lGraphics2D = lBufferedImage.createGraphics();
		chart.paint(lGraphics2D);
		return lBufferedImage;
	}

	void addItem(Chart chart, QueryablePersistenceService service, Date timeBegin, Date timeEnd, Item item,
			int seriesCounter) {
		Color color = LINECOLORS[seriesCounter % LINECOLORS.length];
		seriesCounter++;

		// Get the item label
		String label = null;
		if (itemUIRegistry != null) {
			// Get the item label
			label = itemUIRegistry.getLabel(item.getName());
			if (label != null && label.contains("[") && label.contains("]")) {
				label = label.substring(0, label.indexOf('['));
			}
		}
		if (label == null)
			label = item.getName();

		// Define the data filter
		FilterCriteria filter = new FilterCriteria();
		filter.setBeginDate(timeBegin);
		filter.setEndDate(timeEnd);
		filter.setItemName(item.getName());
		filter.setOrdering(Ordering.ASCENDING);

		// Get the data from the persistence store
		Iterable<HistoricItem> result = service.query(filter);
		Iterator<HistoricItem> it = result.iterator();

		// Generate data collections
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

		// Add the new series to the chart
		Series series = chart.addDateSeries(label, xData, yData);
		series.setMarker(SeriesMarker.NONE);
		series.setLineColor(color);
	}

	@Override
	public String getChartType() {
		return ("image/png");
	}
}
