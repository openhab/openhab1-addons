/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.Zibase;
import fr.zapi.utils.XmlSimpleParse;

/**
 * This Class handle emitter items recognized by the zibase
 *
 * @author Julien Tiphaine
 * @since 1.7.0
 *
 */
public class ZibaseBindingConfigEmitter extends ZibaseBindingConfig {

    /**
     * Map that associate a tag name returned by the zibase with a type that openhab understand (eg: for eventpublisher
     * update).
     * This Map also allow to control item's config
     */
    static final HashMap<String, Class<?>> valueStateMap;

    static {
        valueStateMap = new HashMap<String, Class<?>>();
        valueStateMap.put("rf", org.openhab.core.library.types.StringType.class); // protocol
        valueStateMap.put("noise", org.openhab.core.library.types.DecimalType.class);// Rf signal noise
        valueStateMap.put("lev", org.openhab.core.library.types.DecimalType.class);// Rf signal strenght
        valueStateMap.put("dev", org.openhab.core.library.types.StringType.class); // device's name
        valueStateMap.put("bat", org.openhab.core.library.types.StringType.class); // device's battery state : Ok or Ko
        valueStateMap.put("ch", org.openhab.core.library.types.DecimalType.class);// device's Rf Channel
        valueStateMap.put("tem", org.openhab.core.library.types.DecimalType.class);// temperature
        valueStateMap.put("temc", org.openhab.core.library.types.DecimalType.class);// temperature ceil
        valueStateMap.put("tra", org.openhab.core.library.types.DecimalType.class);// total rain
        valueStateMap.put("cra", org.openhab.core.library.types.DecimalType.class);// current rain
        valueStateMap.put("uvl", org.openhab.core.library.types.DecimalType.class);// Ultra violet level
        valueStateMap.put("awi", org.openhab.core.library.types.DecimalType.class);// Average wind
        valueStateMap.put("dir", org.openhab.core.library.types.DecimalType.class);// wind direction
        valueStateMap.put("sta", org.openhab.core.library.types.StringType.class); // unknown
        valueStateMap.put("kwh", org.openhab.core.library.types.DecimalType.class);// kilowatts per hour
        valueStateMap.put("w", org.openhab.core.library.types.DecimalType.class);// total watt consumption
        valueStateMap.put("hum", org.openhab.core.library.types.DecimalType.class);// humidity
        valueStateMap.put("area", org.openhab.core.library.types.StringType.class); // area (eg. for alarm devices)
        valueStateMap.put("flag1", org.openhab.core.library.types.StringType.class); // custom flag
        valueStateMap.put("flag2", org.openhab.core.library.types.StringType.class); // custom flag
        valueStateMap.put("flag3", org.openhab.core.library.types.StringType.class); // custom flag
    }

    /**
     * openhab type constructor to use with the item.
     * putting this here avoid to do the matching at runtime
     */
    private Constructor<?> constructor = null;

    /**
     * constructor
     * 
     * @param configParameters
     */
    public ZibaseBindingConfigEmitter(String[] configParameters) {
        super(configParameters);

        try {
            constructor = valueStateMap.get(configParameters[ZibaseBindingConfig.POS_VALUES])
                    .getConstructor(String.class);
        } catch (Exception ex) {
            logger.debug("unsupported value {} for item ID {} => value will be passed as is...", configParameters[ZibaseBindingConfig.POS_VALUES], this.getId());
        }
    }

    /**
     * {@link Inherited}
     */
    @Override
    public void sendCommand(Zibase zibase, Command command, int dim) {
        logger.error("sendCommand : not implemented for Config receiver");
    }

    /**
     * {@link Inherited}
     */
    @Override
    protected boolean isItemConfigValid() {
        logger.info("Checking config for Command item {}", this.getId());

        if (ZibaseBindingConfigEmitter.getValueStateMap().containsKey(this.values[ZibaseBindingConfig.POS_VALUES])) {
            logger.info("Config OK for Receiver item {}", this.getId());
        } else {
            logger.info("Unsupported value identifier for item {} => value will be passed as is", this.getId());
        }

        return true;
    }

    /**
     * get valueStateMap
     * 
     * @return valueStateMap
     */
    public static HashMap<String, Class<?>> getValueStateMap() {
        return valueStateMap;
    }

    /**
     * {@link Inherited}
     */
    @Override
    public State getOpenhabStateFromZibaseValue(Zibase zibase, String zbResponseStr) {
        if (constructor != null) {
            try {
                String zibaseValue = XmlSimpleParse.getTagValue(this.values[ZibaseBindingConfig.POS_VALUES],
                        zbResponseStr);
                return (State) constructor.newInstance(zibaseValue);
            } catch (Exception e) {
                logger.error("unable to convert zibase value to openHab State : {}", e.toString());
                e.printStackTrace();
            }
        }

        return new StringType(this.values[ZibaseBindingConfig.POS_VALUES]);
    }
}
