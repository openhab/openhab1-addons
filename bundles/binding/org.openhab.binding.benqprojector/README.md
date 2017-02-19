# BenQ Projector Binding

This binding is designed to interface with BenQ projectors that have an RS232 interface on them that accepts the commands documented by BenQ [here](ftp://ftp.benq-eu.com/projector/benq_rs232_commands.pdf).

The binding has been tested with the following models:

* BenQ W1070
* BenQ W1080

## Status

This binding is current in Beta state with the following commands implemented:

| Command               | Query | Set  |Comments                                    |
|-----------------------|:-----:|:----:|--------------------------------------------|
| Power                 |Y      | Y    | |
| Mute                  |Y      | Y    | |
| Volume                |Y      | Y    | |
| Source                |Y      | Y    | Can be returned as either a string or number |
| Lamp Time             |Y      | N    | |

## Transports

The binding support transport using a TCP/IP to RS232 converter or via a direct RS232 interface.

[USR-TCP232-2](http://en.usr.cn/Ethernet-Module-T24/RS232-to-Ethernet-module.html) is a known working TCP/IP to RS232 converter.

## Binding Configuration

This binding can be configured in the file `services/benqprojector.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| mode     |         |   Yes    | controls how the projector can be reached. `serial` is for a directly connected RS232 serial interface while `network` is for using a TCP/IP to serial converter |
| deviceId |         |   Yes    | if `mode` is `network`, value should be set to the `<hostname>:<port>` of the CP/IP to serial converter<br/>if `mode` is `serial`, value should be `<device>:<speed>`, where `<device>` is the name of the serial port device and `<speed>` is the bitrate (defaults to 57600 if not given) |
| refresh  |         |    ?     | Polling interval in milliseconds, for example 15000 (15 seconds) |


## Examples

items/yourbenq.items

```
Switch gf_lounge_multimedia_projectorPower "Projector Power" (gf_lounge, gf_multimedia) {benqprojector="power"}
Switch gf_lounge_multimedia_projectorMute "Projector Mute" (gf_lounge, gf_multimedia) {benqprojector="mute"}
Number gf_lounge_multimedia_projectorVol "Projector Volume [%d]" (gf_lounge, gf_multimedia) {benqprojector="volume", autoupdate="false"}
Number gf_lounge_multimedia_projectorSourceNum "Projector Source [MAP(ProjSourceNum.map):%s]" (gf_lounge, gf_multimedia) {benqprojector="source_number"}
String gf_lounge_multimedia_projectorSourceString "Projector Source [%s]" (gf_lounge, gf_multimedia) {benqprojector="source_string"}
Number gf_lounge_multimedia_projectorLamp "Projector Lamp [%d hours]" (gf_lounge, gf_multimedia) {benqprojector="lamp_hours"}
```

transform/ProjSourceNum.map

```
0=Computer
1=Computer 2
2=Component
3=DVI-A
4=DVI-D
5=Sky TV (HDMI)
6=Chromecast (HDMI2)
7=Composite
8=S-Video
9=Network
10=USB Display
11=USB Reader
-=Unknown
undefined=Unknown
```

sitemap/yourbenq.sitemap

```
Frame label="Media" {
	Switch item=gf_lounge_multimedia_projectorPower
}
		
Frame label="Projector" visibility=[gf_lounge_multimedia_projectorPower==ON] {
	Selection item=gf_lounge_multimedia_projectorSourceNum label="Projector Source" mappings=[5="Sky TV", 6="Chromecast"]  visibility=[gf_lounge_multimedia_projectorPower==ON]
	Switch item=gf_lounge_multimedia_projectorMute visibility=[gf_lounge_multimedia_projectorPower==ON]
	Setpoint item=gf_lounge_multimedia_projectorVol step=1 minValue=0 maxValue=10 visibility=[gf_lounge_multimedia_projectorPower==ON]				
}
```
