/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.myopenhab.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.util.URIUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

/** 
 * This class provides communication between openHAB and my.openHAB service.
 * It also implements async http proxy for serving requests from user to
 * openHAB through my.openHAB. It uses Socket.IO connection to connect to
 * my.openHAB service and Jetty Http client to send local http requests to
 * openHAB.
 * 
 * @author Victor Belov
 * @since 1.3.0
 *
 */

public class MyOHClient {
	// Logger
	private static Logger logger = LoggerFactory.getLogger(MyOHClient.class);
	// Base URL for my.openhab service
	private String mMyOHBaseUrl = "https://my.openhab.org/";
	// openHAB UUID
	private String mUUID;
	// openHAB Secret
	private String mSecret;
	// local openHAB base URL
	private String mOHBaseUrl = "http://localhost:8080";
	// Http client for issuing local requests to openHAB
	private HttpClient mJettyClient;
	// HashMap of currently running HTTP requests to openHAB
	private HashMap<Integer, MyOHExchange> mRunningRequests;
	// Active connection flag
	private boolean mIsConnected;
	// Version of openHAB
	private String mOpenHABVersion;
	// Socket.io
	private Socket mSocket;
	// MyOHClient listener
	private MyOHClientListener mListener;

	/**
	 * Constructor of MyOHClient
	 * 
	 * @param uuid openHAB's UUID to connect to my.openHAB
	 * @param secret openHAB's Secret to connect to my.openHAB
	 * 
	 */

	public MyOHClient(String uuid, String secret) {
		mUUID = uuid;
		mSecret = secret;
		mRunningRequests = new HashMap<Integer, MyOHExchange>();
		mJettyClient = new HttpClient();
		mJettyClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		mJettyClient.setMaxConnectionsPerAddress(200);
		mJettyClient.setTimeout(30000);
		try {
			mJettyClient.start();
		} catch (Exception e) {
			logger.error("Error starting JettyClient: {}", e.getMessage());
		}
	}
	
	/**
	 * Connect to my.openHAB
	 */
	
