/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010-2014.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: http://scream3r.org | http://code.google.com/p/java-simple-serial-connector/
 */
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <termios.h>
#include <time.h>
#include <errno.h>//-D_TS_ERRNO use for Solaris C++ compiler

#include <sys/select.h>//since 2.5.0

#ifdef __linux__
    #include <linux/serial.h>
#endif
#ifdef __SunOS
    #include <sys/filio.h>//Needed for FIONREAD in Solaris
    #include <string.h>//Needed for select() function
#endif
#ifdef __APPLE__
    #include <serial/ioss.h>//Needed for IOSSIOSPEED in Mac OS X (Non standard baudrate)
#endif

#include <jni.h>
#include "../jssc_SerialNativeInterface.h"

//#include <iostream> //-lCstd use for Solaris linker

/*
 * Get native library version
 */
JNIEXPORT jstring JNICALL Java_jssc_SerialNativeInterface_getNativeLibraryVersion(JNIEnv *env, jobject object) {
    return env->NewStringUTF(jSSC_NATIVE_LIB_VERSION);
}

/* OK */
/*
 * Port opening
 * 
 * In 2.2.0 added useTIOCEXCL
 */
JNIEXPORT jlong JNICALL Java_jssc_SerialNativeInterface_openPort(JNIEnv *env, jobject object, jstring portName, jboolean useTIOCEXCL){
    const char* port = env->GetStringUTFChars(portName, JNI_FALSE);
    jlong hComm = open(port, O_RDWR | O_NOCTTY | O_NDELAY);
    if(hComm != -1){
        //since 2.2.0 -> (check termios structure for separating real serial devices from others)
        termios *settings = new termios();
        if(tcgetattr(hComm, settings) == 0){
        #if defined TIOCEXCL //&& !defined __SunOS
            if(useTIOCEXCL == JNI_TRUE){
                ioctl(hComm, TIOCEXCL);
            }
        #endif
            int flags = fcntl(hComm, F_GETFL, 0);
            flags &= ~O_NDELAY;
            fcntl(hComm, F_SETFL, flags);
        }
        else {
            close(hComm);//since 2.7.0
            hComm = jssc_SerialNativeInterface_ERR_INCORRECT_SERIAL_PORT;//-4;
        }
        delete settings;
        //<- since 2.2.0
    }
    else {//since 0.9 ->
        if(errno == EBUSY){//Port busy
            hComm = jssc_SerialNativeInterface_ERR_PORT_BUSY;//-1
        }
        else if(errno == ENOENT){//Port not found
            hComm = jssc_SerialNativeInterface_ERR_PORT_NOT_FOUND;//-2;
        }//-> since 2.2.0
        else if(errno == EACCES){//Permission denied
            hComm = jssc_SerialNativeInterface_ERR_PERMISSION_DENIED;//-3;
        }
        else {
            hComm = jssc_SerialNativeInterface_ERR_PORT_NOT_FOUND;//-2;
        }//<- since 2.2.0
    }//<- since 0.9
    env->ReleaseStringUTFChars(portName, port);
    return hComm;
}

/* OK */
/*
 * Choose baudrate
 */
speed_t getBaudRateByNum(jint baudRate) {
    switch(baudRate){
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 1800:
            return B1800;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 38400:
            return B38400;
    #ifdef B57600
        case 57600:
            return B57600;
    #endif
    #ifdef B115200
        case 115200:
            return B115200;
    #endif
    #ifdef B230400
        case 230400:
            return B230400;
    #endif
    #ifdef B460800
        case 460800:
            return B460800;
    #endif

    #ifdef B500000
        case 500000:
            return B500000;
    #endif
    #ifdef B576000
        case 576000:
            return B576000;
    #endif
    #ifdef B921600
        case 921600:
            return B921600;
    #endif
    #ifdef B1000000
        case 1000000:
            return B1000000;
    #endif

    #ifdef B1152000
        case 1152000:
            return B1152000;
    #endif
    #ifdef B1500000
        case 1500000:
            return B1500000;
    #endif
    #ifdef B2000000
        case 2000000:
            return B2000000;
    #endif
    #ifdef B2500000
        case 2500000:
            return B2500000;
    #endif

    #ifdef B3000000
        case 3000000:
            return B3000000;
    #endif
    #ifdef B3500000
        case 3500000:
            return B3500000;
    #endif
    #ifdef B4000000
        case 4000000:
            return B4000000;
    #endif
        default:
            return -1;
    }
}

