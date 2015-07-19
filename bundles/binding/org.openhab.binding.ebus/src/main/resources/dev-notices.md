EBus Wolf 5023 Data CRC mit Polynom 0x5C



FF  35  50 22   3   57 AB 27    FF  0   2   07 00   DB      Automatik
FF  35  50 22   3   57 AB 27    FF  0   2   0B 00   3       Standby
FF  35  50 22   3   57 AB 27    FF  0   2   07 00   DB      Sparbetrieb
FF  35  50 22   3   57 AB 27    FF  0   2   08 00   35      (Sparbetrieb) Dauerhafter Absenkbetrieb
FF  35  50 22   3   57 AB 27    FF  0   2   0A 00   98      (Automatik)


WW Programm von 5:30 - 22:00
HZ Programm von 6:00 - 21:30

21:45 Uhr

Wärmeerzeuger Betriebsart
 - Standby

Heizkreis Anforderung Heizkreis
 - Aus             0x0B
 - Automatik an    0x07
 - Automatik aus   0x08
 
Heizkreis Status
 - Aus             0x0B
 - AT Abschaltung  0x08
 
Warmwasser Status
 - Aus

FF  35  50 22   3   9F AC 27    4D  0   2   05 00   76      AT-Abschaltung
FF  35  50 22   3   9F AC 27    4D  0   2   00 00   2C      AUS



Status Solarspeicher    Keine Aktivität
FF  76  50 22   3   46 CE 02    A5  0   2   00 00   2C

Durchfluss  0,0 l/min
FF  76  50 22   3   5E F8 02    D4  0   2   00 00   2C



Normaußentemperatur Heizkurve
FF  30  50 23   9   D8 7F 27 6A FF 5D 01 00 00  EE


Tagtemperatur
FF  30  50 23   9   60 7D 27 C9 00 5D 01 00 00  D9


Wolf Befehl Eco/ABS -3,5°C
FF  30  50 23   9   AC 17 01 DD FF 5D 01 00 00  11


Wolf Befehl Sparfaktor setzen 4,5°C
FF 30 50 23    09   64 81 27 2D 00 5D 01 00 00 17 00 AA

Wolf Befehle Sollwertkorrektur
FF	30	50 23	09	F8 B5 27 F6 FF 5D 01 00 00	45		-10
FF	30	50 23	09	3C B5 27 FB FF 5D 01 00 00	EA		-5
FF	30	50 23	09	CC B5 27 00 00 5D 01 00 00	7E		0
FF	30	50 23	09	A4 B5 27 05 00 5D 01 00 00	67		5
FF	30	50 23	09	1C B5 27 0A 00 5D 01 00 00	4C		10
FF	30	50 23	09	74 B5 27 0F 00 5D 01 00 00	55		15
FF	30	50 23	09	30 B5 27 14 00 5D 01 00 00	11		20
FF	30	50 23	09	F4 B5 27 19 00 5D 01 00 00	BE		25
FF	30	50 23	09	E0 B5 27 1E 00 5D 01 00 00	23		30




Fälle
------------------------------------------------------------------------

ITEMS
##########

id=byte, class=setter, command=xxxx

get:byte.setter.bai00, set:byte.setter.bai00
get:byte.setter.bai00, set:byte.setter.bai00, noflush:1
get:word.setter.bai00, set:word.setter.bai00

get:byte.setter.bai00, execute:setter.bai00, refresh:60

id:no_of_firing, class:heating_kw, cmd:no_of_firing, dst:08, src:FF, refresh:60
id:no_of_firing, class:heating_kw, cmd:no_of_firing, value:no_of_firing


Nur lesen                                    get=xxxx.yyyyy

Wert per Befehl senden                        set=xxxx.yyyyy
Mehrere Werte per Befehl senden                set=xxxx.yyyyy, noflush=1


Befehl senden                                execute=FF 00 XX XX
                                            execute=xxxx.yyyyyy
                                            execute=FF 00 XX XX, refresh=60
                                            execute=xxxx.yyyyyy, refresh=60
                                            
Vorhandene Befehler per Alias senden        execute-ON=xxxxx.yyyy, execute-OFF=xxxxx.yyyy
                                            execute-ON=FF 00 XX XX, execute-OFF=FF 00 XX YY
                                            

get=xxx.yyy, set=xxx.yyy, refresh=60

                                            
openhab.cfg
################

ebus:mapping-controller1
burner1


Eng                 | Deu                 | Short    | Master | Slave  | eBUS Spec
---                 | ---                 | ---      | ---    | ---    | ---
Burner Control 1    | Feuerungsautomat 1  | GBC-e    | 0x03   | 0x08   | Feuerungsautomat 1
Burner Automat 1    | Bedienmodul 1       | BM2      | 0x30   | 0x35   | Heizkreisregler 1
Loop 1              | Regelungsplatine 1  | HCM-2    | 0x50   |        | Mischer 1 (Slave only)
Heater Control      | Heizungsregler      | HCM-2?   | 0xF1   | (0xF6) | Heizungsregler (Hauptregler?)
Solar 1             | Solarregler         | SM1      | 0x71   | 0x76   | Heizungsregler (Solarregler)
ISM7i               | ISM7i               | ISM7i    | 0xFF   | 0x05   | PC
Room Control 1      |                     |          |        |        |
Remote Actuator 1   |                     |          |        |        |
PC 1                |                     |          |        |        | 