/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.CachedMeteringValue;
import org.openhab.binding.digitalstrom.internal.client.entity.DSID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONCachedMeteringValueImpl implements CachedMeteringValue {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONCachedMeteringValueImpl.class);
	
	private DSID dsid = null;
	private double	value = 0;
	private String date = null;
	
	public JSONCachedMeteringValueImpl(JSONObject jObject) {
		if (jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_DSID.getKey()) != null) {
			this.dsid = new DSID(jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_DSID.getKey()).toString());
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_VALUE.getKey()) != null) {
			try {
				this.value = Double.parseDouble(jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_VALUE.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting value: "+jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_VALUE.getKey()).toString());
			}
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_DATE.getKey()) != null) {
			this.date = jObject.get(JSONApiResponseKeysEnum.METERING_GET_LATEST_DATE.getKey()).toString();
		}
		
	}

	@Override
	public DSID getDsid() {
		return dsid;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return "dsid: "+this.getDsid()+", date: "+this.getDate()+", value: "+this.getValue();
	}

}
