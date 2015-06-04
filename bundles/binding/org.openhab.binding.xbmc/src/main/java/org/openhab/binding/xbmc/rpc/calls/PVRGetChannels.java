/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

public class PVRGetChannels extends RpcCall {
	

	private int channelgroupid;
	private String channelName;
	private Integer channelId;

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public PVRGetChannels(AsyncHttpClient client, String uri) {
		super(client, uri);
	}

	@Override
	protected String getName() {
		return "PVR.GetChannels";
	}

	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelgroupid", channelgroupid);
		return params;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void processResponse(Map<String, Object> response) {

		Map<String, Object> result = getMap(response, "result");

		List<Object> channelList = getList(result, "channels");
		
		for (int i = 0; i < channelList.size(); i++) {
			Object channelInfo = channelList.get(i);

			if (channelInfo != null && channelInfo instanceof LinkedHashMap) {
				LinkedHashMap map = (LinkedHashMap) channelList.get(i);
				String channelLabel = getParamAsString(map, "label");
				if (StringUtils.equalsIgnoreCase(channelLabel, channelName))
				{
					channelId = getParamAsInteger(map, "channelid");
					break;
				}
			}

		}
	}

	public void setChannelgroupid(int channelgroupid) {
		this.channelgroupid = channelgroupid;
	}

}
