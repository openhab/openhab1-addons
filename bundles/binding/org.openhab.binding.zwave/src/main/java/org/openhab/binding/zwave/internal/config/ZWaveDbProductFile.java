package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ZWaveDbProductFile {
	public String Model;
	public Integer Endpoints;
	@XStreamImplicit
	public List<ZWaveDbLabel> Label;

	public ZWaveDbConfiguration Configuration;
	public ZWaveDbAssociation Associations;

	List<ZWaveDbConfigurationParameter> getConfiguration() {
		return Configuration.Parameter;
	}
	
	List<ZWaveDbAssociationGroup> getAssociations() {
		return Associations.Group;
	}
	
	class ZWaveDbConfiguration {
		@XStreamImplicit
		public List<ZWaveDbConfigurationParameter> Parameter;		
	}

	class ZWaveDbAssociation {
		@XStreamImplicit
		List<ZWaveDbAssociationGroup> Group;
	}
}
	
