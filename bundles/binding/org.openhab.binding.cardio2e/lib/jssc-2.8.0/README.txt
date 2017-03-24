jSSC-2.8.0 Release version (24.01.2014)

This version contains native libs for Windows(x86, x86-64), Linux(x86, x86-64, ARM soft & hard float), Solaris(x86, x86-64), Mac OS X(x86, x86-64, PPC, PPC64).
All native libs contains in the jssc.jar file and you don't need manage native libs manually.

In this build:

	Fixes:
		* Important! Fixed bug with port handles potential leakage
		
	Additions:
		* Added method "writeString(String string, String charsetName)"
		* Added method "getNativeLibraryVersion" in "SerialNativeInterface" class
		* Enabled Java and Native libraries versions mismatch check
		
With Best Regards, Sokolov Alexey aka scream3r.

============= Previous Builds ==============

///////////////////////////////////////////
//jSSC-2.6.0 Release version (01.06.2013)//
///////////////////////////////////////////

In this build:

	Note: Linux x86 and x86-64 was builded on Ubuntu 10.04 and don't depends GLIBC-2.15 unlike jSSC-2.5.0

	Additions:
		* Added os.name - "Darwin" and os.arch - "universal" support. It can be useful for MacOS X developers.
		* Added ttyO to Linux RegExp for listing OMAP serial devices.
		* Added JSSC_IGNPAR and JSSC_PARMRK properties for enabling IGNPAR and PARMRK flags in _nix termios structure.

///////////////////////////////////////////
//jSSC-2.5.0 Release version (27.04.2013)//
///////////////////////////////////////////

In this build:

	Fixes:
		* Important! Fixed bug with garbage reading on Linux, MacOSX, Solaris, cause of incorrect using of VMIN and VTIME. Now "read" methods works correctly and are blocking like in Windows
		* Important! Fixed error with garbage reading in Windows using jSSC after another application used serial port. To prevent this effect COMMTIMEOUTS structure zeroing added to setParams() method
		* Important! The port handle now stored in variable of type "long" instead of "int", to prevent potential problems with type conversions on Win64
		* Fixed MacOS X 10.8 bug with native lib loading (*.dylib -> *.jnilib)
		* Fixed Linux error with exclusive access to serial port (TIOCEXCL). TIOCNXCL added to closePort() method for clearing exclusive access
		* Fixed Windows native lib port name concatenation error
		* Fixed native lib extraction path if user home is read only, in this situation lib will be extracted to tmp folder
		* Null port name fix. If try to invoke method openPort() for SerialPort(null) object, exception TYPE_NULL_NOT_PERMITTED will be thrown
		* Enabled TIOCEXCL support in Solaris

	Additions:
		* Added ARM Soft & Hard float support (Tested of Raspberry Pi with Oracle JDK(6-7-8))
		* Added ttyACM, ttyAMA, rfcomm to Linux RegExp and tty.usbmodem to MacOS X RegExp
		* Added precompiled RegExp's for Linux, Solaris, MacOS X for more faster port listing
		* Added private common for Linux, Solaris, MacOS X method getUnixBasedPortNames() for listing serial ports
		* Rewrited comparator for sorting port names. Now it's a common comparator for Windows, Linux, Solaris and MacOS X
		* Added some syntax sugar to SerialPortList class, for changing search path, RegExp and comparator
		* Added timeouts for read operations and SerialPortTimeoutException class for catching timeout exceptions
		* Added JSSC_NO_TIOCEXCL JVM property for disable using of exclusive access to serial port
		* Added termios(_nix) and DCB(Windows) structure cheking on port opening, it helps separate real serial devices from others
		* Added "ERR_" constants into SerialNativeInterface
		* Added new exception TYPE_INCORRECT_SERIAL_PORT
		* Added new exception TYPE_PERMISSION_DENIED. It can be very useful for _nix based system if user have no permissions for using serial device
		
And other little modifications...

///////////////////////////////////////////
//jSSC-0.9.0 Release version (21.12.2011)//
///////////////////////////////////////////

