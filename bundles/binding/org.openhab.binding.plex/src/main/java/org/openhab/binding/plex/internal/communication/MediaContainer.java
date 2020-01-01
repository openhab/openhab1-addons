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
package org.openhab.binding.plex.internal.communication;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This object is fetched from the various Plex status URL's.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "MediaContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class MediaContainer {

    @XmlElement(name = "Server")
    private List<Server> servers = new ArrayList<Server>();

    @XmlElement(name = "Device")
    private List<Device> devices = new ArrayList<Device>();

    @XmlElement(name = "Video")
    private List<Video> videos = new ArrayList<Video>();

    @XmlElement(name = "Track")
    private List<Track> tracks = new ArrayList<Track>();

    @XmlAttribute
    private String size;

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Server getServer(String machineId) {
        if (servers != null) {
            for (Server server : servers) {
                if (server.getMachineIdentifier().equals(machineId)) {
                    return server;
                }
            }
        }

        return null;
    }
}
