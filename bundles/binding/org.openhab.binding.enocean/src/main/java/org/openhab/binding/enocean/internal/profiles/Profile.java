/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.profiles;

import org.opencean.core.common.ParameterValueChangeListener;
import org.openhab.core.items.Item;

/**
 * A profile creates a certain set of commands and states. It is activated for
 * specific device and type combinations. Currently we have a
 * RollershutterProfile for a RockerSwitch to control a rollershutter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public interface Profile extends ParameterValueChangeListener {

    void addItem(Item item);

    void removeItem(Item item);

}