/*
 * Module for receiving and decoding of wireless weather station
 * sensor data (433MHz). Protocol used by Cresta/Irox/Mebus/Nexus/
 * Honeywell/Hideki/TFA weather stations.
 * 
 * Protocol was reverse engineered and documented by Ruud v Gessel
 * in "Cresta weather sensor protocol", see
 * http://members.upc.nl/m.beukelaar/Crestaprotocol.pdf
 *
 * Future work was done by Rory O’Hare and documented in
 * "Blind Reverse Engineering a Wireless Protocol", see 
 * https://github.com/r-ohare/Amateur-SIGINT
 *
 * This module utilizes code of the atMETEO Project, see
 * https://github.com/fetzerch/atMETEO
 *
 * License: GPLv3. See license.txt
 */

#include "hideki.h"

#include <sys/stat.h>

#include <fcntl.h>
#include <math.h>
#include <poll.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/* Activate GPIO-Pin
 * Write pin number to /sys/class/gpio/export
 * Result: 0 = O.K., -1 = Error
 */
int gpio_export(int pin);

/* Deactivate GPIO-Pin
 * Write pin number to /sys/class/gpio/unexport
 * Result: 0 = O.K., -1 = Error
 */
int gpio_unexport(int pin);

int gpio_export(int pin)
{
  int result = 0;
  static char buffer[FILENAME_MAX];
  
  struct stat state;
  snprintf(buffer, sizeof(buffer), "/sys/class/gpio/gpio%d", pin);
  if(stat(buffer, &state) == 0) {
    if(S_ISDIR(state.st_mode)) {
      gpio_unexport(pin);
    }
  }

  int fd = open("/sys/class/gpio/export", O_WRONLY);
  if (fd >= 0) {
    int bytes = snprintf(buffer, sizeof(buffer), "%d", pin);
    if (write(fd, buffer, bytes) < 0) {
      snprintf(buffer, sizeof(buffer), "Can not activate pin %d", pin);
      result = -1;
    }
    close(fd);
  } else {
    snprintf(buffer, sizeof(buffer), "Can not export pin %d", pin);
    result = -1;
  }

  if(result == 0) {
    snprintf(buffer, sizeof(buffer), "/sys/class/gpio/gpio%d/direction", pin);
    int fd = open(buffer, O_WRONLY);
    if (fd >= 0) {
      if(write(fd, "in", 2) < 0) {
        snprintf(buffer, sizeof(buffer), "Can not set direction of pin %d", pin);
        result = -1;
      }
      close(fd);
    } else {
      snprintf(buffer, sizeof(buffer), "Can not write to pin %d", pin);
      result = -1;
    }
  }
  
  if(result == 0) {
    snprintf(buffer, sizeof(buffer), "/sys/class/gpio/gpio%d/edge", pin);
    int fd = open(buffer, O_WRONLY);
    if (fd >= 0) {
      if(write(fd, "both", 4) < 0) {
        snprintf(buffer, sizeof(buffer), "Can not set edge of pin %d", pin);
        result = -1;
      }
      close(fd);
    } else {
      snprintf(buffer, sizeof(buffer), "Can not write to pin %d", pin);
      result = -1;
    }
  }

  if(result < 0) {
    perror(buffer);
  }
  
  return result;
}

int gpio_unexport(int pin)
{
  int result = 0;
  static char buffer[FILENAME_MAX];

  int fd = open("/sys/class/gpio/unexport", O_WRONLY);
  if (fd >= 0) {
    int bytes = snprintf(buffer, sizeof(buffer), "%d", pin);
    if (write(fd, buffer, bytes) < 0) {
      snprintf(buffer, sizeof(buffer), "Can not deactivate pin %d", pin);
      result = -1;
    }
    close(fd);
  } else {
    snprintf(buffer, sizeof(buffer), "Can not unexport pin %d", pin);
    result = -1;
  }

  if(result < 0) {
    perror(buffer);
  }

  return result;
}

/* Read value from pin
 * Read from /sys/class/gpio/gpio%d/value
 * Timeout timeout: maximal time to wait for interrupt
 * Duration duration: detected length of pulse. Is timeout, if failed
 * Result: Read value. Is 0xFF, if failed
 */
