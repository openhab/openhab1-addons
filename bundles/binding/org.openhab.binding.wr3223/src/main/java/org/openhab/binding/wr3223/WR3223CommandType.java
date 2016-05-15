/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223;

import org.openhab.binding.wr3223.internal.WR3223Commands;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Michael Fraefel
 * @since 1.8.0
 */
public enum WR3223CommandType {

    /** T1 (de: Verdampfertemepratur) */
    TEMPERATURE_EVAPORATOR {
        {
            command = "temperature_evaporator";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T1;
        }
    },

    /** T2 (de: Kondensatortemperatur) */
    TEMPERATURE_CONDENSER {
        {
            command = "temperature_condenser";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T2;
        }
    },

    /** T3 (de: Aussentemperatur) */
    TEMPERATURE_OUTSIDE {
        {
            command = "temperature_outside";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T3;
        }
    },

    /** T4 (de: Ablufttemperatur (Raumtemperatur)) */
    TEMPERATURE_OUTGOING_AIR {
        {
            command = "temperature_outgoing_air";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T4;
        }
    },

    /** T5 (de: Temperatur nach Wärmetauscher (Fortluft)) */
    TEMPERATURE_AFTER_HEAT_EXCHANGER {
        {
            command = "temperature_after_heat_exchanger";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T5;
        }
    },

    /** T6 (de: Zulufttemperatur) */
    TEMPERATURE_SUPPLY_AIR {
        {
            command = "temperature_supply_air";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T6;
        }
    },

    /** T7 (de: Temperatur nach Solevorerwärmung) */
    TEMPERATURE_AFTER_BRINE_PREHEATING {
        {
            command = "temperature_after_brine_preheating";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T7;
        }
    },

    /** T8 (de: Temperatur nach Vorheizregister) */
    TEMPERATURE_AFTER_PREHEATING_RADIATOR {
        {
            command = "temperature_after_preheating_radiator";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.T8;
        }
    },

    /** (de: Drehzahl Zuluftmotor) */
    ROTATION_SPEED_SUPPLY_AIR_MOTOR {
        {
            command = "rotation_speed_supply_air_motor";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.NZ;
        }
    },

    /** (de: Drehzahl Abluftmotor) */
    ROTATION_SPEED_EXHAUST_AIR_MOTOR {
        {
            command = "rotation_speed_exhaust_air_motor";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.NA;
        }
    },

