/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.squeezeserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.squeezebox.internal.SqueezeboxBinding;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.STATES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generation and parsing of SqueezeCenter commands
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public class SqueezeServer extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(SqueezeboxBinding.class);
	
	private String host;
	private int port;
	private int webport;
	private Socket clientSocket;
	
	private final static String strNewline = System.getProperty("line.separator");
	
	private final static String cmdGetPlayerList = "players 0";
	private final static String cmdGetPlayerStatus = " status - 1 subscribe:10 tags:yagJlN";
	private final static String cmdActivateListenMode = "listen 1";

	private Map<String, SqueezePlayer> players;
	
	/** The value by which the volume is changed by each INCREASE or DECREASE-Event */ 
	private static final int VOLUME_CHANGE_SIZE = 5;
	
	
	public SqueezeServer(String host, int port, int webport) {
		this.host = host;
		this.port = port;
		this.webport = webport;
		
		players = new HashMap<String, SqueezePlayer>();
	}
	
	public void addPlayer(SqueezePlayer player) {
		if (false == players.containsKey(player.getId())) {
			players.put(player.getId(), player);
		}
	}
	
	public void connect() {
		try {
			clientSocket = new Socket(host, port);
			this.start();
		} catch (Exception e) {
			logger.error("Connecting to server (host=" + host +", port=" + port + ") failed", e);
		}
	}
	
	public boolean isConnected() {
		return (clientSocket.isConnected());
	}
	
	private void setMixerVolume(String ohName, int volume) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			if (0 > volume) {
				volume = 0;
			} else if (100 < volume) {
				volume = 100;
			}
			
			sendMsg(id + " mixer volume " + String.valueOf(volume));
		}
		
	}
	
	public void muteByOhName(String ohName, boolean mute) {
		SqueezePlayer player = getPlayerByOhName(ohName);
		if (null != player) {

			if ((true == mute) && (0 < player.getVolume())) {
				setMixerVolume(ohName, 0);
			}else if ((false == mute) && (1 > player.getVolume())) {
				setMixerVolume(ohName, player.getUnmuteVolume());
			} 
		}
	}
	
	public void powerByOhName(String ohName, boolean power) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			if (true == power) {
				sendMsg(id + " power 1");
			} else {
				sendMsg(id + " power 0");
			} 
		}
	}
	
	
	public void syncPlayer(String ohName, String mac, boolean remove) {
		String id = getPlayerIdByOhName(ohName);
		if ((!mac.equals("")) && (!id.equals(""))) {
			if (true == remove) {
				sendMsg(id + " sync -");
			} else {
				sendMsg(id + " sync " + mac);
			} 
		}
	}
	
	public void stopByOhName(String ohName) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " stop");
		}
	}
	
	public void navigateByOhName(String ohName, boolean prev) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			if (true == prev) {
				sendMsg(id + " playlist index -1");
			} else {
				sendMsg(id + " playlist index +1");
			} 
		}
	}
	
	public void increaseVolumeByOhName(String ohName) {
		SqueezePlayer player = getPlayerByOhName(ohName);
		if (null != player) {
			setMixerVolume(ohName, player.getVolume() + VOLUME_CHANGE_SIZE);
		}
	}
	
	public void decreaseVolumeByOhName(String ohName) {
		SqueezePlayer player = getPlayerByOhName(ohName);
		if (null != player) {
			setMixerVolume(ohName, player.getVolume() - VOLUME_CHANGE_SIZE);
		}
	}
	
	public void setVolumeByOhName(String ohName, String volume) {
		SqueezePlayer player = getPlayerByOhName(ohName);
		if (null != player) {
			setMixerVolume(ohName, Integer.parseInt(volume));
		}
	}
	
	public void playByOhName(String ohName) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " play");
		}
	}
	
	public void playUrlByOhName(String ohName, String url) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " playlist play " + url);
		}
	}
	
	public void pauseByOhName(String ohName, boolean pause) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			if (true == pause) {
				sendMsg(id + " pause 1");
			} else {
				sendMsg(id + " pause 0");
			}
		}
	}
	
	public void showStringByOhName(String ohName, String line) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " show display " + line + " 5");
		}
	}
	
	public void showStringsByOhName(String ohName, String line1, String line2) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " show line1:" + line1 + " line2:" + line2);
		}
	}
	
	public void showStringByOhNameWithDuration(String ohName, String line, int i) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " show line1:" + line + " font:huge duration:" + String.valueOf(i));
		}
	}
	
	public void showStringsByOhNameWithDuration(String ohName, String line1, String line2, byte duration) {
		String id = getPlayerIdByOhName(ohName);
		if (!id.equals("")) {
			sendMsg(id + " show line1:" + line1 + " line2:" + line2 + " duration:" + String.valueOf(duration));
		}
	}
	
	private void sendMsg(String msg) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			writer.write(msg + strNewline);
			writer.flush();
		} catch (IOException e) {
			logger.error("sending of squeezeserver message failed");
		}
	}
	
	public void run() {
		String buffer;
		String bufferraw;
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sendMsg(cmdGetPlayerList);
			sendMsg(cmdActivateListenMode);
			
			while ((bufferraw = reader.readLine()) != null) {
				buffer = URLDecoder.decode(bufferraw, "UTF-8");
				logger.debug(buffer);
				if (true == buffer.contains(cmdGetPlayerList)) {
					readPlayerList(buffer);
				} else if (true == buffer.contains(cmdGetPlayerStatus)) {
					readPlayerStatus(bufferraw);
				}
			}
			
		} catch (IOException e) {
			logger.error("error while reading from squeezeserver");
		}
	}
	
	private String getParameterValue(String msg, String parameter) {
		String retVal = "";
		int pos, pos2, length;
		if (0 < (pos = msg.indexOf(parameter))) {
			length = parameter.length();
			pos2 = msg.indexOf(" ", pos + length);
			if (pos2 > 0) {
				retVal = msg.substring(pos + length, msg.lastIndexOf(" ", pos2));
			} else {
				retVal = msg.substring(pos + length);
			}
		}
		
		if (retVal.equals("0")) {
			retVal = "";
		}
		
		return retVal;
	}
	
	private void readPlayerStatus(String msg) {
		String[] paramList = msg.split("\\s");
		SqueezePlayer player = null;
		int pos, pos2;
		try {
			if (0 < paramList.length) {
				String id = URLDecoder.decode(paramList[0], "UTF-8");
				if (true == players.containsKey(id)) {
					player = players.get(id);
				}
			}

			String songArt = "";

			if (player != null) {
				for (String parameter : paramList) {
					logger.debug(parameter);
					// Parameter Power
					if (parameter.contains("power%3A")) {
						String value = parameter.substring(parameter
								.indexOf("%3A") + 3);
						if (value.matches("1")) {
							player.setPowerState(STATES.TRUE);
						} else if (value.matches("0")) {
							player.setPowerState(STATES.FALSE);
						}
					}
					// Parameter Volume
					else if (parameter.contains("mixer%20volume%3A")) {
						String value = parameter.substring(parameter
								.indexOf("%3A") + 3);
						player.setVolume(value);
					}
					// Parameter Mode
					else if (parameter.contains("mode%3A")) {
						String value = parameter.substring(parameter
								.indexOf("%3A") + 3);
						if (value.matches("play")) {
							player.setPlayState(STATES.TRUE);
						} else if (value.matches("stop")) {
							player.setStopState(STATES.TRUE);
						} else if (value.matches("pause")) {
							player.setPauseState(STATES.TRUE);
						}
					}
				}
				// Parameter Title
				player.setCurrentTitle(URLDecoder.decode(getParameterValue(msg, "title%3A"), "UTF-8"));
				// Parameter Genre
				player.setGenre(URLDecoder.decode(getParameterValue(msg, "genre%3A"), "UTF-8"));
				// Parameter Artist
				player.setArtist(URLDecoder.decode(getParameterValue(msg, "artist%3A"), "UTF-8"));
				// Parameter Year
				player.setYear(URLDecoder.decode(getParameterValue(msg, "year%3A"), "UTF-8"));
				// Parameter Album
				player.setAlbum(URLDecoder.decode(getParameterValue(msg, "album%3A"), "UTF-8"));
				// Parameter Art
				if (0 < (pos = msg.indexOf("artwork_track_id%3A"))) {
					pos2 = msg.indexOf(" ", pos + 19);
					if (pos2 > 0) {
						songArt = "http://" + host + ":" + String.valueOf(webport)
								+ "/music/" + msg.substring(pos + 19, msg.lastIndexOf(" ", pos2))
								+ "/cover.jpg";
					} else {
						songArt = msg.substring(pos + 19);
					}
				}
				player.setArt(URLDecoder.decode(songArt, "UTF-8"));
				// Parameter RemoteTitle (radio)
				player.setRemoteTitle(URLDecoder.decode(getParameterValue(msg, "remote_title%3A"), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("error while reading from squeezeserver");
		}
	}

	private void readPlayerList(String msg) {
		String[] playerList = msg.split("playerindex:\\d+\\s");

		for (String player : playerList) {
			String[] paramList = player.split("\\s");

			String id = null;
			String ip = null;
			String uuid = null;
			String name = null;
			String model = null;

			for (String parameter : paramList) {
				if (parameter.contains("playerid")) {
					id = parameter.substring(parameter.indexOf(":") + 1);
				} else if (parameter.contains("ip")) {
					ip = parameter.substring(parameter.indexOf(":") + 1);
				} else if (parameter.contains("uuid")) {
					uuid = parameter.substring(parameter.indexOf(":") + 1);
				} else if (parameter.contains("name")) {
					name = parameter.substring(parameter.indexOf(":") + 1);
				} else if (parameter.contains("model")) {
					model = parameter.substring(parameter.indexOf(":") + 1);
				}
			}

			if ((null != id) && (null != uuid) && (null != name)
					&& (null != model) && (null != ip)
					&& (true == players.containsKey(id))) {
				players.get(id).setUuid(uuid);
				players.get(id).setName(name);
				players.get(id).setModel(model);
				players.get(id).setIpAddr(ip);

				sendMsg(id + cmdGetPlayerStatus);
			}
		}
	}

	public SqueezePlayer getPlayerById(String id) {
		return (players.get(id));
	}

	public SqueezePlayer getPlayerByOhName(String name) {
		return (players.get(getPlayerIdByOhName(name)));
	}

	private String getPlayerIdByOhName(String ohName) {
		String retVal = "";

		for (Entry<String, SqueezePlayer> entry : players.entrySet()) {
			if (0 == entry.getValue().getOhName().compareTo(ohName)) {
				retVal = entry.getValue().getId();
			}
		}
		return (retVal);
	}
	
}
