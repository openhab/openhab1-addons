/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.km200.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.km200.KM200BindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
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
    private Map<String, byte[]> sendMap = Collections.synchronizedMap(new LinkedHashMap<String, byte[]>());
    ExecutorService threadPool = Executors.newSingleThreadExecutor();

    private KM200Device device = null;
    private KM200Comm comm = null;
    private SendKM200Thread sThread = null;

    public KM200Binding() {
        if (device == null) {
            device = new KM200Device();
        }
        if (comm == null) {
            comm = new KM200Comm(device);
        }
    }

    @Override
    public void activate() {
        if (device != null) {
            logger.debug("Starting send thread");
            sThread = new SendKM200Thread(sendMap, device, comm, providers, eventPublisher);
            sThread.start();
        }
        super.activate();

        logger.info("Activated");
    }

    @Override
    public void deactivate() {
        if (sThread != null) {
            logger.debug("Interrupt send thread");
            sThread.interrupt();
        }
        super.deactivate();

        logger.info("Deactivated");
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

        if (config == null) {
            return;
        } else {
            if (config.isEmpty()) {
                return;
            }
            logger.info("Update KM200 Binding configuration, it takes a minute....");
            String ip = Objects.toString(config.get("ip4_address"), null);
            if (StringUtils.isNotBlank(ip)) {
                try {
                    InetAddresses.forString(ip);
                } catch (IllegalArgumentException e) {
                    logger.error("IP4_address in openhab.cfg is not valid!");
                    throw new ConfigurationException("ip4_address", "ip4_address in openhab.cfg is not valid!");
                }
                device.setIP4Address(ip);
            } else {
                logger.error("No ip4_address in openhab.cfg set!");
                throw new ConfigurationException("ip4_address", "No ip4_address in openhab.cfg set!");
            }
            /* There a two possibilities of configuratiom */
            /* 1. With a private key */
            String PrivKey = Objects.toString(config.get("PrivKey"), null);
            if (StringUtils.isNotBlank(PrivKey)) {
                device.setCryptKeyPriv(PrivKey);

            } else { /* 2. With the MD5Salt, the device and user private password */
                String MD5Salt = Objects.toString(config.get("MD5Salt"), null);
                if (StringUtils.isNotBlank(MD5Salt)) {
                    device.setMD5Salt(MD5Salt);
                } else {
                    logger.error("No MD5Salt in openhab.cfg set!");
                    throw new ConfigurationException("MD5Salt", "No MD5Salt in openhab.cfg set!");
                }

                String gpassword = Objects.toString(config.get("GatewayPassword"), null);
                if (StringUtils.isNotBlank(gpassword)) {
                    device.setGatewayPassword(gpassword);
                } else {
                    logger.error("No GatewayPassword in openhab.cfg set!");
                    throw new ConfigurationException("GatewayPassword", "No GatewayPassword in openhab.cfg set!");
                }

                String ppassword = Objects.toString(config.get("PrivatePassword"), null);
                if (StringUtils.isNotBlank(ppassword)) {
                    device.setPrivatePassword(ppassword);
                } else {
                    logger.error("No PrivatePassword in openhab.cfg set!");
                    throw new ConfigurationException("PrivatePassword", "No PrivatePassword in openhab.cfg set!");
                }
            }
            logger.info("Starting communication test..");
            /* Get HTTP Data from device */
            byte[] recData = comm.getDataFromService("/gateway/DateTime");
            if (recData == null) {
                throw new RuntimeException("Communication is not possible!");
            }
            if (recData.length == 0) {
                throw new RuntimeException("No reply from KM200!");
            }
            logger.info("Received data..");
            /* Derypt the message */
            String decodedData = comm.decodeMessage(recData);
            if (decodedData == null) {
                throw new RuntimeException("Decoding of the KM200 message is not possible!");
            }

            if (decodedData == "SERVICE NOT AVAILABLE") {
                logger.error("/gateway/DateTime: SERVICE NOT AVAILABLE");
            } else {
                logger.info("Test of the communication to the gateway was successful..");
            }
            logger.info("Init services..");
            /* communication is working */
            /* Checking of the devicespecific services and creating of a service list */
            for (KM200ServiceTypes service : KM200ServiceTypes.values()) {
                try {
                    logger.debug(service.getDescription());
                    comm.initObjects(service.getDescription());
                } catch (Exception e) {
                    logger.error("Couldn't init service: {} error: {}", service, e.getMessage());
                }
            }
            /* Now init the virtual services */
            logger.debug("init Virtual Objects");
            try {
                comm.initVirtualObjects();
            } catch (Exception e) {
                logger.error("Couldn't init virtual services: {}", e.getMessage());
            }
            /* Output all availible services in the log file */
            /* Now init the virtual services */
            logger.debug("list All Services");
            device.listAllServices();
            logger.info("... Update of the KM200 Binding configuration completed");

            device.setInited(true);
            setProperlyConfigured(true);
        }

    }

    @Override
    protected void execute() {

        logger.debug("KM200 execute");
        if (device == null) {
            return;
        } else if (!device.isConfigured() || !device.getInited()) {
            logger.error("Device is not configured, did you set the configuration?");
            return;
        }
        threadPool.submit(new GetKM200Runnable(device, comm, providers, eventPublisher));

    }

    @SuppressWarnings("null")
    @Override
    public void internalReceiveCommand(String item, Command command) {
        logger.debug("internalReceiveCommand");
        byte[] sendData = null;

        if (device != null) {
            String type = null;
            String service = null;
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
                sendData = comm.sendProvidersState(provider, item, command);
            } catch (Exception e) {
                logger.error("Could not send item state {}", e);
            }

            synchronized (device) {
                service = comm.checkParameterReplacement(provider, item);
                if (sendData != null) {
                    sendMap.put(item, sendData);
                } else if (device.serviceMap.get(service).getVirtual() == 1) {
                    String parent = device.serviceMap.get(service).getParent();
                    for (KM200BindingProvider tmpProvider : providers) {
                        for (String tmpItem : tmpProvider.getItemNames()) {
                            service = comm.checkParameterReplacement(tmpProvider, tmpItem);
                            if (parent.equals(device.serviceMap.get(service).getParent())) {
                                try {
                                    State state = comm.getProvidersState(tmpProvider, tmpItem);
                                    if (state != null) {
                                        eventPublisher.postUpdate(tmpItem, state);
                                    }
                                } catch (Exception e) {
                                    logger.error("Could not get updated item state, Error: {}", e);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * The GetKM200Runnable class get the data from device to the items.
     *
     * @author Markus Eckhardt
     *
     * @since 1.9.0
     */
    private static class GetKM200Runnable implements Runnable {

        public GetKM200Runnable(KM200Device device, KM200Comm comm, Collection<KM200BindingProvider> providers,
                EventPublisher eventPublisher) {
            super();
            this.device = device;
            this.providers = providers;
            this.eventPublisher = eventPublisher;
            this.comm = comm;
        }

        private Collection<KM200BindingProvider> providers;
        private KM200Device device;
        private EventPublisher eventPublisher;
        private KM200Comm comm;

        @Override
        public void run() {
            try {
                logger.debug("GetKM200Runnable");
                org.openhab.core.types.State state = null;
                synchronized (device) {
                    device.resetAllUpdates();
                    for (KM200BindingProvider provider : providers) {
                        for (String item : provider.getItemNames()) {
                            try {
                                state = comm.getProvidersState(provider, item);
                                if (state != null) {
                                    eventPublisher.postUpdate(item, state);
                                }
                            } catch (Exception e) {
                                logger.error("Could not get item state, Error: {}", e);
                            }
                        }
                    }
                }
            } catch (

            Exception e) {
                logger.warn("Error processing command", e);
            }
        }

    }

    /**
     * The sendKM200Thread class sends the data to the device.
     *
     * @author Markus Eckhardt
     *
     * @since 1.9.0
     */
    private static class SendKM200Thread extends Thread {

        public SendKM200Thread(Map<String, byte[]> sendMap, KM200Device device, KM200Comm comm,
                Collection<KM200BindingProvider> providers, EventPublisher eventPublisher) {
            super();
            this.sendMap = sendMap;
            this.device = device;
            this.providers = providers;
            this.eventPublisher = eventPublisher;
            this.comm = comm;
        }

        private Map<String, byte[]> sendMap = null;
        private Collection<KM200BindingProvider> providers;
        private KM200Device device;
        private EventPublisher eventPublisher;
        private KM200Comm comm;

        @Override
        public void run() {
            try {
                logger.debug("Send-Thread started");
                while (!isInterrupted()) {
                    Map.Entry<String, byte[]> nextEntry = null;
                    {
                        /* Check whether a new entry is availible, if yes then take and remove it */
                        synchronized (sendMap) {
                            Iterator<Entry<String, byte[]>> i = sendMap.entrySet().iterator();

                            if (i.hasNext()) {
                                logger.debug("Send-Thread, new entry");
                                nextEntry = i.next();
                                i.remove();
                            }
                        }
                    }

                    if (nextEntry != null) {
                        /* Now send the data to the device */
                        Integer rCode;
                        org.openhab.core.types.State state = null;
                        String item = nextEntry.getKey();
                        KM200BindingProvider provider = null;
                        byte[] encData = nextEntry.getValue();
                        for (KM200BindingProvider tmpProvider : providers) {
                            String type = tmpProvider.getType(item);
                            if (type != null) {
                                provider = tmpProvider;
                                break;
                            }
                        }
                        if (provider == null) {
                            continue;
                        }
                        String service = comm.checkParameterReplacement(provider, item);
                        KM200CommObject object = device.serviceMap.get(service);

                        logger.debug("Sending: {}", provider.getService(item));

                        if (object.getVirtual() == 1) {
                            rCode = comm.sendDataToService(object.getParent(), encData);
                        } else {
                            rCode = comm.sendDataToService(service, encData);
                        }
                        logger.debug("Returncode: {}", rCode);
                        /* set all update flags to zero */

                        logger.debug("Data sended, reset und updated providers");

                        /* Now update the set values and for all virtual values depending on same parent */
                        if (object.getVirtual() == 1) {
                            String parent = object.getParent();
                            device.serviceMap.get(parent).setUpdated(false);

                            for (KM200BindingProvider tmpProvider : providers) {
                                for (String tmpItem : tmpProvider.getItemNames()) {
                                    String tmpService = comm.checkParameterReplacement(tmpProvider, tmpItem);
                                    if (parent.equals(device.serviceMap.get(tmpService).getParent())) {
                                        try {
                                            state = comm.getProvidersState(tmpProvider, tmpItem);
                                            if (state != null) {
                                                eventPublisher.postUpdate(tmpItem, state);
                                            }
                                        } catch (Exception e) {
                                            logger.error("Could not get updated item state, Error: {}", e);
                                        }
                                    }
                                }
                            }
                        } else {
                            try {
                                object.setUpdated(false);
                                state = comm.getProvidersState(provider, item);
                                if (state != null) {
                                    eventPublisher.postUpdate(item, state);
                                }
                                /* Check whether the service is used as a parameter replacement */
                                for (KM200BindingProvider tmpProvider : providers) {
                                    for (String tmpItem : tmpProvider.getItemNames()) {
                                        if (tmpProvider.getParameter(tmpItem).containsKey("current")) {
                                            if (service.equals(tmpProvider.getParameter(tmpItem).get("current"))) {
                                                try {
                                                    state = comm.getProvidersState(tmpProvider, tmpItem);
                                                    if (state != null) {
                                                        eventPublisher.postUpdate(tmpItem, state);
                                                    }
                                                } catch (Exception e) {
                                                    logger.error("Could not get updated item state, Error: {}", e);
                                                }

                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                logger.error("Could not get item state, Error: {}", e);
                            }
                        }

                    }
                    /*
                     * We have time, all changes on same item in this time are overwritten in memory and we need send
                     * only the last state
                     */
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            } catch (

            Exception e) {
                logger.warn("Error processing command", e);
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
