/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.mojio.internal.MojioException;

/**
 * GetMojioData function implements Mojio request and returns all registered mojios.
 *
 * @author Vladimir Pavluk
 * @since 1.0
 */
public class GetMojioData extends AbstractFunction {
	private static final String RESOURCE_URL = API_BASE_URL + "v1/Mojios";

  public static final boolean ASC = false;
  public static final boolean DESC = true;

  private int limit = 10;
  private int offset = 0;
  private String sortBy = "LastContactTime";
  private boolean order = ASC;
  private String criteria = "";

  public GetMojioData(String tag) {
    super(HTTP_GET, tag);
  }

  public GetMojioData(String tag, String IMEI) {
    super(HTTP_GET, tag);
    this.criteria = "Imei=" + IMEI;
  }

	public MojioStatusResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final MojioStatusResponse response = JSON.readValue(json, MojioStatusResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get authorization.", e, url, json);
		}
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?limit=");
			urlBuilder.append(this.limit);
      urlBuilder.append("&offset=");
			urlBuilder.append(offset);
			urlBuilder.append("&sortBy=");
			urlBuilder.append(sortBy);
			urlBuilder.append("&desc=");
			urlBuilder.append(order);
			urlBuilder.append("&criteria=");
			urlBuilder.append(criteria);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new MojioException(e);
		}
	}
}
