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
public class SerialPortTimeoutException extends Exception {

    private String portName;
    private String methodName;
    private int timeoutValue;

    public SerialPortTimeoutException(String portName, String methodName, int timeoutValue) {
        super("Port name - " + portName + "; Method name - " + methodName + "; Serial port operation timeout (" + timeoutValue + " ms).");
        this.portName = portName;
        this.methodName = methodName;
        this.timeoutValue = timeoutValue;
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
     * Getting timeout value in millisecond
     */
    public int getTimeoutValue(){
        return timeoutValue;
    }
}
