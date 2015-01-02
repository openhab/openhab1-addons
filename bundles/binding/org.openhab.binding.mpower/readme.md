# Introduction

A binding for Ubiquiti mPower strips. All models are supported.
The binding supports
- Sending on/off commands
- Receiving data from the sensors
- multiple strips

# Setup

In your openhab.cfg add the following lines

```
mpower:mp1.host=mpower.lan
mpower:mp1.user=ubnt
mpower:mp1.password=ubnt
mpower:mp1.secure=false
```

# Item Binding Configuration

```
Switch mp1 "mp1-1" {mpower="mp1:1:switch"}
Switch mp2 "mp1-2" {mpower="mp1:2:switch"}
Switch mp3 "mp1-3" {mpower="mp1:3:switch"}
Switch mp4 "mp1-4" {mpower="mp1:4:switch"}
Switch mp5 "mp1-5" {mpower="mp1:5:switch"}
Switch mp6 "mp1-6" {mpower="mp1:6:switch"}

Number mp1Volt "mp1-1 voltage [%.2f V]" {mpower="mp1:1:voltage"}
Number mp2Volt "mp1-2 voltage [%.2f V]" {mpower="mp1:2:voltage"}
Number mp3Volt "mp1-3 voltage [%.2f V]" {mpower="mp1:3:voltage"}
Number mp4Volt "mp1-4 voltage [%.2f V]" {mpower="mp1:4:voltage"}
Number mp5Volt "mp1-5 voltage [%.2f V]" {mpower="mp1:5:voltage"}
Number mp6Volt "mp1-6 voltage [%.2f V]" {mpower="mp1:6:voltage"}

Number mp1Power "mp1-1 power [%.2f W]" {mpower="mp1:1:power"}
Number mp2Power "mp1-2 power [%.2f W]" {mpower="mp1:2:power"}
Number mp3Power "mp1-3 power [%.2f W]" {mpower="mp1:3:power"}
Number mp4Power "mp1-4 power [%.2f W]" {mpower="mp1:4:power"}
Number mp5Power "mp1-5 power [%.2f W]" {mpower="mp1:5:power"}
Number mp6Power "mp1-6 power [%.2f W]" {mpower="mp1:6:power"}
```

# Warning
This binding is still under development. Don't use it in production.
