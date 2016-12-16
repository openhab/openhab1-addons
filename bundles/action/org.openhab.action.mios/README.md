Documentation for the MiOS Action Binding.

# Introduction
This binding exposes openHAB Rule extensions to be used with the [MiOS Bridge Binding](https://github.com/openhab/openhab/wiki/MiOS-Binding).

It exposes the ability to do the following things in the MiOS HA Controller from within [openHAB Rules](https://github.com/openhab/openhab/wiki/Rules):

* `Device Actions` - Asynchronously invoke MiOS Device Actions involving 0, 1 or more parameters.
* `Scenes Invocation` - Asynchronously invoke MiOS Scenes

The MiOS Action Binding depends upon the installation of the MiOS Bridge Binding, and need only be installed if the target deployment uses MiOS Action extensions in it's Rules.

# Releases

* 1.7.0 - First release

# Configuration

The MiOS Action Binding relies upon the MiOS Bridge Binding being installed and configured, and the installation of the MiOS Action Binding Bundle (JAR) file.  Once these are done, you're ready to use the Rule extensions this bundle provides.

# Extensions

Add-on Actions - MiOS Action

* `sendMiosAction(Item item, String action)` - requests the _parameterless_ Device Action, specified through `action`, be invoked on the MiOS Device bound to `item`.
* `sendMiosAction(Item item, String action, List<<String,Object>> params)` - as above, but for parameterized Device Actions.
* `sendMiosScene(Item scene)` - requests the scene associated with the `scene` parameter be invoked on the MiOS Unit.

The `action` string, of the `sendMiosAction` extension, is a string of the form:

    <ServiceURN>/<ServiceAction>

or

    <ServiceAlias>/<ServiceAction>

where _ServiceURN_, _ServiceAlias_ and _ServiceAction_ have the same form as described in [MiOS Bridge Binding](https://github.com/openhab/openhab/wiki/MiOS-Binding) commands.

You can use the MiOS `invoke` URL to discover the _Actions_, and _Action-parameters_, your particular MiOS Device supports:
 
    http://<mios:host>:3480/data_request?id=invoke

The available _ServiceAction_s are described in the [MiOS Luup UPnP Variables and Actions](http://wiki.micasaverde.com/index.php/Luup_UPnP_Variables_and_Actions) documentation.

## Examples

* Invoking a Device Action and calling a Scene to turn off the AV.
```
    rule "Test action rules Off"
        when 
            Time cron "0 45 23 * * ?"
        then
            sendMiosAction(FamilyMainLightsId, "Dimmer/SetLoadLevelTarget", newArrayList('newLoadlevelTarget' -> 0))
            sendMiosScene(SceneGoodNight)
    end
```

* Invoking a Sonos Device on MiOS to _say_ something
```
    rule "Test action say"
        when
            Item HallGarageDoorZoneTripped changed to OPEN
        then
            sendMiosAction(OfficeSonosId, "Sonos/Say", newArrayList('Text' -> 'Warning! Garage door opened', 'Volume' -> 50))
    end
```

