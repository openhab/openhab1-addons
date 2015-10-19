/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy;

import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDNode;

/**
 * @author Tim Diekmann
 * 
 */
public interface ISYModelChangeListener {
	public void onModelChanged(final UDControl control, final Object action,
			final UDNode node);

}
