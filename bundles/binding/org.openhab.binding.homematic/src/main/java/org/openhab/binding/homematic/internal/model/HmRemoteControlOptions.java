package org.openhab.binding.homematic.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Object that holds options for a Homematic remote control display.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HmRemoteControlOptions {
	private String text;
	private int beep = 0;
	private int backlight = 0;
	private int unit = 0;
	private List<String> symbols = new ArrayList<String>();

	/**
	 * Returns the text to send to the display.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text to send to the display.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Sets the beep config.
	 */
	public void setBeep(int beep) {
		this.beep = beep;
	}

	/**
	 * Returns the beep config.
	 */
	public int getBeep() {
		return beep;
	}

	/**
	 * Sets the backlight config.
	 */
	public void setBacklight(int backlight) {
		this.backlight = backlight;
	}

	/**
	 * Returns the backlight config.
	 */
	public int getBacklight() {
		return backlight;
	}

	/**
	 * Sets the unit config.
	 */
	public void setUnit(int unit) {
		this.unit = unit;
	}

	/**
	 * Returns the unit config.
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * Returns the symbols to show on the display.
	 */
	public List<String> getSymbols() {
		return symbols;
	}

	/**
	 * Adds a symbol to show on the display.
	 */
	public void addSymbol(String symbol) {
		symbols.add(symbol);
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("text", text).append("beep", beep).append("backlight", backlight).append("unit", unit)
				.append("symbols", symbols);
		return tsb.toString();
	}
}
