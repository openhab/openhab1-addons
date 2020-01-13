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
package org.openhab.io.gcal.internal;

import org.junit.Before;
import org.junit.Test;
import org.openhab.io.gcal.internal.GCalEventDownloader.CalendarEventContent;

import junit.framework.Assert;

/**
 * @author Thomas.Eichstaedt-Engelen
 */
public class GCalEventDownloaderTest {

    GCalEventDownloader downloader;

    @Before
    public void init() {
        downloader = new GCalEventDownloader();
    }

    @Test
    public void testParseCommand() {
        CalendarEventContent content;

        content = downloader.parseEventContent("normalContent", false);
        Assert.assertEquals("normalContent", content.startCommands);
        Assert.assertEquals("", content.endCommands);
        Assert.assertEquals("", content.modifiedByEvent);

        content = downloader.parseEventContent("normalContent\nmodified by {\n\nholidays\n}", false);
        Assert.assertEquals("normalContent", content.startCommands);
        Assert.assertEquals("", content.endCommands);
        Assert.assertEquals("holidays", content.modifiedByEvent);

        content = downloader.parseEventContent("start{startCommand  }\nend\n{  endCommand\n}", false);
        Assert.assertEquals("startCommand", content.startCommands);
        Assert.assertEquals("endCommand", content.endCommands);
        Assert.assertEquals("", content.modifiedByEvent);

        content = downloader
                .parseEventContent("start{startCommand  }\nend\n{  endCommand\n}\nmodified by {\n\nholidays\n}", false);
        Assert.assertEquals("startCommand", content.startCommands);
        Assert.assertEquals("endCommand", content.endCommands);
        Assert.assertEquals("holidays", content.modifiedByEvent);

        content = downloader.parseEventContent("normalContent", true);
        Assert.assertEquals("[PresenceSimulation]" + "\n" + "normalContent", content.startCommands);
        Assert.assertEquals("", content.endCommands);
        Assert.assertEquals("", content.modifiedByEvent);

    }

}
