package org.openhab.binding.withings.internal.model;

/**
 * Java object for response of Withings API.
 * 
 * @see http://www.withings.com/de/api#bodymetrics
 * @author Dennis Nobel
 * @since 1.5.0
 */
public enum Attribute {

	AMBIGUOUS(1), MANUAL(2), NOT_AMBIGUOUS(0), USER_CREATION(4);

	public static Attribute getForType(int type) {
		Attribute[] attributes = values();
		for (Attribute attribute : attributes) {
			if (attribute.type == type) {
				return attribute;
			}
		}
		return null;
	}

	public String description;

	public int type;

	private Attribute(int type) {
		this.type = type;
	}

}
