package org.openhab.binding.urtsi.internal;

import com.google.common.base.Objects;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Future;
import org.apache.commons.io.IOUtils;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.openhab.binding.urtsi.internal.ArrayHelper;
import org.openhab.binding.urtsi.internal.DedicatedThreadExecutor;
import org.openhab.binding.urtsi.internal.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the device. This class is responsible for communicating to the hardware which is connected via a serial port.
 * It completely encapsulates the serial communication and just provides a writeString method which returns true, if the message has been transmitted successfully.
 * @author Oliver Libutzki
 * @since 1.3.0
 */
@SuppressWarnings("all")
public class UrtsiDevice {
  private final static Logger logger = new Function0<Logger>() {
    public Logger apply() {
      Logger _logger = LoggerFactory.getLogger(UrtsiDevice.class);
      return _logger;
    }
  }.apply();
  
  private final static int baud = 9600;
  
  private final static int databits = SerialPort.DATABITS_8;
  
  private final static int stopbit = SerialPort.STOPBITS_1;
  
  private final static int parity = SerialPort.PARITY_NONE;
  
  private String port;
  
  private CommPortIdentifier portId;
  
  private SerialPort serialPort;
  
  private OutputStream outputStream;
  
  private InputStream inputStream;
  
  private DedicatedThreadExecutor threadExecutor = new Function0<DedicatedThreadExecutor>() {
    public DedicatedThreadExecutor apply() {
      DedicatedThreadExecutor _dedicatedThreadExecutor = new DedicatedThreadExecutor();
      return _dedicatedThreadExecutor;
    }
  }.apply();
  
  public UrtsiDevice(final String port) {
    this.port = port;
  }
  
