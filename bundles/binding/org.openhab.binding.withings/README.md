# Withings Binding

The Withings binding allows openHAB to synchronize data from the official
Withings API to items. The following body measure types are supported: diastolic
blood pressure, fat free mass, fat mass weight, fat ratio, heart pulse, height,
systolic blood pressure, and weight.

## Binding Configuration

The binding can be configured in the file `services/withings.cfg`.

| Property       | Default | Required | Description |
|----------------|---------|:--------:|-------------|
| refresh        | 360000  |    No    | The periodicity, in milliseconds, at which the binding requests Withings data. By default the Withings data is requested every 60 minutes (360000 milliseconds). |
| consumerkey    |         |    Yes   | your consumer key |
| consumersecret |         |    Yes   | your consumer secret |
| redirectUrl    |         |    Yes   | your redirect url |

### Configuration for multiple accounts

If using multiple withings accounts, the following properties are required
for each account.

| Property                   | Description |
|----------------------------|-------------|
| `<accountId>`.userid       | The userid for this account |
| `<accountId>`.token        | The token for this account |
| `<accountId>`.tokensecret  | The token secret for this account |


To access Withings data, the user needs to authenticate via an OAuth 1.0 flow.
The binding implements this flow through the command line interface. The first
time the binding is started, it prints the following messages to the console:

````
#########################################################################################
# Withings Binding needs authentication.
# Execute 'withings:startAuthentication "<accountId>"' on OSGi console.
#########################################################################################
````

In order to start the authentication process, the user needs to execute
`withings:startAuthentication <accountId>` on the OSGi console where
`<accountId>` is an arbitrary key also used in the `services/withings.cfg`
file in order to differentiate between the different credentials per Withings
account.

The binding will print the following lines to the console:

````
#########################################################################################
# Withings Binding Setup: 
# 1. Open URL 'http://<auth-url>//' in your webbrowser
# 2. Login, choose your user and allow openHAB to access your Withings data
# 3. Execute 'withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"' on OSGi console
#########################################################################################
````

The user needs to open the given URL in a web browser, log in with his Withings
credentials, and confirm that openHAB is allowed to access the user's data.  At
the end of this process, the user will be redirected to a page on github. There
the user finds the command `withings:finishAuthentication "<accountId>" "<verifier>" "<user-id>"`
with pre-filled parameters that is needed to finish the authentication.

The binding stores the OAuth tokens so that the user does not need to log in
again. From this point, the binding is successfully configured.

### Advanced OAuth Configuration

The Withings Binding uses a default application registration to request an OAuth
token. The application belongs to the binding developer. If the user wants to
use his own application, the binding can be configured to use another OAuth
consumer key and consumer secret. Please read the [Withings documentation](http://oauth.withings.com/en/partner/dashboard)
on how to register an application. After the application is created, Withings
will generate a consumer key and secret. The redirect URL must be configured,
too.


## Item Configuration

To bind a measure value to an item, the measure type has to be defined in the
item binding config. Withings data can be bound to `Number` items only. The
syntax for a Withings item binding is:

````
withings="<measure type>"
````

The following table shows the measure types and units that are supported by the binding:

| Measure Type    | Binding Config | Unit |
|-----------------|----------------|------|
| Weight          | weight         | kg   |
| Height          | height         | meter |
| Fat Free Mass   | fat_free_mass | kg   |
| Fat Ratio       | fat_ratio      | %    |
| Fat Mass Weight | fat_mass_weight | kg |
| Diastolic Blood Pressure | diastolic_blood_pressure | mmHg |
| Systolic Blood Pressure | systolic_blood_pressure | mmHg |
| Heart Pulse     | heart_pulse | bpm |


## Examples

### Examples of multiple account configuration

````
thomas.userid=ImThomas
thomas.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
thomas.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
peter.userid=PeterPeter
peter.token=74c0e77021ef5be1ec8dcb4dd88c1xckusadwe92f9541c86799dcbbfcb8fc8b236
peter.tokensecret=25f1098209xns511711b3287288f90740ff45532cef91658c5043db0b0e0c851c
````

### Item configuration examples

````
Number Weight     "Weight  [%.1f kg]"     { withings = "weight" }
Number FatRatio   "FatRatio [%.1f %%]"    { withings = "fat_ratio" }
Number HeartPulse "HeartPulse [%d bpm]"   { withings = "heart_pulse" }
Number PHeight    "Height  [%d in]"       { withings = "peter:height" }
````
