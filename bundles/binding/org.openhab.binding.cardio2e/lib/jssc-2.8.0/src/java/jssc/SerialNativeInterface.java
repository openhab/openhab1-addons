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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author scream3r
 */
public class SerialNativeInterface {

    private static final String libVersion = "2.8"; //jSSC-2.8.0 Release from 24.01.2014
    private static final String libMinorSuffix = "0"; //since 0.9.0

    public static final int OS_LINUX = 0;
    public static final int OS_WINDOWS = 1;
    public static final int OS_SOLARIS = 2;//since 0.9.0
    public static final int OS_MAC_OS_X = 3;//since 0.9.0

    private static int osType = -1;

    /**
     * @since 2.3.0
     */
    public static final long ERR_PORT_BUSY = -1;
    /**
     * @since 2.3.0
     */
    public static final long ERR_PORT_NOT_FOUND = -2;
    /**
     * @since 2.3.0
     */
    public static final long ERR_PERMISSION_DENIED = -3;
    /**
     * @since 2.3.0
     */
    public static final long ERR_INCORRECT_SERIAL_PORT = -4;

    /**
     * @since 2.6.0
     */
    public static final String PROPERTY_JSSC_NO_TIOCEXCL = "JSSC_NO_TIOCEXCL";
    /**
     * @since 2.6.0
     */
    public static final String PROPERTY_JSSC_IGNPAR = "JSSC_IGNPAR";
    /**
     * @since 2.6.0
     */
    public static final String PROPERTY_JSSC_PARMRK = "JSSC_PARMRK";

    static {
        String libFolderPath;
        String libName;

        String osName = System.getProperty("os.name");
        String architecture = System.getProperty("os.arch");
        String userHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String tmpFolder = System.getProperty("java.io.tmpdir");

        //since 2.3.0 ->
        String libRootFolder = new File(userHome).canWrite() ? userHome : tmpFolder;
        //<- since 2.3.0

        String javaLibPath = System.getProperty("java.library.path");//since 2.1.0

        if(osName.equals("Linux")){
            osName = "linux";
            osType = OS_LINUX;
        }
        else if(osName.startsWith("Win")){
            osName = "windows";
            osType = OS_WINDOWS;
        }//since 0.9.0 ->
        else if(osName.equals("SunOS")){
            osName = "solaris";
            osType = OS_SOLARIS;
        }
        else if(osName.equals("Mac OS X") || osName.equals("Darwin")){//os.name "Darwin" since 2.6.0
            osName = "mac_os_x";
            osType = OS_MAC_OS_X;
        }//<- since 0.9.0

        if(architecture.equals("i386") || architecture.equals("i686")){
            architecture = "x86";
        }
        else if(architecture.equals("amd64") || architecture.equals("universal")){//os.arch "universal" since 2.6.0
            architecture = "x86_64";
        }
        else if(architecture.equals("arm")) {//since 2.1.0
            String floatStr = "sf";
            if(javaLibPath.toLowerCase().contains("gnueabihf") || javaLibPath.toLowerCase().contains("armhf")){
                floatStr = "hf";
            }
            else {
                try {
                    Process readelfProcess =  Runtime.getRuntime().exec("readelf -A /proc/self/exe");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(readelfProcess.getInputStream()));
                    String buffer = "";
                    while((buffer = reader.readLine()) != null && !buffer.isEmpty()){
                        if(buffer.toLowerCase().contains("Tag_ABI_VFP_args".toLowerCase())){
                            floatStr = "hf";
                            break;
                        }
                    }
                    reader.close();
                }
                catch (Exception ex) {
                    //Do nothing
                }
            }
            architecture = "arm" + floatStr;
        }
        
        libFolderPath = libRootFolder + fileSeparator + ".jssc" + fileSeparator + osName;
        libName = "jSSC-" + libVersion + "_" + architecture;
        libName = System.mapLibraryName(libName);

        if(libName.endsWith(".dylib")){//Since 2.1.0 MacOSX 10.8 fix
            libName = libName.replace(".dylib", ".jnilib");
        }

