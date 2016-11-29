#ifndef HIDEKI_H
#define HIDEKI_H

#include <inttypes.h>

#define DATA_BUFFER_LENGTH 14

void setTimeOut(int timeout);

int startDecoder(int pin);
int stopDecoder(int pin);
int getDecodedData(uint8_t data[DATA_BUFFER_LENGTH]);

#endif
