package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value=ToAttributedValueConverter.class, strings={"Label"})
public class ZWaveDbLabel {
	@XStreamAlias("lang")
	String Language;
	
	String Label;
}