/* OK */
/*
 * Choose data bits
 */
int getDataBitsByNum(jint byteSize) {
    switch(byteSize){
        case 5:
            return CS5;
        case 6:
            return CS6;
        case 7:
            return CS7;
        case 8:
            return CS8;
        default:
            return -1;
    }
}

//since 2.6.0 ->
const jint PARAMS_FLAG_IGNPAR = 1;
const jint PARAMS_FLAG_PARMRK = 2;
//<- since 2.6.0

/* OK */
/*
 * Set serial port settings
 *
 * In 2.6.0 added flags parameter
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_setParams
  (JNIEnv *env, jobject object, jlong portHandle, jint baudRate, jint byteSize, jint stopBits, jint parity, jboolean setRTS, jboolean setDTR, jint flags){
    jboolean returnValue = JNI_FALSE;
    
    speed_t baudRateValue = getBaudRateByNum(baudRate);
    int dataBits = getDataBitsByNum(byteSize);
    
    termios *settings = new termios();
    if(tcgetattr(portHandle, settings) == 0){
        if(baudRateValue != -1){
            //Set standart baudrate from "termios.h"
            if(cfsetispeed(settings, baudRateValue) < 0 || cfsetospeed(settings, baudRateValue) < 0){
                goto methodEnd;
            }
        }
        else {
        #ifdef __SunOS
            goto methodEnd;//Solaris don't support non standart baudrates
        #elif defined __linux__
            //Try to calculate a divisor for setting non standart baudrate
            serial_struct *serial_info = new serial_struct();
            if(ioctl(portHandle, TIOCGSERIAL, serial_info) < 0){ //Getting serial_info structure
                delete serial_info;
                goto methodEnd;
            }
            else {
                serial_info->flags |= ASYNC_SPD_CUST;
                serial_info->custom_divisor = (serial_info->baud_base/baudRate); //Calculate divisor
                if(serial_info->custom_divisor == 0){ //If divisor == 0 go to method end to prevent "division by zero" error
                    delete serial_info;
                    goto methodEnd;
                }
                settings->c_cflag |= B38400;
                if(cfsetispeed(settings, B38400) < 0 || cfsetospeed(settings, B38400) < 0){
                    delete serial_info;
                    goto methodEnd;
                }
                if(ioctl(portHandle, TIOCSSERIAL, serial_info) < 0){//Try to set new settings with non standart baudrate
                    delete serial_info;
                    goto methodEnd;
                }
                delete serial_info;
            }
        #endif
        }
    }

    /*
     * Setting data bits
     */
    if(dataBits != -1){
        settings->c_cflag &= ~CSIZE;
        settings->c_cflag |= dataBits;
    }
    else {
        goto methodEnd;
    }

    /*
     * Setting stop bits
     */
    if(stopBits == 0){ //1 stop bit (for info see ->> MSDN)
        settings->c_cflag &= ~CSTOPB;
    }
    else if((stopBits == 1) || (stopBits == 2)){ //1 == 1.5 stop bits; 2 == 2 stop bits (for info see ->> MSDN)
        settings->c_cflag |= CSTOPB;
    }
    else {
        goto methodEnd;
    }

    settings->c_cflag |= (CREAD | CLOCAL);
    settings->c_cflag &= ~CRTSCTS;
    settings->c_lflag &= ~(ICANON | ECHO | ECHOE | ECHOK | ECHONL | ECHOCTL | ECHOPRT | ECHOKE | ISIG | IEXTEN);

    settings->c_iflag &= ~(IXON | IXOFF | IXANY | INPCK | IGNPAR | PARMRK | ISTRIP | IGNBRK | BRKINT | INLCR | IGNCR| ICRNL);
