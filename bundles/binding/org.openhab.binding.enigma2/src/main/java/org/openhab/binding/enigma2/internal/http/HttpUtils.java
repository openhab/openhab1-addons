package org.openhab.binding.enigma2.internal.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpUtils.class);

	private HttpUtils() {
		// hide constructor
	}

	public static String getGetResponse(String hostName, String suffix,
			String username, String password) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://" + hostName + suffix);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			String userpass = username + ":" + password;
			String basicAuth = "Basic "
					+ DatatypeConverter.printBase64Binary(userpass.getBytes());
			connection.setRequestProperty("Authorization", basicAuth);

			// Read response
			int responseCode = connection.getResponseCode();
			logger.debug("\nSending 'GET' request to URL : " + url);
			logger.debug("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception e) {
			throw new IOException("Could not handle http get", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
