org.openhab.binding.enphaseenergy
=================================

openHAB [Enphase Energy](http://enphase.com) Binding.

Configuration
-------------

* This binding is for people with Enphase Energy microinverters used in their solar installation, which means every solar panel has it's own inverter and can be monitored individually. Your installer should have provided your with an [Enlight Manager Account](https://enlighten.enphaseenergy.com). Test that this account is working first and visit [https://enlighten.enphaseenergy.com/support](https://enlighten.enphaseenergy.com/support) to find out your Site ID (also called System ID). This is typically a small integer number, like 12345. It points to a specific installation location, you could potentially have more than one, if you have several homes.

* Create a [developer account](https://developer.enphase.com/). The free "WATT" plan is fine, as it allows 10 hits per minute and up to 10,000 per month. It doesn't make sense to poll the system more often that every 15 minutes, because the data is not updated more frequently anyway. The "WATT" plan requires Enphase attribution, which you could do within openHAB.

* Create an [application](https://developer.enphase.com/admin/applications) on the developer website.

* Open the "Authorization URL" from the application in a web browser to receive a user id. The "user id" can always be seen under the account on the [enphase energy website](https://enlighten.enphaseenergy.com/account)

* Copy the [API Key for the Applicaion](https://developer.enphase.com/admin/applications) and the user id from the Authorization URL into openhab.cfg

```
enphaseenergy:key=ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEF
enphaseenergy:user_id=ABCDEFGHIJKLMNOPQR
# enphaseenergy:refresh=900000
```

* Configure items and rules

Example items:
```
DateTime Enphase_last_report_at "Solar Last Report at [%1$tD %tr]" (Solar) { enphaseenergy="12345#last_report_at"}
Number Enphase_Current_Power "Solar Latest Power [%.0f W]" (Solar) { enphaseenergy="12345#current_power"}
Number Enphase_Energy_Today "Solar Energy Today [%.2f kWh]" (Solar) { enphaseenergy="12345#energy_today"}
Number Enphase_Energy_Lifetime "Solar Energy Lifetime [%.1f MWh]" (Solar) { enphaseenergy="12345#energy_lifetime"}
String Enphase_Status "Solar Status [%s]" (Solar) { enphaseenergy="12345#status"} 
Number Enphase_Modules "Solar Number of Modules [%d]" (Solar) { enphaseenergy="12345#modules"}
Number Enphase_size_w "Solar System Size [%d W]" (Solar) { enphaseenergy="12345#size_w"}
String Enphase_source "Solar Data Source [%s]" (Solar) { enphaseenergy="12345#source"}
DateTime Enphase_summary_date "Solar Summary Date [%1$tD]" (Solar) { enphaseenergy="12345#summary_date"}
DateTime Enphase_operational_at "Solar Operational at [%1$tD]" (Solar) { enphaseenergy="12345#operational_at"}
```