In this build:
* Added Solaris support (x86, x86-64)
* Added Mac OS X support 10.5 and higher(x86, x86-64, PPC, PPC64)
* Fixed some bugs in Linux native part
* Changed openPort() method

Important Note:
	openPort() method has been changed, now if port busy SerialPortException with type: TYPE_PORT_BUSY will be thrown, 
	and if port not found SerialPortException with type: TYPE_PORT_NOT_FOUND will be thrown. 
	
	It's possible to know that port is busy (TYPE_PORT_BUSY) by using TIOCEXCL directive in *nix native library, 
	but using of this directive make some troubles in Solaris OS, that's why TIOCEXCL not used in Solaris (!)
	Be careful with it.
	
	Also Solaris and Mac OS X versions of jSSC not support following events:
	ERR, TXEMPTY, BREAK.
	
	Solaris version not support non standard baudrates
	Mac OS X version not support parity: MARK, SPACE.

* Included javadoc and source codes

/////////////////////////////////////////
//jSSC-0.8 Release version (28.11.2011)//
/////////////////////////////////////////

In this build:
* Implemented events BREAK and ERR (RXFLAG not supported in Linux)
* Added method sendBreak(int duration) - send Break signal for setted time
* Fixed bugs in Linux events listener
* Fixed bug with long port closing operation in Linux

/////////////////////////////
//jSSC-0.8-tb4 (21.11.2011)//
/////////////////////////////

In this build was fixed a bug in getPortNames() method under Linux.

Not implemented yet list:
* Events: BREAK, ERR and RXFLAG

/////////////////////////////
//jSSC-0.8-tb3 (09.09.2011)//
/////////////////////////////

In this build was implemented:
* purgePort()

And was fixed some Linux and Windows lib bugs.

New in this build:
* getInputBufferBytesCount() - get count of bytes in input buffer (if error has occured -1 will be returned)
* getOutputBufferBytesCount() - get count of bytes in output buffer (if error has occured -1 will be returned)
* setFlowControlMode() - setting flow control (available: FLOWCONTROL_NONE, 
                                                          FLOWCONTROL_RTSCTS_IN,
                                                          FLOWCONTROL_RTSCTS_OUT,
                                                          FLOWCONTROL_XONXOFF_IN,
                                                          FLOWCONTROL_XONXOFF_OUT)
* getFlowControlMode() - getting setted flow control mode

Some "syntactic sugar" for more usability:

* writeByte() - write single byte
* writeString() - write string
* writeInt() - write int value (for example 0xFF)
* writeIntArray - write int array (for example new int[]{0xFF, 0x00, 0xFF})

* readString(int byteCount) - read string
* readHexString(int byteCount) - read Hex string with a space separator (for example "FF 00 FF")
* readHexString(int byteCount, String separator) - read Hex string with setted separator (for example if separator : "FF:00:FF")
* readHexStringArray(int byteCount) - read Hex string array (for example {FF, 00, FF})
* readIntArray(int byteCount) - read int array (values in int array are in range from 0 to 255
                                                for example if byte == -1 value in this array it will be 255)

The following methods read all bytes in input buffer, if buffer is empty methods will return null

* readBytes()
* readString()
* readHexString()
* readHexString()
* readHexStringArray()
* readIntArray()

============================================

Not implemented yet list:
* Events: BREAK, ERR and RXFLAG

///////////////////////////////
// jSSC-0.8-tb2 (14.07.2011) //
///////////////////////////////

In this build was implemented:
* getPortNames()
* Parity: MARK and SPACE

And was fixed some Linux lib bugs.

Not implemented yet list:
* purgePort()
* Events: BREAK, ERR and RXFLAG

///////////////////////////////
// jSSC-0.8-tb1 (11.07.2011) //
///////////////////////////////

Not implemented yet list:
* getPortNames()
* Parity: MARK and SPACE
* purgePort()
* Events: BREAK, ERR and RXFLAG
