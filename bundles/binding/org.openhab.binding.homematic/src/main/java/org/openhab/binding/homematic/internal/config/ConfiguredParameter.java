package org.openhab.binding.homematic.internal.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfiguredParameter {

    @XmlAttribute
    private String name;

    @XmlElement(name = "converter")
    private List<ConfiguredConverter> converter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConfiguredConverter> getConverter() {
        return converter;
    }

    public void setConverter(List<ConfiguredConverter> converter) {
        this.converter = converter;
    }

}
