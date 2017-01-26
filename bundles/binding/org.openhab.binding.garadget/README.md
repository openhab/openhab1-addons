# Garadget Binding

The [Garadget](http://garadget.com) is cloud-based device that "futurizes" your existing garage door opener, letting you open and close your garage door from anywhere, including via openHAB with this binding.  Remote access is made possible with the [particle.io](https://www.particle.io/) REST API.

![garadget](https://watou.github.io/images/garadget-1.png)

## Binding Configuration

This binding can be configured in the file `services/garadget.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 180000  |   No     | refresh interval (defaults to three minutes) |
| username |         |   Yes    | username for accessing your account |
| password |         |   Yes    | password for accessing your account. it is your responsbility to ensure that no one can access your username or password |
| granularity | 5000 |   No     | rate at which to check if poll is to run, in milliseconds |
| timeout  | 5000    |   No     | time in milliseonds to allow an API request to complete |


## Item Configuration

The syntax for the Garadget binding configuration string is explained below.

Garadget bindings start with a `<` or `>`, to indicate if the item receives values from the API (in binding) or sends values to the API (out binding), respectively.  A single `garadget=` binding string can contain multiple in and out bindings separated by commas, so that a single openHAB item can be used to send commands to your Garadget device(s) as well as receive state updates.  The Garadget binding suppresses auto-updates of items that are sent commands, so that the actual state reported back from Garadget is used.  See the `doorState` example item below.

The first character (`<` or `>`) is then followed by a section between square brackets (`[` and `]` characters):

```
[<device>#<property>]
```

Where `<device>` is a device identifier and `<property>` is either a device field or variable (in binding) or function (out binding).

> *Where can I find my device identifier?*
> A device identifier is a long decimal number.  One way to find the number is to login to [garag.io](http://garag.io/my/) and read the URL you were taken to in the browser's address bar:
> http://garag.io/my/settings.php?id=270041234567343432313031
> The final component of the URL is your device identifier.

## Examples

Here are some examples of valid binding configuration strings, as you would define in your .items file.  Change the device identifiers below to match your Garadget(s).

```
Group Garadget
Group UI

String name               "Garage Door [%s]"              <rollershutter> (Garadget,UI) { garadget="<[270041234567343432313031#name]" }

// A Contact item supports open and closed, but a Garadget doorStatus_status can be: 
// closed, open, closing, opening, stopped
// (as documented here: https://github.com/Garadget/firmware#door-states-status)
String doorStatus_status  "Status [%s]"                      <garagedoor> (Garadget,UI) { garadget="<[270041234567343432313031#doorStatus_status]" }
String doorStatus_time    "Last Change [%s ago]"                  <clock> (Garadget,UI) { garadget="<[270041234567343432313031#doorStatus_time]" }

// Send the doorState item commands like ZERO, HUNDRED, UP, DOWN, ON, OFF, STOP, or "open", "closed" or "stop".
Rollershutter doorState   "Control"                       <rollershutter> (Garadget,UI) { garadget=">[270041234567343432313031#setState],<[270041234567343432313031#doorStatus_status]" }
Number doorStatus_sensor  "Reflection [%d %%]"                      <sun> (Garadget,UI) { garadget="<[270041234567343432313031#doorStatus_sensor]" }
Number doorConfig_srt     "Threshold [%d %%]"                   <battery> (Garadget,UI) { garadget="<[270041234567343432313031#doorConfig_srt]" }
Number doorStatus_signal  "Signal [%d dB]"                      <battery> (Garadget,UI) { garadget="<[270041234567343432313031#doorStatus_signal]" }
String last_app           "Last App [%s]"                                 (Garadget) { garadget="<[270041234567343432313031#last_app]" }
String last_ip_address    "Last IP Address [%s]"                          (Garadget) { garadget="<[270041234567343432313031#last_ip_address]" }
DateTime last_heard       "Last Heard [%1$tm/%1$td %1$tH:%1$tM]"  <clock> (Garadget) { garadget="<[270041234567343432313031#last_heard]" }
Number product_id         "Product ID [%d]"                               (Garadget) { garadget="<[270041234567343432313031#product_id]" }
Switch connected          "Connected [%s]"                                (Garadget) { garadget="<[270041234567343432313031#connected]" }

String doorStatus         "Door Status [%s]"                              (Garadget) { garadget="<[270041234567343432313031#doorStatus]" }

String doorConfig         "Door Config [%s]"                              (Garadget) { garadget="<[270041234567343432313031#doorConfig]" }
Number doorConfig_ver     "Version [%.1f]"                                (Garadget) { garadget="<[270041234567343432313031#doorConfig_ver]" }
Number doorConfig_rdt     "Sensor Scan Interval [%d ms]"                  (Garadget) { garadget="<[270041234567343432313031#doorConfig_rdt]" }
Number doorConfig_mtt     "Door Moving Time [%d ms]"                      (Garadget) { garadget="<[270041234567343432313031#doorConfig_mtt]" }
Number doorConfig_rlt     "Button Press Time [%d ms]"                     (Garadget) { garadget="<[270041234567343432313031#doorConfig_rlt]" }
Number doorConfig_rlp     "Delay betw Presses [%d ms]"                    (Garadget) { garadget="<[270041234567343432313031#doorConfig_rlp]" }
Number doorConfig_srr     "Sensor reads [%d]"                             (Garadget) { garadget="<[270041234567343432313031#doorConfig_srr]" }
Number doorConfig_aot     "Open Timeout Alert [%d min]"                   (Garadget) { garadget="<[270041234567343432313031#doorConfig_aot]" }
Number doorConfig_ans     "Night time alert start [%d min from midnight]" (Garadget) { garadget="<[270041234567343432313031#doorConfig_ans]" }
Number doorConfig_ane     "Night time alert end [%d min from midnight]"   (Garadget) { garadget="<[270041234567343432313031#doorConfig_ane]" }

String netConfig          "Net Config [%s]"                               (Garadget) { garadget="<[270041234567343432313031#netConfig]" }
String netConfig_ip       "IP Address [%s]"                               (Garadget) { garadget="<[270041234567343432313031#netConfig_ip]" }
String netConfig_snet     "Subnet [%s]"                                   (Garadget) { garadget="<[270041234567343432313031#netConfig_snet]" }
String netConfig_gway     "Gateway [%s]"                                  (Garadget) { garadget="<[270041234567343432313031#netConfig_gway]" }
String netConfig_mac      "MAC address [%s]"                              (Garadget) { garadget="<[270041234567343432313031#netConfig_mac]" }
String netConfig_ssid     "SSID [%s]"                                     (Garadget) { garadget="<[270041234567343432313031#netConfig_ssid]" }

// send the setConfig item commands like "ans=1200|ane=420" to set night time alert to 8pm-7am, for example.
String setConfig          "Workshop Garage Door Config [%s]"              (Garadget) { garadget=">[270041234567343432313031#setConfig],<[270040001747343432313031#doorConfig]" }
```

