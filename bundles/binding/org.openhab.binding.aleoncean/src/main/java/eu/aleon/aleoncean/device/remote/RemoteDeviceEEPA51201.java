package eu.aleon.aleoncean.device.remote;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedInitiation;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.device.RemoteDevice;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA51201;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

public class RemoteDeviceEEPA51201 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA51201.class);

    private Double energyWS;
    private Double powerW;

    public RemoteDeviceEEPA51201(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public Double getEnergy() {
        return energyWS;
    }

    public void setEnergy(final DeviceParameterUpdatedInitiation initiation, final Double energy) {
        final Double oldEnergy = this.energyWS;
        this.energyWS = energy;
        fireParameterChanged(DeviceParameter.ENERGY_WS, initiation, oldEnergy, energy);
    }

    public Double getPower() {
        return powerW;
    }

    public void setPower(final DeviceParameterUpdatedInitiation initiation, final Double power) {
        final Double oldPower = this.powerW;
        this.powerW = power;
        fireParameterChanged(DeviceParameter.POWER_W, initiation, oldPower, power);
    }

    protected void parseRadioPacket4BS(final RadioPacket4BS packet) {
        if (packet.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }

        final UserDataEEPA51201 userData = new UserDataEEPA51201(packet.getUserDataRaw());

        switch (userData.getUnit()) {
            case ENERGY_KWH:
                setEnergy(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getMeterReading() * 3600 * 1000);
                break;
            case POWER_W:
                setPower(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getMeterReading());
                break;
            default:
                LOGGER.warn("Unhandled unit '{}'.", userData.getUnit());
                break;
        }
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.ENERGY_WS);
        params.add(DeviceParameter.POWER_W);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case ENERGY_WS:
                return getEnergy();
            case POWER_W:
                return getPower();
            default:
                return super.getByParameter(parameter);
        }
    }

}
