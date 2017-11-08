/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.myhome.fcrisciani.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myhome.fcrisciani.datastructure.action.Action;
import com.myhome.fcrisciani.datastructure.command.CommandOPEN;
import com.myhome.fcrisciani.datastructure.command.DelayInterval;
import com.myhome.fcrisciani.exception.MalformedCommandOPEN;
import com.myhome.fcrisciani.queue.PriorityCommandQueue;
import com.myhome.fcrisciani.queue.PriorityQueueThread;

/**
 * This is the class that abstract completely the complexity of communicating
 * with the OpenWebNet protocol to a MyHome webserver After having an instance
 * of this class it is possible to send and receive OpenWebNet messages directly
 * form here This class handle also a tail of commands that are sent with an
 * inter-command delay that assures the correct execution of them on the myhome
 * plant
 *
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class MyHomeJavaConnector {

    private static final Logger logger = LoggerFactory.getLogger(MyHomeJavaConnector.class);

    // ----- TYPES ----- //

    // ---- MEMBERS ---- //

    public String ip = null; // MyHome webserver IP address
    public int port = 0; // MyHome webserver port
    public String passwd = null; // MyHome webserver passwd
    private Socket commandSk = null; // Socket for command sending
    private Semaphore commandMutex = null; // Mutex for the send command section

    private Socket monitorSk = null; // Socket for plant monitoring

    private PriorityCommandQueue commandQueue = null; // Queue of commands
    private Thread commandQueueThread = null; // Queue command thread

    // ---- METHODS ---- //
    /**
     * Evaluates if the command that is going to be sent is a valid command
     *
     * @param commandString
     *            is the command in string format
     * @return returns true if the format is correct
     */
    private boolean checkCommandFormat(String commandString) {
        if (commandString.matches("\\*[#0-9]+[*#0-9]*##")) {
            return true;
        }
        return false;
    }

    /**
     * Sends a command as a string on the command socket passed
     *
     * @param sk
     *            command socket on which send the command
     * @param command
     *            string representing the open command
     * @throws IOException
     *             in case of communication error
     */
    private void sendCommandOPEN(final Socket sk, final String command) throws IOException {
        if (command != null) {
            PrintWriter output = new PrintWriter(sk.getOutputStream());
            output.write(command);
            output.flush();
        }
    }

    /**
     * Receives an array of commands ended with a ACK or NACK
     *
     * @param sk
     *            socket used to read commands
     * @return the array of commands received
     * @throws IOException
     *             in case of communication error
     */
    private String[] receiveCommandOPEN(final Socket sk) throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        String[] newMessage = MyHomeSocketFactory.readUntilAckNack(inputStream);

        return newMessage;
    }

    /**
     * Receive message from a monitor socket
     *
     * @param sk
     *            monitor socket used to read commands
     * @return the command received during monitoring
     * @throws IOException
     *             in case of communication error
     */
    private String receiveMonitorOPEN(final Socket sk) throws IOException {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        String newMessage = MyHomeSocketFactory.readUntilDelimiter(inputStream);

        return newMessage;
    }

    /* PUBLIC */
    /**
     * Create an instance of this class
     *
     * @param ip
     *            IP address of the webserver
     * @param port
     *            port number of the webserver
     */
    public MyHomeJavaConnector(final String ip, final int port) {
        this(ip, port, "");
    }

    /* PUBLIC */
    /**
     * Create an instance of this class
     *
     * @param ip
     *            IP address of the webserver
     * @param port
     *            port number of the webserver
     * @param passwd
     *            OpenWebNet password
     */
    public MyHomeJavaConnector(final String ip, final int port, final String passwd) {
        super();
        this.ip = ip;
        this.port = port;
        this.passwd = passwd;
        logger.debug("Created MyHomeJavaConnector with ip = {}, port = {} and password {}", ip, port, passwd);
        this.commandMutex = new Semaphore(1, true);
        this.commandQueue = new PriorityCommandQueue();
        this.commandQueueThread = new Thread(new PriorityQueueThread(this, commandQueue), "TailThread");
        this.commandQueueThread.start();
    }

    /* COMMAND SESSION */
    /* Command Sending Sync */
    /**
     * Send a command synchronously and atomically, create a new command socket,
     * sends the command and returns command results before closing the socket
     * created
     *
     * @param command
     *            string representing the command to send
     * @return the array of commands received as a result of the command sent
     * @throws MalformedCommandOPEN
     */
    public String[] sendCommandSync(final String command) throws MalformedCommandOPEN {
        if (checkCommandFormat(command)) {
            try {
                commandMutex.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            /** START CRITICAL SECTION */
            String[] result = null;

            try {
                commandSk = MyHomeSocketFactory.openCommandSession(ip, port, passwd);

                sendCommandOPEN(commandSk, command);
                result = receiveCommandOPEN(commandSk);

                // Assure an intertime between messages that can be sent with
                // multiple call
                Thread.sleep(300);

                MyHomeSocketFactory.disconnect(commandSk);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            commandMutex.release();
            /** END CRITICAL SECTION */

            return result;
        } else {
            throw new MalformedCommandOPEN(command);
        }
    }

    /**
     * Send a command synchronously and atomically, create a new command socket,
     * sends the command and returns command results before closing the socket
     * created
     *
     * @param command
     *            instance of the commandOpen to send
     * @return the array of commands received as a result of the command sent
     * @throws MalformedCommandOPEN
     */
    public String[] sendCommandSync(final CommandOPEN command) throws MalformedCommandOPEN {
        return sendCommandSync(command.getCommandString());
    }

    /* Command Sending Async */
    /**
     * Send a command asynchronously, this is queued with a priority and sent
     * automatically
     *
     * @param command
     *            string representing the command to send
     * @param priority
     *            queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW}
     * @throws MalformedCommandOPEN
     */
    public void sendCommandAsync(final String command, final int priority) throws MalformedCommandOPEN {
        if (checkCommandFormat(command)) {
            if (priority == 1) {
                commandQueue.addHighLevel(command);
            } else if (priority == 2) {
                commandQueue.addMediumLevel(command);
            } else {
                commandQueue.addLowLevel(command);
            }
        } else {
            throw new MalformedCommandOPEN(command);
        }
    }

    /**
     * Send a command asynchronously, this is queued with a priority and sent
     * automatically
     *
     * @param command
     *            instance of the commandOpen to send
     * @param priority
     *            queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW}
     * @throws MalformedCommandOPEN
     */
    public void sendCommandAsync(final CommandOPEN command, final int priority) throws MalformedCommandOPEN {
        sendCommandAsync(command.getCommandString(), priority);
    }

    /**
     * Send a list of commands asynchronously, these are queued with a priority
     * and sent automatically
     *
     * @param commandList
     *            array of instances of the commandOpen to send
     * @param priority
     *            queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW}
     * @throws MalformedCommandOPEN
     */
    public void sendCommandListAsync(final CommandOPEN[] commandList, final int priority) throws MalformedCommandOPEN {
        for (CommandOPEN command : commandList) {
            sendCommandAsync(command.getCommandString(), priority);
        }
    }

    /**
     * Send an Action asynchronously, all its commands are queued with a
     * priority and sent automatically
     *
     * @param action
     *            instance of Action to send
     * @param priority
     *            queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW}
     * @throws MalformedCommandOPEN
     */
    public void sendAction(final Action action, final int priority) throws MalformedCommandOPEN {
        ArrayList<CommandOPEN> commandList = action.getCommandList();
        for (CommandOPEN command : commandList) {
            if (command != null) {
                if (command instanceof DelayInterval && ((DelayInterval) command).getDelayInMillisecond() > 0) {
                    try {
                        Thread.sleep(((DelayInterval) command).getDelayInMillisecond());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    sendCommandAsync(command, priority);
                }
            }
        }

    }

    /* MONITOR SESSION */
    /**
     * Start a monitoring session
     *
     * @throws IOException
     *             in case of communication error
     */
    public void startMonitoring() throws IOException {
        monitorSk = MyHomeSocketFactory.openMonitorSession(ip, port, passwd);
    }

    /**
     * Reads the next message from the monitor session, note: you always must
     * call the method {@link startMonitoring()} before start reading from a
     * monitor session. This call is blocking on the socket but there is a
     * timeout of 45s after that an exception is thrown. The OpenWebNet protocol
     * states that after 30s the connection is automatically closed, for this
     * reason the monitor socket periodically receive some keep-alive message.
     * In case of connection drop the socket timeout fires and this method tries
     * to establish again the connection forever notifying the attempt number.
     *
     * @return the message from the monitor session
     * @throws InterruptedException
     *             notify problem on sleep method
     */
    public String readMonitoring() throws InterruptedException {
        String result = null;
        int retry = 0;
        do {
            try {
                result = receiveMonitorOPEN(monitorSk);
            } catch (IOException e) {
                try {
                    MyHomeSocketFactory.disconnect(monitorSk);
                } catch (IOException e1) {
                }
                retry++;
                Thread.sleep(1000);
                logger.warn("Monitor connection problem. Attempting retry {}.", retry);
                try {
                    startMonitoring();
                } catch (IOException e1) {
                }
            }
        } while (result == null);

        return result;
    }

    /**
     * Close the monitor session
     *
     * @throws IOException
     *             in case of communication error
     */
    public void stopMonitoring() throws IOException {
        MyHomeSocketFactory.disconnect(monitorSk);
    }

}
