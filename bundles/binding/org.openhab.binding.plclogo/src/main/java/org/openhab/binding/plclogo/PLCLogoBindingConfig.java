package org.openhab.binding.plclogo;



import org.openhab.binding.plclogo.internal.PLCLogoMemMap;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;


public class PLCLogoBindingConfig implements BindingConfig {
	private final String itemName;
	private final String controllerName;
	private final String memloc;
	private final int bit;
	private final boolean invert;
	private final int analogDelta;
	private int realmemloc;
	private int lastvalue;
	private Item itemType;

	
	public PLCLogoBindingConfig(String itemName, Item itemType, String controllerName, String memory, String bit,  boolean invert, int analogDelta){
	
	if (bit.equals("")){ bit = "-1";}
	this.itemName = itemName;
	this.itemType = itemType;
	this.controllerName = controllerName;
	this.memloc = memory;
	this.analogDelta = analogDelta;
	PLCLogoMemMap logoBitMemory = new PLCLogoMemMap();
	int[] bitMemory = logoBitMemory.convertToReal(memloc);
	this.realmemloc = bitMemory[1];
	if (bitMemory[0] != -1)
		this.bit = bitMemory[0];
	else
		this.bit = Integer.parseInt(bit);
	this.invert = invert;
	this.lastvalue = 0;


		
	}
	public void setLastvalue(int lastvalue){
		
		this.lastvalue = lastvalue;
	}
	public String getitemName (){
		
		return itemName;
	}
	public String getcontrollerName (){
		
		return controllerName;
	}
	public String getMemloc (){
		
		return this.memloc;
	}
	public int getRealMemloc (){
		return this.realmemloc;
	}
	public int getBit (){
		
		return this.bit;
	}
	
	public int getAnalogDelta(){
		return this.analogDelta;
	}
	public boolean getInvert (){
		
		return this.invert;
	}
	public int getLastvalue (){
		return this.lastvalue;
	}
	public Item getItemType(){
		return this.itemType;
	}
}
