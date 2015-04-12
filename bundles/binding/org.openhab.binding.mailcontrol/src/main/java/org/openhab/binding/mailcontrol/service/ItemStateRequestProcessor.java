/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

import java.util.ArrayList;
import java.util.List;

import org.creek.mailcontrol.model.data.DateTimeData;
import org.creek.mailcontrol.model.data.ItemStateData;
import org.creek.mailcontrol.model.data.StateTransformable;
import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.types.DateTimeDataType;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
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
        ItemStateData itemState = null;

        try {
            Item item = itemRegistry.getItem(itemId);
            StateTransformable state = getState(item);
            itemState = new ItemStateData(System.currentTimeMillis(), itemId, state);
        } catch (ItemNotFoundException ex) {
            logger.info(itemId + " not found", ex);
        }

        return itemState;
    }

    public List<ItemStateData> getItemStates() throws ServiceException {
        List<ItemStateData> itemStates = new ArrayList<ItemStateData>();

        ItemRegistry itemRegistry = getItemRegistry();
        for (Item item : itemRegistry.getItems()) {
            logger.debug("Item: " + item.getName() + " " + item.getState());
            StateTransformable state = getState(item);
            ItemStateData itemState = new ItemStateData(System.currentTimeMillis(), item.getName(), state);
            itemStates.add(itemState);
        }

        return itemStates;
    }

    private StateTransformable getState(Item item) {
        StateTransformable state = null;
        if (item.getState() instanceof HSBType) {
            HSBType hsb = (HSBType) item.getState();
            state = new HSBData(hsb.getHue().longValue(), hsb.getHue().longValue(), hsb.getHue().longValue());
        } else if (item.getState() instanceof DateTimeType) {
            DateTimeType dt = (DateTimeType) item.getState();
            DateTimeDataType data = new DateTimeDataType(dt.toString());
            state = new DateTimeData(data);
        } else if (item.getState() instanceof DecimalType) {

        } else if (item.getState() instanceof OnOffType) {

        } else if (item.getState() instanceof OpenClosedType) {

        } else if (item.getState() instanceof PercentType) {

        } else if (item.getState() instanceof UpDownType) {

        }
        return state;
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
}
