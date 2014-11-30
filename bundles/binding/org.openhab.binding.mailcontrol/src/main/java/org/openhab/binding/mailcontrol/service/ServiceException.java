package org.openhab.binding.mailcontrol.service;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
    public ServiceException(Throwable ex) {
        super(ex);
    }
}
