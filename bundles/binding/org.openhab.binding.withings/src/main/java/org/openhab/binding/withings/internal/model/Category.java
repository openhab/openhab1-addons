package org.openhab.binding.withings.internal.model;

public enum Category
{
	MEASURE("Measure", 1),
	TARGET("Target", 2);

	public String description;
	public int value;


	private Category(String description, int value)
	{
		this.description = description;
		this.value = value;
	}

	public static Category valueOf(int ordinal)
	{
		Category result = null;
		switch (ordinal)
		{
			case 1:
				result = MEASURE;
				break;
			case 2:
				result = TARGET;
				break;
		}
		return result;
	}
}
