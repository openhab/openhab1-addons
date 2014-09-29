/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * ----------------------------------------------------------------------------
 *
 *  This application listening data from Swegon ventilation system (RS485 bus)
 *  and send valid frames to configurable IP/port address by UDP packets.
 *
 *  Serial settings: 38400 baud, 8 bits, Parity: none, Stop bits 1
 *
 *  Frame format:
 *  +----+----+-----+-----+-------+-------+-----+----+-------+
 *  | CC | 64 | <D> | <S> | <CMD> | <LEN> |  <DATA>  | <CRC> |
 *  +----+----+-----+-----+-------+-------+-----+----+-------+
 *       |<------------ HDR ------------->|
 *                                        |<- LEN -->|
 *       |<----------------- CRC ------------------->|   
 *          
 *  Checksum: CRC16
 *
 *  Author: pauli.anttila@gmail.com
 *
 *  Build: gcc -std=gnu99 -o swegongw swegongw.c
 *
 *  07.09.2013  v1.00   Initial version
 *  28.10.2013  v1.10
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

/* CRC16 implementation acording to CCITT standards */

static const unsigned short crc16tab[256]= {
    0x0000,0x1021,0x2042,0x3063,0x4084,0x50a5,0x60c6,0x70e7,
    0x8108,0x9129,0xa14a,0xb16b,0xc18c,0xd1ad,0xe1ce,0xf1ef,
    0x1231,0x0210,0x3273,0x2252,0x52b5,0x4294,0x72f7,0x62d6,
    0x9339,0x8318,0xb37b,0xa35a,0xd3bd,0xc39c,0xf3ff,0xe3de,
    0x2462,0x3443,0x0420,0x1401,0x64e6,0x74c7,0x44a4,0x5485,
    0xa56a,0xb54b,0x8528,0x9509,0xe5ee,0xf5cf,0xc5ac,0xd58d,
    0x3653,0x2672,0x1611,0x0630,0x76d7,0x66f6,0x5695,0x46b4,
    0xb75b,0xa77a,0x9719,0x8738,0xf7df,0xe7fe,0xd79d,0xc7bc,
    0x48c4,0x58e5,0x6886,0x78a7,0x0840,0x1861,0x2802,0x3823,
    0xc9cc,0xd9ed,0xe98e,0xf9af,0x8948,0x9969,0xa90a,0xb92b,
    0x5af5,0x4ad4,0x7ab7,0x6a96,0x1a71,0x0a50,0x3a33,0x2a12,
    0xdbfd,0xcbdc,0xfbbf,0xeb9e,0x9b79,0x8b58,0xbb3b,0xab1a,
    0x6ca6,0x7c87,0x4ce4,0x5cc5,0x2c22,0x3c03,0x0c60,0x1c41,
    0xedae,0xfd8f,0xcdec,0xddcd,0xad2a,0xbd0b,0x8d68,0x9d49,
    0x7e97,0x6eb6,0x5ed5,0x4ef4,0x3e13,0x2e32,0x1e51,0x0e70,
    0xff9f,0xefbe,0xdfdd,0xcffc,0xbf1b,0xaf3a,0x9f59,0x8f78,
    0x9188,0x81a9,0xb1ca,0xa1eb,0xd10c,0xc12d,0xf14e,0xe16f,
    0x1080,0x00a1,0x30c2,0x20e3,0x5004,0x4025,0x7046,0x6067,
    0x83b9,0x9398,0xa3fb,0xb3da,0xc33d,0xd31c,0xe37f,0xf35e,
    0x02b1,0x1290,0x22f3,0x32d2,0x4235,0x5214,0x6277,0x7256,
    0xb5ea,0xa5cb,0x95a8,0x8589,0xf56e,0xe54f,0xd52c,0xc50d,
    0x34e2,0x24c3,0x14a0,0x0481,0x7466,0x6447,0x5424,0x4405,
    0xa7db,0xb7fa,0x8799,0x97b8,0xe75f,0xf77e,0xc71d,0xd73c,
    0x26d3,0x36f2,0x0691,0x16b0,0x6657,0x7676,0x4615,0x5634,
    0xd94c,0xc96d,0xf90e,0xe92f,0x99c8,0x89e9,0xb98a,0xa9ab,
    0x5844,0x4865,0x7806,0x6827,0x18c0,0x08e1,0x3882,0x28a3,
    0xcb7d,0xdb5c,0xeb3f,0xfb1e,0x8bf9,0x9bd8,0xabbb,0xbb9a,
    0x4a75,0x5a54,0x6a37,0x7a16,0x0af1,0x1ad0,0x2ab3,0x3a92,
    0xfd2e,0xed0f,0xdd6c,0xcd4d,0xbdaa,0xad8b,0x9de8,0x8dc9,
    0x7c26,0x6c07,0x5c64,0x4c45,0x3ca2,0x2c83,0x1ce0,0x0cc1,
    0xef1f,0xff3e,0xcf5d,0xdf7c,0xaf9b,0xbfba,0x8fd9,0x9ff8,
    0x6e17,0x7e36,0x4e55,0x5e74,0x2e93,0x3eb2,0x0ed1,0x1ef0
};

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
    cfsetispeed(&options, B38400);
    cfsetospeed(&options, B38400);
    
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

