Asterisk Binding Configuration

Active call examples
--------------------

1) Switch on a light when there is at least an active call
   When there are no active calls the light will turn off

Switch light (lights) {asterisk="active"}

or

Switch light (lights) {asterisk="active:*:*"}

2) Switch on a light when '215' calls '101' and turn it off when the call ends

Switch light (lights) {asterisk="active:215:101"}

3) Switch on a light on every call to '101' and turn it off when the call ends

Switch light (lights) {asterisk="active:*:101"}

4) Switch on a light on every call originated from '215' and turn it off when the call ends

Switch light (lights) {asterisk="active:215:*"}


DTMF Digit examples
-------------------

1) Switch on a light when '1' digit is sent from '215' to '101' during an active call

Switch light (lights) {asterisk="digit:215:101:1"}

2) Switch on a light whenever a '1' digit is sent to '101' during an active call

Switch light (lights) {asterisk="digit:*:101:1"}

3) Switch on a light whenever a '1' digit is sent from '215' during an active call

Switch light (lights) {asterisk="digit:215:*:1"}

4) Switch on a light whenever a '1' digit is sent during an active call

Switch light (lights) {asterisk="digit:*:*:1"}
