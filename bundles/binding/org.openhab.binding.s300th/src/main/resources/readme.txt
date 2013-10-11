In openhab.cfg add
	s300th:serial:<serial device>
	
To use this binding add items like
Number OutdoorTemp [...] {s300th="address=<address, range from 1-8>; datapoint=<One of TEMPERATURE|HUMIDITY>"}