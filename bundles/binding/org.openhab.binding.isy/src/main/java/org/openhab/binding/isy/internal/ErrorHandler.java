/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.universaldevices.common.UDClientStatus;
import com.universaldevices.resources.errormessages.ErrorEventListener;
import com.universaldevices.resources.errormessages.Errors;
import com.universaldevices.upnp.UDControlPoint;
import com.universaldevices.upnp.UDProxyDevice;

/**
 * Error handler for the ISY client
 * 
 * @author Tim Diekmann
 * @since 1.10.0
 */
public class ErrorHandler implements ErrorEventListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int socket_open_failed_count = 0;

    private void offLine(final int status, Object device) {
        String err = Errors.getErrorMessage(status);

        this.logger.warn("Warning: {} {}", status, (err == null ? " " : err));

        if (UDClientStatus.isBusy()) {
            return;
        }
        if (status == 1050) {
            this.socket_open_failed_count++;
        }
        if (this.socket_open_failed_count < 3) {
            return;
        }
        this.socket_open_failed_count = 0;

        if (device == null) {
            device = UDControlPoint.firstDevice;
        }
        if (device != null && device instanceof UDProxyDevice) {
            UDControlPoint.getInstance().offLine((UDProxyDevice) device);
        }

    }

    private void humanInterventionRequired(final int status, final String msg) {
        String err = Errors.getErrorMessage(status);
        this.logger.warn("Human Intervention Required: {} {} {}", status, (err == null ? " " : err), (msg == null ? " " : msg) );
    }

    private void warning(final int status, final String msg) {
        String err = Errors.getErrorMessage(status);
        this.logger.warn("Warning: {} {} {}", status, (err == null ? " " : err), (msg == null ? " " : msg) );
    }

    private void fatalError(final int status, final String msg) {
        String err = Errors.getErrorMessage(status);
        this.logger.warn("ISY Fatal Error: {} {} {}", status, (err == null ? " " : err), (msg == null ? " " : msg) );
    }

    /**
     * This method is invoked when ISY encounters an error
     * 
     * @param status
     *            - the error code
     * @param msg
     *            - any generated messages by <code>ISYClient</code>
     * @param device
     *            - the <code>UDProxyDevice</code> initiating this event
     * @return - whether or not the client attempt displaying the error on a UI.
     *         if a <code>GUIErrorHandler</code> is installed, that object is
     *         invoked (showError)
     */

    @Override
    public boolean errorOccured(final int status, final String msg, final Object device) {

        switch (status) {
            case -1:
            case 803/* discovering nodes:retry */:
            case 902/* Node Is in Error; Check connections! */:
                warning(status, null);
                break;

            case 801/* maximum secure sessions */:
            case 805/* internal error: reboot */:
            case 815/* Maximum Subscribers Reached */:
            case 903/* System Not Initialized; Restart! */:
            case 905/* Subscription Failed; The device might need reboot! */:
            case 1020/* Couldn't create the event handler socket */:
                fatalError(status, "Exit the application; might have to reboot ISY");
                break;

            case 781/* no such session */:
            case 802/* device in error */:
            case 813/* subscription id not found */:
            case 904/* Subscription Failed! */:
            case 906/* Event received for a different subscription; Restart! */:
            case 907/* Bad Event Received */:
            case 1021/* Subscription failed: socket */:
            case 1022/* Unsubscription failed: socket */:
            case 1023/* Interrupted I/O: ProxyDevice */:
            case 1024/* I/O error: ProxyDevice */:
            case 1025/* Server socket close failed */:
            case 1026/* Server socket close failed-2 */:
            case 1050/* Socket open failed */:
            case 1051/* Socket close failed */:
            case 1100/* Couldn't open the stream */:
            case 1200/* Couldn't resolve localhost */:
            case 1301/* Device not responding */:
                offLine(status, device);
                break;

            case 604/* human intervention required */:
            case 701/* authorization failed */:
            case 1000/* XML parse error */:
            case 4000 /* No < or > in the name */:
            case 5000 /* Invalid userid/pwd */:
            case 5001 /* Invalid length */:
            case 9000 /* Invalid SSL Certificate */:
                humanInterventionRequired(status, null);
                offLine(status, device);
                break;

        }

        return false;
    }

}
