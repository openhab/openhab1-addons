/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * LGInteraction / Implementation of LG API
 * Author Martin Fluch martinfluch@gmx.net 
 */


package org.openhab.binding.lgtv.lginteraction;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.Servlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.model.core.ModelRepository;
import org.openhab.ui.items.ItemUIRegistry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

import org.openhab.binding.lgtv.internal.LgtvConnection;
import org.openhab.binding.lgtv.internal.LgtvEventListener;
import org.openhab.binding.lgtv.internal.LgtvStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * This class forks the reader task to receive tv's messages 
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvMessageReader extends HttpServlet {

	private static Logger logger = LoggerFactory
			.getLogger(LgtvConnection.class);
	private static List<LgtvEventListener> _listeners = new ArrayList<LgtvEventListener>();

	public static final String WEBAPP_ALIAS = "/";
	public static final String SERVLET_NAME = "udap/api/event"; 	
	private static final long serialVersionUID = -4716754591953777793L;

	private static int status = 0;

	public synchronized void addEventListener(LgtvEventListener listener) {
		_listeners.add(listener);
	}


	LgTvMessageReader()
	{
	}

	/**
	 * Remove event listener.
	 **/
	public synchronized void removeEventListener(LgtvEventListener listener) {
		_listeners.remove(listener);
	}

	public LgTvMessageReader(int portno) {
		//serverport = portno;
		logger.debug("LgTvMessageReader initialized");
	}

	//servlet extension
	protected HttpService httpService;
	protected ItemUIRegistry itemUIRegistry;
	protected ModelRepository modelRepository;


	
	protected void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}

	protected void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = null;
	}

	protected void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	protected void unsetModelRepository(ModelRepository modelRepository) {
		this.modelRepository = null;
	}

	public  void setHttpService(HttpService httpService) {
		logger.info("sethttpservice called"); 	
		this.httpService = httpService;
	}

	protected void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}


	/**
	 * Creates a {@link SecureHttpContext} which handles the security for this servlet  
	 * @return a {@link SecureHttpContext}
	 */
	protected HttpContext createHttpContext() {
		if (this.httpService==null)
		{
			logger.error("cannot create http context httpservice null");	
			return null;
		} else
		{	
			HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
			return new SecureHttpContext(defaultHttpContext, "openHAB.org");
		}
	}
	
	@Override
	public String getServletInfo() {
		return "Lgtv Binding Servlet";
	}


	protected void activate() {
		try {	
			logger.info("lgtv servlet activate called");		
			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(WEBAPP_ALIAS + SERVLET_NAME, this, props, createHttpContext());
			logger.info("Started LgTv Servlet at " + WEBAPP_ALIAS + SERVLET_NAME);
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during servlet startup", e);
		} catch (Exception e) {
			logger.error("error during servlet startup", e);
		}	
	}
	
	protected void deactivate() {
		httpService.unregister(WEBAPP_ALIAS + SERVLET_NAME);
		logger.info("Stopped LgTv Servlet ");
	}

	
	//
	public void startserver() throws IOException {

		if (status == 0) {
			activate();
			status = 1;
		} else {
			logger.debug("LgTvMessageReader server already started");
		}

	}

	public void stopserver() throws IOException {

		deactivate();
		logger.debug("LgTvMessageReader Server stopped");
		status = 0;
	}


	public void sendtohandlers(LgtvStatusUpdateEvent event, String remoteaddr,
			String message) {
		// send message to event listeners
		logger.debug("sendtohandlers remoteaddr=" + remoteaddr + " message="
				+ message);
		try {
			Iterator<LgtvEventListener> iterator = _listeners.iterator();

			while (iterator.hasNext()) {
				((LgtvEventListener) iterator.next()).statusUpdateReceived(
						event, remoteaddr, message);
			}

		} catch (Exception e) {
			logger.error(
					"Cannot send to EventListeners / maybe not initialized yet",
					e);
		}

	}


	@Override
        public void doGet(HttpServletRequest req, HttpServletResponse res)
                        throws ServletException, IOException {


                        //String requestMethod = exchange.getRequestMethod();
                        BufferedReader rd = null;
                        StringBuilder sb = null;

                        logger.debug("myhandler called");
                        if (1==1) {
                                //Headers responseHeaders = exchange.getResponseHeaders();
                                //responseHeaders.set("Content-Type", "text/plain");
                                res.setContentType("text/plain");
                                //exchange.sendResponseHeaders(200, 0);
                                res.setStatus(200);
                                OutputStream responseBody = res.getOutputStream();

                                //Headers requestHeaders = exchange.getRequestHeaders();

                                LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);

                                rd = new BufferedReader(new InputStreamReader(
                                                req.getInputStream()));
                                sb = new StringBuilder();
                                String line;
                                while ((line = rd.readLine()) != null) {
                                        sb.append(line + '\n');
                                }

                                String remoteaddr = req.getRemoteAddr();

                                int start = remoteaddr.indexOf(":");
                                String t;
                                if (start > -1)
                                        t = remoteaddr.substring(0, start);
                                else
                                        t = remoteaddr;
                                remoteaddr = t;

                                start = remoteaddr.indexOf("/");
                                if (start > -1)
                                        t = remoteaddr.substring(start + 1, remoteaddr.length());

				  else
                                        t = remoteaddr;
                                remoteaddr = t;

                                logger.debug("httphandler called from remoteaddr=" + remoteaddr
                                                + " result=" + sb.toString());

                                LgTvEventChannelChanged myevent = new LgTvEventChannelChanged();

                                String result = "";
                                try {
                                        result = myevent.readevent(sb.toString());
                                } catch (JAXBException e) {
                                        logger.error("error in httphandler",e);
                                }
                                logger.debug("eventresult=" + result);

                                LgTvEventChannelChanged.envelope envel = myevent.getenvel();

                                String eventname = envel.getchannel().geteventname();

                                if (eventname.equals("ChannelChanged")) {

                                        String name = "CHANNEL_CURRENTNAME="
                                                        + envel.getchannel().getchname();
                                        String number = "CHANNEL_CURRENTNUMBER="
                                                        + envel.getchannel().getmajor();
                                        String set = "CHANNEL_SET=" + envel.getchannel().getmajor();

                                        sendtohandlers(event, remoteaddr, name);
                                        sendtohandlers(event, remoteaddr, number);
                                        sendtohandlers(event, remoteaddr, set);

                                } else if (eventname.equals("byebye")) {

                                        sendtohandlers(event, remoteaddr, "BYEBYE_SEEN=1");

                                } else
                                        logger.debug("warning - unhandled event");

                        responseBody.close();
	}
 }



	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {


			//String requestMethod = exchange.getRequestMethod();
			BufferedReader rd = null;
			StringBuilder sb = null;

			logger.debug("myhandler called");
			if (1==1) {
				//Headers responseHeaders = exchange.getResponseHeaders();
				//responseHeaders.set("Content-Type", "text/plain");
				res.setContentType("text/plain");	
				//exchange.sendResponseHeaders(200, 0);
				res.setStatus(200);
				OutputStream responseBody = res.getOutputStream();
				
				//Headers requestHeaders = exchange.getRequestHeaders();

				LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);

				rd = new BufferedReader(new InputStreamReader(
						req.getInputStream()));
				sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line + '\n');
				}

				String remoteaddr = req.getRemoteAddr();

				int start = remoteaddr.indexOf(":");
				String t;
				if (start > -1)
					t = remoteaddr.substring(0, start);
				else
					t = remoteaddr;
				remoteaddr = t;

				start = remoteaddr.indexOf("/");
				if (start > -1)
					t = remoteaddr.substring(start + 1, remoteaddr.length());
				else
					t = remoteaddr;
				remoteaddr = t;

				logger.debug("httphandler called from remoteaddr=" + remoteaddr
						+ " result=" + sb.toString());

				LgTvEventChannelChanged myevent = new LgTvEventChannelChanged();

				String result = "";
				try {
					result = myevent.readevent(sb.toString());
				} catch (JAXBException e) {
					logger.error("error in httphandler",e);
				}
				logger.debug("eventresult=" + result);

				LgTvEventChannelChanged.envelope envel = myevent.getenvel();

				String eventname = envel.getchannel().geteventname();

				if (eventname.equals("ChannelChanged")) {

					String name = "CHANNEL_CURRENTNAME="
							+ envel.getchannel().getchname();
					String number = "CHANNEL_CURRENTNUMBER="
							+ envel.getchannel().getmajor();
					String set = "CHANNEL_SET=" + envel.getchannel().getmajor();

					sendtohandlers(event, remoteaddr, name);
					sendtohandlers(event, remoteaddr, number);
					sendtohandlers(event, remoteaddr, set);

				} else if (eventname.equals("byebye")) {

					sendtohandlers(event, remoteaddr, "BYEBYE_SEEN=1");

				} else
					logger.debug("warning - unhandled event");

			responseBody.close();
			}
		}

}
