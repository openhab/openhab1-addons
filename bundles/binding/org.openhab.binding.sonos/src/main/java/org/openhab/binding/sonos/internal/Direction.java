/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

/**
 * Each binding configuration can define a direction. This can be used to qualify, limit, control, ... 
 * whatever is received/sent from the binding
 * 
 * IN : for data/updates coming *from* the binding
 * OUT: for data/commands to be sent *to* the binding
 * BIDIRECTIONAL : for data/commands that be sent or be updated at the same time
 * 
 * @author Karel Goderis
 * @since 1.1.0
 */

public enum Direction {
    IN,OUT,BIDIRECTIONAL
}
