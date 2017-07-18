# Panasonic TV Binding

This binding supports Panasonic TVs. It should be compatible with most up-to-date Panasonic Smart-TVs.

## Binding Configuration

This binding can be configured in the file `services/panasonictv.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<instance>` |     |    Yes   | IP address of the Panasonic TV |

### Example

```
bedroom_tv=192.168.1.171
```

## Item Configuration

All items use the following format described below. Currently, only Switch items are allowed that work as toggles. The state of the switches thus do not correspond to the current state of the TV (e.g. power, input, mute etc.).

```
{ panasonictv="<instance>:<command>" }
```

where `<instance>` was introduced in your binding configuration, like `bedroom_tv` in the example above, and `<command>` is from the following table:

| Command | Description |
| :------------- |:-------------| 
| CH_DOWN | Channel down |
| CH_UP | Channel up |
| VOLUP | Volume up |
| VOLDOWN | Volume down |
| MUTE | Mute |
| TV | TV |
| CHG_INPUT | AV |
| RED | Red |
| GREEN | Green |
| YELLOW | Yellow |
| BLUE | Blue |
| VTOOLS | VIERA tools |
| CANCEL | Cancel / Exit |
| SUBMENU | Option |
| RETURN | Return |
| ENTER | Control Center click / enter |
| RIGHT | Control RIGHT |
| LEFT | Control LEFT |
| UP | Control UP |
| DOWN | Control DOWN |
| 3D | 3D button |
| SD_CARD | SD-card |
| DISP_MODE | Display mode / Aspect ratio |
| MENU | Menu |
| INTERNET | VIERA connect |
| VIERA_LINK | VIERA link |
| EPG | Guide / EPG |
| TEXT | Text / TTV |
| STTL | STTL / Subtitles |
| INFO | Info |
| INDEX | TTV index |
| HOLD | TTV hold / image freeze |
| R_TUNE | Last view |
| POWER | Power off |
| REW | Rewind |
| PLAY | Play |
| FF | Fast forward |
| SKIP_PREV | Skip previous |
| PAUSE | Pause |
| SKIP_NEXT | Skip next |
| STOP | Stop |
| REC | Record |
| D1 | Digit 1 |
| D2 | Digit 2 |
| D3 | Digit 3 |
| D4 | Digit 4 |
| D5 | Digit 5 |
| D6 | Digit 6 |
| D7 | Digit 7 |
| D8 | Digit 8 |
| D9 | Digit 9 |
| D0 | Digit 0 |
| P_NR | P-NR (Noise reduction) |
| R_TUNE | Seems to do the same as INFO |
| HDMI1 | Switch to HDMI input 1 |
| HDMI2 | Switch to HDMI input 2 |
| HDMI3 | Switch to HDMI input 3 |
| HDMI4 | Switch to HDMI input 4 |

## Examples

services/panasonictv.cfg

```
bedroom_tv = 192.168.1.171
```

items/panasonictv.items

```
Switch BedroomTVPower           "Power"                 { panasonictv="bedroom_tv:POWER" }
Switch BedroomVolumeUp          "Volume Up"             { panasonictv="bedroom_tv:VOLUP" }
Switch BedroomVolumeDown        "Volume Down"           { panasonictv="bedroom_tv:VOLDOWN" }
Switch BedroomTVInput           "TV input"              { panasonictv="bedroom_tv:TV" }
```
