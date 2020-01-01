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
package org.openhab.binding.smarthomatic;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.smarthomatic.internal.SHCMessage;
import org.openhab.binding.smarthomatic.internal.packetData.Packet;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;

/**
 * test of the different smarthomatic messages to make sure they are parsed
 * correctly
 *
 * @author jbolay
 *
 */
public class TestSHCMessage {

    private Packet packet;

    /**
     * common setup
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        File file = new File("src/main/resources/packet_layout.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(Packet.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        packet = (Packet) jaxbUnmarshaller.unmarshal(file);
    }

    /**
     * test data for the generic messages version: 0.0.0-0
     */
    @Test
    public void testGenericVersionMin() {
        String message = " PKT:SID=10;PC=165;MT=8;MGID=0;MID=1;MD=000000000000;3cf599b2";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Major", 0, ((DecimalType) values.get(0)).intValue());
        assertEquals("Minor", 0, ((DecimalType) values.get(1)).intValue());
        assertEquals("Patch", 0, ((DecimalType) values.get(2)).intValue());
        assertEquals("Hash", 0, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data for the generic messages version: 255.255.255-255 TODO: Hash
     * parsing isn't working with the max hash value of 4294967295
     */
    @Test
    public void testGenericVersionMax() {
        String message = " PKT:SID=10;PC=165;MT=8;MGID=0;MID=1;MD=FFFFFF000000FF;5f552ffb";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Major", 255, ((DecimalType) values.get(0)).intValue());
        assertEquals("Minor", 255, ((DecimalType) values.get(1)).intValue());
        assertEquals("Patch", 255, ((DecimalType) values.get(2)).intValue());
        assertEquals("Hash", 255, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data for the generic messages version: 0.255.0-255 TODO: Hash
     * parsing isn't working with the max hast value of 4294967295
     */
    @Test
    public void testGenericVersionMinMax1() {
        String message = " PKT:SID=10;PC=165;MT=8;MGID=0;MID=1;MD=00FF00000000FF;849eb439";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Major", 0, ((DecimalType) values.get(0)).intValue());
        assertEquals("Minor", 255, ((DecimalType) values.get(1)).intValue());
        assertEquals("Patch", 0, ((DecimalType) values.get(2)).intValue());
        assertEquals("Hash", 255, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data for the generic messages version: 255.0.255-0
     */
    @Test
    public void testGenericVersionMinMax2() {
        String message = " PKT:SID=10;PC=165;MT=8;MGID=0;MID=1;MD=FF00FF00000000;a72a6224";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Major", 255, ((DecimalType) values.get(0)).intValue());
        assertEquals("Minor", 0, ((DecimalType) values.get(1)).intValue());
        assertEquals("Patch", 255, ((DecimalType) values.get(2)).intValue());
        assertEquals("Hash", 0, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data for the generic messages battery: 66 %
     */
    @Test
    public void testGenericBattTyp() {
        String message = " PKT:SID=10;PC=164;MT=8;MGID=0;MID=5;MD=840000000004;9f44b737";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(66, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data for the generic messages battery: 0 %
     */
    @Test
    public void testGenericBattMin() {
        String message = " PKT:SID=10;PC=164;MT=8;MGID=0;MID=5;MD=000000000004;c6415f25";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data for the generic messages battery: 127
     */
    @Test
    public void testGenericBattMax() {
        String message = " PKT:SID=10;PC=164;MT=8;MGID=0;MID=5;MD=FE0000000004;60b90a98";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(127, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: GPIO DigitalPort: 00000000b
     */
    @Test
    public void testGPIODigitalPortAllZero() {
        String message = " PKT:SID=150;PC=1688;MT=8;MGID=1;MID=1;MD=000000000004;14f131ec";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(8, values.size());
        for (Type type : values) {
            assertEquals(OnOffType.OFF, type);
        }
    }

    /**
     * test data is: GPIO DigitalPort: 11111111b
     */
    @Test
    public void testGPIODigitalPortAllOn() {
        String message = " PKT:SID=150;PC=1688;MT=8;MGID=1;MID=1;MD=ff0000000004;6ed826ac";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(8, values.size());
        for (Type type : values) {
            assertEquals(OnOffType.ON, type);
        }
    }

    /**
     * test data is: GPIO DigitalPort: 10100101b
     */
    @Test
    public void testGPIODigitalPortAllMixed() {
        String message = " PKT:SID=150;PC=1688;MT=8;MGID=1;MID=1;MD=a50000000004;584ea808";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(8, values.size());
        assertEquals(OnOffType.ON, values.get(0));
        assertEquals(OnOffType.OFF, values.get(1));
        assertEquals(OnOffType.ON, values.get(2));
        assertEquals(OnOffType.OFF, values.get(3));
        assertEquals(OnOffType.OFF, values.get(4));
        assertEquals(OnOffType.ON, values.get(5));
        assertEquals(OnOffType.OFF, values.get(6));
        assertEquals(OnOffType.ON, values.get(7));
    }

    /**
     * test data is: GPIO AnalogPort:
     * 00000000000000000000000000000000000000000000
     */
    @Test
    public void testGPIOAnalogPortAllZero() {
        String message = " PKT:SID=11;PC=2238;MT=8;MGID=1;MID=10;MD=00000000000000000000000000000000000000000000;055a1dca";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(16, values.size());
        for (int i = 0; i < values.size(); i += 2) {
            assertEquals(OnOffType.OFF, values.get(i));
            assertEquals(0, ((DecimalType) values.get(i + 1)).intValue());
        }
    }

    /**
     * test data is: GPIO AnalogPort:
     * FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
     */
    @Test
    public void testGPIOAnalogPortAllMax() {
        String message = " PKT:SID=11;PC=2238;MT=8;MGID=1;MID=10;MD=FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF;bceeea75";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(16, values.size());
        for (int i = 0; i < values.size(); i += 2) {
            assertEquals(OnOffType.ON, values.get(i));
            assertEquals(2047, ((DecimalType) values.get(i + 1)).intValue());
        }
    }

    /**
     * test data is: GPIO AnalogPort:
     * c4c000c4c0008000010FF2FF00000000000000000000
     */
    @Test
    public void testGPIOAnalogPortAllMixed() {
        String message = " PKT:SID=11;PC=2238;MT=8;MGID=1;MID=10;MD=c4c000c4c0008000010FF2FF00000000000000000000;d4e9f531";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(16, values.size());
        assertEquals("Port0", OnOffType.ON, values.get(0));
        assertEquals("Port0", 1100, ((DecimalType) values.get(1)).intValue());
        assertEquals("Port1", OnOffType.OFF, values.get(2));
        assertEquals("Port1", 0, ((DecimalType) values.get(3)).intValue());
        assertEquals("Port2", OnOffType.ON, values.get(4));
        assertEquals("Port2", 1100, ((DecimalType) values.get(5)).intValue());
        assertEquals("Port3", OnOffType.OFF, values.get(6));
        assertEquals("Port3", 0, ((DecimalType) values.get(7)).intValue());
        assertEquals("Port4", OnOffType.ON, values.get(8));
        assertEquals("Port4", 0, ((DecimalType) values.get(9)).intValue());
        assertEquals("Port5", OnOffType.OFF, values.get(10));
        assertEquals("Port5", 1, ((DecimalType) values.get(11)).intValue());
        assertEquals("Port6", OnOffType.OFF, values.get(12));
        assertEquals("Port6", 255, ((DecimalType) values.get(13)).intValue());
        assertEquals("Port7", OnOffType.OFF, values.get(14));
        assertEquals("Port7", 767, ((DecimalType) values.get(15)).intValue());
    }

    /**
     * test data is: weather temperature: 22.10
     */
    @Test
    public void testWeatherTempTyp() {
        String message = " PKT:SID=11;PC=68026;MT=8;MGID=10;MID=1;MD=08a200000000;3bd82894";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(2210, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: weather temperature: -32768 = 0x8000 (min)
     */
    @Test
    public void testWeatherTempMin() {
        String message = " PKT:SID=11;PC=68026;MT=8;MGID=10;MID=1;MD=800000000000;da125fde";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(-32768, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: weather temperature: 32767 = 0x7FFF (max)
     */
    @Test
    public void testWeatherTempMax() {
        String message = " PKT:SID=11;PC=68026;MT=8;MGID=10;MID=1;MD=7FFF00000000;dbd5c390";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(32767, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: weather temperature & humidity: hum: 53.4 temperatur: 22.40
     */
    @Test
    public void testWeatherTempHumTyp() {
        String message = " PKT:SID=10;PC=17531;MT=8;MGID=10;MID=2;MD=858230000000;33985fff";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(534, ((DecimalType) values.get(0)).intValue());
        assertEquals(2240, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: weather temperature & humidity: 102.3 (0x3FF) temperatur:
     * -327.68 (0x8000)
     */
    @Test
    public void testWeatherTempHumMinMax1() {
        String message = " PKT:SID=10;PC=17531;MT=8;MGID=10;MID=2;MD=FFE000000000;28908fc3";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(1023, ((DecimalType) values.get(0)).intValue());
        assertEquals(-32768, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: weather temperature & humidity: 0 (0x000) temperatur: 32767
     * (0x7FFF)
     */
    @Test
    public void testWeatherTempHumMinMax2() {
        String message = " PKT:SID=10;PC=17531;MT=8;MGID=10;MID=2;MD=001FFFC00000;785c34de";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
        assertEquals(32767, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: weather barometric pressure: 96461 temp: -1
     */
    @Test
    public void testWeatherBaroTempTyp() {
        String message = " PKT:SID=11;PC=68029;MT=8;MGID=10;MID=3;MD=bc75ffff8000;b8d3498b";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(96491, ((DecimalType) values.get(0)).intValue());
        assertEquals(-1, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: weather barometric pressure: 0 temp: -32768
     */
    @Test
    public void testWeatherBaroTempMin() {
        String message = " PKT:SID=11;PC=68029;MT=8;MGID=10;MID=3;MD=000040000000;30bd729d";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
        assertEquals(-32768, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: weather barometric pressure: 131071 temp: 32767
     */
    @Test
    public void testWeatherBaroTempMax() {
        String message = " PKT:SID=11;PC=68029;MT=8;MGID=10;MID=3;MD=FFFFBFFFFFFF;a6819187";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(131071, ((DecimalType) values.get(0)).intValue());
        assertEquals(32767, ((DecimalType) values.get(1)).intValue());
    }

    /**
     * test data is: environment brightness: 37 % (typ)
     */
    @Test
    public void testEnvBrightnessTyp() {
        String message = " PKT:SID=10;PC=123;MT=8;MGID=11;MID=1;MD=4a0000000006;d6a695c8";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(37, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: environment brightness: 0 % (min)
     */
    @Test
    public void testEnvBrightnessMin() {
        String message = " PKT:SID=10;PC=123;MT=8;MGID=11;MID=1;MD=000000000006;b1c1e93d";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: environment brightness: 100 % (max)
     */
    @Test
    public void testEnvBrightnessMax() {
        String message = " PKT:SID=10;PC=123;MT=8;MGID=11;MID=1;MD=FE0000000006;1739bc80";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(127, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: environment distance: 117 cm (typ)
     */
    @Test
    public void testEnvDistanceTyp() {
        String message = " PKT:SID=11;PC=68047;MT=8;MGID=11;MID=2;MD=01d400000000;aabb6194";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(117, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: enviroment distance: 0 cm (min)
     */
    @Test
    public void testEnvDistanceMin() {
        String message = " PKT:SID=11;PC=68047;MT=8;MGID=11;MID=2;MD=000000000000;f0922327";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: enviroment distance: 16383 cm (max)
     */
    @Test
    public void testEnvDistanceMax() {
        String message = " PKT:SID=11;PC=68047;MT=8;MGID=11;MID=2;MD=FFFC00000000;2a1d0241";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(16383, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Brightness: 13 (typ)
     */
    @Test
    public void testDimmerBrightnessTyp() {
        String message = " PKT:SID=13;PC=301;MT=8;MGID=60;MID=1;MD=1A0000000004;57608c20";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(13, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Brightness: 0 (min)
     */
    @Test
    public void testDimmerBrightnessMin() {
        String message = " PKT:SID=13;PC=301;MT=8;MGID=60;MID=1;MD=000000000004;3519983e";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Brightness: 100 (max)
     */
    @Test
    public void testDimmerBrightnessMax() {
        String message = " PKT:SID=13;PC=301;MT=8;MGID=60;MID=1;MD=C80000000004;342bfe8a";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(100, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Animation: 13 (typ)
     */
    @Test
    public void testDimmerAnimationTyp() {
        String message = " PKT:SID=13;PC=534;MT=10;MGID=60;MID=2;MD=40f4006000000000000000000000000000;5817ba52";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("AnimationMode", 1, ((DecimalType) values.get(0)).intValue());
        assertEquals("TimeoutSec", 976, ((DecimalType) values.get(1)).intValue());
        assertEquals("StartBrightness", 0, ((DecimalType) values.get(2)).intValue());
        assertEquals("EndBrightness", 96, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data is: Dimmer Animation: 0 (min)
     */
    @Test
    public void testDimmerAnimationMin() {
        String message = " PKT:SID=13;PC=534;MT=10;MGID=60;MID=2;MD=0000000000000000000000000000000000;8e4c4091";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("AnimationMode", 0, ((DecimalType) values.get(0)).intValue());
        assertEquals("TimeoutSec", 0, ((DecimalType) values.get(1)).intValue());
        assertEquals("StartBrightness", 0, ((DecimalType) values.get(2)).intValue());
        assertEquals("EndBrightness", 0, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data is: Dimmer Animation: 100 (max)
     */
    @Test
    public void testDimmerAnimationMax() {
        String message = " PKT:SID=13;PC=534;MT=10;MGID=60;MID=2;MD=7ffff20000000000000000000000000000;c5c81435";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("AnimationMode", 1, ((DecimalType) values.get(0)).intValue());
        assertEquals("TimeoutSec", 65535, ((DecimalType) values.get(1)).intValue());
        assertEquals("StartBrightness", 100, ((DecimalType) values.get(2)).intValue());
        assertEquals("EndBrightness", 0, ((DecimalType) values.get(3)).intValue());
    }

    /**
     * test data is: Dimmer Color: 13 (typ)
     */
    @Test
    public void testDimmerColorTyp() {
        String message = " PKT:SID=12;PC=203;MT=8;MGID=60;MID=10;MD=340000000003;3b0b8922";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(13, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Color: 0 (min)
     */
    @Test
    public void testDimmerColorMin() {
        String message = " PKT:SID=12;PC=203;MT=8;MGID=60;MID=10;MD=000000000003;85c7de93";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(0, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Color: 63 (max)
     */
    @Test
    public void testDimmerColorMax() {
        String message = " PKT:SID=12;PC=203;MT=8;MGID=60;MID=10;MD=FC0000000003;cc022ece";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals(63, ((DecimalType) values.get(0)).intValue());
    }

    /**
     * test data is: Dimmer Color Animation: (typ)
     */
    @Test
    public void testDimmerColorAnimationTyp() {
        String message = "PKT:SID=12;PC=9417;MT=10;MGID=60;MID=11;MD=1230418000000000000000000000000000;68ddbb91";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Repeat", 1, ((DecimalType) values.get(0)).intValue());
        assertEquals("AutoReverse", OnOffType.OFF, (values.get(1)));
        assertEquals("Time0", 8, ((DecimalType) values.get(2)).intValue());
        assertEquals("Color0", 48, ((DecimalType) values.get(3)).intValue());
        assertEquals("Time1", 8, ((DecimalType) values.get(4)).intValue());
        assertEquals("Color1", 12, ((DecimalType) values.get(5)).intValue());
    }

    /**
     * test data is: Dimmer Color Animation: (min)
     */
    @Test
    public void testDimmerColorAnimationMin() {
        String message = "PKT:SID=12;PC=9417;MT=10;MGID=60;MID=11;MD=0000000000000000000000000000000000;1c733ace";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Repeat", 0, ((DecimalType) values.get(0)).intValue());
        assertEquals("AutoReverse", OnOffType.OFF, (values.get(1)));
        assertEquals("Time0", 0, ((DecimalType) values.get(2)).intValue());
        assertEquals("Color0", 0, ((DecimalType) values.get(3)).intValue());
        assertEquals("Time1", 0, ((DecimalType) values.get(4)).intValue());
        assertEquals("Color1", 0, ((DecimalType) values.get(5)).intValue());
    }

    /**
     * test data is: Dimmer Color Animation: (max)
     */
    @Test
    public void testDimmerColorAnimationMax() {
        String message = "PKT:SID=12;PC=9417;MT=10;MGID=60;MID=11;MD=FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00;369ec114";
        SHCMessage shcMessage = new SHCMessage(message, packet);
        List<Type> values = shcMessage.getData().getOpenHABTypes();
        assertEquals("Repeat", 15, ((DecimalType) values.get(0)).intValue());
        assertEquals("AutoReverse", OnOffType.ON, (values.get(1)));
        assertEquals("Time0", 31, ((DecimalType) values.get(2)).intValue());
        assertEquals("Color0", 63, ((DecimalType) values.get(3)).intValue());
        assertEquals("Time1", 31, ((DecimalType) values.get(4)).intValue());
        assertEquals("Color1", 63, ((DecimalType) values.get(5)).intValue());
    }
}
