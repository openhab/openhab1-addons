package org.openhab.binding.siemenshvac.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.core.types.Type;
import org.json.simple.JSONObject;
import org.openhab.binding.siemenshvac.internal.siemensHvacGenericBindingProvider.siemensHvacBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class siemensHvacConnector extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(siemensHvacBinding.class);

	private String sessionId = null;
	private String baseUrl = "";
	private String userName = "";
	private String userPassword = "";
	private Date lastUpdate;

	private Map<String, Type> updateCommand;

	private boolean interrupted = false;

	private siemensHvacBinding hvacBinding;

	public siemensHvacConnector(siemensHvacBinding hvacBinding) {
		this.hvacBinding = hvacBinding;
		this.updateCommand = new Hashtable<String, Type>();
		start();
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	private void _doAuth() {

		HttpURLConnection connection = null;
		logger.debug("siemensHvac:doAuth()");

		String request = "api/auth/login.json?user=" + userName + "&pwd=" + userPassword;
		StringBuilder response = new StringBuilder();
		BufferedReader in = null;

		try {
			URL url = new URL(baseUrl + request);
			logger.debug("siemensHvac:doAuth:openCnx()");
			connection = (HttpURLConnection) url.openConnection();

			int responseCode = -1;
			if (connection != null) {
				connection.setConnectTimeout(60000);
				connection.setReadTimeout(60000);

				try {
					logger.debug("siemensHvac:doAuth:connect()");
					connection.connect();
					responseCode = connection.getResponseCode();
				} 
				catch (SocketTimeoutException e) {
					logger.error("siemensHvac:doAuth:" + e.getMessage() + " : " + request);
					return;
				}

				logger.debug("siemensHvac:doAuth:response()" + responseCode);
				if (responseCode == HttpURLConnection.HTTP_OK) {
					String inputLine = null;
					in = new BufferedReader(new InputStreamReader(
							connection.getInputStream()));

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
						logger.debug("siemensHvac:doAuthaddline:()" + inputLine);
					}

					in.close();
				} 
				else {
					response = null;
				}
				logger.debug("siemensHvac:doAuth:Endresponse:()" + responseCode);
			}

			if (response != null) {
				String st = response.toString();
				logger.debug("siemensHvac:doAuth:decodeResponse:()" + st);
				JSONObject result = JSONResponseHandler.toJSONObject(st);
				logger.debug("siemensHvac:doAuth:afterJson");
				if (result.containsKey("Result")) {
					JSONObject subResult = (JSONObject) result.get("Result");
					if (subResult.containsKey("Success")) {
						if (subResult.get("Success").equals("true")) {
							if (result.containsKey("SessionId")) {
								sessionId = result.get("SessionId").toString();
							}
						}

					}

				}
			}
		} 
		catch (MalformedURLException e) {
			logger.error("siemensHvac:MalformedURLException by executing jsonRequest: "+ request + " ; " + e.getLocalizedMessage());
		} 
		catch (IOException e) {
			logger.error("siemensHvac:IOException by executing jsonRequest: " + request + " ; " + e.getLocalizedMessage());
		} 
		catch (Exception e) {
			logger.error("siemensHvac:Error during auth: " + e.getLocalizedMessage());
		} 
		finally {
			if (connection != null)
				connection.disconnect();
		}

	}

	public void ReadAllDp() {
		siemensHvacGenericBindingProvider provider = hvacBinding.getProvider();
		logger.debug("siemensHvac:readAllDP():begin");

		Iterator<BindingConfig> it = provider.getBindingConfigs().values().iterator();
		
		while (it.hasNext()) {
			siemensHvacBindingConfig config = (siemensHvacBindingConfig) it.next();
			String name = config.getName();
			String type = config.getType();
			String dp = config.getDpt();
			
			ReadDp(name, dp, type);
		}
		logger.debug("siemensHvac:readAllDP():end");
	}

	public String ReadDp(String name, String dp, String type) {
		String value = _readDpInternal(name, dp, type);

		logger.debug("siemensHvac:ReadDP:" + name + ":" + dp + ":" + value);

		if (value==null || value.equals("----") || value.equals(""))
			return null;
		
		if (type.equals("Numeric")) {
			hvacBinding.getEventPublisher().postUpdate(name, new DecimalType(value));
		}
		else if (type.equals("Enumeration")) {
			String valueEnum = value;
			if (type.equals("Enumeration")) {
				String[] values = value.split(":");
				valueEnum = values[0];
				value = values[1];
			}
			
			hvacBinding.getEventPublisher().postUpdate(name, new StringType(valueEnum));
		}
		else if (type.equals("Text")) {
			hvacBinding.getEventPublisher().postUpdate(name, new StringType(value));
		}
		else {
			hvacBinding.getEventPublisher().postUpdate(name, new StringType(value));
		}

		return value;
	}
		

	public void WriteDp(String name, Type dp) {
		if (sessionId == null)
			_doAuth();

		siemensHvacGenericBindingProvider provider = hvacBinding.getProvider();
		siemensHvacBindingConfig bindingConfig = (siemensHvacBindingConfig) provider.getBindingConfigs().get(name);
		String dpt = bindingConfig.getDpt();
		String type = bindingConfig.getType();

		String valAct = _readDpInternal(name, dpt, type);
		String valActEnum = valAct;
		
		if (type.equals("Enumeration")) {
			String[] values = valAct.split(":");
			valActEnum = values[0];
			valAct = values[1];
		}
		String val = "";
		if (dp instanceof PercentType) {
			PercentType pct = (PercentType) dp;
			val = pct.toString();
		} 
		else if (dp instanceof DecimalType) {
			DecimalType bdc = (DecimalType) dp;
			val = bdc.toString();
		} 
		else if (dp instanceof StringType) {
			StringType bdc = (StringType) dp;
			val = bdc.toString();
		}

		if (valAct != null && val.equals(valAct))
			return;
		if (valActEnum != null && val.equals(valActEnum))
			return;

		siemensHvacBindingConfig config = (siemensHvacBindingConfig) provider.getBindingConfigs().get(name);
		String dpType = config.getType();

		String request = "api/menutree/write_datapoint.json?SessionId="
				+ sessionId + "&Id=" + dpt + "&Value=" + val + "&Type="
				+ dpType;

		String response = DoRequest(request);
		logger.debug("siemensHvac:WriteDP(response) = " + response);
		
		ReadDp(name, dpt, type);
	}

	public String DoRequest(String request) {
		HttpURLConnection connection = null;
		StringBuilder response = new StringBuilder();
		BufferedReader in = null;

		try {
			URL url = new URL(baseUrl + request);

			connection = (HttpURLConnection) url.openConnection();

			int responseCode = -1;
			if (connection != null) {
				connection.setConnectTimeout(6000);
				connection.setReadTimeout(6000);

				try {
					connection.connect();
					responseCode = connection.getResponseCode();
				} 
				catch (SocketTimeoutException e) {
					logger.error("siemensHvac:DoRequest:" + e.getMessage() + " : " + request);
					return null;
				}

				if (responseCode == HttpURLConnection.HTTP_OK) {
					String inputLine = null;
					in = new BufferedReader(new InputStreamReader(
							connection.getInputStream()));

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}

					in.close();
				} else {
					response = null;
				}

				if (response != null)
					return response.toString();

			}
		} 
		catch (MalformedURLException e) {
			logger.error("siemensHvac:DoRequest:MalformedURLException by executing jsonRequest: "
					+ request + " ; " + e.getLocalizedMessage());
		} 
		catch (IOException e) {
			logger.error("siemensHvac:DoRequest:IOException by executing jsonRequest: "
					+ request + " ; " + e.getLocalizedMessage());
		} 
		catch (Exception e) {
			logger.error("siemensHvac:DoRequest:Exception by executing jsonRequest: " + request
					+ " ; " + e.getLocalizedMessage());
		} 
		finally {
			if (connection != null)
				connection.disconnect();
		}

		return null;
	}

	private String _readDpInternal(String name, String dp, String type) {
		logger.debug("siemensHvac:readDP():" + name);


		if (sessionId == null)
			_doAuth();

		try {
			String request = "api/menutree/read_datapoint.json?SessionId="
					+ sessionId + "&Id=" + dp;

			logger.debug("siemensHvac:ReadDp:DoRequest():" + request);
			String response = DoRequest(request);

			logger.debug("siemensHvac:ReadDp:response:" + response);
			if (response != null) {
				JSONObject result = JSONResponseHandler.toJSONObject(response);
				if (result.containsKey("Result")) {
					JSONObject subResult = (JSONObject) result.get("Result");
					if (subResult.containsKey("Success")) {
						if (subResult.get("Success").equals("true")) {
							if (result.containsKey("Data")) {
								subResult = (JSONObject) result.get("Data");

								//{"Value":"Automatique","Type":"Enumeration","EnumValue":"1","Unit":""}
								String typer = "";
								String value = "";
								String enumValue = "";
								
								if (subResult.containsKey("Type")) {
									typer = subResult.get("Type").toString().trim();
								}
								if (subResult.containsKey("Value")) {
									value = subResult.get("Value").toString().trim();
								}
								if (subResult.containsKey("EnumValue")) {
									enumValue = subResult.get("EnumValue").toString().trim();
								}
								
								if (typer.equals("Enumeration")) {
									return "" + enumValue + ":" + value;
								}
								else {
									return value;
								}
							}
						}
						else {
							sessionId = null;
						}
					}
				}
			}
		} 
		catch (Exception e) {
			logger.error("siemensHvac:ReadDp:Error during dp reading: " + name + " ; "
					+ e.getLocalizedMessage());
			// Reset sessionId so we redone _auth on error
			sessionId = null;
		}

		return "";
	}

	public void AddDpUpdate(String itemName, Type dp) {
		synchronized (updateCommand) {
			updateCommand.put(itemName, dp);
			lastUpdate = new java.util.Date();
		}
	}

	public void run() {
		logger.debug("siemensHvac:sender thread start");

		// as long as no interrupt is requested, continue running
		while (!interrupted) {
			try {
				Thread.sleep(2000);
				Date currentDate = new java.util.Date();

				logger.debug("siemensHvac:sender thread alive:" + currentDate);

				if (lastUpdate == null)
					continue;

				long ms = currentDate.getTime() - lastUpdate.getTime();
				if (ms < 3000)
					continue;
				

				synchronized (updateCommand) {
					if (updateCommand.isEmpty())
						continue;

					logger.debug("siemensHvac:sender thread updateCommand");
					
					for (String key : updateCommand.keySet()) {
						Type dp = updateCommand.get(key);
						WriteDp(key, dp);
						
					}
					
					updateCommand.clear();
				}

				
			} 
			catch (Exception e) {
				logger.error("siemensHvac:Error occured will sending update values", e);
			}
		}
	}
}
