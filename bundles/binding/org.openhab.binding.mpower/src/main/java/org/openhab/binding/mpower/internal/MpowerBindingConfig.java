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
		// prepare 9 sockets (for US mPower pro)
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

	public String getVoltageItemName(int socket) {
		return sockets.get(socket).getVoltageItemName();
	}

	public String getPowerItemName(int socket) {
		return sockets.get(socket).getPowerItemName();
	}

	public int findSocketForItemName(String itemName) {
		for (Map.Entry<Integer, MpowerSocket> entry : sockets.entrySet()) {
			if (entry.getValue().getSwitchItemName().equals(itemName)) {
				return entry.getKey();
			}
		}
		return 0;
	}

	public boolean containsItemName(String itemName) {
		for (Map.Entry<Integer, MpowerSocket> entry : sockets.entrySet()) {
			MpowerSocket socket = entry.getValue();
			if (socket.getSwitchItemName().equals(itemName)) {
				return true;
			}
		}
		return false;
	}
}
