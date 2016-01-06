/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal.messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java Bean to represent a JSON response to a <code>devicelist</code> API
 * method call.
 * <p>
 * Sample response:
 * 
 * <pre>
 * { 
 *   "system_id": 17240, 
 *   "modules": 20, 
 *   "size_w": 4160, 
 *   "current_power": 13, 
 *   "energy_today": 18904, 
 *   "energy_lifetime": 28739558, 
 *   "summary_date": "2015-02-23", 
 *   "source": "microinverters", 
 *   "status": "normal", 
 *   "operational_at": 1304566098, 
 *   "last_report_at": 1424743838 
 * } 
 * </pre>
 * 
 * @author Markus Fritze
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemsResponse extends AbstractMessage {

	private static Logger logger = LoggerFactory.getLogger(SystemsResponse.class);

	private Integer system_id;
	private Integer modules;
	private Integer size_w;
	private Integer current_power;
	private Integer energy_today;
	private Integer energy_lifetime;
	private String summary_date;
	private String source;
	private String status;
	private long operational_at;
	private long last_report_at;

    private static final String enphaseDateFormatString = "yyyy-MM-dd";

	/**
	 * "system_id": 12345
	 */
	@JsonProperty("system_id")
	public Integer getSystem_id() {
		return this.system_id;
	}

	/**
	 * "modules": 12345
	 */
	@JsonProperty("modules")
	public Integer getModules() {
		return this.modules;
	}

	/**
	 * "size_w": 12345
	 */
	@JsonProperty("size_w")
	public Integer getSize_w() {
		return this.size_w;
	}

	/**
	 * "current_power": 12345
	 */
	@JsonProperty("current_power")
	public Integer getCurrent_power() {
		return this.current_power;
	}

	/**
	 * "energy_today": 12345
	 */
	@JsonProperty("energy_today")
	public Integer getEnergy_today() {
		return this.energy_today;
	}

	/**
	 * "energy_lifetime": 12345
	 */
	@JsonProperty("energy_lifetime")
	public Integer getEnergy_lifetime() {
		return this.energy_lifetime;
	}

	/**
	 * "summary_date": "2015-02-23"
	 */
	@JsonProperty("summary_date")
	public Calendar getSummary_date() {
		final SimpleDateFormat format = new SimpleDateFormat(enphaseDateFormatString);
		Calendar calendar = Calendar.getInstance(); 
		try {
			Date	date = format.parse(this.summary_date);
			calendar.setTime(date);
		} catch (ParseException e) {
			logger.debug("summary_date {} has an unknown format", this.summary_date);
		}
		return calendar;
	}

	/**
	 * "source": 'microinverter'
	 */
	@JsonProperty("source")
	public String getSource() {
		return this.source;
	}

	/**
	 * "status": 'ok'
	 */
	@JsonProperty("status")
	public String getStatus() {
		return this.status;
	}

	/**
	 * "operational_at": 1304566098
	 */
	@JsonProperty("operational_at")
	public Calendar getOperational_at() {
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTimeInMillis(this.operational_at * 1000);
		return calendar;
	}

	/**
	 * "last_report_at": 1424743838
	 */
	@JsonProperty("last_report_at")
	public Calendar getLast_report_at() {
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTimeInMillis(this.last_report_at * 1000);
		return calendar;
	}


	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.append("energy_lifetime", this.energy_lifetime);
		builder.append("energy_today", this.energy_today);
		builder.append("current_power", this.current_power);
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
