package org.openhab.binding.piface.internal;

/**
 * Thrown on error in network communication with PiFace Raspberry's
 */
public class ErrorResponseException extends Exception {

    public ErrorResponseException(String message) {
        super(message);
    }

}
