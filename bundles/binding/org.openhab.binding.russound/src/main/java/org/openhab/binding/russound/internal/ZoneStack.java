/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.util.ArrayList;
import java.util.List;

public class ZoneStack {

	List<ZoneAddress> mAddresses = new ArrayList<ZoneAddress>();

	public ZoneAddress popAndPutEnd() {
		ZoneAddress returnValue = mAddresses.get(0);
		mAddresses.remove(0);
		mAddresses.add(mAddresses.size(), returnValue);
		return returnValue;
	}

	public void push(ZoneAddress addy) {
		mAddresses.remove(addy);
		mAddresses.add(0, addy);
	}
	public int size()
	{
		return mAddresses.size();
	}
}
