/**
 *
 *	This application listening data from Nibe F1145/F1245 heat pumps (RS485 bus)
 *	and send valid frames to configurable IP/port address by UDP packets.
 *	Application also acknowledge the valid packets to heat pump.
 *
 *	Serial settings: 9600 baud, 8 bits, Parity: none, Stop bits 1
 *
 *	MODBUS module support should be turned ON from the heat pump.
 *
 *	Frame format:
 *	+----+----+----+-----+-----+----+----+-----+
 *	| 5C | 00 | 20 | CMD | LEN |  DATA	 | CHK |
 *	+----+----+----+-----+-----+----+----+-----+
 *
 *	Checksum: XOR
 *
 *	When valid data is received (checksum ok),
 *	 ACK (0x06) should be sent to the heat pump.
 *	When checksum mismatch,
 *	 NAK (0x15) should be sent to the heat pump.
 *
 *	If heat pump does not receive acknowledge in certain time period,
 *	pump will raise an alarm and alarm mode is activated.
 *	Actions on alarm mode can be configured. The different alternatives
 *	are that the Heat pump stops producing hot water (default setting)
 *	and/or reduces the room temperature.
 *
 *	Author: pauli.anttila@gmail.com
 *
 *	Build: gcc -std=gnu99 -o nibegw nibegw.c
 *
 *	3.2.2013	v1.00	Initial version
 *	5.2.2013	v1.01	
 *	4.11.2013	v1.02	Support cheksum and data special cases.
 *	20.12.2013	v1.03	Fixed compiling error.
 *
 */

#include <signal.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <termios.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define FALSE	0
#define TRUE	1

int verbose = 0;

void signalCallbackHandler(int signum)
{
	if (verbose) printf("\nExit...caught signal %d\n", signum);
	
	exit(1);
}

int initSerialPort(int fd)
{
	struct termios options;
	
	// Get the current options for the port...
	
	tcgetattr(fd, &options);
	
	// Set the baud rates
	cfsetispeed(&options, B9600);
	cfsetospeed(&options, B9600);
	
	// Enable the receiver and set local mode...
	options.c_cflag |= (CLOCAL | CREAD);
	
	// 8 data bits, no parity, 1 stop bit
	options.c_cflag &= ~PARENB;
	options.c_cflag &= ~CSTOPB;
	options.c_cflag &= ~CSIZE;
	options.c_cflag |= CS8;
	
	// Flow control
	options.c_iflag &= ~(IXON | IXOFF | IXANY);
	options.c_cflag |= CRTSCTS;
	//options.c_cflag &= ~CRTSCTS;
	
	options.c_cc[VMIN] = 1;		// Min carachters to be read
	options.c_cc[VTIME] = 1;	// Time to wait for data (tenths of seconds)
	
	// Set the new options
	if (tcsetattr(fd, TCSANOW, &options) < 0 )
	{
		return -1;
	}
	
	return 0;
}

int writeByte(int fd, unsigned char byte)
{
	int retval = -1;
	
	if (verbose) printf("writeByte: %02X\n", byte);
	
	if( write( fd, &byte, 1) == 1)
	{
		tcdrain (fd);
		retval = 0;
	}
	
	return retval;
}

int sendAck(int fd)
{
	if (verbose) printf("Send ACK (0x06)\n");
	return writeByte(fd, 0x06);
}

int sendNak(int fd)
{
	if (verbose) printf("Send NAK (0x15)\n");
	return writeByte(fd, 0x15);
}

void printMessage(const unsigned char* const message, int msglen)
{
	printf("Data: ");
	for (int l = 0; l < msglen; l++)
		printf("%02X", message[l]);
	printf("\n");
}

/*
 * Return:
 *	>0 if valid message received (return message len)
 *	 0 if ok, but message not ready
 *	-1 if invalid message
 *	-2 if checksum fails
 */
