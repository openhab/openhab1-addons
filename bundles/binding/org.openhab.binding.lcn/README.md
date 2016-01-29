openHAB LCN binding

Author: Tobias Jüttner, Date: 10/28/2015

# Overview

1. [LCN Quick-Reference](#lcnquickreference)
1. [Configuration Guide](#configurationguide)
1. [Items Configuration Guide](#itemsconfigurationguide)
1. [LCN Demo](#lcndemo)
1. [Examples](#examples)  

# <a name="lcnquickreference"></a>1. LCN Quick-Reference

## 1.1 LCN Modules ##

Active LCN components connected to the LCN bus are called *LCN modules*. LCN modules are addressed by their numeric id: Valid range is 5..254

In larger buildings, a second topologic layer is added: *segments*. Valid range is 5..128 or 0 (= no segments exist) or 3 (= target all segments)

Examples: **S000M005**, **S011M100**

LCN modules within the **same** segment can be grouped: Valid range is 5..254 or 3 (= target all groups)

Examples: **S000G022**, **S011G111**

## 1.2 LCN Firmware Versions ##

Each LCN module has a feature-set based on its firmware version. This version is written as follows: \[year since 1990\]\[month\]\[day\]

Each component is written in hexadecimal with 2 characters. Examples:

- 090101 = 1. january 1990
- 0D0C01 = 1. december 2003
- 170206 = 6. feb. 2013

## 1.3 LCN Output-Ports ##

LCN modules support 2 to 4 analog output-ports (number depends on firmware version). Status values are always in percent.

Modules since 170206 have a 0.5%-steps resolution. Older modules have a 2%-steps resolution.

The time it takes the output-port to reach its set-point is called *ramp*.

## <a name="lcnvariables"></a>1.4 LCN Variables

LCN modules support:

- 3 or 12 (since 170206) analog variables for general usage
- 2 regulator set-points
- 5 or 4x4 (since 170206) thresholds (trigger levels)
- 4 S0-input counters (LCN-BU4L must be connected)

## 1.5 LCN Regulators (additions to variables)

LCN modules have 2 regulators. Each one has a set-point and uses one variable as its value-source (see [LCN Variables](#lcnvariables)).  

## 1.6 LCN Relays

LCN modules support up to 8 relays. If no hardware-relays are connected, the relays can still be used as virtual.

## 1.7 LCN Binary-Sensors

LCN modules support up to 8 binary-sensors (hardware periphery must be connected).
  
## 1.8 LCN LEDs (legacy name: *lamps*)

12x multi-state variables used for logic operations.

Values: 0(OFF), 1(ON), 2(BLINK), 3(FLICKER)

## 1.9 LCN Logic-Operations (legacy name: *sums*)

4x multi-state variables representing the result of associated LEDs.

Values: 0(NOT), 1(OR), 2(AND)

## 1.10 LCN Keys

LCN keys are data-points with bound commands. LCN modules support 3 ("A-C") or 4 ("A-D") key-tables (number depends on firmware version).

Each key-table holds 8 keys. Examples: A1, A7, D8

Each key has 3 command types: HIT(press), MAKE(long press), BREAK(long press release)

**Some of these keys can be locked which means the bound (LCN-)commands cannot be executed (via LCN)!**

----------

# <a name="configurationguide"></a>2. Configuration Guide

The *openHAB LCN binding* connects to one or more LCN-PCHK instances via TCP/IP. **This means 1 unused LCN-PCHK license is required!**

The minimum recommended version is LCN-PCHK 2.8 (older versions will also work, but lack some functionality). Visit [http://www.lcn.de](http://www.lcn.de) for updates.

## 2.1 Example configuration for a connection

    lcn:id1=myhome
    lcn:address1=localhost:4114
    lcn:username1=lcn
    lcn:password1=test123
    lcn:mode1=native200
This defines a connnection (#1) to LCN-PCHK on **localhost** port **4114**. **myhome** will be the connection's unique identifier (*connid*, used in openHAB mapping-definitions). **address** can be any IP or host name. The **port** (:4114) is optional.

**mode** specifies the operation mode of the LCN bus:

- LCN busses with **solely** modules with firmware 170206 (feb. 2013) or newer use `native200` (means: output-ports have 200 dimming steps)
- All other LCN busses use `native50` (means: output-ports have 50 dimming steps)

This is an important setting as the operation mode is programmed into the LCN modules by LCN-PRO.
If you experience unexpected behavior while dimming output-ports, the connection is probably in the wrong operation mode!

**For older versions than LCN-PCHK 2.8: Use `percent50` and `percent200` instead (loses some precision).**

## 2.2 Adding a *second* connection (#2)

	lcn:id2=myotherhome
	lcn:address2=...
	...
More connections are defined by incrementing the appended counter (`2` in the example).

**Do not skip any numbers! GOOD: 1,2,3,4 BAD: 1,3,4**

## 2.3 Advanced settings

`lcn:timeout1=2000`  <- Timeout for requests in milliseconds (default = `3500`)

----------

# <a name="itemsconfigurationguide"></a>3 Items Configuration Guide

LCN items are defined by mapping openHAB commands to LCN commands:

Example item with 2 mappings:

    Switch exampleItem "my text" {lcn="[openHABcmd:connid:lcncmd...], [openHABcmd:connid:lcncmd...], ..."}

- **openHABcmd**: ON, OFF, INCREASE, DECREASE... (must be replaced when using the examples)
- **connid**: Unique identifier for the LCN connection. Always `myhome` in the examples here
- **lcncmd...**: What to do
 
**Everything is case-insensitive.**

Visualization of an item is done as follows:

- With an *explicit* visualization mapping ("best practice")
- or implicitly by analyzing all present mappings ("lazy")

**A visualization-mapping should always be the *first one* specified (before all others)!**

The benefits of an *explicit* visualization mapping: By explicitly telling the openHAB item where to get the current status from, it is possible to visualize one thing, but control something completely different!

## 3.1 LCN Addresses

Examples for S000M005:

	openHABcmd:connid:S0.M5...
	openHABcmd:connid:0.5...  ("M" is implicit if missing)
	openHABcmd:connid:0.M005...

Examples for S000G011:

	openHABcmd:connid:0.G11...
	openHABcmd:connid:S0.G11...

## 3.2 LCN Output-Ports

Switch S000M005, output-port 2 (no ramp):

	Switch exampleO2 "M005 output-port 2" {lcn="[myhome:OUTPUT_STATUS.0.5.2], [ON:myhome:ON.0.5.2], [OFF:myhome:OFF.0.5.2]"}

The same using *lazy* visualization and ramp 10s:

	Switch exampleO2 "M005 output-port 2" {lcn="[ON:myhome:ON.0.5.2.10s], [OFF:myhome:OFF.0.5.2.10s]"}

Visualize S000M005, output-port 3 current value (percent):

	Number exampleO3 "M005 output-port 3 [%d]" {lcn="[myhome:OUTPUT_STATUS.0.5.3]"}
  
Relative output-port commands:

- +10.5% output-port 2: `[openHABcmd:myhome:ADD.0.5.2.10,5%]`
- -5% output-port 2: `[openHABcmd:myhome:SUB.0.5.2.5%]`
- alternative for -5%: `[openHABcmd:myhome:REL.0.5.2.-5%]`

Dimmer S000M005, output-port 2 (5% steps):

	Dimmer exampleO2 "M005 output-port 2 [%s]" {lcn="[myhome:OUTPUT_STATUS.0.5.2], [INCREASE:myhome:ADD.0.5.2.5%], [DECREASE:myhome:SUB.0.5.2.5%], [%i:myhome:DIM.0.5.2.%i]"}
**"%i"-mapping is required for HABDroid**

Toggle S000M005, output-port 2: `[openHABcmd:myhome:TOGGLE.0.5.2]`

with ramp 10s: `[openHABcmd:myhome:TOGGLE.0.5.2.10s]  (also valid: 0,5s,1second,10seconds,30sec)`
  
Dim S000M005, output-port 2 to 66,5%: `[openHABcmd:myhome:DIM.0.5.66,5%]`

with ramp 10s: `[openHABcmd:myhome:DIM.0.5.2.66,5%.10s]`
 
**Note: `ON`/`OFF`/`DIM`/`TOGGLE` also support "all outputs an once": Replace the output-port (1,2,3,4) with `ALL`.**

**The only allowed unit for ramps is seconds!**

## 3.3 LCN Variables

Variables can be shown and changed using several measurement units:

- `lcn` (internal native/raw format)
- `°C` / `°Celsius` / `Celsius`
- `°K` / `°Kelvin` / `Kelvin`
- `°F` / `°Fahrenheit` / `Fahrenheit`
- `lx` / `Lux` (for periphery connected to the LCN module's I-port)
- `lx_T` / `Lux_T` (for periphery connected to the LCN module's T-port)
- `m/s`
- `% `/ `Percent` (used for humidity)
- `ppm` (used for CO2)
- `V` / `VOLT`
- `A` / `Amp` / `Ampere`
- `°` (angle) / `Degree`

**All these alternative spellings can be used.**
  
Visualize S000M0005, Var 2/12:

	String exampleVar2 "M005 Var 2 temperature [%s °C]" {lcn="[myhome:VAR_VALUE.0.5.2.°C]"}
	String exampleVar2 "M005 Var 2 [%s]" {lcn="[myhome:VAR_VALUE.0.5.2]"}
  
Visualize S000M0005, Regulator 1 Set-Point:

	String exampleSetPoint1 "M005 regulator 1 set-point [%s °C]" {lcn="[myhome:SETPOINT_VALUE.0.5.1.°C]"}

Set-point control to change regulator 1:

	Number exampleSetPoint "M005 regulator 1 set-point [%f °C]" {lcn="[myhome:SETPOINT_VALUE.0.5.1.°C], [%i:myhome:SETPOINT.0.5.1.%i°C]"}
	Number exampleSetPoint "M005 regulator 1 set-point [%f °C]" {lcn="[%i:myhome:SETPOINT.0.5.1.%i°C]"}  <- Short form
  
Visualize S000M0005, threshold 3-4 (register is 3!):

	String exampleT34 "M005 threshold 3-4 [%s °C]" {lcn="[myhome:THRESHOLD_VALUE.0.5.3.4.°C]"}
  
Visualize S000M0005, S0-input 3/4 (requires LCN-BU4L connected to the LCN module):

	String exampleS0 "M005 S0-input 3 [%s]" {lcn="[myhome:S0_VALUE.0.5.3]"}
  
### 3.4 Changing Variables

Example S000M005, variable 7:

- Add 10°C: `[openHABcmd:myhome:VAR_ADD.0.5.7.10°C]`
- Sub 10°C: `[openHABcmd:myhome:VAR_SUB.0.5.7.10°C]` or `[openHABcmd:myhome:VAR_REL.0.5.7.-10°C]`
- Add raw value 15: `[openHABcmd:myhome:VAR_ADD.0.5.7.15]` or `[openHABcmd:myhome:VAR_ADD.0.5.7.15LCN]`
- Reset to 0: `[openHABcmd:myhome:VAR_RESET.0.5.7]`
- Set to 10°C: `[openHABcmd:myhome:VAR.0.5.7.10°C]`
 
Example S000M005, set-point regulator 2

- Add 1°C: `[openHABcmd:myhome:SETPOINT_ADD.0.5.2.1°C]`
- Reset to 0: `[openHABcmd:myhome:SETPOINT_RESET.0.5.2]`
- Set to 10°C: `[openHABcmd:myhome:SETPOINT.0.5.2.10°C]`
 
Example S000M005, threshold 3-4 (register is 3!):

- Add 10,5°C: `[openHABcmd:myhome:THRESHOLD_ADD.0.5.3.4.10,5°C]`

### 3.4.1 Special Cases

1. `VAR` and `VAR_RESET` with modules before 170206: If the command's target is an LCN group, those older modules require special commands to work: `VAR_OLD`, `VAR_RESET_OLD`
1. Thresholds with modules before 170206: Older modules only have threshold register 1 (but 5 instead of 4 values!). If the command's target is an LCN group, those older modules require special commands to work: `THRESHOLD_ADD_OLD`, `THRESHOLD_SUB_OLD`, `THRESHOLD_REL_OLD`

## 3.5 LCN Regulators (additions to variables)

Lock/unlock S000M005, Regulator 1/2:
	
	Switch exampleLockReg2 "M005 reg.2 lock/unlock" {lcn="[ON:myhome:LOCK_REGULATOR.0.5.1], [OFF:myhome:UNLOCK_REGULATOR.0.5.1]"}

Visualize S000M005, Regulator 2/2 lock-state:

	Contact exampleLockReg1 "M005 reg.2 lock-state" {lcn="[myhome:REGULATOR_LOCK_STATE.0.5.2]"}

## 3.6 LCN Relays

Switch S000M005, relay 6/8:

	Switch exampleRelay6 "M005 relay 6" {lcn="[ON:myhome:RELAYS.0.5.-----1--], [OFF:myhome:RELAYS.0.5.-----0--]"}
  
Visualize S000M005, relay 6:

	Contact exampleRelay6 "M005 relay 6/8" {lcn="[myhome:RELAY_STATE.0.5.6]"}

More command examples:

Toggle relay 1, turn OFF relays 5+6, turn ON relays 7: `[ON:myhome:RELAY.0.5.T---001-]`

## 3.7 LCN Binary-Sensors

Visualize S000M005, Binary-Sensor 4/8:

	Contact exampleWindow "M005 bin.sensor 4" {lcn="[myhome:BINARY_STATE.0.5.4]"}

## 3.8 LCN LEDs (legacy name: *lamps*)

Visualize S000M005, LED 8/12 numeric state:

	Number exampleLed8 "M005 LED 8 [%d]" {lcn="[myhome:LED_STATE.0.5.8]"}

Visualize S000M005 LED 8 text state:

	String exampleLed8 "M005 LED 8 [%s]" {lcn="[myhome:LED_STATE.0.5.8]"}

Visualize S000M005 LED 8 state with custom texts:

	String exampleLed8 "M005 LED 8 [%s]" {lcn="[myhome:LED_STATE.0.5.8.myOFFtext.myONtext.myBLINKtext.myFLICKERtext]"}

Switch S000M005 LED 8 on/off (*lazy* visualization):

	Switch exampleLed8 "M005 LED 8 on/off" {lcn="[ON:myhome:LED.0.5.8.ON], [OFF:myhome:LED.0.5.8.OFF]}

Switch S000M005 LED 8 blink/flicker (*lazy* visualization):
	
	Switch exampleLed8 "M005 LED 8 on/off" {lcn="[ON:myhome:LED.0.5.8.FLICKER], [OFF:myhome:LED.0.5.8.BLINK]}

## 3.9 LCN Logic-Operations (legacy name: *sums*)

Visualize S000M005 logic-op. 3 numeric state:

	Number exampleOp3 "M005 logic-op. 3 [%d]" {lcn="[myhome:LOGICOP_STATE.0.5.3]"}

Visualize S000M005 logic-op. 3 text state:

	String exampleOp3 "M005 logic-op. 3 [%s]" {lcn="[myhome:LOGICOP_STATE.0.5.3]"}

Visualize S000M005 logic-op. 3 state with custom texts:

	String exampleOp3 "M005 logic-op. 3 [%s]" {lcn="[myhome:LOGICOP_STATE.0.5.3.myNOTtext.myORtext.myANDtext]"}

## 3.10 LCN Keys

Sending keys (a.k.a. execute bound commands):

Example: S000M005, keys A1,A5,D8

- "hit": `[openHABcmd:myhome:KEYS.0.5.A1A5D8]` or `[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT]`
- "make": `[openHABcmd:myhome:KEYS.0.5.A1A5D8.MAKE]`
- "break": `[openHABcmd:myhome:KEYS.0.5.A1A5D8.BREAK]`

Sending keys deferred/delayed:

Example: S000M005, keys A1,A5,D8

- "hit" in 10s: `[openHABcmd:myhome:KEYS.0.5.A1A5D8.10s]` (range 1..60, also valid: `1second`,`10seconds`,`30sec`)
- "hit" in 10m: `[openHABcmd:myhome:KEYS.0.5.A1A5D8.10m]` (range 1..90, also valid: `1minute`,`10minutes`,`45min`)
- "hit" in 1h: `[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT.1h]` (range 1..50, also valid: `1hour`,`24hours`)
- "hit" in 7d: `[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT.7d]` (range 1..45, also valid: `1day`,`7days`)

**"make"/"break" is NOT supported!**

Change lock-state S000M005 lock C1, unlock C2, toggle C7:

	[openHABcmd:myhome:LOCK.0.5.C.10----T-]

Switch S000M005, show lock-state of key C4, control lock-state of C4,C5,C5:

	Switch exampleC456 "M005 C4(,C5,C6) lock" {lcn="[myhome:LOCK_STATE.0.5.C4], [ON:myhome:LOCK.0.5.C.---111--], [OFF:myhome:LOCK.0.5.C.---000--]"}

The same using *lazy* visualization (will visualize C4, as it is the first key in the list):

	Switch exampleC456 "M005 C4 lock" {lcn="[ON:myhome:LOCK.0.5.C.---111--], [OFF:myhome:LOCK.0.5.C.---000--]"}

Lock keys A1,A2,A3 for 10m: `[openHABcmd:myhome:LOCK.0.5.A1A2A3.10m]`

**Same time-values as `KEYS` command.**
**Only table A is supported!**

## 3.11 Misc Commands

Dynamic text for LCN-GTxD displays (support 4 independent text rows):

Example: S000M005 row 1/4:

	[openHABcmd:myhome:DYNTEXT.0.5.1.text up to 60 characters]

**The text will be encoded as UTF-8 which means non-ASCII characters will reduce the total available length.**

Other commands can be sent by using the LCN-PCK format.

**There will be no implicit *lazy* visualization for these commands!**

Beep S000M005 4 times (must be allowed => LCN-PRO):

- tone 1: `[openHABcmd:myhome:PCK.0.5.PIN4]` (range 1..15)
- tone 2: `[openHABcmd:myhome:PCK.0.5.PIS004]`
  
Store current output-port value(s) as light-scene 6/10:

- store output 1: `[openHABcmd:myhome:PCK.0.5.SZS1005]` (range is 0..9)
- store output 2: `[openHABcmd:myhome:PCK.0.5.SZS2005]`
- store output 3(+4): `[openHABcmd:myhome:PCK.0.5.SZS4005]`

**Storing multiple outputs at once: 1+2+3(+4): `SZS7`, 1+2: `SZS3`, 1+3: `SZS5`, 2+3: `SZS6`**

Recall light-scene 6/10:

recall output 1: `[openHABcmd:myhome:PCK.0.5.SZA1005]` (range is 0..9)

**Same as store, just `SZAx` instead of `SZSx`.**

Change light-scene register:

to register 5/10: `[openHABcmd:myhome:PCK.0.5.SZW004]` (range is 0..9)

----------

# <a name="lcndemo"></a>4. LCN Demo

ISSENDORFF KG has a LCN-demo set-up that is reachable via internet.

## openhab.cfg

	lcn:id1=lcndemo
	lcn:address1=access.lcn.de:5225
	lcn:username1=lcn
	lcn:password1=lcn
	lcn:mode1=native200


## Items

	Contact lcnMotionDetect "Motion detect" {lcn="[lcndemo:BINARY_STATE.0.5.4]"}
	Contact lcnRelayVis "Relay" {lcn="[lcndemo:RELAY_STATE.0.5.1]"}
	Switch lcnRelay "Relay" {lcn="[ON:lcndemo:RELAYS.0.5.1-------], [OFF:lcndemo:RELAYS.0.5.0-------]"}
	Switch lcnLightSwitch "Light" {lcn="[ON:lcndemo:ON.0.5.1.2s], [OFF:lcndemo:OFF.0.5.1.2s]"}
	Dimmer lcnLightDimmer "Light [%s]" {lcn="[lcndemo:OUTPUT_STATUS.0.5.1], [INCREASE:lcndemo:ADD.0.5.1.5%], [DECREASE:lcndemo:SUB.0.5.1.5%], [%i:lcndemo:DIM.0.5.1.%i]"}
	Number lcnCounter "Counter [%d]" {lcn="[lcndemo:VAR_VALUE.0.5.1]"}
	String lcnTemp "Temperature [%s °C]" {lcn="[lcndemo:VAR_VALUE.0.5.2.°C]"}
	String lcnSetPoint "Set-point [%s °C]" {lcn="[lcndemo:SETPOINT_VALUE.0.5.1.°C]"}
	Number lcnSetPointDim "Set-point (10-23°C) [%f °C]" {lcn="[lcndemo:SETPOINT_VALUE.0.5.1.°C], [%i:lcndemo:SETPOINT.0.5.1.%i°C]"}
	String lcnRegulatorTarget "Regulator [%s]" {lcn="[lcndemo:OUTPUT_STATUS.0.5.3]"}
	Switch lcnRegulatorLock "Regulator lock" {lcn="[ON:lcndemo:LOCK_REGULATOR.0.5.1], [OFF:lcndemo:UNLOCK_REGULATOR.0.5.1]"}
	String lcnBright "Brightness [%s lx]" {lcn="[lcndemo:VAR_VALUE.0.5.3.lx]"}
	String lcnCO2 "CO2 [%s ppm]" {lcn="[lcndemo:VAR_VALUE.0.5.4.ppm]"}
	String lcnThreshold1 "Threshold 1 [%s °C]" {lcn="[lcndemo:THRESHOLD_VALUE.0.5.1.1.°C]"}
	String lcnThreshold2 "Threshold 2 [%s °C]" {lcn="[lcndemo:THRESHOLD_VALUE.0.5.1.2.°C]"}
	String lcnThreshold3 "Threshold 3 [%s °C]" {lcn="[lcndemo:THRESHOLD_VALUE.0.5.1.3.°C]"}
	String lcnThreshold4 "Threshold 4 [%s °C]" {lcn="[lcndemo:THRESHOLD_VALUE.0.5.1.4.°C]"}

## Sitemap

	sitemap default label="LCN Demo"
	{
		Frame label="Coffee" {
			Text item=lcnMotionDetect
			Text item=lcnRelayVis
			Switch item=lcnRelay icon="none"
			Switch item=lcnLightSwitch
			Slider item=lcnLightDimmer
			Text item=lcnCounter
			Text item=lcnTemp
			Text item=lcnSetPoint
			Setpoint item=lcnSetPointDim step=0.5 minValue=10 maxValue=23
			Text item=lcnRegulatorTarget
			Switch item=lcnRegulatorLock
			Text item=lcnBright
			Text item=lcnCO2
			Text item=lcnThreshold1
			Text item=lcnThreshold2
			Text item=lcnThreshold3
			Text item=lcnThreshold4
		}
	}

## Visualization (through LCN-GVS)

Address: [http://access.lcn.de/LCNGVSDemo](http://access.lcn.de/LCNGVSDemo) -> OpenHAB

Direct address: [http://access.lcn.de/LCNGVSDemo/control.aspx?ui=coffee&proj=OpenHAB](http://access.lcn.de/LCNGVSDemo/control.aspx?ui=coffee&proj=OpenHAB)

Login: guest

Password: lcn

----------

# <a name="examples"></a>5. Examples

**All examples use S000M005 or S000G006**

## 5.1 LCN Output-Ports

	[myhome:OUTPUT_STATUS.0.5.1]
	[openHABcmd:myhome:ON.0.5.1]
	[openHABcmd:myhome:ON.0.5.ALL]
	[openHABcmd:myhome:OFF.0.5.1]
	[openHABcmd:myhome:OFF.0.5.ALL]
	[openHABcmd:myhome:TOGGLE.0.5.1]
	[openHABcmd:myhome:TOGGLE.0.G6.ALL]
	
	[openHABcmd:myhome:DIM.0.5.1.50%]
	[openHABcmd:myhome:DIM.0.5.1.50%.10s]
	[openHABcmd:myhome:DIM.0.G6.ALL.50%.10s]
	[%i:myhome:DIM.0.5.1.%i]
	
	[openHABcmd:myhome:ADD.0.5.1.10%]
	[openHABcmd:myhome:SUB.0.5.1.10,5%]
	[openHABcmd:myhome:REL.0.5.1.-10%]

## 5.2 LCN Variables

Measurement units:

- `lcn`
- `°C`, `°Celsius`, `Celsius`
- `°K`, `°Kelvin`, `Kelvin`
- `°F`, `°Fahrenheit`, `Fahrenheit`
- `lx`, `Lux`, `lx_T`, `Lux_T`
- `m/s`
- `%`, `Percent`
- `ppm`
- `V`, `Volt`
- `A`, `Amp`, `Ampere`
- `°`, `Degree`

### Variables

	[myhome:VAR_VALUE.0.5.1]
	[myhome:VAR_VALUE.0.5.1.°C]
	[myhome:VAR_VALUE.0.5.1.lx]
	[openHABcmd:myhome:VAR.0.5.1.10°C]
	[openHABcmd:myhome:VAR_OLD.0.5.1.10°C]  <- For group-targets before 170206
	[openHABcmd:myhome:VAR_ADD.0.5.1.10°C]
	[openHABcmd:myhome:VAR_SUB.0.5.1.10,5°C]
	[openHABcmd:myhome:VAR_REL.0.5.1.-10°C]
	[openHABcmd:myhome:VAR_RESET.0.5.1]
	[openHABcmd:myhome:VAR_RESET_OLD.0.5.1]  <- For group-targets before 170206

### Set-Points
	
	[myhome:SETPOINT_VALUE.0.5.1]
	[myhome:SETPOINT_VALUE.0.5.1.°C]
	[openHABcmd:myhome:SETPOINT.0.5.1.10°C]
	[openHABcmd:myhome:SETPOINT_ADD.0.5.1.10°C]
	[openHABcmd:myhome:SETPOINT_SUB.0.5.1.10,5°C]
	[openHABcmd:myhome:SETPOINT_REL.0.5.1.-10°C]
	[openHABcmd:myhome:SETPOINT_RESET.0.5.1]
	[%i:myhome:SETPOINT.0.5.1.%i°C]

### Thresholds
	
	[myhome:THRESHOLD_VALUE.0.5.1.1]
	[myhome:THRESHOLD_VALUE.0.5.1.1.°C]
	[openHABcmd:myhome:THRESHOLD_ADD.0.5.1.1.10°C]
	[openHABcmd:myhome:THRESHOLD_SUB.0.5.1.1.10,5°C]
	[openHABcmd:myhome:THRESHOLD_REL.0.5.1.1.-10°C]

**For group-targets before 170206: `THRESHOLD_ADD_OLD`, `THRESHOLD_SUB_OLD`, `THRESHOLD_REL_OLD`**

### S0-Input

	[myhome:S0_VALUE.0.5.1]

## 5.3 LCN Regulators (additions to variables)

	[myhome:REGULATOR_LOCK_STATE.0.5.1]
	[openHABcmd:myhome:LOCK_REGULATOR.0.5.1]
	[openHABcmd:myhome:UNLOCK_REGULATOR.0.5.1]

## 5.4 LCN Relays

	[myhome:RELAY_STATE.0.5.1]
	[openHABcmd:myhome:RELAYS.0.5.-----1--]
	[openHABcmd:myhome:RELAYS.0.5.-----0--]
	[openHABcmd:myhome:RELAYS.0.G6.----TTTT]  <- Toggle

## 5.5 LCN Binary-Sensors

	[myhome:BINARY_STATE.0.5.1]

## 5.6 LCN LEDs (legacy name: *lamps*)

	[myhome:LED_STATE.0.5.1]
	[myhome:LED_STATE.0.5.1.myOFFtext.myONtext.myBLINKtext.myFLICKERtext]
	[openHABcmd:myhome:LED.0.5.1.ON]
	[openHABcmd:myhome:LED.0.5.8.OFF]
	[openHABcmd:myhome:LED.0.5.8.BLINK]
	[openHABcmd:myhome:LED.0.5.8.FLICKER]

## 5.7 LCN Logic-Operations (legacy name: *sums*)

	[myhome:LOGICOP_STATE.0.5.1]
	[myhome:LOGICOP_STATE.0.5.1.myNOTtext.myORtext.myANDtext]

## 5.8 LCN Keys

	[openHABcmd:myhome:KEYS.0.5.A1A5D8]
	[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT]
	[openHABcmd:myhome:KEYS.0.G6.A1A5D8.MAKE]
	[openHABcmd:myhome:KEYS.0.G6.A1A5D8.BREAK]
	
	[openHABcmd:myhome:KEYS.0.5.A1A5D8.10s]    (range 1..60, also valid: 1second,10seconds,30sec)
	[openHABcmd:myhome:KEYS.0.5.A1A5D8.10m]    (range 1..90, also valid: 1minute,10minutes,45min)
	[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT.1h] (range 1..50, also valid: 1hour,24hours)
	[openHABcmd:myhome:KEYS.0.5.A1A5D8.HIT.7d] (range 1..45, also valid: 1day,7days)

**"make"/"break" is NOT supported!**

	[myhome:LOCK_STATE.0.5.A1]
	[openHABcmd:myhome:LOCK.0.5.A.-10-----]
	[openHABcmd:myhome:LOCK.0.5.D.------TT]  <- Toggle
	
	[openHABcmd:myhome:LOCK.0.5.A1A2A3.10s]
	[openHABcmd:myhome:LOCK.0.5.A1A2A3.10m]
	[openHABcmd:myhome:LOCK.0.5.A1A2A3.1h]
	[openHABcmd:myhome:LOCK.0.5.A1A2A3.7d]

**Only table A is supported!**

## 5.9 Misc Commands

	[openHABcmd:myhome:DYNTEXT.0.5.1.text in row 1]
	[openHABcmd:myhome:DYNTEXT.0.5.4.text in row 4]