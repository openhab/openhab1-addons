package org.openhab.binding.withings.internal.model;

/**
 * Java object for response of Withings API.
 * 
 * @see http://www.withings.com/de/api#bodymetrics
 * @author Dennis Nobel
 * @since 1.5.0
 */
public enum Category {
	MEASURE("Measure", 1), TARGET("Target", 2);

	public static Category getForType(int type) {
		Category[] categories = values();
		for (Category category : categories) {
			if (category.type == type) {
				return category;
			}
		}
		return null;
	}

	public String description;

	private int type;

	private Category(String description, int type) {
		this.description = description;
		this.type = type;
	}
}
