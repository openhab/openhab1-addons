package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ZWaveConfigCommandClass {
	@XStreamAsAttribute
	int id;
	
	@XStreamImplicit
	List<ZWaveConfigValue> Value;
	
//	@XStreamOmitField
	@XStreamImplicit
	ZWaveConfigAssociation Associations;
}
