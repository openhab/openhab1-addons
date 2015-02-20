package org.openhab.binding.nest.internal.api;

import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.openhab.binding.nest.internal.api.NestAPI.AuthenticationListener;
import org.openhab.binding.nest.internal.api.listeners.Listener;
import org.openhab.binding.nest.internal.api.listeners.Listener.SmokeCOAlarmListener;
import org.openhab.binding.nest.internal.api.listeners.Listener.StructureListener;
import org.openhab.binding.nest.internal.api.listeners.Listener.ThermostatListener;
import org.openhab.binding.nest.internal.api.model.AccessToken;
import org.openhab.binding.nest.internal.api.model.SmokeCOAlarm;
import org.openhab.binding.nest.internal.api.model.Structure;
import org.openhab.binding.nest.internal.api.model.Thermostat;

public class NestAPITest {

	private final String clientId = "204b41ce-edc4-4a1d-b38a-5910b8082a7b";
	private final String clientSecret = "yRXUdeiUwFsDLXezKykeQGijZ";
	
	@Test
	public void test() throws InterruptedException {
		NestAPI nestApi = new NestAPI(clientId, clientSecret);
		Listener.Builder listenerBuilder = new Listener.Builder();
		listenerBuilder.setThermostatListener(new TListener());
		listenerBuilder.setSmokeCOAlarmListener(new PListener());
		listenerBuilder.setStructureListener(new SListener());
		nestApi.addUpdateListener(listenerBuilder.build());
		
//		ClientMetadata mClientMetadata = new ClientMetadata.
//				Builder()
//				.setClientID(clientId)
//				.setClientSecret(clientSecret)
//				.build();
		
		String code = "Y9BH8JHU";
		
//		AccessToken accessToken = loadInBackground(code, mClientMetadata);
		
		NestAPI.AuthenticationListener authernticationListener = new AListener();
		
		nestApi.authenticate(code, authernticationListener);
		Thread.sleep(6 * 60 * 60 * 1000);
		fail("Not yet implemented");
	}

	private class TListener implements ThermostatListener {
		@Override
		public void onThermostatUpdated(Thermostat thermostat) {
				System.out.println("Thermostat Update: " + thermostat);
		}
	}

	private class PListener implements SmokeCOAlarmListener {
		@Override
		public void onSmokeCOAlarmUpdated(SmokeCOAlarm smokeCOAlarm) {
			System.out.println("Protect Update: " + smokeCOAlarm);
		}
	}
	
	private class SListener implements StructureListener {
		@Override
		public void onStructureUpdated(Structure structure) {
			System.out.println("Structure Update: " + structure);
		}
	}

	private class AListener implements AuthenticationListener {

		@Override
		public void onAuthenticationSuccess() {
			System.out.println("Authentication Success");
		}

		@Override
		public void onAuthenticationFailure(int errorCode) {
			System.out.println("Authentication Failure");
		}
		
	}

}
