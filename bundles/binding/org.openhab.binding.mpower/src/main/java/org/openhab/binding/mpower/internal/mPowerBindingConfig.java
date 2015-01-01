package org.openhab.binding.mpower.internal;

import java.util.HashMap;

import org.openhab.core.binding.BindingConfig;

public class mPowerBindingConfig implements BindingConfig {
	private String mPowerInstance;
	private HashMap<Integer, mPowerSocket> sockets = new HashMap<Integer, mPowerSocket>();

	public mPowerBindingConfig(String id) {
		setmPowerInstance(id);
		// prepare 9 sockets (for US mPower pro)
		for (int i = 1; i < 10; i++) {
			sockets.put(i, new mPowerSocket());
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
}
