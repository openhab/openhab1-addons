/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PCA9685GpioServoExample.java  
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import com.pi4j.component.servo.Servo;
import com.pi4j.component.servo.impl.GenericServo;
import com.pi4j.component.servo.impl.GenericServo.Orientation;
import com.pi4j.component.servo.impl.PCA9685GpioServoProvider;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Simple servo tester application demonstrating Pi4J's Servo component.
 * 
 * @author Christian Wehrli
 * @see Servo
 * @see com.pi4j.gpio.extension.pca.PCA9685GpioProvider
 */
public class PCA9685GpioServoExample {

    //------------------------------------------------------------------------------------------------------------------
    // main
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String args[]) throws Exception {
        System.out.println("<--Pi4J--> PCA9685 Servo Tester Example ... started.");
        PCA9685GpioServoExample example = new PCA9685GpioServoExample();
        Scanner scanner = new Scanner(System.in);
        char command = ' ';
        while (command != 'x') {
            printUsage();
            command = readCommand(scanner);
            switch (command) {
                case 'c' : // Choose Channel
                    example.chooseChannel(scanner);
                    break;
                case 'n' : // Neutral Position
                    example.approachNeutralPosition();
                    break;
                case 'm' : // Move
                    example.move(scanner);
                    break;
                case 's' : // Sub Trim
                    example.subtrim(scanner);
                    break;
                case 'r' : // Reverse
                    example.reverse();
                    break;
                case 't' : // Travel (adjust endpoints)
                    example.travel(scanner);
                    break;
                case 'p' : // Sweep
                    example.sweep(scanner);
                    break;
                case 'i' : // Info
                    example.info();
                    break;
                case 'x' : // Exit
                    System.out.println("Servo Example - END.");
                    break;
                case ' ' :
                    System.err.println("Invalid input.");
                    break;
                default :
                    System.err.println("Unknown command [" + command + "].");
                    break;
            }
        }
    }

    private static char readCommand(Scanner scanner) {
        char result = ' ';
        String input = scanner.nextLine();
        if (input.trim().isEmpty() == false) {
            result = input.trim().toLowerCase().charAt(0);
        }
        return result;
    }

    private static void printUsage() {
        System.out.println("");
        System.out.println("|- Commands ---------------------------------------------------------------------");
        System.out.println("| c : choose active servo channel                                                ");
        System.out.println("| n : neutral - approach neutral position                                        ");
        System.out.println("| m : move servo position                                                        ");
        System.out.println("| s : subtrim                                                                    ");
        System.out.println("| r : reverse servo direction                                                    ");
        System.out.println("| t : travel - adjust endpoints                                                  ");
        System.out.println("| p : sweep - continuously move between max left and max right position)         ");
        System.out.println("| i : info - provide info for all servo channels                                 ");
        System.out.println("| x : exit                                                                       ");
        System.out.println("|--------------------------------------------------------------------------------");
    }

    //------------------------------------------------------------------------------------------------------------------
    // PCA9685GpioProvider
    //------------------------------------------------------------------------------------------------------------------
    private final PCA9685GpioProvider gpioProvider;
    private final PCA9685GpioServoProvider gpioServoProvider;
    
    private final Servo[] servos;
    private int activeServo;

    public PCA9685GpioServoExample() throws Exception {
        gpioProvider = createProvider();

        // Define outputs in use for this example
        provisionPwmOutputs(gpioProvider);
        
        gpioServoProvider = new PCA9685GpioServoProvider(gpioProvider);
        
        servos = new Servo[16];

        // Provide servo on channel 0
        servos[0] = new GenericServo(gpioServoProvider.getServoDriver(PCA9685Pin.PWM_00), "Servo_1 (default settings)");

        // Provide servo on channel 1
        servos[1] = new GenericServo(gpioServoProvider.getServoDriver(PCA9685Pin.PWM_01), "Servo_2 (max. endpoints)");
        servos[1].setProperty(Servo.PROP_END_POINT_LEFT, Float.toString(Servo.END_POINT_MAX));
        servos[1].setProperty(Servo.PROP_END_POINT_RIGHT, Float.toString(Servo.END_POINT_MAX));

        // Provide servo on channel 2
        servos[2] = new GenericServo(gpioServoProvider.getServoDriver(PCA9685Pin.PWM_02), "Servo_3 (subtrim)");
        servos[2].setProperty(Servo.PROP_SUBTRIM, Float.toString(Servo.SUBTRIM_MAX_LEFT));

        // Provide servo on channel 3
        servos[3] = new GenericServo(gpioServoProvider.getServoDriver(PCA9685Pin.PWM_03), "Servo_4 (reverse)");
        servos[3].setProperty(Servo.PROP_IS_REVERSE, Boolean.toString(true));

        // Set active servo
        activeServo = 0;
    }

    public void chooseChannel(Scanner scanner) {
        System.out.println("");
        System.out.println("|- Choose channel ---------------------------------------------------------------");
        System.out.println("| Choose active servo channel [0..15]                                            ");
        System.out.println("| Example: 0<Enter>                                                              ");
        System.out.println("|--------------------------------------------------------------------------------");

        int channel = -1;
        boolean isValidChannel = false;
        while (isValidChannel == false) {
            String input = null;
            try {
                input = scanner.nextLine();
                channel = Integer.parseInt(input);
                if (channel >= 0 && channel <= 15) {
                    isValidChannel = true;
                } else {
                    System.err.println("Unsupported servo channel [" + channel + "], provide number between 0 and 15.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input [" + input + "], provide number between 0 and 15.");
            }
        }
        activeServo = channel;
        System.out.println("Active servo channel: " + activeServo);
    }

    public void approachNeutralPosition() {
        System.out.println("Approach neutral position");
        servos[activeServo].setPosition(0);
    }

    public void move(Scanner scanner) {
        System.out.println("");
        System.out.println("|- Move Position ----------------------------------------------------------------");
        System.out.println("| Move servo position to the left or to the right.                               ");
        System.out.println("| Example: l10<Enter> this would move the servo from its current position to the ");
        System.out.println("|          left by 10%                                                           ");
        System.out.println("| Example: r<Enter> this would move the servo from its current position to the   ");
        System.out.println("|          right by 1%                                                           ");
        System.out.println("| -> subsequent single <Enter> will repeat the previous command                  ");
        System.out.println("| -> max travel to either side is 100%                                           ");
        System.out.println("| Exit command: x<Enter>                                                         ");
        System.out.println("|--------------------------------------------------------------------------------");

        String command = null;
        while ("x".equals(command) == false) {
            float currentPosition = servos[activeServo].getPosition();
            System.out.println("Current servo position: " + currentPosition);
            String input = scanner.nextLine();
            if (input.trim().isEmpty() == false) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                int sign;
                if (command.startsWith("l")) {
                    sign = -1;
                } else if (command.startsWith("r")) {
                    sign = 1;
                } else if (command.equals("x")) {
                    continue;
                } else {
                    System.err.println("Unknown command [" + command + "].");
                    command = null;
                    continue;
                }

                int moveAmount = 1;
                try {
                    moveAmount = Integer.parseInt(command.substring(1));
                    if (moveAmount < 0 || moveAmount > 100) {
                        moveAmount = 1;
                        System.out.println("Move amount is out of range - defaulted to [1].");
                    }
                    System.out.println("Move amount is [" + moveAmount + "].");
                } catch (Exception e) {
                    System.out.println("Move amount defaulted to [1].");
                }
                float newPosition = currentPosition + moveAmount * sign;
                if (newPosition < Servo.POS_MAX_LEFT) {
                    newPosition = Servo.POS_MAX_LEFT;
                    System.out.println("Max left position exceeded - set position to " + Servo.POS_MAX_LEFT + "%");
                } else if (newPosition > Servo.POS_MAX_RIGHT) {
                    newPosition = Servo.POS_MAX_RIGHT;
                    System.out.println("Max right position exceeded - set position to " + Servo.POS_MAX_RIGHT + "%");
                }
                servos[activeServo].setPosition(newPosition);
                command = (sign == 1 ? "r" : "l") + moveAmount;
            }
        }
    }

    public void subtrim(Scanner scanner) {
        System.out.println("");
        System.out.println("|- Subtrim, adjust servo neutral position ---------------------------------------");
        System.out.println("| Example: r<Enter> this would move the servos neutral position by one step to   ");
        System.out.println("|          the right                                                             ");
        System.out.println("| Example: l<Enter> this would move the servos neutral position by one step to   ");
        System.out.println("|          the left                                                              ");
        System.out.println("| -> subsequent single <Enter> will repeat the previous command                  ");
        System.out.println("| -> max adjustment to either side is 200 steps                                  ");
        System.out.println("| Exit command: x<Enter>                                                         ");
        System.out.println("|--------------------------------------------------------------------------------");
        System.out.println("| Current Servo position: " + servos[activeServo].getPosition() + "]             ");
        System.out.println("|--------------------------------------------------------------------------------");

        String command = null;
        while ("x".equals(command) == false) {
            String propertySubtrim = servos[activeServo].getProperty(Servo.PROP_SUBTRIM, Servo.PROP_SUBTRIM_DEFAULT);
            int currentSubtrim = Integer.parseInt(propertySubtrim);
            System.out.println("Current subtrim: " + currentSubtrim);
            String input = scanner.nextLine();
            if (input.trim().isEmpty() == false) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                int moveAmount;
                if (command.startsWith("l")) {
                    moveAmount = -1;
                } else if (command.startsWith("r")) {
                    moveAmount = 1;
                } else if (command.equals("x")) {
                    continue;
                } else {
                    System.err.println("Unknown command [" + command + "].");
                    command = null;
                    continue;
                }

                float newSubtrim = currentSubtrim + moveAmount;
                if (newSubtrim < Servo.SUBTRIM_MAX_LEFT) {
                    newSubtrim = Servo.SUBTRIM_MAX_LEFT;
                    System.out.println("Max left subtrim exceeded - set value to " + Servo.SUBTRIM_MAX_LEFT);
                } else if (newSubtrim > Servo.SUBTRIM_MAX_RIGHT) {
                    newSubtrim = Servo.SUBTRIM_MAX_RIGHT;
                    System.out.println("Max right subtrim exceeded - set value to " + Servo.SUBTRIM_MAX_RIGHT);
                }
                servos[activeServo].setProperty(Servo.PROP_SUBTRIM, Float.toString(newSubtrim));
            }
        }
    }

    public void reverse() {
        boolean isReverse = Boolean.parseBoolean(servos[activeServo].getProperty(Servo.PROP_IS_REVERSE));
        Boolean newValue = isReverse ? Boolean.FALSE : Boolean.TRUE;
        servos[activeServo].setProperty(Servo.PROP_IS_REVERSE, newValue.toString());
        System.out.println("is reverse: " + newValue);
    }

    public void travel(Scanner scanner) {
        System.out.println("");
        System.out.println("|- Travel -----------------------------------------------------------------------");
        System.out.println("| Adjust endpoints.                                                              ");
        System.out.println("| Example: r125<Enter>  adjust RIGHT endpoint to 125                             ");
        System.out.println("| -> min: 0, max: 150, default 100                                               ");
        System.out.println("| Exit command: x<Enter>                                                         ");
        System.out.println("|--------------------------------------------------------------------------------");

        String command = null;
        while ("x".equals(command) == false) {
            String propertyEndpointLeft = servos[activeServo].getProperty(Servo.PROP_END_POINT_LEFT, Servo.PROP_END_POINT_DEFAULT);
            String propertyEndpointRight = servos[activeServo].getProperty(Servo.PROP_END_POINT_RIGHT, Servo.PROP_END_POINT_DEFAULT);
            System.out.println("Current endpoints: LEFT [" + propertyEndpointLeft + "], RIGHT [" + propertyEndpointRight + "]");

            String input = scanner.nextLine();
            if (input.trim().isEmpty() == false) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                String propertyToAdjust;
                if (command.startsWith("l")) {
                    propertyToAdjust = Servo.PROP_END_POINT_LEFT;
                } else if (command.startsWith("r")) {
                    propertyToAdjust = Servo.PROP_END_POINT_RIGHT;
                } else if (command.equals("x")) {
                    continue;
                } else {
                    System.err.println("Unknown command [" + command + "].");
                    command = null;
                    continue;
                }

                int newEndpointValue;
                try {
                    newEndpointValue = Integer.parseInt(command.substring(1));
                    if (newEndpointValue < Servo.END_POINT_MIN || newEndpointValue > Servo.END_POINT_MAX) {
                        System.out.println("Endpoint value is out of range - defaulted to [" + Servo.PROP_END_POINT_DEFAULT + "].");
                        newEndpointValue = Integer.parseInt(Servo.PROP_END_POINT_DEFAULT);
                    }
                    System.out.println("New value for property [" + propertyToAdjust + "]: " + newEndpointValue + "");
                } catch (Exception e) {
                    System.out.println("Endpoint value for property [" + propertyToAdjust + "] defaulted to [" + Servo.PROP_END_POINT_DEFAULT + "].");
                    newEndpointValue = Integer.parseInt(Servo.PROP_END_POINT_DEFAULT);
                }
                servos[activeServo].setProperty(propertyToAdjust, Integer.toString(newEndpointValue));
            }
        }
    }

    public void sweep(Scanner scanner) throws Exception {
        System.out.println("");
        System.out.println("|- Sweep ------------------------------------------------------------------------");
        System.out.println("| Continuously moves the servo between POS_MAX_LEFT and POS_MAX_RIGHT.           ");
        System.out.println("| To change speed provide value between 1 and 10 (10 for max speed)              ");
        System.out.println("| Example: 7<Enter>                                                              ");
        System.out.println("| Default speed: 5                                                               ");
        System.out.println("| Exit command: x<Enter>                                                         ");
        System.out.println("|--------------------------------------------------------------------------------");

        // create and start sweeper thread
        Sweeper sweeper = new Sweeper();
        sweeper.start();

        // handle user commands
        String command = null;
        while ("x".equals(command) == false) {
            String input = scanner.nextLine();
            if (input.trim().isEmpty() == false) {
                command = input.trim().toLowerCase();
            }
            if (command != null) {
                if (command.equals("x")) {
                    continue;
                }
                try {
                    int speed = Integer.parseInt(command);
                    sweeper.setSpeed(speed);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid speed value [" + command + "]. Allowed values [1..10] ");
                }
            }
        }
        sweeper.interrupt();
        servos[activeServo].setPosition(Servo.POS_NEUTRAL);
    }

    public void info() {
        for (int i = 0; i < servos.length; i++) {
            Servo servo = servos[i];
            System.out.println("Channel " + (i < 10 ? " " : "") + i + ": " + (servo != null ? servo.toString() : "N.A."));
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Helpers
    //------------------------------------------------------------------------------------------------------------------
    private PCA9685GpioProvider createProvider() throws IOException {
        BigDecimal frequency = PCA9685GpioProvider.ANALOG_SERVO_FREQUENCY;
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
        
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        return new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);
    }

    private GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Servo 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
        return myOutputs;
    }

    private class Sweeper extends Thread {

        private int speed = 5;
        private final int step = 1; // make sure this is always true: 100 % step = 0
        private final int maxSleepBetweenSteps = 100;

        @Override
        public void run() {
            int position = 0;
            Orientation orientation = Orientation.RIGHT;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (orientation == Orientation.RIGHT) {
                        if (position < Servo.POS_MAX_RIGHT) {
                            position += step;
                        } else {
                            orientation = Orientation.LEFT;
                            position -= step;
                        }
                    } else if (orientation == Orientation.LEFT) {
                        if (position > Servo.POS_MAX_LEFT) {
                            position -= step;
                        } else {
                            orientation = Orientation.RIGHT;
                            position += step;
                        }
                    } else {
                        System.err.println("Unsupported value for enum <ServoBase.Orientation>: [" + orientation + "].");
                    }

                    servos[activeServo].setPosition(position);
                    Thread.currentThread();
                    if (position % 10 == 0) {
                        System.out.println("Position: " + position);
                    }
                    Thread.sleep(maxSleepBetweenSteps / speed);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void setSpeed(int speed) {
            if (speed < 1) {
                this.speed = 1;
            } else if (speed > 10) {
                this.speed = 10;
            } else {
                this.speed = speed;
            }
        }
    }
}
