package org.openhab.binding.myq.internal;
import org.openhab.core.binding.BindingConfig;
/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author scooter_seh
 * @since 1.8.0
 */
public class myqBindingConfig implements BindingConfig 
{
	// put member fields here which holds the parsed values
	public enum ITEMTYPE {	Switch, StringStatus, ContactStatus
	};

	ITEMTYPE type;
	String id;
	String MyQName;		
}