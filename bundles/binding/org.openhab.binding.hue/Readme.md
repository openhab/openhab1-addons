
About HueTap Enhancements
=========================
The Hue Tap Switch provides a nice way to switch hue lamps. However, there was no way to program its behaviour from a rule, so 
it would always be a bit undefined. Furthermore there is no way out of the box to configure the Big Button (.) as a toggle button. 
To solve this, I provided a set of actions to programm the hue bridges's behaviour from rules. Note that the actions have been 
integrated into the hue binding, so no separate actions jar is needed.


Item Configurations
===================

Item configurations have been enhanced to distinguish between lights and tap devices, while keeping backward compatibility with 
the old configuration syntax.

Hue Lamps
---------
Lamp configurations follow the conventional hue syntax with just a number

	Color Color_1     "Küche"   			(Colorize,rWohn,gTest,gLichtWohnzimmer)  	{hue="1"}

Hue Tap Switches Configuration
------------------------------

Hue tap switches are configured with one Item per Switch Element, so a tap device has 4 Items. Syntax is:

	tap:<device id>:<button# 1..4>

For example the Button "." on Tap device with id 2 is defined in hue.items as:

	Switch Tap_1_1 {hue="tap;2;1",autoupdate="false"}

The "autoupdate" binding has been added since a tap device's button is a stateless pushbutton.

HueTap as a Toggle Button
=========================

In hue.items
------------

	Switch Tap_1_1 {hue="tap;2;1",autoupdate="false"}
	Switch Tap_1_2 {hue="tap;2;2",autoupdate="false"}
	Switch Tap_1_3 {hue="tap;2;3",autoupdate="false"}
	Switch Tap_1_4 {hue="tap;2;4",autoupdate="false"}
	
	//switches top set scenes from ui to current settings
	Switch Set_Tap_1_2 {autoupdate="false"}
	Switch Set_Tap_1_3 {autoupdate="false"}
	Switch Set_Tap_1_4 {autoupdate="false"}
	