int checkMessage(const unsigned char* const data, int len)
{
	if (len <= 0)
		return 0;
	
	if (len >= 1)
	{
		if (data[0] != 0x5C)
			return -1;
		
		if (len >= 3)
		{
			if (data[1] != 0x00 && data[2] != 0x20)
				return -1;
		}
		
		if (len >= 6)
		{
			int datalen = data[4];
			
			if (len < datalen + 6)
				return 0;
			
			unsigned char checksum = 0;
			
			// calculate XOR checksum
			for(int i = 2; i < (datalen + 5); i++)
				checksum ^= data[i];
			
			unsigned char msg_checksum = data[datalen + 5];
			
			if (verbose)
			{
				printf("calc checksum %02X\n", checksum);
				printf("recv checksum %02X\n", msg_checksum);
			}
			
			if (checksum != msg_checksum)
			{
				// check special case, if checksum is 0x5C (start character), 
				// heat pump seems to send 0xC5 checksum
				if (checksum != 0x5C && msg_checksum != 0xC5)
					return -2;
			}
			
			return datalen + 6;
		}
		
	}
	
	return 0;
	
}

void printUsage(char* appname)
{
	char* usage = "%s usage:\n\n" \
	"\t-h				  Print help\n"								  \
	"\t-d <device name>	  Serial port device (default: /dev/ttyS0)\n" \
	"\t-a <address>		  IP address (default: 127.0.0.1)\n"		  \
	"\t-p <port>		  UDP port (default: 9999)\n"				  \
	;
	
	fprintf (stderr, usage, appname);
}