  /**
   * Initialize this device and open the serial port
   * 
   * @throws InitializationException
   *             if port can not be opened
   */
  public void initialize() throws InitializationException {
    Enumeration portList = CommPortIdentifier.getPortIdentifiers();
    boolean _hasMoreElements = portList.hasMoreElements();
    boolean _while = _hasMoreElements;
    while (_while) {
      {
        Object _nextElement = portList.nextElement();
        final CommPortIdentifier id = ((CommPortIdentifier) _nextElement);
        int _portType = id.getPortType();
        boolean _equals = (_portType == CommPortIdentifier.PORT_SERIAL);
        if (_equals) {
          String _name = id.getName();
          boolean _equals_1 = _name.equals(this.port);
          if (_equals_1) {
            UrtsiDevice.logger.debug("Serial port \'{}\' has been found.", this.port);
            this.portId = id;
          }
        }
      }
      boolean _hasMoreElements_1 = portList.hasMoreElements();
      _while = _hasMoreElements_1;
    }
    boolean _notEquals = (!Objects.equal(this.portId, null));
    if (_notEquals) {
      try {
        RXTXPort _open = this.portId.open("openHAB", 2000);
        this.serialPort = ((SerialPort) _open);
      } catch (final Throwable _t) {
        if (_t instanceof PortInUseException) {
          final PortInUseException e = (PortInUseException)_t;
          InitializationException _initializationException = new InitializationException(e);
          throw _initializationException;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      try {
        this.serialPort.setSerialPortParams(UrtsiDevice.baud, UrtsiDevice.databits, UrtsiDevice.stopbit, UrtsiDevice.parity);
      } catch (final Throwable _t_1) {
        if (_t_1 instanceof UnsupportedCommOperationException) {
          final UnsupportedCommOperationException e_1 = (UnsupportedCommOperationException)_t_1;
          InitializationException _initializationException_1 = new InitializationException(e_1);
          throw _initializationException_1;
        } else {
          throw Exceptions.sneakyThrow(_t_1);
        }
      }
      try {
        InputStream _inputStream = this.serialPort.getInputStream();
        this.inputStream = _inputStream;
      } catch (final Throwable _t_2) {
        if (_t_2 instanceof IOException) {
          final IOException e_2 = (IOException)_t_2;
          InitializationException _initializationException_2 = new InitializationException(e_2);
          throw _initializationException_2;
        } else {
          throw Exceptions.sneakyThrow(_t_2);
        }
      }
      try {
        OutputStream _outputStream = this.serialPort.getOutputStream();
        this.outputStream = _outputStream;
      } catch (final Throwable _t_3) {
        if (_t_3 instanceof IOException) {
          final IOException e_3 = (IOException)_t_3;
          InitializationException _initializationException_3 = new InitializationException(e_3);
          throw _initializationException_3;
        } else {
          throw Exceptions.sneakyThrow(_t_3);
        }
      }
    } else {
      StringBuilder _stringBuilder = new StringBuilder();
      final StringBuilder sb = _stringBuilder;
      Enumeration _portIdentifiers = CommPortIdentifier.getPortIdentifiers();
      portList = _portIdentifiers;
      boolean _hasMoreElements_1 = portList.hasMoreElements();
      boolean _while_1 = _hasMoreElements_1;
      while (_while_1) {
        {
          Object _nextElement = portList.nextElement();
          final CommPortIdentifier id = ((CommPortIdentifier) _nextElement);
          int _portType = id.getPortType();
          boolean _equals = (_portType == CommPortIdentifier.PORT_SERIAL);
          if (_equals) {
            String _name = id.getName();
            String _plus = (_name + "\n");
            sb.append(_plus);
          }
        }
        boolean _hasMoreElements_2 = portList.hasMoreElements();
        _while_1 = _hasMoreElements_2;
      }
      String _plus = ("Serial port \'" + this.port);
      String _plus_1 = (_plus + "\' could not be found. Available ports are:\n");
      String _string = sb.toString();
      String _plus_2 = (_plus_1 + _string);
      InitializationException _initializationException_4 = new InitializationException(_plus_2);
      throw _initializationException_4;
    }
  }
  
  /**
   * Sends a string to the serial port of this device.
   * The execution of the msg is executed in a dedicated Thread, so it's guaranteed that the device doesn't get multiple messages concurrently.
   * 
   * @param msg
   *            the string to send
   * @return Returns true, if the message has been transmitted successfully, otherwise false.
   */
  public boolean writeString(final String msg) {
    try {
      Boolean _xblockexpression = null;
      {
        ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(msg, this.port);
        UrtsiDevice.logger.debug("Writing \'{}\' to serial port {}", _newArrayList);
        final Function1<Object,Boolean> _function = new Function1<Object,Boolean>() {
            public Boolean apply(final Object it) {
              try {
                try {
                  final List<Boolean> listenerResult = CollectionLiterals.<Boolean>newArrayList();
                  final Procedure1<SerialPortEvent> _function = new Procedure1<SerialPortEvent>() {
                      public void apply(final SerialPortEvent event) {
                        int _eventType = event.getEventType();
                        final int _switchValue = _eventType;
                        boolean _matched = false;
                        if (!_matched) {
                          if (Objects.equal(_switchValue,SerialPortEvent.DATA_AVAILABLE)) {
                            _matched=true;
                            StringBuilder _stringBuilder = new StringBuilder();
                            final StringBuilder sb = _stringBuilder;
                            final byte[] readBuffer = ArrayHelper.getByteArray(20);
                            try {
                              boolean _dowhile = false;
                              do {
                                {
                                  int _available = UrtsiDevice.this.inputStream.available();
                                  boolean _greaterThan = (_available > 0);
                                  boolean _while = _greaterThan;
                                  while (_while) {
                                    {
                                      final int bytes = UrtsiDevice.this.inputStream.read(readBuffer);
                                      String _string = new String(readBuffer, 0, bytes);
                                      sb.append(_string);
                                    }
                                    int _available_1 = UrtsiDevice.this.inputStream.available();
                                    boolean _greaterThan_1 = (_available_1 > 0);
                                    _while = _greaterThan_1;
                                  }
                                  try {
                                    Thread.sleep(100);
                                  } catch (final Throwable _t) {
                                    if (_t instanceof InterruptedException) {
                                      final InterruptedException e = (InterruptedException)_t;
                                    } else {
                                      throw Exceptions.sneakyThrow(_t);
                                    }
                                  }
                                }
                                int _available = UrtsiDevice.this.inputStream.available();
                                boolean _greaterThan = (_available > 0);
                                _dowhile = _greaterThan;
                              } while(_dowhile);
                              final String result = sb.toString();
                              boolean _equals = Objects.equal(result, msg);
                              if (_equals) {
                                listenerResult.add(Boolean.valueOf(true));
                              }
                            } catch (final Throwable _t) {
                              if (_t instanceof IOException) {
                                final IOException e = (IOException)_t;
                                String _message = e.getMessage();
                                ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(UrtsiDevice.this.port, _message);
                                UrtsiDevice.logger.debug("Error receiving data on serial port {}: {}", _newArrayList);
                              } else {
                                throw Exceptions.sneakyThrow(_t);
                              }
                            }
                          }
                        }
                      }
                    };
                  UrtsiDevice.this.serialPort.addEventListener(new SerialPortEventListener() {
                      public void serialEvent(SerialPortEvent p0) {
                        _function.apply(p0);
                      }
                  });
                  UrtsiDevice.this.serialPort.notifyOnDataAvailable(true);
                  byte[] _bytes = msg.getBytes();
                  UrtsiDevice.this.outputStream.write(_bytes);
                  UrtsiDevice.this.outputStream.flush();
                  long _currentTimeMillis = System.currentTimeMillis();
                  final long timeout = (_currentTimeMillis + 1000);
                  boolean _and = false;
                  boolean _isEmpty = listenerResult.isEmpty();
                  if (!_isEmpty) {
                    _and = false;
                  } else {
                    long _currentTimeMillis_1 = System.currentTimeMillis();
                    boolean _lessThan = (_currentTimeMillis_1 < timeout);
                    _and = (_isEmpty && _lessThan);
                  }
                  boolean _while = _and;
                  while (_while) {
                    Thread.sleep(100);
                    boolean _and_1 = false;
                    boolean _isEmpty_1 = listenerResult.isEmpty();
                    if (!_isEmpty_1) {
                      _and_1 = false;
                    } else {
                      long _currentTimeMillis_2 = System.currentTimeMillis();
                      boolean _lessThan_1 = (_currentTimeMillis_2 < timeout);
                      _and_1 = (_isEmpty_1 && _lessThan_1);
                    }
                    _while = _and_1;
                  }
                  boolean _isEmpty_1 = listenerResult.isEmpty();
                  return Boolean.valueOf((!_isEmpty_1));
                } catch (final Throwable _t) {
                  if (_t instanceof IOException) {
                    final IOException e = (IOException)_t;
                    String _message = e.getMessage();
                    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(msg, UrtsiDevice.this.port, _message);
                    UrtsiDevice.logger.error("Error writing \'{}\' to serial port {}: {}", _newArrayList);
                  } else {
                    throw Exceptions.sneakyThrow(_t);
                  }
                } finally {
                  UrtsiDevice.this.serialPort.removeEventListener();
                }
                return null;
              } catch (Exception _e) {
                throw Exceptions.sneakyThrow(_e);
              }
            }
          };
        final Future<Boolean> future = this.threadExecutor.<Boolean>execute(_function);
        Boolean _get = future.get();
        _xblockexpression = (_get);
      }
      return (_xblockexpression).booleanValue();
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Close this serial device
   */
  public void close() {
    this.serialPort.removeEventListener();
    IOUtils.closeQuietly(this.outputStream);
    IOUtils.closeQuietly(this.inputStream);
    this.serialPort.close();
  }
}
