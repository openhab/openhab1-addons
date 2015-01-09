package org.openhab.binding.mpower.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerBindingConfig implements BindingConfig {
	private String mPowerInstance;
	private HashMap<Integer, MpowerSocket> sockets = new HashMap<Integer, MpowerSocket>();

	public MpowerBindingConfig(String id) {
		setmPowerInstance(id);
		// prepare max 9 sockets (for US mPower pro)
		for (int i = 1; i < 10; i++) {
			sockets.put(i, new MpowerSocket());
		}
	}

	public String getmPowerInstance() {
		return mPowerInstance;
	}

	public void setmPowerInstance(String mPowerInstance) {
		this.mPowerInstance = mPowerInstance;
	}

	public void setVoltageItemName(int socket, String name) {
		sockets.get(socket).setVoltageItemName(name);
	}

	public void setPowerItemName(int socket, String name) {
		sockets.get(socket).setPowerItemName(name);
	}

	public void setSwitchItemName(int socket, String name) {
		sockets.get(socket).setSwitchItemName(name);
	}

	public String getSwitchItemName(int socket) {
		return sockets.get(socket).getSwitchItemName();
	}

	public String getVoltageItemName(int socket) {
		return sockets.get(socket).getVoltageItemName();
	}

	public String getEnergyItemName(int socket) {
		return sockets.get(socket).getEnergyItemName();
	}

	public void setEnergyItemName(int socket, String name) {
		sockets.get(socket).setEnergyItemName(name);
	}

	public void setEnergyTodayItemName(int socket, String name) {
		sockets.get(socket).setEnergyTodayItemName(name);
	}

	public String getEnergyTodayItemName(int socket) {
		return sockets.get(socket).getEnergyTodayItemName();
	}

	public String getPowerItemName(int socket) {
		return sockets.get(socket).getPowerItemName();
	}

	public MpowerSocketState getCachedState(int socket) {
		return sockets.get(socket).getValueCache();
	}

	public void setCachedState(int socket, MpowerSocketState state) {
		sockets.get(socket).setLastUpdated(System.currentTimeMillis());
		sockets.get(socket).setValueCache(state);
		sockets.get(socket).updateTotalConsumptionAtMidnight(state.getEnergy());
	}

	/**
	 * looks through all sockets and returns the number for a given switch item
	 * name
	 * 
	 * @param itemName
	 * @return
	 */
	public int findSocketForItemName(String itemName) {
		for (Map.Entry<Integer, MpowerSocket> entry : sockets.entrySet()) {
			if (entry.getValue().getSwitchItemName().equals(itemName)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	/**
	 * Checks if the given switch item name is part of this strip
	 * 
	 * @param itemName
	 * @return
	 */
	public boolean containsItemName(String itemName) {
		for (Map.Entry<Integer, MpowerSocket> entry : sockets.entrySet()) {
			MpowerSocket socket = entry.getValue();
			if (socket.getSwitchItemName() != null
					&& socket.getSwitchItemName().equals(itemName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the cached (last) state of the socket
	 * 
	 * @param socketNumber
	 * @return
	 */
	public MpowerSocketState getCacheForSocket(int socketNumber) {
		return sockets.get(socketNumber).getValueCache();
	}

	public long getConsumptionAtMidnight(int socketNumber) {
		return sockets.get(socketNumber).getTotalConsumptionAtMidnight();
	}

	public boolean needsUpdate(int socketNumber, long refreshTime) {
		Long lastUpdated = sockets.get(socketNumber).getLastUpdated();
		Long currentTime = System.currentTimeMillis();
		return currentTime - refreshTime > lastUpdated;
	}
}
