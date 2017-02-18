/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.asterisk.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.DtmfEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.openhab.binding.asterisk.AsteriskBindingProvider;
import org.openhab.binding.asterisk.internal.AsteriskGenericBindingProvider.AsteriskBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Asterisk binding connects to a Manager Interface of an Asterisk VOIP PBX
 * and listens to event notifications from this box.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class AsteriskBinding extends AbstractBinding<AsteriskBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(AsteriskBinding.class);

    protected static ManagerConnection managerConnection;

    /** The hostname of the Asterisk Manager Interface to connect to */
    protected static String host;
    /** The username to connect to the Manager Interface */
    protected static String username;
    /** The password to connect to the Manager Interface */
    protected static String password;

    public void activate() {
    }

    public void deactivate() {
        disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        if (config == null) {
            return;
        }

        disconnect();

        AsteriskBinding.host = Objects.toString(config.get("host"), null);
        AsteriskBinding.username = Objects.toString(config.get("username"), null);
        AsteriskBinding.password = Objects.toString(config.get("password"), null);

        if (StringUtils.isNotBlank(AsteriskBinding.host) && StringUtils.isNotBlank(AsteriskBinding.username)) {
            connect(AsteriskBinding.host, AsteriskBinding.username, AsteriskBinding.password);
        } else {
            logger.warn("Cannot connect to Asterisk manager interface because of missing "
                    + "parameters (host={}, username={})", AsteriskBinding.host, AsteriskBinding.username);
        }
    }

    /**
     * Connects to <code>host</code> and opens a ManagerConnection by using the
     * given <code>username</code> and <code>password</code>. Note: The Asterisk
     * ManagerInterface on your Asterisk PBX is deactivated by default. Please
     * refer to the documentation how to activate the ManagerInterface (AMI).
     * 
     * @param host where to find the Asterisk PBX
     * @param username username to login to Asterisk ManagerInterface
     * @param password password to login to Asterisk ManagerInterface
     */
    private void connect(String host, String username, String password) {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);

        AsteriskBinding.managerConnection = factory.createManagerConnection();
        AsteriskBinding.managerConnection.addEventListener(new AsteriskEventManager());

        try {
            AsteriskBinding.managerConnection.login();
        } catch (AuthenticationFailedException afe) {
            logger.error("Authentication failed.  Please verify username and password.");
        } catch (IOException ioe) {
            logger.error("Could not connect to manager interface on host {}: {}", host, ioe);
        } catch (Exception e) {
            logger.error("Login to Asterisk manager interface on host {} threw an exception: {}", host, e);
        }

        try {
            AsteriskBinding.managerConnection.sendAction(new StatusAction());
        } catch (Exception e) {
            logger.error("Registering for status update threw an exception: {}", e);
        }
    }

    /**
     * Disconnects from the current Asterisk ManagerInterface.
     */
    private void disconnect() {
        if (AsteriskBinding.managerConnection != null) {
            AsteriskBinding.managerConnection.logoff();
            AsteriskBinding.managerConnection = null;
        }
    }

    /**
     * @author Thomas.Eichstaedt-Engelen
     */
    private class AsteriskEventManager implements ManagerEventListener {

        /** holds call details of the currently active calls */
        protected Map<String, CallType> eventCache;

        public AsteriskEventManager() {
            eventCache = new HashMap<String, CallType>();
        }

        /**
         * @{inheritDoc}
         */
        public void onManagerEvent(ManagerEvent managerEvent) {
            for (AsteriskBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames()) {
                    Class<? extends Item> itemType = provider.getItemType(itemName);
                    AsteriskBindingConfig config = (AsteriskBindingConfig) provider.getConfig(itemName);
                    handleManagerEvent(itemName, itemType, managerEvent, config);
                }
            }
        }

        /**
         * Dispatches the given <code>managerEvent</code> to the specialized
         * handler methods.
         * 
         * @param itemName the corresponding item
         * @param itemType the Type of the corresponding item
         * @param managerEvent the {@link ManagerEvent} to dispatch
         */
        private void handleManagerEvent(String itemName, Class<? extends Item> itemType, ManagerEvent managerEvent,
                AsteriskBindingConfig config) {
            if (managerEvent instanceof NewChannelEvent) {
                handleNewCall(itemName, itemType, (NewChannelEvent) managerEvent, config);
            } else if (managerEvent instanceof HangupEvent) {
                handleHangupCall(itemName, itemType, (HangupEvent) managerEvent, config);
            } else if (managerEvent instanceof DtmfEvent) {
                handleDtmfEvent(itemName, itemType, (DtmfEvent) managerEvent, config);
            }
        }

        private void handleDtmfEvent(String itemName, Class<? extends Item> itemType, DtmfEvent event,
                AsteriskBindingConfig config) {
            if (config.type.equals("digit") && event.isBegin()) {
                CallType call = eventCache.get(event.getUniqueId());
                if (call != null) {
                    String reqCid = config.getCallerId();
                    String reqExt = config.getExtension();
                    String reqDigit = config.getDigit();

                    String src = null;
                    String dst = null;

                    if (event.getDirection().toString().equals("Sent")) {
                        src = call.getDestNum().toString();
                        dst = call.getOrigNum().toString();
                    } else {
                        src = call.getOrigNum().toString();
                        dst = call.getDestNum().toString();
                    }

                    if ((reqCid == null || (reqCid != null && reqCid.equals(src)))
                            && (reqExt == null || (reqExt != null && reqExt.equals(dst)))
                            && (reqDigit.equals(event.getDigit().toString()))) {

                        if (itemType.isAssignableFrom(SwitchItem.class)) {
                            eventPublisher.postUpdate(itemName, OnOffType.ON);
                        } else {
                            logger.warn("DTMF event not applicable to item {}", itemName);
                        }

                    }

                    logger.info("DTMF event received. Digit '{}' sent from '{}' to '{}'", event.getDigit(), src, dst);
                }
            }
        }

        private void handleNewCall(String itemName, Class<? extends Item> itemType, NewChannelEvent event,
                AsteriskBindingConfig config) {
            if (event.getCallerIdNum() == null || event.getExten() == null) {
                logger.debug("calleridnum or exten is null -> handle new call aborted!");
                return;
            }

            CallType call = new CallType(new StringType(event.getCallerIdNum()), new StringType(event.getExten()));
            eventCache.put(event.getUniqueId(), call);

            if (config.type.equals("active")) {

                String reqCid = config.getCallerId();
                String reqExt = config.getExtension();

                if ((reqCid == null || (reqCid != null && reqCid.equals(event.getCallerIdNum().toString())))
                        && (reqExt == null || (reqExt != null && reqExt.equals(event.getExten().toString())))) {

                    if (itemType.isAssignableFrom(SwitchItem.class)) {
                        eventPublisher.postUpdate(itemName, OnOffType.ON);
                    } else if (itemType.isAssignableFrom(CallItem.class)) {
                        eventPublisher.postUpdate(itemName, call);
                    } else {
                        logger.warn("Handle call for item '{}' is undefined", itemName);
                    }

                }
            }

        }

        /**
         * Removes <code>event</code> from the <code>eventCache</code> and posts
         * updates according to the content of the <code>eventCache</code>. If
         * there is no active call left we send an OFF-State (resp. empty)
         * {@link CallType} and ON-State (one of the remaining active calls)
         * in all other cases.
         * 
         * @param itemName
         * @param itemType
         * @param event
         */
        private void handleHangupCall(String itemName, Class<? extends Item> itemType, HangupEvent event,
                AsteriskBindingConfig config) {
            eventCache.remove(event.getUniqueId());

            if (config.type.equals("active")) {
                String reqCid = config.getCallerId();
                String reqExt = config.getExtension();

                if (reqCid == null && reqExt == null) { // if both requirements are null, toggle the switch or call the
                                                        // old way

                    if (itemType.isAssignableFrom(SwitchItem.class)) {
                        OnOffType activeState = (eventCache.size() == 0 ? OnOffType.OFF : OnOffType.ON);
                        eventPublisher.postUpdate(itemName, activeState);
                    } else if (itemType.isAssignableFrom(CallItem.class)) {
                        CallType call = (CallType) (eventCache.size() == 0 ? CallType.EMPTY
                                : eventCache.values().toArray()[0]);
                        eventPublisher.postUpdate(itemName, call);
                    } else {
                        logger.warn("handleHangupCall - postUpdate for item '{}' is undefined", itemName);
                    }
                } else {
                    if ((reqCid == null || (reqCid != null && reqCid.equals(event.getCallerIdNum().toString())))
                            && (reqExt == null || (reqExt != null && reqExt.equals(event.getExten().toString())))) {

                        if (itemType.isAssignableFrom(SwitchItem.class)) {
                            eventPublisher.postUpdate(itemName, OnOffType.OFF);
                        } else if (itemType.isAssignableFrom(CallItem.class)) {
                            eventPublisher.postUpdate(itemName, CallType.EMPTY);
                        } else {
                            logger.warn("handleHangupCall - postUpdate for item '{}' is undefined", itemName);
                        }
                    }
                }
            }
        }
    }
}
