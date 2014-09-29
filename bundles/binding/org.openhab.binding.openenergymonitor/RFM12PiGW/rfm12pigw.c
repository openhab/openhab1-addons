/**
 *
 *  Copyright (c) 2010-2014, openHAB.org and others.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * ----------------------------------------------------------------------------
 *
 *  This application listening data from RFM12Pi module and send valid frames
 *  to configurable IP/port address by UDP packets.
 *
 *  Serial settings: 9600 baud, 8 bits, Parity: none, Stop bits 1
 *
 *  Frame format:
 *  +----+----+----+----+----+-----+----+----+----+
 *  | XX | XX | XX | XX | XX | ... | XX | CR | LF |
 *  +----+----+----+----+----+-----+----+----+----+
 *
 *
 *  Author: pauli.anttila@gmail.com
 *
 *  Build: gcc -std=gnu99 -o rfm12pigw rfm12pigw.c
 *
 *  15.09.2013	v1.00	Initial version
 *  19.10.2013	v1.10	Added RFM12Pi initialization command support
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

#define FALSE   0
#define TRUE    1

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
    
    // Raw input
    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);

    options.c_cc[VMIN] = 1;     // Min carachters to be read
    options.c_cc[VTIME] = 0;    // Time to wait for data (tenths of seconds)
    
    // Set the new options
    if (tcsetattr(fd, TCSANOW, &options) < 0 )
    {
        return -1;
    }
    
    return 0;
}

void printData(const char* prefix, const unsigned char* const data, int len, const char* suffix)
{
    printf(prefix);
    for (int l = 0; l < len; l++)
        printf("%02X", data[l]);
    printf(suffix);
}

void printUsage(char* appname)
{
    char* usage = "%s usage:\n\n" \
    "\t-h                 Print help\n" \
    "\t-d <device name>   Serial port device (default: /dev/ttyS0)\n" \
    "\t-a <address>       IP address (default: 127.0.0.1)\n"  \
    "\t-p <port>          UDP port (default: 9999)\n" \
    "\t-i <init cmd>      RFM12i initialization command (default: 8b 200g)\n" \
    ;
    
    fprintf (stderr, usage, appname);
}

int main(int argc, char **argv)
{
    char *device = "/dev/ttyAMA0";
    char *address = "127.0.0.1";

    char defaultcmd[20] = "8b 200g";
    char *initcmd = defaultcmd;

    int port = 9997;
    int c;
    
    opterr = 0;
    
    while ((c = getopt (argc, argv, "hvd:a:p:i:")) != -1)
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

        case 'i':
            initcmd = optarg;
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
            if (verbose) printf("open serial port: %s\n", device);
            serialport_fd = open(device, O_RDWR | O_NOCTTY ); // | O_NDELAY
            
            if (serialport_fd < 0)
            {
                fprintf(stderr, "Failed to open %s: %s\n", device, strerror(errno));
                //return 1;
            }
            
            // Initialize serial port
            if (initSerialPort(serialport_fd) == -1)
            {
                fprintf(stderr, "Failed to set serial port: %s\n", strerror(errno));
                close(serialport_fd);
            }
            
            // Initialize RFM12Pi
            if (verbose) printf("Whole initialization command '%s'\n", initcmd);
            sleep(1);
            
            char *cmd = strtok(initcmd, " ");
            while (cmd != NULL)
            {
                if (verbose) printf("Write initialization command '%s'\n", cmd);
                
                if (write(serialport_fd, cmd, sizeof(cmd)) != sizeof(cmd))
                {
                    fprintf(stderr, "Failed to initialize RFM12Pi: %s\n", strerror(errno));
                    close(serialport_fd);
                }
                
                sleep(1);
                cmd = strtok(NULL, " ");
            }
            
            tcflush(serialport_fd, TCIOFLUSH);
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
        
        int msg_len = 0;
        
        // ignore first message
        int ignore_message = 1;
        
        if (serialport_fd > 0)
        {
            ssize_t len = 0;
            
            while ((len = read(serialport_fd, buffer, MAX_DATA_LEN)) > 0)
            {
                if (verbose > 3) 
                {
                	printf("read %u bytes from serial port: ", len);
                	printData( "", buffer, len, "\n");
                }
                
                for (int i = 0; i < len; i++)
                {
                    if (verbose > 4) printf("%u: 0x%02X\n", i, buffer[i]);
                    
                    if (buffer[i] == 0x0D || buffer[i] == 0x0A )
                    {
                    	if (msg_len > 0 && ignore_message == 0)
                    	{
                    		if (verbose > 1)
                    		{
								printf("sending UDP data to %s:%u", address, port);
								printData( ", data: ", message, msg_len, "\n");
							}
								
							if (sendto(udp_fd, message, msg_len, 0 , (struct sockaddr *)&dest, sizeof(dest)) == -1)
							{
								fprintf(stderr, "Failed to send udp packet: %s\n", strerror(errno));
							}
                    	}
                    	
                    	ignore_message = 0;
                    	msg_len = 0;
                    }
                    else
                    {
                    	if (msg_len > MAX_DATA_LEN)
                    	{
                    		fprintf(stderr, "too long message\n");
                    		
                    		ignore_message = 1;
                    		msg_len = 0;
                    	}
                    	else
                    	{
                    		message[msg_len++] = buffer[i];	
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