    /** (de: Bypass) */
    BYPASS {
        {
            command = "bypass";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Kompressor Relais) */
    COMPRESSOR {
        {
            command = "compressor";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Zusatzheizung Relais) */
    ADDITIONAL_HEATER {
        {
            command = "additional_heater";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Netzrelais Bypass) */
    BYPASS_RELAY {
        {
            command = "bypass_relay";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Vorheizen aktiv) */
    PREHEATING_RADIATOR_ACTIVE {
        {
            command = "preheating_radiator_active";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Bedienteil aktiv) */
    CONTROL_DEVICE_ACTIVE {
        {
            command = "control_device_active";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Erdwärmetauscher) */
    EARTH_HEAT_EXCHANGER {
        {
            command = "earth_heat_exchanger";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Magnetventil) */
    MAGNET_VALVE {
        {
            command = "magnet_valve";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Bedienung über RS Schnittstelle) */
    OPENHAB_INTERFACE_ACTIVE {
        {
            command = "openhab_interface_active";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Vorheizregister) */
    PREHEATING_RADIATOR {
        {
            command = "preheating_radiator";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: WW_Nachheizrgister) */
    WARM_WATER_POST_HEATER {
        {
            command = "warm_water_post_heater";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: Luftstufe vorhanden) */
    VENTILATION_LEVEL_AVAILABLE {
        {
            command = "ventilation_level_available";
            itemClass = ContactItem.class;
            wr3223Command = null;
        }
    },

    /** (de: aktuelle Luftstufe) */
    VENTILATION_LEVEL {
        {
            command = "ventilation_level";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.LS;
        }
    },

    /** (de: Betriebsart) */
    OPERATION_MODE {
        {
            command = "operation_mode";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.MD;
        }
    },

    /** (de: Zuluftsoll Temperatur) */
    TEMPERATURE_SUPPLY_AIR_TARGET {
        {
            command = "temperature_supply_air_target";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.SP;
        }
    },

    /** (de: RWZ aktl., Aktuelle Rückwärmzahl in %) */
    HEAT_FEEDBACK_RATE {
        {
            command = "heat_feedback_rate";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.RA;
        }
    },

    /** (de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 1 */
    SPEED_DEVIATION_MAX_LEVEL_1 {
        {
            command = "speed_deviation_max_level_1";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.D1;
        }
    },

    /** (de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 2 */
    SPEED_DEVIATION_MAX_LEVEL_2 {
        {
            command = "speed_deviation_max_level_2";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.D2;
        }
    },

    /** (de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 3 */
    SPEED_DEVIATION_MAX_LEVEL_3 {
        {
            command = "speed_deviation_max_level_3";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.D3;
        }
    },

    /** (de: Drehzahlerhöhung Zuluftventilator Stufe 1, wenn Erdwärmetauscher ein (0% bis 40%) */
    SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_1 {
        {
            command = "speed_increase_earth_heat_exchanger_level_1";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.E1;
        }
    },

    /** (de: Drehzahlerhöhung Zuluftventilator Stufe 2, wenn Erdwärmetauscher ein (0% bis 40%) */
    SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_2 {
        {
            command = "speed_increase_earth_heat_exchanger_level_2";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.E2;
        }
    },

    /** (de: Drehzahlerhöhung Zuluftventilator Stufe 3, wenn Erdwärmetauscher ein (0% bis 40%) */
    SPEED_INCREASE_EARTH_HEAT_EXCHANGER_LEVEL_3 {
        {
            command = "speed_increase_earth_heat_exchanger_level_3";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.E3;
        }
    },

    /** (de: LuflREDUK, Luftwechsel um 3% reduziert ab Außentemp. ...°C (-20°C bis +10°C) */
    AIR_EXCHANGE_DECREASE_OUTSIDE_TEMPERATURE {
        {
            command = "air_exchange_decrease_outside_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.LR;
        }
    },

    /** (de: Luftstufe 1, % des max. Ventilatorstellwerts (40 bis100%) */
    VENTILATION_SPEED_LEVEL_1 {
        {
            command = "ventilation_speed_level_1";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.L1;
        }
    },

    /** (de: Luftstufe 2, % des max. Ventilatorstellwerts (40 bis100%) */
    VENTILATION_SPEED_LEVEL_2 {
        {
            command = "ventilation_speed_level_2";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.L2;
        }
    },

    /** (de: Luftstufe 3, % des max. Ventilatorstellwerts (40 bis100%) */
    VENTILATION_SPEED_LEVEL_3 {
        {
            command = "ventilation_speed_level_3";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.L3;
        }
    },

    /** (de: ET sommer >, Einschalt-Außentemperatur Erdwämietauscher im Sommer (20°C bis 40°C) */
    SUMMER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE {
        {
            command = "summer_earth_heat_exchanger_activation_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.ES;
        }
    },

    /** (de: ET winter<, Einschalt-Außentemperatur Erdwärmetauscher im Winter (-20°C bis 10°C) */
    WINTER_EARTH_HEAT_EXCHANGER_ACTIVATION_TEMPERATURE {
        {
            command = "winter_earth_heat_exchanger_activation_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.EW;
        }
    },

    /** (de: Abtau EIN, Beginn Abtauung ab Verdampfertemperatur ...°C */
    DEFROSTING_START_TEMPERATURE {
        {
            command = "defrosting_start_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.AE;
        }
    },

    /** (de: Abtau AUS, Ende Abtauung ab Verdampfertemperatur ...°C */
    DEFROSTING_END_TEMPERATURE {
        {
            command = "defrosting_end_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.AA;
        }
    },

    /** (de: Abtau Luft, Lüfterstufe im Abtaubetrieb */
    DEFROSTING_VENTILATION_LEVEL {
        {
            command = "defrosting_ventilation_level";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.Az;
        }
    },

    /** (de: Abtaupause, Sperrzeit für den nächsten Abtauvorgang */
    DEFROSTING_HOLD_OFF_TIME {
        {
            command = "defrosting_hold_off_time";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.AP;
        }
    },

    /** (de: Abtau NFL, Abtaunachlauzeit */
    DEFROSTING_OVERTRAVEL_TIME {
        {
            command = "defrosting_overtravel_time";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.AN;
        }
    },

    /** (de: Abtau RWZ, Abtaurückwärmezahl Schaltpunkt (20% bis 80 %) */
    DEFROSTING_HEAT_FEEDBACK_RATE {
        {
            command = "defrosting_heat_feedback_rate";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.AR;
        }
    },

    /** (de: Solar max */
    SOLAR_MAX {
        {
            command = "solar_max";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.SM;
        }
    },

    /** (de: Solar Nutzen (Stunden) */
    SOLAR_USAGE {
        {
            command = "solar_usage";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.SN;
        }
    },

    /** (de: Delta T Aus Temperaturdifferenz zwischen Speicher u. Kollektor */
    DELTA_T_OFF {
        {
            command = "delta_t_off";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.DA;
        }
    },

    /** (de: Delta T Ein Temperaturdifferenz zwischen Speicher u. Kollektor */
    DELTA_T_ON {
        {
            command = "delta_t_on";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.DE;
        }
    },

    /** (de: Maximale Kondensatortemperatur */
    TEMPERATURE_CONDENSER_MAX {
        {
            command = "temperature_condenser_max";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.KM;
        }
    },

    /** (de: Pausezeit für Druckabbau bei automatischer Umschaltung */
    IDLE_TIME_PRESSURE_REDUCTION {
        {
            command = "idle_time_pressure_reduction";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.PA;
        }
    },

    /** (de: Unterstuetzungsgeblaese bei Luftstufe 1 bei EWT */
    SUPPORT_FAN_LEVEL_1_EARTH_HEAT_EXCHANGER {
        {
            command = "support_fan_level_1_earth_heat_exchanger";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.S1;
        }
    },

    /** (de: Unterstuetzungsgeblaese bei Luftstufe 2 bei EWT */
    SUPPORT_FAN_LEVEL_2_EARTH_HEAT_EXCHANGER {
        {
            command = "support_fan_level_2_earth_heat_exchanger";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.S2;
        }
    },

    /** (de: Unterstuetzungsgeblaese bei Luftstufe 3 bei EWT */
    SUPPORT_FAN_LEVEL_3_EARTH_HEAT_EXCHANGER {
        {
            command = "support_fan_level_3_earth_heat_exchanger";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.S3;
        }
    },

    /** (de: Steuerspannung Abluft */
    CONTROL_VOLTAGE_OUTGOING_AIR {
        {
            command = "control_voltage_outgoing_air";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.UA;
        }
    },

    /** (de: Steuerspannung Zuluft */
    CONTROL_VOLTAGE_SUPPLY_AIR {
        {
            command = "control_voltage_supply_air";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.UZ;
        }
    },

    /** (de: Warmwasser Sollwert */
    WARM_WATER_TARGET_TEMPERATURE {
        {
            command = "warm_water_target_temperature";
            itemClass = NumberItem.class;
            wr3223Command = WR3223Commands.WS;
        }
    },

    /** (de: Waermepumpe freigegeben (1) oder aus (0) */
    HEAT_PUMP_OPEN {
        {
            command = "heat_pump_open";
            itemClass = SwitchItem.class;
            wr3223Command = WR3223Commands.WP;
        }
    },

    /** (de: Zusatzheizung ausgeschaltet (0) oder freigegeben (1) */
    ADDITIONAL_HEATER_OPEN {
        {
            command = "additional_heater_open";
            itemClass = SwitchItem.class;
            wr3223Command = WR3223Commands.WP;
        }
    },;

    /** Represents the WR3223 command as it will be used in *.items configuration */
    String command;
    Class<? extends Item> itemClass;
    WR3223Commands wr3223Command;
    Integer minValue;
    Integer maxValue;

    public String getCommand() {
        return command;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    public WR3223Commands getWr3223Command() {
        return wr3223Command;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    /**
     * Find the right command for an item string.
     * 
     * @param bindingConfigString
     * @return
     */
    public static WR3223CommandType fromString(String bindingConfigString) {

        if ("".equals(bindingConfigString)) {
            return null;
        }
        for (WR3223CommandType c : WR3223CommandType.values()) {

            if (c.getCommand().equalsIgnoreCase(bindingConfigString)) {
                return c;
            }
        }
        throw new IllegalArgumentException("cannot find wr3223Command for '" + bindingConfigString + "'");
    }

    public static boolean validateBinding(WR3223CommandType bindingConfig, Class<? extends Item> itemClass) {
        if (bindingConfig != null && itemClass != null) {
            return bindingConfig.getItemClass().equals(itemClass);
        }
        return false;
    }

}
