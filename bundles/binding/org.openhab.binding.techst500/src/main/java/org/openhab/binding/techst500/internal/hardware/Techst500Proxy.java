/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.techst500.internal.hardware;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Tech ST 500 Proxy 
 * 
 * @author Eric Thill
 * @author Ben Jones
 * @author Stephan Noerenberg
 * @since 1.6.0
 */
public class Techst500Proxy {

	private final String host;
	private final String username;
	private final String password;

	public Techst500Proxy(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
		
	public Techst500State getState() throws IOException {
		
		float chTemperature = 0;
		float chTemperatureShould = 0;
		float fanSpeed = 0;
		boolean fanOperates = false;
		boolean feederOperates = false;
		boolean chPumpOperates = false;
		boolean wwPumpOperates = false;
		float htwTemperature = 0;
		String currentTime = "";
		Day currentDay = Day.MONDAY;
		float wwTemperature = 0;
		float outsideTemperature = 0;
		float feederTemperature = 0;
		
		String response = postAndGetResponse("gt=501,742,502,764,654,511,510,500,581,761,664,665,513,515,546,547,531a,512a,880,559,874,873a,873b,826,652,760,668,548,759,898,790,791,827,531,873,",
				"user");
		
		// sample response =  501_750,742_490,502_80,764_0,654_50,511_0,
		//   			      500_2,581_0,761_63536,664_2601,665_5,513_1,
		//					  515_0,531_100,512_1,880_270,826p1_0,826p2_0,
		//					  790_20525,791_19240,531_100,
		
		String[] values = response.split(",");
		
		for(String value: values){
			try {
				Pattern regex = Pattern.compile("(\\d+)_(\\d+)");
				Matcher regexMatcher = regex.matcher(value);
				
				while (regexMatcher.find()) {	
					// group (1) is key e.g. 501
					switch (Integer.parseInt(regexMatcher.group(1))) {
						
					// current temperature
					case 501:
						chTemperature = Integer.parseInt(regexMatcher.group(2)) / 10;
						break;
						
					case 502:
						chTemperatureShould = Integer.parseInt(regexMatcher.group(2));
						break;
						
					case 511:
						feederOperates = "1".equals(regexMatcher.group(2));
						break;

					case 512:
						fanOperates = "1".equals(regexMatcher.group(2));
						break;
						
					case 513:
						chPumpOperates = "1".equals(regexMatcher.group(2));
						break;
					
					case 515:
						wwPumpOperates = "1".equals(regexMatcher.group(2));
						break;
						
					case 531:
						fanSpeed = Integer.parseInt(regexMatcher.group(2));
						break;
						
					case 654:
						htwTemperature = Integer.parseInt(regexMatcher.group(2));
						break;		
						
					case 664:
						currentTime = getTimeFromString(Integer.parseInt(regexMatcher.group(2)));
						break;
						
					case 665:
						currentDay = getDayFromInt(Integer.parseInt(regexMatcher.group(2)));
						break;
						
					case 742:
						wwTemperature = Integer.parseInt(regexMatcher.group(2)) / 10;
						break;
						
					case 761:
						outsideTemperature = convertUint16toInt16(Integer.parseInt(regexMatcher.group(2))) / 10;
						break;
						
					case 880:
						feederTemperature = Integer.parseInt(regexMatcher.group(2)) / 10;
						break;
											
					default:
							break;
					}		
				} 
			} catch (PatternSyntaxException ex) {
				throw new PatternSyntaxException("regex to parse key/values", "(\\d+)_(\\d+)", 0);
			}
		}
		return new Techst500State(chTemperature, chTemperatureShould, fanSpeed, fanOperates, feederOperates, chPumpOperates, wwPumpOperates, htwTemperature, currentTime, currentDay, wwTemperature, outsideTemperature, feederTemperature);
	}
	
	private int convertUint16toInt16(int value)
	{
		if (value > 32767)
		{
			return -(65536 - value);
		}
		else
			return value;
		
	}
	
	public enum Day {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
	}
	
	public static Day getDayFromInt(int x) {
		switch (x) {
		case 0:
			return Day.SUNDAY;
		case 1:
			return Day.MONDAY;
		case 2:
			return Day.TUESDAY;
		case 3:
			return Day.WEDNESDAY;
		case 4:
			return Day.THURSDAY;
		case 5:
			return Day.FRIDAY;
		case 6:
			return Day.SATURDAY;
		}
		return null;
	}

	private String getTimeFromString(Integer value)
	{
		Integer hour = ((value >> 8) & 0xFF);
		Integer minute = (value & 0xFF);
		
		return String.format("%02d", hour) + ":" + String.format("%02d", minute);
	}
	
	private String postAndGetResponse(String message, String role) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://" + host + "/" + role + "/cgi-bin/edition.cgi");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(message.length()));
			
			// set username and password for basic authentication of ST-500
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			connection.setRequestProperty ("Authorization", basicAuth);
			
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(message);
			wr.flush();
			wr.close();
			
			// Read response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			throw new IOException("Could not handle http post", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public void setTemperatureShould(float newTemperatureShould) throws IOException {
		postAndGetResponse("st=502_" + Math.round(newTemperatureShould), "admin");		
	}
}
