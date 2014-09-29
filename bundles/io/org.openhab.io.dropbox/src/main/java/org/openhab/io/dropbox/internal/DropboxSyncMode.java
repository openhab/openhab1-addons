/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

/**
 * Enumerates all valid synchronization Modes
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public enum DropboxSyncMode {
	
	DROPBOX_TO_LOCAL,
	LOCAL_TO_DROPBOX,
	BIDIRECTIONAL;
	
}
