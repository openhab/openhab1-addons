# DIYOnXBee Binding

The openHAB DIYOnXBee binding allows interaction with self built (DIY) hardware that communicate via XBee modules.


## Prerequisites

The XBee connected to openHAB has to be configured to run in API mode.


## Binding Configuration

The binding may be configured in the file `services/diyonxbee.cfg`.

| Property   | Default | Required | Description                         |
|------------|---------|:--------:|-------------------------------------|
| serialPort |         | Yes      | The serial port on which to connect |
| baudRate   | 9600    | No       | The baud rate to use                |


## Item Configuration

A binding beginning with `<` defines an input, while a binding beginning with `>` defines an output.


## Examples

### diyonxbee.cfg

    serialPort=/dev/serial/by-id/usb-FTDI_FT232R_USB_UART_A702NX9M-if00-port0                                                                                
    baudRate=9600                                                                                                                                            

### Items

    Number  Living_Temperature      "Temperatur [%.1f °C]"          <temperature>   (GF_Living)     { diyonxbee="<0013A20040B40F18:Temperature" }
    Switch  Living_Motion           "Bewegung [MAP(de.map):%s]"     <present>       (GF_Living,GPresence)   { diyonxbee="<0013A20040B40F18:MOTION" }
    Color   Living_Stripe           "Living RGB Stripe"          <lights>   (GF_Living)     { diyonxbee="<0013A20040B40F18:Stripe" }


## Notes

### Protocol

The protocol is text-based to allow simple development/debugging of the Arduino code
even without using the XBee (any serial console will do).

For the pure sensors, the Arduino has to send lines conaining key=value. From the
example item configuration above, a valid message would be:

    Temperature=21.3
    MOTION=ON

For the actors, the Arduino has to send the received command back to the sender;
this ensures that the openHAB item state is only updated when the command was
correctly understood.

Currently, RGB LED stripes (openHAB type Color), switches and dimmers are supported.

### Sample Arduino Sketch
There is an Arduino library with example [here](https://github.com/juri8/diyonxbee-library).
