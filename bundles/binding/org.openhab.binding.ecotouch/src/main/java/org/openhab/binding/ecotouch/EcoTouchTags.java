/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid commands which could be processed by this binding
 *
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */
public enum EcoTouchTags {

    // German: Außentemperatur
    TYPE_TEMPERATURE_OUTSIDE {
        {
            command = "temperature_outside";
            itemClass = NumberItem.class;
            tagName = "A1";
        }
    },

    // German: Außentemperatur gemittelt über 1h
    TYPE_TEMPERATURE_OUTSIDE_1H {
        {
            command = "temperature_outside_1h";
            itemClass = NumberItem.class;
            tagName = "A2";
        }
    },

    // German: Außentemperatur gemittelt über 24h
    TYPE_TEMPERATURE_OUTSIDE_24H {
        {
            command = "temperature_outside_24h";
            itemClass = NumberItem.class;
            tagName = "A3";
        }
    },

    // German: Quelleneintrittstemperatur
    TYPE_TEMPERATURE_SOURCE_IN {
        {
            command = "temperature_source_in";
            itemClass = NumberItem.class;
            tagName = "A4";
        }
    },

    // German: Quellenaustrittstemperatur
    TYPE_TEMPERATURE_SOURCE_OUT {
        {
            command = "temperature_source_out";
            itemClass = NumberItem.class;
            tagName = "A5";
        }
    },

    // German: Verdampfungstemperatur
    TYPE_TEMPERATURE_EVAPORATION {
        {
            command = "temperature_evaporation";
            itemClass = NumberItem.class;
            tagName = "A6";
        }
    },

    // German: Sauggastemperatur
    TYPE_TEMPERATURE_SUCTION {
        {
            command = "temperature_suction";
            itemClass = NumberItem.class;
            tagName = "A7";
        }
    },

    // German: Verdampfungsdruck
    TYPE_PRESSURE_EVAPORATION {
        {
            command = "pressure_evaporation";
            itemClass = NumberItem.class;
            tagName = "A8";
        }
    },

    // German: Temperatur Rücklauf Soll
    TYPE_TEMPERATURE_RETURN_SET {
        {
            command = "temperature_return_set";
            itemClass = NumberItem.class;
            tagName = "A10";
        }
    },

    // German: Temperatur Rücklauf
    TYPE_TEMPERATURE_RETURN {
        {
            command = "temperature_return";
            itemClass = NumberItem.class;
            tagName = "A11";
        }
    },

    // German: Temperatur Vorlauf
    TYPE_TEMPERATURE_FLOW {
        {
            command = "temperature_flow";
            itemClass = NumberItem.class;
            tagName = "A12";
        }
    },

    // German: Kondensationstemperatur
    TYPE_TEMPERATURE_CONDENSATION {
        {
            command = "temperature_condensation";
            itemClass = NumberItem.class;
            tagName = "A14";
        }
    },

    // German: Kondensationsdruck
    TYPE_PRESSURE_CONDENSATION {
        {
            command = "pressure_condensation";
            itemClass = NumberItem.class;
            tagName = "A15";
        }
    },

    // German: Speichertemperatur
    TYPE_TEMPERATURE_STORAGE {
        {
            command = "temperature_storage";
            itemClass = NumberItem.class;
            tagName = "A16";
        }
    },

    // German: Raumtemperatur
    TYPE_TEMPERATURE_ROOM {
        {
            command = "temperature_room";
            itemClass = NumberItem.class;
            tagName = "A17";
        }
    },

    // German: Raumtemperatur gemittelt über 1h
    TYPE_TEMPERATURE_ROOM_1H {
        {
            command = "temperature_room_1h";
            itemClass = NumberItem.class;
            tagName = "A18";
        }
    },

    // German: Warmwassertemperatur
    TYPE_TEMPERATURE_WATER {
        {
            command = "temperature_water";
            itemClass = NumberItem.class;
            tagName = "A19";
        }
    },

    // German: Pooltemperatur
    TYPE_TEMPERATURE_POOL {
        {
            command = "temperature_pool";
            itemClass = NumberItem.class;
            tagName = "A20";
        }
    },

    // German: Solarkollektortemperatur
    TYPE_TEMPERATURE_SOLAR {
        {
            command = "temperature_solar";
            itemClass = NumberItem.class;
            tagName = "A21";
        }
    },

