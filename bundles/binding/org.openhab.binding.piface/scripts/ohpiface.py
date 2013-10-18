#!/usr/bin/python
"""
ohpiface.py
A network listener for handling openHAB UDP commands for PiFace pins
"""
import getopt
import signal
import socket
import struct
import sys
import thread
import time
import piface.pfio as pfio
import SocketServer

ERROR_ACK     = 0
WATCHDOG_CMD  = 14
WATCHDOG_ACK  = 15

WRITE_OUT_CMD = 1
WRITE_OUT_ACK = 2
READ_OUT_CMD  = 3
READ_OUT_ACK  = 4
READ_IN_CMD   = 5
READ_IN_ACK   = 6
DIGITAL_WRITE_CMD = 7
DIGITAL_WRITE_ACK = 8
DIGITAL_READ_CMD  = 9
DIGITAL_READ_ACK  = 10

# verbose debug logging
verbose = False

# number of seconds between monitoring polls
poll = 0.1

class UdpPacket(object):    
    def __init__(self, command=0, pin=0, value=0):
        self.command  = command
        self.pin      = pin
        self.value    = value

    def for_network(self):
        """Returns this packet as a struct"""
        return struct.pack("BBB", self.command, self.pin, self.value)

    def from_network(self, raw_struct):
        """Returns this packet with new values interpereted from
        the struct received"""
        self.command, = struct.unpack("B", raw_struct[0])
        self.pin,     = struct.unpack("B", raw_struct[1])
        self.value,   = struct.unpack("B", raw_struct[2])
        return self

class UDPHandler(SocketServer.BaseRequestHandler):
    """
    self.request consists of a pair of data and client socket
    """
    def handle(self):
        data = self.request[0]
        socket = self.request[1]

        # read the UDP packet into our own object
        packet = UdpPacket().from_network(data)

        # only support digital read/writes
        if packet.command == DIGITAL_WRITE_CMD:
            if verbose:
                print "Digital write request for pin %d -> %d" % (packet.pin, packet.value)
            pfio.digital_write(packet.pin, packet.value)
            response = UdpPacket(DIGITAL_WRITE_ACK, packet.pin)
            socket.sendto(response.for_network(), self.client_address)

        elif packet.command == DIGITAL_READ_CMD:
            if verbose:
                print "Digital read request for pin %d" % packet.pin
            response = UdpPacket(DIGITAL_READ_ACK, packet.pin, pfio.digital_read(packet.pin))
            socket.sendto(response.for_network(), self.client_address)

        elif packet.command == WATCHDOG_CMD:
            if verbose:
                print "Watchdog request"
            response = UdpPacket(WATCHDOG_ACK, packet.pin)
            socket.sendto(response.for_network(), self.client_address)

        else:
            if verbose:
                print "Unknown packet command (%d)" % packet.command
            response = UdpPacket(ERROR_ACK, packet.pin)
            socket.sendto(response.for_network(), self.client_address)

def start():
    global verbose
    listenerport = 0
    broadcastport = 0

    # parse out the command line arguments
    try:
        opts, args = getopt.getopt(sys.argv[1:],"hl:b:v",["help","listenerport=","broadcastport="])
    except getopt.GetoptError:
        print_usage(sys.argv[0])
        sys.exit(2)
    for opt, arg in opts:
        if opt == "-v":
            verbose = True
        elif opt in ("-h", "--help"):
            print_usage(sys.argv[0])
            sys.exit()
        elif opt in ("-l", "--listenerport"):
            listenerport = int(arg)
        elif opt in ("-b", "--broadcastport"):
            broadcastport = int(arg)

    # initialise the PiFace IO library
    pfio.init()

    # start the client thread
    if (broadcastport != 0):
        if (verbose):
            print "Starting monitor on %d..." % broadcastport
        thread.start_new_thread(start_client, (broadcastport, ))

    # start the listener in this thread
    if (listenerport != 0):
        if (verbose):
            print "Starting listener on %d..." % listenerport
        server = SocketServer.UDPServer((get_hostname(), listenerport), UDPHandler)
        server.serve_forever()

def start_client(port):
    # initialise pin values so we can detect changes
    values = [0, 0, 0, 0, 0, 0, 0, 0]

    # initialise the broadcast socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
	
    # endless loop, checking each pin state
    while True:
        for pin in range(8):
            values[pin] = monitor_pin(pin, values[pin], sock, port)

        time.sleep(poll)

def monitor_pin(pin, value, sock, port):
    latest = pfio.digital_read(pin)
    if latest != value:
        if (verbose):
            print "Pin %d changed to %d" % (pin, latest)
        send(pin, latest, sock, port)
    return latest

def send(pin, value, sock, port):
    data = struct.pack("BB", pin, value)
    sock.sendto(data, ("<broadcast>", port))

def get_hostname():
    # only way I could find to resolve the active IP address
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80)) 		# try to connect to google's dns
    hostname = s.getsockname()[0] # get this device's hostname
    s.close()
    if (verbose):
        print "Hostname resolved to %s" % hostname
    return hostname

def print_usage(scriptname):
    print "%s -l <listenerport> -b <broadcastport>" % scriptname

def cleanup(*args):
    pfio.deinit()

if __name__ == '__main__':
    signal.signal(signal.SIGINT, cleanup)
    signal.signal(signal.SIGTERM, cleanup)
    start()
