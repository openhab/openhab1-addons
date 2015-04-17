/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.io.cv.CVApplication;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class acts as a Cometvisu resource for reading RRD files.
 * </p>
 * 
 * 
 * <p>
 * This resource is registered with the Jersey servlet.
 * </p>
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@Path(RrdResource.PATH_RRD)
public class RrdResource {
	private static final Logger logger = LoggerFactory
			.getLogger(RrdResource.class);

	public static final String PATH_RRD = "rrdfetch";

	protected final static String RRD_FOLDER = "." + File.separator + "etc"
			+ File.separator + "rrd4j";

	// pattern RRDTool uses to format doubles in XML files
	static final String PATTERN = "0.0000000000E00";

	static final DecimalFormat df;

	static {
		df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.applyPattern(PATTERN);
		// df.setPositivePrefix("+");
	}

	@Context
	UriInfo uriInfo;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRrd(@Context HttpHeaders headers,
			@QueryParam("rrd") String itemName,
			@QueryParam("ds") String consFunction,
			@QueryParam("start") String start, @QueryParam("end") String end,
			@QueryParam("res") long resolution) {

		if (logger.isDebugEnabled())
			logger.debug("Received GET request at '{}' for rrd '{}'.",
					uriInfo.getPath(), itemName);
		String responseType = MediaTypeHelper.getResponseMediaType(headers
				.getAcceptableMediaTypes());

		if (responseType != null) {

			// RRD specific: no equivalent in PersistenceService known
			ConsolFun consilidationFunction = ConsolFun.valueOf(consFunction);

			// read the start/end time as they are provided in the RRD-way, we
			// use
			// the RRD4j to read them
			long[] times = Util.getTimestamps(start, end);
			Date startTime = new Date();
			startTime.setTime(times[0] * 1000L);
			Date endTime = new Date();
			endTime.setTime(times[1] * 1000L);

			if (itemName.endsWith(".rrd"))
				itemName = itemName.substring(0, itemName.length() - 4);
			String[] parts = itemName.split(":");
			String service = "rrd4j";

			if (parts.length == 2) {
				itemName = parts[1];
				service = parts[0];
			}

			Item item;
			try {
				item = CVApplication.getItemUIRegistry().getItem(itemName);
				logger.debug("item '{}' found ", item);

				// Prefer RRD-Service
				QueryablePersistenceService persistenceService = CVApplication
						.getPersistenceServices().get(service);
				// Fallback to first persistenceService from list
				if (persistenceService == null) {
					Iterator<Entry<String, QueryablePersistenceService>> pit = CVApplication
							.getPersistenceServices().entrySet().iterator();
					if (pit.hasNext()) {
						persistenceService = pit.next().getValue();
					} else {
						throw new IllegalArgumentException(
								"No Persistence service found.");
					}
				}
				Object data = null;
				if (persistenceService.getName().equals("rrd4j")) {
					data = getRrdSeries(persistenceService, item,
							consilidationFunction, startTime, endTime,
							resolution);
				} else {
					data = getPersistenceSeries(persistenceService, item,
							startTime, endTime, resolution);
				}
				return Response.ok(data, responseType).build();
			} catch (ItemNotFoundException e) {
				logger.error(
						"Item '{}' not found error while requesting series data.",
						itemName);
			}
			return Response.serverError().build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	/**
	 * returns a rrd series data, an array of [[timestamp,data1,data2,...]]
	 * 
	 * @param persistenceService
	 * @param item
	 * @param consilidationFunction
	 * @param timeBegin
	 * @param timeEnd
	 * @param resolution
	 * @return
	 */
	public Object getRrdSeries(QueryablePersistenceService persistenceService,
			Item item, ConsolFun consilidationFunction, Date timeBegin,
			Date timeEnd, long resolution) {
		Map<Long, ArrayList<String>> data = new TreeMap<Long, ArrayList<String>>();
		try {
			List<String> itemNames = new ArrayList<String>();

			if (item instanceof GroupItem) {
				GroupItem groupItem = (GroupItem) item;
				for (Item member : groupItem.getMembers()) {
					itemNames.add(member.getName());
				}
			} else {
				itemNames.add(item.getName());
			}
			for (String itemName : itemNames) {
				addRrdData(data, itemName, consilidationFunction, timeBegin,
						timeEnd, resolution);
			}

		} catch (FileNotFoundException e) {
			// rrd file does not exist, fallback to generic persistance service
			logger.debug("no rrd file found '{}'", (RRD_FOLDER + File.separator
					+ item.getName() + ".rrd"));
			return getPersistenceSeries(persistenceService, item, timeBegin,
					timeEnd, resolution);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage()
					+ ": fallback to generic persistance service");
			return getPersistenceSeries(persistenceService, item, timeBegin,
					timeEnd, resolution);
		}
		return convertToRrd(data);
	}

	public Object getPersistenceSeries(
			QueryablePersistenceService persistenceService, Item item,
			Date timeBegin, Date timeEnd, long resolution) {
		Map<Long, ArrayList<String>> data = new TreeMap<Long, ArrayList<String>>();

		// Define the data filter
		FilterCriteria filter = new FilterCriteria();
		filter.setBeginDate(timeBegin);
		filter.setEndDate(timeEnd);
		filter.setItemName(item.getName());
		filter.setOrdering(Ordering.ASCENDING);

		// Get the data from the persistence store
		Iterable<HistoricItem> result = persistenceService.query(filter);
		Iterator<HistoricItem> it = result.iterator();

		// Iterate through the data
		int dataCounter = 0;
		while (it.hasNext()) {
			dataCounter++;
			HistoricItem historicItem = it.next();
			State state = historicItem.getState();
			if (state instanceof DecimalType) {
				ArrayList<String> vals = new ArrayList<String>();
				vals.add(formatDouble(((DecimalType) state).doubleValue(),
						"null", true));
				data.put(historicItem.getTimestamp().getTime(), vals);
			}
		}
		logger.debug(
				"'{}' querying item '{}' from '{}' to '{}' => '{}' results",
				persistenceService.getName(), filter.getItemName(),
				filter.getBeginDate(), filter.getEndDate(), dataCounter);
		return convertToRrd(data);
	}

	private String convertToRrd(Map<Long, ArrayList<String>> data) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[");
		for (Long time : data.keySet()) {
			// change to microseconds
			buffer.append("[" + time + ",");
			buffer.append("[");
			buffer.append("\""+StringUtils.join(data.get(time), "\",\"")+"\"");
			buffer.append("]],");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append("]");
		
		return buffer.toString();
	}

	private Map<Long, ArrayList<String>> addRrdData(
			Map<Long, ArrayList<String>> data, String itemName,
			ConsolFun consilidationFunction, Date timeBegin, Date timeEnd,
			long resolution) throws IOException {
		RrdDb rrdDb = new RrdDb(RRD_FOLDER + File.separator + itemName + ".rrd");
		FetchRequest fetchRequest = rrdDb.createFetchRequest(
				consilidationFunction, Util.getTimestamp(timeBegin),
				Util.getTimestamp(timeEnd), resolution);
		FetchData fetchData = fetchRequest.fetchData();
		// logger.info(fetchData.toString());
		long[] timestamps = fetchData.getTimestamps();
		double[][] values = fetchData.getValues();

		logger.debug("RRD fetch returned '{}' rows and '{}' columns",
				fetchData.getRowCount(), fetchData.getColumnCount());

		for (int row = 0; row < fetchData.getRowCount(); row++) {
			// change to microseconds
			long time = timestamps[row] * 1000L;

			if (!data.containsKey(time)) {
				data.put(time, new ArrayList<String>());
			}
			ArrayList<String> vals = data.get(time);
			int indexOffset = vals.size();
			for (int dsIndex = 0; dsIndex < fetchData.getColumnCount(); dsIndex++) {
				vals.add(dsIndex + indexOffset,
						formatDouble(values[dsIndex][row], "null", true));
			}
		}
		rrdDb.close();

		return data;
	}

	static String formatDouble(double x, String nanString,
			boolean forceExponents) {
		if (Double.isNaN(x)) {
			return nanString;
		}
		if (forceExponents) {
			return df.format(x);
		}
		return "" + x;
	}
}
