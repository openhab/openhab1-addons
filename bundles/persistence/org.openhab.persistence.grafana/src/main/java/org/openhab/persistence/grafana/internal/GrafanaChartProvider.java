/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.grafana.internal;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.ui.chart.ChartProvider;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This chart provider acts as a proxy to the grafana back end
 * 
 * @author magcode
 * @since 1.7.0
 *
 */
public class GrafanaChartProvider implements ChartProvider, ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(GrafanaChartProvider.class);
	private HttpClient proxy;

	private static final String GRAFANA_BASE_URL = "/render/dashboard-solo/db/";

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

	protected ItemUIRegistry itemUIRegistry;
	private String grafanaUrl;
	private String theme;
	private String apiKey;

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}

	public void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = null;
	}

	public void activate(final BundleContext bundleContext,
			final Map<String, Object> config) {
		logger.debug("Starting up grafana chart provider");
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		proxy = new HttpClient(connectionManager);
	}

	@Override
	public String getName() {
		return "grafana";
	}

	@Override
	public BufferedImage createChart(String service, String theme,
			Date startTime, Date endTime, int height, int width, String items,
			String groups) throws ItemNotFoundException {
		GetMethod get = null;
		try {
			get = new GetMethod(buildUrl(items, width + "", height + "",
					startTime.getTime() + ""));
			get.setRequestHeader("Authorization", "Bearer " + this.apiKey);
			proxy.executeMethod(get);
			InputStream is = get.getResponseBodyAsStream();
			if (get.getStatusCode() == HttpStatus.SC_OK) {
				BufferedImage bufferedImage = javax.imageio.ImageIO.read(is);
				return bufferedImage;
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			get.releaseConnection();
		}
		return null;
	}

	/**
	 * Constructs the URL for the grafana backend call
	 * 
	 * @param itemName
	 * @param width
	 * @param height
	 * @param from
	 * @return
	 */
	private String buildUrl(String itemName, String width, String height,
			String from) {
		String label = itemUIRegistry.getLabel(itemName);
		StringBuffer buf = new StringBuffer();
		buf.append(this.grafanaUrl);
		buf.append(GRAFANA_BASE_URL);
		buf.append(StringUtils.substringBeforeLast(label, "-"));
		buf.append("?panelId=" + StringUtils.substringAfterLast(label, "-"));
		buf.append("&fullscreen&theme=");
		buf.append(this.theme);
		buf.append("&from=");
		buf.append(from);
		buf.append("&to=now");
		buf.append("&width=");
		buf.append(width);
		buf.append("&height=");
		buf.append(height);
		return buf.toString();
	}

	@Override
	public ImageType getChartType() {
		return ImageType.png;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

		if (properties == null)
			return;

		if (properties.get("url") != null) {
			grafanaUrl = (String) properties.get("url");
		}
		if (properties.get("theme") != null) {
			theme = (String) properties.get("theme");
		}
		if (properties.get("apiKey") != null) {
			apiKey = (String) properties.get("apikey");
		}
	}
}