uint8_t read_value(unsigned int pin, int timeout, unsigned int* duration)
{
  uint8_t result = 0xFF;  // Assume, we get error...
  static char buffer[FILENAME_MAX];

  *duration = timeout;

  // Open pin
  snprintf(buffer, FILENAME_MAX, "/sys/class/gpio/gpio%d/value", pin);
  int fd = open(buffer, O_RDONLY | O_NONBLOCK);
  if (fd >= 0) {
    // Prepare interrupt struct
    struct pollfd polldat;
    memset(&polldat, 0, sizeof(polldat));
    polldat.fd = fd;
    polldat.events = POLLPRI;

    // delete possible interrupts
    lseek(fd, 0, SEEK_SET);
    memset(buffer, 0, sizeof(buffer));
    int rc = read(fd, buffer, FILENAME_MAX - 1);

    static struct timespec tOld;
    clock_gettime(CLOCK_REALTIME, &tOld);
    int pc = poll(&polldat, 1, timeout);
    static struct timespec tNew;
    clock_gettime(CLOCK_REALTIME, &tNew);
    
    if (pc > 0) {
      if (polldat.revents & POLLPRI) {
        result = atoi(buffer);
        // Pulse lenght in microseconds
        *duration = round((tNew.tv_nsec - tOld.tv_nsec) / 1000.0);
      }
    } else { // poll() failed or timeout!
      if(rc < 0) { // read() failed!
        snprintf(buffer, sizeof(buffer), "Can not read from pin %d", pin);
      } else {
        snprintf(buffer, sizeof(buffer), "Call of poll on pin %d failed", pin);
      }
    }

    close(fd);
  } else {
    snprintf(buffer, sizeof(buffer), "Can not read from pin %d", pin);
    result = 0xFF;
  }

  if(result == 0xFF) {
    perror(buffer);
  }
  
  return result;
}

int TimeOut = 5000;
pthread_t DecoderThread;
pthread_rwlock_t DecoderLock = PTHREAD_RWLOCK_INITIALIZER;
struct DecoderData {
  volatile int pin;
  volatile int terminate;

  volatile int ready;
  uint8_t data[DATA_BUFFER_LENGTH];
} DecoderData;

// Set limits according to
// http://jeelabs.org/2010/04/16/cresta-sensor/index.html
// http://jeelabs.org/2010/04/17/improved-ook-scope/index.html
static const unsigned int LOW_TIME = 200; //183;
static const unsigned int MID_TIME = 750; //726;
static const unsigned int HIGH_TIME = 1300; //1464;

