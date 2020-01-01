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
package org.openhab.binding.novelanheatpump;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid commands which could be processed by this binding
 *
 * @author Jan-Philipp Bolle
 * @since 1.0.0
 */
public enum HeatpumpCommandType {

    // in german Außentemperatur
    TYPE_TEMPERATURE_OUTSIDE {
        {
            command = "temperature_outside";
            itemClass = NumberItem.class;
        }
    },

    // in german Außentemperatur
    TYPE_TEMPERATURE_OUTSIDE_AVG {
        {
            command = "temperature_outside_avg";
            itemClass = NumberItem.class;
        }
    },

    // in german Rücklauf
    TYPE_TEMPERATURE_RETURN {
        {
            command = "temperature_return";
            itemClass = NumberItem.class;
        }
    },

    // in german Rücklauf Soll
    TYPE_TEMPERATURE_REFERENCE_RETURN {
        {
            command = "temperature_reference_return";
            itemClass = NumberItem.class;
        }
    },

    // in german Vorlauf
    TYPE_TEMPERATURE_SUPPLAY {
        {
            command = "temperature_supplay";
            itemClass = NumberItem.class;
        }
    },

    // in german Brauchwasser Soll
    TYPE_TEMPERATURE_SERVICEWATER_REFERENCE {
        {
            command = "temperature_servicewater_reference";
            itemClass = NumberItem.class;
        }
    },

    // in german Brauchwasser Ist
    TYPE_TEMPERATURE_SERVICEWATER {
        {
            command = "temperature_servicewater";
            itemClass = NumberItem.class;
        }
    },

    TYPE_HEATPUMP_STATE {
        {
            command = "state";
            itemClass = StringItem.class;
        }
    },

    TYPE_HEATPUMP_SIMPLE_STATE {
        {
            command = "simple_state";
            itemClass = StringItem.class;
        }
    },

    TYPE_HEATPUMP_SIMPLE_STATE_NUM {
        {
            command = "simple_state_num";
            itemClass = NumberItem.class;
        }
    },

    TYPE_HEATPUMP_SWITCHOFF_REASON_0 {
        {
            command = "switchoff_reason_0";
            itemClass = NumberItem.class;
        }
    },

    TYPE_HEATPUMP_SWITCHOFF_CODE_0 {
        {
            command = "switchoff_code_0";
            itemClass = NumberItem.class;
        }
    },

    TYPE_HEATPUMP_EXTENDED_STATE {
        {
            command = "extended_state";
            itemClass = StringItem.class;
        }
    },

    TYPE_HEATPUMP_SOLAR_COLLECTOR {
        {
            command = "temperature_solar_collector";
            itemClass = NumberItem.class;
        }
    },

    // in german Temperatur Heissgas
    TYPE_TEMPERATURE_HOT_GAS {
        {
            command = "temperature_hot_gas";
            itemClass = NumberItem.class;
        }
    },

    // in german Sondentemperatur WP Eingang
    TYPE_TEMPERATURE_PROBE_IN {
        {
            command = "temperature_probe_in";
            itemClass = NumberItem.class;
        }
    },

    // in german Sondentemperatur WP Ausgang
    TYPE_TEMPERATURE_PROBE_OUT {
        {
            command = "temperature_probe_out";
            itemClass = NumberItem.class;
        }
    },

    // in german Vorlauftemperatur MK1 IST
    TYPE_TEMPERATURE_MK1 {
        {
            command = "temperature_mk1";
            itemClass = NumberItem.class;
        }
    },

    // in german Vorlauftemperatur MK1 SOLL
    TYPE_TEMPERATURE_MK1_REFERENCE {
        {
            command = "temperature_mk1_reference";
            itemClass = NumberItem.class;
        }
    },

    // in german Vorlauftemperatur MK1 IST
    TYPE_TEMPERATURE_MK2 {
        {
            command = "temperature_mk2";
            itemClass = NumberItem.class;
        }
    },