In hueTap.rules
---------------

	import org.openhab.core.library.types.*
	import org.openhab.core.persistence.*
	import org.openhab.model.script.actions.*
	
	// example configs for initial scene settings
	val String bodyOn2="{\"on\":true,\"ct\":153,\"bri\":255}"
	val String bodyOn3="{\"on\":true,\"bri\":240,\"hue\":15331,\"sat\":121\"transitiontime\":0}"
	val String bodyOn4="{\"on\":true,\"ct\":500,\"bri\":60}"
	val String bodyOff="{\"on\":false,\"transitiontime\":0}"
	
	
	// my scenes for tap configuration
	val String offSceneId="tap-2-4-off"
	val String scene2Id="tap-2-2-on"
	val String scene3Id="tap-2-3-on"
	val String scene4Id="tap-2-4-on"
	
	// lichter im Wohnzimmer
	val String lights="1,2,3,5"
	
	val String tapDevice="2"
	
	var String onTapScene=null
	
	var Boolean sceneOn
	
	
	/**
	 * Implement toggle functionality
	 */ 
	rule "Set_Tap_1_2"
	when Item Set_Tap_1_2 received command
	then
		logInfo("Set_Tap_1_2","received command "+receivedCommand)	
		val response=hueSetScene(scene2Id,scene2Id,lights)	
		logInfo("Set_Tap_1_2","response='"+response+"'")
		logInfo("Set_Tap_1_2","done.")
	end
	rule "Set_Tap_1_3"
	when Item Set_Tap_1_3 received command
	then
		logInfo("Set_Tap_1_3","received command "+receivedCommand)	
		val response=hueSetScene(scene3Id,scene3Id,lights)	
		logInfo("Set_Tap_1_3","response='"+response+"'")
		logInfo("Set_Tap_1_3","done.")
	end
	rule "Set_Tap_1_4"
	when Item Set_Tap_1_4 received command
	then
		logInfo("Set_Tap_1_4","received command "+receivedCommand)	
		val response=hueSetScene(scene4Id,scene4Id,lights)	
		logInfo("Set_Tap_1_4","response='"+response+"'")
		logInfo("Set_Tap_1_4","done.")
	end
	
	rule "startup_tap"
	when System started
	then
		onTapScene=scene2Id 
	//	hueSetSceneForButton("tap 2-1",tapDevice,1,offSceneId)
		sceneOn=true
	end
	
	
	rule "Tap_1_1"
	when Item Tap_1_1 received command
	then
		logInfo("Tap_1_1","received command "+receivedCommand)
		logInfo("Tap_1_1","sceneOn "+sceneOn)
		logInfo("Tap_1_1","onTapScene '"+onTapScene+"'")
		
		if(sceneOn) {
			logInfo("Tap_1_1","tapDevice '"+tapDevice+"' off:"+offSceneId)
	 
			hueSetSceneForButton("tap 2-1",tapDevice,1,offSceneId)
			sceneOn=false
		}else{
			logInfo("Tap_1_1","tapDevice '"+tapDevice+"' on:"+onTapScene)
			hueSetSceneForButton("tap 2-1",tapDevice,1,onTapScene)
			sceneOn=true
		}
		
	end
	
	rule "Tap_1_2"
	when Item Tap_1_2 received command
	then
		logInfo("Tap_1_2","received command "+receivedCommand)
		onTapScene=scene2Id
		sceneOn=true
	end
	
	rule "Tap_1_3"
	when Item Tap_1_3 received command
	then
		logInfo("Tap_1_3","received command "+receivedCommand)
		onTapScene=scene3Id
		sceneOn=true
	end
	
	rule "Tap_1_4"
	when Item Tap_1_4 received command
	then
		logInfo("Tap_1_4","received command "+receivedCommand)
		onTapScene=scene4Id
		sceneOn=true
	end
	
	
	
	
	
	
	
	/**
	 * Configuration: set tap rules from ui to defined rules and scenes, so switching from now on works nicely
	 */
	rule "setTapRules"
	when Item setTapRules  received command
	then
		logInfo("setTapRules","received command "+receivedCommand)
		
	
		//val boolean r=pingHue()	
		//logInfo("setTapRules","pingHue returned '"+r+"'")
		
		//val Rule x=hueTest("hu")	
		//var org.openhab.action.hue.Rule r1=new Rule("Rule1");
		
		
		hueSetScene(offSceneId,"wz off",lights)
		hueSetSceneSettings(offSceneId,lights,bodyOff)
		
		hueSetScene(scene2Id,"tap 2-2",lights)
		hueSetSceneSettings(scene2Id,lights,bodyOn2)
	
		hueSetScene(scene3Id,"tap 2-3",lights)
		hueSetSceneSettings(scene3Id,lights,bodyOn3)
	
		hueSetScene(scene4Id,"tap 2-4",lights)
		hueSetSceneSettings(scene4Id,lights,bodyOn4)
		
		
		val allLampsGroup="0" //???
			// wohnzimmer aus: "scene": "35df7fe0d-off-0"
		//scenes: "scene": beach: "5e680bd82-on-0", off: "35df7fe0d-off-0"
	
	 	//val sceneBeach=	"5e680bd82-on-0"
	 	//val sceneOff="35df7fe0d-off-0"
	 	
	 	//val lightsWohnzimmer="1,2,3,5"
	 	
	 	
	 	hueDeleteAllRules()
	 	
	 	var r1 = hueCreateRule("tap 2-1")	
		r1=hueAddTapButtonEqualsCondition(r1,tapDevice,1)
		r1=hueAddSensorChangedCondition(r1,tapDevice)
		r1=hueAddGroupAction(r1,allLampsGroup,"scene", offSceneId)
		var response1=hueSetRule(r1)
	 	
	 	r1 = hueCreateRule("tap 2-2")	
		r1=hueAddTapButtonEqualsCondition(r1,tapDevice,2)
		r1=hueAddSensorChangedCondition(r1,tapDevice)
		r1=hueAddGroupAction(r1,allLampsGroup,"scene", scene2Id)
		response1=hueSetRule(r1)
	 	
	 	r1 = hueCreateRule("tap 2-3")	
		r1=hueAddTapButtonEqualsCondition(r1,tapDevice,3)
		r1=hueAddSensorChangedCondition(r1,tapDevice)
		r1=hueAddGroupAction(r1,allLampsGroup,"scene", scene3Id)
		response1=hueSetRule(r1)
	 
	 	
		r1 = hueCreateRule("tap 2-4")
		
		r1=hueAddTapButtonEqualsCondition(r1,tapDevice,4)
		r1=hueAddSensorChangedCondition(r1,tapDevice)
		r1=hueAddGroupAction(r1,allLampsGroup,"scene", scene4Id)
		logInfo("testTapRule",r1)
		response1=hueSetRule(r1)
		
		logInfo("testTapRule",response1)
		
		logInfo("testTapRule","done.")
	end
	
	
	/**
	 * Configuration: set tap scenes to current settings
	 */ 
	rule "Set_Tap_1_2"
	when Item Set_Tap_1_2 received command
	then
		logInfo("Set_Tap_1_2","received command "+receivedCommand)	
		val response=hueSetScene(scene2Id,scene2Id,lights)	
		logInfo("Set_Tap_1_2","response='"+response+"'")
		logInfo("Set_Tap_1_2","done.")
	end
	rule "Set_Tap_1_3"
	when Item Set_Tap_1_3 received command
	then
		logInfo("Set_Tap_1_3","received command "+receivedCommand)	
		val response=hueSetScene(scene3Id,scene3Id,lights)	
		logInfo("Set_Tap_1_3","response='"+response+"'")
		logInfo("Set_Tap_1_3","done.")
	end
	rule "Set_Tap_1_4"
	when Item Set_Tap_1_4 received command
	then
		logInfo("Set_Tap_1_4","received command "+receivedCommand)	
		val response=hueSetScene(scene4Id,scene4Id,lights)	
		logInfo("Set_Tap_1_4","response='"+response+"'")
		logInfo("Set_Tap_1_4","done.")
	end




 
