package org.openhab.binding.withings.internal.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MeasureGroup {

	@SerializedName("attrib")
	public Attribute attribute;

	public Category category;

	public int date;

	@SerializedName("grpid")
	public int groupId;

	public List<Measure> measures;

}
