package it.cicolella.openwebnet;

/*****************************************************************
 * BTicinoWriteThread.java                                       *
 * Original code:			          -              *
 * date          : Sep 8, 2004                                   *
 * copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy  *
 *                     Embedded Software Development Laboratory  *
 * license       : GPL                                           *
 * email         : 		             		         *
 * web site      : www.bticino.it; www.myhome-bticino.it         *
 *                                                               *
 * Modified and adapted for Freedomotic project by:              *
 * Mauro Cicolella - Enrico Nicoletti                            *
 * date          : 24/11/2011                                    *
 * web site      : www.freedomotic.com                           *
 *****************************************************************/
/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
/**
 * Description: Thread for sending Open command by GestSocketCommand class
 * 
 */
public class BTicinoWriteThread extends Thread {

    String open = null;
    Integer sentCommand = 0;

    /**
     * Costructor
     * @param Open command
     */
    public BTicinoWriteThread(String openCommand) {
        open = openCommand;
    }

    public int returnValue() {
        return (sentCommand);
    }

    /**
     * Thread for sending Open command
     * return value is stored in sentCommand variable
     */
    public void run() {
        Integer sentCommand = 0;
        sentCommand = OpenWebNet.gestSocketCommands.send(open);
    }
}
