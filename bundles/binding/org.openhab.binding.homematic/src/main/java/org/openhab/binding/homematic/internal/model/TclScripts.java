/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.homematic.internal.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple class with the JAXB mapping for a list of TclRega scripts. Used to
 * load the resource homematic/tclrega-scripts.xml.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
@XmlRootElement(name = "scripts")
@XmlAccessorType(XmlAccessType.FIELD)
public class TclScripts {

    @XmlElement(name = "script")
    private List<TclScript> scripts = new ArrayList<TclScript>();

    /**
     * Returns all scripts.
     */
    public List<TclScript> getScripts() {
        return scripts;
    }

}