    // in german Vorlauftemperatur MK1 SOLL
    TYPE_TEMPERATURE_MK2_REFERENCE {
        {
            command = "temperature_mk2_reference";
            itemClass = NumberItem.class;
        }
    },

    // in german Temperatur externe Energiequelle
    TYPE_TEMPERATURE_EXTERNAL_SOURCE {
        {
            command = "temperature_external_source";
            itemClass = NumberItem.class;
        }
    },

    // in german Betriebsstunden Verdichter1
    TYPE_HOURS_COMPRESSOR1 {
        {
            command = "hours_compressor1";
            itemClass = StringItem.class;
        }
    },

    // in german Impulse (Starts) Verdichter 1
    TYPE_STARTS_COMPRESSOR1 {
        {
            command = "starts_compressor1";
            itemClass = NumberItem.class;
        }
    },

    // in german Betriebsstunden Verdichter2
    TYPE_HOURS_COMPRESSOR2 {
        {
            command = "hours_compressor2";
            itemClass = StringItem.class;
        }
    },

    // in german Impulse (Starts) Verdichter 2
    TYPE_STARTS_COMPRESSOR2 {
        {
            command = "starts_compressor2";
            itemClass = NumberItem.class;
        }
    },

    // Temperatur_TRL_ext
    TYPE_TEMPERATURE_OUT_EXTERNAL {
        {
            command = "temperature_out_external";
            itemClass = NumberItem.class;
        }
    },

