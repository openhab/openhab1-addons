package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbAssociationGroup {
		Integer Index;
		Integer Maximum;
		boolean Autoset;
		@XStreamImplicit
		public List<ZWaveDbLabel> Label;
}
