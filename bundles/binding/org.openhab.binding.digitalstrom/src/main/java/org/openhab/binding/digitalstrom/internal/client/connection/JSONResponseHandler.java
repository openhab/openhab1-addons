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
package org.openhab.binding.digitalstrom.internal.client.connection;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public class JSONResponseHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONResponseHandler.class);
	
	public JSONResponseHandler() {
		
	}
	
	public boolean checkResponse(JSONObject jsonResponse) {
		if(jsonResponse == null)
			return false;
		else if (jsonResponse.get(JSONApiResponseKeysEnum.RESPONSE_OK.getKey()) != null) {
			return jsonResponse.get(JSONApiResponseKeysEnum.RESPONSE_OK.getKey()).toString().equals(JSONApiResponseKeysEnum.RESPONSE_SUCCESSFUL.getKey());
		}
		else{
			logger.error("error in json request. Error message : "+jsonResponse.get(JSONApiResponseKeysEnum.RESPONSE_MESSAGE).toString());
		}
		return false;	
	}
	
	public JSONObject toJSONObject(String jsonResponse) {
		if (jsonResponse != null && !jsonResponse.trim().equals("")) {
			try {
				return (JSONObject)new JSONParser().parse(jsonResponse);
			}
			catch (ParseException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		return null;
	}
	
	public JSONObject getResultJSONObject(JSONObject jsonObject) {
		if (jsonObject != null) {
			return (JSONObject) jsonObject.get(JSONApiResponseKeysEnum.RESULT.getKey()); 
		}
		return null;
	}

}