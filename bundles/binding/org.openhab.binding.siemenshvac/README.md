## Introduction

This is a OpenHab binding for siemens hvac controller OZW672
http://www.oilon.com/uploadedFiles/OilonHome/Materials/Siemens_webserver_OZW672_%20manual_EN_2014-10-15.pdf
https://community.openhab.org/t/new-biding-addon-for-siemens-hvac-controller-ozw672-01/6359


Siemens controller OZW672 is a web gateway for siemens RVS41.813/327 controller that is in use in many heat pump and boiler system.
OZW672 connect to the LPB (Local Process Bus) or BSB (Boiler System Bus) and on the other side to the LAN.

The binding is fairly complete and supports the following functionality.

* Read datapoint on a regular basic (specify by the refresh settings).
* Write datapoint on openhab change

## Installation 

Copy the binding jar (org.openhab.siemenshvacbinding*.jar) to the addons directory
add the following to your openhab.cfg and comment out the relevant options
```
############################### siemenshvac binding #########################
#
#
#siemenshvac:baseUrl=http://192.168.254.53/
#siemenshvac:userName=Administrator
#siemenshvac:userPassword=mypassword
#siemenshvac:refresh=30000
```

## Items

Format of item for configuration:
Number	 						Temperature_Consigne_Pompe 	"Consigne Pompe [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2458;type:Numeric"			}

binding is write like this 
siemenshvac="parameter1;parameter2;parametern;"

where parameter can be
dpt:xxxx		: the datapoint ID to read / write
type:Numeric 	: the datapoint type, can be :Numeric, Enumeration, Text
rw:true			: indicate that this datapoint is a read/write datapoint

datapointID may differ in your setup.
You will have to find the right value looking on the OZW672 webserver

My current configuration for Atlantic Hybrid Duo heat pump is the following:

Number 							Temperature_Exterieure_Ch	"Exterieure (Ch) [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2470;type:Numeric"			}
Number 							Temperature_Exterieure_Min	"Exterieure (Min) [%.1f °C]"			<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2471;type:Numeric" 	  		}
Number 							Temperature_Exterieure_Max	"Exterieure (Max) [%.1f °C]"			<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2472;type:Numeric" 	  		}
Number	 						Temperature_Consigne_C	 	"Consigne confort [%.1f °C]"			<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1958;rw:true;type:Numeric"	}
Number	 						Temperature_Consigne_R	 	"Consigne réduit [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1959;rw:true;type:Numeric"	}
Number	 						Temperature_Consigne_H	 	"Consigne hg [%.1f °C]"					<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1960;rw:true;type:Numeric"	}
Number	 						Temperature_Confort_Max	 	"Confort maximum [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1961;rw:true;type:Numeric"	}

String							Chauffage_Mode				"Mode [%s]"								<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1957;rw:true;type:Enumeration"	}

String	 						Chaudiere_Regime		 	"Regime [%.1f °C]"						<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2265;type:Text" 			}
Number	 						Chaudiere_Pente			 	"Pente de la courbe [%.1f °C]"			<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:1962;rw:true;type:Numeric"	}


Number	 						Temperature_Consigne_Pompe 	"Consigne Pompe [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2458;type:Numeric"			}
Number	 						Temperature_Depart_Pompe 	"Départ Pompe [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2459;type:Numeric"			}
Number	 						Modulation_Pompe 			"Modulation Pompe [%d %]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2460;type:Numeric"			}

Number	 						Temperature_Depart_Reel 	"Départ réel [%.1f °C]"					<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2480;type:Numeric"			}
Number	 						Temperature_Depart_Consigne	"Départ consigne [%.1f °C]"				<temperature>		(gChauffage, Temperature) 	{ siemenshvac = "dpt:2481;type:Numeric"			}

Number 							Co2 