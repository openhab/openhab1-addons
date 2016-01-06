package org.openhab.binding.ipx800.internal.command;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
/**
 * Type of a ipx800 port
 * @author Seebag
 * @since 1.8.0
 *
 */
public enum Ipx800PortType {
	OUPUT("O", 8, 32, SwitchItem.class),
	INPUT("I", 8, 32, SwitchItem.class),
	COUNTER("C", 8, 8, NumberItem.class),
	ANALOG("A", 4, 16, NumberItem.class);
	private String prefix;
	private int maxSlots;
	private int portPerDevice;
	
	Ipx800PortType(String prefix, int portPerDevice, int maxSlots, Class<? extends Item> itemClass) {
		this.prefix = prefix;
		this.portPerDevice = portPerDevice;
		this.maxSlots = maxSlots;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void assertSlotCompatible(int slotNumber) {
		if (slotNumber > maxSlots || slotNumber <= 0) {
			//throw new Exception("slotNumber is incompatible with this commandType");
		}
	}
	
	public static Ipx800PortType getSlotByPrefix(String prefix) {
		for (Ipx800PortType slot : Ipx800PortType.values()) {
			if (slot.prefix.equals(prefix)) {
				return slot;
			}
		}// Throw unknown slot
		return null;
	}
	
	public int getPortPerDevice() {
		return portPerDevice;
	}
	
	public int getMaxSlots() {
		return maxSlots;
	}
}