void* decode(void* parameter)
{
  struct DecoderData* data = (struct DecoderData*)parameter;

  static int count = 0;   // Current bit count
  static int halfBit = 0; // Indicator for received half bit
  static uint32_t value = 0; // Received byte + parity value
  
  while(!data->terminate) {
    int reset = 1;
    unsigned int duration = 0;
    read_value(data->pin, TimeOut, &duration);  // Catch next edge time
  
    // First half bit or one
    if ((MID_TIME <= duration) && (duration < HIGH_TIME)) { // Got 1
      value = value + 1;
      value = value << 1;
      count = count + 1;
      reset = 0;
      halfBit = 0;
    } else if ((LOW_TIME <= duration) && (duration < MID_TIME)) { // Got 0?
      if(halfBit == 1) { // Got 0
        value = value + 0;
        value = value << 1;
        count = count + 1;
      }
      reset = 0;
      halfBit = (halfBit + 1) % 2;
    }

    static uint8_t byte = 0;
    static uint8_t buffer[sizeof(data->data)] = {0};
    int length = sizeof(buffer) / sizeof(buffer[0]) + 1;
    if((byte > 2) && (reset == 0)) {
      length = (buffer[2] >> 1) & 0x1F;
      if(length > sizeof(buffer) / sizeof(buffer[0]) - 1) {
        reset = 1;
      }
    }

    // Last byte has 8 bits only. No parity will be read
    // Fake parity bit to pass next step
    if((byte == length + 2) && (reset == 0) && (count == 8))
    {
      count = count + 1;
      value = __builtin_parity(value) + (value << 1);
    }

    if((count == 9) && (reset == 0)) {
      value = value >> 1; // We made 1 shift more than need. Shift back.
      if(__builtin_parity(value >> 1) == value % 2) {
        buffer[byte] = (value >> 1) & 0xFF;
        buffer[byte] = ((buffer[byte] & 0xAA) >> 1) | ((buffer[byte] & 0x55) << 1);
        buffer[byte] = ((buffer[byte] & 0xCC) >> 2) | ((buffer[byte] & 0x33) << 2);
        buffer[byte] = ((buffer[byte] & 0xF0) >> 4) | ((buffer[byte] & 0x0F) << 4);

        if(buffer[0] == 0x9F) {
          byte = byte + 1;
        } else {
          reset = 1;
        }

        if((byte > 2) && (reset == 0)) {
          length = (buffer[2] >> 1) & 0x1F;
          if(length > sizeof(buffer) / sizeof(buffer[0]) - 1) {
            reset = 1;
          }
        }

        if((byte > length + 1) && (reset == 0)) {
          uint8_t crc1 = 0, i = 0;
          for (i = 1; i < length + 1; ++i) {
            crc1 = crc1 ^ buffer[i];
          }
          if (crc1 != buffer[length + 1]) {
            reset = 1;
          }
        }

        if((byte > length + 2) && (reset == 0)) {
          uint8_t crc2 = 0, i = 0;
          for (i = 1; i < length + 2; ++i) {
            crc2 = crc2 ^ buffer[i];
            uint8_t j = 0;
            for (j = 0; j < 8; ++j) {
              if ((crc2 & 0x01) != 0) {
                crc2 = (crc2 >> 1) ^ 0xE0;
              } else {
                crc2 = (crc2 >> 1);
              }
            }
          }

          if (crc2 == buffer[length + 2]) {
            pthread_rwlock_wrlock(&DecoderLock);
            data->ready = 1;
            memcpy(data->data, buffer, sizeof(buffer));
            pthread_rwlock_unlock(&DecoderLock);
          }
          reset = 1;
        }
      }
      count = 0;
      value = 0;
      halfBit = 0;
    }

    if(reset == 1) { // Reset if failed or got valid data
      byte = 0;
      count = 0;
      value = 0;
      halfBit = 0;
      memset(buffer, 0, sizeof(buffer));
    }
  }

  return NULL;
}

void setTimeOut(int timeout) {
  if(timeout > 0) {
    TimeOut = timeout;
  }
}

/* Start decoder on pin
 * Result: 0 = O.K., -1 = Error
 */
int startDecoder(int pin) {
  int result = -1;
  memset(&DecoderData, 0, sizeof(struct DecoderData));

  if((0 < pin) && (pin < 41)) {
    DecoderData.pin = pin;
    result = pthread_rwlock_init(&DecoderLock, NULL);
    if(result == 0) {
      result = gpio_export(pin);
    }

    if(result == 0) {
      result = pthread_create(&DecoderThread, NULL, decode, &DecoderData);
    }
  }

  return (result != 0) ? -1 : result;
}

/* Stop decoder on pin
 * Result: 0 = O.K., -1 = Error
 */
int stopDecoder(int pin) {
  int result = -1;
  DecoderData.terminate = 1;

  result = pthread_join(DecoderThread, NULL);
  if(result == 0) {
    result = pthread_rwlock_destroy(&DecoderLock);
    if(result == 0) {
      result = gpio_unexport(pin);
    }
  }
  
  memset(&DecoderData, 0, sizeof(struct DecoderData));
  return (result != 0) ? -1 : result;
}

/* Get decoded data
 * Result: Length of received new data, 0 if nothing new or error
 */
int getDecodedData(uint8_t data[DATA_BUFFER_LENGTH]) {
  int result = -1;
  
  pthread_rwlock_rdlock(&DecoderLock);
  if(DecoderData.ready == 1) {
    memcpy(data, DecoderData.data, sizeof(DecoderData.data));
    result = (data[2] >> 1) & 0x1F;

    pthread_rwlock_unlock(&DecoderLock);
    pthread_rwlock_wrlock(&DecoderLock);

    DecoderData.ready = 0;
    memset(DecoderData.data, 0, sizeof(DecoderData.data));
  }
  pthread_rwlock_unlock(&DecoderLock);
  
  return result + 1;
}

/* Compile with: gcc -Wall -lm -lpthread -o hideki hideki.c */
