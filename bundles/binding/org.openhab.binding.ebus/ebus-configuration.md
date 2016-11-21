## JSON Configuration Example

    {
      "comment": "<Regler> Kromschröder/Wolf: Hot Water Temperature",
      "device": "Wolf CSZ-2 > CGB-2 > HCM-2/GBC-e, FW: 1.6",
      
      "id": "temp_boiler",
      "class": "heating_kw",
      
      "command": "50 22",
      "data": "CC 0E 00",
      
      "values": {
        "temp_boiler": {"type": "word", "pos": 12, "label": "Hot Water Temperature", "factor": 0.1 , "min": 1, "max": 100}
      }
    }


## Level 1 - Parameters
Entry | Description
--- | ---
comment | Leave comments to this commands (text)
device | Add the device name, firmware version etc. Only used for ducumentation. (text)
id | The internal id used for openHAB items (string)
class | The internal class used for openHAB items (string)
command | The eBUS command ``PBSB`` (hex string)
data | The master data payload (hex string)
values | Das ist ein Array mit den verschieden Werten eines Telegramms, siehe 2. Ebene.
computed_values | Hier können nach den ``values```noch weitere Werte anhand der Daten berechnet werden, siehe 2. Ebene.

### Optional Parameters

Entry | Description
--- | ---
filter | The filter will find matching telegrams by using RegEx. The placeholder ``??`` will be replaced by ``[A-Z0-9]{2}``. (do not use it in normal cases)
debug | You can enable debbung for this command, alowed values are numeric 1 and 2
dst | Destination address ``QQ``, required for polling if not explicite set in openHAB item, see [here](https://github.com/openhab/openhab/wiki/eBUS-Binding#writing-parameter) (hex string)
src | Source addres ``ZZ`` (hex string)

## Level 2 - Paramters
Entry | Description
--- | ---
type | ``data2b``, ``data2c``, ``data1c``, ``byte``, ``bit``, ``bcd``, ``word``, ``char``, ``uchar`` und ``script``(only for ``computed_values``)
pos | The position where the datatype starts, counting starts with index 1. (integer)
bit | Used for type Beim Typ ``bit`` to specify the position, counting starts with index 0. (integer)
label | A label that descrips the value (string)
script | Here you can use JavaScript to post process value(s). You can use the global variable ``thisValue`` or the id to access the value. If you use this in parameter block ``computed_values`` you can access all values by there ids. The returned value will overwrite the orgin value. Please keep in mind to encode the " sign (also used by json file). For simple multiplying use ``factor``.

### Optional Parameters
Entry | Description
--- | ---
factor | Multiply the orgin value with this factor (number)
min | Minimum allowed number value for a value (number)
max | Maximum allowed number value for a value (number)
debug | Hier kann für diesen Eintrag noch spezielles Debugging aktiviert werden.