	public void connect() {
		try {
			mSocket = IO.socket(mMyOHBaseUrl);
			mSocket.io().on(Manager.EVENT_TRANSPORT, 
				new Emitter.Listener() {
					@Override
					public void call(Object... args) {
						logger.debug("Manager.EVENT_TRANSPORT");
						Transport transport = (Transport)args[0];
						transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
							@Override
							public void call(Object... args) {
								logger.debug("Transport.EVENT_REQUEST_HEADERS");
								Map<String, String> headers = (Map<String, String>) args[0];
								headers.put("uuid", mUUID);
								headers.put("secret", mSecret);
								headers.put("openhabversion", mOpenHABVersion);
								headers.put("myohversion", MyOpenHABServiceImpl.myohVersion);
							}
						});
					}
				}
			);
			mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					logger.debug("Socket.IO connected");
					mIsConnected = true;
					onConnect();
				}
			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					logger.debug("Socket.IO disconnected");
					mIsConnected = false;
					onDisconnect();
				}
			}).on(Socket.EVENT_ERROR, new Emitter.Listener() {
								@Override
				public void call(Object... args) {
					logger.error("Socket.IO error: " + args[0]);
				}
			}).on("request", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					onEvent("request", (JSONObject)args[0]);
				}
			}).on("cancel", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					onEvent("cancel", (JSONObject)args[0]);
				}
			}).on("command", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					onEvent("command", (JSONObject)args[0]);
				}
			});
			mSocket.connect();
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Callback method for socket.io client which is called when connection is established
	 */
	
	public void onConnect() {
		logger.info("Connected to my.openHAB service");
		mIsConnected = true;
	}

	/**
	 * Callback method for socket.io client which is called when disconnect occurs
	 */
	
	public void onDisconnect() {
		logger.info("Disconnected from my.openHAB service");		
		mIsConnected = false;
	}

	/**
	 * Callback method for socket.io client which is called when an error occurs
	 */

	public void onError(IOException error) {
		logger.error(error.getMessage());
	}
	
	/**
	 * Callback method for socket.io client which is called when a message is received
	 */

	public void onEvent(String event, JSONObject data) {
		// TODO Auto-generated method stub
		logger.debug("on(): " + event);
		if (event.equals("request")) {
			try {
				// Get myOH uniq request Id
				int requestId = data.getInt("id");
				logger.debug(String.format("Got request %d", requestId));
				// Get request path
				String requestPath = data.getString("path");
				// Get request method
				String requestMethod = data.getString("method");
				// Get request body
				String requestBody = data.getString("body");
				// Get JSONObject for request headers
				JSONObject requestHeadersJson = data.getJSONObject("headers");
				logger.debug(requestHeadersJson.toString());
				// Get JSONObject for request query parameters
				JSONObject requestQueryJson = data.getJSONObject("query");
				Iterator<String> headersIterator = requestHeadersJson.keys();
				// Create URI builder with base request URI of openHAB and path from request
				String newPath = URIUtil.addPaths(mOHBaseUrl, requestPath);
				Iterator<String> queryIterator = requestQueryJson.keys();
				// Add query parameters to URI builder, if any
				newPath +="?";
				while (queryIterator.hasNext()) {
					String queryName = queryIterator.next();
					newPath += queryName;
					newPath += "=";
					newPath += URLEncoder.encode(requestQueryJson.getString(queryName), "UTF-8");
					if (queryIterator.hasNext())
						newPath += "&";
				}
				// Finally get the future request URI
				URI requestUri = new URI(newPath);
				// All preparations which are common for different methods are done
				// Now perform the request to openHAB
				// If method is GET
				logger.debug("Request method is " + requestMethod);
				MyOHExchange exchange = new MyOHExchange(requestId);
				exchange.setURI(requestUri);
				exchange.setRequestHeaders(requestHeadersJson);
				exchange.setRequestHeader("X-Forwarded-Proto", "https");
				if (requestMethod.equals("GET")) {
					exchange.setMethod("GET");
				// Else if method is POST
				} else if (requestMethod.equals("POST")) {
					exchange.setMethod("POST");
					Buffer requestContent = new ByteArrayBuffer(requestBody);
					exchange.setRequestContent(requestContent);
				} else {
					// TODO: Reject unsupported methods
					logger.error("Unsupported request method " + requestMethod);
					return;
				}
				mJettyClient.send(exchange);
				// If successfully submitted request to http client, add it to the list of currently
				// running requests to be able to cancel it if needed
				mRunningRequests.put(requestId, exchange);
			} catch (JSONException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (URISyntaxException e) {
				logger.error(e.getMessage());
			}
		} else if (event.equals("cancel")) {
			try {
				int requestId = data.getInt("id");
				logger.debug(String.format("Received cancel for request %d", requestId));
				// Find and cancel running request
				if (mRunningRequests.containsKey(requestId)) {
					MyOHExchange requestExchange = mRunningRequests.get(requestId);
					requestExchange.cancel();
					mRunningRequests.remove(requestId);
				}
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else if (event.equals("command")) {
			try {
				logger.debug("Received command " + data.getString("command") + " for item " + data.getString("item"));
				if (this.mListener != null)
					this.mListener.sendCommand(data.getString("item"), data.getString("command"));
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.warn("Unsopported event from my.openHAB: " + event);
		}
	}
	
	/**
	 * This method sends notification to my.openHAB
	 * 
	 * @param userId my.openHAB user id
	 * @param message notification message text
	 * @param icon name of the icon for this notification
	 * @param severity severity name for this notification
	 * 
	 */

	public void sendNotification(String userId, String message, String icon, String severity) {
		if (isConnected()) {
			JSONObject notificationMessage = new JSONObject();
			try {
				notificationMessage.put("userId", userId);
				notificationMessage.put("message", message);
				notificationMessage.put("icon", icon);
				notificationMessage.put("severity", severity);
				mSocket.emit("notification", notificationMessage);
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.debug("No connection, notification is not sent");
		}
	}

	/**
	 * Send SMS to my.openHAB
	 * 
	 * @param phone number to send notification to
	 * @param message text to send
	 * 
	 */
	
	public void sendSMS(String phone, String message) {
		if (isConnected()) {
			JSONObject smsMessage = new JSONObject();
			try {
				smsMessage.put("phone", phone);
				smsMessage.put("message", message);
				mSocket.emit("sms", smsMessage);
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.debug("No connection, SMS is not sent");
		}
	}

	/**
	 * Send item update to my.openHAB
	 * 
	 * @param itemName the name of the item
	 * @param itemStatus updated item status
	 * 
	 */

	public void sendItemUpdate(String itemName, String itemStatus) {
		if (isConnected()) {
			JSONObject itemUpdateMessage = new JSONObject();
			try {
				itemUpdateMessage.put("itemName", itemName);
				itemUpdateMessage.put("itemStatus", itemStatus);
				mSocket.emit("itemupdate", itemUpdateMessage);
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.debug("No connection, Item update is not sent");
		}
	}
	
	/**
	 * Returns true if my.openHAB connection is active
	 */

	public boolean isConnected() {
		return mIsConnected;
	}

	/**
	 * Disconnect from my.openHAB
	 */

	public void shutdown() {
		logger.info("Shutting down my.openHAB service connection");
		try {
			mJettyClient.stop();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		mSocket.disconnect();
	}

	/**
	 * Set base URL of my.openHAB cloud
	 * 
	 * @param myOHBaseUrl base URL in http://host:port form
	 * 
	 */

	public void setMyOHBaseUrl(String myOHBaseUrl) {
		mMyOHBaseUrl = myOHBaseUrl;
	}

	/**
	 * Set base local URL of openHAB
	 * 
	 * @param ohBaseUrl base local URL of openHAB in http://host:port form
	 * 
	 */

	public void setOHBaseUrl(String ohBaseUrl) {
		this.mOHBaseUrl = ohBaseUrl;
	}

	public String getOpenHABVersion() {
		return mOpenHABVersion;
	}

	public void setOpenHABVersion(String mOpenHABVersion) {
		this.mOpenHABVersion = mOpenHABVersion;
	}
	
	public void setListener(MyOHClientListener mListener) {
		this.mListener = mListener;
	}

	/*
	 * An internal class which extends ContentExchange and forwards response
	 * headers and data back to my.openHAB
	 * 
	 */
	
	private class MyOHExchange extends ContentExchange {
		
		private int mRequestId;
		private HashMap<String, String> mResponseHeaders;

		public MyOHExchange(int requestId) {
			mRequestId = requestId;
			mResponseHeaders = new HashMap<String, String>();
		}

		public void setRequestHeaders(JSONObject requestHeadersJson) {
			Iterator<String> headersIterator = requestHeadersJson.keys();
			// Convert JSONObject of headers into Header ArrayList
			while (headersIterator.hasNext()) {
				String headerName = headersIterator.next();
				String headerValue;
				try {
					headerValue = requestHeadersJson.getString(headerName);
					logger.debug("Jetty set header " + headerName + " = " + headerValue);
					if (!headerName.equalsIgnoreCase("Content-Length"))
						this.setRequestHeader(headerName, headerValue);
				} catch (JSONException e) {
					logger.error("Error processing request headers: {}", e.getMessage());
				}
			}			
		}

		public JSONObject getJSONHeaders() {
			JSONObject headersJSON = new JSONObject();
			try {
				for (Map.Entry<String, String> responseHeader : mResponseHeaders.entrySet()) {
						headersJSON.put(responseHeader.getKey(), responseHeader.getValue());
				}
			} catch (JSONException e) {
				logger.error("Error forming response headers: {}", e.getMessage());
			}
			return headersJSON;
		}

		/*
		 * This is old onResponseContent which used base64 encoding, keep it here commented for some time
		 * The new onResponseContent uses 'responseContentBinary' to send response data
		 
		@Override
		public void onResponseContent(Buffer content) {
			logger.debug("Jetty received response content of size " + String.valueOf(content.length()));
			JSONObject responseJson = new JSONObject();
			String base64ResponseBody = Base64.encodeBytes(content.asArray());
			try {
				responseJson.put("id", mRequestId);
				responseJson.put("body", base64ResponseBody);
				if (this.getStatus() != STATUS_CANCELLING && this.getStatus() != STATUS_CANCELLED)
					mSocket.emit("responseContent", responseJson);
				logger.debug(String.format("Sent content to request %d", mRequestId));
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}*/
		
		/*
		 * This is a new onResponseContent which uses binary encoding
		 */

		@Override
		public void onResponseContent(Buffer content) {
			logger.debug("Jetty received response content of size " + String.valueOf(content.length()));
			JSONObject responseJson = new JSONObject();
			try {
				responseJson.put("id", mRequestId);
				responseJson.put("body", content.asArray());
				if (this.getStatus() != STATUS_CANCELLING && this.getStatus() != STATUS_CANCELLED)
					mSocket.emit("responseContentBinary", responseJson);
				logger.debug(String.format("Sent content to request %d", mRequestId));
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		}

		@Override
		public void onResponseHeader(Buffer name, Buffer value) {
			mResponseHeaders.put(name.toString(), value.toString());
			logger.debug("Jetty received header " + name.toString() + " = " + value.toString());
		}

		@Override
		public void onResponseHeaderComplete() {
			logger.debug("Jetty finished receiving response header");
			JSONObject responseJson = new JSONObject();
			try {
				responseJson.put("id", mRequestId);
				responseJson.put("headers", getJSONHeaders());
				responseJson.put("responseStatusCode", getResponseStatus());
				responseJson.put("responseStatusText", "OK");
				if (this.getStatus() != STATUS_CANCELLING && this.getStatus() != STATUS_CANCELLED)
					mSocket.emit("responseHeader", responseJson);
				logger.debug(String.format("Sent headers to request %d", mRequestId));
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		}

		@Override
		protected void onResponseComplete() {
			int status = getResponseStatus();
			logger.debug(String.format("Jetty request complete %d with status %d", mRequestId, status));
			// Remove this request from list of running requests
			mRunningRequests.remove(mRequestId);
			JSONObject responseJson = new JSONObject();
			try {
				responseJson.put("id", mRequestId);
				if (this.getStatus() != STATUS_CANCELLING && this.getStatus() != STATUS_CANCELLED)
					mSocket.emit("responseFinished", responseJson);
				logger.debug(String.format("Finished responding to request %d", mRequestId));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}
		
		@Override
		protected void onConnectionFailed(Throwable x) {
			logger.error(x.getMessage());
			JSONObject responseJson = new JSONObject();
			try {
				responseJson.put("id", mRequestId);
				responseJson.put("responseStatusText", "openHAB connection error: " + x.getMessage());
				if (this.getStatus() != STATUS_CANCELLING && this.getStatus() != STATUS_CANCELLED)
					mSocket.emit("responseError", responseJson);
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		}
		
	}
}
