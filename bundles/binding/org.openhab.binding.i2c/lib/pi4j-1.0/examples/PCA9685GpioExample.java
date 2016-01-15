/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCA9685GpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import java.math.BigDecimal;
import java.util.Scanner;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for GPIO PWM pin control using the PCA9685 16-channel, 12-bit PWM I2C-bus LED/Servo controller.
 * </p>
 * <p>
 * More information about the PCA9685 can be found here:<br>
 * <a href="http://www.nxp.com/documents/data_sheet/PCA9685.pdf">PCA9685.pdf</a><br><br>
 * ...and especially about the board here:<br>
 * <a href="http://www.adafruit.com/products/815">Adafruit 16-Channel 12-bit PWM/Servo Driver</a>
 * </p>
 * 
 * @author Christian Wehrli
 * @see PCA9685GpioProvider
 */
public class PCA9685GpioExample {

    private static final int SERVO_DURATION_MIN = 900;
    private static final int SERVO_DURATION_NEUTRAL = 1500;
    private static final int SERVO_DURATION_MAX = 2100;

    @SuppressWarnings("resource")
    public static void main(String args[]) throws Exception {
        System.out.println("<--Pi4J--> PCA9685 PWM Example ... started.");
        // This would theoretically lead into a resolution of 5 microseconds per step:
        // 4096 Steps (12 Bit)
        // T = 4096 * 0.000005s = 0.02048s
        // f = 1 / T = 48.828125
        BigDecimal frequency = new BigDecimal("48.828");
        // Correction factor: actualFreq / targetFreq
        // e.g. measured actual frequency is: 51.69 Hz
        // Calculate correction factor: 51.65 / 48.828 = 1.0578
        // --> To measure actual frequency set frequency without correction factor(or set to 1)
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
        // Create custom PCA9685 GPIO provider
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);
        // Define outputs in use for this example
        GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider);
        // Reset outputs
        gpioProvider.reset();
        //
        // Set various PWM patterns for outputs 0..9
        final int offset = 400;
        final int pulseDuration = 600;
        for (int i = 0; i < 10; i++) {
            Pin pin = PCA9685Pin.ALL[i];
            int onPosition = checkForOverflow(offset * i);
            int offPosition = checkForOverflow(pulseDuration * (i + 1));
            gpioProvider.setPwm(pin, onPosition, offPosition);
        }
        // Set full ON
        gpioProvider.setAlwaysOn(PCA9685Pin.PWM_10);
        // Set full OFF
        gpioProvider.setAlwaysOff(PCA9685Pin.PWM_11);
        // Set 0.9ms pulse (R/C Servo minimum position) 
        gpioProvider.setPwm(PCA9685Pin.PWM_12, SERVO_DURATION_MIN);
        // Set 1.5ms pulse (R/C Servo neutral position) 
        gpioProvider.setPwm(PCA9685Pin.PWM_13, SERVO_DURATION_NEUTRAL);
        // Set 2.1ms pulse (R/C Servo maximum position) 
        gpioProvider.setPwm(PCA9685Pin.PWM_14, SERVO_DURATION_MAX);
        //
        // Show PWM values for outputs 0..14
        for (GpioPinPwmOutput output : myOutputs) {
            int[] onOffValues = gpioProvider.getPwmOnOffValues(output.getPin());
            System.out.println(output.getPin().getName() + " (" + output.getName() + "): ON value [" + onOffValues[0] + "], OFF value [" + onOffValues[1] + "]");
        }
        System.out.println("Press <Enter> to terminate...");
        new Scanner(System.in).nextLine();
    }

    private static int checkForOverflow(int position) {
        int result = position;
        if (position > PCA9685GpioProvider.PWM_STEPS - 1) {
            result = position - PCA9685GpioProvider.PWM_STEPS - 1;
        }
        return result;
    }

    private static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
        return myOutputs;
    }
}
