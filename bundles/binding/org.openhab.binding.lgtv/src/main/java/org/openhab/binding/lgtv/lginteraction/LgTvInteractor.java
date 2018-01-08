/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.EventObject;

import javax.xml.bind.JAXBException;

import org.openhab.binding.lgtv.internal.LgtvConnection;
import org.openhab.binding.lgtv.internal.LgtvEventListener;
import org.openhab.binding.lgtv.internal.LgtvStatusUpdateEvent;
import org.openhab.binding.lgtv.lginteraction.LgTvAppSet.oneapp;
import org.openhab.binding.lgtv.lginteraction.LgTvChannelSet.onechannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the interaction between one tv and the plugin
 *
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvInteractor implements LgtvEventListener {

    // status of connection to tv
    public enum lgtvconnectionstatus {
        CS_NOTCONNECTED,
        CS_CONNECTED,
        CS_WAITFORPAIRKEY,
        CS_PAIRED,

    }

    /**
     * @param args
     */
    private static Logger logger = LoggerFactory.getLogger(LgtvConnection.class);
    private int lgport = 8080; // tv's communication port
    private String lgip; // tv's ip
    private int lglocalport; // local port for message server
    private String pairkey = ""; // pair key
    private String xmldatafiles = ""; // location to save xml files (application
                                      // list of tv, channel list of tv)
    private lgtvconnectionstatus connectionstatus = lgtvconnectionstatus.CS_NOTCONNECTED;
    private long lastsuccessfulinteraction = 0;

    // Lists for Applications &Channels
    private LgTvChannelSet channelset = new LgTvChannelSet();
    private LgTvAppSet appset = new LgTvAppSet();

    // the message reader server
    private LgTvMessageReader associatedreader = null;

    private int waitafterbyebye = 10; // wait 10secs for new interaction with tv
                                      // after byebye event
    private long byebyeseen = -1; // time when last byebye event occurred

    public String quickfind(String sourcestring, String tag) {
        String retval = "#notfound";
        String starttag = "<" + tag + ">";
        int startpt = sourcestring.indexOf(starttag);
        if (startpt >= 0) {
            int endpt = sourcestring.indexOf("</" + tag + ">");
            if (endpt >= 0) {
                retval = sourcestring.substring(startpt + starttag.length(), endpt);
            }
        }
        return retval;
    }

    // used to reach list of eventlisteners to promote connectionstatus
    public void associatereader(LgTvMessageReader m) {
        associatedreader = m;
    }

    public lgtvconnectionstatus getconnectionstatus() {
        return connectionstatus;
    }

    public void setconnectionstatus(lgtvconnectionstatus s) {
        if (s != connectionstatus) {
            logger.info("lgtv connectionstatus of ip=" + lgip + " changed from " + connectionstatus + " to " + s);
            connectionstatus = s;

            if (associatedreader != null) {
                String status = "CONNECTION_STATUS=" + ((s == lgtvconnectionstatus.CS_PAIRED) ? "1" : "0");
                LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);
                associatedreader.sendtohandlers(event, lgip, status);

                if (s == lgtvconnectionstatus.CS_NOTCONNECTED) {
                    associatedreader.sendtohandlers(event, lgip, status);

                    String name = "CHANNEL_CURRENTNAME=0";
                    String number = "CHANNEL_CURRENTNUMBER=0";
                    String vol = "VOLUME_CURRENT=0";
                    associatedreader.sendtohandlers(event, lgip, name);
                    associatedreader.sendtohandlers(event, lgip, number);
                    associatedreader.sendtohandlers(event, lgip, vol);
                }

            }

        }

    }

    public boolean ispaired() {
        return (connectionstatus == lgtvconnectionstatus.CS_PAIRED);
    }

    public LgTvInteractor(String ip, int port, int localport, String xmldf) throws InterruptedException {

        lgip = ip;
        lgport = port;
        lglocalport = localport;
        xmldatafiles = xmldf;

    }

    public void seensuccessfulinteraction() {
        lastsuccessfulinteraction = System.currentTimeMillis();
    }

    public long getlastsuccessfulinteraction() {
        return lastsuccessfulinteraction;
    }

    public String requestpairkey() {
        String message = "";
        message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"pairing\"><name>showKey</name></api></envelope>";
        String answer = sendtotv("POST", "udap/api/pairing", message);
        logger.debug("answer: " + answer);
        if (answer.startsWith("200")) {
            setconnectionstatus(lgtvconnectionstatus.CS_WAITFORPAIRKEY);
        } else {
            setconnectionstatus(lgtvconnectionstatus.CS_NOTCONNECTED);
        }
        return new String(answer);
    }

    public String getcurrentchannel() {
        String message = "";
        message = "";
        String answer = "#notpaired";
        if (ispaired()) {
            answer = sendtotv("GET", "udap/api/data?target=cur_channel", message);
        }
        return new String(answer);
    }

    public String getallchannels() {
        String message = "";
        message = "";
        String answer = "#notpaired";

        if (ispaired()) {
            answer = sendtotv("GET", "udap/api/data?target=channel_list", message);

            try {
                channelset.loadchannels(answer);
                int i = channelset.getsize();
                String s = String.valueOf(i);
                answer = s;
                if (this.xmldatafiles.length() > 0) {

                    String filename = this.xmldatafiles + lgip + "_lgtvallchannels.xml";
                    logger.debug("xmldatafiles is set - writing file=" + filename);
                    channelset.savetofile(filename);

                }

            } catch (JAXBException e) {

                logger.error("exception in getallchannels: ", e);
                org.xml.sax.SAXParseException f = (org.xml.sax.SAXParseException) e.getLinkedException();
                logger.error("parse exception e=" + e.toString() + " line=" + f.getLineNumber() + " columns="
                        + f.getColumnNumber() + " local=" + f.getLocalizedMessage());
            }
        }

        return new String(answer);
    }

    public String getallapps() {
        String message = "";
        message = "";
        String answer = "#notpaired";
        if (ispaired()) {
            answer = sendtotv("GET", "udap/api/data?target=applist_get&type=1&index=1&number=1024", message);

            logger.debug("answer: " + answer);
            try {
                appset.loadapps(answer);
                int i = appset.getsize();
                String s = String.valueOf(i);
                answer = s;

                if (this.xmldatafiles.length() > 0) {

                    String filename = this.xmldatafiles + lgip + "_lgtvallapps.xml";
                    logger.debug("xmldatafiles is set - writing file=" + filename);
                    appset.savetofile(filename);

                }

            } catch (JAXBException e) {

                logger.error("error in getallapps", e);
                org.xml.sax.SAXParseException f = (org.xml.sax.SAXParseException) e.getLinkedException();
                logger.error("parse exception e=" + e.toString() + " line=" + f.getLineNumber() + " columns="
                        + f.getColumnNumber() + " local=" + f.getLocalizedMessage());
            }

        }
        return new String(answer);
    }

    public String getvolumeinfo(int dontcheckpairing) {
        String message = "";
        message = "";
        String answer = "#notpaired";

        if (dontcheckpairing == 1 || ispaired()) {
            answer = sendtotv("GET", "udap/api/data?target=volume_info", message);
            logger.debug("answer: " + answer);
        }

        return new String(answer);
    }

    public String handlevolchangeeasy(String sval) {
        String res = "#notpaired";
        int val;

        int pos = sval.indexOf(".");
        if (pos > 0) {
            String ns = sval.substring(0, pos);
            val = Integer.parseInt(ns);
        } else {
            val = Integer.parseInt(sval);
        }

        if (ispaired()) {
            res = getvolumeinfo(0);
            String currentvol = quickfind(res, "level");
            int cvol = Integer.parseInt(currentvol);
            int todo = val - cvol;
            logger.debug("currentvolume=" + cvol + " newvolume=" + val + " todo=" + todo);

            LgTvCommand volup = LgTvCommand.valueOf("VOLUME_UP");
            LgTvCommand voldown = LgTvCommand.valueOf("VOLUME_DOWN");

            String usecommand = todo > 0 ? volup.getLgSendCommand() : voldown.getLgSendCommand();

            if (todo < 0) {
                todo = todo * -1;
            }
            for (int i = 0; i < todo; i++) {
                handlekeyinput(usecommand);
            }

            logger.debug("currentvolume=" + cvol + " newvolume=" + val + " todo=" + todo + " usecommand=" + usecommand);

            if (todo != 0) {
                res = getvolumeinfo(0);
                currentvol = quickfind(res, "level");

                if (associatedreader != null) {
                    String volume = "VOLUME_CURRENT=" + val;
                    LgtvStatusUpdateEvent event = new LgtvStatusUpdateEvent(this);
                    associatedreader.sendtohandlers(event, lgip, volume);
                }

            }
        }

        return new String(res);
    }

    public void setpairkey(String s) {
        pairkey = s;
    }

    public String getpairkey() {
        return pairkey;
    }

    public long getbyebyeseen() {
        return byebyeseen;
    }

    public void checkpairing() {
        int nobyebyeproblem = 1;

        long duration = System.currentTimeMillis() - byebyeseen;

        nobyebyeproblem = ((duration > (waitafterbyebye * 1000)) == true) ? 1 : 0;

        if (byebyeseen == -1 || nobyebyeproblem == 1) {
            if (connectionstatus != lgtvconnectionstatus.CS_PAIRED && pairkey.length() > 0) {
                sendpairkey();
            } else if (pairkey.length() == 0) {
                logger.error("no pairkey defined");
            }
        }

    }

    public String sendpairkey() {
        logger.debug("sending pairkey key=" + pairkey);
        String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"pairing\"><name>hello</name><value>"
                + pairkey + "</value><port>" + Integer.toString(lglocalport) + "</port></api></envelope>";
        String answer = sendtotv("POST", "udap/api/pairing", message);
        logger.debug("answer: " + answer);
        String success = "";
        if (answer.startsWith("200")) {
            success = "success";
            setconnectionstatus(lgtvconnectionstatus.CS_PAIRED);
        }

        logger.info(
                "sendpairkey with result=" + answer + " " + success + "connectionstatus=" + connectionstatus.name());
        return new String(answer);
    }

    public String handlekeyinput(String key) {
        String answer = "#notpaired";
        String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"command\"><name>HandleKeyInput</name><value>"
                + key + "</value></api></envelope>";

        if (ispaired()) {
            answer = sendtotv("POST", "udap/api/command", message);
            logger.debug("answer: " + answer);
        }
        return new String(answer);
    }

    public String appexecute(String appname, String id, String contentid) {
        String answer = "#notpaired";

        String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"command\"><name>AppExecute</name><auid>"
                + id + "</auid><appname>" + appname + "</appname><contentId>" + contentid
                + "</contentId></api></envelope>";
        if (ispaired()) {
            answer = sendtotv("POST", "udap/api/command", message);
            logger.debug("answer: " + answer);
        }
        return new String(answer);
    }

    public String appexecuteeasy(String name) {

        String cid = "";
        String id = "";

        if (name.length() == 0) {
            return new String("#appnotfound");
        }

        LgTvAppSet.envelope env = appset.getenvel();
        if (env == null) {
            logger.error("envelope=null maybe not connected");
            return new String("#appnotfound");
        } else {

            oneapp e = appset.getenvel().find(name);
            if (e != null) {

                cid = String.valueOf(e.getcpid());
                id = e.getid();

                return new String(appexecute(name, id, cid));
            } else {
                id = cid = name = "";
                return new String("#appnotfound");
            }
        }

    }

    public String appterminate(String appname, String id) {

        String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"command\"><name>AppTerminate</name><auid>"
                + id + "</auid><appname>" + appname + "</appname></api></envelope>";
        String answer = "#notpaired";
        if (ispaired()) {
            answer = sendtotv("POST", "udap/api/command", message);
            logger.debug("answer: " + answer);
        }
        return new String(answer);
    }

    public String appterminateeasy(String name) {
        String id = "";

        if (name.length() == 0) {
            return new String("#appnotfound");
        }

        oneapp e = appset.getenvel().find(name);
        if (e != null) {

            id = e.getid();

            return new String(appterminate(name, id));
        } else {
            id = name = "";
            return new String("#appnotfound");
        }

    }

    public String handlechannelchange(String major, String minor, String sourceindex, int phys) {
        String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?><envelope><api type=\"command\"><name>HandleChannelChange</name><major>"
                + major + "</major><minor>" + minor + "</minor><sourceIndex>" + sourceindex + "</sourceIndex>"
                + "<physicalNum>" + String.valueOf(phys) + "</physicalNum>" + "</api></envelope>";
        String answer = "#notpaired";
        if (ispaired()) {
            answer = sendtotv("POST", "udap/api/command", message);
            logger.debug("answer: " + answer);
        }
        return new String(answer);
    }

    public String handlechannelchangeeasy(String major) {
        String minor = "";
        String sourceindex = "";

        String c = major;
        // String chname="";
        // String chtype="";
        int phys = -1;
        String res = "#error";

        int pos = major.indexOf(".");
        if (pos > 0) {
            String ns = major.substring(0, pos);
            c = ns;
        }

        // logger.debug("hceasy: "+major+" -- "+c);
        LgTvChannelSet.envelope env = null;
        if (channelset == null) {
            logger.error("channelset=null");
        } else {
            env = channelset.getenvel();
        }

        if (env == null) {
            logger.error("envelope=null maybe not connected");
        } else {
            onechannel e = channelset.getenvel().find(Integer.parseInt(c));
            if (e != null) {
                minor = String.valueOf(e.getminor());
                phys = e.getphysicalnum();
                // chtype=e.getchtype();
                // chname=e.getchname();
                sourceindex = String.valueOf(e.getsourceindex());
                res = handlechannelchange(c, minor, sourceindex, phys);
            } else {
                minor = "";
                // chtype="";
            }
        }

        // channelset.getchannelinfo(Integer.parseInt(major), minor, chname,
        // chtype, sourceindex);
        return new String(res);
    }

    public String sendtotv(String typ, String command, String s) {
        HttpURLConnection connection = null;
        OutputStreamWriter wr = null;
        String conf_tv_url;
        Integer responsecode = -1;

        conf_tv_url = "http://" + lgip + ":" + Integer.toString(lgport); // 192.168.77.15:8080";

        logger.debug("sendtotv: url=" + conf_tv_url + " typ=" + typ + " command=" + command + " s=" + s);
        if (connectionstatus != lgtvconnectionstatus.CS_PAIRED) {
            logger.error("sendtotv but connection status is " + connectionstatus.name());
        }

        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
        String returnmessage = "#error";
        URL serverAddress = null;
        String serverAddressString = conf_tv_url + "/" + command;

        try {
            serverAddress = new URL(serverAddressString);

            // set up out communications stuff
            connection = null;

            // Set up the initial connection
            connection = (HttpURLConnection) serverAddress.openConnection();

            connection.setRequestProperty("content-type", "text/xml; charset=utf-8");
            connection.setRequestProperty("Host", lgip + ":" + Integer.toString(lgport)); // "192.168.77.15:8080");

            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("User-Agent", "Linux/2.6.18 UDAP/2.0 CentOS/5.8");
            connection.setUseCaches(false);
            connection.setReadTimeout(10000);

            if (typ.equals("POST")) {
                connection.setDoOutput(true);
            }
            connection.setDoInput(true);
            connection.setRequestMethod(typ);
            connection.connect();

            if (typ.equals("POST")) {
                wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(s);
                wr.flush();
            }

            // read the result from the server
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }

            logger.debug(sb.toString());
            returnmessage = sb.toString();
            responsecode = connection.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("MalformedUrlException at Connection to: " + serverAddressString);
            returnmessage = "#error/url";
        } catch (ProtocolException e) {
            e.printStackTrace();
            logger.error("Protocol Exception at Connection to: " + serverAddressString);
            returnmessage = "#error/protocol";
        } catch (IOException e) {
            logger.error("IO Exception at Connection to: " + serverAddressString);
            // logger.debug(e.toString());
            returnmessage = "#error/connect";
            setconnectionstatus(lgtvconnectionstatus.CS_NOTCONNECTED);
            // e.printStackTrace();
        }
        // close the connection, set all objects to null
        returnmessage += Integer.toString(responsecode);

        if (connectionstatus != lgtvconnectionstatus.CS_NOTCONNECTED) {
            seensuccessfulinteraction();
        }
        if (connection != null) {
            connection.disconnect();
        }
        rd = null;
        sb = null;
        wr = null;
        connection = null;
        return new String(returnmessage);
    }

    public void statusUpdateReceived(EventObject event, String ip, String data) {

        if (ip.equals(lgip)) {

            if (data.startsWith("BYEBYE_SEEN") == true) {
                byebyeseen = System.currentTimeMillis(); // as tv keeps alive
                                                         // for ~10secs we
                                                         // have to prevent
                                                         // any further
                                                         // communication
                setconnectionstatus(lgtvconnectionstatus.CS_NOTCONNECTED);
            }

        }

    }

}
