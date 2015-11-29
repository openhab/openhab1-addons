package orvibo2

import (
	"encoding/hex"
	"errors"
	"fmt"
	"net"
	"strconv"
)

// All exported events and vars are at the top, unexported events and vars at the bottom
// -------------------------------------------------------------------------------------

// EventStruct is our equivalent to node.js's Emitters, of sorts.
// This basically passes back to our Event channel, info about what event was raised
// (e.g. Device, plus an event name) so we can act appropriately
type EventStruct struct {
	Name       string
	DeviceInfo *Device
}

// A list of supported products
const (
	UNKNOWN = -1 + iota // UNKNOWN is obviously a device that isn't implemented or is unknown. iota means add 1 to the next const, so SOCKET = 0, ALLONE = 1 etc.
	SOCKET              // SOCKET is an S10 / S20 powerpoint socket
	ALLONE              // ALLONE is the AllOne IR blaster
	RF                  // RF switch. Not yet implemented
	KEPLER              // KEPLER is Orvibo's latest product, a timer / gas detector. Not yet implemented
)

// AllOne is Orvibo's IR and 433mhz blaster.
type AllOne struct {
	Name          string              // The name of our item
	DeviceType    int                 // What type of device this is. See the const below for valid types
	IP            *net.UDPAddr        // The IP address of our item
	MACAddress    string              // The MAC Address of our item. Necessary for controlling the S10 / S20 / AllOne
	Subscribed    bool                // Have we subscribed to this item yet? Doing so lets us control
	Queried       bool                // Have we queried this item for it's name and details yet?
	RFSwitches    map[string]RFSwitch // What switches are attached to this device? RF switches aren't WiFi enabled, so they've all got to come through the AllOne anyway
	LastIRMessage string              // Not yet implemented.
	LastMessage   string              // The last message to come through for this device
}

// Socket refers to the S10 and S20 WiFi enabled power sockets
type Socket struct {
	Name        string       // The name of our item
	DeviceType  int          // What type of device this is. See the const below for valid types
	IP          *net.UDPAddr // The IP address of our item
	MACAddress  string       // The MAC Address of our item. Necessary for controlling the S10 / S20 / AllOne
	Subscribed  bool         // Have we subscribed to this item yet? Doing so lets us control
	Queried     bool         // Have we queried this item for it's name and details yet?
	State       bool         // Is the item turned on or off? Will always be "false" for the AllOne, which doesn't do states, just IR & 433
	LastMessage string       // The last message to come through for this device
}

// RFSwitch is Orvibo's RF (433mhz) switch. It's not WiFi enabled, so it receives signals from an AllOne
type RFSwitch struct {
	Name        string // The name of our item
	DeviceType  int    // What type of device this is. See the const below for valid types
	State       bool   // I don't think the RF switches are "state aware". If they aren't, it's up to you to set this!
	AllOne      AllOne // Which AllOne is this switch attached to?
	LastMessage string // The last message to come through for this device
}

// Kepler is Orvibo's newest product. It's a Gas & CO2 detector that has a timer built in
type Kepler struct {
	Name       string       // The name of our item
	DeviceType int          // What type of device this is. See the const below for valid types
	IP         *net.UDPAddr // The IP address of our item
	MACAddress string       // The MAC Address of our item. Necessary for controlling the S10 / S20 / AllOne
	Subscribed bool         // Have we subscribed to this item yet? Doing so lets us control
	Queried    bool         // Have we queried this item for it's name and details yet?
	CO2        int          // The current amount of CO2 in the air
	Gas        int          // The current amount of gas in the air
}

// Devices is a list of devices we know about. It's an interface, so it can be anything. Be careful with this, as things like Subscribe() won't work with an RFSwitch (as it has no MACAddress field)
var Devices map[string]interface{}

// Gas levels for reporting. Exportable so you can set 'em. I think these values are in PPM?
// NOTE: These have NOT been tested. For your own health and safety: DO NOT RELY ON THESE VALUES!!
var GasWarnLevel = 6     // Strange levels of gas, but not yet dangerous (?!)
var GasDangerLevel = 50  // Dangerous levels of gas. HIDE YO KIDS, HIDE YO WIFE!
var CO2WarnLevel = 100   // Unusual levels of C02 in the air, but not yet dangerous (?!)
var CO2DangerLevel = 400 // HIDE YO HUSBAND, COZ THEY SUFFOCATING' ERRBODY OUT THERE!

