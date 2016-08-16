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
#include <jni.h>

void JNICALL Java_org_openhab_binding_hideki_internal_HidekiDecoder_setTimeOut(JNIEnv* env, jclass obj, jint timeout)
{
  (*env)->MonitorEnter(env, obj);
  setTimeOut(timeout);
  (*env)->MonitorExit(env, obj);
}

jint JNICALL Java_org_openhab_binding_hideki_internal_HidekiDecoder_startDecoder(JNIEnv* env, jclass obj, jint pin)
{
  jint result = 0;
  
  (*env)->MonitorEnter(env, obj);
  result = startDecoder(pin);
  (*env)->MonitorExit(env, obj);

  return result;
}

jint JNICALL Java_org_openhab_binding_hideki_internal_HidekiDecoder_stopDecoder(JNIEnv* env, jclass obj, jint pin)
{
  jint result = 0;
  
  (*env)->MonitorEnter(env, obj);
  result = stopDecoder(pin);
  (*env)->MonitorExit(env, obj);

  return result;
}

jintArray JNICALL Java_org_openhab_binding_hideki_internal_HidekiDecoder_getDecodedData(JNIEnv* env, jclass obj)
{
  jintArray result = NULL;

  (*env)->MonitorEnter(env, obj);
  
  uint8_t data[DATA_BUFFER_LENGTH];
  jint length = getDecodedData(data);
  if (length > 0) {
    result = (*env)->NewIntArray(env, length);
    jint* buffer = (*env)->GetIntArrayElements(env, result, NULL);

    jint i;
    for (i = 0; i < length; i++) {
      buffer[i] = data[i];
    }

    (*env)->ReleaseIntArrayElements(env, result, buffer, 0);
  }
 
  (*env)->MonitorExit(env, obj);
 
  return result;
}
