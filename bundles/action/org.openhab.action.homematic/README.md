# Homematic Action

The Homematic action lets you send messages to a Homematic remote control with a display, currently the HM-RC-19-B (Radio remote control 19 button).

## Prerequisites

The Homematic Binding (1.x) must be installed and configured.

## Actions

*   `sendHomematicDisplay(REMOTE_CONTROL_ADDRESS, TEXT)`
*   `sendHomematicDisplay(REMOTE_CONTROL_ADDRESS, TEXT, OPTIONS)`

The remote control display is limited to five characters; a longer text is truncated.

You have several additional options to control the display.

*   `BEEP` _(TONE1, TONE2, TONE3)_ - let the remote control beep
*   `BACKLIGHT` _(BACKLIGHT_ON, BLINK_SLOW, BLINK_FAST)_ - control the display backlight
*   `UNIT` _(PERCENT, WATT, CELSIUS, FAHRENHEIT)_ - display one of these units
*   `SYMBOL` _(BULB, SWITCH, WINDOW, DOOR, BLIND, SCENE, PHONE, BELL, CLOCK, ARROW_UP, ARROW_DOWN)_ - display symbols, multiple symbols possible

You can combine any option, they must be separated by a comma.
If you specify more than one option for BEEP, BACKLIGHT and UNIT, only the first one is taken into account and all others are ignored.
For SYMBOL you can specify multiple options.

## Examples

Show message TEST:

```
sendHomematicDisplay("KEQ0012345", "TEST");
```

Show message TEXT, beep once and turn backlight on:

```
sendHomematicDisplay("KEQ0012345", "TEXT", "TONE1, BACKLIGHT_ON");
```

Show message 15, beep once, turn backlight on and shows the Celsius unit:

```
sendHomematicDisplay("KEQ0012345", "15", "TONE1, BACKLIGHT_ON, CELSIUS");
```

Show message ALARM, beep three times, let the backlight blink fast and shows a bell symbol:

```
sendHomematicDisplay("KEQ0012345", "ALARM", "TONE3, BLINK_FAST, BELL");
```

Duplicate options (TONE3 is ignored, because TONE1 is specified previously):

```
sendHomematicDisplay("KEQ0012345", "TEXT", "TONE1, BLINK_FAST, TONE3");
```
