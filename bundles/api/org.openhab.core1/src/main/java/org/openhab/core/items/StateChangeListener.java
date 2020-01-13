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
package org.openhab.core.items;

import org.openhab.core.types.State;

/**
 * <p>
 * This interface must be implemented by all classes that want to be notified
 * about changes in the state of an item.
 * </p>
 * <p>
 * The {@link GenericItem} class provides the possibility to register such
 * listeners.
 * </p>
 *
 * @author Kai Kreuzer
 *
 */
public interface StateChangeListener {

    /**
     * This method is called, if a state has changed.
     *
     * @param item the item whose state has changed
     * @param oldState the previous state
     * @param newState the new state
     */
    public void stateChanged(Item item, State oldState, State newState);

    /**
     * This method is called, if a state was updated, but has not changed
     *
     * @param item the item whose state was updated
     * @param state the current state, same before and after the update
     */
    public void stateUpdated(Item item, State state);

}
