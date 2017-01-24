Documentation of the Withings binding Bundle.

## Introduction

The Withings Binding allows to synchronize data from the official Withings API to items. The following body measure types are supported: diastolic blood pressure, fat free mass, fat mass weight, fat ratio, heart pulse, height, systolic blood pressure, weight.

For installation of the binding, please see Wiki page [[Bindings]].

## Setup

To access Withings data the user needs to authenticate via an OAuth 1.0 flow. The binding implements the flow through the command line interface. The first time the binding is started, it prints the following messages to the console:

    #########################################################################################
    # Withings Binding needs authentication.
    # Execute 'withings:startAuthentication "<accountId>"' on OSGi console.
    #########################################################################################

In order to start the authentication process the user needs to execute `withings:startAuthentication <accountId>` on the OSGi console whereas `accountId` is an arbitrary key also used in the `openhab.cfg` in order to differentiate between the different credentials per withings account. The binding will print the following lines to the console

    #########################################################################################
    # Withings Binding Setup: 
    # 1. Open URL 'http://<auth-url>//' in your webbrowser
    # 2. Login, choose your user and allow openHAB to access your Withings data
    # 3. Execute 'withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"' on OSGi console
    #########################################################################################

So the user needs to open the shown url in a web browser, login with his withings credentials, confirm that openHAB is allowed to access his data and at the end he is redirected to a page on github. There the user finds the command `withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"` with filled parameters that is needed to finish the authentication. 

The binding stores the OAuth tokens, so that the user does not need to login again. From this point the binding is successfully configured.

## Item Binding Configuration

To bind a measure value to an item the measure type has to be defined in the generic binding config. Withings data can be bound to `NumberItem`s only. The syntax for a Withings binding is `withings="<measure type>"` The following table shows the measure types and units, that are supported by the binding:

<table>
<tr>
<th>Measure type</th>
<th>Binding Config</th>
<th>Unit</th>
</tr>
<tr><td>Weight</td><td>weight</td><td>kg</td></tr>
<tr><td>Height</td><td>height</td><td>meter</td></tr>
<tr><td>Fat Free Mass</td><td>fat_free_mass</td><td>kg</td></tr>
<tr><td>Fat Ratio</td><td>fat_ratio</td><td>%</td></tr>
<tr><td>Fat Mass Weight</td><td>fat_mass_weight</td><td>kg</td></tr>
<tr><td>Diastolic Blood Pressure</td><td>diastolic_blood_pressure</td><td>mmHg</td></tr>
<tr><td>Systolic Blood Pressure</td><td>systolic_blood_pressure</td><td>mmHg</td></tr>
<tr><td>Heart Pulse</td><td>heart_pulse</td><td>bpm</td></tr>
</table>

The following snippet shows some sample bindings:

    Number Weight     "Weight  [%.1f kg]"     { withings = "weight" }
    Number FatRatio   "FatRatio [%.1f %%]"    { withings = "fat_ratio" }
    Number HeartPulse "HeartPulse [%d bpm]"   { withings = "heart_pulse" }

## Synchronization

By default the Withings data is requested every 60 minutes. The interval can be configured in the `openhab.cfg` file. The interval must be specified in ms. The following snippet shows a data refresh interval configuration of 120 minutes:

    withings:refresh=7200000 

## Advanced OAuth Configuration

The Withings Binding uses a default application registration to request an OAuth token. The application belongs to the binding developer. If the user wants to use his own application, the binding can be configured to use another OAuth consumer key and consumer secret. Please read the Withings documentation how to register an application (http://oauth.withings.com/en/partner/dashboard). After the application was created, Withings will generate a consumer key and secret. The redirect url must be configured, too. To change the OAuth parameters for the Withings binding the user can specify the following values in the `openhab.cfg` :

    withings-oauth:consumerKey="<your consumer key>"
    withings-oauth:consumerSecret="<your consumer secret>"
    withings-oauth:redirectUrl="<your redirect url>"

Furthermore: if there were no values stored into openhab.cfg yet the Binding itself saves it's configuration into the file `services/withings-oauth.cfg`. Since this specific file is only meant to configure withings values no further prefix is needed. An example file look like:

    thomas.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
    thomas.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
    ...
    peter.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
    peter.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c

## Multiple accounts

If one own multiple withings accounts these accounts must be configured specifically. In addition to the above mentioned `openhab.cfg` configuration, an `accountId` can be specified.

    withings-oauth:thomas.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
    withings-oauth:thomas.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
    ...
    withings-oauth:peter.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
    withings-oauth:peter.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c