import com.pi4j.component.button.ButtonEvent;
import com.pi4j.component.button.ButtonPressedListener;
import com.pi4j.component.button.ButtonReleasedListener;
import com.pi4j.device.pibrella.Pibrella;
import com.pi4j.device.pibrella.PibrellaLed;
import com.pi4j.device.pibrella.impl.PibrellaDevice;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PibrellaExample.java  
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
public class PibrellaExample {

    // create the Pibrella controller
    static final Pibrella pibrella = new PibrellaDevice();

    // stores the pulse rate for the LEDs
    static int pulseRate = 100;

    // create a tune playing thread
    static SampleTuneThread sampleTuneThread = new SampleTuneThread();

    /**
     * Start Pibrella Example
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String args[]) throws InterruptedException, IOException {

        System.out.println("<--Pi4J--> Pibrella Example ... started.");

        // -----------------------------------------------------------------
        // create a button listener
        // -----------------------------------------------------------------
        pibrella.button().addListener(new ButtonPressedListener() {
            @Override
            public void onButtonPressed(ButtonEvent event) {
                System.out.println("[BUTTON PRESSED]");
                pulseRate = 30;

                // START or STOP sample tune
                if(sampleTuneThread.isAlive())
                    stopSampleTune();
                else
                    playSampleTune();
            }
        });

        pibrella.button().addListener(new ButtonReleasedListener() {
            @Override
            public void onButtonReleased(ButtonEvent event) {
                System.out.println("[BUTTON RELEASED]");
                pulseRate = 100;
            }
        });

        // run continuously until user aborts with CTRL-C
        while(true) {

            // step up the ladder
            for(int index = PibrellaLed.RED.getIndex(); index <= PibrellaLed.GREEN.getIndex(); index++) {
                pibrella.getLed(index).pulse(pulseRate, true);
                //Thread.sleep(pulseRate);
            }

            // step down the ladder
            for(int index = PibrellaLed.GREEN.getIndex(); index >= PibrellaLed.RED.getIndex(); index--) {
                pibrella.getLed(index).pulse(pulseRate, true);
                //Thread.sleep(pulseRate);
            }
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        //gpio.shutdown();  // <-- uncomment if your program terminates
    }

    /**
     * Play sample tune using the Pibrella buzzer
     */
    public static void playSampleTune() {
        if(!sampleTuneThread.isAlive()){
            sampleTuneThread = new SampleTuneThread();
            sampleTuneThread.start();
        }
    }

    /**
     * Stop playing sample tune
     */
    public static void stopSampleTune() {
        if(sampleTuneThread.isAlive())
            sampleTuneThread.terminate();
    }

    /**
     * THE EXAMPLE CODE FOR PLAYING THE TUNE IS BASED ON THE EXAMPLE PROVIDED IN THE THE WIRING PI PROJECT
     * https://git.drogon.net/?p=pibrella;a=blob;f=tune.c;h=9ea899048461e080cc3e141e1beef67489d9b756;hb=HEAD
     */
    public static class SampleTuneThread extends Thread {

        // simple scale frequencies
        private final int scale[] = { 262, 294, 330, 349, 392, 440, 494, 525 };

        // sample tune
        private final int tune [] =
                {
                        0,6, 1,2, 2,6, 0,2,		// Doe, a deer, a
                        2,4, 0,4, 2,8,		//   fe - male deer,
                        1,6, 2,2, 3,2, 3,2, 2,2, 1,2,	// ray, a drop of gold-en
                        3,14, -1,2,			//   sun.
                        2,6, 3,2, 4,6, 2,2,		// Me, a name I
                        4,4, 2,4, 4,8,		//   call my - self
                        3,6, 4,2, 5,2, 5,2, 4,2, 3,2,	// Far, a long, long way to
                        5, 16,			//  run.
                        4,6, 0,2, 1,2, 2,2, 3,2, 4,2,	// Sew, a nee - dle pull - ing
                        5,14, -1,2,			// thread
                        5,6, 1,2, 2,2, 3,2, 4,2, 5,2,	// La, a note to fol - low
                        6,14, -1,2,			//  so.
                        6,6, 2,2, 3,2, 4,2, 5,2, 6,2,	// Tea, a drink with jam and
                        7,12, -1,4,			//  bread
                        6,2, 6,2, 5,4, 4,4, 6,4, 4,4,	// that will bring us back to
                        7,4, 4,4, 2,4, 1,4,		// do, oh, oh, oh!
                        -1, 16, -1, -1
                } ;

        private final int TEMPO=120;
        private final int MS_PER_BEAT=((1000*60/TEMPO)/4);
        private boolean stop = false;

        public void terminate(){
            stop = true;
        }

        @Override
        public void run() {
            int note;
            int duration;
            int i;

            // Play the tune
            for (i = 0 ;; i += 2)
            {
                // watch for stop requests or thread interrupts
                if(stop || Thread.interrupted())
                    break;

                note = tune[i];
                duration  = tune[i + 1];

                // validate note array element
                if ((note <= -1) && (duration <= -1)) break;

                // play note if defined; otherwise rest
                if (note >=  0)
                    pibrella.buzzer().buzz(scale [note], (MS_PER_BEAT * duration - 5));

                // Stop sound for a few mS between notes
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
