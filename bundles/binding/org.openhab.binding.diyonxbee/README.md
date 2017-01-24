since 1.9

#Introduction
This binding is to interact with self built (DIY) hardware that all communicate via XBee modules.

#Hardware setup
The XBee connected to openhab has to be configured to run in API mode for this plugin to work.

# Binding configuration
_Example to add in openhab.cfg:_

         diyonxbee:serialPort=/dev/serial/by-id/usb-FTDI_FT232R_USB_UART_A702NX9M-if00-port0                                                                                
         diyonxbee:baudRate=9600                                                                                                                                            
 
# Item configuration

     Number  Living_Temperature      "Temperatur [%.1f °C]"          <temperature>   (GF_Living)     { diyonxbee="<0013A20040B40F18:Temperature" }
     Switch  Living_Motion           "Bewegung [MAP(de.map):%s]"     <present>       (GF_Living,GPresence)   { diyonxbee="<0013A20040B40F18:MOTION" }
     Color  Living_Stripe      "Living RGB Stripe"          <lights>   (GF_Living)     { diyonxbee="<0013A20040B40F18:Stripe" }

(regard the > and < arrows that define in and out).


# Protocol
The Protocol is text based to allow simple development/debugging the Arduino code even without using the XBee (any serial console will do).
For the pure sensors, the Arduino has to send lines conaining key=value, e.g from the example item configuration above, a valid message would be:

    Temperature=21.3
    MOTION=ON

For the actors, the Arduino has to send the received command back to the sender, this ensures that the openhab item state is only updated when the command was correctly understood. Currently, RGB led stripes (openhab type Color), switches and Dimmers are supported.

# Sample Arduino Sketch
Find a Arduino library with example [here](https://github.com/juri8/diyonxbee-library)