/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.sunnywebbox.requests;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;

import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
 /*
 * @author Manolis Nikiforakis
 * @author Yiannis Gkoufas
 * @since 1.5.0
 */
public class GetPlantOverviewRequest {

	String version;
	String proc;
	String id;
	String format;

	private static final Logger logger = LoggerFactory
			.getLogger(GetPlantOverviewRequest.class);

	public GetPlantOverviewRequest() {
		this.version = "1.0";
		this.id = "1";
		this.format = "JSON";
		this.proc = "GetPlantOverview";
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProc() {
		return proc;
	}

	public void setProc(String proc) {
		this.proc = proc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public HashMap<String, Object> getValues(String URL) {
		HashMap<String, Object> toRet = new HashMap<String, Object>();
		Resty r = new Resty();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
		String req = "RPC=" + json;
		try {
			JSONResource resource = r
					.json("http://" + URL + "/rpc",
							new Content("application/json", req.getBytes()));
			
			String response = resource.toObject().toString();
			logger.debug("JSON received:{}", response);

			List<Object> keyValues = JsonPath.read(response,
					"$.result.overview[*]");
			for (Object keyValue : keyValues) {
				JSONObject jsonObjValue = (JSONObject) keyValue;
				String meta = (String) jsonObjValue.get("meta");
				meta = meta.replaceAll("\\.", "");
				Object value = jsonObjValue.get("value");
				if (meta.equals("Error"))
					continue;

				if (NumberUtils.isNumber(value.toString())) {
					toRet.put(meta, Double.parseDouble(value.toString()));
				} else {
					toRet.put(meta, value);
				}
			}
			toRet.put("updated", new Date());
			return toRet;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return toRet;
		}
	}
}
