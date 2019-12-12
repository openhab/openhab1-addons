# Hue Binding

This binding connects openHAB to your Philips Hue lighting system.

Click the image below for a YouTube video demonstrating setup, configuration and a simple custom scene controller (openHAB 1, but still relevant to usage on openHAB 2).

[![openHAB Hue binding](http://img.youtube.com/vi/Q4_LkXIRBWc/0.jpg)](http://www.youtube.com/watch?v=Q4_LkXIRBWc)

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/hue/).

## Prerequisites

### Pairing the Philips Hue bridge

In order to use your Philips Hue system within openHAB, you need to publicize openHAB to your Philips Hue bridge.  To do so, you need to link the systems by pressing the connect button on the bridge when starting up openHAB.  In the `openhab.log` file you can see when openHAB is waiting to be paired to the bridge. Look out for the following lines:

```
Please press the connect button on the Hue bridge. Waiting for pairing for 100 seconds...
Please press the connect button on the Hue bridge. Waiting for pairing for 99 seconds...
Please press the connect button on the Hue bridge. Waiting for pairing for 98 seconds...
```

If you see this you should press the button on the bridge. You should see the following in the logs:

```
Hue bridge successfully paired!
```

This procedure has only to be done once. Now you are ready to go.

Additional note for Philips HUE gen1 (the round shaped HUE):

With the most recent firmware applied (Dec. 10, 2016) the Automatic pairing no longer works in openhab 1.8.3.
Pressing the connect button does nothing during the "Waiting for pairing.." phase of openhab startup.

To workaround this issue: 

Install the HUE2 app from the Android store onto a phone or tablet or Android emulator.

Launch the app.  It will ask you to press the Pair button on the HUE, press it, the phone is now paired.

Now use a tool like Wireshark to monitor traffic from your Phone to the HUE. You'll see your phone makes GET requests to the HUE with a URL like this: 

GET /api/y9vSIEglT3JeBFQpDXqPYwvm-6Q-juiVdzjMxXuC/config HTTP/1.1

Take the portion between the (api/ /config) to steal this connections "secret" to be used on the gen1 that
can't pair on its own. Set the hue:secret value to the data found while spying on the connection from the android app:

```
secret=y9vSIEglT3JeBFQpDXqPYwvm-6Q-juiVdzjMxXuC
```

Then, in Android, uninstall the app, or goto settings -> applications -> HUE2 -> "clear data" & "clear cache"

This ensures a new secret will be generated for the android app so there are not two devices sharing the same "secret" on the same network.

For more information on this issue please see: https://github.com/openhab/openhab1-addons/pull/4311

This issue is/will be fixed in Openhab 1.8.4 or 1.9-SNAPSHOT.

# Binding Configuration

This binding can be configured with the file `services/hue.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         | preferred | IP address of the Hue bridge. If it is not provided, the binding tries to find the bridge on its own. This may not always work perfectly. The preferred way should be a defined IP. |
| secret   |         |   Yes    | a string that gets stored in the hue bridge when pairing it with openHAB. That way the bridge 'knows' openHAB and allows it to send commands. It is kind of a password. Be aware that it is not encrypted in the communication. You may change this value to anything you like using characters and numbers. It must be between 10 and 40 characters long. |
| refresh  |         |   No     | defines the polling interval in milliseconds (1000 milliseconds = 1 second) to retrieve Philips bulb status. Polling is enabled if refresh is specified. Be aware that polling will consume resources, so a small refresh interval will increase CPU load. |

Other apps can change Hue status or a physical switch can turn on / off lamp status. If this happens the status of hue lamps within OpenHAB won't reflect the real status.

Currently (September 2014) there is no push technology available, so the only option is to poll Philips bulbs to retrieve status and update items accordingly to reflect changes.

## Item Configuration

In order to bind an item to a Philips Hue bulb, you need to provide configuration settings. The easiest way to do so is to add some binding information in your items file. The syntax for the Philips Hue binding configuration string is explained in the following sections.

### Switch items

The switch item is the easiest way to control your bulbs. It enables you to turn on and off your bulbs without changing color or brightness.

```
Switch <Item_Name> hue="<bulb number>"
```

The bulb number is assigned to the bulb by your Philips Hue bridge. The numbers should start with 1 and increase for every connected bulb by 1. If you have a starter kit, the first bulbs are numbered 1, 2, 3. 

Here are some examples of valid binding configuration strings for switch items:

```
Switch  Light_GF_Lounge_BackCorner     { hue="1" }
Switch  Light_GF_Lounge_FrontCorner    { hue="2" }
Switch  Light_GF_Lounge_Ceiling        { hue="3" }
```

### Color items

The color item allows you to change color and brightness of a bulb.

```
Color <Item_Name> hue="<bulb number>"
```

Here are some examples of valid binding configuration strings for color items:

```
Color  Light_GF_Lounge_BackCorner_C     {hue="1"}
Color  Light_GF_Lounge_FrontCorner_C    {hue="2"}
Color  Light_GF_Lounge_Ceiling_C        {hue="3"}
```

### Dimmer items

Dimmer items enable you to do two different things:

1. Change the brightness of a bulb without changing the color, or
1. Change the white color temperature of a bulb from warm to cold.

#### Brightness dimmer items

```
Dimmer <Item_Name> hue="<bulb number>;brightness[;<step size>]"
```

where the part in `[]` is optional.

The step size defines how fast the dimmer changes the brightness. If no value is defined the default value of 25 is used.

Here are some examples of valid binding configuration strings for brightness dimmer items:

```
Dimmer  Light_GF_Lounge_BackCorner_B     {hue="1;brightness"}
Dimmer  Light_GF_Lounge_FrontCorner_B    {hue="2;brightness;20"}
Dimmer  Light_GF_Lounge_Ceiling_B        {hue="3;brightness;100"}
```

#### Color temperature dimmer items

```
Dimmer <Item_Name> hue="<bulb number>;colorTemperature[;<step size>]"
```

where the part in `[]` is optional.

The step size defines how fast the dimmer changes the color temperature. If no value is defined the default value of 25 is used.

Here are some examples of valid binding configuration strings for color temperature dimmer items:

```
Dimmer  Light_GF_Lounge_BackCorner_T     {hue="1;colorTemperature"}
Dimmer  Light_GF_Lounge_FrontCorner_T    {hue="2;colorTemperature;20"}
Dimmer  Light_GF_Lounge_Ceiling_T        {hue="3;colorTemperature;100"}
```

## Examples

### Items

As a result, your lines in the items file might look like the following:

```
Switch Light_GF_Lounge_BackCorner   "Back corner"     (gLights) {hue="1"}
Switch Light_GF_Lounge_FrontCorner  "Front corner"    (gLights) {hue="2"}
Switch Light_GF_Lounge_Ceiling      "Ceiling light"   (gLights) {hue="3"}

Color  Light_GF_Lounge_BackCorner_C    "Back Color"     (gLights)  {hue="1"}
Color  Light_GF_Lounge_FrontCorner_C   "Front Color"    (gLights)  {hue="2"}
Color  Light_GF_Lounge_Ceiling_C       "Ceiling Color"   (gLights)  {hue="3"}

Dimmer  Light_GF_Lounge_BackCorner_B    "Back Dimmer"     (gLights)  {hue="1;brightness;30"}
Dimmer  Light_GF_Lounge_FrontCorner_B   "Front Dimmer"    (gLights)  {hue="2;brightness;30"}
Dimmer  Light_GF_Lounge_Ceiling_B       "Ceiling Dimmer"  (gLights)  {hue="3;brightness;30"}

Dimmer  Light_GF_Lounge_BackCorner_T    "Back Temperature"      (gLights)  {hue="1;colorTemperature;30"}
Dimmer  Light_GF_Lounge_FrontCorner_T   "Front Temperature"     (gLights)  {hue="2;colorTemperature;30"}
Dimmer  Light_GF_Lounge_Ceiling_T       "Ceiling Temperature"   (gLights)  {hue="3;colorTemperature;30"}
```

### Sitemap

#### Basic

Using the above example items file as an example. our sitemap file might look like the following:

```
Switch         item=Light_GF_Lounge     label="Lounge Light Switch"
Colorpicker    item=Light_GF_Lounge_C   label="Lounge Color"
Slider         item=Light_GF_Lounge_B   label="Lounge Brightness" 
Slider         item=Light_GF_Lounge_T   label="Lounge Color Temperature" 
```

#### Using a Dynamic Sitemap to hide clutter

By adding a visibility tag (see [[Explanation-of-Sitemaps#visibility]] for more) we can hide the widgets for the color, brightness and color temperature if the light is not on. 

```
Switch         item=Light_GF_Lounge                                          label="Lounge Light Switch"
Colorpicker    item=Light_GF_Lounge_C    visibility=[Light_GF_Lounge==ON]    label="Lounge Color"
Slider         item=Light_GF_Lounge_B    visibility=[Light_GF_Lounge==ON]    label="Lounge Brightness" 
Slider         item=Light_GF_Lounge_T    visibility=[Light_GF_Lounge==ON]    label="Lounge Color Temperature"  
```

### Rules

Use the corresponding items within the rules:

```
sendCommand(Light_GF_Lounge, ON)
sendCommand(Light_GF_Lounge_C, HSBType::GREEN)
sendCommand(Light_GF_Lounge_B, 20)
sendCommand(Light_GF_Lounge_T, 60)
```

To set a custom color within a rule file:

```
var DecimalType hue = new DecimalType(240) // 0-360; 0=red, 120=green, 240=blue, 360=red(again)
var PercentType sat = new PercentType(100) // 0-100
var PercentType bright = new PercentType(100) // 0-100
var HSBType light = new HSBType(hue,sat,bright)
sendCommand(Light_GF_Lounge_C, light)
```

Or use variables for the brightness:

```
var PercentType bright = new PercentType(30) // 0-100
sendCommand(Light_GF_Lounge_B, bright)
```

For more information on the used API see the following link: http://developers.meethue.com/

If you like dedicated Hue icons, please consider using those posted in the forum: https://groups.google.com/d/msg/openhab/1FXial-JCA0/gjd1Fq-sniQJ. Versions of openHAB after March 2015 include these images in the release. You can now show icons using:

```
Switch Light_GF_Lounge_C   "left bulb"   <hue>   (Switching) {hue="1"}
```

## Indirectly using a LivingColors remote or Hue Dimmer Switch

The LivingColors remote is only compatible to Philips branded bulbs. We can't control for e.g. Osram bulbs with this remote. However the Hue Bridge can control these bulbs, since it fully supports the underlying ZigBee protocol - I guess the LivingColor remote and Philips bulbs use some proprietary protocol to communicate with each other.

I use one of my LivingColor remote's scene for a sunset-like ambience. The following rule basically listens for a certain hue value from one of my Philips bulbs (which can be controlled by the remote) and if these values match (=when I activated the sunset scene) it sends a command to the unsupported bulb. This turns on the light with a delay of several seconds, since OpenHAB polls the values from the bridge every few seconds and while the bridge itself is also just polling the values.

```
rule "LivingColorsRemoteGrab"
when
  Item philipsColor1 received update
then
  logInfo("RemoteGrab", "philipsColor1 was updated")
  
  if (philipsColor1.state instanceof HSBType) {
    var HSBType currentState
    currentState = philipsColor1.state

    var DecimalType hue = currentState.getHue()
    var PercentType sat = currentState.getSaturation()
    var PercentType bright = currentState.getBrightness()
    
    // Check if new values match our scene's values pressed on LivingColors remote
    if ( hue > 23 && hue < 25 && sat == 100 ) {
      logInfo("RemoteGrab", "philipsColor1 was set to SUNSET")

      // Send command to unsuppoted bulb
      sendCommand(osramColor1, new HSBType(new DecimalType(37.5),new PercentType(100),new PercentType(100)))
    }
  }
end
```
