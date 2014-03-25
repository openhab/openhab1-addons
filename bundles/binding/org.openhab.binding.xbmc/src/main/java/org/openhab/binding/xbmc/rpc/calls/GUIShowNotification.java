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
package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * GUI.ShowNotification RPC
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class GUIShowNotification extends RpcCall {

	private String title;
	private String message;
	private Object image = null;
	private int displaytime = 5000;

	public GUIShowNotification(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setImage(Object image) {
		this.image = image;
	}


	public void setDisplaytime(int displaytime) {
		this.displaytime = displaytime;
	}


	@Override
	protected String getName() {
		return "GUI.ShowNotification";
	}

	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("message", message);
		if (image != null) params.put("image", image);
		params.put("displaytime", displaytime);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) throws RpcException {
		// TODO: ack?
	}
}
