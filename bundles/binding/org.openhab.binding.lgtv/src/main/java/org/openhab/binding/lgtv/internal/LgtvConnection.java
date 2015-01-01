/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EventObject;

import org.openhab.binding.lgtv.lginteraction.LgTvCommand;
import org.openhab.binding.lgtv.lginteraction.LgTvInteractor;
import org.openhab.binding.lgtv.lginteraction.LgTvMessageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.osgi.service.http.HttpService;

/**
 * This class open a TCP/IP connection to the LGTV device and send a command.
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgtvConnection implements LgtvEventListener {

	private static Logger logger = LoggerFactory.getLogger(LgtvConnection.class);

	private String ip = "";
	private int port = 0;
	private int checkalive = 0;
	private int lastvolume = -1;

	BundleContext bundleContext = null;
	private LgTvInteractor connection = null;
	private static LgTvMessageReader reader = null;

	public int getcheckalive() {
		return checkalive;
	}

	public boolean ispaired() {
		if (connection == null)
			return false;
		return connection.ispaired();
	}

	public void checkalive() {

		if (checkalive > 0 && connection != null) {

			long millis = System.currentTimeMillis();

			if (connection.getconnectionstatus() == LgTvInteractor.lgtvconnectionstatus.CS_PAIRED
					&& connection.getlastsuccessfulinteraction() < (millis - checkalive * 1000)) {
				String res = connection.getvolumeinfo(1);

				String currentvol = quickfind(res, "level");
				int cvol = Integer.parseInt(currentvol);

				if (lastvolume != cvol && reader != null) {

					String volume = "VOLUME_CURRENT=" + cvol;
					LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);
					reader.sendtohandlers(event, ip, volume);
				}

			} else if (connection.getconnectionstatus() == LgTvInteractor.lgtvconnectionstatus.CS_NOTCONNECTED) {
				InetAddress inet;
				try {
					inet = InetAddress.getByName(ip);
					if (inet.isReachable(5000)) {
						logger.debug("checkalive host reachable");
						connection.checkpairing();
					}
				} catch (UnknownHostException e) {
					logger.error("checkalive - host name=" + ip + " is unknown");
				} catch (IOException e) {
					// Don't handle - it's normal to get an exception when tv
					// switched off
				}

			}

		}
	}

	public LgtvConnection(String ip, int port, int localport, String pkey, String xmldf, int checkalive) {
		this.ip = ip;
		this.port = port;
		this.checkalive = checkalive;

		bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			connection = new LgTvInteractor(ip, port, localport, xmldf);
			connection.setpairkey(pkey);

		} catch (InterruptedException e) {
			logger.error("cannot create Interactor", e);
		}

		if (localport != 0 && reader == null) {
			reader = new LgTvMessageReader(localport);
			HttpService h = null;

			if (bundleContext != null) {
				ServiceReference<?> serviceReference = bundleContext.getServiceReference(HttpService.class.getName());
				if (serviceReference != null) {
					h = (HttpService) bundleContext.getService(serviceReference);
					reader.setHttpService(h);
				} else
					logger.error("serviceRefeerence=null");
			} else
				logger.error("bundleContext=null");
		}

		if (connection != null)
			addEventListener(connection);

		if (reader != null) {
			// interactor listens to events too - if a event is received from an
			// ip a device is up&paired
			connection.associatereader(reader);
		}
	}

	public void openConnection() {
		try {
			if (reader != null)
				reader.startserver();
		} catch (IOException e) {
			logger.error("Error at openconnection", e);
		}

		if (connection.getpairkey().length() == 0) {
			connection.requestpairkey();
		} else {
			connection.sendpairkey();
		}

	}

	public void closeConnection() {
		try {
			if (reader != null)
				reader.stopserver();
		} catch (IOException e) {
			logger.error("Error at closeconnection", e);
		}
	}

	public void addEventListener(LgtvEventListener listener) {
		if (reader != null)
			reader.addEventListener(listener);
	}

	public void removeEventListener(LgtvEventListener listener) {
		if (reader != null)
			reader.removeEventListener(listener);
	}

	public String quickfind(String sourcestring, String tag) {
		String retval = "#notfound";
		String starttag = "<" + tag + ">";
		int startpt = sourcestring.indexOf(starttag);
		if (startpt >= 0) {
			int endpt = sourcestring.indexOf("</" + tag + ">");
			if (endpt >= 0) {
				retval = sourcestring.substring(startpt + starttag.length(), endpt);
			}
		}
		return retval;
	}

	/**
	 * Sends a command to LGTV device.
	 * 
	 * @param cmd
	 *            LGTV command to send
	 * @param val
	 * @param itemName
	 */

	public String send(final String cmd, String val) {

		String result = "#error";

		connection.checkpairing();

		{

			try {
				LgTvCommand lgcmd = LgTvCommand.valueOf(cmd);

				logger.debug("send to tv ip=" + ip + " command=" + cmd + " type=" + lgcmd.getCommandType() + " val="
						+ val + " lgsendparam=" + lgcmd.getLgSendCommand());
				switch (lgcmd.getCommandType()) {
				case 0:
					result = connection.handlekeyinput(lgcmd.getLgSendCommand());
					break;
				case 1:
					result = connection.requestpairkey();
					break;
				case 2:
					connection.setpairkey(val);
					result = connection.sendpairkey();
					break;
				case 3:
					result = connection.handlechannelchangeeasy(val);
					break;
				case 4: {

					String answer = connection.getvolumeinfo(0);
					String ismuted = quickfind(answer, "mute");
					if (ismuted == "true")
						ismuted = "1";
					else
						ismuted = "0";

					String level = quickfind(answer, "level");
					logger.debug("getvolume answer=" + answer + " ismuted=" + ismuted + " level=" + level);

					String setter = "#nothing";
					setter = (lgcmd.getLgSendCommand() == "LEVEL") ? level : setter;
					setter = (lgcmd.getLgSendCommand() == "ISMUTED") ? ismuted : setter;
					result = setter;

				}
					;
					break;

				case 5: {
					String answer = connection.getcurrentchannel();
					String displaymajor = quickfind(answer, "displayMajor");
					String chname = quickfind(answer, "chname");
					String progname = quickfind(answer, "progName");
					logger.debug("getcurrentchannel answer=" + answer + " displayMajor=" + displaymajor + " chname="
							+ chname + " progname=" + progname);

					String setter = "#nothing";
					setter = (lgcmd.getLgSendCommand() == "NUMBER") ? displaymajor : setter;
					setter = (lgcmd.getLgSendCommand() == "PROG") ? progname : setter;
					setter = (lgcmd.getLgSendCommand() == "NAME") ? chname : setter;
					result = setter;
				}
					;
					break;

				case 6: {
					String answer = connection.getallchannels();
					result = answer;
				}
					;
					break;

				case 7: {
					String answer = connection.getallapps();
					result = answer;
				}
					;
					break;
				case 8: {

					String answer = connection.appexecuteeasy(val);
					result = answer;
				}
					;
					break;
				case 9: {
					String answer = connection.appterminateeasy(val);
					result = answer;
				}
					;
					break;
				case 11: {
					String answer = connection.handlevolchangeeasy(val);
					result = answer;
				}
					;
					break;
				case 12: {
					// dummy / fuer browser
					result = "";
				}
					;
					break;

				}
			} catch (Exception e) {

				logger.error("Could not send command to device on {} : {}", ip + ":" + port, e);
			}

		}
		return new String(result);
	}

	public void statusUpdateReceived(EventObject event, String ip, String data) {
		logger.debug("Mf: statusupdaterec ip=" + ip + " data=" + data);
		if (data.startsWith("CONNECTION_STATUS=CS_PAIRED")) {
			logger.debug("paired in connection object");
		}
	}
}