#ifdef IUCLC
    settings->c_iflag &= ~IUCLC;
#endif
    settings->c_oflag &= ~OPOST;

    //since 2.6.0 ->
    if((flags & PARAMS_FLAG_IGNPAR) == PARAMS_FLAG_IGNPAR){
        settings->c_iflag |= IGNPAR;
    }
    if((flags & PARAMS_FLAG_PARMRK) == PARAMS_FLAG_PARMRK){
        settings->c_iflag |= PARMRK;
    }
    //<- since 2.6.0

    //since 0.9 ->
    settings->c_cc[VMIN] = 0;
    settings->c_cc[VTIME] = 0;
    //<- since 0.9

    /*
     * Parity bits
     */
#ifdef PAREXT
    settings->c_cflag &= ~(PARENB | PARODD | PAREXT);//Clear parity settings
#elif defined CMSPAR
    settings->c_cflag &= ~(PARENB | PARODD | CMSPAR);//Clear parity settings
#else
    settings->c_cflag &= ~(PARENB | PARODD);//Clear parity settings
#endif
    if(parity == 1){//Parity ODD
        settings->c_cflag |= (PARENB | PARODD);
        settings->c_iflag |= INPCK;
    }
    else if(parity == 2){//Parity EVEN
        settings->c_cflag |= PARENB;
        settings->c_iflag |= INPCK;
    }
    else if(parity == 3){//Parity MARK
    #ifdef PAREXT
        settings->c_cflag |= (PARENB | PARODD | PAREXT);
        settings->c_iflag |= INPCK;
    #elif defined CMSPAR
        settings->c_cflag |= (PARENB | PARODD | CMSPAR);
        settings->c_iflag |= INPCK;
    #endif
    }
    else if(parity == 4){//Parity SPACE
    #ifdef PAREXT
        settings->c_cflag |= (PARENB | PAREXT);
        settings->c_iflag |= INPCK;
    #elif defined CMSPAR
        settings->c_cflag |= (PARENB | CMSPAR);
        settings->c_iflag |= INPCK;
    #endif
    }
    else if(parity == 0){
        //Do nothing (Parity NONE)
    }
    else {
        goto methodEnd;
    }

    if(tcsetattr(portHandle, TCSANOW, settings) == 0){//Try to set all settings
    #ifdef __APPLE__
        //Try to set non-standard baud rate in Mac OS X
        if(baudRateValue == -1){
            speed_t speed = (speed_t)baudRate;
            if(ioctl(portHandle, IOSSIOSPEED, &speed) < 0){//IOSSIOSPEED must be used only after tcsetattr
                goto methodEnd;
            }
        }
    #endif
        int lineStatus;
        if(ioctl(portHandle, TIOCMGET, &lineStatus) >= 0){
            if(setRTS == JNI_TRUE){
                lineStatus |= TIOCM_RTS;
            }
            else {
                lineStatus &= ~TIOCM_RTS;
            }
            if(setDTR == JNI_TRUE){
                lineStatus |= TIOCM_DTR;
            }
            else {
                lineStatus &= ~TIOCM_DTR;
            }
            if(ioctl(portHandle, TIOCMSET, &lineStatus) >= 0){
                returnValue = JNI_TRUE;
            }
        }
    }
    methodEnd: {
        delete settings;
        return returnValue;
    }
}

const jint PURGE_RXABORT = 0x0002; //ignored
const jint PURGE_RXCLEAR = 0x0008;
const jint PURGE_TXABORT = 0x0001; //ignored
const jint PURGE_TXCLEAR = 0x0004;