        boolean loadLib = false;

        if(isLibFolderExist(libFolderPath)){
            if(isLibFileExist(libFolderPath + fileSeparator + libName)){
                loadLib = true;
            }
            else {
                if(extractLib((libFolderPath + fileSeparator + libName), osName, libName)){
                    loadLib = true;
                }
            }
        }
        else {
            if(new File(libFolderPath).mkdirs()){
                if(extractLib((libFolderPath + fileSeparator + libName), osName, libName)){
                    loadLib = true;
                }
            }
        }

        if (loadLib) {
            System.load(libFolderPath + fileSeparator + libName);
            String versionBase = getLibraryBaseVersion();
            String versionNative = getNativeLibraryVersion();
            if (!versionBase.equals(versionNative)) {
                System.err.println("Warning! jSSC Java and Native versions mismatch (Java: " + versionBase + ", Native: " + versionNative + ")");
            }
        }
    }

    /**
     * Is library folder exists
     *
     * @param libFolderPath
     *
     * @since 0.8
     */
    private static boolean isLibFolderExist(String libFolderPath) {
        boolean returnValue = false;
        File folder = new File(libFolderPath);
        if(folder.exists() && folder.isDirectory()){
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Is library file exists
     * 
     * @param libFilePath
     *
     * @since 0.8
     */
    private static boolean isLibFileExist(String libFilePath) {
        boolean returnValue = false;
        File folder = new File(libFilePath);
        if(folder.exists() && folder.isFile()){
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Extract lib to lib folder
     *
     * @param libFilePath
     * @param osName
     * @param libName
     *
     * @since 0.8
     */
    private static boolean extractLib(String libFilePath, String osName, String libName) {
        boolean returnValue = false;
        File libFile = new File(libFilePath);
        InputStream input = null;
        FileOutputStream output = null;
        input = SerialNativeInterface.class.getResourceAsStream("/libs/" + osName + "/" + libName);
        if(input != null){
            int read;
            byte[] buffer = new byte[4096];
            try {
                output = new FileOutputStream(libFilePath);
                while((read = input.read(buffer)) != -1){
                    output.write(buffer, 0, read);
                }
                output.close();
                input.close();
                returnValue = true;
            }
            catch (Exception ex) {
                try {
                    output.close();
                    if(libFile.exists()){
                        libFile.delete();
                    }
                }
                catch (Exception ex_out) {
                    //Do nothing
                }
                try {
                    input.close();
                }
                catch (Exception ex_in) {
                    //Do nothing
                }
            }
        }
        return returnValue;
    }

    /**
     * Get OS type (OS_LINUX || OS_WINDOWS || OS_SOLARIS)
     * 
     * @since 0.8
     */
    public static int getOsType() {
        return osType;
    }

    /**
     * Get jSSC version. The version of library is <b>Base Version</b> + <b>Minor Suffix</b>
     *
     * @since 0.8
     */
    public static String getLibraryVersion() {
        return libVersion + "." + libMinorSuffix;
    }

    /**
     * Get jSSC Base Version
     *
     * @since 0.9.0
     */
    public static String getLibraryBaseVersion() {
        return libVersion;
    }

    /**
     * Get jSSC minor suffix. For example in version 0.8.1 - <b>1</b> is a minor suffix
     *
     * @since 0.9.0
     */
    public static String getLibraryMinorSuffix() {
        return libMinorSuffix;
    }

    /**
     * Get jSSC native library version
     *
     * @return native lib version (for jSSC-2.8.0 should be 2.8 for example)
     *
     * @since 2.8.0
     */
    public static native String getNativeLibraryVersion();

    /**
     * Open port
     *
     * @param portName name of port for opening
     * @param useTIOCEXCL enable/disable using of <b>TIOCEXCL</b>. Take effect only on *nix based systems
     * 
     * @return handle of opened port or -1 if opening of the port was unsuccessful
     */
    public native long openPort(String portName, boolean useTIOCEXCL);

    /**
     * Setting the parameters of opened port
     *
     * @param handle handle of opened port
     * @param baudRate data transfer rate
     * @param dataBits number of data bits
     * @param stopBits number of stop bits
     * @param parity parity
     * @param setRTS initial state of RTS line (ON/OFF)
     * @param setDTR initial state of DTR line (ON/OFF)
     * @param flags additional Native settings. Take effect only on *nix based systems
     * 
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean setParams(long handle, int baudRate, int dataBits, int stopBits, int parity, boolean setRTS, boolean setDTR, int flags);

    /**
     * Purge of input and output buffer
     * 
     * @param handle handle of opened port
     * @param flags flags specifying required actions for purgePort method
     *
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean purgePort(long handle, int flags);

    /**
     * Close port
     * 
     * @param handle handle of opened port
     * 
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean closePort(long handle);

    /**
     * Set events mask
     *
     * @param handle handle of opened port
     * @param mask events mask
     * 
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean setEventsMask(long handle, int mask);

    /**
     * Get events mask
     * 
     * @param handle handle of opened port
     * 
     * @return Method returns event mask as a variable of <b>int</b> type
     */
    public native int getEventsMask(long handle);

    /**
     * Wait events
     *
     * @param handle handle of opened port
     *
     * @return Method returns two-dimensional array containing event types and their values
     * (<b>events[i][0] - event type</b>, <b>events[i][1] - event value</b>).
     */
    public native int[][] waitEvents(long handle);

    /**
     * Change RTS line state
     * 
     * @param handle handle of opened port
     * @param value <b>true - ON</b>, <b>false - OFF</b>
     *
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean setRTS(long handle, boolean value);

    /**
     * Change DTR line state
     *
     * @param handle handle of opened port
     * @param value <b>true - ON</b>, <b>false - OFF</b>
     *
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean setDTR(long handle, boolean value);

    /**
     * Read data from port
     * 
     * @param handle handle of opened port
     * @param byteCount count of bytes required to read
     * 
     * @return Method returns the array of read bytes
     */
    public native byte[] readBytes(long handle, int byteCount);

    /**
     * Write data to port
     * 
     * @param handle handle of opened port
     * @param buffer array of bytes to write
     * 
     * @return If the operation is successfully completed, the method returns true, otherwise false
     */
    public native boolean writeBytes(long handle, byte[] buffer);

    /**
     * Get bytes count in buffers of port
     *
     * @param handle handle of opened port
     *
     * @return Method returns the array that contains info about bytes count in buffers:
     * <br><b>element 0</b> - input buffer</br>
     * <br><b>element 1</b> - output buffer</br>
     *
     * @since 0.8
     */
    public native int[] getBuffersBytesCount(long handle);

    /**
     * Set flow control mode
     *
     * @param handle handle of opened port
     * @param mask mask of flow control mode
     *
     * @return If the operation is successfully completed, the method returns true, otherwise false
     *
     * @since 0.8
     */
    public native boolean setFlowControlMode(long handle, int mask);

    /**
     * Get flow control mode
     *
     * @param handle handle of opened port
     *
     * @return Mask of setted flow control mode
     *
     * @since 0.8
     */
    public native int getFlowControlMode(long handle);

    /**
     * Get serial port names like an array of String
     *
     * @return unsorted array of String with port names
     */
    public native String[] getSerialPortNames();

    /**
     * Getting lines states
     * 
     * @param handle handle of opened port
     *
     * @return Method returns the array containing information about lines in following order:
     * <br><b>element 0</b> - <b>CTS</b> line state</br>
     * <br><b>element 1</b> - <b>DSR</b> line state</br>
     * <br><b>element 2</b> - <b>RING</b> line state</br>
     * <br><b>element 3</b> - <b>RLSD</b> line state</br>
     */
    public native int[] getLinesStatus(long handle);

    /**
     * Send Break singnal for setted duration
     * 
     * @param handle handle of opened port
     * @param duration duration of Break signal
     * @return If the operation is successfully completed, the method returns true, otherwise false
     *
     * @since 0.8
     */
    public native boolean sendBreak(long handle, int duration);
}
