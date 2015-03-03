org.openhab.binding.netaqua
===========================

openHAB netAQUA sprinkler binding.

Configuration
-------------

* This binding is for people with the Roslen netAQUA 9D or netAQUA 9S irrigation controller <http://www.roslen.com>. It supports monitoring, manual control and simple programming directly from openHAB. For more complex setup, it can be programmed via a web interface.

* Set the IP address (in my example it is 10.0.1.10) of the controller into openhab.cfg. The refresh and long refresh are optional. By default the watering status, current cycle and zone, as well as the time in the cycle or till the next watering are updating once a minute. All others are updated every 15 minutes. This should probably never be changed. The long cycle should also always be a multiple of the normal refresh time.

```
############################### netAQUA Binding #######################################
#

netaqua:server=10.0.1.10
#netaqua:refresh=60000
#netaqua:longrefresh=900000

```

Configure items and rules
-------------------------

For the items, you can simply add the "netaqua.items" file to your Items folder. This will provide all available items automatically.

Add the following group to your sitemap. It is an example what can be done. You obviously want to rename the zone labels and maybe want to add more cycles, if you need to. I kept it simple.

```
		Text label="Sprinklers" icon="garden" {
			Frame label="Watering Status" {
				Switch item=netAqua_watering label="Currently Watering [%s]" icon="none" mappings=[ON="Stop"]
//				Text item=netAqua_cycle_nbr
				Text item=netAqua_zone_nbr label="Current Watering Zone [%s]"
				Text item=netAqua_time_left label="Watering Time Left [%s]" icon="clock-on"
				Text item=netAqua_last_cycle label="Last Watering Cycle [%s]" icon="clock-on"
				Text item=netAqua_next_cycle label="Next Watering Cycle [%s]" icon="clock-on"
			}
			Frame label="Monitoring" {
				Text item=Outdoor_netAqua_ext_temperature icon="temperature"
				Text item=Outdoor_netAqua_local_rainfall
			}
			Frame label="Notifications" {
				Text item=netAqua_notifications label="Notifications [%s]"
			}
			Frame {
				Text label="Run Sprinklers Manually" icon="none" {
					Switch item=netAqua_watering label="Currently Watering [%s]" icon="none" mappings=[ON="Stop"]
					Switch item=netAqua_run_cycle_1 label="Run Full Cycle" icon="none" mappings=[ON="Run"]
//					Switch item=netAqua_test_next_zone label="Switch to Next Zone" icon="none" mappings=[ON="Run"]
					Frame label="Run Zone for 1 Minute" {
						Switch item=netAqua_run_zone_1 label="Back Lawn 1" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_2 label="Back Lawn 2" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_3 label="Backyard" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_4 label="Poolyard" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_5 label="Front Lawn 1" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_6 label="Street" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_7 label="Front Lawn 2" icon="none" mappings=[ON="Run"]
						Switch item=netAqua_run_zone_8 label="Courtyard" icon="none" mappings=[ON="Run"]
					}
				}
				Text label="Set Timers" icon="clock-on" {
	//				Text label="Cycle #1" icon="clock-on" {
	//					Text item=netAqua_cycle_1_name
						Selection item=netAqua_cycle_1_frequency label="Frequency" mappings=[0="Every Day"/*, 1="Select Days"*/, 2="Even Days", 3="Odd Days", 4="Every Two Days", 5="Every Three Days", 6="Every Four Days", 7="Every Five Days", 8="Every Six Days"/*, 9="Two Weeks"*/]
						Frame label="Timer #1" {
							Switch item=netAqua_cycle_1_timer_1_enabled label="Enabled"
							Setpoint item=netAqua_cycle_1_timer_1_hour12 label="Hour [%d]" minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_1_timer_1_minute label="Minute [%d]" minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_1_timer_1_ampm label="AM/PM"
						}
						Frame label="Timer #2" {
							Switch item=netAqua_cycle_1_timer_2_enabled label="Enabled"
							Setpoint item=netAqua_cycle_1_timer_2_hour12 label="Hour [%d]" minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_1_timer_2_minute label="Minute [%d]" minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_1_timer_2_ampm label="AM/PM"
						}
						Frame label="Timer #3" {
							Switch item=netAqua_cycle_1_timer_3_enabled label="Enabled"
							Setpoint item=netAqua_cycle_1_timer_3_hour12 label="Hour [%d]" minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_1_timer_3_minute label="Minute [%d]" minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_1_timer_3_ampm label="AM/PM"
						}
						Frame label="Timer #4" {
							Switch item=netAqua_cycle_1_timer_4_enabled label="Enabled"
							Setpoint item=netAqua_cycle_1_timer_4_hour12 label="Hour [%d]" minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_1_timer_4_minute label="Minute [%d]" minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_1_timer_4_ampm label="AM/PM"
						}
						Frame label="Zone Watering Duration" {
							Setpoint item=netAqua_cycle_1_zone_1_duration label="Back Lawn 1 [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_2_duration label="Back Lawn 2 [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_3_duration label="Backyard [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_4_duration label="Poolyard [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_5_duration label="Front Lawn 1 [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_6_duration label="Street [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_7_duration label="Front Lawn 2 [%d min]" minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_1_zone_8_duration label="Courtyard [%d min]" minValue=0 maxValue=59 step=1
	//						Setpoint item=netAqua_cycle_1_zone_9_duration label="UNUSED [%d min]" minValue=0 maxValue=59 step=1
						}
	//				}
	/*				Text label="Cycle #2" icon="clock-on" {
						Text item=netAqua_cycle_2_name
						Selection item=netAqua_cycle_2_frequency mappings=[0="Every Day", 1="Select Days", 2="Even Days", 3="Odd Days", 4="Every Two Days", 5="Every Three Days", 6="Every Four Days", 7="Every Five Days", 8="Every Six Days", 9="Two Weeks"]
						Frame label="Timer #1" {
							Switch item=netAqua_cycle_2_timer_1_enabled
							Setpoint item=netAqua_cycle_2_timer_1_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_2_timer_1_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_2_timer_1_ampm
						}
						Frame label="Timer #2" {
							Switch item=netAqua_cycle_2_timer_2_enabled
							Setpoint item=netAqua_cycle_2_timer_2_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_2_timer_2_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_2_timer_2_ampm
						}
						Frame label="Timer #3" {
							Switch item=netAqua_cycle_2_timer_3_enabled
							Setpoint item=netAqua_cycle_2_timer_3_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_2_timer_3_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_2_timer_3_ampm
						}
						Frame label="Timer #4" {
							Switch item=netAqua_cycle_2_timer_4_enabled
							Setpoint item=netAqua_cycle_2_timer_4_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_2_timer_4_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_2_timer_4_ampm
						}
						Frame label="Zone Watering Duration" {
							Setpoint item=netAqua_cycle_2_zone_1_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_2_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_3_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_4_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_5_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_6_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_7_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_8_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_2_zone_9_duration minValue=0 maxValue=59 step=1
						}
					}
					Text label="Cycle #3" icon="clock-on" {
						Text item=netAqua_cycle_3_name
						Selection item=netAqua_cycle_3_frequency mappings=[0="Every Day", 1="Select Days", 2="Even Days", 3="Odd Days", 4="Every Two Days", 5="Every Three Days", 6="Every Four Days", 7="Every Five Days", 8="Every Six Days", 9="Two Weeks"]
						Frame label="Timer #1" {
							Switch item=netAqua_cycle_3_timer_1_enabled
							Setpoint item=netAqua_cycle_3_timer_1_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_3_timer_1_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_3_timer_1_ampm
						}
						Frame label="Timer #2" {
							Switch item=netAqua_cycle_3_timer_2_enabled
							Setpoint item=netAqua_cycle_3_timer_2_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_3_timer_2_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_3_timer_2_ampm
						}
						Frame label="Timer #3" {
							Switch item=netAqua_cycle_3_timer_3_enabled
							Setpoint item=netAqua_cycle_3_timer_3_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_3_timer_3_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_3_timer_3_ampm
						}
						Frame label="Timer #4" {
							Switch item=netAqua_cycle_3_timer_4_enabled
							Setpoint item=netAqua_cycle_3_timer_4_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_3_timer_4_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_3_timer_4_ampm
						}
						Frame label="Zone Watering Duration" {
							Setpoint item=netAqua_cycle_3_zone_1_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_2_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_3_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_4_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_5_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_6_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_7_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_8_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_3_zone_9_duration minValue=0 maxValue=59 step=1
						}
					}
					Text label="Cycle #4" icon="clock-on" {
						Text item=netAqua_cycle_4_name
						Selection item=netAqua_cycle_4_frequency mappings=[0="Every Day", 1="Select Days", 2="Even Days", 3="Odd Days", 4="Every Two Days", 5="Every Three Days", 6="Every Four Days", 7="Every Five Days", 8="Every Six Days", 9="Two Weeks"]
						Frame label="Timer #1" {
							Switch item=netAqua_cycle_4_timer_1_enabled
							Setpoint item=netAqua_cycle_4_timer_1_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_4_timer_1_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_4_timer_1_ampm
						}
						Frame label="Timer #2" {
							Switch item=netAqua_cycle_4_timer_2_enabled
							Setpoint item=netAqua_cycle_4_timer_2_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_4_timer_2_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_4_timer_2_ampm
						}
						Frame label="Timer #3" {
							Switch item=netAqua_cycle_4_timer_3_enabled
							Setpoint item=netAqua_cycle_4_timer_3_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_4_timer_3_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_4_timer_3_ampm
						}
						Frame label="Timer #4" {
							Switch item=netAqua_cycle_4_timer_4_enabled
							Setpoint item=netAqua_cycle_4_timer_4_hour12 minValue=1 maxValue=12 step=1
							Setpoint item=netAqua_cycle_4_timer_4_minute minValue=0 maxValue=59 step=1
							Switch item=netAqua_cycle_4_timer_4_ampm
						}
						Frame label="Zone Watering Duration" {
							Setpoint item=netAqua_cycle_4_zone_1_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_2_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_3_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_4_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_5_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_6_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_7_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_8_duration minValue=0 maxValue=59 step=1
							Setpoint item=netAqua_cycle_4_zone_9_duration minValue=0 maxValue=59 step=1
						}
					}
	*/
				}
	/*			Text label="Zone Names" icon="none" {
					Text item=netAqua_zone_1_name
					Text item=netAqua_zone_2_name
					Text item=netAqua_zone_3_name
					Text item=netAqua_zone_4_name
					Text item=netAqua_zone_5_name
					Text item=netAqua_zone_6_name
					Text item=netAqua_zone_7_name
					Text item=netAqua_zone_8_name
					Text item=netAqua_zone_9_name
				}
	*/
				Text label="netAQUA™ Info" icon="none" {
					Text item=netAqua_name
					Text item=netAqua_model
					Text item=netAqua_serialNumber
					Text item=netAqua_app_version
					Text item=netAqua_boot_version
					Text item=netAqua_uptime icon="clock-on"
					Text item=netAqua_int_temperature icon="temperature"
				}
			}
```