/* OK */
/*
 * PurgeComm
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_purgePort
  (JNIEnv *env, jobject object, jlong portHandle, jint flags){
    int clearValue = -1;
    if((flags & PURGE_RXCLEAR) && (flags & PURGE_TXCLEAR)){
        clearValue = TCIOFLUSH;
    }
    else if(flags & PURGE_RXCLEAR) {
        clearValue = TCIFLUSH;
    }
    else if(flags & PURGE_TXCLEAR) {
        clearValue = TCOFLUSH;
    }
    else if((flags & PURGE_RXABORT) || (flags & PURGE_TXABORT)){
        return JNI_TRUE;
    }
    else {
        return JNI_FALSE;
    }
    return tcflush(portHandle, clearValue) == 0 ? JNI_TRUE : JNI_FALSE;
}

/* OK */
/* Closing the port */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_closePort
  (JNIEnv *env, jobject object, jlong portHandle){
#if defined TIOCNXCL //&& !defined __SunOS
    ioctl(portHandle, TIOCNXCL);//since 2.1.0 Clear exclusive port access on closing
#endif
    return close(portHandle) == 0 ? JNI_TRUE : JNI_FALSE;
}

/* OK */
/*
 * Setting events mask
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_setEventsMask
  (JNIEnv *env, jobject object, jlong portHandle, jint mask){
    //Don't needed in linux, implemented in java code
    return JNI_TRUE;
}

/* OK */
/*
 * Getting events mask
 */
JNIEXPORT jint JNICALL Java_jssc_SerialNativeInterface_getEventsMask
  (JNIEnv *env, jobject object, jlong portHandle){
    //Don't needed in linux, implemented in java code
    return -1;
}

/* OK */
/* 
 * RTS line status changing (ON || OFF)
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_setRTS
  (JNIEnv *env, jobject object, jlong portHandle, jboolean enabled){
    int returnValue = 0;
    int lineStatus;
    ioctl(portHandle, TIOCMGET, &lineStatus);
    if(enabled == JNI_TRUE){
        lineStatus |= TIOCM_RTS;
    }
    else {
        lineStatus &= ~TIOCM_RTS;
    }
    returnValue = ioctl(portHandle, TIOCMSET, &lineStatus);
    return (returnValue >= 0 ? JNI_TRUE : JNI_FALSE);
}

/* OK */
/* 
 * DTR line status changing (ON || OFF)
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_setDTR
  (JNIEnv *env, jobject object, jlong portHandle, jboolean enabled){
    int returnValue = 0;
    int lineStatus;
    ioctl(portHandle, TIOCMGET, &lineStatus);
    if(enabled == JNI_TRUE){
        lineStatus |= TIOCM_DTR;
    }
    else {
        lineStatus &= ~TIOCM_DTR;
    }
    returnValue = ioctl(portHandle, TIOCMSET, &lineStatus);
    return (returnValue >= 0 ? JNI_TRUE : JNI_FALSE);
}

/* OK */
/*
 * Writing data to the port
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_writeBytes
  (JNIEnv *env, jobject object, jlong portHandle, jbyteArray buffer){
    jbyte* jBuffer = env->GetByteArrayElements(buffer, JNI_FALSE);
    jint bufferSize = env->GetArrayLength(buffer);
    jint result = write(portHandle, jBuffer, (size_t)bufferSize);
    env->ReleaseByteArrayElements(buffer, jBuffer, 0);
    return result == bufferSize ? JNI_TRUE : JNI_FALSE;
}

/* OK */
/*
 * Reading data from the port
 *
 * Rewrited in 2.5.0 (using select() function for correct block reading in MacOS X)
 */
JNIEXPORT jbyteArray JNICALL Java_jssc_SerialNativeInterface_readBytes
  (JNIEnv *env, jobject object, jlong portHandle, jint byteCount){
    fd_set read_fd_set;
    jbyte *lpBuffer = new jbyte[byteCount];
    int byteRemains = byteCount;
    while(byteRemains > 0) {
        FD_ZERO(&read_fd_set);
        FD_SET(portHandle, &read_fd_set);
        select(portHandle + 1, &read_fd_set, NULL, NULL, NULL);
        int result = read(portHandle, lpBuffer + (byteCount - byteRemains), byteRemains);
        if(result > 0){
            byteRemains -= result;
        }
    }
    FD_CLR(portHandle, &read_fd_set);
    jbyteArray returnArray = env->NewByteArray(byteCount);
    env->SetByteArrayRegion(returnArray, 0, byteCount, lpBuffer);
    delete lpBuffer;
    return returnArray;
}

