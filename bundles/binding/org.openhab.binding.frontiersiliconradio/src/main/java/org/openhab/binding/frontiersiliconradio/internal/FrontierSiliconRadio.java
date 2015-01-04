/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.frontiersiliconradio.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class representing a internet radio based on the frontier silicon chipset.
 * Tested with "hama IR110" internet radio.
 * 
 * @author Rainer Ostendorf
 * @since 1.7.0
 */
/**
 * @author rainer
 *
 */
/**
 * @author rainer
 *
 */
public class FrontierSiliconRadio 
{
	
	private static final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioConnection.class);
	
	// The http connection/session used for controlling the radio
	private FrontierSiliconRadioConnection conn;

	// the volume of the radio. we cache it for fast increase/decrease
	private int currentVolume=0;
	
	/**
	 *  Constructor for the Radio class
	 * 
	 * @param hostname Hostname of the Radio adressed, e.g. "192.168.2.137"
	 * @param port Portnumber, default: 80 (http)
	 * @param pin Access PIN number. Must be 4 digits on hama radio. e.g. "1234"
	 * 
	 * @author Rainer Ostendorf
	 * @since 1.7.0
	 */
	public FrontierSiliconRadio( String hostname, int port, String pin ) {
		this.conn = new FrontierSiliconRadioConnection( hostname, port, pin );
	}
	
	/**
	 * Perform login to the radio and establish new session
	 * 
	 * @author Rainer Ostendorf
	 * @since 1.7.0
	 */
	public void login() {
		conn.doLogin();
	}
	
	
	/**
	 * get the radios power state
	 * 
	 * @return true when radio is on, false when radio is off
	 */
	public Boolean getPower() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.sys.power");
			return result.getValueU8AsBoolean();
		}
		catch (Exception e) {
			logger.error("request failed");
		}
		
		return false;
	}
	
	/**
	 * Turn radio on/off
	 * 
	 * @param powerOn true turns on the radio, false turns it off
	 */
	public void setPower(Boolean powerOn) {
		try {
			conn.doRequest("SET/netRemote.sys.power", "value="+(powerOn?"1":"0"));
			return;
		}
		catch (Exception e) {
			logger.error("request failed");
		}
	}
	
	/**
	 * read the volume (as absolute value, 0-32)
	 * 
	 * @return volume: 0=muted, 32=max. volume
	 */
	public int getVolume() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.sys.audio.volume");
			currentVolume = result.getValueU8AsInt();
			return currentVolume;
		}
		catch (Exception e) {
			logger.error("request failed");
		}
		return 0;
	}
	
	/**
	 * Set the radios volume
	 * 
	 * @param volume Radio volume: 0=mute, 32=max. volume
	 */
	public void setVolume( int volume ) {
		try {
			conn.doRequest("SET/netRemote.sys.audio.volume", "value="+volume );
			currentVolume = volume;
			return;
		}
		catch (Exception e) {
			logger.error("request failed");
		}
	}
	
	/**
	 * increase radio volume by 1 step
	 * 
	 */
	public void increaseVolume() {
		if( currentVolume < 32)
			setVolume( currentVolume+1 );
	}
	
	/**
	 * decrease radio volume by 1 step
	 * 
	 */
	public void decreaseVolume() {
		if( currentVolume>0)
		setVolume( currentVolume-1 );
	}
	
	
	/**
	 *  Read the radios operating mode
	 * 
	 * @return operating mode. On hama radio: 0="Internet Radio", 1=Spotify, 2=Player, 3="AUX IN"
	 */
	public int getMode() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.sys.mode");
			return result.getValueU32AsInt();
		}
		catch (Exception e) {
			logger.error("request failed");
		}
		return 0;
	}
	
	/**
	 * Set the radio operating mode
	 * 
	 * @param mode On hama radio: 0="Internet Radio", 1=Spotify, 2=Player, 3="AUX IN"
	 */
	public void setMode( int mode ) {
		try {
			conn.doRequest("SET/netRemote.sys.mode", "value="+mode );
			return;
		}
		catch (Exception e) {
			logger.error("request failed");
		}
	}
	
	
	/**
	 * Read the Station info name, e.g. "WDR2"
	 * 
	 * @return the station name, e.g. "WDR2"
	 */
	public String getPlayInfoName() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.play.info.name");
			return result.getValueC8ArrayAsString();
		}
		catch (Exception e) {
			logger.error("request failed");
			return "";
		}
	}
	
	/**
	 * read the stations radio text like the song name currently playing
	 * 
	 * @return the radio info text, e.g. music title
	 */
	public String getPlayInfoText() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.play.info.text");
			return result.getValueC8ArrayAsString();
		}
		catch (Exception e) {
			logger.error("request failed");
			return "";
		}
	}
	
	
	/**
	 * set a station preset. Tunes the radio to a preselected station.
	 * 
	 * @param presetId
	 */
	public void setPreset(Integer presetId) {
		try {		
			conn.doRequest("SET/netRemote.nav.state", "value=1");
			conn.doRequest("SET/netRemote.nav.action.selectPreset", "value="+presetId.toString() );
			conn.doRequest("SET/netRemote.nav.state", "value=0");
			return;
		}
		catch (Exception e) {
			logger.error("request failed");
			return;
		}
	}
	
	/**
	 * read the muted state
	 * 
	 * @return true: radio is muted, false: radio is not muted
	 */
	public Boolean getMuted() {
		try {
			FrontierSiliconRadioApiResult result = conn.doRequest("GET/netRemote.sys.audio.mute");
			return result.getValueU8AsBoolean();
		}
		catch (Exception e) {
			logger.error("request failed");
		}
		
		return false;
	}
	
	
	/** 
	 * mute the radio volume
	 * 
	 * @param muted true: mute the radio, false: unmute the radio
	 */
	public void setMuted(Boolean muted) {
		try {
			conn.doRequest("SET/netRemote.sys.audio.mute", "value="+(muted?"1":"0"));
			return;
		}
		catch (Exception e) {
			logger.error("request failed");
		}
	}
	
	
	/**
	 * map the radio volume values to percent values
	 * 
	 * receiver volume 0 is 0%
	 * receiver volume 32 is 100%
	 * 
	 * @param volume the receiver volume value
	 * 
	 */
	public Integer convertVolumeToPercent( Integer volume )	{
		Integer percent = Math.round( (volume*100)/32 );
		logger.debug("converted volume '" +  volume.toString() + "' to '" + percent.toString() + "%'" );
		return percent;
	}
	
	/**
	 * map percent values to radio volumes
	 * 
	 * receiver volume 0 is 0%
	 * receiver volume 32 is 100%
	 * 
	 * @param volume the receiver volume value
	 * 
	 */
	public Integer convertPercentToVolume( Integer percent )	{
		Integer volume = Math.round( (percent*32)/100 );
		logger.debug("converted " +  percent.toString() + "% to volume " + volume.toString() );
		return volume;
	}
	
}
