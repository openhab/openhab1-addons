/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.internal.chart;

import java.awt.Color;
import java.awt.Font;
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

import java.awt.BasicStroke;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager.LegendPosition;

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

	private int legendPosition = 0;
	
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
			int height, int width, String items, String groups) throws ItemNotFoundException, IllegalArgumentException {

		QueryablePersistenceService persistenceService;

		int seriesCounter = 0;

		// Create Chart
		Chart chart = new ChartBuilder().width(width).height(height).build();
		
		// Define the time axis - the defaults are not very nice
		long period = (endTime.getTime() - startTime.getTime()) / 1000;
		String pattern = "HH:mm";
		if(period <= 600) {				// 10 minutes
			pattern = "mm:ss";
		}
		else if(period <= 86400) {		// 1 day
			pattern = "HH:mm";
		}
		else if(period <= 604800) {		// 1 week
			pattern = "EEE d";
		}
		else {
			pattern = "d MMM";
		}

		chart.getStyleManager().setDatePattern(pattern);
		chart.getStyleManager().setAxisTickLabelsFont(new Font("SansSerif", Font.PLAIN, 11));
		chart.getStyleManager().setChartPadding(5);
		chart.getStyleManager().setLegendBackgroundColor(Color.LIGHT_GRAY);
		chart.getStyleManager().setChartBackgroundColor(Color.LIGHT_GRAY);

		chart.getStyleManager().setXAxisMin(startTime.getTime());
		chart.getStyleManager().setXAxisMax(endTime.getTime());

		// If a persistence service is specified, find the provider
		persistenceService = null;
		if (service != null) {
			persistenceService = getPersistenceServices().get(service);
		} else {
			// Otherwise, just get the first service
			persistenceService = getPersistenceServices().entrySet().iterator().next().getValue();
		}

		// Did we find a service?
		if (persistenceService == null) {
			throw new IllegalArgumentException("Persistence service not found '" + service + "'.");
		}

		// Loop through all the items
		if (items != null) {
			String[] itemNames = items.split(",");
			for (String itemName : itemNames) {
				Item item = itemUIRegistry.getItem(itemName);
				if(addItem(chart, persistenceService, startTime, endTime, item, seriesCounter))
					seriesCounter++;
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
						if(addItem(chart, persistenceService, startTime, endTime, member, seriesCounter))
							seriesCounter++;
					}
				} else {
					throw new ItemNotFoundException("Item '" + item.getName() + "' defined in groups is not a group.");
				}
			}
		}

		// If there are no series, render a blank chart
		if(seriesCounter == 0) {
			chart.getStyleManager().setLegendVisible(false);

			Collection<Date> xData = new ArrayList<Date>();
			Collection<Number> yData = new ArrayList<Number>();

			xData.add(startTime);
			yData.add(0);
			xData.add(endTime);
			yData.add(0);

			Series series = chart.addDateSeries("NONE", xData, yData);
			series.setMarker(SeriesMarker.NONE);
			series.setLineStyle(new BasicStroke(0f));
		}

		// Legend position (top-left or bottom-left) is dynamically selected based on the data
		// This won't be perfect, but it's a good compromise
		if(legendPosition < 0) {
			chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
		}
		else {
			chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);
		}

		// Write the chart as a PNG image
		BufferedImage lBufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D lGraphics2D = lBufferedImage.createGraphics();
		chart.paint(lGraphics2D);
		return lBufferedImage;
	}

	boolean addItem(Chart chart, QueryablePersistenceService service, Date timeBegin, Date timeEnd, Item item,
			int seriesCounter) {
		Color color = LINECOLORS[seriesCounter % LINECOLORS.length];

		// Get the item label
		String label = null;
		if (itemUIRegistry != null) {
			// Get the item label
			label = itemUIRegistry.getLabel(item.getName());
			if (label != null && label.contains("[") && label.contains("]")) {
				label = label.substring(0, label.indexOf('['));
			}
		}
		if (label == null) {
			label = item.getName();
		}

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
				xData.add(historicItem.getTimestamp());
				yData.add((DecimalType) state);
			}
		}

		// Add the new series to the chart - only if there's data elements to display
		if(xData.size() == 0) {
			return false;
		}

		// If there's only 1 data point, plot it again!
		if(xData.size() == 1) {
			xData.add(xData.iterator().next());
			yData.add(yData.iterator().next());
		}

		Series series = chart.addDateSeries(label, xData, yData);
		series.setLineStyle(new BasicStroke(1.5f));
		series.setMarker(SeriesMarker.NONE);
		series.setLineColor(color);
		
		// If the start value is below the median, then count legend position down
		// Otherwise count up.
		// We use this to decide whether to put the legend in the top or bottom corner.
		if(yData.iterator().next().floatValue() > ((series.getyMax().floatValue() - series.getyMin().floatValue()) / 2 + series.getyMin().floatValue())) {
			legendPosition++;
		}
		else {
			legendPosition--;
		}
		
		return true;
	}

	@Override
	public ImageType getChartType() {
		return (ImageType.png);
	}
}
