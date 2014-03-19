/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity;


/**
 * @author	Alexander Betker
 * @since 1.3.0
 */
public interface DeviceSceneSpec {
	
	public Scene getScene();
	
	public boolean isDontCare();
	public void setDontcare(boolean dontcare);
	
	public boolean isLocalPrio();
	public void setLocalPrio(boolean localPrio);
	
	public boolean isSpecialMode();
	public void setSpecialMode(boolean specialMode);
	
	public boolean isFlashMode();
	public void setFlashMode(boolean flashMode);
	
}
