/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

/**
 * Commands for the WR3223 Connector.
 *
 * @author Michael Fraefel
 *
 */
public enum WR3223Commands {
    /** Abtau ein, beginn Abtauung ab Verdampfertemperatur. Temperatur lesen/schreiben */
    AE,

    /** Abtau aus, ende Abtauung ab Verdampfertemperatur. Temperatur lesen/schreiben */
    AA,

    /** Abtau Zuluft lesen/schreiben */
    Az,

    /** Abtauluft lesen/schreiben */
    Aa,

    /** Frost aus lesen/schreiben */
    AR,

    /** Frost an Schnittstelle lesen/schreiben */
    AZ,

    /** Abtaupause lesen/schreiben */
    AP,

    /** Abtaunachlauf lesen/schreiben */
    AN,

    /** Frost Verzögerung lesen */
    AV,

    /** Verdampfertemperatur istwert auslesen */
    T1,

    /** Kondensatortemperatur auslesen */
    T2,

    /** Aussentemperatur auslesen */
    T3,

    /** Ablufttemperatur (Raumtemperatur) */
    T4,

    /** Temperatur nach Wärmetauscher (Fortluft) lesen */
    T5,

    /** Zulufttemperatur auslesen */
    T6,

    /** Temperatur nach Solevorerwärmung lesen */
    T7,

    /** Temperatur nach Vorheizregister lesen */
    T8,

    /** aktuelle Luftstufe lesen/schreiben */
    LS,

    /** Luftstufe 1 lesen/schreiben */
    L1,

    /** Luftstufe 2 lesen/schreiben */
    L2,

    /** Luftstufe 3 lesen/schreiben */
    L3,

    /** Zuluft +/- lesen/schreiben */
    LD,

    /** Abluft +/- lesen/schreiben */
    Ld,

    /** Erdwärmetauscher vorhanden lesen/schreiben */
    EC,

    /** Schaltpunkt Sommer stopp lesen/schreiben */
    Es,

    /** Schaltpunkt Erdwärmetauscher Sommer lesen/schreiben */
    ES,

    /** Schaltpunkt Erdwärmetauscher Winter lesen/schreiben */
    EW,

    /** Schaltpunkt Solepumpe Ein lesen/schreiben */
    EE,

    /** Schaltpunkt Solepumpe aus lesen/schreiben */
    EA,

    /** Fehler lesen */
    ER,

    /** Status auslesen */
    ST,

    /** Status schreib byte auslesen/schreiben (nur bei PC Steuerung) */
    SW,

    /** Zuluftsoll Temperatur auslesen/schreiben (nur bei PC Steuerung) */
    SP,

    /** Mode auslesen/schreiben (nur bei PC Steuerung) */
    MD,

    /** Relais lesen */
    RL,

    /** Steuerspannnung Zuluft lesen */
    UZ,

    /** Steuerspannnung Abluft lesen */
    UA,

    /** Drehzahl Zuluftmotor lesen */
    NZ,

    /** Drehzahl Abluftmotor lesen */
    NA,

    /** Grenzdrehzahl lesen/schreiben (*50) */
    NM,

    /** Configuration auslesen */
    CN,

    /** Maximale Kondensationstemperatur lesen/schreiben */
    KM,

    /** Zusatzheizung frei (ausgeschaltet (0) oder freigegeben (1)) lesen/schreiben */
    ZH,

    /** Zusatzheizung Ein lesen/schreiben */
    ZE,

    /** Wärmepumpe frei (freigegeben (1) oder aus (0)) lesen/schreiben */
    WP,

    /** Pausezeit für Druckabbau bei automatischer Umschaltung lesen/schreiben */
    PA,

    /** Identifikation lesen */
    II,

    /** Messwert, RWZ aktl., Aktuelle Rückwärmzahl in % */
    RA,

    /** Parameter, Delta n 1 max, Max. Drehzahlabweichung Zu-/Abluft in Stufe 1 */
    D1,

    /** Parameter, Delta n 2 max, Max. Drehzahlabweichung Zu-/Abluft in Stufe 2 */
    D2,

    /** Parameter, Delta n 3 max, Max. Drehzahlabweichung Zu-/Abluft in Stufe 3 */
    D3,

    /** Parameter, ERDluft +S1, Drehzahlerhöhung Zuluftventilator Stufe 1, wenn Erdwärmetauscher ein (0% bis 40%) */
    E1,

    /** Parameter, ERDluft +S2, Drehzahlerhöhung Zuluftventilator Stufe 2, wenn Erdwärmetauscher ein (0% bis 40%) */
    E2,

    /** Parameter, ERDluft +S3, Drehzahlerhöhung Zuluftventilator Stufe 3, wenn Erdwärmetauscher ein (0% bis 40%) */
    E3,

    /** Parameter, LuflREDUK, Luftwechsel um 3% reduziert ab Außentemp. ...°C (-20°C bis +10°C) */
    LR,

    /** Parameter Solar max */
    SM,

    /** Messwert Solar Nutzen (Stunden) */
    SN,

    /** Parameter Delta T Aus Temperaturdifferenz zwischen Speicher u. Kollektor */
    DA,

    /** Parameter Delta T Ein Temperaturdifferenz zwischen Speicher u. Kollektor */
    DE,

    /** Parameter Unterstuetzungsgeblaese bei Luftstufe 1 bei EWT */
    S1,

    /** Parameter Unterstuetzungsgeblaese bei Luftstufe 2 bei EWT */
    S2,

    /** Parameter Unterstuetzungsgeblaese bei Luftstufe 3 bei EWT */
    S3,

    /** Parameter Warmwasser Sollwert */
    WS,

    /** Parameter EVU Sperre */
    Tf,

    /** Status auslesen */
    Ta

}
