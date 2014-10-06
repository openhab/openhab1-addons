/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation for culfw based devices which communicate via network port
 * (CUN for example).
 * 
 * @author Markus Heberling
 * @since 1.5.0
 */
public class CULNetworkHandlerImpl extends AbstractCULHandler {

	private static final int CUN_DEFAULT_PORT = 2323;
	private int credit10ms = 0;

	/**
	 * Thread which receives all data from the CUL.
	 * 
	 * @author Markus Heberling
	 * @since 1.5.0
	 * 
	 */
	private class ReceiveThread extends Thread {

		private final Logger logger = LoggerFactory.getLogger(ReceiveThread.class);

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					String data = br.readLine();
					log.debug("Received raw message from CUL: " + data);
					if ("EOB".equals(data)) {
						log.warn("(EOB) End of Buffer. Last message lost. Try sending less messages per time slot to the CUL");
						return;
					} else if ("LOVF".equals(data)) {
						log.warn("(LOVF) Limit Overflow: Last message lost. You are using more than 1% transmitting time. Reduce the number of rf messages");
						return;
					} else if (data.matches("^.. *\\d*"))
					{					
						String[] report = data.split(" ");					
						credit10ms = Integer.parseInt(report[report.length-1]);
						log.debug("credit10ms = "+credit10ms);
						return;
					}
					notifyDataReceived(data);
					requestCreditReport();
				} catch (IOException e) {
					log.error("Exception while reading from serial port", e);
					notifyError(e);
				}				
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					logger.debug("Error while sleeping in ReceiveThread", e);
				}
			}
		}
	}
	
	private final static Logger log = LoggerFactory.getLogger(CULNetworkHandlerImpl.class);

	private ReceiveThread receiveThread;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	private BufferedWriter bw;

	public CULNetworkHandlerImpl(String deviceName, CULMode mode) {
		super(deviceName, mode);
	}

	private void requestCreditReport()
	{
		/* this requests a report which provides credit10ms */
		log.debug("Requesting credit report");
		try {
			bw.write("X\r\n");
			bw.flush();
		} catch (IOException e) {
			log.error("Can't write report command to CUL", e);
		}
	}
	
	@Override
	protected void writeMessage(String message) {
		log.debug("Sending raw message to CUL: " + message);
		if (bw == null) {
			log.error("Can't write message, BufferedWriter is NULL");
		}
		synchronized (bw) {
			try {
				bw.write(message);
				bw.flush();
			} catch (IOException e) {
				log.error("Can't write to CUL", e);
			}
			
			requestCreditReport();
		}

	}

	@Override
	protected void openHardware() throws CULDeviceException {
		log.debug("Opening network CUL connection for " + deviceName);
		try {
			 URI uri = new URI("cul://" + deviceName);
			  String host = uri.getHost();
			  int port = uri.getPort()==-1?CUN_DEFAULT_PORT:uri.getPort();

			  if (uri.getHost() == null || uri.getPort() == -1) {
					throw new CULDeviceException("Could not parse host:port from "+deviceName);
			  }
			log.debug("Opening network CUL connection to " + host+":"+port);
			socket=new Socket(host, port);
			log.info("Connected network CUL connection to " + host+":"+port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));

			log.debug("Starting network listener Thread");
			receiveThread=new ReceiveThread();
			receiveThread.start();
		} catch (IOException e) {
			throw new CULDeviceException(e);
		} catch (URISyntaxException e) {
			throw new CULDeviceException("Could not parse host:port from "+deviceName, e);
		}

	}

	@Override
	protected void closeHardware() {
		receiveThread.interrupt();
		
		log.debug("Closing network device " + deviceName);
		
		try {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
		} catch (IOException e) {
			log.error("Can't close the input and output streams propberly", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.error("Can't close the socket propberly", e);
				}
			}
		}
	}

	@Override
	public int getCredit10ms() { 
		return credit10ms;
	}
}
