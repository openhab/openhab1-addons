/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import static org.creek.mailcontrol.model.data.DataType.DECIMAL;
import static org.creek.mailcontrol.model.data.DataType.HSB;
import static org.creek.mailcontrol.model.data.DataType.INCREASE_DECREASE;
import static org.creek.mailcontrol.model.data.DataType.ON_OFF;
import static org.creek.mailcontrol.model.data.DataType.OPEN_CLOSED;
import static org.creek.mailcontrol.model.data.DataType.PERCENT;
import static org.creek.mailcontrol.model.data.DataType.STOP_MOVE;
import static org.creek.mailcontrol.model.data.DataType.STRING;

import java.util.ArrayList;
import java.util.List;

import org.creek.mailcontrol.model.data.DataType;
import org.creek.mailcontrol.model.data.DateTimeData;
import org.creek.mailcontrol.model.data.DecimalData;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.data.OnOffData;
import org.creek.mailcontrol.model.data.OpenClosedData;
import org.creek.mailcontrol.model.data.PercentData;
import org.creek.mailcontrol.model.data.StateTransformable;
import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.data.UpDownData;
import org.creek.mailcontrol.model.types.DateTimeDataType;
import org.creek.mailcontrol.model.types.DecimalDataType;
import org.creek.mailcontrol.model.types.OnOffDataType;
import org.creek.mailcontrol.model.types.OpenClosedDataType;
import org.creek.mailcontrol.model.types.PercentDataType;
import org.creek.mailcontrol.model.types.UpDownDataType;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class ItemStateRequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ItemStateRequestProcessor.class);

    public ItemStateData getItemState(String itemId) throws ServiceException {
        ItemRegistry itemRegistry = getItemRegistry();
        
        try {
            Item item = itemRegistry.getItem(itemId);
            StateTransformable state = getState(item);
            List<DataType> acceptedCommands = buildAcceptedCommandsList(item);
            return new ItemStateData(System.currentTimeMillis(), itemId, state, acceptedCommands);
        } catch (ItemNotFoundException ex) {
            logger.info(itemId + " not found", ex);
        }

        throw new ServiceException("Item not found " + itemId);
    }

    public List<ItemStateData> getItemStates() throws ServiceException {
        List<ItemStateData> itemStates = new ArrayList<ItemStateData>();
        
        ItemRegistry itemRegistry = getItemRegistry();
        for (Item item: itemRegistry.getItems()) {
            logger.debug("Item: " + item.getName() + " " + item.getState());
            StateTransformable state = getState(item);
            List<DataType> acceptedCommands = buildAcceptedCommandsList(item);
            ItemStateData itemState = new ItemStateData(System.currentTimeMillis(), item.getName(), state, acceptedCommands);
            itemStates.add(itemState);
         }
        
        return itemStates;
    }

    private StateTransformable getState(Item item) throws ServiceException {
        State state = item.getState();
        if (state instanceof HSBType) {
            HSBType hsb = (HSBType)state;
            return new HSBData(hsb.getHue().longValue(), hsb.getHue().longValue(), hsb.getHue().longValue());
        } else if (state instanceof DateTimeType) {
            DateTimeType dt = (DateTimeType)state;
            DateTimeDataType data = new DateTimeDataType(dt.toString());
            return new DateTimeData(data);
        } else if (state instanceof DecimalType) {
            DecimalType dt = (DecimalType)state;
            return new DecimalData(new DecimalDataType(dt.toString()));
        } else if (state instanceof OnOffType) {
            OnOffType dt = (OnOffType)state;
            return new OnOffData(OnOffDataType.valueOf(dt.toString()));
        } else if (item.getState() instanceof OpenClosedType) {
            OpenClosedType dt = (OpenClosedType)state;
            return new OpenClosedData(OpenClosedDataType.valueOf(dt.toString()));
        } else if (item.getState() instanceof PercentType) {
            PercentType dt = (PercentType)state;
            return new PercentData(new PercentDataType(dt.toString()));
        } else if (item.getState() instanceof UpDownType) {
            UpDownType dt = (UpDownType)state;
            return new UpDownData(UpDownDataType.valueOf(dt.toString()));
        }

        throw new ServiceException("Wrong command " + state.getClass().getName());
    }

    private ItemRegistry getItemRegistry() throws ServiceException {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        if (bundleContext != null) {
            ServiceReference<?> serviceReference = bundleContext.getServiceReference(ItemRegistry.class.getName());
            if (serviceReference != null) {
                ItemRegistry itemregistry = (ItemRegistry) bundleContext.getService(serviceReference);
                return itemregistry;
            }
        }
        
        throw new ServiceException("Cannot get ItemRegistry");
    }
    
    private List<DataType> buildAcceptedCommandsList(Item item) throws ServiceException {
        List<DataType> acceptedCommands = new ArrayList<DataType>();
        
        for (Class<? extends Command> commandClass: item.getAcceptedCommandTypes()) {
            DataType dataType = convertCommand(commandClass);
            acceptedCommands.add(dataType);
        }

        return acceptedCommands;
    }

    private DataType convertCommand(Class<? extends Command> commandClass) throws ServiceException {
        if (commandClass == DecimalType.class) {
            return DECIMAL;
        } else if (commandClass == HSBType.class) {
            return HSB;
        } else if (commandClass == IncreaseDecreaseType.class) {
            return INCREASE_DECREASE;
        } else if (commandClass == OnOffType.class) {
            return ON_OFF;
        } else if (commandClass == OpenClosedType.class) {
            return OPEN_CLOSED;
        } else if (commandClass == PercentType.class) {
            return PERCENT;
        } else if (commandClass == StopMoveType.class) {
            return STOP_MOVE;
        } else if (commandClass == StringType.class) {
            return STRING;
        }

        throw new ServiceException("Wrong command " + commandClass.getName());
    }
}
