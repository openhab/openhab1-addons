/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal.busparser;

import org.openhab.binding.russound.internal.AudioZone;
import org.openhab.binding.russound.internal.ZoneAddress;

/**
 * Represents classes which watch for specific pattern of bytes.
 * 
 * @author craigh
 * 
 */
public interface BusParser {
	public ZoneAddress matches(byte[] bytes);

	public void parse(byte[] bytes, AudioZone zone);

}
