package org.openhab.core.items;

/**
 * This is an abstract parent exception to be extended by any exceptions
 * related to item lookups in the item registry.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public abstract class ItemLookupException extends Exception {
	
	public ItemLookupException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -4617708589675048859L;

}
