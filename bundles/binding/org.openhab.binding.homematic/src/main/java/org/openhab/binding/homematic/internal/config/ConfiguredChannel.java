package org.openhab.binding.homematic.internal.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredChannel {

    @XmlAttribute
    private String name;

    @XmlElement(name = "parameter")
    private List<ConfiguredParameter> parameter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConfiguredParameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<ConfiguredParameter> parameter) {
        this.parameter = parameter;
    }

}
