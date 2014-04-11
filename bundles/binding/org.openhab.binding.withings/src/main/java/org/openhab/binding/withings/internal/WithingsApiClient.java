package org.openhab.binding.withings.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.withings.internal.model.MeasureGroup;
import org.openhab.binding.withings.internal.model.MeasureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class WithingsApiClient {

	private static final Logger logger = LoggerFactory
			.getLogger(WithingsApiClient.class);

	private OAuthConsumer consumer;
	private String userId;

	private Gson gson;

	private JsonParser jsonParser;

	public WithingsApiClient(OAuthConsumer consumer, String userId) {
		this.consumer = consumer;
		this.userId = userId;
		this.gson = new Gson();
		this.jsonParser = new JsonParser();
	}

	public List<MeasureGroup> getMeasures() {
		try {
			String url = "http://wbsapi.withings.net/measure?action=getmeas&userid="
					+ this.userId;
			
			String signedUrl = consumer.sign(url);
			HttpURLConnection httpURLConnection;

			httpURLConnection = (HttpURLConnection) new URL(signedUrl)
					.openConnection();

			httpURLConnection.connect();
			logger.info(String.valueOf(httpURLConnection.getResponseCode()));
			logger.info(httpURLConnection.getResponseMessage());
			InputStream inputStream = httpURLConnection.getInputStream();
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);
			logger.info(jsonObject.toString());
			int status = jsonObject.get("status").getAsInt();
			logger.info(String.valueOf(status));
			if(status == 0) {
				return gson.fromJson(jsonObject.get("body").getAsJsonObject(), MeasureResult.class).measureGroups;
			} else {
				
			}
			return null;
//			, new TypeToken<List<MeasureGroup>>(){}.getType()
		} catch (IOException | OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
