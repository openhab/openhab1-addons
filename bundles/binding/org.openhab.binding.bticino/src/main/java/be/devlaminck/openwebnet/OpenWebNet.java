/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package be.devlaminck.openwebnet;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.bticino.internal.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myhome.fcrisciani.connector.MyHomeJavaConnector;
import com.myhome.fcrisciani.exception.MalformedCommandOPEN;

/**
 * OpenWebNet - OpenHab device communicator Based on code from Mauro Cicolella
 * (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic/freedomotic
 * /tree/master/plugins/devices/openwebnet) and on code of Flavio Fcrisciani
 * released as EPL (https://github.com/fcrisciani/java-myhome-library)
 * 
 * @author Tom De Vlaminck, LAGO Moreno
 * @serial 1.0
 * @since 1.7.0
 */
public class OpenWebNet extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(OpenWebNet.class);

	/*
	 * Initializations
	 */
	private String host = "";

	// standard port for the MH200(N) of bticino
	private Integer port = 20000;
	private Date lastBusScan = new Date(0);

	// bus scan interval in seconds
	private Integer scanInterval = 120;
	private Integer firstScanDelay = 60;
	public MyHomeJavaConnector myHomeConnector = null;
	private MonitorSessionThread monitorSessionThread = null;

	/*
	 * OWN Diagnostic Frames
	 */
	private final static String LIGHTNING_DIAGNOSTIC_FRAME = "*#1*0##";
	private final static String AUTOMATIONS_DIAGNOSTIC_FRAME = "*#2*0##";
	private final static String ALARM_DIAGNOSTIC_FRAME = "*#5##";
	private final static String POWER_MANAGEMENT_DIAGNOSTIC_FRAME = "*#3##";
	// private final static String TERMOREGULATION_DIAGNOSTIC_FRAME =
	// "*#4*00##";

	/*
	 * Event listeners = they receive an object when something happens on the
	 * bus
	 */
	private List<IBticinoEventListener> eventListenerList = new LinkedList<IBticinoEventListener>();

	// ------------------------------------------------------------------------

	public OpenWebNet(String host, int port, int rescanInterval) throws InitializationException {
		if (StringUtils.isBlank(host)) {
			throw new InitializationException("Invalid host name [" + host + "]");
		}
		try {
			this.host = host;
			this.port = port;
			this.scanInterval = rescanInterval;
		} catch (Exception ex) {
			throw new InitializationException("Error on OpenWebNet creation [host :" + host + ", port :" + port
					+ ", rescanInterval :" + rescanInterval);
		}
	}

	// ------------------------------------------------------------------------

	public MyHomeJavaConnector connect() {
		this.myHomeConnector = new MyHomeJavaConnector(getHost(), getPort());
		return this.myHomeConnector;
	}

	// ------------------------------------------------------------------------

	public void startMonitoring() throws IOException {
		this.myHomeConnector.startMonitoring();
	}

	// ------------------------------------------------------------------------

	public String readMonitoring() throws InterruptedException {
		return this.myHomeConnector.readMonitoring();
	}

	// ------------------------------------------------------------------------

	public String getHost() {
		return this.host;
	}

	// ------------------------------------------------------------------------

	public int getPort() {
		return this.port;
	}

	// ------------------------------------------------------------------------

	/*
	 * Sensor side
	 */
	public void onStart() {
		// create thread
		monitorSessionThread = new MonitorSessionThread(this);

		// start first bus scan 30 secs later
		lastBusScan = new Date((new Date()).getTime() - (1000 * this.scanInterval) + (1000 * firstScanDelay));

		// start thread
		monitorSessionThread.start();

		logger.trace("Connected to [" + this.host + ":" + this.port + "], Rescan bus every [" + this.scanInterval
				+ "] seconds, first scan over [" + (((new Date()).getTime() - lastBusScan.getTime()) / 1000)
				+ "] seconds.");
		// start the processing thread
		start();
	}

	// ------------------------------------------------------------------------

	/**
	 * Actuator side Send command on bus
	 */
	public void onCommand(ProtocolRead c) throws IOException, Exception {
		try {
			myHomeConnector.sendCommandAsync(OWNUtilities.createFrame(c), 1);
		} catch (MalformedCommandOPEN ex) {
			logger.error("onCommand error : " + ex.getMessage());
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// synchronizes the software with the system status
				// Every x seconds do a full bus scan
				checkForBusScan();
				Thread.sleep(1000);
			}
		} catch (InterruptedException iex) {
			logger.error("Openwebnet.run, InterruptedException : " + iex.getMessage());
		} catch (Exception ex) {
			logger.error("Openwebnet.run, Exception : " + ex.getMessage());
		} finally {
			// interrupt handler on monitor thread will stop thread
			monitorSessionThread.interrupt();
			logger.error("Stopped monitorSessionThread thread");
		}
		logger.error("Stopped OpenWebNet thread");
	}

	// ------------------------------------------------------------------------

	private void checkForBusScan() {
		Date now = new Date();
		if (((now.getTime() - lastBusScan.getTime()) / 1000) > this.scanInterval) {
			initSystem();
			this.lastBusScan = now;
		}
	}

	// ------------------------------------------------------------------------

	// sends diagnostic frames to initialize the system
	private void initSystem() {
		try {

			logger.info("Sending " + LIGHTNING_DIAGNOSTIC_FRAME + " frame to (re)initialize LIGHTNING");
			myHomeConnector.sendCommandSync(LIGHTNING_DIAGNOSTIC_FRAME);

			logger.info("Sending " + AUTOMATIONS_DIAGNOSTIC_FRAME + " frame to (re)initialize AUTOMATIONS");
			myHomeConnector.sendCommandSync(AUTOMATIONS_DIAGNOSTIC_FRAME);

			logger.info("Sending " + ALARM_DIAGNOSTIC_FRAME + " frame to (re)initialize ALARM");
			myHomeConnector.sendCommandSync(ALARM_DIAGNOSTIC_FRAME);

			logger.info("Sending " + POWER_MANAGEMENT_DIAGNOSTIC_FRAME + " frame to (re)initialize POWER MANAGEMENT");
			myHomeConnector.sendCommandSync(POWER_MANAGEMENT_DIAGNOSTIC_FRAME);

			// logger.info("Sending " + TERMOREGULATION_DIAGNOSTIC_FRAME + "
			// frame to (re)initialize T");
			// myPlant.sendCommandSync(TERMOREGULATION_DIAGNOSTIC_FRAME);

		} catch (Exception e) {
			logger.error("initSystem failed : ", e);
		}
	}

	// ------------------------------------------------------------------------

	public void notifyEvent(ProtocolRead event) {
		for (IBticinoEventListener eventListener : eventListenerList) {
			try {
				eventListener.handleEvent(event);
			} catch (Exception ex) {
				logger.error("notifyEvent, Exception : ", ex);
			}
		}
	}

	// ------------------------------------------------------------------------

	public void addEventListener(IBticinoEventListener eventListener) {
		if (eventListener != null)
			eventListenerList.add(eventListener);
		else
			logger.warn("addEventListener failed, event listener to add is null");
	}

	// ------------------------------------------------------------------------

	public void removeEventListener(IBticinoEventListener eventListener) {
		if (eventListener != null)
			eventListenerList.remove(eventListener);
	}

	// ------------------------------------------------------------------------

}
