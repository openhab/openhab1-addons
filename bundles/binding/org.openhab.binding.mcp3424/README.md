## Introduction
This binding provides native access for MCP3424 18-bit delta sigma ADC
on I2C bus. Please consider datasheet for IC for future information.

## Generic Item Binding Configuration
Since MCP3424 is ADC converter on I2C bus, only two types of items are supported:
`Number` for raw conversion output and `Dimmer` for conversion output in percent.
Percent value is calculated dependent on set resolution. Find the example below.

# Binding Configuration in openhab.cfg
No special configuration within openhab.cfg is needed.

# Item Configuration
`Number Test1 "Test 1" (Tests) { mcp3424="{address:6C, pin:'CH0', gain:1, resolution:12"} }`
returns the raw conversion result on channel 0 (CH1 on datasheet) of the IC on address 0x6C
`Dimmer Test2 "Test 2" (Tests) { mcp3424="{address:6C, pin:'CH1', gain:1, resolution:12"} }`
returns the conversion result in percent on channel 1 (CH2 on datasheet) of the IC on address 0x6C
