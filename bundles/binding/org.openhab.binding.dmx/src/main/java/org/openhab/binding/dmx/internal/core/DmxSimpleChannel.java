/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

/**
 * DmxChannel. Represents a single dimmable channel in DMX universe.
 *
 * When the channel is switched on, it will switch on to the last know state.
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */
public class DmxSimpleChannel implements Comparable<DmxSimpleChannel> {

    public static final int MAX_CHANNEL_ID = 512;
    public static final int MAX_UNIVERSE_ID = 65535;

    private int universeId;
    private int channelId;

    /**
     * Create a new for Dmx channel
     *
     * @param universeId
     *            id of Dmx universe
     * @param channelId
     *            id of channel in universe
     */
    public DmxSimpleChannel(int universeId, int channelId) {
        setUniverseId(universeId);
        setChannelId(channelId);
    }

    /**
     * constructor with default universe
     *
     * @param channelId
     *            id of dmx channel
     */
    public DmxSimpleChannel(int channelId) {
        this(1, channelId);
    }

    /**
     * default constructor
     */
    public DmxSimpleChannel() {
        this(1, 1);
    }

    public DmxSimpleChannel(String str) {
        if (str.contains("|")) {
            String[] splitStr = str.split("|");
            setUniverseId(Integer.parseInt(splitStr[0]));
            setChannelId(Integer.parseInt(splitStr[1]));
        } else {
            setUniverseId(1);
            setChannelId(Integer.parseInt(str));
            System.out.println("channel= " + toString());
        }
    }

    /**
     * @return dmx channel id.
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * @return dmx universe id.
     */
    public int getUniverseId() {
        return universeId;
    }

    /**
     * sets this channels id
     *
     * @param channelId
     *            value of channel id
     */
    public void setChannelId(int channelId) {
        if (channelId < 1) {
            this.channelId = 1;
        } else if (channelId > MAX_CHANNEL_ID) {
            this.channelId = 512;
        } else {
            this.channelId = channelId;
        }
    }

    /**
     * sets this channels universe
     *
     * @param universeId
     *            value of universe id
     */
    public void setUniverseId(int universeId) {
        if (universeId < 1) {
            this.universeId = 1;
        } else if (universeId > MAX_UNIVERSE_ID) {
            this.universeId = 512;
        } else {
            this.universeId = universeId;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int compareTo(DmxSimpleChannel arg0) {

        if (arg0 == null) {
            return -1;
        }
        int uCompare = Integer.compare(getUniverseId(), arg0.getUniverseId());
        if (uCompare != 0) {
            return uCompare;
        } else {
            return Integer.compare(getChannelId(), arg0.getChannelId());

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%d|%d", universeId, channelId);
    }
}