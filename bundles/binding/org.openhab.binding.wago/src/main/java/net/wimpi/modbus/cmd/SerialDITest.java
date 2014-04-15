/***
 * Copyright 2002-2010 jamod development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***/

package net.wimpi.modbus.cmd;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;
import net.wimpi.modbus.util.BitVector;

/**
 * Class that implements a simple commandline
 * tool for reading an analog input.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class SerialDITest {

  public static void main(String[] args) {

    SerialConnection con = null;
    ModbusSerialTransaction trans = null;
    ReadInputDiscretesRequest req = null;
    ReadInputDiscretesResponse res = null;

    String portname = null;
    int unitid = 0;
    int ref = 0;
    int count = 0;
    int repeat = 1;

    try {

      //1. Setup the parameters
      if (args.length < 4) {
        printUsage();
        System.exit(1);
      } else {
        try {
          portname = args[0];
          unitid = Integer.parseInt(args[1]);
          ref = Integer.parseInt(args[2]);
          count = Integer.parseInt(args[3]);
          if (args.length == 5) {
            repeat = Integer.parseInt(args[4]);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          printUsage();
          System.exit(1);
        }
      }

      //2. Set slave identifier for master response parsing
      ModbusCoupler.getReference().setUnitID(unitid);

      System.out.println("net.wimpi.modbus.debug set to: " +
                         System.getProperty("net.wimpi.modbus.debug"));

      //3. Setup serial parameters
      SerialParameters params = new SerialParameters();
      params.setPortName(portname);
      params.setBaudRate(115200);
      params.setDatabits(7);
      params.setParity("None");
      params.setStopbits(2);
//       params.setEncoding("rtu");
//       params.setEcho(true);
      if (Modbus.debug) System.out.println("Encoding [" + params.getEncoding() + "]");

      //4. Open the connection
      con = new SerialConnection(params);
      con.open();


      //5. Prepare a request
      req = new ReadInputDiscretesRequest(ref, count);
      req.setUnitID(unitid);
      req.setHeadless();
      if (Modbus.debug) System.out.println("Request: " + req.getHexMessage());

      //6. Prepare the transaction
      trans = new ModbusSerialTransaction(con);
      trans.setRequest(req);

      //7. Execute the transaction repeat times
      int k = 0;
      do {
        trans.execute();

        res = (ReadInputDiscretesResponse) trans.getResponse();
        if (Modbus.debug) System.out.println("Response: " + res.getHexMessage());
        BitVector inputs = res.getDiscretes();
        byte ret[] = new byte[inputs.size()];
        for (int i = 0; i < count; i++) {
          System.out.println("Bit " + i + " = " + inputs.getBit(i));
        }

        k++;
      } while (k < repeat);

      //8. Close the connection
      con.close();

    } catch (Exception ex) {
      ex.printStackTrace();
      // Close the connection
      con.close();
    }
  }//main

  private static void printUsage() {
    System.out.println(
        "java net.wimpi.modbus.cmd.SerialAITest <portname [String]>  <Unit Address [int8]> <register [int16]> <wordcount [int16]> {<repeat [int]>}"
    );
  }//printUsage

}//class SerialAITest
