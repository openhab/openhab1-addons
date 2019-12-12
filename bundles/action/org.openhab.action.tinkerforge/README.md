# TinkerForge Actions

The TinkerForge Action service provides direct interaction with some of the TinkerForge devices.

## Prerequisites

The action service depends on the TinkerForge Binding (1.x) being installed and configured. Add at least a hosts configuration value in the binding's configuration.

## Actions

These action functions are available:

- `tfClearLCD(String uid)`

Clears the display of the LCD with the given uid.

Example:

  ```javascript
  rule "clear lcd"
      when
          Item ClearLCD received command ON
      then
         tfClearLCD("d4j")
  end
  ```

- `tfServoSetposition(String uid, String num, String position, String velocity, String acceleration)`

Sets the position of a TinkerForge servo with the uid $uid and servo number to the position $position using the speed $speed and acceleration $acceleration.

Example:

```javascript
rule "move servo"
when
  Item MoveServo received command ON
then
  tfServoSetposition("6Crt5W", "servo0", "0", "65535", "65535")
  Thread::sleep(1000)
  tfServoSetposition("6Crt5W", "servo0", "-9000", "65535", "65535")
  Thread::sleep(1000)
  tfServoSetposition("6Crt5W", "servo0", "9000", "65535", "65535")
end
```

- `tfDCMotorSetspeed(String uid, String speed, String acceleration, String drivemode)`

Sets the speed of a TinkerForge DC motor with the given uid to $speed using the acceleration $acceleration and the drivemode $drivemode.

- speed: value between -32767 - 32767
- drivemode is either "break" or "coast"

Example:

```javascript
rule "move motor"
when
  Item DCMOVE received command ON
then
  var String acceleration = "10000"
  var String speed = "15000"
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
end
```

- `tfDCMotorSetspeed(String uid, Short speed, Integer acceleration, String drivemode)`

Sets the speed of a TinkerForge DC motor with the given uid to $speed using the acceleration $acceleration and the drivemode $drivemode.

- speed: value between -32767 - 32767
- drivemode is either "break" or "coast"

Example:

```javascript
rule "move motor"
when
  Item DCMOVE received command ON
then
  var Integer acceleration = 10000
  var Short speed = 15000
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
  Thread::sleep(1000)
  tfDCMotorSetspeed("62Zduj", speed, acceleration, "break")
end
```

- `tfRotaryEncoderClear(String uid)`

Clears the rotary encoder counter with the given uid.

Example:

```javascript
rule "Clear"
  when Item Clear changed
then
  tfRotaryEncoderClear("kHv")
end
```

- `tfLoadCellTare(String uid)`
Sets tare on the load cell bricklet with the given uid.

Example:

```javascript
rule "Tare"
when
  Item Tare changed to ON
then
  postUpdate(TareValue, Weight.state)
  tfLoadCellTare("v8V")
end
```
