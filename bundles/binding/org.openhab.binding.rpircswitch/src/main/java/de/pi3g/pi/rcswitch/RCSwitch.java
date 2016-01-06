/*
 * RCSwitch - port of the Arduino libary for remote control outlet switches.
 * Arduino library is Copyright (c) 2011 Suat Özgür.  All right reserved.
 * Ported to Java by Florian Frankenberger 2013.
 *
 * Contributors for the Arduino library:
 * - Andre Koehler / info(at)tomate-online(dot)de
 * - Gordeev Andrey Vladimirovich / gordeev(at)openpyro(dot)com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package de.pi3g.pi.rcswitch;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Gpio;
import java.util.BitSet;

/**
 * Transmittes signals to 433 MHz electrical switching units. Based on the Arduino library
 * but enhanced to fit a more object oriented approach in Java.
 * <p />
 * This library is designed to be used with a RF transmitter like this
 * one: http://www.watterott.com/de/RF-Link-Sender-434MHz
 * <p />
 * Just connect the DATA IN Pin with the pin provided in the constructor. The
 * VCC with +5V (PIN2) and GND with Ground (PIN6).
 * <p />
 * Usage example:
 * <p />
 * <pre>
 * //our switching group address is 01011 (marked with 1 to 5 on the DIP switch
 * //on the switching unit itself)
 * BitSet address = RCSwitch.getSwitchGroupAddress("01011");
 *
 * RCSwitch transmitter = new RCSwitch(RaspiPin.GPIO_00);
 * transmitter.switchOn(address, 1); //switches the switch unit A (A = 1, B = 2, ...) on
 * Thread.sleep(5000); //wait 5 sec.
 * transmitter.switchOff(address, 1); //switches the switch unit A off
 * </pre>
 *
 * @author Suat Özgür
 * @author Florian Frankenberger
 */
public class RCSwitch {

    private final GpioPinDigitalOutput transmitterPin;

    private final int pulseLength = 350;
    private final int repeatTransmit = 10;

    public RCSwitch(Pin transmitterPin) {
        final GpioController gpio = GpioFactory.getInstance();
        this.transmitterPin = gpio.provisionDigitalOutputPin(transmitterPin);
    }

    /**
     * Switch a remote switch on (Type A with 10 pole DIP switches)
     *
     * @param switchGroupAddress Code of the switch group (refers to DIP switches 1..5 where
     * "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param switchCode Number of the switch itself (1..4)
     */
    public void switchOn(BitSet switchGroupAddress, int switchCode) {
        if (switchGroupAddress.length() > 5) {
            throw new IllegalArgumentException("switch group address has more than 5 bits!");
        }
        this.sendTriState(this.getCodeWordA(switchGroupAddress, switchCode, true));
    }

    /**
     * Switch a remote switch off
     *
     * @param switchGroupAddress Code of the switch group (refers to DIP switches 1..5 where
     * "1" = on and "0" = off, if all DIP switches are on it's "11111")
     * @param switchCode Number of the switch itself (1..4 for A..D)
     */
    public void switchOff(BitSet switchGroupAddress, int switchCode) {
        if (switchGroupAddress.length() > 5) {
            throw new IllegalArgumentException("switch group address has more than 5 bits!");
        }
        this.sendTriState(this.getCodeWordA(switchGroupAddress, switchCode, false));
    }

    /**
    * Switch a remote switch on (Type B with two rotary/sliding switches)
    *
    * @param nAddressCode Number of the switch group (1..4)
    * @param nChannelCode Number of the switch itself (1..4)
    */
    public void switchOn(int nAddressCode, int nChannelCode) {
            sendTriState(getCodeWordB(nAddressCode, nChannelCode, true));
    }
    /**
    * Switch a remote switch off (Type B with two rotary/sliding switches)
    *
    * @param nAddressCode Number of the switch group (1..4)
    * @param nChannelCode Number of the switch itself (1..4)
    */
    public void switchOff(int nAddressCode, int nChannelCode) {
            sendTriState(getCodeWordB(nAddressCode, nChannelCode, false));
    }

    /**
     * Like getCodeWord (Type A)
     */
    private String getCodeWordA(BitSet switchGroupAddress, int channelCode, boolean status) {
        int nReturnPos = 0;
        char[] sReturn = new char[12];

        String[] code = new String[]{"FFFFF", "0FFFF", "F0FFF", "FF0FF", "FFF0F", "FFFF0"};

        if (channelCode < 1 || channelCode > 5) {
            return "";
        }

        for (int i = 0; i < 5; i++) {
            if (!switchGroupAddress.get(i)) {
                sReturn[nReturnPos++] = 'F';
            } else {
                sReturn[nReturnPos++] = '0';
            }
        }

        for (int i = 0; i < 5; i++) {
            sReturn[nReturnPos++] = code[channelCode].charAt(i);
        }

        if (status) {
            sReturn[nReturnPos++] = '0';
            sReturn[nReturnPos++] = 'F';
        } else {
            sReturn[nReturnPos++] = 'F';
            sReturn[nReturnPos++] = '0';
        }

        return new String(sReturn);
    }


