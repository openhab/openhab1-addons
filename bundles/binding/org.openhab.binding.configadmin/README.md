Documentation of the ConfigAdmin binding Bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

With this binding it is possible to dynamically change settings that are defined in openhab.cfg. The openhab.cfg file is as such used to define the initial configuration, while you can use this binding to change values during runtime, simply by sending an item command.

In order to bind an item to a configuration value, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the !ConfigAdmin binding configuration string is explained here:

    configadmin="<pid>:<command>"

Note: `<pid>` can be configured either fully qualified with preceding 'org.openhab' or without it as a shortcut. In case no '.' is found within the pid it is prefixed with 'org.openhab '.

Here are some examples of valid binding configuration strings:

    configadmin="dropbox:syncmode"
    configadmin="org.openhab.gcal:refresh"


As a result, your lines in the items file might look like the following:

    Switch    Dropbox_OnOff      "Activate Sync"   (State)   { configadmin="dropbox:activate" }
    String    Dropbox_SyncMode   "Syncmode"        (State)   { configadmin="dropbox:syncmode" }