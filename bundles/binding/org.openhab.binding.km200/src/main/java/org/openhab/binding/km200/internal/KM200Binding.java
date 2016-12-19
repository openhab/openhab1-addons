/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.km200.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.km200.KM200BindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

/**
 * The KM200 binding connects to a Buderus Gateway Logamatic web KM50/100/200.
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */
public class KM200Binding extends AbstractActiveBinding<KM200BindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(KM200Binding.class);

    private KM200Device device = null;

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        super.bindingChanged(provider, itemName);
        logger.trace("KN200 bindingChanged: {}", bindingsExist());

        conditionalDeActivate();
    }

    private void conditionalDeActivate() {
        logger.trace("KN200 conditional deActivate: {}", bindingsExist());

        if (bindingsExist()) {
            activate();
        } else {
            deactivate();
        }
    }

    @Override
    public void activate() {
        super.activate();

        logger.debug("KM200 binding has been started.");
    }

    @Override
    public void deactivate() {
        super.deactivate();

        logger.debug("KM200 binding has been stopped.");
    }

    protected void addBindingProvider(KM200BindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(KM200BindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        logger.info("Update KM200 Binding configuration, it takes a minute....");

        if (config == null) {
            logger.info("Configuration is null");
            return;
        } else {
            if (config.isEmpty()) {
                throw new RuntimeException("No properties in openhab.cfg set!");
            }
            if (device == null) {
                device = new KM200Device();

            }
            String ip = (String) config.get("ip4_address");
            if (StringUtils.isNotBlank(ip)) {
                try {
                    InetAddresses.forString(ip);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("ip4_address in openhab.cfg is not valid!");
                }
                device.setIP4Address(ip);
            } else {
                throw new RuntimeException("No ip4_address in openhab.cfg set!");
            }
            /* There a two possibilities of configuratiom */
            /* 1. With a private key */
            String PrivKey = (String) config.get("PrivKey");
            if (StringUtils.isNotBlank(PrivKey)) {
                device.setCryptKeyPriv(PrivKey);

            } else { /* 2. With the MD5Salt, the device and user private password */
                String MD5Salt = (String) config.get("MD5Salt");
                if (StringUtils.isNotBlank(MD5Salt)) {
                    device.setMD5Salt(MD5Salt);
                } else {
                    throw new RuntimeException("No MD5Salt in openhab.cfg set!");
                }

                String gpassword = (String) config.get("GatewayPassword");
                if (StringUtils.isNotBlank(gpassword)) {
                    device.setGatewayPassword(gpassword);
                } else {
                    throw new RuntimeException("No GatewayPassword in openhab.cfg set!");
                }

                String ppassword = (String) config.get("PrivatePassword");
                if (StringUtils.isNotBlank(ppassword)) {
                    device.setPrivatePassword(ppassword);
                } else {
                    throw new RuntimeException("No PrivatePassword in openhab.cfg set!");
                }
            }
            /* try to communicate */
            KM200Comm comm = new KM200Comm();
            /* Get HTTP Data from device */
            byte[] recData = comm.getDataFromService(device, "/gateway/DateTime");
            if (recData == null) {
                throw new RuntimeException("Communication is not possible!");
            }
            if (recData.length == 0) {
                throw new RuntimeException("No reply from KM200!");
            }
            /* Derypt the message */
            String decodedData = comm.decodeMessage(device, recData);
            if (decodedData == null) {
                throw new RuntimeException("Decoding of the KM200 message is not possible!");
            }

            if (decodedData == "SERVICE NOT AVAILABLE") {
                logger.error("/gateway/DateTime: SERVICE NOT AVAILABLE");
            } else {
                logger.info("Test of the communication to the gateway was successfull");
            }
            /* communication is working */
            /* Checking of the devicespecific services and creating of a service list */
            for (KM200ServiceTypes service : KM200ServiceTypes.values()) {
                comm.initObjects(device, service.getDescription());
            }
            /* Output all availible services in the log file */
            device.listAllServices();

            logger.info("... Update of the KM200 Binding configuration completed");
            device.setInited(true);
            setProperlyConfigured(true);
        }

    }

    @Override
    protected void execute() {

        logger.debug("KN200 execute");
        if (device == null) {
            return;
        } else if (!device.isConfigured()) {
            logger.error("Device is not configured, did you set the configuration?");
            return;
        }

        try {
            KM200Comm comm = new KM200Comm();
            for (KM200BindingProvider provider : providers) {
                for (String item : provider.getItemNames()) {
                    State state = comm.getProvidersState(device, provider, item);
                    if (state != null) {
                        eventPublisher.postUpdate(item, state);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Could not get item state ", e);
        }

    }

    @Override
    public void internalReceiveCommand(String item, Command command) {
        logger.debug("internalReceiveCommand");

        if (device != null) {
            String type = null;
            KM200BindingProvider provider = null;
            for (KM200BindingProvider tmpProvider : providers) {
                type = tmpProvider.getType(item);
                if (type != null) {
                    provider = tmpProvider;
                    break;
                }
            }
            if (type == null) {
                return;
            }
            logger.debug("KM200 type: {} {}", type, provider.getService(item));
            try {
                KM200Comm comm = new KM200Comm();
                comm.sendProvidersState(device, provider, item, command);
            } catch (Exception e) {
                logger.warn("Could not send item state ", e);
            }

        }
    }

    @Override
    protected long getRefreshInterval() {
        return 60000L;
    }

    @Override
    protected String getName() {
        return "KM200 Binding";
    }

}