    // German: Solarkreis Vorlauf
    TYPE_TEMPERATURE_SOLAR_FLOW {
        {
            command = "temperature_solar_flow";
            itemClass = NumberItem.class;
            tagName = "A22";
        }
    },

    // German: Ventilöffnung elektrisches Expansionsventil
    TYPE_POSITION_EXPANSION_VALVE {
        {
            command = "position_expansion_valve";
            itemClass = NumberItem.class;
            tagName = "A23";
        }
    },

    // German: elektrische Leistung Verdichter
    TYPE_POWER_COMPRESSOR {
        {
            command = "power_compressor";
            itemClass = NumberItem.class;
            tagName = "A25";
        }
    },

    // German: abgegebene thermische Heizleistung der Wärmepumpe
    TYPE_POWER_HEATING {
        {
            command = "power_heating";
            itemClass = NumberItem.class;
            tagName = "A26";
        }
    },

    // German: abgegebene thermische KälteLeistung der Wärmepumpe
    TYPE_POWER_COOLING {
        {
            command = "power_cooling";
            itemClass = NumberItem.class;
            tagName = "A27";
        }
    },

    // German: COP Heizleistung
    TYPE_COP_HEATING {
        {
            command = "cop_heating";
            itemClass = NumberItem.class;
            tagName = "A28";
        }
    },

    // German: COP Kälteleistungleistung
    TYPE_COP_COOLING {
        {
            command = "cop_cooling";
            itemClass = NumberItem.class;
            tagName = "A29";
        }
    },

    // German: Aktuelle Heizkreistemperatur
    TYPE_TEMPERATURE_HEATING {
        {
            command = "temperature_heating_return";
            itemClass = NumberItem.class;
            tagName = "A30";
        }
    },

    // German: Geforderte Temperatur im Heizbetrieb
    TYPE_TEMPERATURE_HEATING_SET {
        {
            command = "temperature_heating_set";
            itemClass = NumberItem.class;
            tagName = "A31";
        }
    },

    // German: Sollwertvorgabe Heizkreistemperatur
    TYPE_TEMPERATURE_HEATING_SET2 {
        {
            command = "temperature_heating_set2";
            itemClass = NumberItem.class;
            tagName = "A32";
        }
    },

    // German: Aktuelle Kühlkreistemperatur
    TYPE_TEMPERATURE_COOLING {
        {
            command = "temperature_cooling_return";
            itemClass = NumberItem.class;
            tagName = "A33";
        }
    },

    // German: Geforderte Temperatur im Kühlbetrieb
    TYPE_TEMPERATURE_COOLING_SET {
        {
            command = "temperature_cooling_set";
            itemClass = NumberItem.class;
            tagName = "A34";
        }
    },

    // German: Sollwertvorgabe Kühlbetrieb
    TYPE_TEMPERATURE_COOLING_SET2 {
        {
            command = "temperature_cooling_set2";
            itemClass = NumberItem.class;
            tagName = "A35";
        }
    },

    // German: Sollwert Warmwassertemperatur
    TYPE_TEMPERATURE_WATER_SET {
        {
            command = "temperature_water_set";
            itemClass = NumberItem.class;
            tagName = "A37";
        }
    },

    // German: Sollwertvorgabe Warmwassertemperatur
    TYPE_TEMPERATURE_WATER_SET2 {
        {
            command = "temperature_water_set2";
            itemClass = NumberItem.class;
            tagName = "A38";
        }
    },

    // German: Sollwert Poolwassertemperatur
    TYPE_TEMPERATURE_POOL_SET {
        {
            command = "temperature_pool_set";
            itemClass = NumberItem.class;
            tagName = "A40";
        }
    },

    // German: Sollwertvorgabe Poolwassertemperatur
    TYPE_TEMPERATURE_POOL_SET2 {
        {
            command = "temperature_pool_set2";
            itemClass = NumberItem.class;
            tagName = "A41";
        }
    },

    // German: geforderte Verdichterleistung
    TYPE_COMPRESSOR_POWER {
        {
            command = "compressor_power";
            itemClass = NumberItem.class;
            tagName = "A50";
        }
    },

    // German: % Heizungsumwälzpumpe
    TYPE_PERCENT_HEAT_CIRC_PUMP {
        {
            command = "percent_heat_circ_pump";
            itemClass = NumberItem.class;
            tagName = "A51";
        }
    },

