/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class AudioZone {

	private ZoneAddress mAddress;
	private ZonePower mPower = ZonePower.Unknown;
	private int mVolume = 0;
	private int mSource = 0;

	private List<ZoneListener> mListeners = new ArrayList<ZoneListener>();

	public enum ZonePower {
		On, Off, Unknown
	}

	public AudioZone(ZoneAddress address) {
		mAddress = address;
	}

	public ZoneAddress getZoneAddress() {
		return mAddress;
	}

	public void setPower(ZonePower power) {
		// if (this.mPower == null || this.mPower != power) {
		PropertyChangeEvent pcEvent = new PropertyChangeEvent(this, "power",
				this.mPower, power);
		mPower = power;
		notifyListener(pcEvent);
		// }
	}

	public void setVolume(int volume) {
		// if (this.mVolume != volume) {
		PropertyChangeEvent pcEvent = new PropertyChangeEvent(this, "volume",
				this.mVolume, volume);
		mVolume = volume;
		notifyListener(pcEvent);
		// }
	}

	public void setSource(int source) {
		// if (this.mSource != source) {
		PropertyChangeEvent pcEvent = new PropertyChangeEvent(this, "source",
				this.mSource, source);
		mSource = source;
		notifyListener(pcEvent);
		// }
	}

	public void addListener(ZoneListener listener) {
		mListeners.add(listener);
	}

	private void notifyListener(PropertyChangeEvent ev) {
		for (ZoneListener listener : mListeners) {
			listener.onPropertyChange(ev, mAddress);
		}

	}

}
