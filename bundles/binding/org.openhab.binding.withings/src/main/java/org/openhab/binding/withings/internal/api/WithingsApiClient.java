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

	private static final String GET_MEASURES_METHOD = "getmeas";

	private static final String MEASURE_ENDPOINT = "measure";

	private static final String WITHINGS_API_URL = "http://wbsapi.withings.net/";

	private OAuthConsumer consumer;
	private Gson gson;

	private JsonParser jsonParser;

	private String userId;

	public WithingsApiClient(OAuthConsumer consumer, String userId) {
		this.consumer = consumer;
		this.userId = userId;
		this.gson = createGsonBuilder().create();
		this.jsonParser = new JsonParser();
	}

	public List<MeasureGroup> getMeasures() throws OAuthException,
			WithingsConnectionException {

		String url = getServiceUrl(MEASURE_ENDPOINT, GET_MEASURES_METHOD);

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
		return WITHINGS_API_URL + endpoint + "?action=" + method + "&userid="
				+ this.userId;
	}

}
