package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbConfigurationListItem {
	public Integer Value;
	@XStreamImplicit
	public List<ZWaveDbLabel> Label;
}
