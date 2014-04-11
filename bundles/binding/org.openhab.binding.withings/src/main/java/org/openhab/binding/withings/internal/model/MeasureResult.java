package org.openhab.binding.withings.internal.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MeasureResult {
	
	public int updatetime;
	
	@SerializedName("measuregrps")
	public List<MeasureGroup> measureGroups;

}
