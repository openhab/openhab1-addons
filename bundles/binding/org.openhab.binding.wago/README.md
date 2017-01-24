# Introduction
The WAGO Binding provides a interface to ethernet-enabled Modbus-Controllers from WAGO.<br>
It polls the controller in a configurable interval.<br>
It fetches the configuration xml-file from the WAGO-Controller and derives the used modules, their position and their type from it.

# Details
## Binding Configuration

### setting the poll interval
     wago:refresh=<value>

### configure a slave
     wago:<slave-name>.ip=<slave-address>
     wago:<slave-name>.username=<username>
     wago:<slave-name>.password=<password>

## Item Binding Configuration
     <item-type> <item-name> <item-descriptor> (<group>) {wago="<slave-name>:<module>:<coil>"}
Example for a simple switch item bound to coil 3 of module 1 of "slave2":<br>
     `Switch MySwitch "My WAGO Switch" (ALL) {wago="slave2:1:3"}`