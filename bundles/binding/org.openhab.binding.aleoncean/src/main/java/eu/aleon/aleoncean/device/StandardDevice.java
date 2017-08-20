/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.RadioPacketADT;
import eu.aleon.aleoncean.packet.radio.RadioPacketMSC;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.RadioPacketUTE;
import eu.aleon.aleoncean.packet.radio.RadioPacketVLD;
import eu.aleon.aleoncean.packet.radio.userdata.UserData;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class StandardDevice implements Device {

    private final Logger logger = LoggerFactory.getLogger(StandardDevice.class);

    protected final DeviceParameterUpdatedSupport parameterChangedSupport;

    private final ESP3Connector conn;
    private final EnOceanId addressRemote;
    private final EnOceanId addressLocal;

    public StandardDevice(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        this.parameterChangedSupport = new DeviceParameterUpdatedSupport(this, true);
        this.conn = conn;
        this.addressRemote = addressRemote;
        this.addressLocal = addressLocal;
    }

    public ESP3Connector getConn() {
        return conn;
    }

    @Override
    public EnOceanId getAddressRemote() {
        return addressRemote;
    }

    @Override
    public EnOceanId getAddressLocal() {
        return addressLocal;
    }

    protected ResponsePacket send(final UserData userData) {
        return send(userData.generateRadioPacket());
    }

    protected ResponsePacket send(final RadioPacket packet) {
        packet.setDestinationId(getAddressRemote());
        packet.setSenderId(getAddressLocal());
        final ResponsePacket response = getConn().write(packet);
        return response;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.conn);
        hash = 97 * hash + Objects.hashCode(this.addressRemote);
        hash = 97 * hash + Objects.hashCode(this.addressLocal);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StandardDevice other = (StandardDevice) obj;
        if (!Objects.equals(this.conn, other.conn)) {
            return false;
        }
        if (!Objects.equals(this.addressRemote, other.addressRemote)) {
            return false;
        }
        if (!Objects.equals(this.addressLocal, other.addressLocal)) {
            return false;
        }

        return Objects.equals(this.parameterChangedSupport, other.parameterChangedSupport);
    }

    @Override
    public int compareTo(final Device o) {
        int comp;

        if (o == null) {
            return 1;
        }

        if (this.equals(o)) {
            return 0;
        }

        comp = getAddressRemote().compareTo(o.getAddressRemote());
        if (comp != 0) {
            return comp;
        }

        comp = getAddressLocal().compareTo(o.getAddressLocal());
        if (comp != 0) {
            return comp;
        }

        if (getClass() != o.getClass()) {
            return getClass().toString().compareTo(o.getClass().toString());
        }

        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    private void parseRadioPacketUnhandled(final RadioPacket packet) {
        logger.warn("Don't know how to handle radio choice {}. {}", String.format("0x%02X", packet.getChoice()), packet);
    }

    protected void parseRadioPacket1BS(final RadioPacket1BS packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacket4BS(final RadioPacket4BS packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacketADT(final RadioPacketADT packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacketMSC(final RadioPacketMSC packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacketRPS(final RadioPacketRPS packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacketUTE(final RadioPacketUTE packet) {
        parseRadioPacketUnhandled(packet);
    }

    protected void parseRadioPacketVLD(final RadioPacketVLD packet) {
        parseRadioPacketUnhandled(packet);
    }

    @Override
    public final void parseRadioPacket(final RadioPacket packet) {
        if (!packet.getSenderId().equals(getAddressRemote())) {
            logger.warn("Got a package that sender ID does not fit (senderId={}, expected={}). {}"+packet.getSenderId(), getAddressRemote(), packet);
        }
        if (!packet.getDestinationId().equals(getAddressLocal())) {
            logger.warn("Got a package that destination ID does not fit (destinationId={}, expected={}). {}" + packet.getDestinationId(), getAddressLocal(), packet);
        }

        if (packet instanceof RadioPacket1BS) {
            parseRadioPacket1BS((RadioPacket1BS) packet);
        } else if (packet instanceof RadioPacket4BS) {
            parseRadioPacket4BS((RadioPacket4BS) packet);
        } else if (packet instanceof RadioPacketADT) {
            parseRadioPacketADT((RadioPacketADT) packet);
        } else if (packet instanceof RadioPacketMSC) {
            parseRadioPacketMSC((RadioPacketMSC) packet);
        } else if (packet instanceof RadioPacketRPS) {
            parseRadioPacketRPS((RadioPacketRPS) packet);
        } else if (packet instanceof RadioPacketUTE) {
            parseRadioPacketUTE((RadioPacketUTE) packet);
        } else if (packet instanceof RadioPacketVLD) {
            parseRadioPacketVLD((RadioPacketVLD) packet);
        } else {
            logger.warn("Unknown radio packet choice {}. {}", String.format("0x%02X", packet.getChoice()), packet);
        }
    }

    @Override
    public final Set<DeviceParameter> getParameters() {
        final Set<DeviceParameter> params = new HashSet<>();
        fillParameters(params);
        return params;
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        throw new IllegalDeviceParameterException(String.format("Given parameter (%s) is not supported.", parameter));
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value)
            throws IllegalDeviceParameterException {
        throw new IllegalDeviceParameterException(String.format("Given parameter (%s) is not supported.", parameter));
    }

    protected abstract void fillParameters(final Set<DeviceParameter> params);

    @Override
    public final void addParameterUpdatedListener(final DeviceParameterUpdatedListener listener) {
        parameterChangedSupport.addParameterUpdatedListener(listener);
    }

    @Override
    public final void removeParameterUpdatedListener(final DeviceParameterUpdatedListener listener) {
        parameterChangedSupport.removeParameterUpdatedListener(listener);
    }

    protected final void fireParameterChanged(final DeviceParameter parameter,
            final DeviceParameterUpdatedInitiation initiation, final Object oldValue, final Object newValue) {
        parameterChangedSupport.fireParameterUpdated(parameter, initiation, oldValue, newValue);
    }

}