unsigned short crc16_ccitt(const void *buf, int len)
{
    register int counter;
    register unsigned short crc = 0xFFFF;
    for( counter = 0; counter < len; counter++)
        crc = (crc<<8) ^ crc16tab[((crc>>8) ^ *(char *)buf++)&0x00FF];
    
    return crc;
}

void printUsage(char* appname)
{
    char* usage = "%s usage:\n\n" \
    "\t-h                 Print help\n" \
    "\t-d <device name>   Serial port device (default: /dev/ttyS0)\n" \
    "\t-a <address>       IP address (default: 127.0.0.1)\n" \
    "\t-p <port>          UDP port (default: 9998)\n" \
    "\t-i                 Ignore CRC failures\n" \
    ;
    
    fprintf (stderr, usage, appname);
}

int main(int argc, char **argv)
{
    char *device = "/dev/ttySO";
    char *address = "127.0.0.1";
    int port = 9998;
    int ignore_crc_failure = FALSE;
    
    int c;
    
    opterr = 0;
    
    while ((c = getopt (argc, argv, "hvd:a:p:i")) != -1)
    {
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
        	ignore_crc_failure = TRUE;
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
    
    #define MAX_DATA_LEN    200
    #define HEADER_LEN      5
    #define CRC_LEN         2
    
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
        int data_len = 0;
        int msg_len = 0;
        
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
                    
                    if (start_found == FALSE && buffer[i] == 0xCC)
                    {
                        if (verbose > 1) printf("start of frame candidate found\n");
                        start_found = TRUE;
                        msg_len = 0;
                        data_len = 0;
                        continue;
                    } 
                    
                    if (start_found)
                    {
                        message[msg_len++] = buffer[i];

                        if (message[0] != 0x64) 
                        {
                            if (verbose > 1) printf("not valid frame, restart indexing\n");
                            start_found = FALSE;
                            continue;
                        }
                        
                        if (msg_len >= (MAX_DATA_LEN - 1))
                        {
                            if (verbose > 1) printf("too long message, restart indexing\n");
                            start_found = FALSE;
                            continue;
                        }
                        
                        if (msg_len == HEADER_LEN)
                        {
                            data_len = message[4];
                                
                            if (data_len <= 0 || data_len > 30)
                            {
                                if (verbose > 1) printf("not valid message len, restart indexing\n");
                                start_found = FALSE;
                                continue;
                            }
                        } 

                        if (data_len > 0 && msg_len == (HEADER_LEN + data_len + CRC_LEN) )
                        {
                            // whole frame received
                            
                            int calculatedCRC = crc16_ccitt( message, msg_len - CRC_LEN );
                            int msgCRC = ((message[HEADER_LEN + data_len] << 8) & 0xFF00) + message[HEADER_LEN + data_len + 1];

                            if (verbose > 2) {
                                printf("whole frame received\n");
                                printf(" - destination address: 0x%02X\n",  message[2]);
                                printf(" - source address: 0x%02X\n",  message[3]);
                                printf(" - message len: %u (0x%02X)\n", data_len, data_len);
                                printf(" - commmand: %u (0x%02X)\n", message[5], message[5]);
                                printData( " - data: ", &message[6], data_len - HEADER_LEN, "\n");
                                printf(" - message CRC: %u (0x%04X)\n", msgCRC, msgCRC);
                            }
                            
                            if (verbose > 1) printf("calculated CRC %u (0x%04X)...", calculatedCRC, calculatedCRC);
                            
                            int crc_ok = FALSE;
                            
                            if (msgCRC == calculatedCRC)
                            {
                            	crc_ok = TRUE;
                            }
                            
                            if (crc_ok || ignore_crc_failure)
                            {
                                if (verbose > 1) {
                                    printf("ok\n");
                                    printf("sending UDP data to %s:%u", address, port);
                                    printData( ", data: ", message, HEADER_LEN + data_len, "\n");
                                }
                                
                                if (sendto(udp_fd, message, HEADER_LEN + data_len + CRC_LEN, 0 , (struct sockaddr *)&dest, sizeof(dest)) == -1)
                                {
                                    fprintf(stderr, "Failed to send udp packet: %s\n", strerror(errno));
                                }
                            }
                            else
                            {
                                if (verbose > 1) printf("failed (%u != %u)\n", calculatedCRC, msgCRC);
                            }


                            if (verbose) {
                                printf("addresses: 0x%02X -> 0x%02X  len: %03u (0x%02X) command: %03u (0x%02X) ", 
                                    message[3], message[2],  data_len, data_len, message[5], message[5]);
                                
                                printData( "data: ", &message[6], data_len - CRC_LEN, " ");
                                printf("CRC: %u (0x%04X) crc ok: %u\n", msgCRC, msgCRC, crc_ok);
                            }
                            
                            start_found = FALSE;
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
