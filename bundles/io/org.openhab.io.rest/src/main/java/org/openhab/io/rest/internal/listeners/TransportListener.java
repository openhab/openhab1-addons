package org.openhab.io.rest.internal.listeners;


import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.atmosphere.cpr.HeaderConfig;

public class TransportListener implements AtmosphereResourceEventListener {

    public TransportListener() {
    }


    public void onSuspend(final AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event){
    	String transport = event.getResource().getRequest().getHeader("X-Atmosphere-Transport");
    	String upgrade = event.getResource().getRequest().getHeader("Upgrade");
    	if((transport== null || transport.isEmpty()) && (upgrade==null || !upgrade.equalsIgnoreCase("websocket")) )
    		event.getResource().resume();
    	
    }

    public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        
    }

    public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        
    }

    public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        
    }

    public void onThrowable(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        event.throwable().printStackTrace(System.err);
    }
}