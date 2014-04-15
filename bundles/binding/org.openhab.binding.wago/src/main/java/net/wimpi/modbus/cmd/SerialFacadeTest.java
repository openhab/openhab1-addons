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

//////////////////////////////////////////////////////////////////////////
//
//  File:  SerialFacadeTest.java
//
//  Description: Unit test driver to exerecise the methods for
//  ModbusSerialMaster class.
//
//  Programmer:  JDC (CCC), Wed Feb  4 11:54:23 2004
//
//  Change History: 
//
//  $Log: SerialFacadeTest.java,v $
//  Revision 1.2  2004/10/21 16:44:36  wimpi
//  Please see status file for changes.
//
//  Revision 1.1  2004/09/30 01:45:38  jdcharlton
//  Test driver for ModbusSerialMaster facade
//
//
//
//////////////////////////////////////////////////////////////////////////

package net.wimpi.modbus.cmd;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.facade.ModbusSerialMaster;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;
import net.wimpi.modbus.util.ModbusUtil;
import net.wimpi.modbus.util.SerialParameters;

public class SerialFacadeTest {

  public static void main(String[] args) {
    int inChar = -1;
    int result = 0;
    boolean finished = false;
    int slaveId = 88;
    String portname = null;
    ModbusSerialMaster msm = null;

    try {
      //1. Setup the parameters
      if (args.length < 2) {
        printUsage();
        System.exit(1);
      } else {
        try {
          portname = args[0];
          slaveId = Integer.parseInt(args[1]);
        } catch (Exception ex) {
          ex.printStackTrace();
          printUsage();
          System.exit(1);
        }
      }

      System.out.println(" sending test messages to slave: " + slaveId);
      System.out.println("net.wimpi.modbus.debug set to: " +
          System.getProperty("net.wimpi.modbus.debug"));

      System.out.println("Hit enter to start and <s enter> to terminate the test.");
      inChar = System.in.read();
      if ((inChar == 's') || (inChar == 'S')) {
        System.out.println("Exiting");
        System.exit(0);
      }

      //2. Setup serial parameters
      SerialParameters params = new SerialParameters();
      params.setPortName(portname);
      params.setBaudRate(38400);
      params.setDatabits(8);
      params.setParity("None");
      params.setStopbits(1);
      params.setEncoding("rtu");
      params.setEcho(true);
      if (Modbus.debug) System.out.println("Encoding [" + params.getEncoding() + "]");

      //3. Create the master facade
      msm = new ModbusSerialMaster(params);
      msm.connect();

      do {
        if (msm.writeCoil(slaveId, 4, true) == true) {
          System.out.println("Set output 5 to true");
        } else {
          System.err.println("Error setting slave " + slaveId + " output 5");
        }
        BitVector coils = msm.readCoils(slaveId, 0, 8);
        if (coils != null) {
          System.out.print("Coils:");
          for (int i = 0; i < coils.size(); i++) {
            System.out.print(" " + i + ": " + coils.getBit(i));
          }
          System.out.println();

          try {
            msm.writeMultipleCoils(slaveId, 0, coils);
          } catch (ModbusException ex) {
            System.out.println("Error writing coils: " + result);
          }
        } else {
          System.out.println("Outputs: null");
          msm.disconnect();
          System.exit(-1);
        }


        BitVector digInp = msm.readInputDiscretes(slaveId, 0, 8);

        if (digInp != null) {
          System.out.print("Digital Inputs:");
          for (int i = 0; i < digInp.size(); i++) {
            System.out.print(" " + i + ": " + digInp.getBit(i));
          }
          System.out.println();
          System.out.println("Inputs: " + ModbusUtil.toHex(digInp.getBytes()));
        } else {
          System.out.println("Inputs: null");
          msm.disconnect();
          System.exit(-1);
        }

        InputRegister[] ai = null;
        for (int i = 1000; i < 1010; i++) {
          ai = msm.readInputRegisters(slaveId, i, 1);
          if (ai != null) {
            System.out.print("Tag " + i + ": ");
            for (int n = 0; n < ai.length; n++) {
              System.out.print(" " + ai[n].getValue());
            }
            System.out.println();
          } else {
            System.out.println("Tag: " + i + " null");
            msm.disconnect();
            System.exit(-1);
          }
        }

        Register[] regs = null;
        for (int i = 1000; i < 1005; i++) {
          regs = msm.readMultipleRegisters(slaveId, i, 1);
          if (regs != null) {
            System.out.print("RWRegisters " + i + " length: " + regs.length);
            for (int n = 0; n < regs.length; n++) {
              System.out.print(" " + regs[n].getValue());
            }
            System.out.println();
          } else {
            System.out.println("RWRegisters " + i + ": null");
            msm.disconnect();
            System.exit(-1);
          }
        }
        regs = msm.readMultipleRegisters(slaveId, 0, 10);
        System.out.println("Registers: ");
        if (regs != null) {
          System.out.print("regs :");
          for (int n = 0; n < regs.length; n++) {
            System.out.print("  " + n + "= " + regs[n]);
          }
          System.out.println();
        } else {
          System.out.println("Registers: null");
          msm.disconnect();
          System.exit(-1);
        }
        while (System.in.available() > 0) {
          inChar = System.in.read();
          if ((inChar == 's') || (inChar == 'S')) {
            finished = true;
          }
        }
      } while (!finished);
    } catch (Exception e) {
      System.err.println("SerialFacadeTest driver: " + e);
      e.printStackTrace();
    }
    msm.disconnect();
  }

  private static void printUsage() {
    System.out.println("java net.wimpi.modbus.cmd.SerialAITest <portname [String]>  <Unit Address [int8]>");
  }//printUsage
}
