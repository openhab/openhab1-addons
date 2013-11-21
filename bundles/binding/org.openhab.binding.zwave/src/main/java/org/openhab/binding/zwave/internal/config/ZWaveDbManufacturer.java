package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbManufacturer {
	@XStreamConverter(HexToIntegerConverter.class)
	Integer Id;

	String  Name;

	@XStreamImplicit
	public List<ZWaveDbProduct> Product;
}
