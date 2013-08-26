package org.openhab.binding.homematic.internal.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredDevice {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    @XmlElement(name = "channel")
    private List<ConfiguredChannel> channels;

    public List<ConfiguredChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<ConfiguredChannel> channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
