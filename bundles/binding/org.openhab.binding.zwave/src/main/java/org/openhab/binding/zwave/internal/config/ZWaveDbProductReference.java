package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamConverter;

public class ZWaveDbProductReference {
	@XStreamConverter(HexToIntegerConverter.class)
	public Integer Type;
	@XStreamConverter(HexToIntegerConverter.class)
	public Integer Id;
}
