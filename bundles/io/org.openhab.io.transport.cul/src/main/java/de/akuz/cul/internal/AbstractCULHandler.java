package de.akuz.cul.internal;

import java.util.ArrayList;
import java.util.List;

import de.akuz.cul.CULHandler;
import de.akuz.cul.CULListener;
import de.akuz.cul.CULMode;

public abstract class AbstractCULHandler implements CULHandler,
		CULHandlerInternal {

	protected String deviceName;
	protected CULMode mode;

	protected List<CULListener> listeners = new ArrayList<CULListener>();

	protected AbstractCULHandler(String deviceName, CULMode mode) {
		this.mode = mode;
		this.deviceName = deviceName;
	}

	@Override
	public CULMode getCULMode() {
		return mode;
	}

	@Override
	public void registerListener(CULListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void unregisterListener(CULListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public boolean hasListeners() {
		return listeners.size() > 0;
	}

	protected boolean isMessageAllowed(String message) {
		if (message.startsWith("X") || message.startsWith("x")) {
			return false;
		}
		if (message.startsWith("Ar")) {
			return false;
		}
		return true;
	}
}
