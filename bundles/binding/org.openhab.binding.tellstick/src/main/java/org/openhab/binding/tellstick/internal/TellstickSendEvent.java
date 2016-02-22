package org.openhab.binding.tellstick.internal;

import org.openhab.binding.tellstick.TellstickBindingConfig;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.core.types.Command;

public class TellstickSendEvent implements Comparable<TellstickSendEvent> {
    private TellstickBindingConfig config;
    private TellstickDevice dev;
    private Command command;
    private Long eventTime;

    public TellstickSendEvent(TellstickBindingConfig config, TellstickDevice dev, Command command, Long eventTime) {
        this.config = config;
        this.dev = dev;
        this.command = command;
        this.eventTime = eventTime;
    }

    public Command getCommand() {
        return command;
    }

    public TellstickBindingConfig getConfig() {
        return config;
    }

    public TellstickDevice getDev() {
        return dev;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setConfig(TellstickBindingConfig config) {
        this.config = config;
    }

    public void setDev(TellstickDevice dev) {
        this.dev = dev;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int compareTo(TellstickSendEvent o) {
        return eventTime.compareTo(o.getEventTime());
    }
}
