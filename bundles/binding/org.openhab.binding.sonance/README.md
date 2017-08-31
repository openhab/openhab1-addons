# Sonance Binding

This binding integrates with [Sonance DSP Amplifiers](http://www.sonance.com/electronics/amplifiers/dsp). It supports all three models (DSP 2-150, DSP 8-130 and DSP 2-750) but for now it's only tested with the DSP 8-130.  For each group you can enable or disable sound (toggle mute) or set the volume.

## Binding Configuration

This binding can be configured in the file `services/sonance.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 60000   |   No     | refresh rate in milliseconds. 60000 is one minute.  When a command for a new volume or mute toggle is pressed, the value is updated immediately. So their is no need to lower the refresh rate to get a faster response. |

## Item Configuration

Please see the examples below.

## Examples

items/sonancedemo.items

```
/* Sonance items*/
Switch 	 Sonance "Amplifier" {sonance="10.0.0.8:52000:power"}

Switch 	 Sonance_bedroom_mute "Bedroom" {sonance="10.0.0.8:52000:mute:00"}
Number 	 Sonance_bedroom_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:00"}

Switch 	 Sonance_bathroom_mute "Bathroom" {sonance="10.0.0.8:52000:mute:01"}
Number 	 Sonance_bathroom_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:01"}

Switch 	 Sonance_spare_room_mute "Spare room" {sonance="10.0.0.8:52000:mute:02"}
Number 	 Sonance_spare_room_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:02"}

Switch 	 Sonance_office_mute "Office" {sonance="10.0.0.8:52000:mute:03"}
Number 	 Sonance_office_volume "Volume [%.0f db]" <chart> {sonance="10.0.0.8:52000:volume:03"}
```

sitemaps/sonancedemo.sitemap.fragment

```
Frame label="Amplifier" {
	Switch item=Sonance

	Switch item=Sonance_bedroom_mute
	Setpoint item=Sonance_bedroom_volume minValue=-70.0 maxValue=12
	
	Switch item=Sonance_office_mute
	Setpoint item=Sonance_office_volume minValue=-70.0 maxValue=12
	
	Switch item=Sonance_bathroom_mute
	Setpoint item=Sonance_bathroom_volume minValue=-70.0 maxValue=12

	Switch item=Sonance_spare_room_mute
	Setpoint item=Sonance_spare_room_volume minValue=-70.0 maxValue=12				
}			
```

## Troubleshooting

In order to diagnose issues with the binding set the binding's rooter logger `org.openhab.binding.sonance` to `DEBUG` or `TRACE` and request assistance at the [openHAB Community](https://community.openhab.org).

## Known Issues

1. Getting current power status from the amplifier fails because of a bug in the Sonance software version 2.31. This is fixed in the Sonance firmware version 2.39.
1. The auto on method "music" doesn't work when using an digital input module. Sonance reports this is a hardware limitation which will never be fixed.
1. Absolute commands to directly set a volume (so not relative like UP and DOWN) need firmware version v2.51 or higher. Most GUI's make the change directly (like the Android app and the web GUI).