    // in german Betriebsstunden ZWE1
    TYPE_HOURS_ZWE1 {
        {
            command = "hours_zwe1";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden ZWE1
    TYPE_HOURS_ZWE2 {
        {
            command = "hours_zwe2";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden ZWE1
    TYPE_HOURS_ZWE3 {
        {
            command = "hours_zwe3";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden Wärmepumpe
    TYPE_HOURS_HETPUMP {
        {
            command = "hours_heatpump";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden Heizung
    TYPE_HOURS_HEATING {
        {
            command = "hours_heating";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden Brauchwasser
    TYPE_HOURS_WARMWATER {
        {
            command = "hours_warmwater";
            itemClass = StringItem.class;
        }
    },

    // in german Betriebsstunden Brauchwasser
    TYPE_HOURS_COOLING {
        {
            command = "hours_cooling";
            itemClass = StringItem.class;
        }
    },

    // in german Waermemenge Heizung
    TYPE_THERMALENERGY_HEATING {
        {
            command = "thermalenergy_heating";
            itemClass = NumberItem.class;
        }
    },

    // in german Waermemenge Brauchwasser
    TYPE_THERMALENERGY_WARMWATER {
        {
            command = "thermalenergy_warmwater";
            itemClass = NumberItem.class;
        }
    },

    // in german Waermemenge Schwimmbad
    TYPE_THERMALENERGY_POOL {
        {
            command = "thermalenergy_pool";
            itemClass = NumberItem.class;
        }
    },

    // in german Waermemenge gesamt seit Reset
    TYPE_THERMALENERGY_TOTAL {
        {
            command = "thermalenergy_total";
            itemClass = NumberItem.class;
        }
    },

    // in german Massentrom
    TYPE_MASSFLOW {
        {
            command = "massflow";
            itemClass = NumberItem.class;
        }
    },

    TYPE_HEATPUMP_SOLAR_STORAGE {
        {
            command = "temperature_solar_storage";
            itemClass = NumberItem.class;
        }
    },

    // in german Heizung Betriebsart
    TYPE_HEATING_OPERATION_MODE {
        {
            command = "heating_operation_mode";
            itemClass = NumberItem.class;
        }
    },
    // in german Heizung Temperatur (Parallelverschiebung)
    TYPE_HEATING_TEMPERATURE {
        {
            command = "heating_temperature";
            itemClass = NumberItem.class;
        }
    },
    // in german Warmwasser Betriebsart
    TYPE_WARMWATER_OPERATION_MODE {
        {
            command = "warmwater_operation_mode";
            itemClass = NumberItem.class;
        }
    },
    // in german Warmwasser Temperatur
    TYPE_WARMWATER_TEMPERATURE {
        {
            command = "warmwater_temperature";
            itemClass = NumberItem.class;
        }
    },
    // in german Comfort Kühlung Betriebsart
    TYPE_COOLING_OPERATION_MODE {
        {
            command = "cooling_operation_mode";
            itemClass = NumberItem.class;
        }
    },
    // in german Comfort Kühlung AT-Freigabe
    TYPE_COOLING_RELEASE_TEMPERATURE {
        {
            command = "cooling_release_temperature";
            itemClass = NumberItem.class;
        }
    },
    // in german Solltemp MK1
    TYPE_COOLING_INLET_TEMP {
        {
            command = "cooling_inlet_temperature";
            itemClass = NumberItem.class;
        }
    },
    // in german AT-Überschreitung
    TYPE_COOLING_START_AFTER_HOURS {
        {
            command = "cooling_start_hours";
            itemClass = NumberItem.class;
        }
    },
    // in german AT-Unterschreitung
    TYPE_COOLING_STOP_AFTER_HOURS {
        {
            command = "cooling_stop_hours";
            itemClass = NumberItem.class;
        }
    },
    // in german AV (Abtauventil)
    TYPE_OUTPUT_AV {
        {
            command = "output_av";
            itemClass = SwitchItem.class;
        }
    },
    // in german BUP (Brauchwasserpumpe/Umstellventil)
    TYPE_OUTPUT_BUP {
        {
            command = "output_bup";
            itemClass = SwitchItem.class;
        }
    },
    // in german HUP (Heizungsumwälzpumpe)
    TYPE_OUTPUT_HUP {
        {
            command = "output_hup";
            itemClass = SwitchItem.class;
        }
    },
    // in german MA1 (Mischkreis 1 auf)
    TYPE_OUTPUT_MA1 {
        {
            command = "output_ma1";
            itemClass = SwitchItem.class;
        }
    },
    // in german MZ1 (Mischkreis 1 zu)
    TYPE_OUTPUT_MZ1 {
        {
            command = "output_mz1";
            itemClass = SwitchItem.class;
        }
    },
    // in german VEN (Ventilation/Lüftung)
    TYPE_OUTPUT_VEN {
        {
            command = "output_ven";
            itemClass = SwitchItem.class;
        }
    },
    // in german VBO (Solepumpe/Ventilator)
    TYPE_OUTPUT_VBO {
        {
            command = "output_vbo";
            itemClass = SwitchItem.class;
        }
    },
    // in german VD1 (Verdichter 1)
    TYPE_OUTPUT_VD1 {
        {
            command = "output_vd1";
            itemClass = SwitchItem.class;
        }
    },
    // in german VD2 (Verdichter 2)
    TYPE_OUTPUT_VD2 {
        {
            command = "output_vd2";
            itemClass = SwitchItem.class;
        }
    },
    // in german ZIP (Zirkulationspumpe)
    TYPE_OUTPUT_ZIP {
        {
            command = "output_zip";
            itemClass = SwitchItem.class;
        }
    },
    // in german ZUP (Zusatzumwälzpumpe)
    TYPE_OUTPUT_ZUP {
        {
            command = "output_zup";
            itemClass = SwitchItem.class;
        }
    },
    // in german ZW1 (Steuersignal Zusatzheizung v. Heizung)
    TYPE_OUTPUT_ZW1 {
        {
            command = "output_zw1";
            itemClass = SwitchItem.class;
        }
    },
    // in german ZW2 (Steuersignal Zusatzheizung/Störsignal)
    TYPE_OUTPUT_ZW2SST {
        {
            command = "output_zw2sst";
            itemClass = SwitchItem.class;
        }
    },
    // in german ZW3 (Zusatzheizung 3)
    TYPE_OUTPUT_ZW3SST {
        {
            command = "output_zw3sst";
            itemClass = SwitchItem.class;
        }
    },
    // in german FP2 (Pumpe Mischkreis 2)
    TYPE_OUTPUT_FP2 {
        {
            command = "output_fp2";
            itemClass = SwitchItem.class;
        }
    },
    // in german SLP (Solarladepumpe)
    TYPE_OUTPUT_SLP {
        {
            command = "output_slp";
            itemClass = SwitchItem.class;
        }
    },
    // in german SUP (Schwimmbadpumpe)
    TYPE_OUTPUT_SUP {
        {
            command = "output_sup";
            itemClass = SwitchItem.class;
        }
    },
    // in german MA2 (Mischkreis 2 auf)
    TYPE_OUTPUT_MA2 {
        {
            command = "output_ma2";
            itemClass = SwitchItem.class;
        }
    },
    // in german MZ2 (Mischkreis 2 zu)
    TYPE_OUTPUT_MZ2 {
        {
            command = "output_mz2";
            itemClass = SwitchItem.class;
        }
    },
    // in german MA3 (Mischkreis 3 auf)
    TYPE_OUTPUT_MA3 {
        {
            command = "output_ma3";
            itemClass = SwitchItem.class;
        }
    },
    // in german MZ3 (Mischkreis 3 zu)
    TYPE_OUTPUT_MZ3 {
        {
            command = "output_mz3";
            itemClass = SwitchItem.class;
        }
    },
    // in german FP3 (Pumpe Mischkreis 3)
    TYPE_OUTPUT_FP3 {
        {
            command = "output_fp3";
            itemClass = SwitchItem.class;
        }
    },
    // in german VSK
    TYPE_OUTPUT_VSK {
        {
            command = "output_vsk";
            itemClass = SwitchItem.class;
        }
    },
    // in german FRH
    TYPE_OUTPUT_FRH {
        {
            command = "output_frh";
            itemClass = SwitchItem.class;
        }
    },
    // in german VDH (Verdichterheizung)
    TYPE_OUTPUT_VDH {
        {
            command = "output_vdh";
            itemClass = SwitchItem.class;
        }
    },
    // in german AV2 (Abtauventil 2)
    TYPE_OUTPUT_AV2 {
        {
            command = "output_av2";
            itemClass = SwitchItem.class;
        }
    },
    // in german VBO2 (Solepumpe/Ventilator)
    TYPE_OUTPUT_VBO2 {
        {
            command = "output_vbo2";
            itemClass = SwitchItem.class;
        }
    },
    // in german VD12 (Verdichter 1/2)
    TYPE_OUTPUT_VD12 {
        {
            command = "output_vd12";
            itemClass = SwitchItem.class;
        }
    },
    // in german VDH2 (Verdichterheizung 2)
    TYPE_OUTPUT_VDH2 {
        {
            command = "output_vdh2";
            itemClass = SwitchItem.class;
        }
    };

    /** Represents the heatpump command as it will be used in *.items configuration */
    String command;
    Class<? extends Item> itemClass;

    public String getCommand() {
        return command;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    /**
     * 
     * @param bindingConfig command string e.g. state, temperature_solar_storage,..
     * @param itemClass class to validate
     * @return true if item class can bound to heatpumpCommand
     */
    public static boolean validateBinding(HeatpumpCommandType bindingConfig, Class<? extends Item> itemClass) {
        boolean ret = false;
        for (HeatpumpCommandType c : HeatpumpCommandType.values()) {
            if (c.getCommand().equals(bindingConfig.getCommand()) && c.getItemClass().equals(itemClass)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static HeatpumpCommandType fromString(String heatpumpCommand) {

        if ("".equals(heatpumpCommand)) {
            return null;
        }
        for (HeatpumpCommandType c : HeatpumpCommandType.values()) {

            if (c.getCommand().equals(heatpumpCommand)) {
                return c;
            }
        }

        throw new IllegalArgumentException("cannot find novelanHeatpumpCommand for '" + heatpumpCommand + "'");

    }

}
