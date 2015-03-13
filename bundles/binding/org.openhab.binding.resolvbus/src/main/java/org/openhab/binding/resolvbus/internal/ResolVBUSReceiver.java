package org.openhab.binding.resolvbus.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;


import org.openhab.binding.resolvbus.model.ResolVBUSConfig;
import org.openhab.binding.resolvbus.model.ResolVBUSDevice;
import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResolVBUSReceiver implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(ResolVBUSReceiver.class);

	private ResolVBUSListener listener;
	private Socket vBusSocket;
	private InputStream inStream;
	private boolean running = false;
	private ResolVBUSConfig config;
	private ResolVBUSInputStream resolStream;
	private List<Byte> resolStreamRAW;

	public ResolVBUSReceiver(ResolVBUSListener listener) {
		this.listener = listener;
	}

	/**
	 * Open Socket to the LAN-Adapter
	 */
	public void initializeReceiver(String host, int port, ResolVBUSConfig config) {

		try {
			this.config = config;
			getDeviceConfig();
			vBusSocket = new Socket(host, port);
			inStream = vBusSocket.getInputStream();
			logger.debug("Connected to: " + host + ":" + port);
		} catch (IOException e) {

			logger.debug(e.getMessage());
		}
	}

	private void getDeviceConfig() {

	}

	/**
	 * Stop the thread
	 */
	public void stopListener() {
		running = false;
		if (vBusSocket != null)
			try {
				vBusSocket.close();
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		vBusSocket = null;
	}

	@Override
	public void run() {
		if (vBusSocket == null)
			throw new IllegalStateException(
					"Cannot access socket. You must call"
							+ " call initializeListener(..) first!");

		if (initDevice())
			running = true; // start loop
		try {
			byte [] bBuffer = new byte[1];
			
			//Waiting for input which is send periodically
			while (running) {
				
				do {
					inStream.read(bBuffer);
					resolStreamRAW.add(bBuffer[0]);

				} while (bBuffer[0] != (byte) 0xAA);
		
				resolStreamRAW.add(0, (byte) 0xAA);

				resolStream = new ResolVBUSInputStream(resolStreamRAW);
				

				if (!resolStream.isErrorFree()) {
					logger.debug("Error in received stream...trying next stream");
					resolStreamRAW.clear();
					return;							
				}
				
				processData();	

				while (bBuffer[0] != (byte) 0xAA) {
					inStream.read(bBuffer);
				}
				Thread.sleep(5000);
			}
			
			inStream.close();
		
			
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}
	}

	private void processData() {
		if (resolStream!= null) {
			logger.debug("Processing resolStream...");
			
			
		}
		
	}

	private boolean initDevice() {
		String inputString;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					vBusSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					vBusSocket.getOutputStream()));

			inputString = reader.readLine();
			logger.debug("Received input: "+inputString);
			if (inputString.startsWith("+HELLO")) {
				logger.debug("Welcome message...sending password");
				writer.write("PASS vbus");
				writer.flush();
			} else {
				logger.debug("No welcome Message...Exiting");
				writer.close();
				reader.close();
				return false;
			}

			inputString = reader.readLine();
			logger.debug("Received input: "+inputString);
			if (inputString.startsWith("+OK:")) {
				logger.debug("Password accepted..");
				writer.write("DATA");
				writer.flush();
			} else {
				logger.debug("Password not accepted...Exiting");
				writer.close();
				reader.close();
				return false;
			}

			inputString = reader.readLine();
			logger.debug("Waiting for SYNC byte...");
			int syncByte = -1;
			while (syncByte != 127) {
				syncByte = reader.read();
			}
			logger.debug("SYNC byte received...Data processing..");
		} catch (IOException e) {
			logger.debug("Error while initializing LAN interface)");
			logger.debug(e.getMessage());
			return false;
		}
		return true;
	}

}
