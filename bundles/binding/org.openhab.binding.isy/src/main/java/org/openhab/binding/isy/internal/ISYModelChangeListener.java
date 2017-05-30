/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDNode;

/**
 * @author Tim Diekmann
 * @since 1.10.0
 */
public interface ISYModelChangeListener {
    public void onModelChanged(final UDControl control, final Object action, final UDNode node);

}
