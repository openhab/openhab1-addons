package org.openhab.io.rest.internal.resources;

import com.sun.jersey.api.json.JSONWithPadding;

public class ResponseHelper {
	
	public static Object wrapContentIfNeccessary(String callback,
			final String responseType, final Object content) {
		if (responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT)) {
			return new JSONWithPadding(content, callback);
		}
		return content;
	}
	
}
