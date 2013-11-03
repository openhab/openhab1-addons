package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveConfigManufacturerList {
	@XStreamImplicit
	List<ZWaveConfigManufacturer> Manufacturer;
}
