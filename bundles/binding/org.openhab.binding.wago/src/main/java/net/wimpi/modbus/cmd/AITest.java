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

import java.net.InetAddress;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.Modbus;

/**
 * Class that implements a simple commandline
 * tool for reading an analog input.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class AITest {

  public static void main(String[] args) {

    InetAddress addr = null;
    TCPMasterConnection con = null;
    ModbusRequest req = null;
    ModbusTransaction trans = null;
    int ref = 0;
    int count = 0;
    int repeat = 1;
    int port = Modbus.DEFAULT_PORT;

    try {

      //1. Setup parameters
      if (args.length < 3) {
        printUsage();
        System.exit(1);
      } else {
        try {
          String astr = args[0];
          int idx = astr.indexOf(':');
          if(idx > 0) {
            port = Integer.parseInt(astr.substring(idx+1));
            astr = astr.substring(0,idx);
          }
          addr = InetAddress.getByName(astr);
          ref = Integer.parseInt(args[1]);
          count = Integer.parseInt(args[2]);
          if(args.length == 4) {
            repeat = Integer.parseInt(args[3]);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          printUsage();
          System.exit(1);
        }
      }

      //2. Open the connection
      con = new TCPMasterConnection(addr);
      con.setPort(port);
      con.connect();

      if (Modbus.debug) System.out.println("Connected to " + addr.toString() + ":" + con.getPort());

      //3. Prepare the request
      req = new ReadInputRegistersRequest(ref, count);
      req.setUnitID(0);
      if (Modbus.debug) System.out.println("Request: " + req.getHexMessage());

      //4. Prepare the transaction
      trans = new ModbusTCPTransaction(con);
      trans.setRequest(req);

      //5. Execute the transaction repeat times
      int k = 0;
      do {
        trans.execute();
     
        ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();
        if (Modbus.debug)
          System.out.println("Response: " +
              res.getHexMessage()
          );

        for (int n = 0; n < res.getWordCount(); n++) {
          System.out.println("Word " + n + "=" + res.getRegisterValue(n));
        }

        k++;
      } while (k < repeat);

      //6. Close the connection
      con.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }//main

  private static void printUsage() {
    System.out.println(
        "java net.wimpi.modbus.cmd.AITest <address{:port} [String]> <register [int16]> <wordcount [int16]> {<repeat [int]>}"
    );
  }//printUsage

}//class AITest
