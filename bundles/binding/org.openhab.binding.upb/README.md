Documentation of the UPB binding bundle.

# Introduction

The UPB binding is used to enable communication between openHAB and UPB devices. This binding requires the use of a UPB PIM or power-line modem. This binding has only been tested against simply automated devices.

# Binding Configuration

To configure the binding add this to your openhab.cfg file:

    upb:port=/dev/ttyUSB0
    upb:network=55

# Item Binding Configuration

The binding only supports Switches or Dimmers.

    Dimmer Light_Dining_Room        "Dining Room"           (Lights) {upb="id=2"}
    Switch Light_Kitchen            "Kitchen"               (Lights) {upb="id=3"}

To activate a link set the link property to true:

    Dimmer Light_Lamps              "Living Room Lamps"     (Lights) {upb="id=4 link=true"}

# Other notes
Binding not currently compatible with MS Windows.

If you don't see this in the addons, you can find it here: https://github.com/openhab/openhab/pull/3883