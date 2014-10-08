/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera;

import org.fourthline.cling.model.types.UnsignedIntegerOneByte;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * An enumeration of supported Vera service variables 
 * that can be bound to {@link Item}s.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public enum VeraBindingVariable {

    Armed               (SwitchItem.class,   OnOffType.class,      Boolean.class,                "Armed", "SetArmed", "newArmedValue"),
    BatteryLevel        (NumberItem.class,   PercentType.class,    Integer.class,                "BatteryLevel" /* read-only */),
    CurrentLevel        (NumberItem.class,   PercentType.class,    Integer.class,                "CurrentLevel" /* read-only */),
    CurrentSetpoint     (NumberItem.class,   DecimalType.class,    Integer.class,                "CurrentSetpoint", "SetCurrentSetpoint", "NewCurrentSetpoint"),
    CurrentTemperature  (NumberItem.class,   DecimalType.class,    Integer.class,                "CurrentTemperature" /* read-only */),
    LastTrip            (DateTimeItem.class, DateTimeType.class,   Integer.class,                "LastTrip" /* read-only */),
    LoadLevelStatus     (DimmerItem.class,   PercentType.class,    UnsignedIntegerOneByte.class, "LoadLevelStatus", "SetLoadLevelTarget", "newLoadlevelTarget"),
    ModeState           (StringItem.class,   StringType.class,     String.class,                 "ModeState" /* read-only */),
    ModeStatus          (StringItem.class,   StringType.class,     String.class,                 "ModeStatus", "SetModeTarget", "NewModeTarget"),
    Status              (SwitchItem.class,   OnOffType.class,      Boolean.class,                "Status", "SetTarget", "newTargetValue"),
    Tripped             (ContactItem.class,  OpenClosedType.class, Boolean.class,                "Tripped" /* read-only */);

    /**
     * The item type.
     */
    private final Class<? extends Item> itemType;
    
    /**
     * The state type.
     */
    private final Class<? extends State> stateType;
    
    /**
     * The upnp type.
     */
    private final Class<?> upnpType;
    
    /**
     * The state variable name that's used in updates.
     */
    private final String stateVariableName;
    
    /**
     * The action name used to invoke actions.
     */
    private final String actionName;
    
    /**
     * The action argument named when invoking actions.
     */
    private final String actionArgumentName;

    /**
     * Creates a new (read-only) binding variable instance.
     * 
     * @param itemType the item type
     * @param stateType the state type
     * @param upnpType the upnp type
     * @param stateVariableName the state variable name
     */
    private VeraBindingVariable(Class<? extends Item> itemType, Class<? extends State> valueType, Class<?> upnpType, String statusVariable) {
        this(itemType, valueType, upnpType, statusVariable, null, null);
    }

    /**
     * Creates a new binding variable instance.
     * 
     * @param itemType the item type
     * @param stateType the state type
     * @param upnpType the upnp type
     * @param stateVariableName the state variable name
     * @param actionName the action name
     * @param actionArgumentName the action argument name.
     */
    private VeraBindingVariable(Class<? extends Item> itemType, Class<? extends State> stateType, Class<?> upnpType, String stateVariableName, String actionName, String actionArgumentName) {
        this.itemType = itemType;
        this.stateType = stateType;
        this.upnpType = upnpType;
        this.stateVariableName = stateVariableName;
        this.actionName = actionName;
        this.actionArgumentName = actionArgumentName;
    }
    
    /**
     * Tests if this variable supports the given <code>itemType</code>. 
     * @param itemType the @{link Item} type to test
     * @return if this variable supports the <code>itemType</code>
     */
    public boolean supports(Class<? extends Item> itemType) {
        return this.itemType.isAssignableFrom(itemType);
    }

    /**
     * Gets the action argument name.
     * @return the action argument name.
     */
    public String getActionArgumentName() {
        return actionArgumentName;
    }

    /**
     * Gets the action name.
     * @return the action name.
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Gets the openHAB {@link State} type.
     * @return the state type
     */
    public Class<? extends State> getStateType() {
        return stateType;
    }

    /**
     * Gets the state variable name.
     * @return the state variable name.
     */
    public String getStateVariableName() {
        return stateVariableName;
    }

    /**
     * Gets the upnp type.
     * @return the upnp type.
     */
    public Class<?> getUpnpType() {
        return upnpType;
    }
    
}
