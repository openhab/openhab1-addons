## Installation and Configuration
See [[CUL Transport]] for general configuration options (such as serial device parameters).

## Item configuration
    {cul="TR3D4900"} is binding to receive the valve position in percent. It can be bound to a number item
    {cul="TR952E90"} is a binding to receive the window state of a FHT window contact. Note that these two bindings can only be read only. None of these devices does receive commands
    {cul="TR3D49"} This would be a binding to receive reports of a FHT80b. Currently only the measured temperature is received
    {cul="TW3D49"} This binding enables you to send commands to a FHT80b. Currently this will be mostly the desired temperature. But you need also a writeable binding to update the time on your FHT80b.
