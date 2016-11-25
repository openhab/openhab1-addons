/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.harmonyhub;

/**
 * A {@link HarmonyHubDiscoveryListener} listeners for discovered hubs
 *
 * @author Dan Cunningham - Initial contribution
 *
 */
public interface HarmonyHubDiscoveryListener {

    /**
     * The discovery process has finished
     */
    public void hubDiscoveryFinished();

    /**
     * The discovery process has discovered a hub
     *
     * @param result
     */
    public void hubDiscovered(HarmonyHubDiscoveryResult result);
}