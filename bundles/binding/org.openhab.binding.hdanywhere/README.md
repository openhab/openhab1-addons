## HDanywhere Binding

[HDanywhere](http://www.hdanywhere.co.uk) is a manufacturer of multiroom/distributed audio/video equipment. This binding supports their V3 of the Multiroom+ HDMI matrix running firmware V1.2(20131222). These matrices support the highest HD resolutions, including 1080p 3D & 4K, use a single Cat5e/6/7 wiring structure with reliable performance up to 100m, have IR passback to allow you to select and control what you watch from every room and are fully compatible with universal remote controls.

The matrixes can be controlled by either UDP/IP and/or Serial connections, but due to the lack of feedback on the actual state of the HDMI matrix when using those methods, this binding operates by controlling the built-in webserver of the matrix.  

## Binding Configuration

This binding can be configured in the file `services/hdanywhere.cfg`.

A binding configuration with multiple configuration parts would typically be used to control multiple output ports at the same time, e.g. have the switch together to the same HDMI source port.

Furthermore, in openhab.cfg the 

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ports    | 4       |    No    | number of input ports/output ports if different from 4 (e.g. 8 for the 8x8 matrix) |


## Item Configuration

The format of the binding configuration is simple and looks like this:

```
hdanywhere="[<IP address>:<port number>:<polling interval>], [<IP address>:<port number>:<polling interval>], ..."
```

Where:

* `<IP address>` corresponds with the IP address of the HDMI matrix
* `<port number>` corresponds with the output port number on the matrix
* `<polling Interval>` is the interval in seconds to poll state of the given port

## HDanywhere Items

The HDanywhere matrix is very simple device. Since it is all about assigning a source HDMI port to an output port only Number Items are used/allowed to control the matrix. You feed it the source port number you want it to be connected to,e.g. updating the Item with a value of 2 will switch the defined output port to source port #2

## Examples

Here are a few examples of valid binding configuration strings, as defined in the items configuration file:

```
Number OutputPort1 "Output port 1 is currently connected to Source port [%d]" { hdanywhere="[192.168.0.88:1:15]" } 
Number OutputPorts1And2 "Output ports 1 and 2 are currently connected to Source port [%d]" { hdanywhere="[192.168.0.88:1:15],[192.168.0.88:2:15]" }
```
