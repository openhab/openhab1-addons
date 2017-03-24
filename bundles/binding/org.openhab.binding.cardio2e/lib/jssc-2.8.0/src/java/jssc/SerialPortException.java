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
package jssc;

/**
 *
 * @author scream3r
 */
public class SerialPortException extends Exception {

    final public static String TYPE_PORT_ALREADY_OPENED = "Port already opened";
    final public static String TYPE_PORT_NOT_OPENED = "Port not opened";
    final public static String TYPE_CANT_SET_MASK = "Can't set mask";
    final public static String TYPE_LISTENER_ALREADY_ADDED = "Event listener already added";
    final public static String TYPE_LISTENER_THREAD_INTERRUPTED = "Event listener thread interrupted";
    final public static String TYPE_CANT_REMOVE_LISTENER = "Can't remove event listener, because listener not added";
    /**
     * @since 0.8
     */
    final public static String TYPE_PARAMETER_IS_NOT_CORRECT = "Parameter is not correct";
    /**
     * @since 0.8
     */
    final public static String TYPE_NULL_NOT_PERMITTED = "Null not permitted";
    /**
     * @since 0.9.0
     */
    final public static String TYPE_PORT_BUSY = "Port busy";
    /**
     * @since 0.9.0
     */
    final public static String TYPE_PORT_NOT_FOUND = "Port not found";
    /**
     * @since 2.2.0
     */
    final public static String TYPE_PERMISSION_DENIED = "Permission denied";
    /**
     * @since 2.3.0
     */
    final public static String TYPE_INCORRECT_SERIAL_PORT = "Incorrect serial port";

    private String portName;
    private String methodName;
    private String exceptionType;

    public SerialPortException(String portName, String methodName, String exceptionType){
        super("Port name - " + portName + "; Method name - " + methodName + "; Exception type - " + exceptionType + ".");
        this.portName = portName;
        this.methodName = methodName;
        this.exceptionType = exceptionType;
    }

    /**
     * Getting port name during operation with which the exception was called
     */
    public String getPortName(){
        return portName;
    }

    /**
     * Getting method name during execution of which the exception was called
     */
    public String getMethodName(){
        return methodName;
    }

    /**
     * Getting exception type
     */
    public String getExceptionType(){
        return exceptionType;
    }
}
