/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import org.openhab.binding.souliss.SoulissBindingProvider;
import org.openhab.binding.souliss.internal.network.typicals.Constants;
import org.openhab.binding.souliss.internal.network.typicals.Monitor;
import org.openhab.binding.souliss.internal.network.typicals.RefreshHEALTY;
import org.openhab.binding.souliss.internal.network.typicals.RefreshSUBSCRIPTION;
import org.openhab.binding.souliss.internal.network.typicals.SoulissGenericTypical;
import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT11;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT12;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT14;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT16;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT18;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT19;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT21;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT22;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT31;
import org.openhab.binding.souliss.internal.network.typicals.StateTraslator;
import org.openhab.binding.souliss.internal.network.udp.HalfFloatUtils;
import org.openhab.binding.souliss.internal.network.udp.SendDispatcher;
import org.openhab.binding.souliss.internal.network.udp.UDPServerThread;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class load from openhab.cfg all configuration parameters
 * Receive Command from OpenHAB, translate and send it to Souliss
 *
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissBinding<E> extends AbstractActiveBinding<SoulissBindingProvider>implements ManagedService {

    private static Logger logger = LoggerFactory.getLogger(SoulissBinding.class);
    Monitor mon;
    SendDispatcher sendDisp;
    UDPServerThread UDP_Server;
    RefreshSUBSCRIPTION susbcription;
    RefreshHEALTY healty;
    private static final int OH_REFRESH_TIME = 50;
    long start_time = System.currentTimeMillis();

    Timers timers = new Timers(4);

    protected void addBindingProvider(SoulissBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(SoulissBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * Read parameters from cfg file
     *
     * @author Tonino Fazio
     * @since 1.7.0
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            Enumeration<String> enumConfig = config.keys();

            while (enumConfig.hasMoreElements()) {
                String sName = enumConfig.nextElement();
                logger.info("PARAMETER: {} = {}", sName, config.get(sName));
                if (sName.equals("IP_LAN")) {
                    SoulissNetworkParameter.IPAddressOnLAN = (String) config.get(sName);
                } else if (sName.equals("REFRESH_DBSTRUCT_TIME")) {
                    SoulissNetworkParameter.REFRESH_DBSTRUCT_TIME = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("REFRESH_SUBSCRIPTION_TIME")) {
                    SoulissNetworkParameter.REFRESH_SUBSCRIPTION_TIME = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("REFRESH_HEALTY_TIME")) {
                    SoulissNetworkParameter.REFRESH_HEALTY_TIME = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("REFRESH_MONITOR_TIME")) {
                    SoulissNetworkParameter.REFRESH_MONITOR_TIME = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("SEND_DELAY")) {
                    SoulissNetworkParameter.SEND_DELAY = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("SEND_MIN_DELAY")) {
                    SoulissNetworkParameter.SEND_MIN_DELAY = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("SECURE_SEND_TIMEOUT_TO_REQUEUE")) {
                    SoulissNetworkParameter.SECURE_SEND_TIMEOUT_TO_REQUEUE = Long.parseLong((String) config.get(sName));
                } else if (sName.equals("SECURE_SEND_TIMEOUT_TO_REMOVE_PACKET")) {
                    SoulissNetworkParameter.SECURE_SEND_TIMEOUT_TO_REMOVE_PACKET = Long
                            .parseLong((String) config.get(sName));
                } else if (sName.equals("USER_INDEX")) {
                    SoulissNetworkParameter.UserIndex = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("NODE_INDEX")) {
                    SoulissNetworkParameter.NodeIndex = Integer.parseInt((String) config.get(sName));
                } else if (sName.equals("SERVERPORT")) {
                    if (config.get(sName).equals("")) {
                        SoulissNetworkParameter.serverPort = null;
                    } else {
                        SoulissNetworkParameter.serverPort = Integer.parseInt((String) config.get(sName));
                    }
                }

            }
        }
        initialize();
        setProperlyConfigured(true);
    }

    @Override
    /**
     * Get the souliss's typical from the hash table and send a command
     *
     * @author Tonino Fazio
     * @since 1.7.0
     */
    public void receiveCommand(String itemName, Command command) {

        // Get the typical defined in the hash table
        SoulissGenericTypical T = SoulissGenericBindingProvider.SoulissTypicalsRecipients.getTypicalFromItem(itemName);
        if (T == null) {
            logger.debug("receiveCommand - itemName '{}' not a SoulissTypical", itemName);
            return;
        }
        logger.info("receiveCommand - {} = {} - Typical: 0x{}", itemName, command, Integer.toHexString(T.getType()));

        switch (T.getType()) {
            case Constants.Souliss_T11:
                SoulissT11 T11 = (SoulissT11) T;
                T11.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                break;
            case Constants.Souliss_T12:
                SoulissT12 T12 = (SoulissT12) T;

                if (itemName.equals(T12.getsItemNameAutoModeValue())) {
                    T12.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T12_Use_Of_Slot_AUTOMODE + "_" + command.toString()));
                } else if (itemName.equals(T12.getsItemNameSwitchValue())) {
                    T12.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T12_Use_Of_Slot_SWITCH + "_" + command.toString()));
                }

                break;
            case Constants.Souliss_T14:
                SoulissT14 T14 = (SoulissT14) T;
                T14.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                break;
            case Constants.Souliss_T18:
                SoulissT18 T18 = (SoulissT18) T;
                T18.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                break;
            case Constants.Souliss_T16:
                SoulissT16 T16 = (SoulissT16) T;
                String cmd = command.getClass().getSimpleName();
                if (cmd.equals(Constants.Openhab_RGB_TYPE)) {
                    String HSB[] = command.toString().split(",");

                    short RGB[] = HSBtoRGB(Float.parseFloat(HSB[0]), Float.parseFloat(HSB[1]),
                            Float.parseFloat(HSB[2]));

                    T16.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.getClass().getSimpleName()),
                            RGB[0], RGB[1], RGB[2]);
                } else {
                    T16.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                }
                break;
            case Constants.Souliss_T19:
                SoulissT19 T19 = (SoulissT19) T;
                if (command instanceof PercentType) {
                    int percentToShort = (((PercentType) command).shortValue() * 254 / 100);
                    T19.commandSEND(Constants.Souliss_T1n_Set, Short.parseShort(String.valueOf(percentToShort)));
                } else if (command instanceof DecimalType) {
                    int decimalToShort = (((DecimalType) command).shortValue() * 254 / 100);
                    T19.commandSEND(Constants.Souliss_T1n_Set, Short.parseShort(String.valueOf(decimalToShort)));
                } else {
                    T19.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                }
                break;
            case Constants.Souliss_T21:
                SoulissT21 T21 = (SoulissT21) T;
                T21.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                break;
            case Constants.Souliss_T22:
                SoulissT22 T22 = (SoulissT22) T;
                T22.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
                break;

            case Constants.Souliss_T31:
                SoulissT31 T31 = (SoulissT31) T;
                // Setpoint
                if (itemName.equals(T31.getsItemNameSetpointValue())) {
                    if (command instanceof DecimalType) {
                        int uu = HalfFloatUtils.fromFloat(((DecimalType) command).floatValue());
                        byte B2 = (byte) (uu >> 8);
                        byte B1 = (byte) uu;
                        // setpoint command
                        T31.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                                Constants.Souliss_T31_Use_Of_Slot_SETPOINT_COMMAND), B1, B2);
                    }
                }
                // Set As Measured
                else if (itemName.equals(T31.setAsMeasured.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_SETASMEASURED + "_" + command.toString()));
                } else if (itemName.equals(T31.heatingCoolingModeValue.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_HEATING_COOLING + "_" + command.toString()));
                } else if (itemName.equals(T31.fanAutoMode.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_FANAUTOMODE + "_" + command.toString()));
                } else if (itemName.equals(T31.fanOff.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_FANOFF + "_" + command.toString()));
                } else if (itemName.equals(T31.fanLow.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_FANLOW + "_" + command.toString()));
                } else if (itemName.equals(T31.fanMed.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_FANMED + "_" + command.toString()));
                } else if (itemName.equals(T31.fanHigh.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_FANHIGH + "_" + command.toString()));
                } else if (itemName.equals(T31.power.getName())) {
                    T31.commandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),
                            Constants.Souliss_T31_Use_Of_Slot_POWER + "_" + command.toString()));
                }
                break;

            default:
                logger.debug("Typical Unknown");
        }
    }

    /**
     * Convert color format from HSB to RGB
     *
     * @param H
     * @param S
     * @param B
     * @return short RGBList[] contain RGB components
     */
    private short[] HSBtoRGB(Float H, Float S, Float B) {
        short RGBList[] = hsvToRgb(H, S, B);
        return RGBList;
    }

    public short[] hsvToRgb(float H, float S, float V) {
        float R, G, B;

        H /= 360f;
        S /= 100f;
        V /= 100f;

        if (S == 0) {
            R = V * 255;
            G = V * 255;
            B = V * 255;
        } else {
            float var_h = H * 6;
            if (var_h == 6) {
                var_h = 0; // H must be < 1
            }
            int var_i = (int) Math.floor(var_h); // Or ... var_i =
                                                 // floor( var_h )
            float var_1 = V * (1 - S);
            float var_2 = V * (1 - S * (var_h - var_i));
            float var_3 = V * (1 - S * (1 - (var_h - var_i)));

            float var_r;
            float var_g;
            float var_b;
            if (var_i == 0) {
                var_r = V;
                var_g = var_3;
                var_b = var_1;
            } else if (var_i == 1) {
                var_r = var_2;
                var_g = V;
                var_b = var_1;
            } else if (var_i == 2) {
                var_r = var_1;
                var_g = V;
                var_b = var_3;
            } else if (var_i == 3) {
                var_r = var_1;
                var_g = var_2;
                var_b = V;
            } else if (var_i == 4) {
                var_r = var_3;
                var_g = var_1;
                var_b = V;
            } else {
                var_r = V;
                var_g = var_1;
                var_b = var_2;
            }

            R = var_r * 255; // RGB results from 0 to 255
            G = var_g * 255;
            B = var_b * 255;
        }

        short RGBList[] = { (short) R, (short) G, (short) B };
        return RGBList;
    }

    /**
     * Start threads and prepare other functionality (used with Binding.execute()
     */
    private void initialize() {
        logger.info("START");
        try {
            // Start listening on the UDP socket
            UDPServerThread UDP_Server = null;
            UDP_Server = new UDPServerThread(SoulissGenericBindingProvider.SoulissTypicalsRecipients);
            UDP_Server.start();

            // Start the thread that send back to openHAB the souliss'
            // typical values
            mon = new Monitor(SoulissGenericBindingProvider.SoulissTypicalsRecipients,
                    SoulissNetworkParameter.REFRESH_MONITOR_TIME, eventPublisher);

            // Start the thread that send network packets to the Souliss
            // network
            sendDisp = new SendDispatcher(SoulissGenericBindingProvider.SoulissTypicalsRecipients,
                    SoulissNetworkParameter.SEND_DELAY, SoulissNetworkParameter.SEND_MIN_DELAY);

            // Start the thread that subscribe data from the Souliss network
            susbcription = new RefreshSUBSCRIPTION(UDP_Server.getSocket(), SoulissNetworkParameter.IPAddressOnLAN);
            healty = new RefreshHEALTY(UDP_Server.getSocket(), SoulissNetworkParameter.IPAddressOnLAN);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void execute() {

        if (timers.checkTime(0, SoulissNetworkParameter.REFRESH_MONITOR_TIME)) {
            mon.tick();
            timers.resetTime(0);
        }
        if (timers.checkTime(1, SoulissNetworkParameter.SEND_MIN_DELAY)) {
            sendDisp.tick();
            timers.resetTime(1);
        }

        if (timers.checkTime(2, SoulissNetworkParameter.REFRESH_SUBSCRIPTION_TIME)) {
            susbcription.tick();
            timers.resetTime(2);
        }
        if (timers.checkTime(3, SoulissNetworkParameter.REFRESH_HEALTY_TIME)) {
            healty.tick();
            timers.resetTime(3);
        }

    }

    // timer for execution of code in Binding.execute()
    private class Timers {
        long[] timersArray;

        public Timers(int nrTimers) {
            timersArray = new long[nrTimers];
        }

        private boolean checkTime(int iNrTimer, long t) {
            // return true when code can be executed
            return (System.currentTimeMillis() - timersArray[iNrTimer] > t);
        }

        private void resetTime(int iNrTimer) {
            timersArray[iNrTimer] = System.currentTimeMillis();
        }

    }

    @Override
    protected long getRefreshInterval() {
        return OH_REFRESH_TIME;
    }

    @Override
    protected String getName() {
        return "Souliss Refresh Service";
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        // TODO Auto-generated method stub
        super.bindingChanged(provider, itemName);
        this.addBindingProvider(provider);
    }

}
