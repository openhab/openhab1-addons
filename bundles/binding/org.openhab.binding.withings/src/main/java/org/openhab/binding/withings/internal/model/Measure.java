package org.openhab.binding.withings.internal.model;

public class Measure
{

	public int value;
	public int type;
	public int unit;

	public Measure() {};

	public Measure(int value, int type, int unit)
	{
		this.value = value;
		this.type = type;
		this.unit = unit;
	}

}
