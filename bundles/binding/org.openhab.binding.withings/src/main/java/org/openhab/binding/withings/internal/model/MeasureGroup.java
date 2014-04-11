package org.openhab.binding.withings.internal.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MeasureGroup {
	
	@SerializedName("grpid")
	public int groupId;
	
	@SerializedName("attrib")
	public AttributionStatus attributionStatus;
	
	@SerializedName("date")
	public int date;
	
	@SerializedName("category")
	public Category category;
	
	@SerializedName("measures")
	public List<Measure> measures;

}