    // German: % Quellenpumpe
    TYPE_PERCENT_SOURCE_PUMP {
        {
            command = "percent_source_pump";
            itemClass = NumberItem.class;
            tagName = "A52";
        }
    },

    // German: % Leistung Verdichter
    TYPE_PERCENT_COMPRESSOR {
        {
            command = "percent_compressor";
            itemClass = NumberItem.class;
            tagName = "A58";
        }
    },

    // German: Hysterese Heizung
    TYPE_HYSTERESIS_HEATING {
        {
            command = "hysteresis_heating";
            itemClass = NumberItem.class;
            tagName = "A61";
        }
    },

    // German: Außentemperatur gemittelt über 1h (scheinbar identisch zu A2)
    TYPE_TEMPERATURE2_OUTSIDE_1H {
        {
            command = "temperature2_outside_1h";
            itemClass = NumberItem.class;
            tagName = "A90";
        }
    },

    // German: Heizkurve - nviNormAussen
    TYPE_NVINORMAUSSEN {
        {
            command = "nviNormAussen";
            itemClass = NumberItem.class;
            tagName = "A91";
        }
    },

    // German: Heizkurve - nviHeizkreisNorm
    TYPE_NVIHEIZKREISNORM {
        {
            command = "nviHeizkreisNorm";
            itemClass = NumberItem.class;
            tagName = "A92";
        }
    },

    // German: Heizkurve - nviTHeizgrenze
    TYPE_NVITHEIZGRENZE {
        {
            command = "nviTHeizgrenze";
            itemClass = NumberItem.class;
            tagName = "A93";
        }
    },

    // German: Heizkurve - nviTHeizgrenzeSoll
    TYPE_NVITHEIZGRENZESOLL {
        {
            command = "nviTHeizgrenzeSoll";
            itemClass = NumberItem.class;
            tagName = "A94";
        }
    },

    // German: undokumentiert: Heizkurve max. VL-Temp (??)
    TYPE_MAX_VL_TEMP {
        {
            command = "maxVLTemp";
            itemClass = NumberItem.class;
            tagName = "A95";
        }
    },

    // German: undokumentiert: Heizkreis Soll-Temp bei 0° Aussen
    TYPE_TEMP_SET_0DEG {
        {
            command = "tempSet0Deg";
            itemClass = NumberItem.class;
            tagName = "A97";
        }
    },

    // German: undokumentiert: Kühlen Einschalt-Temp. Aussentemp (??)
    TYPE_COOLENABLETEMP {
        {
            command = "coolEnableTemp";
            itemClass = NumberItem.class;
            tagName = "A108";
        }
    },

    // German: Heizkurve - nviSollKuehlen
    TYPE_NVITSOLLKUEHLEN {
        {
            command = "nviSollKuehlen";
            itemClass = NumberItem.class;
            tagName = "A109";
        }
    },

    // German: Temperaturveränderung Heizkreis bei PV-Ertrag
    TYPE_TEMPCHANGE_HEATING_PV {
        {
            command = "tempchange_heating_pv";
            itemClass = NumberItem.class;
            tagName = "A682";
        }
    },

    // German: Temperaturveränderung Kühlkreis bei PV-Ertrag
    TYPE_TEMPCHANGE_COOLING_PV {
        {
            command = "tempchange_cooling_pv";
            itemClass = NumberItem.class;
            tagName = "A683";
        }
    },

    // German: Temperaturveränderung Warmwasser bei PV-Ertrag
    TYPE_TEMPCHANGE_WARMWATER_PV {
        {
            command = "tempchange_warmwater_pv";
            itemClass = NumberItem.class;
            tagName = "A684";
        }
    },

    // German: Temperaturveränderung Pool bei PV-Ertrag
    TYPE_TEMPCHANGE_POOL_PV {
        {
            command = "tempchange_pool_pv";
            itemClass = NumberItem.class;
            tagName = "A685";
        }
    },

