Documentation for the horizon mediabox binding

## Introduction

This binding supports the horizon mediabox used by cable companies in the Netherlands and some other countries. The box is manufactured by Samsung and known as SMT-G7400 and SMT-G7401. This binding is tested with the SMT-G7401 but should also work with the SMT-G7400. The binding only works when your box is connected to your home network. When you are able to use the HorizonGo remote control app, you should also be able to use this binding.

## Configuration

```
# horizon:<instance>.host = <ip>

# IP address of a horizon box, for example
horizon:livingroom.host=192.168.1.206

```

You can find the ip address of you box at menu->setting-> my homenetwork->status my homenetwork->IPv4 address

## Items

All items use the format described below. The "instance" in the items declaration has to be defined in openhab.cfg first.

```
{horizon="<openHAB-command>:<instance>:<device-command>"}
```

It is not possible to query the box for current states. So you can only send commands to the box. This means the state of your items will not be updated when you send commands to the box with an other app or remote control. Therefor the most convenient way to configure items, is as pushbuttons as described [here](https://github.com/openhab/openhab/wiki/Samples-Item-Definitions#how-to-configure-a-switch-to-be-a-pushbutton).

The commands correspond to the commands on your remote control. The following commands are available:

| Command | Description |
| :------------- |:-------------| 
| POWER | Power |
| OK | Ok |
| BACK | Back |
| CHANNEL_UP | Channel up |
| CHANNEL_DOWN | Channel down |
| HELP | Help |
| MENU | Menu |
| GUIDE | Guide |
| INFO | Info |
| TEXT | Text |
| DPAD_UP | Up |
| DPAD_DOWN | Down |
| DPAD_LEF | Left |
| DPAD_RIGHT | Right |
| 0 | Digit 0 |
| 1 | Digit 1 |
| 2 | Digit 2 |
| 3 | Digit 3 |
| 4 | Digit 4 |
| 5 | Digit 5 |
| 6 | Digit 6 |
| 7 | Digit 7 |
| 8 | Digit 8 |
| 9 | Digit 9 |
| PAUSE | Play/Pause |
| STOP | Stop |
| RECORD | Record |
| FWD | Forward |
| RWD | Rewind |
| ID | Id |
| RC_PAIR | Remote Control pair |
| ONDEMAND | On Demand |
| DVR | DVR |
| TV | TV |

## Examples

openhab.cfg
```
horizon:livingroom.host=192.168.1.206
```

Items

```
Switch  power {horizon="ON:livingroom:POWER", autoupdate="false"}
Switch  ok  {horizon="ON:livingroom:OK", autoupdate="false"}
Switch  back {horizon="ON:livingroom:BACK", autoupdate="false"}
```

There's an URL that is only reachable when the box is switch on and not stand-by. This URL can be used to update the state of your power item as below:

```
rule horizon_state

when  
	Time cron "0 * * * * ?"
then
	var ret_val = sendHttpGetRequest('http://192.168.1.206:62137/DeviceDescription.xml')
	if (ret_val != null) {
		postUpdate(horizon_power, ON)
	} else {
		postUpdate(horizon_power, OFF)
	}
end
```