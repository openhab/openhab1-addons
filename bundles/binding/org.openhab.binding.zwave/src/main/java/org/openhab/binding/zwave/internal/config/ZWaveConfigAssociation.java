package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveConfigAssociation {
	@XStreamAsAttribute
	int num_groups;

	@XStreamImplicit
	List<ZWaveConfigGroup> Group;
}
