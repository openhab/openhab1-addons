# item configuration

## configuration parameters

### REMOTEID

The EnOcean ID of the remote device. If not set FF:FF:FF:FF will be used.

### LOCALID

The EnOcean ID of the local side. If not set FF:FF:FF:FF will be used.

You need this for exmaple if you want to send something with a specific sender ID.

### TYPE

The device type that should be used.

The device type identifiers are defined by the aleoncean library.

### PARAMETER

The parameter of the device that should be used by the item.

The parameters of the devices are defined by the aleoncean library.

### CONVPARAM

A parameter that could be used to specify the internal converter if multiple ones are available.

A converter is used to translate between the item and the device parameter.

E.g. a switch could be used to handle a OnOffType (ON or OFF). A rocker switch action could be "up pressed", "up released", "down pressed" or "down released". Here we can define, if how the on / off should be changed.

### ACTIONI

The action that should be taken if an incoming parameter of a devices changed.

Normally you should not change this setting.

You can use 'c', 's' or 'd' (default value).

* If you choose 'c' an incoming parameter change will be submitted to openHAB as a command (if possible).
* If you choose 's' an incoming parameter change will be submitted to openHAB as a state (if possible).
* If you choose 'd' the default logic is applied (mostly state updates).

With this option you change the normal logic, so do it only if you know what you do.

This could for someone be helpful to use direct bindings.
For example if you would like to use a rocker to switch a light of another binding. The other binding normally wants to get a command. So you do not want to get the current state of the rocker but trigger a command.

## Examples

```
// Map a rocker switch to a roller shutter item (up / down / stop) -- see converter documentation
Rollershutter EO_xxyyzz01_ROLLERSHUTTER "Jalousie" {aleoncean="REMOTEID=01:02:03:01,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A"}

// Motion sensor
Number EO_xxyyzz02_TEMPERATURE "Temperatur [%.1f °C]" {aleoncean="REMOTEID=01:02:03:02,TYPE=RD_A5-08-02,PARAMETER=TEMPERATURE_CELSIUS"}
Number EO_xxyyzz02_ILLUMINANCE "Helligkeit [%.1f lx]" {aleoncean="REMOTEID=01:02:03:02,TYPE=RD_A5-08-02,PARAMETER=ILLUMINATION_LUX"}
Number EO_xxyyzz02_POWER "Power [%.1f Volt]" {aleoncean="REMOTEID=01:02:03:02,TYPE=RD_A5-08-02,PARAMETER=SUPPLY_VOLTAGE_V"}
Switch EO_xxyyzz02_MOVEMENT "Bewegung [%s]" {aleoncean="REMOTEID=01:02:03:02,TYPE=RD_A5-08-02,PARAMETER=MOTION"}

// Climate sensor
Number EO_xxyyzz03_TEMPERATURE "Temperatur [%.1f °C]" {aleoncean="REMOTEID=01:02:03:03,TYPE=RD_A5-04-01,PARAMETER=TEMPERATURE_CELSIUS"}
Number EO_xxyyzz03_HUMIDITY "Feuchtigkeit [%.1f %%]" {aleoncean="REMOTEID=01:02:03:03,TYPE=RD_A5-04-01,PARAMETER=HUMIDITY_PERCENT"}

// EEP F6-02-01 and F6-02-02 differ between up / down direction.
Switch ROCKER_DIM_01_A "R (A) up: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=UpPressedReleased"}
Switch ROCKER_DIM_02_A "R (A) up: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-02,PARAMETER=BUTTON_DIM_A,CONVPARAM=UpPressedReleased"}

// Test different converters using Switch item and a rocker switch action
Number ROCKER_TEST_A_N "R (A) [%d]" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A"}
Switch ROCKER_TEST_A_1 "R (A) up: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=UpPressedReleased"}
Switch ROCKER_TEST_A_2 "R (A) down: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=DownPressedReleased"}
Switch ROCKER_TEST_A_3 "R (A) pressed: up / down" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=PressedUpDown"}
Switch ROCKER_TEST_A_4 "R (A) released: up / down" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=ReleasedUpDown"}
Number ROCKER_TEST_B_N "R (B) [%d]" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_B"}
Switch ROCKER_TEST_B_1 "R (B) up: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_B,CONVPARAM=UpPressedReleased"}
Switch ROCKER_TEST_B_2 "R (B) down: pressed / released" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_B,CONVPARAM=DownPressedReleased"}
Switch ROCKER_TEST_B_3 "R (B) pressed: up / down" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_B,CONVPARAM=PressedUpDown"}
Switch ROCKER_TEST_B_4 "R (B) released: up / down" {aleoncean="REMOTEID=01:02:03:04,TYPE=RD_F6-02-01,PARAMETER=BUTTON_DIM_B,CONVPARAM=ReleasedUpDown"}

// Using a local device to simulate rocker switch action
Switch LOCAL_ROCKER_A_PRESS_UP_DOWN "L (A) up: pressed / released" {aleoncean="LOCALID=FF:80:03:04,TYPE=LD_F6-02-01,PARAMETER=BUTTON_DIM_A,CONVPARAM=UpPressedReleased"}
Switch LOCAL_ROCKER_B_PRESS_UP_DOWN "L (B) up: pressed / released" {aleoncean="LOCALID=FF:80:03:04,TYPE=LD_F6-02-01,PARAMETER=BUTTON_DIM_B,CONVPARAM=UpPressedReleased"}

// Smart plug
Number EO_0086FB1E_ENERGY {aleoncean="LOCALID=01:88:66:15,REMOTEID=02:88:FB:1E,TYPE=RD_D2-01-08,PARAMETER=ENERGY_WS"}
Number EO_0086FB1E_POWER {aleoncean="LOCALID=01:88:66:15,REMOTEID=02:88:FB:1E,TYPE=RD_D2-01-08,PARAMETER=POWER_W"}
Switch EO_0086FB1E_ON {aleoncean="LOCALID=01:87:E8:15,REMOTEID=02:88:FB:1E,TYPE=RD_D2-01-08,PARAMETER=SWITCH"}
Switch EO_0103421B_ON {aleoncean="LOCALID=01:87:E8:15,REMOTEID=02:88:42:1B,TYPE=RD_D2-01-08,PARAMETER=SWITCH"}
Switch EO_0083C0F4_ON {aleoncean="LOCALID=FF:F4:0A:81,REMOTEID=02:88:C0:F4,TYPE=RD_D2-01-08,PARAMETER=SWITCH"}
Switch EO_01037079_ON {aleoncean="LOCALID=FF:F4:0A:81,REMOTEID=02:88:70:79,TYPE=RD_D2-01-08,PARAMETER=SWITCH"}
Switch EO_01034F50_ON {aleoncean="LOCALID=FF:F4:0A:82,REMOTEID=02:88:4F:50,TYPE=RD_D2-01-08,PARAMETER=SWITCH"}

```