// Start listens on UDP port 10000 for incoming messages.
func Start() error {
	udpAddr, err := net.ResolveUDPAddr("udp4", ":10000") // Get our address ready for listening
	if err != nil {
		return err
	}

	conn, err = net.ListenUDP("udp", udpAddr) // Now we listen on the address we just resolved
	if listenErr != nil {
		return listenErr
	}

	// Hand a message back to our calling code. Because it's not about a particular device, we just pass back an empty AllOne
	passMessage("ready", &AllOne{})
	return nil
}

// Discover all Orvibo devices
func Discover() {
	// magicWord + packet length + command ID (7161 = "qa", which means search for sockets where MAC is unknown)
	err := broadcastMessage(magicWord + "0006" + "7161")
	if err != nil {
		return
	}

	passMessage("discover", &AllOne{})
	return
}

// Subscribe loops over all the Devices we know about, and asks for control (subscription)
func Subscribe() {
	for d := range Devices { // Loop over all devices we know about
		if d.DeviceType != RF { // Obviously the RF switch isn't WiFi, so it has no MAC address, and therefore can't be subscribed to.
			// We send a message to each socket. reverseMAC takes a MAC address and reverses each pair (e.g. AC CF 23 becomes CA FC 32)
			sendMessage("636c", reverseMAC(Devices[d].MACAddress)+macPadding, Devices[d])
			passMessage("subscribe", &d)
		}
	}

	return
}

// EnterLearningMode lets us learn an IR code. We pass an interface{} because the Kepler, RFSwitch, AllOne and Socket structs have different fields
func EnterLearningMode(device interface{}) error {
	if device.DeviceType != AllOne { // We only want AllOne devices, as they're the only one capable of entering learning mode
		return errors.New("Unable to enter learning mode. Device passed is not an AllOne")
	}
	if device.MACAddress == "ALL" { // If we've passed it an AllOne with
		for _, a := range Devices {
			if a.DeviceType == ALLONE {
				sendMessage("6c73", "010000000000", a)
				passMessage("irlearnmode", a)
			}
		}
	} else {
		if device.DeviceType == ALLONE {
			sendMessage("6c73", "010000000000", device)
			passMessage("irlearnmode", device)
		}
	}
}

// sendMessage pieces together a lot of the standard Orvibo packet, including correct packet length.
// It ultimately uses sendMessageRaw to sent out the packet
func sendMessage(commandID string, msg string, device interface{}) error {
	if device.DeviceType == RF {
		return errors.New("Cannot call SendMessage on an RF switch")
	}

	packet := magicWord + "0000" + commandID + device.MACAddress + macPadding + msg
	packetlen := fmt.Sprintf("%04s", strconv.FormatInt(int64(len(packet)/2), 16))
	packet = magicWord + packetLen + commandID + device.MACAddress + macPadding + msg
	sendMessageRaw(packet, device)
}

// sendMessageRaw is the heart of our library. Sends UDP messages to specified IP addresses
func sendMessageRaw(msg string, device interface{}) error {
	// Turn this hex string into bytes for sending
	buf, _ := hex.DecodeString(msg)

	// Resolve our address, ready for sending data
	udpAddr, err := net.ResolveUDPAddr("udp4", device.IP.String())
	if err != nil {
		return err
	}

	// Actually write the data and send it off
	_, err = conn.WriteToUDP(buf, udpAddr)
	// If we've got an error
	if err != nil {
		return err
	}

	passMessage("sendMessageRaw", device)
	return nil
}

// Sends a message to the whole network via UDP
func broadcastMessage(commandID, msg string) error {
	udpAddr, err := net.ResolveUDPAddr("udp4", net.IPv4bcast.String()+":10000")
	// We create a temporary AllOne with an IP address of 255.255.255.255 so sendMessageRaw will work without modification
	sendMessage(commandID, msg, &AllOne{IP: udpAddr})
	if err != nil {
		return err
	}

	// Info for our calling code
	passMessage("broadcast", &AllOne{})
	return nil
}

// passMessage adds items to our Events channel so the calling code can be informed
// It's non-blocking or whatever.
func passMessage(message string, device interface{}) bool {

	select {
	case Events <- EventStruct{message, device}:

	default:
	}

	return true
}

// Via http://stackoverflow.com/questions/19239449/how-do-i-reverse-an-array-in-go
// Splits up a hex string into bytes then reverses the bytes
func reverseMAC(mac string) string {
	s, _ := hex.DecodeString(mac)
	for i, j := 0, len(s)-1; i < j; i, j = i+1, j-1 {
		s[i], s[j] = s[j], s[i]
	}
	return hex.EncodeToString(s)
}

// All Orvibo packets start with this sequence, which is "hd" in hex
var magicWord = "6864"

// Every packet also includes the MAC address, plus padding. We put the padding in here for brevity
var macPadding = "202020202020"
