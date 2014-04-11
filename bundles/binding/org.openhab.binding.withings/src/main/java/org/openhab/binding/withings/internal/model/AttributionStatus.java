package org.openhab.binding.withings.internal.model;

public enum AttributionStatus
{
	NOT_AMBIGUOUS(0),
	AMBIGUOUS(1),
	MANUAL(2),
	USER_CREATION(4);

	public String description;
	public int value;

	private AttributionStatus(int value) {
		this.value = value;
	}



	public static AttributionStatus valueOf(int ordinal)
	{
		AttributionStatus result = null;
		switch(ordinal)
		{
			case 0:
				result = NOT_AMBIGUOUS;
				break;
			case 1:
				result = AMBIGUOUS;
				break;
			case 2:
				result = MANUAL;
				break;
			case 4:
				result = USER_CREATION;
				break;
		}
		return result;
	}
}
