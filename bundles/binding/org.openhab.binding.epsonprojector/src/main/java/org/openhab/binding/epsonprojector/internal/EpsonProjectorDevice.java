/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.epsonprojector.connector.EpsonProjectorConnector;
import org.openhab.binding.epsonprojector.connector.EpsonProjectorSerialConnector;
import org.openhab.binding.epsonprojector.connector.EpsonProjectorTcpConnector;
import org.openhab.binding.epsonprojector.i18n.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide high level interface to Epson projector.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorDevice {

	public enum AspectRatio {
		NORMAL(0x00), AUTO(0x30), FULL(0x40), ZOOM(0x50), WIDE(0x70), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, AspectRatio> typesByValue = new HashMap<Integer, AspectRatio>();

	    static {
	        for (AspectRatio type : AspectRatio.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private AspectRatio(int value) {
			this.value = value;
		}

	    public static AspectRatio forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}
	}

	public enum Luminance {
		NORMAL(0x00), ECO(0x01), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, Luminance> typesByValue = new HashMap<Integer, Luminance>();

	    static {
	        for (Luminance type : Luminance.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private Luminance(int value) {
			this.value = value;
		}

		public static Luminance forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum Source {
		COMPONENT(0x14), PC_DSUB(0x20), HDMI1(0x30), HDMI2(0xA0), VIDEO(0x41), 
		SVIDEO(0x42), ERROR(0xFF);

		private int value;
		private static final Map<Integer, Source> typesByValue = new HashMap<Integer, Source>();

	    static {
	        for (Source type : Source.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private Source(int value) {
			this.value = value;
		}

		public static Source forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}
	}

	public enum ColorMode {
		DYNAMIC(0x06), NATURAL(0x07), XVCOLOR(0x0B), LIVINGROOM(0x0C), 
		CINEMA(0x15), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, ColorMode> typesByValue = new HashMap<Integer, ColorMode>();

	    static {
	        for (ColorMode type : ColorMode.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private ColorMode(int value) {
			this.value = value;
		}

	    public static ColorMode forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}
	}

	public enum PowerStatus {
		STANDBY(0x00), ON(0x01), WARPUP(0x02), COOLDOWN(0x03), 
		STANDBYNETWORKON(0x04), ABNORMALSTANDBY(0x05), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, PowerStatus> typesByValue = new HashMap<Integer, PowerStatus>();

	    static {
	        for (PowerStatus type : PowerStatus.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private PowerStatus(int value) {
			this.value = value;
		}

	    public static PowerStatus forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}
	}

	public enum Sharpness {
		STANDARD(0x00), HIGHPASS(0x01), LOWPASS(0x02), HORIZONTAL(0x04), 
		VERTICAL(0x05), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, Sharpness> typesByValue = new HashMap<Integer, Sharpness>();

	    static {
	        for (Sharpness type : Sharpness.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private Sharpness(int value) {
			this.value = value;
		}

	    public static Sharpness forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum GammaStep {
		TONE1(0x00), TONE2(0x01), TONE3(0x02), TONE4(0x03), TONE5(0x04), 
		TONE6(0x05), TONE7(0x06), TONE8(0x07), TONE9(0x08), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, GammaStep> typesByValue = new HashMap<Integer, GammaStep>();

	    static {
	        for (GammaStep type : GammaStep.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private GammaStep(int value) {
			this.value = value;
		}

	    public static GammaStep forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum Color {
		RGB_RGBCMY(0x07), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, Color> typesByValue = new HashMap<Integer, Color>();

	    static {
	        for (Color type : Color.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private Color(int value) {
			this.value = value;
		}

	    public static Color forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum Gamma {
		G2_0(0x20), G2_1(0x21), G2_2(0x22), G2_3(0x23), G2_4(0x24), 
		CUSTOM(0xF0), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, Gamma> typesByValue = new HashMap<Integer, Gamma>();

	    static {
	        for (Gamma type : Gamma.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }

	    private Gamma(int value) {
			this.value = value;
		}
	    
	    public static Gamma forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum Background {
		BLACK(0x00), BLUE(0x01), LOGO(0x02), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, Background> typesByValue = new HashMap<Integer, Background>();

	    static {
	        for (Background type : Background.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private Background(int value) {
			this.value = value;
		}

	    public static Background forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum CommunicationSpeed {
		S9600(0x00), S18200(0x01), S38400(0x02), S57600(0x03), ERROR(0xFF);

		private int value;
	    private static final Map<Integer, CommunicationSpeed> typesByValue = new HashMap<Integer, CommunicationSpeed>();

	    static {
	        for (CommunicationSpeed type : CommunicationSpeed.values()) {
	            typesByValue.put(type.value, type);
	        }
	    }
	    
		private CommunicationSpeed(int value) {
			this.value = value;
		}

	    public static CommunicationSpeed forValue(int value) {
	        return typesByValue.get(value);
	    }

		public int toInt() {
			return value;
		}

	}

	public enum Switch {
		ON, OFF;
	}

	final private int defaultTimeout = 5000;
	final private int powerStateTimeout = 40000;

	private static Logger logger = LoggerFactory
			.getLogger(EpsonProjectorDevice.class);

	private EpsonProjectorConnector connection = null;
	private boolean connected = false;
	
	public EpsonProjectorDevice(String serialPort) {
		connection = (EpsonProjectorConnector) new EpsonProjectorSerialConnector(serialPort);
	}

	public EpsonProjectorDevice(String ip, int port) {
		connection = (EpsonProjectorConnector) new EpsonProjectorTcpConnector(ip, port);
	}
	
	private String sendQuery(String query, int timeout) throws EpsonProjectorException {
		
		logger.debug("Query: '{}'", query);
		String response = connection.sendMessage(query, timeout);
		response = response.replace("\r:", "");
		logger.debug("Response: '{}'", response);
		
		if (response.length() == 0)
			throw new EpsonProjectorException("No response received");

		if (response.equals("ERR"))
			throw new EpsonProjectorException("Error response received");

		return response;
	}

	@SuppressWarnings("unused")
	private String sendQuery(String query) throws EpsonProjectorException {
		return sendQuery(query, defaultTimeout);
	}

	protected void sendCommand(String command, int timeout) throws EpsonProjectorException {
		sendQuery(command, timeout);
	}

	protected void sendCommand(String command) throws EpsonProjectorException {
		sendCommand(command, defaultTimeout);
	}

	protected int queryInt(String query, int timeout, int radix) throws EpsonProjectorException {
		
		String response = sendQuery(query, timeout);
		
		if (response != null && !response.equals("")) {
			
			try {
				String[] pieces = response.split("=");
				String str = pieces[1].trim();

				return Integer.parseInt(str, radix);	
				
			} catch (Exception e) {
				 throw new EpsonProjectorException("Illegal response");
			}
		} else {
			throw new EpsonProjectorException("No response received");	
		}
		
	}

	protected int queryInt(String query, int timeout) throws EpsonProjectorException {
		return queryInt(query, timeout, 10);
	}

	protected int queryInt(String query) throws EpsonProjectorException {
		return queryInt(query, defaultTimeout, 10);
	}

	protected int queryHexInt(String query, int timeout) throws EpsonProjectorException {
		return queryInt(query, timeout, 16);
	}

	protected int queryHexInt(String query) throws EpsonProjectorException {
		return queryInt(query, defaultTimeout, 16);
	}

	public void connect() throws EpsonProjectorException {
		connection.connect();
		 connected = true;
	}

	public void disconnect() throws EpsonProjectorException {
		connection.disconnect();
		 connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}

	/*
	 * Power
	 */
	public PowerStatus getPowerStatus() throws EpsonProjectorException {
		int val = queryInt("PWR?");
		PowerStatus retval = PowerStatus.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to PowerStatus");
		}
	}

	public void setPower(Switch value) throws EpsonProjectorException {
		sendCommand(String.format("PWR %s", value.name()), powerStateTimeout);
	}

	/*
	 * Key code
	 */
	public void sendKeyCode(int value) throws EpsonProjectorException {
		sendCommand(String.format("KEY %02X", value));
	}

	/*
	 * Vertical Keystone
	 */
	public int getVerticalKeystone() throws EpsonProjectorException {
		return queryInt("VKEYSTONE?");
	}

	public void setVerticalKeystone(int value) throws EpsonProjectorException {
		sendCommand(String.format("VKEYSTONE %d", value));
	}

	/*
	 * Horizontal Keystone
	 */
	public int getHorizontalKeystone() throws EpsonProjectorException {
		return queryInt("HKEYSTONE?");
	}

	public void setHorizontalKeystone(int value) throws EpsonProjectorException {
		sendCommand(String.format("HKEYSTONE %d", value));
	}

	/*
	 * Auto Keystone
	 */
	public int getAutoKeystone() throws EpsonProjectorException {
		return queryInt("AUTOKEYSTONE?");
	}

	public void setAutoKeystone(int value) throws EpsonProjectorException {
		sendCommand(String.format("HKEYSTONE %d", value));
	}

	/*
	 * Aspect Ratio
	 */
	public AspectRatio getAspectRatio() throws EpsonProjectorException {
		int val = queryHexInt("ASPECT?");
		AspectRatio retval = AspectRatio.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to AspectRatio");
		}
	}

	public void setAspectRatio(AspectRatio value)
			throws EpsonProjectorException {
		sendCommand(String.format("ASPECT %02X", value.toInt()));
	}

	/*
	 * Luminance
	 */
	public Luminance getLuminance() throws EpsonProjectorException {
		int val = queryHexInt("LUMINANCE?");
		Luminance retval = Luminance.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to Luminance");
		}
	}

	public void setLuminance(Luminance value) throws EpsonProjectorException {
		sendCommand(String.format("LUMINANCE %02X", value.toInt()));
	}

	/*
	 * Source
	 */
	public Source getSource() throws EpsonProjectorException {
		int val = queryHexInt("SOURCE?");
		Source retval = Source.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to Source");
		}
	}

	public void setSource(Source value) throws EpsonProjectorException {
		sendCommand(String.format("SOURCE %02X", value.toInt()));
	}

	public int getDirectSource() throws EpsonProjectorException {
		return queryHexInt("SOURCE?");
	}

	public void setDirectSource(int value) throws EpsonProjectorException {
		sendCommand(String.format("SOURCE %02X", value));
	}

	/*
	 * Brightness
	 */
	public int getBrightness() throws EpsonProjectorException {
		return queryInt("BRIGHT?");
	}

	public void setBrightness(int value) throws EpsonProjectorException {
		sendCommand(String.format("BRIGHT %d", value));
	}

	/*
	 * Contrast
	 */
	public int getContrast() throws EpsonProjectorException {
		return queryInt("CONTRAST?");
	}

	public void setContrast(int value) throws EpsonProjectorException {
		sendCommand(String.format("CONTRAST %d", value));
	}

	/*
	 * Density
	 */
	public int getDensity() throws EpsonProjectorException {
		return queryInt("DENSITY?");
	}

	public void setDensity(int value) throws EpsonProjectorException {
		sendCommand(String.format("DENSITY %d", value));
	}

	/*
	 * Tint
	 */
	public int getTint() throws EpsonProjectorException {
		return queryInt("TINT?");
	}

	public void setTint(int value) throws EpsonProjectorException {
		sendCommand(String.format("TINT %d", value));
	}

	/*
	 * Sharpness
	 */
	public int getSharpness(Sharpness sharpness) throws EpsonProjectorException {
		return queryHexInt("SHARP? %02X", sharpness.toInt());
	}

	public void setSharpness(Sharpness sharpness, int value)
			throws EpsonProjectorException {
		sendCommand(String.format("SHARP %d %02X", value, sharpness.toInt()));
	}

	/*
	 * Color Temperature
	 */
	public int getColorTemperature() throws EpsonProjectorException {
		return queryInt("CTEMP?");
	}

	public void setColorTemperature(int value) throws EpsonProjectorException {
		sendCommand(String.format("CTEMP %d", value));
	}

	/*
	 * Flesh Color
	 */
	public int getFleshColor() throws EpsonProjectorException {
		return queryHexInt("FCOLOR?");
	}

	public void setFleshColor(int value) throws EpsonProjectorException {
		sendCommand(String.format("FCOLOR %02X", value));
	}

	/*
	 * Color Mode
	 */
	public ColorMode getColorMode() throws EpsonProjectorException {
		int val = queryHexInt("CMODE?");
		ColorMode retval = ColorMode.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to ColorMode");
		}
	}

	public void setColorMode(ColorMode value) throws EpsonProjectorException {
		sendCommand(String.format("CMODE %02X", value.toInt()));
	}

	/*
	 * Horizontal Position
	 */
	public int getHorizontalPosition() throws EpsonProjectorException {
		return queryInt("HPOS?");
	}

	public void setHorizontalPosition(int value) throws EpsonProjectorException {
		sendCommand(String.format("HPOS %d", value));
	}

	/*
	 * Vertical Position
	 */
	public int getVerticalPosition() throws EpsonProjectorException {
		return queryInt("VPOS?");
	}

	public void setVerticalPosition(int value) throws EpsonProjectorException {
		sendCommand(String.format("VPOS %d", value));
	}

	/*
	 * Tracking
	 */
	public int getTracking() throws EpsonProjectorException {
		return queryInt("TRACKIOK?");
	}

	public void setTracking(int value) throws EpsonProjectorException {
		sendCommand(String.format("TRACKIOK %d", value));
	}

	/*
	 * Sync
	 */
	public int getSync() throws EpsonProjectorException {
		return queryInt("SYNC?");
	}

	public void setSync(int value) throws EpsonProjectorException {
		sendCommand(String.format("SYNC %d", value));
	}

	/*
	 * Offset Red
	 */
	public int getOffsetRed() throws EpsonProjectorException {
		return queryInt("OFFSETR?");
	}

	public void setOffsetRed(int value) throws EpsonProjectorException {
		sendCommand(String.format("OFFSETR %d", value));
	}

	/*
	 * Offset Green
	 */
	public int getOffsetGreen() throws EpsonProjectorException {
		return queryInt("OFFSETG?");
	}

	public void setOffsetGreen(int value) throws EpsonProjectorException {
		sendCommand(String.format("OFFSETG %d", value));
	}

	/*
	 * Offset Blue
	 */
	public int getOffsetBlue() throws EpsonProjectorException {
		return queryInt("OFFSETB?");
	}

	public void setOffsetBlue(int value) throws EpsonProjectorException {
		sendCommand(String.format("OFFSETB %d", value));
	}

	/*
	 * Gain Red
	 */
	public int getGainRed() throws EpsonProjectorException {
		return queryInt("GAINR?");
	}

	public void setGainRed(int value) throws EpsonProjectorException {
		sendCommand(String.format("GAINR %d", value));
	}

	/*
	 * Gain Green
	 */
	public int getGainGreen() throws EpsonProjectorException {
		return queryInt("GAING?");
	}

	public void setGainGreen(int value) throws EpsonProjectorException {
		sendCommand(String.format("GAING %d", value));
	}

	/*
	 * Gain Blue
	 */
	public int getGainBlue() throws EpsonProjectorException {
		return queryInt("GAINB?");
	}

	public void setGainBlue(int value) throws EpsonProjectorException {
		sendCommand(String.format("GAINB %d", value));
	}

	/*
	 * Gamma
	 */
	public Gamma getGamma() throws EpsonProjectorException {
		int val = queryHexInt("GAMMA?");
		Gamma retval = Gamma.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to Gamma");
		}
	}

	public void setGamma(Gamma value) throws EpsonProjectorException {
		sendCommand(String.format("GAMMA %02X", value.toInt()));
	}

	/*
	 * Gamma Step
	 */
	public int getGammaStep(GammaStep step) throws EpsonProjectorException {
		return queryHexInt(String.format("GAMMALV? %02X", step.toInt()));
	}

	public void setGammaStep(GammaStep step, int value)
			throws EpsonProjectorException {
		sendCommand(String.format("GAMMALV %02X %d", step.toInt(), value));
	}

	/*
	 * Memory
	 */
	public void LoadMemory(int number) throws EpsonProjectorException {
		sendCommand(String.format("POPMEM 02 %02X", number));
	}

	public void SaveMemory(int number) throws EpsonProjectorException {
		sendCommand(String.format("PUSHMEM 02 %02X", number));
	}

	public void EraseAllMemory() throws EpsonProjectorException {
		sendCommand("ERASEMEM 00");
	}

	public void EraseMemory(int number) throws EpsonProjectorException {
		sendCommand(String.format("ERASEMEM 02 %02X", number));
	}

	/*
	 * Color
	 */
	public Color getColor() throws EpsonProjectorException {
		int val = queryHexInt("CSEL?");
		Color retval = Color.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to Color");
		}
	}

	public void setColor(Color value) throws EpsonProjectorException {
		sendCommand(String.format("CSEL %02X", value.toInt()));
	}

	/*
	 * Mute
	 */
	public Switch getMute() throws EpsonProjectorException {
		int val = queryInt("MUTE?");
		try {
			return Switch.values()[val];
		} catch (Exception e) {
			throw new EpsonProjectorException("Can't convert value" + val + " to Switch");
		}
	}

	public void setMute(Switch value) throws EpsonProjectorException {
		sendCommand(String.format("MUTE %s", value.name()), defaultTimeout);
	}

	/*
	 * Horizontal Reverse
	 */
	public Switch getHorizontalReverse() throws EpsonProjectorException {
		int val = queryInt("HREVERSE?");
		try {
			return Switch.values()[val];
		} catch (Exception e) {
			throw new EpsonProjectorException("Can't convert value" + val + " to Switch");
		}
	}

	public void setHorizontalReverse(Switch value)
			throws EpsonProjectorException {
		sendCommand(String.format("HREVERSE %s", value.name()));
	}

	/*
	 * Vertical Reverse
	 */
	public Switch getVerticalReverse() throws EpsonProjectorException {
		int val = queryInt("VREVERSE?");
		try {
			return Switch.values()[val];
		} catch (Exception e) {
			throw new EpsonProjectorException("Can't convert value" + val + " to Switch");
		}
	}

	public void setVerticalReverse(Switch value) throws EpsonProjectorException {
		sendCommand(String.format("VREVERSE %s", value.name()));
	}

	/*
	 * Background
	 */
	public Background getBackground() throws EpsonProjectorException {
		int val = queryHexInt("MSEL?");
		Background retval = Background.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to Background");
		}
	}

	public void setBackground(Background value) throws EpsonProjectorException {
		sendCommand(String.format("MSEL %02X", value.toInt()));
	}

	/*
	 * Reset all
	 */
	public void ResetAll() throws EpsonProjectorException {
		sendCommand("INITALL");
	}

	/*
	 * Speed
	 */
	public CommunicationSpeed getCommunicationSpeed()
			throws EpsonProjectorException {
		int val = queryInt("SPEED?");
		CommunicationSpeed retval = CommunicationSpeed.forValue(val);
		if (retval != null) {
			return retval;
		} else {
			throw new EpsonProjectorException("Can't convert value" + val + " to CommunicationSpeed");
		}
	}

	public void setCommunicationSpeed(CommunicationSpeed value)
			throws EpsonProjectorException {
		sendCommand(String.format("SPEED %s", value.toInt()));
	}

	/*
	 * Lamp Time
	 */
	public int getLampTime() throws EpsonProjectorException {
		return queryInt("LAMP?");
	}

	/*
	 * Error
	 */
	public int getError() throws EpsonProjectorException {
		return queryHexInt("ERR?");
	}

	/*
	 * Error
	 */
	public String getErrorString() throws EpsonProjectorException {
		String errString = null;

		int err = queryInt("ERR?");

		switch (err) {
		case 0:
			errString = Messages.EpsonProjectorBinding_NO_ERROR;
			break;
		case 1:
			errString = Messages.EpsonProjectorBinding_ERROR1;
			break;
		case 3:
			errString = Messages.EpsonProjectorBinding_ERROR3;
			break;
		case 4:
			errString = Messages.EpsonProjectorBinding_ERROR4;
			break;
		case 6:
			errString = Messages.EpsonProjectorBinding_ERROR6;
			break;
		case 7:
			errString = Messages.EpsonProjectorBinding_ERROR7;
			break;
		case 8:
			errString = Messages.EpsonProjectorBinding_ERROR8;
			break;
		case 9:
			errString = Messages.EpsonProjectorBinding_ERROR9;
			break;
		case 10:
			errString = Messages.EpsonProjectorBinding_ERROR10;
			break;
		case 11:
			errString = Messages.EpsonProjectorBinding_ERROR11;;
			break;
		case 12:
			errString = Messages.EpsonProjectorBinding_ERROR12;
			break;
		case 13:
			errString = Messages.EpsonProjectorBinding_ERROR13;
			break;
		case 14:
			errString = Messages.EpsonProjectorBinding_ERROR14;
			break;
		case 15:
			errString = Messages.EpsonProjectorBinding_ERROR15;
			break;
		case 16:
			errString = Messages.EpsonProjectorBinding_ERROR16;
			break;
		case 17:
			errString = Messages.EpsonProjectorBinding_ERROR17;
			break;
		case 18:
			errString = Messages.EpsonProjectorBinding_ERROR18;
			break;
		case 19:
			errString = Messages.EpsonProjectorBinding_ERROR19;
			break;
		case 20:
			errString = Messages.EpsonProjectorBinding_ERROR20;
			break;
		case 21:
			errString = Messages.EpsonProjectorBinding_ERROR21;
			break;
		case 22:
			errString = Messages.EpsonProjectorBinding_ERROR22;
			break;
		default:
			errString = String.format(Messages.EpsonProjectorBinding_UNKNOWN_ERROR + " %d", err);
		}

		return errString;
	}

}