int main(int argc, char **argv)
{
	char *device = "/dev/ttySO";
	char *address = "127.0.0.1";
	int port = 9999;
	int c;
	
	opterr = 0;
	
	while ((c = getopt (argc, argv, "hvd:a:p:")) != -1)
		switch (c)
	{
		case 'v':
			verbose++;
			break;
			
		case 'd':
			device = optarg;
			break;
			
		case 'a':
			address = optarg;
			break;
			
		case 'p':
			port = atoi(optarg);
			break;
			
		case '?':
			if (optopt == 'd' || optopt == 'a' || optopt == 'p')
				fprintf (stderr, "Option -%c requires an argument.\n", optopt);
			else if (isprint (optopt))
				fprintf (stderr, "Unknown option `-%c'.\n", optopt);
			else
				fprintf (stderr,
						 "Unknown option character `\\x%x'.\n",
						 optopt);
			return 1;
			
		case 'h':
		default:
			printUsage(argv[0]);
			return 1;
	}
	
	// Install signal handlers
	signal(SIGINT, signalCallbackHandler);
	
	int serialport_fd = -1;
	int udp_fd = -1;
	
	// Initialize destination address
	struct sockaddr_in dest;
	dest.sin_family = AF_INET;
	dest.sin_addr.s_addr = inet_addr(address);
	dest.sin_port = htons(port);
	
	#define MAX_DATA_LEN 200
	
	unsigned char buffer[MAX_DATA_LEN];
	unsigned char message[MAX_DATA_LEN];
	
	for (;;)
	{
		/*
		 * Error tolerant solution.
		 *
		 * Try to open serial port and udp socket on every round,
		 * if open has failed.
		 *
		 */
		
		if ( serialport_fd < 0 )
		{
			// Open the serial port
			if (verbose) printf("Open serial port: %s\n", device);
			serialport_fd = open(device, O_RDWR | O_NOCTTY); // | O_NDELAY
			
			if (serialport_fd < 0)
			{
				fprintf(stderr, "Failed to open %s: %s\n", device, strerror(errno));
				//return 1;
			}
			
			// Initialize serial port
			if (initSerialPort(serialport_fd) == -1)
			{
				fprintf(stderr, "Failed to set serial port: %s\n", strerror(errno));
				//return 1;
			}
			
		}
		
		
		if ( udp_fd < 0 )
		{
			// Open UDP socket
			udp_fd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
			
			if (udp_fd < 0)
			{
				fprintf(stderr, "Failed to open UDP socket: %s\n", strerror(errno));
				//return 1;
			}
			
			if (verbose) printf("UDP address %s:%u\n", address, port);
			
		}
		
		int start_found = FALSE;
		int index = 0;
		
		/*
		unsigned char testdata[] = "\x01\x02" \
		"\x5C\x00\x20\x6B\x00\x4B" \
		"\x5C\x00\x20\x69\x00\x49\xC0\x69\x00\xA9" \
		"\x5C\x00\x20\x68\x50\x01\xA8\x1F\x01\x00\xA8\x64\x00\xFD" \
		"\xA7\xD0\x03\x44\x9C\x1E\x00\x4F\x9C\xA0\x00\x50\x9C\x78" \
		"\x00\x51\x9C\x03\x01\x52\x9C\x1B\x01\x87\x9C\x14\x01\x4E" \
		"\x9C\xC6\x01\x47\x9C\x01\x01\x15\xB9\xB0\xFF\x3A\xB9\x4B" \
		"\x00\xC9\xAF\x00\x00\x48\x9C\x0D\x01\x4C\x9C\xE7\x00\x4B" \
		"\x9C\x00\x00\xFF\xFF\x00\x00\xFF\xFF\x00\x00\xFF\xFF\x00" \
		"\x00\x45";
		
		ssize_t len = sizeof(testdata);
		
		memcpy( buffer, testdata, len);
		*/
		
		if (serialport_fd > 0)
		{
			ssize_t len = 0;
			
			while ((len = read(serialport_fd, buffer, MAX_DATA_LEN)) > 0)
			{
				for (int i = 0; i < len; i++)
				{
					if (verbose) printf("%02X ", buffer[i]);
					
					if (start_found == FALSE && buffer[i] == 0x5C)
					{
						start_found = TRUE;
						index = 0;
					}
					
					if (start_found)
					{
						if ((index+1) >= MAX_DATA_LEN)
						{
							start_found = FALSE;
						}
						else
						{
							message[index++] = buffer[i];
							
							int msglen = checkMessage(message, index);
							
							switch (msglen)
							{
								case 0: // Ok, but not ready
									break;
									
								case -1: // Invalid message
									start_found = FALSE;
									break;
									
								case -2: // Checksum error
									sendNak(serialport_fd);
									start_found = FALSE;
									break;
									
								default:
									if (verbose) printf("Valid message received, len=%u\n", msglen);
									
									sendAck(serialport_fd);
									
									// send UDP packet if message is a data packet
									// if data contains 0x5C (start character), message len can be bigger than 0x50 
									if (buffer[0] == 0x5C && buffer[1] == 0x00 && 
										buffer[2] == 0x20 && buffer[3] == 0x68 && 
										buffer[4] >= 0x50)
									{
										if (verbose) printf("Send UDP data to %s:%u\n", address, port);
										if (verbose) printMessage( message, msglen);
									
										if (sendto(udp_fd, message, msglen + 1, 0 , (struct sockaddr *)&dest, sizeof(dest)) == -1)
										{
											fprintf(stderr, "Failed to send udp packet: %s\n", strerror(errno));
										}
									}

									// Wait new message
									start_found = FALSE;
									break;
							}
						}
						
					}
					
				}
			}
			
			if (len < 0 )
			{
				if (errno == EINTR)
				{
					if (verbose) printf("Interrupted\n");
					break;
				}
				else
				{
					fprintf(stderr, "Read failed: %s\n", strerror(errno));
					sleep(1);
				}
			}
			else if (len == 0)
			{
				if (verbose) printf("Read return 0\n");
			}
		}
		else
		{
			sleep(1);
		}
	}
	
	close(serialport_fd);
	close(udp_fd);
	
	return 0;
}
