/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.octoller;

import org.openhab.core.binding.BindingProvider;
import org.openhab.binding.octoller.internal.*;

/**
 * @author JPlenert
 * @since 1.8.0
 */
public interface OctollerBindingProvider extends BindingProvider {
	public OctollerBindingConfig getConfig(String itemName);

}
