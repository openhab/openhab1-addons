package org.openhab.ui.chart;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.openhab.core.items.ItemNotFoundException;

/**
 * Defines the interface for chart providers. A chart provider interfaces with
 * the persistence store to get the data and receives parameters from the UI
 * chart servlet and returns a chart image object (PNG).
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 * 
 */
public interface ChartProvider {
	/**
	 * Gets the name of this chart provider.
	 * 
	 * @return String containing the provider name
	 */
	String getName();

	/**
	 * Creates a chart object. This sets the initial parameters for the chart
	 * before the items are added
	 * 
	 * @param service
	 *            A string containing the name of the persistence service. May
	 *            be null in which case the chart provider can decide itself
	 *            which store to use.
	 * @param widget
	 *            The widget ID. This allows the chart provider to look up the
	 *            widget and get the items directly from the sitemap definition.
	 *            May be null.
	 * @param theme
	 *            A string containing a theme name for the chart. The provider
	 *            should store its own themes. May be null to use a default
	 *            theme.
	 * @param height
	 *            The height of the chart.
	 * @param width
	 *            The width of the chart.
	 * @param startTime
	 *            The start time of the chart
	 * @param endTime
	 *            The end time of the chart
	 * @param height
	 *            The height of the chart
	 * @param width
	 *            The width of the chart
	 * @param items
	 *            The items to display on the chart
	 * @param groups
	 *            The groups to display on the chart
	 * 
	 * @return BufferedImage object if the chart is rendered correctly,
	 *         otherwise null.
	 * 
	 * @throws ItemNotFoundException if an item or group is not found
	 * @throws IllegalArgumentException if an invalid argument is passed
	 */
	BufferedImage createChart(String service, String theme, Date startTime, Date endTime, int height,
			int width, String items, String groups) throws ItemNotFoundException;

	/**
	 * Gets the type of data that will be written by the chart.
	 * 
	 * @return ImageType
	 */
	ImageType getChartType();
	
	/**
	 * Provides a list of image types
	 *
	 */
	public enum ImageType {
		png, jpg, gif;
	}
}
