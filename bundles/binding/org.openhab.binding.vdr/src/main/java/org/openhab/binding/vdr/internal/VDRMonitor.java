/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vdr.internal;

import org.openhab.binding.vdr.VDRBindingProvider;
import org.openhab.binding.vdr.VDRCommandType;
import org.openhab.binding.vdr.internal.VDRBinding.VDRConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The VDRMonitor connects periodically to a VDR on the svdr port and updates
 * several state informations
 *
 * The followining checks are implemented: - recording status
 *
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public class VDRMonitor extends AbstractActiveService {

    static final Logger logger = LoggerFactory.getLogger(VDRMonitor.class);

    /* the VDRBinding Service */
    private static VDRBinding vdrBinding;

    /**
     * the refresh interval which is used to poll the several VDR variables
     * (e.g. recording state; defaults to 60000ms)
     */
    private long refreshInterval = 120000;

    private EventPublisher eventPublisher = null;

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    public void setVDRBinding(VDRBinding vdrBinding) {
        VDRMonitor.vdrBinding = vdrBinding;
        setProperlyConfigured(true);
    }

    public void unsetVDRBinding(VDRBinding vdrBinding) {
        VDRMonitor.vdrBinding = null;
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "VDR Monitor Service";
    }

    @Override
    protected void execute() {
        logger.debug("VDRMonitor execute called");
        if (vdrBinding == null) {
            logger.info("VDRBinding not available");
            return;
        }

        for (VDRConfig vdrConfig : vdrBinding.vdrConfigCache.values()) {
            logger.debug("Loop examining vdrConfig '{}' ...", vdrConfig);
            Boolean recording = checkRecording(vdrConfig);
            if (recording == null || eventPublisher == null) {
                continue;
            }

            VDRBindingProvider bindingProvider = vdrBinding.findFirstMatchingBindingProviderByVDRId(vdrConfig.vdrId);
            if (bindingProvider == null) {
                continue;
            }

            String itemName = bindingProvider.getBindingItemName(vdrConfig.vdrId, VDRCommandType.RECORDING);
            if (itemName == null) {
                logger.debug(
                        "itemName was null, so this must not be a recording item binding. Not publishing anything.");
            } else {
                State newState = recording.booleanValue() ? OnOffType.ON : OnOffType.OFF;
                logger.debug("Publishing update for recording item binding '{}' with state '{}'", itemName, newState);
                eventPublisher.postUpdate(itemName, newState);
            }
        }
    }

    private Boolean checkRecording(VDRConfig vdrConfig) {
        return vdrConfig.getVDRConnection().isRecording();
    }
}
