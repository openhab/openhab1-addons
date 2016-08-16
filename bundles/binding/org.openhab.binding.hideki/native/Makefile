TARGET=libhideki.so

DEBUG	     = -O3
CC	       = gcc
INCLUDE	   = -I. -IwiringPi/wiringPi                           \
             -IwiringPi/devLib                                 \
             -I/$(JAVA_HOME)/include                           \
             -I/$(JAVA_HOME)/include/linux                     \
             -I/usr/lib/jvm/jdk-8-oracle/include               \
             -I/usr/lib/jvm/jdk-8-oracle/include/linux         \
             -I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include       \
             -I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include/linux \
             -I/usr/lib/jvm/java-8-openjdk-armhf/include       \
             -I/usr/lib/jvm/java-8-openjdk-armhf/include/linux \
             -I/usr/local/include -I/usr/local/include/linux

CFLAGS      := $(DEBUG) -Wall $(INCLUDE) -Winline -pipe $(CARGS) -fPIC
LIBS         =  -lm -lpthread

CLASS_PATH = ../target
vpath %.class $(CLASS_PATH)

# Should not alter anything below this line
###############################################################################
SRC	=   hideki.c HidekiDecoder.c

OBJ =	$(SRC:.c=.o)

all:	$(OBJ)
	@$(CC) $(OBJ) -shared -o $(TARGET) $(INCLUDE) $(LIBS)

.c.o:
	@echo [COMPILE] $<
	@$(CC) -c $(CFLAGS) $< -o $@

clean:
	rm -f $(OBJ) $(TARGET) *~ core tags Makefile.bak

tags:	$(SRC)
	@echo [ctags]
	@ctags $(SRC)

depend:
	makedepend -Y $(SRC)

install:	$(TARGET)
	@echo [install]
	install -m 0755 -d /usr/local/lib
	install -m 0755 -d /usr/local/include
	install -m 0644 $(TARGET)	/usr/local/lib

uninstall:
	@echo [uninstall]
	rm -f /usr/local/lib/$(TARGET)

hideki.o: hideki.h
HidekiDecoder.o: HidekiDecoder.h
#HidekiDecoder.h : HidekiDecoder.class
#	javah -classpath $(CLASS_PATH) $*