    // German: undokumentiert: Firmware-Version Regler
    // value 10401 => 01.04.01
    TYPE_VERSION_CONTROLLER {
        {
            command = "version_controller";
            itemClass = NumberItem.class;
            tagName = "I1";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Firmware-Build Regler
    TYPE_VERSION_CONTROLLER_BUILD {
        {
            command = "version_controller_build";
            itemClass = NumberItem.class;
            tagName = "I2";
            type = Type.Word;
        }
    },

    // German: undokumentiert: BIOS-Version
    // value 620 => 06.20
    TYPE_VERSION_BIOS {
        {
            command = "version_bios";
            itemClass = NumberItem.class;
            tagName = "I3";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Datum: Tag
    TYPE_DATE_DAY {
        {
            command = "date_day";
            itemClass = NumberItem.class;
            tagName = "I5";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Datum: Monat
    TYPE_DATE_MONTH {
        {
            command = "date_month";
            itemClass = NumberItem.class;
            tagName = "I6";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Datum: Jahr
    TYPE_DATE_YEAR {
        {
            command = "date_year";
            itemClass = NumberItem.class;
            tagName = "I7";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Uhrzeit: Stunde
    TYPE_TIME_HOUR {
        {
            command = "time_hour";
            itemClass = NumberItem.class;
            tagName = "I8";
            type = Type.Word;
        }
    },

    // German: undokumentiert: Uhrzeit: Minute
    TYPE_TIME_MINUTE {
        {
            command = "time_minute";
            itemClass = NumberItem.class;
            tagName = "I9";
            type = Type.Word;
        }
    },

    // German: Betriebsstunden Verdichter 1
    TYPE_OPERATING_HOURS_COMPRESSOR1 {
        {
            command = "operating_hours_compressor1";
            itemClass = NumberItem.class;
            tagName = "I10";
            type = Type.Word;
        }
    },

    // German: Betriebsstunden Verdichter 2
    TYPE_OPERATING_HOURS_COMPRESSOR2 {
        {
            command = "operating_hours_compressor2";
            itemClass = NumberItem.class;
            tagName = "I14";
            type = Type.Word;
        }
    },

    // German: Betriebsstunden Heizungsumwälzpumpe
    TYPE_OPERATING_HOURS_CIRCULATION_PUMP {
        {
            command = "operating_hours_circulation_pump";
            itemClass = NumberItem.class;
            tagName = "I18";
            type = Type.Word;
        }
    },

    // German: Betriebsstunden Quellenpumpe
    TYPE_OPERATING_HOURS_SOURCE_PUMP {
        {
            command = "operating_hours_source_pump";
            itemClass = NumberItem.class;
            tagName = "I20";
            type = Type.Word;
        }
    },

    // German: Betriebsstunden Solarkreis
    TYPE_OPERATING_HOURS_SOLAR {
        {
            command = "operating_hours_solar";
            itemClass = NumberItem.class;
            tagName = "I22";
            type = Type.Word;
        }
    },

    // German: Handabschaltung Heizbetrieb
    TYPE_ENABLE_HEATING {
        {
            command = "enable_heating";
            itemClass = SwitchItem.class;
            tagName = "I30";
            type = Type.Word;
        }
    },

    // German: Handabschaltung Kühlbetrieb
    TYPE_ENABLE_COOLING {
        {
            command = "enable_cooling";
            itemClass = SwitchItem.class;
            tagName = "I31";
            type = Type.Word;
        }
    },

    // German: Handabschaltung Warmwasserbetrieb
    TYPE_ENABLE_WARMWATER {
        {
            command = "enable_warmwater";
            itemClass = SwitchItem.class;
            tagName = "I32";
            type = Type.Word;
        }
    },

    // German: Handabschaltung Pool_Heizbetrieb
    TYPE_ENABLE_POOL {
        {
            command = "enable_pool";
            itemClass = SwitchItem.class;
            tagName = "I33";
            type = Type.Word;
        }
    },

    // German: undokumentiert: vermutlich Betriebsmodus PV 0=Aus, 1=Auto, 2=Ein
    TYPE_ENABLE_PV {
        {
            command = "enable_pv";
            itemClass = NumberItem.class;
            tagName = "I41";
            type = Type.Word;
        }
    },

    // German: Status der Wärmepumpenkomponenten
    TYPE_STATE {
        {
            command = "state";
            itemClass = NumberItem.class;
            tagName = "I51";
            type = Type.Word;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Quellenpumpe
    TYPE_STATE_SOURCEPUMP {
        {
            command = "state_sourcepump";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 0;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Heizungsumwälzpumpe
    TYPE_STATE_HEATINGPUMP {
        {
            command = "state_heatingpump";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 1;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Freigabe Regelung EVD /
    // Magnetventil
    TYPE_STATE_EVD {
        {
            command = "state_evd";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 2;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Verdichter 1
    TYPE_STATE_compressor1 {
        {
            command = "state_compressor1";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 3;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Verdichter 2
    TYPE_STATE_compressor2 {
        {
            command = "state_compressor2";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 4;
        }
    },

    // German: Status der Wärmepumpenkomponenten: externer Wärmeerzeuger
    TYPE_STATE_extheater {
        {
            command = "state_extheater";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 5;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Alarmausgang
    TYPE_STATE_alarm {
        {
            command = "state_alarm";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 6;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Motorventil Kühlbetrieb
    TYPE_STATE_cooling {
        {
            command = "state_cooling";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 7;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Motorventil Warmwasser
    TYPE_STATE_water {
        {
            command = "state_water";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 8;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Motorventil Pool
    TYPE_STATE_pool {
        {
            command = "state_pool";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 9;
        }
    },

    // German: Status der Wärmepumpenkomponenten: Solarbetrieb
    TYPE_STATE_solar {
        {
            command = "state_solar";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 10;
        }
    },

    // German: Status der Wärmepumpenkomponenten: 4-Wegeventil im Kältekreis
    TYPE_STATE_cooling4way {
        {
            command = "state_cooling4way";
            itemClass = SwitchItem.class;
            tagName = "I51";
            type = Type.Bitfield;
            bitnum = 11;
        }
    },

    // German: Meldungen von Ausfällen F0xx die zum Wärmepumpenausfall führen
    TYPE_ALARM {
        {
            command = "alarm";
            itemClass = NumberItem.class;
            tagName = "I52";
            type = Type.Word;
        }
    },

    // German: Unterbrechungen
    TYPE_INTERRUPTIONS {
        {
            command = "interruptions";
            itemClass = NumberItem.class;
            tagName = "I53";
            type = Type.Word;
        }
    },

    // German: Serviceebene (0: normal, 1: service)
    TYPE_STATE_SERVICE {
        {
            command = "state_service";
            itemClass = NumberItem.class;
            tagName = "I135";
            type = Type.Word;
        }
    },

    // German: Temperaturanpassung für die Heizung
    TYPE_ADAPT_HEATING {
        {
            command = "adapt_heating";
            itemClass = NumberItem.class;
            tagName = "I263";
            type = Type.Word; // value range 0..8 => -2K .. +2K
        }
    },

    // German: Handschaltung Heizungspumpe (H-0-A)
    // H:Handschaltung Ein 0:Aus A:Automatik
    // Kodierung: 0:? 1:? 2:Automatik
    TYPE_MANUAL_HEATINGPUMP {
        {
            command = "manual_heatingpump";
            itemClass = NumberItem.class;
            tagName = "I1270";
            type = Type.Word;
        }
    },

    // German: Handschaltung Quellenpumpe (H-0-A)
    TYPE_MANUAL_SOURCEPUMP {
        {
            command = "manual_sourcepump";
            itemClass = NumberItem.class;
            tagName = "I1281";
            type = Type.Word;
        }
    },

    // German: Handschaltung Solarpumpe 1 (H-0-A)
    TYPE_MANUAL_SOLARPUMP1 {
        {
            command = "manual_solarpump1";
            itemClass = NumberItem.class;
            tagName = "I1287";
            type = Type.Word;
        }
    },

    // German: Handschaltung Solarpumpe 2 (H-0-A)
    TYPE_MANUAL_SOLARPUMP2 {
        {
            command = "manual_solarpump2";
            itemClass = NumberItem.class;
            tagName = "I1289";
            type = Type.Word;
        }
    },

    // German: Handschaltung Speicherladepumpe (H-0-A)
    TYPE_MANUAL_TANKPUMP {
        {
            command = "manual_tankpump";
            itemClass = NumberItem.class;
            tagName = "I1291";
            type = Type.Word;
        }
    },

    // German: Handschaltung Brauchwasserventil (H-0-A)
    TYPE_MANUAL_VALVE {
        {
            command = "manual_valve";
            itemClass = NumberItem.class;
            tagName = "I1293";
            type = Type.Word;
        }
    },

    // German: Handschaltung Poolventil (H-0-A)
    TYPE_MANUAL_POOLVALVE {
        {
            command = "manual_poolvalve";
            itemClass = NumberItem.class;
            tagName = "I1295";
            type = Type.Word;
        }
    },

    // German: Handschaltung Kühlventil (H-0-A)
    TYPE_MANUAL_COOLVALVE {
        {
            command = "manual_coolvalve";
            itemClass = NumberItem.class;
            tagName = "I1297";
            type = Type.Word;
        }
    },

    // German: Handschaltung Vierwegeventil (H-0-A)
    TYPE_MANUAL_4WAYVALVE {
        {
            command = "manual_4wayvalve";
            itemClass = NumberItem.class;
            tagName = "I1299";
            type = Type.Word;
        }
    },

    // German: Handschaltung Multiausgang Ext. (H-0-A)
    TYPE_MANUAL_MULTIEXT {
        {
            command = "manual_multiext";
            itemClass = NumberItem.class;
            tagName = "I1319";
            type = Type.Word;
        }
    },

    // German: Umgebung
    TYPE_TEMPERATURE_SURROUNDING {
        {
            command = "temperature_surrounding";
            itemClass = NumberItem.class;
            tagName = "I2020";
            type = Type.Analog;
            divisor = 100;
        }
    },

    // German: Sauggas
    TYPE_TEMPERATURE_SUCTION_AIR {
        {
            command = "temperature_suction_air";
            itemClass = NumberItem.class;
            tagName = "I2021";
            type = Type.Analog;
            divisor = 100;
        }
    },

    // German: Ölsumpf
    TYPE_TEMPERATURE_SUMP {
        {
            command = "temperature_sump";
            itemClass = NumberItem.class;
            tagName = "I2023";
            type = Type.Analog;
            divisor = 100;
        }
    },

    ;

    /**
     * Represents the heatpump command as it will be used in *.items
     * configuration
     */
    String command;
    /**
     * Represents the internal raw heatpump command as it will be used in
     * querying the heat pump
     */
    String tagName;
    Class<? extends Item> itemClass;

    /**
     * The heatpump always returns 16-bit integers encoded as ASCII. They need
     * to be interpreted according to the context.
     */
    public enum Type {
        Analog,
        Word,
        Bitfield
    };

    /**
     * The format of the response of the heat pump
     */
    Type type = Type.Analog;

    /**
     * If \c type is Type.Bitfield, this determines the bit number (0-based)
     */
    int bitnum = 0;

    /**
     * If type is {@link Type#Analog} this is used as divisor for the scaled integer.
     * Defaults to 10 and should be a power of 10 (e.g. 10, 100, 1000).
     */
    int divisor = 10;

    /**
     * @return command name (uses in *.items files)
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return tag name (raw communication with heat pump)
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @return type: how to interpret the response from the heat pump
     */
    public Type getType() {
        return type;
    }

    /**
     * @return bitnum: if the value is a bit field, this indicates the bit
     *         number (0-based)
     */
    public int getBitNum() {
        return bitnum;
    }

    /**
     * @return Divisor for scaled integer analog values.
     */
    public int getDivisor() {
        return divisor;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    /**
     *
     * @param bindingConfig
     *            command e.g. TYPE_TEMPERATURE_OUTSIDE,..
     * @param itemClass
     *            class to validate
     * @return true if item class can bound to heatpumpCommand
     */
    public static boolean validateBinding(EcoTouchTags bindingConfig, Class<? extends Item> itemClass) {
        boolean ret = false;
        for (EcoTouchTags c : EcoTouchTags.values()) {
            if (c.getCommand().equals(bindingConfig.getCommand()) && c.getItemClass().equals(itemClass)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * Searches the available heat pump commands and returns the matching one.
     *
     * @param heatpumpCommand
     *            command string e.g. "temperature_outside"
     * @return matching EcoTouchTags instance, if available
     */
    public static EcoTouchTags fromString(String heatpumpCommand) {
        if ("".equals(heatpumpCommand)) {
            return null;
        }
        for (EcoTouchTags c : EcoTouchTags.values()) {
            if (c.getCommand().equals(heatpumpCommand)) {
                return c;
            }
        }

        throw new IllegalArgumentException("cannot find EcoTouch tag for '" + heatpumpCommand + "'");
    }

    /**
     * Searches the available heat pump commands and returns the first matching
     * one.
     *
     * @param tag
     *            raw heatpump tag e.g. "A1"
     * @return first matching EcoTouchTags instance, if available
     */
    public static EcoTouchTags fromTag(String tag) {
        for (EcoTouchTags c : EcoTouchTags.values()) {
            if (c.getTagName().equals(tag)) {
                return c;
            }
        }

        throw new IllegalArgumentException("cannot find EcoTouch tag for '" + tag + "'");
    }
}
