/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.lginteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.openhab.binding.lgtv.LgtvBindingProvider;
import org.openhab.binding.lgtv.internal.LgtvConnection;
import org.openhab.binding.lgtv.internal.LgtvEventListener;
import org.openhab.binding.lgtv.internal.LgtvStatusUpdateEvent;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.State;
import org.openhab.io.net.http.SecureHttpContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class forks the reader task to receive tv's messages
 *
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvMessageReader extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(LgTvMessageReader.class);
    private static List<LgtvEventListener> _listeners = new ArrayList<LgtvEventListener>();
    protected static ItemRegistry itemRegistry; // mf11102014
    public static final String WEBAPP_ALIAS = "/";
    public static final String SERVLET_NAME = "udap/api/event";
    private static final long serialVersionUID = -4716754591953777793L;

    private static int status = 0;

    protected BundleContext bundleContext = null;
    protected LgtvBindingProvider bindingprovider = null;
    protected ItemRegistry itemregistry = null;

    public synchronized void addEventListener(LgtvEventListener listener) {
        _listeners.add(listener);
    }

    LgTvMessageReader() {
        logger.debug("LgTvMessageReader initialized");
    }

    /**
     * Remove event listener.
     **/
    public synchronized void removeEventListener(LgtvEventListener listener) {
        _listeners.remove(listener);
    }

    public LgTvMessageReader(int portno) {
        logger.debug("LgTvMessageReader initialized");
    }

    // servlet extension
    protected HttpService httpService;

    public void setHttpService(HttpService httpService) {
        logger.debug("setHttpService called");
        this.httpService = httpService;
    }

    public void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

    /**
     * Creates a {@link SecureHttpContext} which handles the security for this
     * servlet
     * 
     * @return a {@link SecureHttpContext}
     */
    protected HttpContext createHttpContext() {
        if (httpService == null) {
            logger.warn("Cannot create HttpContext because the httpService is null");
            return null;
        } else {
            HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
            return new SecureHttpContext(defaultHttpContext, "openHAB.org");
        }
    }

    @Override
    public String getServletInfo() {
        return "Lgtv Binding Servlet";
    }

    public void activate() {
        logger.debug("LgTv servlet activate called");

        try {
            bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
            if (bundleContext != null) {
                ServiceReference<?> serviceReference1 = bundleContext
                        .getServiceReference(LgtvBindingProvider.class.getName());
                if (serviceReference1 != null) {
                    bindingprovider = (LgtvBindingProvider) bundleContext.getService(serviceReference1);
                } else {
                    logger.warn("Unable to get service reference from LgTv binding provider class");
                }

                ServiceReference<?> serviceReference2 = bundleContext.getServiceReference(ItemRegistry.class.getName());
                if (serviceReference2 != null) {
                    itemregistry = (ItemRegistry) bundleContext.getService(serviceReference2);
                } else {
                    logger.warn("Unable to get service reference from ItemRegistry class");
                }
            } else {
                logger.warn("bundleContext is null. Unable to get binding provider or item registry.");
            }

            Hashtable<String, String> props = new Hashtable<String, String>();
            httpService.registerServlet(WEBAPP_ALIAS + SERVLET_NAME, this, props, createHttpContext());
            logger.info("Started LgTv Servlet at {}{}", WEBAPP_ALIAS, SERVLET_NAME);
        } catch (NamespaceException e) {
            logger.warn("NamespaceException occurred during servlet startup", e);
        } catch (ServletException e) {
            logger.warn("ServletException occurred during servlet startup", e);
        } catch (Exception e) {
            logger.warn("Unexpected exception occurred during servlet startup", e);
        }
    }

    public void deactivate() {
        try {
            httpService.unregister(WEBAPP_ALIAS + SERVLET_NAME);
        } catch(IllegalArgumentException e) {
            logger.debug("LgTv Servlet '{}' was not registered. Nothing to deactivate.", WEBAPP_ALIAS + SERVLET_NAME);
        } finally {
            logger.info("Stopped LgTv Servlet");
        }
    }

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

    public void sendtohandlers(LgtvStatusUpdateEvent event, String remoteaddr, String message) {
        // send message to event listeners
        logger.debug("sendtohandlers remoteaddr={} message={}", remoteaddr, message);
        try {
            Iterator<LgtvEventListener> iterator = _listeners.iterator();

            while (iterator.hasNext()) {
                iterator.next().statusUpdateReceived(event, remoteaddr, message);
            }
        } catch (Exception e) {
            logger.warn("Cannot send to EventListeners. Maybe not initialized yet", e);
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String remoteaddr = req.getRemoteAddr();

        int start = remoteaddr.indexOf(":");
        String t;
        if (start > -1) {
            t = remoteaddr.substring(0, start);
        } else {
            t = remoteaddr;
        }
        remoteaddr = t;

        start = remoteaddr.indexOf("/");
        if (start > -1) {
            t = remoteaddr.substring(start + 1, remoteaddr.length());
        } else {
            t = remoteaddr;
        }
        remoteaddr = t;

        PrintWriter out = res.getWriter();
        res.setStatus(200);

        String devicename = req.getParameter("devicename");
        String value = req.getParameter("command");
        if (value != null && !value.equals("")) {
            res.setContentType("text/plain");
            if (value.equals("geturl")) {

                if (bindingprovider != null && itemregistry != null) {
                    for (String itemName : bindingprovider.getItemNames()) {
                        HashMap<String, String> values = bindingprovider.getDeviceCommands(itemName);

                        for (String cmd : values.keySet()) {

                            String[] commandParts = values.get(cmd).split(":");
                            String deviceCmd = commandParts[1];
                            String deviceId = commandParts[0];

                            if (deviceId.equals(devicename) && deviceCmd.equals("BROWSER_URL")) {
                                try {
                                    Item i = itemregistry.getItem(itemName);
                                    State state = i.getState();
                                    String va = state.toString();
                                    out.print(va);

                                } catch (ItemNotFoundException e) {
                                    logger.warn("Item not found");
                                }
                            }
                        }
                    }

                } else {
                    logger.warn("itemregistry or bindingprovider is null");
                }

            } else {
                out.println("command: " + value);
            }

        } else {
            String url = req.getRequestURL().toString();
            // String devicename=req.getParameter("devicename");

            res.setContentType("text/html");
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("<TITLE>LgTv Binding</TITLE>");
            out.println("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>");
            out.println("<script type=\"text/javascript\">");
            out.println("var serviceaddress;");
            out.println("var oldpage;");
            out.println("function CallRegular(){");
            out.println("$.ajax({ type:\'Get\', url: serviceaddress, success:function(data) ");
            out.println(" { ");
            out.println(
                    "   if (data!=\"Uninitialized\"&&data!=oldpage) {document.getElementById(\'content1\').src=data;oldpage=data;} ");
            out.println(" } })");
            out.println("}");
            out.println("function LoadPage(){");
            out.println(" serviceaddress=\'" + url + "?command=geturl&devicename=" + devicename + "\'");
            out.println(" CallRegular(); ");
            out.println(" setInterval(CallRegular,10000);");
            out.println("}");
            out.println(" </script>");
            out.println("</HEAD>");
            out.println("<frameset rows=\"100%,*\" onload=\"LoadPage();\">");
            out.println("<frame src=\"#\" id=\"content1\">");
            out.println("</frameset>");
            out.println("<BODY>");
            out.println("<BIG>Hello World</BIG>");
            out.println("</BODY></HTML>");
        }

        out.close();

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        BufferedReader rd = null;
        StringBuilder sb = null;

        res.setContentType("text/plain");
        res.setStatus(200);
        OutputStream responseBody = res.getOutputStream();

        LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);

        rd = new BufferedReader(new InputStreamReader(req.getInputStream()));
        sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line + '\n');
        }

        String remoteaddr = req.getRemoteAddr();

        int start = remoteaddr.indexOf(":");
        String t;
        if (start > -1) {
            t = remoteaddr.substring(0, start);
        } else {
            t = remoteaddr;
        }
        remoteaddr = t;

        start = remoteaddr.indexOf("/");
        if (start > -1) {
            t = remoteaddr.substring(start + 1, remoteaddr.length());
        } else {
            t = remoteaddr;
        }
        remoteaddr = t;

        logger.debug("httphandler called from remoteaddr={} result={}", remoteaddr, sb);

        LgTvEventChannelChanged myevent = new LgTvEventChannelChanged();

        String result = "";
        try {
            result = myevent.readevent(sb.toString());
        } catch (JAXBException e) {
            logger.warn("Error occurred in httphandler", e);
        }
        logger.debug("eventresult={}", result);

        LgTvEventChannelChanged.envelope envel = myevent.getenvel();

        String eventname = envel.getchannel().geteventname();

        if (eventname.equals("ChannelChanged")) {
            String name = "CHANNEL_CURRENTNAME=" + envel.getchannel().getchname();
            String number = "CHANNEL_CURRENTNUMBER=" + envel.getchannel().getmajor();
            String set = "CHANNEL_SET=" + envel.getchannel().getmajor();

            sendtohandlers(event, remoteaddr, name);
            sendtohandlers(event, remoteaddr, number);
            sendtohandlers(event, remoteaddr, set);
        } else if (eventname.equals("byebye")) {
            sendtohandlers(event, remoteaddr, "BYEBYE_SEEN=1");
        } else {
            logger.debug("unhandled event: {}", eventname);
        }

        responseBody.close();
    }
}
