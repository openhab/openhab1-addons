/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver.hub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.openhab.binding.insteonplm.internal.driver.IOStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements IOStream for a Hub 2014 device
 * @author Daniel Pfrommer
 * @since 1.7.0
 *
 */
public class HubIOStream extends IOStream implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HubIOStream.class);
	
	/** time between polls (in milliseconds */
	private int		m_pollTime					= 1000;

	private String	m_host 						= null;
	private int		m_port 						= -1;
	private String	m_user						= null;
	private String	m_pass						= null;
	
	private DefaultHttpClient m_client 			= null;
	private Thread m_pollThread 				= null;
	
	// index of the last byte we have read in the buffer
	private int m_bufferIdx 					= -1;
	
	/**
	 * Constructor for HubIOStream
	 * @param host	host name of hub device
	 * @param port	port to connect to
	 * @param pollTime	time between polls (in milliseconds)
	 * @param user	hub user name
	 * @param pass	hub password
	 */
	public HubIOStream(String host, int port, int pollTime, String user, String pass) {
		m_host		= host;
		m_port		= port;
		m_pollTime	= pollTime;
		m_user 		= user;
		m_pass 		= pass;
	}
	
	@Override
	public boolean open() {
		m_client = new DefaultHttpClient();
		if (m_user != null && m_pass != null) {
			m_client.getCredentialsProvider().setCredentials(new AuthScope(m_host, m_port),  
															 new UsernamePasswordCredentials(m_user, m_pass));
		}
		HttpConnectionParams.setConnectionTimeout(m_client.getParams(), 5000);

		m_in = new HubInputStream();

		m_pollThread = new Thread(this);
		m_pollThread.start();
		
		m_out = new HubOutputStream();
		return true;
	}

	@Override
	public void close() {
		m_pollThread.interrupt();
		m_client = null;
		
		try {
			m_in.close();
			m_out.close();
		} catch (IOException e) {
			logger.error("failed to close streams", e);
		}
	}
	
	/**
	 * Fetches the latest status buffer from the Hub
	 * @return string with status buffer
	 * @throws IOException
	 */
	private synchronized String bufferStatus() throws IOException {
		String result = getURL("/buffstatus.xml");
		String[] parts = result.split("<BS>");
		if (parts.length > 1) {
			result = parts[1].split("</BS>")[0].trim();
		} else if (result.startsWith("401 Unauthorized:")) {
			logger.error("bad username or password. See bottom label of hub for correct login");
			throw new IOException("login credentials incorrect");
		} else {
			logger.error("got invalid buffer status: {}", result);
			throw new IOException("malformed bufferstatus.xml");
		}
		return result;
	}
	/**
	 * Sends command to Hub to clear the status buffer
	 * @throws IOException
	 */
	private synchronized void clearBuffer() throws IOException {
		getURL("/1?XB=M=1");
		m_bufferIdx = 0;
	}

	/**
	 * Sends Insteon message (byte array) as a readable ascii string to the Hub 
	 * @param msg byte array representing the Insteon message
	 * @throws IOException in case of I/O error
	 */
	public synchronized void write(ByteBuffer msg) throws IOException {
		poll(); 		// fetch the status buffer before we send out commands
		clearBuffer(); 	// clear the status buffer explicitly.
		
		StringBuilder b = new StringBuilder();
		while (msg.remaining() > 0) {
			b.append(String.format("%02x", msg.get()));
		}
		String hexMSG = b.toString();
		getURL("/3?"+ hexMSG + "=I=3");
	}

	/**
	 * Polls the Hub web interface to fetch the status buffer
	 * @throws IOException if something goes wrong with I/O
	 */
	public synchronized void poll() throws IOException {
		String buffer = bufferStatus(); // fetch via http call
		logger.trace("poll: {}", buffer);
		//
		// The Hub maintains a ring buffer where the last two digits (in hex!) represent
		// the position of the last byte read.
		//
		String data = buffer.substring(0, buffer.length() - 2); // pure data w/o index pointer

		int nIdx = -1;
		try {
			nIdx = Integer.parseInt(buffer.substring(buffer.length() - 2, buffer.length()), 16);
		} catch (NumberFormatException e) {
			m_bufferIdx = -1;
			logger.error("invalid buffer size received in line: {}", buffer);
			return;
		}
		
		if (m_bufferIdx == -1) {
			// this is the first call or first call after error, no need for buffer copying
			m_bufferIdx = nIdx;
			return; // XXX why return here???? 
		}
		
		StringBuilder msg = new StringBuilder();
		if (nIdx < m_bufferIdx) {
			msg.append(data.substring(m_bufferIdx + 1, data.length()));
			msg.append(data.substring(0, nIdx));
			logger.trace("wrap around: copying new data on: {}", msg.toString());
		} else {
			msg.append(data.substring(m_bufferIdx, nIdx));
			logger.trace("no wrap:      appending new data: {}", msg.toString());
		}
		if (msg.length() != 0) {
			ByteBuffer buf = ByteBuffer.wrap(s_hexStringToByteArray(msg.toString()));
			((HubInputStream) m_in).handle(buf);
		}
		m_bufferIdx = nIdx;
	}

	/**
	 * Helper method to fetch url from http server
	 * @param resource the url
	 * @return contents returned by http server
	 * @throws IOException
	 */
	private String getURL(String resource) throws IOException {
		synchronized (m_client) {
			StringBuilder b = new StringBuilder();
			b.append("http://");
			b.append(m_host);
			if (m_port != -1) {
				b.append(":").append(m_port);
			}
			b.append(resource);

			HttpGet get = new HttpGet(b.toString());
			HttpResponse res = m_client.execute(get);
			String  html = EntityUtils.toString(res.getEntity());
			return html;
		}
    }

	/**
	 * Entry point for thread
	 */
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				poll();
			} catch (IOException e) {
				logger.error("got exception while polling: {}", e.toString());
			}
			try {
				Thread.sleep(m_pollTime);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * Helper function to convert an ascii hex string (received from hub)
	 * into a byte array
	 * @param s string received from hub
	 * @return simple byte array
	 */
	public static byte[] s_hexStringToByteArray(String s) {
		return new BigInteger(s, 16).toByteArray();
	}

	/**
	 * Implements an InputStream for the Hub 2014
	 * @author Daniel Pfrommer
	 *
	 */
	public class HubInputStream extends InputStream {	
		
		//A buffer to keep bytes while we are waiting for the inputstream to read
		private ReadByteBuffer m_buffer = new ReadByteBuffer(1024);

		public HubInputStream() {}
		
		public void handle(ByteBuffer buffer) throws IOException {
			//Make sure we cleanup as much space as possible
			m_buffer.makeCompact();
			m_buffer.add(buffer.array());
		}
		
		@Override
		public int read() throws IOException {
			return m_buffer.get();
		}
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return m_buffer.get(b, off, len);
		}
		@Override
		public void close() throws IOException {}
	}

	/**
	 * Implements an OutputStream for the Hub 2014
	 * 
	 * @author Daniel Pfrommer
	 *
	 */
	public class HubOutputStream extends OutputStream {
		private ByteArrayOutputStream m_out = new ByteArrayOutputStream();
		
		@Override
		public void write(int b) {
			m_out.write(b);
			flushBuffer();
		}
		@Override
		public void write(byte[] b, int off, int len) {
			m_out.write(b, off, len);
			flushBuffer();
		}
		private void flushBuffer() {
			ByteBuffer buffer = ByteBuffer.wrap(m_out.toByteArray());
			try {
				HubIOStream.this.write(buffer);
			} catch (IOException e) {
				logger.error("failed to write to hub: {}", e.toString());
			}
			m_out.reset();
		}
	}
}
