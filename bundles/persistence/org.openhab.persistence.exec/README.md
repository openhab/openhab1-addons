Documentation of the Exec Persistence Service

## Introduction

This service allows you to execute commands in the underlying OS to persist. It could be used to call e.g. the original rrdtool API.

## Features

This persistence service supports writing information to the command line.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called exec.persist in the `${openhab.home}/configuration/persistence` folder.

## Configuration

There is nothing to configure in the openhab.cfg file for this persistence service.

All item and event related configuration is done in the exec.persist file. Aliases do have special meaning for the exec Persistence Service because they carry out the command line to execute e.g. like

`"echo \"%2$tY-%2$tm-%2$td %2$tT : %1$s\""`

To enhance the given command line with the current state or the current date the service incorporates the [String.format()](http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html) method. The first parameter is always the state of the particular item, the second parameter is the current date.