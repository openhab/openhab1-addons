package org.openhab.binding.withings.internal.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthException;

import org.openhab.binding.withings.internal.model.Attribute;
import org.openhab.binding.withings.internal.model.Category;
import org.openhab.binding.withings.internal.model.MeasureGroup;
import org.openhab.binding.withings.internal.model.MeasureResult;
import org.openhab.binding.withings.internal.model.MeasureType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WithingsApiClient {

	private static final String API_ENDPOINT_MEASURE = "measure";

	private static final String API_METHOD_GET_MEASURES = "getmeas";

	private static final String API_URL = "http://wbsapi.withings.net/";

	private final OAuthConsumer consumer;

	private final Gson gson;

	private final JsonParser jsonParser;

	private final String userId;

	public WithingsApiClient(OAuthConsumer consumer, String userId) {
		this.consumer = consumer;
		this.userId = userId;
		this.gson = createGsonBuilder().create();
		this.jsonParser = new JsonParser();
	}

	public List<MeasureGroup> getMeasures() throws OAuthException,
			WithingsConnectionException {
		return getMeasures(0);
	}

	public List<MeasureGroup> getMeasures(int startTime) throws OAuthException,
			WithingsConnectionException {

		String url = getServiceUrl(API_ENDPOINT_MEASURE,
				API_METHOD_GET_MEASURES);
		if (startTime > 0) {
			url = url + "&startdate=" + startTime;
		}

		try {
			JsonObject jsonObject = call(consumer.sign(url));

			int status = jsonObject.get("status").getAsInt();

			if (status == 0) {
				JsonElement body = jsonObject.get("body");
				return gson.fromJson(body.getAsJsonObject(),
						MeasureResult.class).measureGroups;
			} else {
				throw new WithingsConnectionException(
						"Withings API call failed: " + status);
			}

		} catch (Exception ex) {
			throw new WithingsConnectionException("Could not connect to URL: "
					+ ex.getMessage(), ex);
		}
	}

	private JsonObject call(String signedUrl) throws IOException,
			MalformedURLException, WithingsConnectionException,
			UnsupportedEncodingException {

		HttpURLConnection httpURLConnection;
		httpURLConnection = (HttpURLConnection) new URL(signedUrl)
				.openConnection();
		httpURLConnection.connect();

		int responseCode = httpURLConnection.getResponseCode();

		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new WithingsConnectionException("Illegal response code: "
					+ responseCode);
		}

		InputStream inputStream = httpURLConnection.getInputStream();
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);
		reader.close();

		return jsonObject;
	}

	private GsonBuilder createGsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(MeasureType.class,
				new JsonDeserializers.MeasureTypeJsonDeserializer());
		gsonBuilder.registerTypeAdapter(Category.class,
				new JsonDeserializers.CategoryJsonDeserializer());
		gsonBuilder.registerTypeAdapter(Attribute.class,
				new JsonDeserializers.AttributeJsonDeserializer());
		return gsonBuilder;
	}

	private String getServiceUrl(String endpoint, String method) {
		return API_URL + endpoint + "?action=" + method + "&userid="
				+ this.userId;
	}

}
