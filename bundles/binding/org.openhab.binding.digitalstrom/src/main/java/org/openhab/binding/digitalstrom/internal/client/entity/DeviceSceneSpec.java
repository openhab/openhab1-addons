/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.digitalstrom.internal.client.entity;

/**
 * @author Alexander Betker
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
