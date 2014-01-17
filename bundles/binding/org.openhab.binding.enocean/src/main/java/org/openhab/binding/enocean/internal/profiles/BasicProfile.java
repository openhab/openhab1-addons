/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.profiles;

import java.util.HashSet;
import java.util.Set;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Some basic functionality for profiles. E.g. a profile has an event bus and
 * can have multiple items.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public abstract class BasicProfile implements Profile {

    protected Set<Item> items = new HashSet<Item>();
    protected EventPublisher eventPublisher;

    public BasicProfile(Item item, EventPublisher eventPublisher) {
        items.add(item);
        this.eventPublisher = eventPublisher;
    }

    protected void postCommand(Command command) {
        if (command != null) {
            for (Item item : items) {
                eventPublisher.postCommand(item.getName(), command);
            }
        }
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public void removeItem(Item item) {
        items.remove(item);
    }

}