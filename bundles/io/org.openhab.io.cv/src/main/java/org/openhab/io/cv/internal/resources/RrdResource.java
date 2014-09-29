/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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
 * TODO: 
 *  - gzip the response to save bandwidth
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
	private static final Logger logger = LoggerFactory.getLogger(RrdResource.class);

	public static final String PATH_RRD = "rrdfetch";
	
	// pattern RRDTool uses to format doubles in XML files
    static final String PATTERN = "0.0000000000E00";

    static final DecimalFormat df;

    static {
        df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
        df.applyPattern(PATTERN);
        //df.setPositivePrefix("+");
    }


	@Context UriInfo uriInfo;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRrd(@Context HttpHeaders headers,
			@QueryParam("rrd") String rrdName, @QueryParam("ds") String ds,
			@QueryParam("start") String start, @QueryParam("end") String end,
			@QueryParam("res") long res) {

		logger.debug("Received GET request at '{}' for rrd '{}'.",
				new String[] { uriInfo.getPath(), rrdName });
		String responseType = MediaTypeHelper.getResponseMediaType(headers
				.getAcceptableMediaTypes());
		
		if (responseType != null) {
			try {
				RrdDb rrdDb = new RrdDb("./etc/rrd4j/" + rrdName);
				long[] times = Util.getTimestamps(start, end);
				FetchRequest fetchRequest = rrdDb.createFetchRequest(
						ConsolFun.valueOf(ds), times[0], times[1], res);
				FetchData fetchData = fetchRequest.fetchData();
				StringBuilder buffer = new StringBuilder();
				long[] timestamps = fetchData.getTimestamps();
				double[][] values = fetchData.getValues();
				buffer.append("[");
				for (int row = 0; row < fetchData.getRowCount(); row++) {
					// change to microseconds
					buffer.append("[" + (timestamps[row]*1000) + ",");
					buffer.append("[");
					ArrayList<String> data = new ArrayList<String>();
					for (int dsIndex = 0; dsIndex < fetchData.getColumnCount(); dsIndex++) {
						data.add(dsIndex,RrdResource.formatDouble(values[dsIndex][row],"null",true));
					}
					buffer.append(StringUtils.join(data, ","));
					buffer.append("]],");
				}
				buffer.deleteCharAt(buffer.length()-1);
				buffer.append("]");
				rrdDb.close();
				return Response.ok(buffer.toString(),responseType).build();
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
			return Response.serverError().build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}
	
	static String formatDouble(double x, String nanString, boolean forceExponents) {
        if (Double.isNaN(x)) {
            return nanString;
        }
        if (forceExponents) {
            return df.format(x);
        }
        return "" + x;
    }

}
