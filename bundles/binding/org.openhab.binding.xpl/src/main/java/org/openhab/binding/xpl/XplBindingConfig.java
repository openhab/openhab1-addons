package org.openhab.binding.xpl;

import org.cdp1802.xpl.xPL_MutableMessageI;
import org.openhab.core.binding.BindingConfig;

/**
 * This is a class that stores xPL binding configuration elements :
 *	- an interface to the message template to match toward incoming messages
 * 	- the name of the body key that will be returned if matched
 * 
 * @author clinique
 * @since 1.6.0
 */
public class XplBindingConfig implements BindingConfig {
	public xPL_MutableMessageI Message;
	public String NamedParameter;
}