    /**
    * Returns a char[13], representing the Code Word to be send.
    * A Code Word consists of 9 address bits, 3 data bits and one sync bit but in our case only the first 8 address bits and the last 2 data bits were used.
    * A Code Bit can have 4 different states: "F" (floating), "0" (low), "1" (high), "S" (synchronous bit)
    *
    * +-------------------------------+--------------------------------+-----------------------------------------+-----------------------------------------+----------------------+------------+
    * | 4 bits address (switch group) | 4 bits address (switch number) | 1 bit address (not used, so never mind) | 1 bit address (not used, so never mind) | 2 data bits (on|off) | 1 sync bit |
    * | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 | 1=0FFF 2=F0FF 3=FF0F 4=FFF0 | F | F | on=FF off=F0 | S |
    * +-------------------------------+--------------------------------+-----------------------------------------+-----------------------------------------+----------------------+------------+
    *
    * @param nAddressCode Number of the switch group (1..4)
    * @param nChannelCode Number of the switch itself (1..4)
    * @param bStatus Wether to switch on (true) or off (false)
    *
    * @return char[13]
    */
    private String getCodeWordB(int nAddressCode, int nChannelCode,
                    boolean bStatus) {
            int nReturnPos = 0;
            char[] sReturn = new char[13];
            String[] code = new String[] { "FFFF", "0FFF", "F0FF", "FF0F", "FFF0" };
            if (nAddressCode < 1 || nAddressCode > 4 || nChannelCode < 1
                            || nChannelCode > 4) {
                    return "";
            }
            for (int i = 0; i < 4; i++) {
                    sReturn[nReturnPos++] = code[nAddressCode].charAt(i);
            }
            for (int i = 0; i < 4; i++) {
                    sReturn[nReturnPos++] = code[nChannelCode].charAt(i);
            }
            sReturn[nReturnPos++] = 'F';
            sReturn[nReturnPos++] = 'F';
            sReturn[nReturnPos++] = 'F';
            if (bStatus) {
                    sReturn[nReturnPos++] = 'F';
            } else {
                    sReturn[nReturnPos++] = '0';
            }
            return new String(sReturn);
    }

    /**
     * Sends a Code Word
     *
     * @param codeWord /^[10FS]*$/ -> see getCodeWord
     */
    private void sendTriState(String codeWord) {
        for (int nRepeat = 0; nRepeat < repeatTransmit; nRepeat++) {
            for (int i = 0; i < codeWord.length(); ++i) {
                switch (codeWord.charAt(i)) {
                    case '0':
                        this.sendT0();
                        break;
                    case 'F':
                        this.sendTF();
                        break;
                    case '1':
                        this.sendT1();
                        break;
                }
            }
            this.sendSync();
        }
    }

    /**
     * Sends a "Sync" Bit
     *                       _
     * Waveform Protocol 1: | |_______________________________
     *                       _
     * Waveform Protocol 2: | |__________
     */
    private void sendSync() {
        this.transmit(1, 31);
    }

    /**
     * Sends a Tri-State "0" Bit
     *            _     _
     * Waveform: | |___| |___
     */
    private void sendT0() {
        this.transmit(1, 3);
        this.transmit(1, 3);
    }

    /**
     * Sends a Tri-State "1" Bit
     *            ___   ___
     * Waveform: |   |_|   |_
     */
    private void sendT1() {
        this.transmit(3, 1);
        this.transmit(3, 1);
    }

    /**
     * Sends a Tri-State "F" Bit
     *            _     ___
     * Waveform: | |___|   |_
     */
    private void sendTF() {
        this.transmit(1, 3);
        this.transmit(3, 1);
    }

    private void transmit(int nHighPulses, int nLowPulses) {
        if (this.transmitterPin != null) {
            this.transmitterPin.high();
            Gpio.delayMicroseconds(this.pulseLength * nHighPulses);

            this.transmitterPin.low();
            Gpio.delayMicroseconds(this.pulseLength * nLowPulses);
        }
    }

    /**
     * convenient method to convert a string like "11011" to a BitSet.
     *
     * @param address
     * @return a bitset containing the address that can be used for switchOn()/switchOff()
     */
    public static BitSet getSwitchGroupAddress(String address) {
        if (address.length() != 5) {
            throw new IllegalArgumentException("the switchGroupAddress must consist of exactly 5 bits!");
        }
        BitSet bitSet = new BitSet(5);
        for (int i = 0; i < 5; i++) {
            bitSet.set(i, address.charAt(i) == '1');
        }
        return bitSet;
    }

}
