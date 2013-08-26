package org.openhab.binding.homematic.internal.config;

/**
 * A parameter is part of a physical device that holds a specific value. It can
 * be r/w or r/o.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public interface ParameterAddress {

    String getAsString();

    String getParameterId();

    String getDeviceId();

    String getChannelId();

}
