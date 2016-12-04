package org.openhab.binding.plclogo;



import org.openhab.binding.plclogo.internal.PLCLogoBinding;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.binding.plclogo.internal.PLCLogoMemoryConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PLCLogoBindingConfig implements BindingConfig {
	private final String itemName;
	private final String controllerName;
	private final PLCLogoMemoryConfig rdMem;
	private final PLCLogoMemoryConfig wrMem;
	private boolean invert;
	private int analogDelta;
	private int lastvalue;
	private Item itemType;

	private static final Logger logger =
			LoggerFactory.getLogger(PLCLogoBinding.class);


	public PLCLogoBindingConfig(String itemName, Item itemType, String configString) 
			throws BindingConfigParseException 
	{
		this.itemName = itemName;
		this.itemType = itemType;

		// the config string has the format
		//
		//  instancename:memloc.bit [activelow:yes|no]
		//
		String shouldBe = "should be controllername:memloc[.bit] [activelow:yes|no]";
		String[] segments = configString.split(" ");
		if (segments.length > 2)
			throw new BindingConfigParseException("invalid item format: " + configString + ", " + shouldBe);
		String[] dev = segments[0].split(":");
		if (dev.length < 2) 
			throw new BindingConfigParseException("invalid item name/memory format: " + configString + ", " + shouldBe);
		
		controllerName = dev[0];
		rdMem = new PLCLogoMemoryConfig(dev[1]);
		if (dev.length == 3)
			wrMem = new PLCLogoMemoryConfig(dev[2]);
		else
			wrMem = rdMem;

		// check for invert or analogdelta
		if (segments.length == 2) {
			logger.debug("Addtional binding config " + segments[1]);
			String[] parts = segments[1].split("=");
			if (parts.length != 2)
				throw new BindingConfigParseException("invalid second parameter: " + configString + ", " + shouldBe);

			if (parts[0].equalsIgnoreCase("activelow")) {
				invert = parts[1].equalsIgnoreCase("yes");
			}
			if (parts[0].equalsIgnoreCase("analogdelta")) {
				analogDelta = Integer.parseInt(parts[1]);
				logger.debug("Setting analogDelta " + analogDelta);
			}
		}
		
		this.lastvalue = 0;
	}

	public String getItemName()
	{
		return itemName;
	}

	public String getcontrollerName()
	{
		return controllerName;
	}

	public PLCLogoMemoryConfig getRD()
	{
		return rdMem;
	}

	public PLCLogoMemoryConfig getWR()
	{
		return wrMem;
	}

	public int getAnalogDelta()
	{
		return this.analogDelta;
	}

	public boolean getInvert()
	{
		return this.invert;
	}

	public int getLastValue()
	{
		return this.lastvalue;
	}

	public void setLastValue(int lastvalue)
	{
		this.lastvalue = lastvalue;
	}

	public Item getItemType(){
		return this.itemType;
	}
}
