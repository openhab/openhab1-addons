Documentation of the CUPS binding Bundle
## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a CUPS printer, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the CUPS binding configuration string is explained here: 

    cups="<printerName>#<whichJobs>"
Where `<printerName>` is the name or URL of the printer, as it is known by the CUPS-Server. And `<whichJobs>` is one the following
- NOT_COMPLETED: Jobs that are not printed yet (default value)
- COMPLETED: already printed jobs
- ALL: all jobs

Here are some examples of valid binding configuration strings: 

    cups="MX-870#NOT_COMPLETED"
    cups="http://127.0.0.1:631/printers/MX-870"

As a result, your lines in the items file might look like the following: 

    Number  Print_Jobs_Queued   "Unfinished print jobs"   (FF_Office)   { cups="MX-870#NOT_COMPLETED" }

In order to find out the name/url of your printer on the CUPS-Server you can download the current `cups4j.runnable-x.x.x.jar` file from http://www.cups4j.org/ and query the printers by using the following command

    java -jar cups4j.runnable-x.x.x.jar -h <CUPS-Server name> getPrinters

## Example Use Case

The CUPS Binding can be used to switch on a printer if there are print jobs in the queue and switch it off if the queue is empty.

### Items

    Number Print_Jobs_Queued  "Unfinished print jobs"  (FF_Office)   { cups="MX-870#NOT_COMPLETED" }
    Switch Printer            "Printer"

### Rule

    import org.openhab.model.script.actions.Timer
    
    var Timer printerTimer = null
    
    rule "CUPS-Printer Queue"
    when
    	Item Print_Jobs_Queued changed
    then
    	if (Print_Jobs_Queued.state>0) {
    		if (printerTimer!=null) {
    			printerTimer.cancel
    			printerTimer=null
    		}
    		if (Printer.state==OFF) {
    			sendCommand(Printer,ON)
    		}
    	}
    	else if (Printer.state==ON) {
    		printerTimer = createTimer(now.plusMinutes(5)) [|
    			sendCommand(Printer,OFF)
    		]
    	}
    end