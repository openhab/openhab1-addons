/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cicolella.openwebnet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author mauro
 */
public class OWNUtilities {

	/**
	 * frame validation
	 * 
	 */
	private static boolean isValidFrame(String frameOpen) {
		int MAX_LENGTH_OPEN = 30;
		int lengthFrameOpen = frameOpen.length();
		String frameType = null;
		int j = 0;
		int i = 0;
		String sup = null;
		String campo = null;

		// frame ACK -->
		if (frameOpen.equals(OpenWebNet.MSG_OPEN_ACK)) {
			frameType = "OK_FRAME";
			return (true);
		}

		// frame NACK -->
		if (frameOpen.equals(OpenWebNet.MSG_OPEN_NACK)) {
			frameType = "KO_FRAME";
			return (true);
		}

		// se il primo carattere non è *
		// oppure la frame è¨ troppo lunga
		// oppure se non termina con due '#'
		if ((frameOpen.charAt(0) != '*') || (lengthFrameOpen > MAX_LENGTH_OPEN)
				|| (frameOpen.charAt(lengthFrameOpen - 1) != '#')
				|| (frameOpen.charAt(lengthFrameOpen - 2) != '#')) {
			frameType = "ERROR_FRAME";
			return (false);
		}

		// Controllo se sono stati digitati dei caratteri
		for (j = 0; j < lengthFrameOpen; j++) {
			if (!Character.isDigit(frameOpen.charAt(j))) {
				if ((frameOpen.charAt(j) != '*')
						&& (frameOpen.charAt(j) != '#')) {
					System.out.println("Â°Â°Â° FRAME ERROR Â°Â°Â°");
					frameType = "ERROR_FRAME";
					return (false);
				}
			}
		}

		// frame normale ...
		// *chi#indirizzo*cosa*dove#livello#indirizzo*quando##
		if (frameOpen.charAt(1) != '#') {
			// System.out.println("frame normale");
			frameType = "NORMAL_FRAME";
			// estraggo i vari campi della frame open nella prima modalitÃ 
			// (chi+indirizzo e dove+livello+interfaccia)
			// assegnaChiCosaDoveQuando();
			// estraggo gli eventuali valori di indirizzo
			// assegnaIndirizzo();
			// estraggo gli eventuali valori di livello ed interfaccia
			// assegnaLivelloInterfaccia();
			return (true);
		}

		// per le altre tipologie di frame
		sup = null;
		sup = frameOpen.substring(2, frameOpen.length());
		// sprintf(sup, "%s", frameOpen+2);
		campo = null;
		i = 0;
		while (sup.charAt(i) != '*') {
			i++;
		}
		campo = sup.substring(0, i);
		// sprintf(campo, "%s", strtok(sup, "*"));
		sup = null;
		sup = frameOpen.substring(2 + campo.length() + 1, frameOpen.length());
		// sprintf(sup, "%s", frameOpen+2+strlen(campo)+1);
		if (sup.charAt(0) != '*') {
			i = 0;
			boolean trovato = false;
			while (i < sup.length()) {
				if (sup.charAt(i) == '*') {
					trovato = true;
					break;
				}
				i++;
			}
			if (trovato) {
				campo = campo.concat(sup.substring(0, i));
			}

			// sprintf(campo, "%s%s", campo, strtok(sup, "*"));
			sup = null;
			sup = frameOpen.substring(2 + campo.length() + 1,
					frameOpen.length());
			// sprintf(sup, "%s", frameOpen+2+strlen(campo)+1);
		}

		// frame richiesta stato ...
		// *#chi*dove##
		if (sup.charAt(0) != '*') {
			// System.out.println("frame stato");
			frameType = "STATE_FRAME";
			return (true);
		} else {
			// frame di richiesta misura ...
			// *#chi*dove*grandezza## o *#chi*dove*#grandezza*valore_Nï¿½##
			if (sup.charAt(1) != '#') {
				// System.out.println("Measure state");
				frameType = "MEASURE_FRAME";
				return (true);
			} // frame di scrittura grandezza ...
				// *#chi*dove*#grandezza*valore_Nï¿½##
			else {
				// System.out.println("frame write");
				frameType = "WRITE_FRAME";
				return (true);
			}
		}
	}

	// create the frame to send to the own gateway
	public static String createFrame(ProtocolRead c) {
		String frame = null;
		String address[] = null;
		String who = null;
		String what = null;
		String where = null;

		who = c.getProperty("who");
		what = c.getProperty("what");
		address = c.getProperty("address").split("\\*");
		where = address[0];
		frame = "*" + who + "*" + what + "*" + where + "##";
		return (frame);
	}

	public static String getDateTime() {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return (sdf.format(calendar.getTime()));
	}
}
