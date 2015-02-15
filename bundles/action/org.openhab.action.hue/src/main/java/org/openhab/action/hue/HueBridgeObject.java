package org.openhab.action.hue;

/**
 * An Object existing on a hue Bridge. 
 * @author Gernot Eger
 *
 */
public interface HueBridgeObject {
	
	/**
	 * get the json representation of the object
	 * @return
	 */
	public String json();
}
