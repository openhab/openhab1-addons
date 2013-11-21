package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbConfigurationParameter {
	public Integer Index;
	public String Type;
	public String Default;
	public Integer Size;
	public Integer Minimum;
	public Integer Maximum;
	@XStreamImplicit
	public List<ZWaveDbLabel> Label;
	@XStreamImplicit
	public List<ZWaveDbLabel> Help;
	@XStreamImplicit
	public List<ZWaveDbConfigurationListItem> Item;
}