/* OK */
/*
 * Get bytes count in serial port buffers (Input and Output)
 */
JNIEXPORT jintArray JNICALL Java_jssc_SerialNativeInterface_getBuffersBytesCount
  (JNIEnv *env, jobject object, jlong portHandle){
    jint returnValues[2];
    returnValues[0] = -1; //Input buffer
    returnValues[1] = -1; //Output buffer
    jintArray returnArray = env->NewIntArray(2);
    ioctl(portHandle, FIONREAD, &returnValues[0]);
    ioctl(portHandle, TIOCOUTQ, &returnValues[1]);
    env->SetIntArrayRegion(returnArray, 0, 2, returnValues);
    return returnArray;
}

const jint FLOWCONTROL_NONE = 0;
const jint FLOWCONTROL_RTSCTS_IN = 1;
const jint FLOWCONTROL_RTSCTS_OUT = 2;
const jint FLOWCONTROL_XONXOFF_IN = 4;
const jint FLOWCONTROL_XONXOFF_OUT = 8;

/* OK */
/*
 * Setting flow control mode
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_setFlowControlMode
  (JNIEnv *env, jobject object, jlong portHandle, jint mask){
    jboolean returnValue = JNI_FALSE;
    termios *settings = new termios();
    if(tcgetattr(portHandle, settings) == 0){
        settings->c_cflag &= ~CRTSCTS;
        settings->c_iflag &= ~(IXON | IXOFF);
        if(mask != FLOWCONTROL_NONE){
            if(((mask & FLOWCONTROL_RTSCTS_IN) == FLOWCONTROL_RTSCTS_IN) || ((mask & FLOWCONTROL_RTSCTS_OUT) == FLOWCONTROL_RTSCTS_OUT)){
                settings->c_cflag |= CRTSCTS;
            }
            if((mask & FLOWCONTROL_XONXOFF_IN) == FLOWCONTROL_XONXOFF_IN){
                settings->c_iflag |= IXOFF;
            }
            if((mask & FLOWCONTROL_XONXOFF_OUT) == FLOWCONTROL_XONXOFF_OUT){
                settings->c_iflag |= IXON;
            }
        }
        if(tcsetattr(portHandle, TCSANOW, settings) == 0){
            returnValue = JNI_TRUE;
        }
    }
    delete settings;
    return returnValue;
}

/* OK */
/*
 * Getting flow control mode
 */
JNIEXPORT jint JNICALL Java_jssc_SerialNativeInterface_getFlowControlMode
  (JNIEnv *env, jobject object, jlong portHandle){
    jint returnValue = 0;
    termios *settings = new termios();
    if(tcgetattr(portHandle, settings) == 0){
        if(settings->c_cflag & CRTSCTS){
            returnValue |= (FLOWCONTROL_RTSCTS_IN | FLOWCONTROL_RTSCTS_OUT);
        }
        if(settings->c_iflag & IXOFF){
            returnValue |= FLOWCONTROL_XONXOFF_IN;
        }
        if(settings->c_iflag & IXON){
            returnValue |= FLOWCONTROL_XONXOFF_OUT;
        }
    }
    return returnValue;
}

/* OK */
/*
 * Send break for setted duration
 */
