/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Java Bean to represent a JSON response to a <code>getmeasure</code> API
 * method call.
 * <p>
 * Sample response:
 * 
 * <pre>
 * {"status":"ok",
 *  "body":[{"beg_time":1367965202,
 *           "value":[[23.2,
 *                     64,
 *                     1254,
 *                     1011.5,
 *                     34]]}],
 *  "time_exec":0.0072271823883057,
 *  "time_server":1367965438}
 * </pre>
 * 
 * @author Andreas Brenk
 * @author Gaël L'hopital
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementResponse extends AbstractResponse {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body extends AbstractMessagePart {

		private List<List<BigDecimal>> values;
		private long begTime;

		@JsonProperty("value")
		public List<List<BigDecimal>> getValues() {
			return this.values;
		}
		
		@JsonProperty("beg_time")
		public long getBegTime() {
			return this.begTime;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("values", this.values);
			builder.append("begTime",this.begTime);
			return builder.toString();
		}
	}

	private String status;

	private List<Body> body;

	@JsonProperty("body")
	public List<Body> getBody() {
		return this.body;
	}

	@JsonProperty("status")
	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("status", this.status);
		builder.append("body", this.body);

		return builder.toString();
	}
	
	public Calendar getTimeStamp() {
		Calendar result = null;
		if ((body != null) && !body.isEmpty()) {
			result = Calendar.getInstance();
			/* We make the asumption that the timestamp of the first value is
			 * the good one for the whole module => get(0)
			 */
			result.setTimeInMillis(body.get(0).getBegTime() * 1000);
		}
		return result;
	}
}
