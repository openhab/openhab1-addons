# Withings Binding

The Withings binding allows openHAB to synchronize data from the official Withings API to items. The following body measure types are supported: diastolic blood pressure, fat free mass, fat mass weight, fat ratio, heart pulse, height, systolic blood pressure, weight.

## Binding Configuration

The binding can be configured in the file `services/withings.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 360000  |    No    | The rate, in milliseconds, at which the binding requests Withings data. By default the Withings data is requested every 60 minutes (360000 milliseconds). |
| consumerKey |      |    Yes   | your consumer key |
| consumerSecret |   |    Yes   | your consumer secret |
| redirectUrl |      |    Yes   | your redirect url |

To access Withings data, the user needs to authenticate via an OAuth 1.0 flow. The binding implements the flow through the command line interface. The first time the binding is started, it prints the following messages to the console:

```
#########################################################################################
# Withings Binding needs authentication.
# Execute 'withings:startAuthentication "<accountId>"' on OSGi console.
#########################################################################################
```

In order to start the authentication process, the user needs to execute `withings:startAuthentication <accountId>` on the OSGi console where `<accountId>` is an arbitrary key also used in the `services/withings-oauth.cfg` in order to differentiate between the different credentials per Withings account. 

The binding will print the following lines to the console:

```
#########################################################################################
# Withings Binding Setup: 
# 1. Open URL 'http://<auth-url>//' in your webbrowser
# 2. Login, choose your user and allow openHAB to access your Withings data
# 3. Execute 'withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"' on OSGi console
#########################################################################################
```

So the user needs to open the shown URL in a web browser, login with his Withings credentials, confirm that openHAB is allowed to access his data and at the end he is redirected to a page on github. There the user finds the command `withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"` with filled parameters that is needed to finish the authentication.

The binding stores the OAuth tokens, so that the user does not need to login again. From this point the binding is successfully configured.

### Advanced OAuth Configuration

The Withings Binding uses a default application registration to request an OAuth token. The application belongs to the binding developer. If the user wants to use his own application, the binding can be configured to use another OAuth consumer key and consumer secret. Please read the Withings documentation how to register an application (http://oauth.withings.com/en/partner/dashboard). After the application was created, Withings will generate a consumer key and secret. The redirect url must be configured, too. To change the OAuth parameters for the Withings binding the user can specify the following values in the `services/withings.cfg`:

The OAuth configuration must be stored in the file `services/withings-oauth.cfg`.

### Multiple accounts

If one owns multiple withings accounts these accounts must be configured specifically. In addition to the above mentioned `services/withings.cfg` configuration, an `accountId` can be specified.

```
thomas.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
thomas.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
peter.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
peter.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
```

## Item Configuration

To bind a measure value to an item, the measure type has to be defined in the item binding config. Withings data can be bound to `Number` items only. The syntax for a Withings binding is 

```
withings="<measure type>"
``` 

The following table shows the measure types and units, that are supported by the binding:

| Measure type | Binding Config | Unit |
|--------------|----------------|------|
| Weight       | weight         | kg   |
| Height       | height         | meter |
| Fat Free Mass | fat_free_mass | kg   |
| Fat Ratio    | fat_ratio      | %    |
| Fat Mass Weight | fat_mass_weight | kg |
| Diastolic Blood Pressure | diastolic_blood_pressure | mmHg |
| Systolic Blood Pressure | systolic_blood_pressure | mmHg |
| Heart Pulse | heart_pulse | bpm |


## Examples

The following snippet shows some sample bindings:

```
Number Weight     "Weight  [%.1f kg]"     { withings = "weight" }
Number FatRatio   "FatRatio [%.1f %%]"    { withings = "fat_ratio" }
Number HeartPulse "HeartPulse [%d bpm]"   { withings = "heart_pulse" }
```