JNIEXPORT jboolean JNICALL Java_jssc_SerialNativeInterface_sendBreak
  (JNIEnv *env, jobject object, jlong portHandle, jint duration){
    jboolean returnValue = JNI_FALSE;
    if(duration > 0){
        if(ioctl(portHandle, TIOCSBRK, 0) >= 0){
            int sec = (duration >= 1000 ? duration/1000 : 0);
            int nanoSec = (sec > 0 ? duration - sec*1000 : duration)*1000000;
            struct timespec *timeStruct = new timespec();
            timeStruct->tv_sec = sec;
            timeStruct->tv_nsec = nanoSec;
            nanosleep(timeStruct, NULL);
            delete(timeStruct);
            if(ioctl(portHandle, TIOCCBRK, 0) >= 0){
                returnValue = JNI_TRUE;
            }
        }
    }
    return returnValue;
}

/* OK */
/*
 * Return "statusLines" from ioctl(portHandle, TIOCMGET, &statusLines)
 * Need for "_waitEvents" and "_getLinesStatus"
 */
int getLinesStatus(jlong portHandle) {
    int statusLines;
    ioctl(portHandle, TIOCMGET, &statusLines);
    return statusLines;
}

/* OK */
/*
 * Not supported in Solaris and Mac OS X
 * 
 * Get interrupts count for:
 * 0 - Break(for BREAK event)
 * 1 - TX(for TXEMPTY event)
 * --ERRORS(for ERR event)--
 * 2 - Frame
 * 3 - Overrun
 * 4 - Parity
 */
void getInterruptsCount(jlong portHandle, int intArray[]) {
#ifdef TIOCGICOUNT
    struct serial_icounter_struct *icount = new serial_icounter_struct();
    if(ioctl(portHandle, TIOCGICOUNT, icount) >= 0){
        intArray[0] = icount->brk;
        intArray[1] = icount->tx;
        intArray[2] = icount->frame;
        intArray[3] = icount->overrun;
        intArray[4] = icount->parity;
    }
    delete icount;
#endif
}

const jint INTERRUPT_BREAK = 512;
const jint INTERRUPT_TX = 1024;
const jint INTERRUPT_FRAME = 2048;
const jint INTERRUPT_OVERRUN = 4096;
const jint INTERRUPT_PARITY = 8192;

const jint EV_CTS = 8;
const jint EV_DSR = 16;
const jint EV_RING = 256;
const jint EV_RLSD = 32;
const jint EV_RXCHAR = 1;
//const jint EV_RXFLAG = 2; //Not supported
const jint EV_TXEMPTY = 4;
const jint events[] = {INTERRUPT_BREAK,
                       INTERRUPT_TX,
                       INTERRUPT_FRAME,
                       INTERRUPT_OVERRUN,
                       INTERRUPT_PARITY,
                       EV_CTS,
                       EV_DSR,
                       EV_RING,
                       EV_RLSD,
                       EV_RXCHAR,
                       //EV_RXFLAG, //Not supported
                       EV_TXEMPTY};

/* OK */
/*
 * Collecting data for EventListener class (Linux have no implementation of "WaitCommEvent" function from Windows)
 * 
 */
