# CUPS Binding

The openHAB CUPS binding allows interaction with printers and their print queues via a CUPS server.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/ipp/).

## Prerequisites

To set up this binding, it is necessary to know the names of the printers registered with the CUPS server.

One way to get this information is to download the current `cups4j.runnable-x.x.x.jar` file from http://www.cups4j.org/ and query the printers by using the following command

    java -jar cups4j.runnable-x.x.x.jar -h <CUPS server name> getPrinters


## Binding Configuration

The binding can be configured in the file `services/cups.cfg`.

| Property | Default | Required | Description                                   |
|----------|---------|:--------:|-----------------------------------------------|
| host     |         | Yes      | The hostname or IP address of the CUPS server |
| port     | 631     | No       | The port used to connect to the CUPS server   |
| refresh  | 60000   | No       | The refresh interval (in milliseconds)        |


## Item Configuration

Each item binding should have this format: 

```
    cups="<printerName>[#whichJobs]"
```

`<printerName>` is the name or URL of the printer, as registered with the CUPS server.
`whichJobs` has a default value of NOT_COMPLETED; possible values are:

- NOT_COMPLETED
- COMPLETED
- ALL


## Examples

- Number  Print_Jobs_Queued   "Unfinished print jobs"   (FF_Office)   { cups="MX-870#NOT_COMPLETED" }

- Number  Print_Jobs_Completed   "Completed print jobs"   (FF_Office)   { cups="http://127.0.0.1:631/printers/MX-870" }

### Example Use Case

The CUPS Binding can be used to switch on a printer if there are print jobs in the queue and switch it off if the queue is empty.

#### Items

    Number Print_Jobs_Queued  "Unfinished print jobs"  (FF_Office)   { cups="MX-870#NOT_COMPLETED" }
    Switch Printer            "Printer"

#### Rules

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
