package org.openhab.binding.zwave.internal.config;

import java.util.List;

import org.openhab.binding.zwave.internal.protocol.initialization.HexToIntegerConverter;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbProduct {
	@XStreamImplicit
	public List<ZWaveDbProductReference> Reference;

	public String Model;

	@XStreamImplicit
	public List<ZWaveDbLabel> Label;

	public String ConfigFile;
}