JNIEXPORT jobjectArray JNICALL Java_jssc_SerialNativeInterface_waitEvents
  (JNIEnv *env, jobject object, jlong portHandle) {

    jclass intClass = env->FindClass("[I");
    jobjectArray returnArray = env->NewObjectArray(sizeof(events)/sizeof(jint), intClass, NULL);

    /*Input buffer*/
    jint bytesCountIn = 0;
    ioctl(portHandle, FIONREAD, &bytesCountIn);
    
    /*Output buffer*/
    jint bytesCountOut = 0;
    ioctl(portHandle, TIOCOUTQ, &bytesCountOut);

    /*Lines status*/
    int statusLines = getLinesStatus(portHandle);

    jint statusCTS = 0;
    jint statusDSR = 0;
    jint statusRING = 0;
    jint statusRLSD = 0;
    
    /*CTS status*/
    if(statusLines & TIOCM_CTS){
        statusCTS = 1;
    }

    /*DSR status*/
    if(statusLines & TIOCM_DSR){
        statusDSR = 1;
    }

    /*RING status*/
    if(statusLines & TIOCM_RNG){
        statusRING = 1;
    }

    /*RLSD(DCD) status*/
    if(statusLines & TIOCM_CAR){
        statusRLSD = 1;
    }

    /*Interrupts*/
    int interrupts[] = {-1, -1, -1, -1, -1};
    getInterruptsCount(portHandle, interrupts);

    jint interruptBreak = interrupts[0];
    jint interruptTX = interrupts[1];
    jint interruptFrame = interrupts[2];
    jint interruptOverrun = interrupts[3];
    jint interruptParity = interrupts[4];

    for(int i = 0; i < sizeof(events)/sizeof(jint); i++){
        jint returnValues[2];
        switch(events[i]) {
            
            case INTERRUPT_BREAK: //Interrupt Break - for BREAK event
                returnValues[1] = interruptBreak;
                goto forEnd;
            case INTERRUPT_TX: //Interrupt TX - for TXEMPTY event
                returnValues[1] = interruptTX;
                goto forEnd;
            case INTERRUPT_FRAME: //Interrupt Frame - for ERR event
                returnValues[1] = interruptFrame;
                goto forEnd;
            case INTERRUPT_OVERRUN: //Interrupt Overrun - for ERR event
                returnValues[1] = interruptOverrun;
                goto forEnd;
            case INTERRUPT_PARITY: //Interrupt Parity - for ERR event
                returnValues[1] = interruptParity;
                goto forEnd;
            case EV_CTS:
                returnValues[1] = statusCTS;
                goto forEnd;
            case EV_DSR:
                returnValues[1] = statusDSR;
                goto forEnd;
            case EV_RING:
                returnValues[1] = statusRING;
                goto forEnd;
            case EV_RLSD: /*DCD*/
                returnValues[1] = statusRLSD;
                goto forEnd;
            case EV_RXCHAR:
                returnValues[1] = bytesCountIn;
                goto forEnd;
            /*case EV_RXFLAG: // Event RXFLAG - Not supported
                returnValues[0] = EV_RXFLAG;
                returnValues[1] = 0;
                goto forEnd;*/
            case EV_TXEMPTY:
                returnValues[1] = bytesCountOut;
                goto forEnd;
        }
        forEnd: {
            returnValues[0] = events[i];
            jintArray singleResultArray = env->NewIntArray(2);
            env->SetIntArrayRegion(singleResultArray, 0, 2, returnValues);
            env->SetObjectArrayElement(returnArray, i, singleResultArray);
        };
    }
    return returnArray;
}

/* OK */
/*
 * Getting serial ports names like an a String array (String[])
 */
JNIEXPORT jobjectArray JNICALL Java_jssc_SerialNativeInterface_getSerialPortNames
  (JNIEnv *env, jobject object){
    //Don't needed in linux, implemented in java code (Note: null will be returned)
    return NULL;
}

/* OK */
/*
 * Getting lines status
 *
 * returnValues[0] - CTS
 * returnValues[1] - DSR
 * returnValues[2] - RING
 * returnValues[3] - RLSD(DCD)
 */
JNIEXPORT jintArray JNICALL Java_jssc_SerialNativeInterface_getLinesStatus
  (JNIEnv *env, jobject object, jlong portHandle){
    jint returnValues[4];
    for(jint i = 0; i < 4; i++){
        returnValues[i] = 0;
    }
    jintArray returnArray = env->NewIntArray(4);

    /*Lines status*/
    int statusLines = getLinesStatus(portHandle);

    /*CTS status*/
    if(statusLines & TIOCM_CTS){
        returnValues[0] = 1;
    }

    /*DSR status*/
    if(statusLines & TIOCM_DSR){
        returnValues[1] = 1;
    }

    /*RING status*/
    if(statusLines & TIOCM_RNG){
        returnValues[2] = 1;
    }

    /*RLSD(DCD) status*/
    if(statusLines & TIOCM_CAR){
        returnValues[3] = 1;
    }
    
    env->SetIntArrayRegion(returnArray, 0, 4, returnValues);
    return returnArray;
}
