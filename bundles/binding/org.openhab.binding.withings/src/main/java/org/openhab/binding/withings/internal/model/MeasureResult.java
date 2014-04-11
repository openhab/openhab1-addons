package org.openhab.binding.withings.internal.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MeasureResult {

	@SerializedName("measuregrps")
	public List<MeasureGroup> measureGroups;

	public int updatetime;

}
